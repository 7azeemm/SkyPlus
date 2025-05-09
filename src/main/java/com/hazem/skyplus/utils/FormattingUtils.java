package com.hazem.skyplus.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for formatting numbers, prices, times, and enum names in various styles.
 */
public class FormattingUtils {
    private static final NumberFormat COMPACT_NUMBER_FORMATTER = NumberFormat.getCompactNumberInstance(Locale.CANADA, NumberFormat.Style.SHORT);
    private static final NumberFormat NUMBER_FORMATTER = NumberFormat.getInstance(Locale.CANADA);

    static {
        COMPACT_NUMBER_FORMATTER.setMaximumFractionDigits(2);
    }

    /**
     * Formats a price value using a compact number format (e.g., 1K, 1M).
     *
     * @param price the price value to be formatted.
     * @return the formatted price as a string.
     */
    public static String formatPrice(double price) {
        return COMPACT_NUMBER_FORMATTER.format(price);
    }

    /**
     * Formats a number by adding commas to separate thousands (e.g., 1,000,000).
     *
     * @param number the number to be formatted.
     * @return the formatted number with commas as a string.
     */
    public static String formatNumber(int number) {
        return NUMBER_FORMATTER.format(number);
    }

    /**
     * Formats a time value given in milliseconds into a human-readable string.
     * The format follows: "Xd Xh Xm Xs", depending on the time duration.
     *
     * @param millis the time in milliseconds to be formatted.
     * @return the formatted time as a string, e.g., "1d 5h 30m 10s" or "45s".
     */
    public static String formatTime(long millis) {
        long totalSeconds = millis / 1000;
        long days = totalSeconds / 86400;  // 86400 seconds in a day
        long hours = (totalSeconds % 86400) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        StringBuilder formattedTime = new StringBuilder();

        if (days > 0) formattedTime.append(days).append("d ");
        if (hours > 0) formattedTime.append(hours).append("h ");
        if (minutes > 0) formattedTime.append(minutes).append("m ");
        if (seconds > 0 || formattedTime.isEmpty()) formattedTime.append(seconds).append("s");

        return formattedTime.toString().trim();
    }

    /**
     * Converts an enum value to a formatted string by capitalizing each word and separating them with spaces.
     * The enum name should use underscores to separate words.
     * For example, the enum value "SOME_ENUM_VALUE" will be converted to "Some Enum Value".
     *
     * @param enumValue the enum value to be formatted.
     * @return the formatted string representation of the enum value.
     */
    public static String formatEnumName(Enum<?> enumValue) {
        String name = enumValue.name();
        String[] words = name.split("_");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                // Capitalize the first letter and make the rest lowercase
                formattedName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" "); // Add a space after each word
            }
        }

        return formattedName.toString().trim(); // Remove the trailing space
    }
}
