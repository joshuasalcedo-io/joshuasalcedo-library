package io.joshuasalcedo.library.prettyconsole.style;

import io.joshuasalcedo.library.prettyconsole.style.core.AnsiConstants;
import io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils;

import java.util.regex.Pattern;

/**
 * StyleManager provides common styling functionality for the Pretty library.
 */
public final class StyleManager {
    // Cached pattern for faster ANSI stripping
    private static final Pattern ANSI_PATTERN = Pattern.compile(AnsiConstants.ANSI_PATTERN);
    
    /**
     * Apply formatting to the given text.
     *
     * @param component The style component to apply
     * @param text      The text to format
     * @return The formatted text with reset code at the end
     */
    public static String apply(StyleComponent component, String text) {
        return component.apply(text);
    }
    
    /**
     * Apply formatting to the given text.
     *
     * @param formatter The formatter with combined styling components
     * @param text      The text to format
     * @return The formatted text with reset code at the end
     */
    public static String apply(StyleFormatter formatter, String text) {
        return formatter.apply(text);
    }
    
    /**
     * Safely apply formatting to the given text if the terminal supports ANSI codes.
     *
     * @param component The style component to apply
     * @param text      The text to format
     * @return The formatted text if supported, the original text otherwise
     */
    public static String safeApply(StyleComponent component, String text) {
        return TerminalUtils.isAnsiSupported() ? apply(component, text) : text;
    }
    
    /**
     * Safely apply formatting to the given text if the terminal supports ANSI codes.
     *
     * @param formatter The formatter with combined styling components
     * @param text      The text to format
     * @return The formatted text if supported, the original text otherwise
     */
    public static String safeApply(StyleFormatter formatter, String text) {
        return TerminalUtils.isAnsiSupported() ? apply(formatter, text) : text;
    }
    
    /**
     * Remove all ANSI escape sequences from a string.
     *
     * @param text The text with ANSI codes
     * @return The text without any ANSI codes
     */
    public static String stripAnsi(String text) {
        if (text == null) {
            return null;
        }
        return ANSI_PATTERN.matcher(text).replaceAll("");
    }
    
    private StyleManager() {
        // Prevent instantiation
    }
}