package com.nlweb.api;

import com.nlweb.amho.service.AmhoQueryService;
import com.nlweb.auth.dto.request.*;
import com.nlweb.restdocs.RestDocumentation;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest extends RestDocumentation {

    private String accessToken;
    private String refreshToken;

    @Autowired
    private AmhoQueryService amhoQueryService;

    @Test
    @Order(1)
    void register() throws Exception {
        String userCode = amhoQueryService.getActiveAmho().getUserCode();

        RegisterRequest registerRequest = new RegisterRequest(
                userCode,
                "test",
                "0000",
                "26010001",
                "늘혬코러스",
                "test@nlweb.com",
                "010-0000-0000",
                1,
                "VOCAL"
        );

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("auth/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

    }

    @Test
    @Order(2)
    void login() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test", "0000");

        String responseContent = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth/login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var responseNode = objectMapper.readTree(responseContent);
        var tokenNode = responseNode.path("data").path("token");
        this.accessToken = tokenNode.path("accessToken").asText();
        this.refreshToken = tokenNode.path("refreshToken").asText();
    }

    @Test
    @Order(3)
    void refreshToken() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken);

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth/refresh-token",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @Order(4)
    void logout() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth/logout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}
