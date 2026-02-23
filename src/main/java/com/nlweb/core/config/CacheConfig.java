package com.nlweb.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    /** Caffeine 기반 캐시 매니저 설정 */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // 기본 Caffeine 설정
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .recordStats()
        );
        
        return cacheManager;
    }

    /** 사용자 관련 캐시 - 짧은 TTL */
    @Bean("userCacheManager")
    public CacheManager userCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("users", "user-permissions");
        
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .recordStats()
        );
        
        return cacheManager;
    }

    /** Amho 캐시 - 중간 TTL */
    @Bean("amhoCacheManager")
    public CacheManager amhoCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("amho", "active-amho");
        
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(200)
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .recordStats()
        );
        
        return cacheManager;
    }

    /** 설정값 캐시 - 긴 TTL */
    @Bean("configCacheManager") 
    public CacheManager configCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("configs", "system-settings");
        
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(2, TimeUnit.HOURS)
                .recordStats()
        );
        
        return cacheManager;
    }

    /** JWT 블랙리스트 캐시 - 토큰 만료시간과 동일하게 설정 */
    @Bean("jwtBlacklistCacheManager")
    public CacheManager jwtBlacklistCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("jwt-blacklist");
        
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(10000)                         // 최대 10,000개 블랙리스트 토큰
                .expireAfterWrite(24, TimeUnit.HOURS)       // 24시간 후 자동 삭제 (토큰 만료시간과 맞춤)
                .recordStats()
        );
        
        return cacheManager;
    }
}
