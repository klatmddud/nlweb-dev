package com.nlweb.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMyRoleRequest(
        @NotBlank(message = "역할명은 필수입니다.")
        @Size(max = 100, message = "역할명은 100자 이하여야 합니다.")
        String role
) {
}
