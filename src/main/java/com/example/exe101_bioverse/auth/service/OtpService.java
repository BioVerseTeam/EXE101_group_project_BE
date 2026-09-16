package com.example.exe101_bioverse.auth.service;

import com.example.exe101_bioverse.auth.dto.PendingRegistration;
import com.example.exe101_bioverse.auth.enums.OtpPurpose;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;

@Service
public class OtpService {

    private static final String OTP_PREFIX = "auth:otp:";
    private static final String ATTEMPTS_SUFFIX = ":attempts";
    private static final String COOLDOWN_PREFIX = "auth:otp:cooldown:";
    private static final String PENDING_REGISTER_PREFIX = "auth:pending:register:";
    private static final String RESET_TICKET_PREFIX = "auth:reset:ticket:";

    private final StringRedisTemplate redisTemplate;
    private final JsonMapper jsonMapper;
    private final SecureRandom secureRandom = new SecureRandom();
    private final int otpLength;
    private final long ttlSeconds;
    private final long resendCooldownSeconds;
    private final int maxAttempts;
    private final long resetTokenTtlSeconds;

    public OtpService(
            StringRedisTemplate redisTemplate,
            JsonMapper jsonMapper,
            @Value("${bioverse.otp.length:6}") int otpLength,
            @Value("${bioverse.otp.ttl-seconds:600}") long ttlSeconds,
            @Value("${bioverse.otp.resend-cooldown-seconds:60}") long resendCooldownSeconds,
            @Value("${bioverse.otp.max-attempts:5}") int maxAttempts,
            @Value("${bioverse.otp.reset-token-ttl-seconds:600}") long resetTokenTtlSeconds
    ) {
        this.redisTemplate = redisTemplate;
        this.jsonMapper = jsonMapper;
        this.otpLength = Math.max(4, Math.min(otpLength, 8));
        this.ttlSeconds = ttlSeconds;
        this.resendCooldownSeconds = resendCooldownSeconds;
        this.maxAttempts = maxAttempts;
        this.resetTokenTtlSeconds = resetTokenTtlSeconds;
    }

    public String issueOtp(String email, OtpPurpose purpose) {
        assertCanResend(email, purpose);
        String otp = generateOtp();
        Duration ttl = Duration.ofSeconds(ttlSeconds);
        redisTemplate.opsForValue().set(otpKey(email, purpose), sha256(otp), ttl);
        redisTemplate.delete(attemptsKey(email, purpose));
        return otp;
    }

    public void markCooldown(String email, OtpPurpose purpose) {
        redisTemplate.opsForValue().set(
                cooldownKey(email, purpose),
                "1",
                Duration.ofSeconds(resendCooldownSeconds)
        );
    }

    public void verifyAndConsume(String email, String otp, OtpPurpose purpose) {
        String storedHash = redisTemplate.opsForValue().get(otpKey(email, purpose));
        if (storedHash == null) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        String attemptsRaw = redisTemplate.opsForValue().get(attemptsKey(email, purpose));
        int attempts = attemptsRaw == null ? 0 : Integer.parseInt(attemptsRaw);
        if (attempts >= maxAttempts) {
            throw new AppException(ErrorCode.OTP_MAX_ATTEMPTS);
        }

        if (!MessageDigest.isEqual(storedHash.getBytes(StandardCharsets.UTF_8), sha256(otp).getBytes(StandardCharsets.UTF_8))) {
            Long next = redisTemplate.opsForValue().increment(attemptsKey(email, purpose));
            if (next != null && next == 1) {
                redisTemplate.expire(attemptsKey(email, purpose), remainingTtl(otpKey(email, purpose)));
            }
            if (next != null && next >= maxAttempts) {
                throw new AppException(ErrorCode.OTP_MAX_ATTEMPTS);
            }
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        redisTemplate.delete(otpKey(email, purpose));
        redisTemplate.delete(attemptsKey(email, purpose));
    }

    public void savePendingRegistration(PendingRegistration pending) {
        try {
            redisTemplate.opsForValue().set(
                    pendingKey(pending.getEmail()),
                    jsonMapper.writeValueAsString(pending),
                    Duration.ofSeconds(ttlSeconds)
            );
        } catch (JacksonException ex) {
            throw new IllegalStateException("Cannot serialize pending registration", ex);
        }
    }

    public PendingRegistration requirePendingRegistration(String email) {
        String json = redisTemplate.opsForValue().get(pendingKey(email));
        if (json == null) {
            throw new AppException(ErrorCode.PENDING_REGISTRATION_EXPIRED);
        }
        try {
            return jsonMapper.readValue(json, PendingRegistration.class);
        } catch (JacksonException ex) {
            throw new AppException(ErrorCode.PENDING_REGISTRATION_EXPIRED);
        }
    }

    public void deletePendingRegistration(String email) {
        redisTemplate.delete(pendingKey(email));
    }

    public String createResetTicket(String email) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(resetTicketKey(token), email, Duration.ofSeconds(resetTokenTtlSeconds));
        return token;
    }

    public String consumeResetTicket(String token) {
        String key = resetTicketKey(token);
        String email = redisTemplate.opsForValue().get(key);
        if (email == null) {
            throw new AppException(ErrorCode.INVALID_RESET_TOKEN);
        }
        redisTemplate.delete(key);
        return email;
    }

    public long getTtlSeconds() {
        return ttlSeconds;
    }

    public long getResendCooldownSeconds() {
        return resendCooldownSeconds;
    }

    public long getResetTokenTtlSeconds() {
        return resetTokenTtlSeconds;
    }

    public long getExpireMinutes() {
        return Math.max(1, ttlSeconds / 60);
    }

    private void assertCanResend(String email, OtpPurpose purpose) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cooldownKey(email, purpose)))) {
            throw new AppException(ErrorCode.OTP_RESEND_TOO_SOON);
        }
    }

    private String generateOtp() {
        int bound = (int) Math.pow(10, otpLength);
        int value = secureRandom.nextInt(bound);
        return String.format("%0" + otpLength + "d", value);
    }

    private Duration remainingTtl(String key) {
        Long ttl = redisTemplate.getExpire(key);
        if (ttl == null || ttl < 0) {
            return Duration.ofSeconds(ttlSeconds);
        }
        return Duration.ofSeconds(Math.max(1, ttl));
    }

    private String sha256(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }

    private String otpKey(String email, OtpPurpose purpose) {
        return OTP_PREFIX + purpose.name() + ":" + email;
    }

    private String attemptsKey(String email, OtpPurpose purpose) {
        return otpKey(email, purpose) + ATTEMPTS_SUFFIX;
    }

    private String cooldownKey(String email, OtpPurpose purpose) {
        return COOLDOWN_PREFIX + purpose.name() + ":" + email;
    }

    private String pendingKey(String email) {
        return PENDING_REGISTER_PREFIX + email;
    }

    private String resetTicketKey(String token) {
        return RESET_TICKET_PREFIX + token;
    }
}
