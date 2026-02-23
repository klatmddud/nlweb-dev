package com.nlweb.program.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateProgramRequest(
        @Size(max = 255, message = "제목은 255자 이하여야 합니다.")
        String title,

        LocalDateTime programApplyStartAt,
        LocalDateTime programApplyEndAt,

        LocalDateTime ensembleApplyStartAt,
        LocalDateTime ensembleApplyEndAt,

        LocalDateTime sessionApplyStartAt,
        LocalDateTime sessionApplyEndAt,

        LocalDateTime timeslotApplyStartAt,
        LocalDateTime timeslotApplyEndAt
) { }
