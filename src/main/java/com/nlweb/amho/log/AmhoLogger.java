package com.nlweb.amho.log;

import com.nlweb.common.log.BaseDomainLogger;

import java.util.Map;
import java.util.UUID;

public class AmhoLogger extends BaseDomainLogger {
    
    private static final String EVENT_TYPE_AMHO = "amho_management";

    public static void logSuccessAmhoEvent(String event, UUID userId, String username, Map<String, Object> details) {
        logSuccessEvent(EVENT_TYPE_AMHO, event, userId, username, details);
    }

    public static void logFailureAmhoEvent(String operation, UUID userId, String username, Exception error, Map<String, Object> details) {
        BaseDomainLogger.logError(EVENT_TYPE_AMHO, operation, userId, username, error, details);
    }

    private AmhoLogger() {}
}