package com.nlweb.common.log;

import java.util.Map;

public class SecurityLogger extends BaseDomainLogger {

    private static final String EVENT_TYPE_SECURITY = "security";
    
    public static void logAuthenticationSuccess(String username, String ipAddress, String userAgent) {
        Map<String, Object> details = Map.of(
            "ip_address", ipAddress,
            "user_agent", userAgent
        );
        logSuccessEvent(EVENT_TYPE_SECURITY, "jwt_auth_success", null, username, details);
    }

    public static void logAuthenticationFailure(String reason, String ipAddress, String userAgent, String tokenInfo) {
        Map<String, Object> details = Map.of(
            "reason", reason,
            "ip_address", ipAddress,
            "user_agent", userAgent,
            "token_info", tokenInfo
        );
        logError(EVENT_TYPE_SECURITY, "jwt_auth_failure", null, "unknown", new RuntimeException(reason), details);
    }

    public static void logBlacklistedTokenAttempt(String ipAddress, String userAgent, String tokenPrefix) {
        Map<String, Object> details = Map.of(
            "ip_address", ipAddress,
            "user_agent", userAgent,
            "token_prefix", tokenPrefix
        );
        logError(EVENT_TYPE_SECURITY, "blacklisted_token_usage", null, "unknown", new RuntimeException("Blacklisted token usage"), details);
    }

    public static void logAccessDenied(String username, String requestedResource, String ipAddress, String reason) {
        Map<String, Object> details = Map.of(
            "requested_resource", requestedResource,
            "ip_address", ipAddress,
            "reason", reason
        );
        logError(EVENT_TYPE_SECURITY, "access_denied", null, username, new RuntimeException(reason), details);
    }

    public static void logLoginFailure(String identifier, String ipAddress, String userAgent, String reason) {
        Map<String, Object> details = Map.of(
            "identifier", identifier,
            "ip_address", ipAddress,
            "user_agent", userAgent,
            "reason", reason
        );
        logError(EVENT_TYPE_SECURITY, "login_failure", null, identifier, new RuntimeException(reason), details);
    }

    public static void logRegistrationFailure(String username, String email, String ipAddress, String reason) {
        Map<String, Object> details = Map.of(
            "email", email,
            "ip_address", ipAddress,
            "reason", reason
        );
        logError(EVENT_TYPE_SECURITY, "registration_failure", null, username, new RuntimeException(reason), details);
    }

    public static void logSuspiciousActivity(String username, String activity, String ipAddress, Map<String, Object> details) {
        Map<String, Object> securityDetails = Map.of(
            "ip_address", ipAddress,
            "details", details != null ? details : Map.of()
        );
        logError(EVENT_TYPE_SECURITY, activity, null, username, new RuntimeException("Suspicious activity detected"), securityDetails);
    }

    public static void logTokenManipulation(String event, String username, String ipAddress, String tokenInfo) {
        Map<String, Object> details = Map.of(
            "ip_address", ipAddress,
            "token_info", tokenInfo
        );
        logError(EVENT_TYPE_SECURITY, event, null, username, new RuntimeException("Token manipulation detected"), details);
    }

    private SecurityLogger() {}
}