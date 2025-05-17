package io.joshuasalcedo.library.prettyconsole.style;

import io.joshuasalcedo.library.prettyconsole.style.color.ColorConverter;
import io.joshuasalcedo.library.prettyconsole.style.core.AnsiConstants;

/**
 * Utility class for applying styles to text.
 * <p>
 * This class provides static methods for applying styles to text, creating
 * style components, and manipulating styled text.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public final class Style {

    /**
     * Create an RGB foreground color.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @return A new RGB color
     */
    public static RgbColor rgb(int r, int g, int b) {
        return new RgbColor(r, g, b);
    }

    /**
     * Create an RGB background color.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @return A new RGB background color
     */
    public static RgbColor rgbBackground(int r, int g, int b) {
        return new RgbColor(r, g, b, true);
    }

    /**
     * Create an 8-bit (256 color) foreground color.
     *
     * @param colorCode Color code (0-255)
     * @return A new 8-bit color
     */
    public static Color256 color256(int colorCode) {
        return new Color256(colorCode);
    }

    /**
     * Create an 8-bit (256 color) background color.
     *
     * @param colorCode Color code (0-255)
     * @return A new 8-bit background color
     */
    public static Color256 background256(int colorCode) {
        return new Color256(colorCode, true);
    }

    /**
     * Extend a string with formatting capabilities.
     *
     * @param text The text to format
     * @return A string decorator for the text
     */
    public static StringDecorator decorate(String text) {
        return new StringDecorator(text);
    }

    /**
     * Apply formatting to the given text.
     *
     * @param component The style component to apply
     * @param text      The text to format
     * @return The formatted text with reset code at the end
     */
    public static String apply(StyleComponent component, String text) {
        return StyleManager.apply(component, text);
    }

    /**
     * Apply formatting to the given text.
     *
     * @param formatter The formatter with combined styling components
     * @param text      The text to format
     * @return The formatted text with reset code at the end
     */
    public static String apply(StyleFormatter formatter, String text) {
        return StyleManager.apply(formatter, text);
    }

    /**
     * Safely apply formatting to the given text if the terminal supports ANSI codes.
     *
     * @param component The style component to apply
     * @param text      The text to format
     * @return The formatted text if supported, the original text otherwise
     */
    public static String safeApply(StyleComponent component, String text) {
        return StyleManager.safeApply(component, text);
    }

    /**
     * Safely apply formatting to the given text if the terminal supports ANSI codes.
     *
     * @param formatter The formatter with combined styling components
     * @param text      The text to format
     * @return The formatted text if supported, the original text otherwise
     */
    public static String safeApply(StyleFormatter formatter, String text) {
        return StyleManager.safeApply(formatter, text);
    }

    /**
     * Remove all ANSI escape sequences from a string.
     *
     * @param text The text with ANSI codes
     * @return The text without any ANSI codes
     */
    public static String stripAnsi(String text) {
        return StyleManager.stripAnsi(text);
    }

    /**
     * Convert a hex color code to an RGB color.
     *
     * @param hexColor The hex color code (e.g., "#FF5500" or "FF5500")
     * @return A new RGB color object, or null if the input is invalid
     */
    public static RgbColor hexToRgb(String hexColor) {
        return ColorConverter.hexToRgb(hexColor);
    }

    /**
     * Convert RGB values to a hex color code.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @return A hex color code string
     */
    public static String rgbToHex(int r, int g, int b) {
        return ColorConverter.rgbToHex(r, g, b);
    }

    /**
     * Convert RGB values to a hex color code.
     *
     * @param color The RGB color object
     * @return A hex color code string
     */
    public static String rgbToHex(RgbColor color) {
        return ColorConverter.rgbToHex(color);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private Style() {
        // Utility class, prevent instantiation
    }
}