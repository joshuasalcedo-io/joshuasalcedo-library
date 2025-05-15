package io.joshuasalcedo.library.prettyconsole.style;

import io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils;

import java.util.regex.Pattern;

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
     * ANSI reset code that returns terminal to default state.
     */
    public static final String RESET = "\033[0m";

    /**
     * ANSI CSI (Control Sequence Introducer) prefix for all ANSI escape sequences.
     */
    public static final String CSI = "\033[";

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
        return component.safeApply(text);
    }

    /**
     * Safely apply formatting to the given text if the terminal supports ANSI codes.
     *
     * @param formatter The formatter with combined styling components
     * @param text      The text to format
     * @return The formatted text if supported, the original text otherwise
     */
    public static String safeApply(StyleFormatter formatter, String text) {
        return formatter.safeApply(text);
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
        // Pattern to match ANSI escape sequences
        Pattern pattern = Pattern.compile("\u001B\\[[;\\d]*[ -/]*[@-~]");
        return pattern.matcher(text).replaceAll("");
    }

    /**
     * Convert a hex color code to an RGB color.
     *
     * @param hexColor The hex color code (e.g., "#FF5500" or "FF5500")
     * @return A new RGB color object, or null if the input is invalid
     */
    public static RgbColor hexToRgb(String hexColor) {
        if (hexColor == null || hexColor.isEmpty()) {
            return null;
        }

        // Remove # if present
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }

        // Check length
        if (hexColor.length() != 6) {
            return null;
        }

        try {
            int r = Integer.parseInt(hexColor.substring(0, 2), 16);
            int g = Integer.parseInt(hexColor.substring(2, 4), 16);
            int b = Integer.parseInt(hexColor.substring(4, 6), 16);
            return new RgbColor(r, g, b);
        } catch (NumberFormatException e) {
            return null;
        }
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
        return String.format("#%02X%02X%02X",
                Math.max(0, Math.min(255, r)),
                Math.max(0, Math.min(255, g)),
                Math.max(0, Math.min(255, b)));
    }

    /**
     * Convert RGB values to a hex color code.
     *
     * @param color The RGB color object
     * @return A hex color code string
     */
    public static String rgbToHex(RgbColor color) {
        return rgbToHex(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private Style() {
        // Utility class, prevent instantiation
    }
}