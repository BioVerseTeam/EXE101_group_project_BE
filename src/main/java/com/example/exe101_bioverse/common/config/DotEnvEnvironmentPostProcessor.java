package com.example.exe101_bioverse.common.config;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Map;

public class DotEnvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, String> dotenv = DotEnvLoader.asMap();
        if (dotenv.isEmpty()) {
            return;
        }
        Map<String, Object> values = new HashMap<>(dotenv);
        MapPropertySource propertySource = new MapPropertySource("dotenv-file", values);
        if (environment.getPropertySources().contains(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)) {
            environment.getPropertySources().addAfter(
                    StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                    propertySource
            );
        } else {
            environment.getPropertySources().addFirst(propertySource);
        }
    }
}
