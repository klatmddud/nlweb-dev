package com.nlweb.program.dto.response;

import com.nlweb.program.entity.ProgramUser;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProgramUserResponse(
        UUID id,
        UUID programId,
        String programTitle,
        UUID userId,
        String username,
        String fullName,
        String session
) {

    /** 조회용 (생성 포함) */
    public static ProgramUserResponse forPublic(ProgramUser programUser) {
        return new ProgramUserResponse(
                programUser.getId(),
                programUser.getProgram().getId(),
                programUser.getProgram().getTitle(),
                programUser.getUser().getId(),
                programUser.getUser().getUsername(),
                programUser.getUser().getFullName(),
                programUser.getUser().getSession().toString()
        );
    }

}
