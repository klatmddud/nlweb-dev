package com.nlweb.common.util;

import com.nlweb.core.security.NlwebUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /** 현재 인증된 사용자의 username을 반환 */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof NlwebUserDetails userDetails) {
            return userDetails.getUsername();
        }
        
        if (principal instanceof String string) {
            return string;
        }
        
        return null;
    }

    /** 현재 인증된 사용자의 NlwebUserDetails를 반환 */
    public static NlwebUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof NlwebUserDetails) {
            return (NlwebUserDetails) principal;
        }
        
        return null;
    }

    /** 현재 사용자의 인증여부 확인 */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() 
               && !"anonymousUser".equals(authentication.getPrincipal());
    }

    private SecurityUtils() {
        // 인스턴스 생성 방지용
    }
}
