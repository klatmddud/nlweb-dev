package com.nlweb.auth.log;

import com.nlweb.common.log.BaseDomainLogger;

import java.util.Map;
import java.util.UUID;

public class AuthLogger extends BaseDomainLogger {
    
    private static final String EVENT_TYPE_AUTH = "authentication";
    private static final String EVENT_TYPE_SECURITY = "security";

    public static void logSuccessEvent(String event, UUID userId, String username, Map<String, Object> details) {
        logSuccessEvent(EVENT_TYPE_AUTH, event, userId, username, details);
    }

    public static void logFailureEvent(String operation, UUID userId, String username, Exception error, Map<String, Object> details) {
        logError(EVENT_TYPE_SECURITY, operation, userId, username, error, details);
    }
    
    private AuthLogger() {}
}