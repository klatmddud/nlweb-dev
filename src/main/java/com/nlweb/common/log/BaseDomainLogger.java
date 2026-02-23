package com.nlweb.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

public abstract class BaseDomainLogger {
    
    protected static final Logger BUSINESS_LOG = LoggerFactory.getLogger("com.nlweb.business");
    
    // Common MDC Keys
    protected static final String EVENT_TYPE_KEY = "event_type";
    protected static final String EVENT_KEY = "event";
    protected static final String USERNAME_KEY = "username";
    protected static final String USER_ID_KEY = "user_id";
    protected static final String RESULT_KEY = "result";
    protected static final String IP_ADDRESS_KEY = "ip_address";
    protected static final String OPERATION_KEY = "operation";
    protected static final String ERROR_CLASS_KEY = "error_class";
    protected static final String ERROR_MESSAGE_KEY = "error_message";
    protected static final String DETAIL_PREFIX = "detail_";
    
    protected static void putDetailsInMDC(Map<String, Object> details) {
        if (details != null) {
            details.forEach((key, value) -> MDC.put(DETAIL_PREFIX + key, String.valueOf(value)));
        }
    }
    
    protected static void clearDomainMDC() {
        MDC.remove(EVENT_TYPE_KEY);
        MDC.remove(EVENT_KEY);
        MDC.remove(USERNAME_KEY);
        MDC.remove(USER_ID_KEY);
        MDC.remove(RESULT_KEY);
        MDC.remove(IP_ADDRESS_KEY);
        MDC.remove(OPERATION_KEY);
        MDC.remove(ERROR_CLASS_KEY);
        MDC.remove(ERROR_MESSAGE_KEY);
        
        MDC.getCopyOfContextMap().keySet().stream()
                .filter(key -> key.startsWith(DETAIL_PREFIX))
                .forEach(MDC::remove);
    }
    
    protected static void logEvent(String eventType, String event, UUID userId, String username, String result, Map<String, Object> details) {
        try {
            MDC.put(EVENT_TYPE_KEY, eventType);
            MDC.put(EVENT_KEY, event);
            if (userId != null) MDC.put(USER_ID_KEY, userId.toString());
            if (username != null) MDC.put(USERNAME_KEY, username);
            MDC.put(RESULT_KEY, result);
            
            putDetailsInMDC(details);
            
            String logMessage = buildLogMessage(eventType, event, username, result);
            
            if ("failed".equals(result)) {
                BUSINESS_LOG.warn(logMessage, event, username != null ? username : "unknown", result);
            } else {
                BUSINESS_LOG.info(logMessage, event, username != null ? username : "unknown", result);
            }
            
        } finally {
            clearDomainMDC();
        }
    }
    
    protected static void logSuccessEvent(String eventType, String event, UUID userId, String username, Map<String, Object> details) {
        logEvent(eventType, event, userId, username, "completed", details);
    }
    
    protected static void logFailureEvent(String eventType, String event, UUID userId, String username, String reason, Map<String, Object> details) {
        if (reason != null && details == null) {
            details = Map.of("reason", reason);
        } else if (reason != null) {
            details = new java.util.HashMap<>(details);
            details.put("reason", reason);
        }
        logEvent(eventType, event, userId, username, "failed", details);
    }
    
    private static String buildLogMessage(String eventType, String event, String username, String result) {
        String domain = eventType.replace("_management", "").replace("authentication", "Auth").replace("amho", "Amho");
        return domain + " event: {} - User: {} - Result: {}";
    }
    
    protected static void logError(String eventType, String operation, UUID userId, String username, Exception error, Map<String, Object> details) {
        try {
            MDC.put(EVENT_TYPE_KEY, eventType);
            MDC.put(OPERATION_KEY, operation);
            MDC.put(USER_ID_KEY, userId != null ? userId.toString() : null);
            MDC.put(USERNAME_KEY, username);
            MDC.put(ERROR_CLASS_KEY, error.getClass().getSimpleName());
            MDC.put(ERROR_MESSAGE_KEY, error.getMessage());
            
            putDetailsInMDC(details);
            
            BUSINESS_LOG.error("Operation failed: {} - User: {} - Error: {}", 
                    operation, username, error.getMessage(), error);
            
        } finally {
            clearDomainMDC();
        }
    }

}