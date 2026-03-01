package com.nlweb.api;

import com.nlweb.restdocs.RestDocumentation;
import com.nlweb.auth.dto.request.LoginRequest;
import com.nlweb.admin.dto.request.*;
import com.nlweb.amho.service.AmhoQueryService;
import com.nlweb.program.dto.request.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.UUID;
import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProgramControllerTest extends RestDocumentation {

    @Autowired
    private AmhoQueryService amhoQueryService;

    private String accessToken;
    private UUID programId;

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
    void getAdminAuthority() throws Exception {
        String adminCode = amhoQueryService.getActiveAmho().getAdminCode();

        CreateAdminRequest request = new CreateAdminRequest(adminCode);

        mockMvc.perform(post("/api/admins")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(3)
    void createNewProgram() throws Exception {
        String responseContent = mockMvc.perform(post("/api/programs")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test"))
                        .andExpect(status().isCreated())
                        .andDo(print())
                        .andDo(document("programs/create-new-program",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();


        var responseNode = objectMapper.readTree(responseContent);
        var dataNode = responseNode.path("data");
        this.programId = UUID.fromString(dataNode.path("id").asText());
    }

    @Test
    @Order(4)
    void updateProgram() throws Exception {
        UpdateProgramRequest request = new UpdateProgramRequest(
                programId,
                "2026년 1학기 합주스터디",
                LocalDateTime.of(2026, 3, 1, 18, 0, 0),
                LocalDateTime.of(2026, 3, 7, 0, 0, 0),
                LocalDateTime.of(2026, 4, 1, 18, 0, 0),
                LocalDateTime.of(2026, 4, 10, 18, 0, 0),
                LocalDateTime.of(2026, 4, 13, 18, 0, 0),
                LocalDateTime.of(2026, 4, 16, 20, 0, 0),
                LocalDateTime.of(2026, 4, 20, 18, 0, 0),
                LocalDateTime.of(2026, 4, 25, 23, 59, 59)
        );

        mockMvc.perform(patch("/api/programs")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.USER_AGENT, "MockMvc-Test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("programs/update-program",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @Order(5)
    void applyProgram() throws Exception {
        CreateProgramUserRequest request = new CreateProgramUserRequest(programId);

        mockMvc.perform(post("/api/programs/apply")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("programs/apply-program",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @Order(6)
    void cancelProgramApplication() throws Exception {
        DeleteProgramUserRequest request = new DeleteProgramUserRequest(programId);

        mockMvc.perform(delete("/api/programs/apply")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("programs/cancel-program-application",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @Order(7)
    void revokeAdminAuthority() throws Exception {
        mockMvc.perform(delete("/api/admins/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test"))
                .andExpect(status().isOk());
    }
}
