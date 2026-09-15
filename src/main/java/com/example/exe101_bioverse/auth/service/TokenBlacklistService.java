package com.example.exe101_bioverse.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Date;
import java.util.HexFormat;

@Service
public class TokenBlacklistService {

    private static final String JWT_BLACKLIST_PREFIX = "auth:jwt:blacklist:";
    private static final String USER_INVALIDATED_PREFIX = "auth:user:";
    private static final String USER_INVALIDATED_SUFFIX = ":invalidated_at";

    private final StringRedisTemplate redisTemplate;

    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklistAccessToken(String token, Claims claims) {
        long ttlMs = remainingTtlMs(claims);
        if (ttlMs <= 0) {
            return;
        }
        redisTemplate.opsForValue().set(jwtKey(token, claims), "1", Duration.ofMillis(ttlMs));
    }

    public boolean isBlacklisted(String token, Claims claims) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(jwtKey(token, claims)));
    }

    public void invalidateAllUserTokens(Long userId, long ttlMs) {
        redisTemplate.opsForValue().set(
                userInvalidatedKey(userId),
                String.valueOf(System.currentTimeMillis()),
                Duration.ofMillis(Math.max(ttlMs, 1))
        );
    }

    public boolean isUserTokenRevoked(Long userId, Date issuedAt) {
        String value = redisTemplate.opsForValue().get(userInvalidatedKey(userId));
        if (value == null || issuedAt == null) {
            return false;
        }
        return issuedAt.getTime() <= Long.parseLong(value);
    }

    private String jwtKey(String token, Claims claims) {
        String jti = claims.getId();
        String id = (jti != null && !jti.isBlank()) ? jti : sha256(token);
        return JWT_BLACKLIST_PREFIX + id;
    }

    private String userInvalidatedKey(Long userId) {
        return USER_INVALIDATED_PREFIX + userId + USER_INVALIDATED_SUFFIX;
    }

    private long remainingTtlMs(Claims claims) {
        Date expiration = claims.getExpiration();
        if (expiration == null) {
            return 0;
        }
        return Math.max(0, expiration.getTime() - System.currentTimeMillis());
    }

    private String sha256(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }
}
