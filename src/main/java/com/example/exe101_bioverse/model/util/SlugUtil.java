package com.example.exe101_bioverse.model.util;

import java.text.Normalizer;
import java.util.Locale;

public final class SlugUtil {

    private SlugUtil() {
    }

    public static String slugify(String text) {
        if (text == null || text.isBlank()) {
            return "item";
        }
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .replace('đ', 'd')
                .replace('Đ', 'd')
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
        if (normalized.isBlank()) {
            return "item";
        }
        return normalized.substring(0, Math.min(80, normalized.length()));
    }
}
