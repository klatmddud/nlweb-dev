package com.nlweb.auth.dto.request;

import jakarta.validation.constraints.*;

public record RegisterRequest(
    @NotBlank(message = "가입 코드는 필수입니다.")
    @Pattern(regexp = "\\d{8}", message = "가입 코드는 8자리 숫자여야 합니다.")
    String amhoCode,

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 4, max = 100, message = "아이디는 4자 이상 100자 이하여야 합니다.")
    String username,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 100, message = "비밀번호는 4자 이상 100자 이하여야 합니다.")
    String password,

    @Pattern(regexp = "^$|\\d{8}", message = "학번은 8자리 숫자여야 합니다.")
    String studentId,

    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 100, message = "이름은 100자 이하여야 합니다.")
    String fullName,

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String email,

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "010-\\d{4}-\\d{4}", message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
    String phone,

    @NotNull(message = "기수는 필수입니다.")
    @Min(value = 1, message = "기수는 1 이상이어야 합니다.")
    @Max(value = 100, message = "기수는 100 이하여야 합니다.")
    Integer batch,

    @NotBlank(message = "세션은 필수입니다.")
    @Pattern(regexp = "^(VOCAL|GUITAR|BASS|DRUM|KEYBOARD)$", 
             message = "유효한 세션을 선택해야 합니다. (VOCAL, GUITAR, BASS, DRUM, KEYBOARD 중 하나)")
    String session
) {}
