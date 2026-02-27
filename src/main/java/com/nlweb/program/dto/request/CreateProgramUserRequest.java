package com.nlweb.program.dto.request;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record CreateProgramUserRequest(

        @NotBlank(message = "프로그램 ID는 필수입니다.")
        UUID programId

) { }
