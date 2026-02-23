package com.nlweb.auth.dto.response;

import com.nlweb.auth.dto.object.AccessTokenObject;
import com.nlweb.auth.dto.object.RefreshTokenObject;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenResponse(
        String accessToken,
        String refreshToken,
        Long accessExpiresIn,
        Long refreshExpiresIn,
        String tokenType
) {

    public static TokenResponse from(AccessTokenObject accessToken, RefreshTokenObject refreshToken) {
        return new TokenResponse(
                accessToken.getAccessToken(),
                refreshToken.getRefreshToken(),
                accessToken.getAccessExpiresIn(),
                refreshToken.getRefreshExpiresIn(),
                "Bearer"
        );
    }
}