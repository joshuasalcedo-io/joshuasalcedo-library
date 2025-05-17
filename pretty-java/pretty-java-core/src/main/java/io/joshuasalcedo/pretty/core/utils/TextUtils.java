package io.joshuasalcedo.pretty.core.utils;

import java.util.regex.Pattern;

/**
 * Utility class for text manipulation operations.
 * <p>
 * This class provides methods for common text operations used in console formatting,
 * such as truncation, padding, centering, and ANSI code handling.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public final class TextUtils {

    // Pattern to match ANSI escape codes
    private static final Pattern ANSI_PATTERN = Pattern.compile("\u001B\\[[;\\d]*m");

    private TextUtils() {
        // Utility class, prevent instantiation
    }

    /**
     * Calculates the visible length of a string, ignoring ANSI escape codes.
     *
     * @param text The text to measure
     * @return The visible length of the text
     */
    public static int visibleLength(String text) {
        if (text == null) {
            return 0;
        }
        // Remove all ANSI escape codes before calculating length
        return ANSI_PATTERN.matcher(text).replaceAll("").length();
    }

    /**
     * Truncates a string to the specified length, preserving ANSI escape codes.
     *
     * @param text The text to truncate
     * @param maxLength The maximum visible length
     * @return The truncated text
     */
    public static String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }

        if (visibleLength(text) <= maxLength) {
            return text;
        }

        StringBuilder result = new StringBuilder();
        StringBuilder current = new StringBuilder();
        int visibleChars = 0;
        boolean inEscapeSequence = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\u001B') {
                // Start of escape sequence
                inEscapeSequence = true;
                current.append(c);
            } else if (inEscapeSequence) {
                current.append(c);
                if (c == 'm') {
                    // End of escape sequence
                    inEscapeSequence = false;
                    result.append(current);
                    current.setLength(0);
                }
            } else {
                if (visibleChars < maxLength) {
                    result.append(c);
                    visibleChars++;
                } else {
                    break;
                }
            }
        }

        return result.toString();
    }

    /**
     * Repeats a character a specified number of times.
     *
     * @param c The character to repeat
     * @param count The number of times to repeat
     * @return A string containing the repeated character
     */
    public static String repeat(char c, int count) {
        if (count <= 0) {
            return "";
        }
        char[] chars = new char[count];
        java.util.Arrays.fill(chars, c);
        return new String(chars);
    }

    /**
     * Centers text within a specified width.
     *
     * @param text The text to center
     * @param width The total width
     * @param padChar The character to use for padding
     * @return The centered text
     */
    public static String center(String text, int width, char padChar) {
        if (text == null) {
            text = "";
        }

        int textLength = visibleLength(text);
        if (textLength >= width) {
            return text;
        }

        int leftPadding = (width - textLength) / 2;
        int rightPadding = width - textLength - leftPadding;

        return repeat(padChar, leftPadding) + text + repeat(padChar, rightPadding);
    }

    /**
     * Pads text on the left to reach the specified width.
     *
     * @param text The text to pad
     * @param width The total width
     * @param padChar The character to use for padding
     * @return The left-padded text
     */
    public static String padLeft(String text, int width, char padChar) {
        if (text == null) {
            text = "";
        }

        int textLength = visibleLength(text);
        if (textLength >= width) {
            return text;
        }

        return repeat(padChar, width - textLength) + text;
    }

    /**
     * Pads text on the right to reach the specified width.
     *
     * @param text The text to pad
     * @param width The total width
     * @param padChar The character to use for padding
     * @return The right-padded text
     */
    public static String padRight(String text, int width, char padChar) {
        if (text == null) {
            text = "";
        }

        int textLength = visibleLength(text);
        if (textLength >= width) {
            return text;
        }

        return text + repeat(padChar, width - textLength);
    }
}
