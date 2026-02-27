package com.nlweb.api;

import com.nlweb.restdocs.RestDocumentation;
import com.nlweb.auth.dto.request.LoginRequest;
import com.nlweb.user.facade.UserFacade;
import com.nlweb.user.dto.request.UpdateUserRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest extends RestDocumentation {

    private String accessToken;

    @Test
    @Order(1)
    void login() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test", "0000");

        String responseContent = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var responseNode = objectMapper.readTree(responseContent);
        var tokenNode = responseNode.path("data").path("token");
        this.accessToken = tokenNode.path("accessToken").asText();
    }

    @Test
    @Order(2)
    void getMyprofile() throws Exception {
        mockMvc.perform(get("/api/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.USER_AGENT, "MockMvc-Test"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("users/get-my-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}
