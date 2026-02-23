package com.nlweb.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;

    private static final String REFRESH_TOKEN_CACHE = "tokenCache";
    private static final String BLACKLIST_CACHE = "blacklistCache";
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    /** 리프레시 토큰 캐시에 저장 */
    public void saveRefreshToken(String username, String refreshToken) {
        String cacheKey = REFRESH_TOKEN_PREFIX + username;
        Objects.requireNonNull(cacheManager.getCache(REFRESH_TOKEN_CACHE)).put(cacheKey, refreshToken);
    }

    /** 사용자명으로 리프레시 토큰 캐시에서 삭제 */
    public void deleteRefreshToken(String username) {
        String cacheKey = REFRESH_TOKEN_PREFIX + username;
        Objects.requireNonNull(cacheManager.getCache(REFRESH_TOKEN_CACHE)).evict(cacheKey);
    }

    /** 토큰 블랙리스트에 추가 */
    public void blacklistToken(String token, Long remainingTime) {
        String blacklistKey = BLACKLIST_PREFIX + token;

        if (remainingTime > 0) {
            Objects.requireNonNull(cacheManager.getCache(BLACKLIST_CACHE)).put(blacklistKey, true);
        }
    }

    /** 캐시에 저장된 리프레시 토큰과 일치하는지 확인 */
    public void validateRefreshToken(String username, String refreshToken) {
        String cacheKey = REFRESH_TOKEN_PREFIX + username;
        String cachedToken = Objects.requireNonNull(cacheManager.getCache(REFRESH_TOKEN_CACHE)).get(cacheKey, String.class);
        
        if (cachedToken == null || !cachedToken.equals(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }
    }

}