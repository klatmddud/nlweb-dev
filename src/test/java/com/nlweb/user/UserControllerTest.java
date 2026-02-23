package com.nlweb.user;

import com.nlweb.core.security.NlwebUserDetails;
import com.nlweb.restdocs.RestDocumentation;
import com.nlweb.user.dto.request.UpdateUserRequest;
import com.nlweb.user.dto.response.UserResponse;
import com.nlweb.user.entity.User;
import com.nlweb.user.enums.UserSessionType;
import com.nlweb.user.facade.UserFacade;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends RestDocumentation {

    private UserFacade userFacade;

    @Test
    void getMyProfile() throws Exception {
        UserResponse response = new UserResponse(
                UUID.randomUUID(),
                "userTester",
                "유저테스터",
                "user@test.com",
                26,
                "GUITAR",
                false,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        doReturn(response).when(userFacade).getProfile(any(), anyString());

        mockMvc.perform(get("/api/users/me")
                        .with(authentication(userAuthentication(false))))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("users/get-my-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void updateMyProfile() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("유저변경", "updated@test.com", 27, UserSessionType.DRUM);

        UserResponse response = new UserResponse(
                UUID.randomUUID(),
                "userTester",
                "유저변경",
                "updated@test.com",
                27,
                "DRUM",
                false,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        doReturn(response).when(userFacade).updateProfile(any(), anyString(), any(UpdateUserRequest.class));

        mockMvc.perform(patch("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(userAuthentication(false))))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("users/update-my-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void softDeleteMyProfile() throws Exception {
        UserResponse response = new UserResponse(
                UUID.randomUUID(),
                "userTester",
                "유저테스터",
                "user@test.com",
                26,
                "GUITAR",
                false,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        doReturn(response).when(userFacade).softDeleteUser(any(), anyString());

        mockMvc.perform(delete("/api/users/me")
                        .with(authentication(userAuthentication(false))))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("users/delete-my-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void searchUsers() throws Exception {
        UserResponse user1 = new UserResponse(null, "userA", "유저A", "a@test.com", 26, "GUITAR", false, false, null, null);
        UserResponse user2 = new UserResponse(null, "userB", "유저B", "b@test.com", 26, "GUITAR", false, false, null, null);

        doReturn(List.of(user1, user2)).when(userFacade)
                .searchUsers(any(UserSessionType.class), anyInt(), any(), anyString());

        mockMvc.perform(get("/api/users")
                        .param("session", "GUITAR")
                        .param("batch", "26")
                        .with(authentication(userAuthentication(false))))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("users/search-users",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    private Authentication userAuthentication(boolean isAdmin) {
        User user = User.builder()
                .username("userTester")
                .password("password")
                .fullName("유저테스터")
                .email("user@test.com")
                .batch(26)
                .session(UserSessionType.GUITAR)
                .isVocalAllowed(false)
                .isAdmin(isAdmin)
                .build();

        ReflectionTestUtils.setField(user, "id", UUID.randomUUID());

        NlwebUserDetails details = new NlwebUserDetails(user);
        return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
    }
}
