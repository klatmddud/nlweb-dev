package com.nlweb.user.dto.request;

import com.nlweb.user.enums.UserSessionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateUserRequest(
        @Size(max = 100, message = "이름은 100자 이하여야 합니다.")
        String fullName,

        @Email(message = "올바른 이메일 형식이어야 합니다.")
        String email,

        @Min(value = 0, message = "기수는 0 이상이어야 합니다.")
        @Max(value = 100, message = "기수는 100 이하여야 합니다.")
        Integer batch,

        UserSessionType session
) {}
