package com.nlweb.auth.service;

import com.nlweb.auth.dto.object.*;
import com.nlweb.common.util.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtils jwtUtils;

    /** 토큰 검증 */
    public void validateToken(String token) {
        if (!jwtUtils.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다");
        }
    }

    /** http 요청 헤더에서 token 추출 */
    public AccessTokenObject getTokenFromHttpRequest(HttpServletRequest request) {
        String token = jwtUtils.resolveToken(request);

        if (token == null) {
            throw new IllegalArgumentException("Access Token이 필요합니다.");
        }

        return AccessTokenObject.builder()
                .accessToken(token)
                .accessExpiresIn(jwtUtils.getRemainingTime(token))
                .build();
    }

    /** 엑세스 토큰 생성 */
    public AccessTokenObject generateAccessToken(String username) {
        return AccessTokenObject.builder()
                .accessToken(jwtUtils.generateAccessToken(username))
                .accessExpiresIn(jwtUtils.getAccessTokenValidity())
                .build();
    }

    /** 리프레시 토큰 생성 */
    public RefreshTokenObject generateRefreshToken(String username) {
        return RefreshTokenObject.builder()
                .refreshToken(jwtUtils.generateRefreshToken(username))
                .refreshExpiresIn(jwtUtils.getRefreshTokenValidity())
                .build();
    }

    /** 토큰에서 username 추출 */
    public String getUsernameFromToken(String token) {
        if (!jwtUtils.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        return jwtUtils.getUsername(token);
    }

}
