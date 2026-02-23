package com.nlweb.auth.dto.response;

import com.nlweb.user.dto.response.UserResponse;
import com.nlweb.user.entity.User;
import com.nlweb.auth.dto.object.AccessTokenObject;
import com.nlweb.auth.dto.object.RefreshTokenObject;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
    TokenResponse token,
    UserResponse user,
    String message
) {
    
    /** 로그인 응답용 */
    public static AuthResponse forLogin(AccessTokenObject accessToken, RefreshTokenObject refreshToken, User user) {
        return new AuthResponse(
            TokenResponse.from(accessToken, refreshToken),
            UserResponse.forLogin(user),
            null
        );
    }
    
    /** 토큰 갱신 응답용 */
    public static AuthResponse forRefresh(AccessTokenObject accessToken, RefreshTokenObject refreshToken, User user) {
        return new AuthResponse(
            TokenResponse.from(accessToken, refreshToken),
            UserResponse.forLogin(user),
            "토큰이 성공적으로 갱신되었습니다."
        );
    }
    
    /** 회원가입 응답용 */
    public static AuthResponse forRegister(User user) {
        return new AuthResponse(
            null, // 회원가입 시에는 토큰 없음
            UserResponse.forRegistration(user),
            "회원가입이 완료되었습니다."
        );
    }
}