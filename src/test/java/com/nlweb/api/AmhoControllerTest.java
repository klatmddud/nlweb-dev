package com.nlweb.api;

import com.nlweb.core.security.NlwebUserDetails;
import com.nlweb.restdocs.RestDocumentation;
import com.nlweb.user.entity.User;
import com.nlweb.user.enums.UserSessionType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;


class AmhoControllerTest extends RestDocumentation {

    @Test
    void getCurrentAmho() throws Exception {
        mockMvc.perform(get("/api/amhos")
                        .with(authentication(userAuthentication(true))))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("amhos/get-current-amho",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void resetCurrentAmho() throws Exception {
        mockMvc.perform(post("/api/amhos")
                        .with(authentication(userAuthentication(true))))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("amhos/reset-current-amho",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    private Authentication userAuthentication(boolean isAdmin) {
        User user = User.builder()
                .username("admin")
                .password("0000")
                .fullName("관리자")
                .email("admin@nlweb.com")
                .batch(1)
                .session(UserSessionType.GUITAR)
                .isVocalAllowed(false)
                .isAdmin(isAdmin)
                .build();

        ReflectionTestUtils.setField(user, "id", UUID.randomUUID());

        NlwebUserDetails details = new NlwebUserDetails(user);
        return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
    }

}
