package com.nlweb.common.util;

import com.nlweb.core.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.cache.CacheManager;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;
    private final CacheManager cacheManager;

    /** 시크릿 키 생성 */
    private SecretKey getSigninKey() {
        try {
            String secret = jwtProperties.getSecret();
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            
            // 키가 256비트(32바이트) 미만이면 SHA-256으로 해싱하여 확장
            if (keyBytes.length < 32) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                keyBytes = digest.digest(keyBytes);
            }
            
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 알고리즘을 찾을 수 없습니다: {}", e.getMessage());
            throw new RuntimeException("JWT 키 생성 실패", e);
        }
    }

    /** http 요청에서 토큰 추출 */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getHeaderName());

        if (bearerToken != null && bearerToken.startsWith(jwtProperties.getTokenPrefix())) {
            return bearerToken.substring(jwtProperties.getTokenPrefix().length());
        }

        return null;
    }

    /** 엑세스 토큰 생성 */
    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getAccessTokenValidity());

        return Jwts.builder()
                .setSubject(username)
                .claim("type", "access")
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigninKey())
                .compact();
    }

    /** 엑세스 토큰 유효시간 반환 */
    public Long getAccessTokenValidity() {
        return jwtProperties.getAccessTokenValidity();
    }

    /** 리프레시 토큰 생성 */
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshTokenValidity());

        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigninKey())
                .compact();
    }

    /** 리프레시 토큰 유효시간 반환 */
    public Long getRefreshTokenValidity() {
        return jwtProperties.getRefreshTokenValidity();
    }

    /** 토큰 잔여 시간 반환 */
    public Long getRemainingTime(String token) {
        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();
        long now = System.currentTimeMillis();
        return Math.max(0, expiration.getTime() - now);
    }

    /** 토큰에서 username 추출 */
    public String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /** 토큰에서 User UUID 추출 */
    public UUID getUserId(String token) {
        Claims claims = parseToken(token);
        return UUID.fromString((String) claims.get("userId"));
    }

    /** 토큰 유효성 검증 */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);

            if (isTokenBlacklisted(token)) {
                log.debug("블랙리스트에 등록된 토큰입니다");
                return false;
            }

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                log.debug("만료된 토큰입니다");
                return false;
            }

            return true;

        } catch (ExpiredJwtException e) {
            log.debug("만료된 토큰: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.debug("지원되지 않는 토큰: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.debug("잘못된 토큰 형식: {}", e.getMessage());
        } catch (SignatureException e) {
            log.debug("토큰 서명 검증 실패: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.debug("잘못된 토큰: {}", e.getMessage());
        }

        return false;
    }

    /** 토큰 파싱 */
    private Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigninKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException e) {
            log.error("유효하지 않은 JWT 토큰: {}", e.getMessage());
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰: {}", e.getMessage());
            throw new IllegalArgumentException("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰: {}", e.getMessage());
            throw new IllegalArgumentException("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 비어있습니다: {}", e.getMessage());
            throw new IllegalArgumentException("JWT 토큰이 비어있습니다.", e);
        }
    }

    /** 블랙리스트 확인 */
    public boolean isTokenBlacklisted(String token) {
        try {
            String blacklistKey = "blacklist:" + token;
            return Objects.requireNonNull(cacheManager.getCache("blacklistCache")).get(blacklistKey) != null;
        } catch (Exception e) {
            log.error("블랙리스트 확인 중 오류: {}", e.getMessage());
            return false;
        }
    }

}
