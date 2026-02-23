package com.nlweb.user.dto.request;

import com.nlweb.auth.dto.request.RegisterRequest;
import com.nlweb.user.enums.UserSessionType;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateUserRequest(
    String username,
    String password,
    String fullName,
    String email,
    Integer batch,
    UserSessionType session
) {
    
    public static CreateUserRequest from(RegisterRequest registerRequest) {
        return new CreateUserRequest(
            registerRequest.username(),
            registerRequest.password(),
            registerRequest.fullName(),
            registerRequest.email(),
            registerRequest.batch(),
            UserSessionType.valueOf(registerRequest.session())
        );
    }
}
