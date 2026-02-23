package com.nlweb.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtils {

    /** 클라이언트의 IP 주소 추출 */
    public static String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    private IpUtils() {
        // 인스턴스 생성 방지용
    }
}