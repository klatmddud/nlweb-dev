package com.nlweb.program.dto.response;

import com.nlweb.program.entity.Program;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProgramResponse(
        UUID id,
        String title,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime programApplyStartAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime programApplyEndAt,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime ensembleApplyStartAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime ensembleApplyEndAt,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime sessionApplyStartAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime sessionApplyEndAt,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime timeslotApplyStartAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime timeslotApplyEndAt
) {

    /** 조회용 (생성, 수정 포함) */
    public static ProgramResponse forPublic(Program program) {
        return new ProgramResponse(
                program.getId(),
                program.getTitle(),
                program.getProgramApplyStartAt(),
                program.getProgramApplyEndAt(),
                program.getEnsembleApplyStartAt(),
                program.getEnsembleApplyEndAt(),
                program.getSessionApplyStartAt(),
                program.getSessionApplyEndAt(),
                program.getTimeslotApplyStartAt(),
                program.getTimeslotApplyEndAt()
        );
    }

}
