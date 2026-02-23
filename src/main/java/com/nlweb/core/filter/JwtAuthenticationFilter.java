package com.nlweb.core.filter;

import com.nlweb.common.util.IpUtils;
import com.nlweb.common.util.JwtUtils;
import com.nlweb.common.log.SecurityLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CacheManager cacheManager;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String BLACKLIST_CACHE = "jwt-blacklist";

    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        String ipAddress = IpUtils.getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                String tokenPrefix = jwt.substring(0, Math.min(jwt.length(), 20));

                Cache blacklistCache = cacheManager.getCache(BLACKLIST_CACHE);
                if (blacklistCache != null && blacklistCache.get(jwt) != null) {
                    SecurityLogger.logBlacklistedTokenAttempt(ipAddress, userAgent, tokenPrefix);
                    filterChain.doFilter(request, response);
                    return;
                }

                if (jwtUtils.validateToken(jwt)) {
                    String username = jwtUtils.getUsername(jwt);
                    UUID userId = jwtUtils.getUserId(jwt);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                            );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    SecurityLogger.logAuthenticationSuccess(username, ipAddress, userAgent);
                    log.debug("JWT 인증 성공: username = {}, userId = {}", username, userId);
                } else {
                    SecurityLogger.logAuthenticationFailure("invalid_token", ipAddress, userAgent, tokenPrefix);
                    log.debug("유효하지 않은 JWT 토큰");
                }
            }
        } catch (Exception e) {
            SecurityLogger.logAuthenticationFailure("jwt_processing_error", ipAddress, userAgent, e.getMessage());
            log.error("JWT 인증 처리 중 오류 발생: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /** Authorization 헤더에서 JWT 토큰 추출 */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
