package com.khahnm04.ecommerce.common.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugUtils {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern LEADING_TRAILING_DASH = Pattern.compile("(^-|-$)");
    private static final Pattern MULTIPLE_DASHES = Pattern.compile("-{2,}");

    private SlugUtils() {}

    public static String toSlug(String input) {

        if (input == null || input.isBlank()) {
            return "";
        }

        // Normalize unicode (Vietnamese -> ASCII)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replace("Đ", "D").replace("đ", "d");

        // Replace whitespace with dash
        String slug = WHITESPACE.matcher(normalized).replaceAll("-");

        // Remove non la-tin chars
        slug = NON_LATIN.matcher(slug).replaceAll("");

        // Remove multiple dashes
        slug = MULTIPLE_DASHES.matcher(slug).replaceAll("-");

        // Trim dash at start/end
        slug = LEADING_TRAILING_DASH.matcher(slug).replaceAll("");

        return slug.toLowerCase(Locale.ENGLISH);
    }

}
