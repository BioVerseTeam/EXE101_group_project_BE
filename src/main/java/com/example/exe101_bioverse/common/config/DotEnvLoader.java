package com.example.exe101_bioverse.common.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class DotEnvLoader {

    private static final Map<String, String> VALUES = load();

    private DotEnvLoader() {
    }

    static String get(String key) {
        return VALUES.get(key);
    }

    static Map<String, String> asMap() {
        return VALUES;
    }

    static Path loadedFrom() {
        return LOADED_FROM;
    }

    private static Path LOADED_FROM;

    private static Map<String, String> load() {
        Path envFile = findEnvFile();
        LOADED_FROM = envFile;
        if (envFile == null) {
            return Map.of();
        }
        return parse(envFile);
    }

    private static Path findEnvFile() {
        Path cwd = Path.of(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
        List<Path> candidates = List.of(
                cwd.resolve(".env"),
                cwd.resolve("EXE101_group_project_BE/.env"),
                cwd.resolve("Backend/EXE101_group_project_BE/.env")
        );
        for (Path candidate : candidates) {
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        Path current = cwd;
        for (int i = 0; i < 5; i++) {
            Path nested = current.resolve("Backend/EXE101_group_project_BE/.env");
            if (Files.isRegularFile(nested)) {
                return nested;
            }
            Path sibling = current.resolve(".env");
            if (Files.isRegularFile(sibling)) {
                return sibling;
            }
            current = current.getParent();
            if (current == null) {
                break;
            }
        }
        return null;
    }

    private static Map<String, String> parse(Path envFile) {
        Map<String, String> values = new LinkedHashMap<>();
        try {
            for (String raw : Files.readAllLines(envFile, StandardCharsets.UTF_8)) {
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("export ")) {
                    line = line.substring(7).trim();
                }
                int eq = line.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String key = line.substring(0, eq).trim();
                String value = stripQuotes(line.substring(eq + 1).trim());
                values.put(key, value);
            }
        } catch (IOException ignored) {
            return Map.of();
        }
        return Map.copyOf(values);
    }

    private static String stripQuotes(String value) {
        if (value.length() >= 2) {
            char first = value.charAt(0);
            char last = value.charAt(value.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }
}
