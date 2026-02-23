package com.nlweb.core.filter;

import com.nlweb.common.util.IpUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestTracingFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_KEY = "traceId";
    private static final String USER_IP_KEY = "userIp";
    private static final String REQUEST_URI_KEY = "requestUri";
    private static final String REQUEST_METHOD_KEY = "requestMethod";
    private static final String USER_AGENT_KEY = "userAgent";
    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String traceId = getOrGenerateTraceId(request);
        String userIp = IpUtils.getClientIp(request);
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        String userAgent = request.getHeader("User-Agent");

        try {
            MDC.put(TRACE_ID_KEY, traceId);
            MDC.put(USER_IP_KEY, userIp);
            MDC.put(REQUEST_URI_KEY, requestUri);
            MDC.put(REQUEST_METHOD_KEY, requestMethod);
            MDC.put(USER_AGENT_KEY, userAgent);

            response.setHeader(REQUEST_ID_HEADER, traceId);

            long startTime = System.currentTimeMillis();
            
            log.info("Request started: {} {} from {}", requestMethod, requestUri, userIp);

            filterChain.doFilter(request, response);

            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            
            log.info("Request completed: {} {} - Status: {} - Duration: {}ms", 
                    requestMethod, requestUri, status, duration);

        } catch (Exception e) {
            log.error("Request failed: {} {} from {} - Error: {}", 
                    requestMethod, requestUri, userIp, e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }

    private String getOrGenerateTraceId(HttpServletRequest request) {
        String existingTraceId = request.getHeader(REQUEST_ID_HEADER);
        if (existingTraceId != null && !existingTraceId.trim().isEmpty()) {
            return existingTraceId;
        }
        return generateTraceId();
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/actuator/health") || 
               path.startsWith("/favicon.ico") ||
               path.startsWith("/static/");
    }
}