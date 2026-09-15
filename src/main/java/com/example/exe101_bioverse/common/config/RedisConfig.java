package com.example.exe101_bioverse.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;

@Configuration
public class RedisConfig {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(Environment env) {
        String redisUrl = firstNonBlank(env.getProperty("REDIS_URL"));
        RedisStandaloneConfiguration standalone = new RedisStandaloneConfiguration();
        boolean useSsl = parseBoolean(firstNonBlank(env.getProperty("REDIS_SSL"), env.getProperty("spring.data.redis.ssl.enabled")));
        String password = resolvePassword(env);

        if (redisUrl != null) {
            URI uri = URI.create(redisUrl.trim());
            if (uri.getHost() == null) {
                throw new IllegalArgumentException("REDIS_URL is missing host: " + uri.getScheme());
            }
            standalone.setHostName(uri.getHost());
            standalone.setPort(uri.getPort() > 0 ? uri.getPort() : 6379);
            applyUserInfo(standalone, uri.getUserInfo());
            useSsl = isSslScheme(uri.getScheme()) || useSsl;
            if (!standalone.getPassword().isPresent() && password != null) {
                standalone.setPassword(RedisPassword.of(password));
            }
        } else {
            standalone.setHostName(firstNonBlank(
                    env.getProperty("REDIS_HOST"),
                    env.getProperty("spring.data.redis.host"),
                    "localhost"
            ));
            standalone.setPort(parsePort(firstNonBlank(
                    env.getProperty("REDIS_PORT"),
                    env.getProperty("spring.data.redis.port"),
                    "6380"
            )));
            String username = firstNonBlank(
                    env.getProperty("REDIS_USERNAME"),
                    env.getProperty("spring.data.redis.username")
            );
            if (username != null) {
                standalone.setUsername(username);
            }
            if (password != null) {
                standalone.setPassword(RedisPassword.of(password));
            }
        }

        if (!standalone.getPassword().isPresent()) {
            Path envFile = DotEnvLoader.loadedFrom();
            throw new IllegalStateException(
                    "REDIS_PASSWORD is required. Looked at OS env, Spring env, and "
                            + (envFile != null ? envFile : ".env (not found)")
                            + ". Set REDIS_PASSWORD in Backend/EXE101_group_project_BE/.env then restart."
            );
        }

        Duration timeout = parseDuration(firstNonBlank(
                env.getProperty("REDIS_TIMEOUT"),
                env.getProperty("spring.data.redis.timeout"),
                "2s"
        ));
        LettuceClientConfiguration.LettuceClientConfigurationBuilder client =
                LettuceClientConfiguration.builder().commandTimeout(timeout);
        if (useSsl) {
            client.useSsl();
        }

        log.info("Redis connected to {}:{} (ssl={}, dotenv={})",
                standalone.getHostName(),
                standalone.getPort(),
                useSsl,
                DotEnvLoader.loadedFrom());
        return new LettuceConnectionFactory(standalone, client.build());
    }

    private String resolvePassword(Environment env) {
        return firstNonBlank(
                env.getProperty("REDIS_PASSWORD"),
                DotEnvLoader.get("REDIS_PASSWORD"),
                env.getProperty("spring.data.redis.password")
        );
    }

    private void applyUserInfo(RedisStandaloneConfiguration standalone, String userInfo) {
        if (userInfo == null || userInfo.isBlank()) {
            return;
        }
        int colon = userInfo.indexOf(':');
        if (colon < 0) {
            standalone.setPassword(RedisPassword.of(userInfo));
            return;
        }
        String user = userInfo.substring(0, colon);
        String pass = userInfo.substring(colon + 1);
        if (!user.isBlank()) {
            standalone.setUsername(user);
        }
        if (!pass.isBlank()) {
            standalone.setPassword(RedisPassword.of(pass));
        }
    }

    private boolean isSslScheme(String scheme) {
        if (scheme == null) {
            return false;
        }
        String normalized = scheme.toLowerCase();
        return normalized.equals("rediss") || normalized.contains("ssl") || normalized.contains("tls");
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private boolean parseBoolean(String value) {
        return Boolean.parseBoolean(value);
    }

    private int parsePort(String value) {
        return Integer.parseInt(value);
    }

    private Duration parseDuration(String value) {
        return DurationStyle.detectAndParse(value);
    }
}
