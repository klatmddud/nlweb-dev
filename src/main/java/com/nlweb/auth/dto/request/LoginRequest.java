package com.nlweb.auth.dto.request;

import jakarta.validation.constraints.*;

public record LoginRequest(
    @NotBlank(message = "아이디 또는 이메일은 필수입니다.")
    @Size(min = 4, max = 100, message = "아이디 또는 이메일은 4자 이상 100자 이하여야 합니다.")
    String identifier,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 100, message = "비밀번호는 4자 이상 100자 이하여야 합니다.")
    String password
) {}
