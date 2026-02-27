package com.nlweb.auth.facade;

import com.nlweb.core.security.NlwebUserDetails;
import com.nlweb.auth.dto.request.LoginRequest;
import com.nlweb.auth.dto.request.RefreshTokenRequest;
import com.nlweb.auth.dto.request.RegisterRequest;
import com.nlweb.auth.dto.response.AuthResponse;
import com.nlweb.auth.service.*;
import com.nlweb.auth.dto.object.*;
import com.nlweb.amho.exception.InvalidAmhoException;
import com.nlweb.amho.service.*;
import com.nlweb.amho.entity.Amho;
import com.nlweb.user.service.*;
import com.nlweb.user.entity.User;
import com.nlweb.user.dto.request.CreateUserRequest;
import com.nlweb.auth.log.AuthLogger;
import com.nlweb.common.log.SecurityLogger;
import com.nlweb.common.util.IpUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthFacade {

    private final TokenService tokenService;
    private final PasswordService passwordService;
    private final CacheService cacheService;
    private final AmhoQueryService amhoQueryService;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
        try {
            Amho amho = amhoQueryService.getActiveAmho();

            if (!request.amhoCode().equals(amho.getUserCode())) {
                throw new InvalidAmhoException("유효하지 않은 가입 코드입니다.");
            }

            User user = userCommandService.create(CreateUserRequest.from(request));

            AuthLogger.logSuccessEvent("user_registration", user.getId(), user.getUsername(), null);
            
            return AuthResponse.forRegister(user);
            
        } catch (Exception e) {
            String ipAddress = IpUtils.getClientIp(httpRequest);
            
            SecurityLogger.logRegistrationFailure(
                    request.username(),
                    request.email(),
                    ipAddress != null ? ipAddress : "unknown",
                    e.getClass().getSimpleName() + ": " + e.getMessage());

            AuthLogger.logFailureEvent("user_registration", null, request.username(), e, null);
            throw e;
        }
    }

    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        try {
            // identifier(username, email)로 사용자 조회
            User user = userQueryService.getByIdentifier(request.identifier());

            // 비밀번호 검증
            passwordService.validateUserPassword(user.getPassword(), request.password());

            // 엑세스, 리프레시 토큰 생성
            AccessTokenObject accessTokenObject = tokenService.generateAccessToken(user.getUsername());
            RefreshTokenObject refreshTokenObject = tokenService.generateRefreshToken(user.getUsername());

            // 캐시에 리프레시 토큰 저장
            cacheService.saveRefreshToken(user.getUsername(), refreshTokenObject.getRefreshToken());

            AuthLogger.logSuccessEvent("user_login", user.getId(), user.getUsername(), null);

            return AuthResponse.forLogin(accessTokenObject, refreshTokenObject, user);
            
        } catch (Exception e) {
            String ipAddress = IpUtils.getClientIp(httpRequest);
            String userAgent = httpRequest.getHeader("User-Agent");
            
            SecurityLogger.logLoginFailure(
                    request.identifier(),
                    ipAddress != null ? ipAddress : "unknown", 
                    userAgent != null ? userAgent : "unknown", 
                    e.getClass().getSimpleName() + ": " + e.getMessage());

            AuthLogger.logFailureEvent("user_login", null, request.identifier(), e, null);
            throw e;
        }
    }

    @Transactional
    public void logout(NlwebUserDetails principal, HttpServletRequest request) {
        String username = null;
        try {
            // http 요청에서 엑세스 토큰
            AccessTokenObject accessTokenObject = tokenService.getTokenFromHttpRequest(request);
            username = tokenService.getUsernameFromToken(accessTokenObject.getAccessToken());
            
            // Logout attempt will be logged on success/failure
            
            // 액세스 토큰을 블랙리스트에 추가
            cacheService.blacklistToken(accessTokenObject.getAccessToken(), accessTokenObject.getAccessExpiresIn());
            
            // 캐시에서 리프레시 토큰 삭제
            cacheService.deleteRefreshToken(username);

            AuthLogger.logSuccessEvent("user_logout", principal.getUserId(), username, null);
            
        } catch (Exception e) {
            AuthLogger.logFailureEvent("user_logout", principal.getUserId(), principal.getUsername(), e, null);
            throw e;
        }
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String username = null;
        try {
            // 리프레시 토큰에서 사용자명 추출 및 검증
            username = tokenService.getUsernameFromToken(request.refreshToken());
            
            // Token refresh attempt will be logged on success/failure
            
            // 캐시에 저장된 리프레시 토큰과 일치하는지 확인
            cacheService.validateRefreshToken(username, request.refreshToken());
            
            // 새로운 액세스 토큰 생성
            AccessTokenObject newAccessToken = tokenService.generateAccessToken(username);
            
            // 새로운 리프레시 토큰 생성
            RefreshTokenObject newRefreshToken = tokenService.generateRefreshToken(username);
            
            // 기존 리프레시 토큰 삭제 후 새 리프레시 토큰 저장
            cacheService.deleteRefreshToken(username);
            cacheService.saveRefreshToken(username, newRefreshToken.getRefreshToken());
            
            User user = userQueryService.getByUsername(username);

            AuthLogger.logSuccessEvent("refresh_token", user.getId(), username, null);
            
            return AuthResponse.forRefresh(newAccessToken, newRefreshToken, user);
            
        } catch (Exception e) {
            AuthLogger.logFailureEvent("token_refresh", null, username != null ? username : "unknown", e, null);
            throw e;
        }
    }




}
