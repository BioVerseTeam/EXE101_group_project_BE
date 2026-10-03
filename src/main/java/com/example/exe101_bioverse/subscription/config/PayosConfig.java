package com.example.exe101_bioverse.subscription.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Slf4j
@Getter
@Configuration
public class PayosConfig {

    @Value("${bioverse.payos.client-id:${PAYOS_CLIENT_ID:}}")
    private String clientId;

    @Value("${bioverse.payos.api-key:${PAYOS_API_KEY:}}")
    private String apiKey;

    @Value("${bioverse.payos.checksum-key:${PAYOS_CHECKSUM_KEY:}}")
    private String checksumKey;

    @Value("${bioverse.payos.return-url:${PAYOS_RETURN_URL:http://localhost:5173/checkout}}")
    private String returnUrl;

    @Value("${bioverse.payos.cancel-url:${PAYOS_CANCEL_URL:http://localhost:5173/checkout}}")
    private String cancelUrl;

    /**
     * Cờ cho phép người dùng trải nghiệm miễn phí toàn bộ hệ thống để thu hút người dùng.
     * Mặc định là false (tức là không enforce quota, ai cũng được dùng hết tính năng).
     */
    @Value("${bioverse.subscription.enforce-quota:false}")
    private boolean enforceQuota;

    public String getClientId() {
        if (clientId != null && !clientId.isBlank()) return clientId;
        return com.example.exe101_bioverse.common.config.DotEnvLoader.get("PAYOS_CLIENT_ID");
    }

    public String getApiKey() {
        if (apiKey != null && !apiKey.isBlank()) return apiKey;
        return com.example.exe101_bioverse.common.config.DotEnvLoader.get("PAYOS_API_KEY");
    }

    public String getChecksumKey() {
        if (checksumKey != null && !checksumKey.isBlank()) return checksumKey;
        return com.example.exe101_bioverse.common.config.DotEnvLoader.get("PAYOS_CHECKSUM_KEY");
    }

    public String getReturnUrl() {
        if (returnUrl != null && !returnUrl.isBlank()) return returnUrl;
        String val = com.example.exe101_bioverse.common.config.DotEnvLoader.get("PAYOS_RETURN_URL");
        return (val != null && !val.isBlank()) ? val : "http://localhost:5173/checkout";
    }

    public String getCancelUrl() {
        if (cancelUrl != null && !cancelUrl.isBlank()) return cancelUrl;
        String val = com.example.exe101_bioverse.common.config.DotEnvLoader.get("PAYOS_CANCEL_URL");
        return (val != null && !val.isBlank()) ? val : "http://localhost:5173/checkout";
    }

    public boolean isConfigured() {
        String cId = getClientId();
        String aKey = getApiKey();
        String cKey = getChecksumKey();
        return cId != null && !cId.isBlank()
                && aKey != null && !aKey.isBlank()
                && cKey != null && !cKey.isBlank();
    }

    @Bean
    public PayOS payOS() {
        if (!isConfigured()) {
            log.warn("PayOS credentials are not configured yet in .env (PAYOS_CLIENT_ID / PAYOS_API_KEY / PAYOS_CHECKSUM_KEY). Initializing fallback client. Checkout will notify users to configure credentials.");
            return new PayOS("placeholder-client-id", "placeholder-api-key", "placeholder-checksum-key");
        }
        String cId = getClientId().trim();
        String aKey = getApiKey().trim();
        String cKey = getChecksumKey().trim();
        log.info("Initializing PayOS client with Client ID: {}...", cId.substring(0, Math.min(6, cId.length())));
        return new PayOS(cId, aKey, cKey);
    }

    @Bean
    public com.fasterxml.jackson.databind.ObjectMapper objectMapper() {
        return new com.fasterxml.jackson.databind.ObjectMapper();
    }
}
