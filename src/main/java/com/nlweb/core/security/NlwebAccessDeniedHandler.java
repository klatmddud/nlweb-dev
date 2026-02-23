package com.nlweb.core.security;

import com.nlweb.common.dto.ApiResponse;
import com.nlweb.common.util.IpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class NlwebAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String ipAddress = IpUtils.getClientIp(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "anonymous";

        log.warn("권한 없는 접근 시도: {} {} by {} - IP: {}",
                method, requestURI, username, ipAddress);

        // 응답 설정
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 에러 응답 생성
        ApiResponse<?> errorResponse = ApiResponse.error(
                "ACCESS_DENIED",
                "이 리소스에 접근할 권한이 없습니다."
        );

        // 요청 경로 정보 추가
        errorResponse.setPath(requestURI);

        // JSON 응답 작성
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

}
