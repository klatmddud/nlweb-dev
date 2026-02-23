package com.nlweb.auth.controller;

import com.nlweb.core.security.NlwebUserDetails;
import com.nlweb.auth.dto.request.LoginRequest;
import com.nlweb.auth.dto.request.RefreshTokenRequest;
import com.nlweb.auth.dto.request.RegisterRequest;
import com.nlweb.auth.dto.response.AuthResponse;
import com.nlweb.auth.facade.AuthFacade;
import com.nlweb.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    /**
     * POST
     * /api/auth/register
     * 회원가입 */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @RequestBody RegisterRequest registerRequest,
            HttpServletRequest httpRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(authFacade.register(registerRequest, httpRequest)));
    }

    /**
     * POST
     * /api/auth/login
     * 로그인 */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest httpRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(authFacade.login(loginRequest, httpRequest)));
    }

    /**
     * POST
     * /api/auth/logout
     * 로그아웃 */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal NlwebUserDetails principal,
            HttpServletRequest httpRequest
    ) {
        authFacade.logout(principal, httpRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * POST
     * /api/auth/refresh
     * 토큰 재발급 */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(authFacade.refreshToken(refreshTokenRequest)));
    }

}
