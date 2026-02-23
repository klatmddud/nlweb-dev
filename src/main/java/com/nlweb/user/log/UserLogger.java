package com.nlweb.user.log;

import com.nlweb.common.log.BaseDomainLogger;

import java.util.Map;
import java.util.UUID;

public class UserLogger extends BaseDomainLogger {
    
    private static final String EVENT_TYPE_USER = "user_management";

    public static void logSuccessUserEvent(String event, UUID userId, String username, Map<String, Object> details) {
        logSuccessEvent(EVENT_TYPE_USER, event, userId, username, details);
    }

    public static void logFailureUserEvent(String operation, UUID userId, String username, Exception error, Map<String, Object> details) {
        logError(EVENT_TYPE_USER, operation, userId, username, error, details);
    }

    private UserLogger() {}
}
