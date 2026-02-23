package com.nlweb.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank(message = "JWT secret key는 필수입니다")
    private String secret;

    @Min(value = 60000, message = "Access token 유효시간은 최소 1분이어야 합니다")
    private long accessTokenValidity = 900000L;  // 15분

    @Min(value = 3600000, message = "Refresh token 유효시간은 최소 1시간이어야 합니다")
    private long refreshTokenValidity = 604800000L;  // 7일

    @NotNull
    private String issuer = "nlweb";

    @NotNull
    private String headerName = "Authorization";

    @NotNull
    private String tokenPrefix = "Bearer ";

    @Min(value = 30000, message = "토큰 갱신 임계시간은 최소 30초여야 합니다")
    private long refreshThreshold = 300000L;  // 5분

    public String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(tokenPrefix)) {
            return bearerToken.substring(tokenPrefix.length());
        }
        return null;
    }

}
