package com.nlweb.api;

import com.nlweb.restdocs.RestDocumentation;
import com.nlweb.auth.dto.request.LoginRequest;
import com.nlweb.admin.dto.request.*;
import com.nlweb.amho.service.AmhoQueryService;
import com.nlweb.program.dto.request.*;
import com.nlweb.ensemble.dto.request.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EnsembleControllerTest extends RestDocumentation {

    @Autowired
    private AmhoQueryService amhoQueryService;

    private String accessToken;
    private UUID programId;
    private UUID ensembleId;

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
                LocalDateTime.of(2026, 2, 1, 18, 0, 0),
                LocalDateTime.of(2026, 3, 7, 0, 0, 0),
                LocalDateTime.of(2026, 2, 1, 18, 0, 0),
                LocalDateTime.of(2026, 4, 10, 18, 0, 0),
                LocalDateTime.of(2026, 2, 13, 18, 0, 0),
                LocalDateTime.of(2026, 4, 16, 20, 0, 0),
                LocalDateTime.of(2026, 2, 20, 18, 0, 0),
                LocalDateTime.of(2026, 4, 25, 23, 59, 59)
        );

        mockMvc.perform(patch("/api/programs")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
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
    void applyEnsemble() throws Exception {
        CreateEnsembleRequest request = new CreateEnsembleRequest(
                programId,
                "늘혬코러스",
                "늘혬가",
                null,
                "36기 김기타",
                "X",
                "33기 최베이스",
                "35기 이드럼",
                "박피아노",
                "X",
                "X"
        );

        String responseContent = mockMvc.perform(post("/api/ensembles")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andDo(print())
                        .andDo(document("ensembles/create-ensemble",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        var responseNode = objectMapper.readTree(responseContent);
        var dataNode = responseNode.path("data");
        this.ensembleId = UUID.fromString(dataNode.path("id").asText());
    }

    @Test
    @Order(7)
    void applyEnsemble2() throws Exception {
        CreateEnsembleRequest request = new CreateEnsembleRequest(
                programId,
                "늘혬코러스",
                "늘혬가 2",
                "34기 강보컬",
                "31기 김기타",
                "37기 최기타",
                "30기 최베이스",
                "33기 이드럼",
                "38기 박피아노",
                "X",
                "X"
        );

        mockMvc.perform(post("/api/ensembles")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(8)
    void updateEnsemble() throws Exception {
        UpdateEnsembleRequest request = new UpdateEnsembleRequest(
                ensembleId,
                null,
                "늘혬가_수정",
                null,
                null,
                "김리듬기타",
                null,
                null,
                null,
                null,
                null
        );

        mockMvc.perform(patch("/api/ensembles")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andDo(document("ensembles/update-ensemble",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())));
    }

    @Test
    @Order(9)
    void applySession() throws Exception {
        ApplySessionRequest request = new ApplySessionRequest(ensembleId, "vocal");

        mockMvc.perform(patch("/api/ensembles/session")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("ensembles/apply-session",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @Order(10)
    void getEnsembles() throws Exception {
        GetAllEnsembleRequest request = new GetAllEnsembleRequest(programId);

        mockMvc.perform(get("/api/ensembles")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("ensembles/get-all-ensembles-in-program",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @Order(11)
    void revokeAdminAuthority() throws Exception {
        mockMvc.perform(delete("/api/admins/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.USER_AGENT, "MockMvc-Test"))
                .andExpect(status().isOk());
    }

}
