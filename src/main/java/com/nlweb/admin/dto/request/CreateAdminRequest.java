package com.nlweb.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAdminRequest(

        @NotBlank(message = "암호 코드는 필수입니다.")
        String amhoCode

) { }
