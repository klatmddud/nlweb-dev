package com.nlweb.amho;

import com.nlweb.restdocs.RestDocumentation;
import org.junit.jupiter.api.Test;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class AmhoControllerTest extends RestDocumentation {

    @Test
    void getCurrentAmho() throws Exception {
        mockMvc.perform(get("/api/amhos"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("amhos/get-current-amho",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void resetCurrentAmho() throws Exception {
        mockMvc.perform(post("/api/amhos"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("amhos/reset-current-amho",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

}