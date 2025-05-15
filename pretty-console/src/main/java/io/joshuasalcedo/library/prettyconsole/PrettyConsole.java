package io.joshuasalcedo.library.prettyconsole;

import io.joshuasalcedo.library.prettyconsole.style.BackgroundColor;
import io.joshuasalcedo.library.prettyconsole.style.Color256;
import io.joshuasalcedo.library.prettyconsole.style.ForegroundColor;
import io.joshuasalcedo.library.prettyconsole.style.RgbColor;
import io.joshuasalcedo.library.prettyconsole.style.Style;
import io.joshuasalcedo.library.prettyconsole.style.StyleComponent;
import io.joshuasalcedo.library.prettyconsole.style.StyleFormatter;
import io.joshuasalcedo.library.prettyconsole.style.StringDecorator;
import io.joshuasalcedo.library.prettyconsole.style.TextStyle;
import io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils;

/**
 * Enhanced terminal formatting system for console output styling.
 * <p>
 * This class provides a modular and flexible approach to console formatting
 * with support for basic ANSI codes, RGB colors, and common console patterns.
 * </p>
 * <p>
 * Usage examples:
 * </p>
 * <pre>
 * // Apply a single style
 * System.out.println(PrettyConsole.apply(ForegroundColor.RED, "This text is red!"));
 *
 * // Combine multiple styles
 * StyleFormatter formatter = new StyleFormatter()
 *     .withStyle(TextStyle.BOLD)
 *     .withColor(ForegroundColor.BLUE)
 *     .withBackground(BackgroundColor.BRIGHT_WHITE);
 * System.out.println(PrettyConsole.apply(formatter, "Bold blue text on bright white background"));
 *
 * // Using RGB colors
 * System.out.println(PrettyConsole.rgb(255, 100, 50).apply("Custom orange text"));
 *
 * // Using fluent string extension methods
 * String result = PrettyConsole.decorate("Warning:")
 *                          .style(TextStyle.BOLD)
 *                          .color(ForegroundColor.YELLOW)
 *                          .bg(BackgroundColor.BLACK)
 *                          .text();
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public final class PrettyConsole {

    /**
     * Resets all formatting and returns to default console output.
     * Should be used after any formatting sequence.
     */
    public static final String RESET = "\033[0m";

    /**
     * ANSI CSI (Control Sequence Introducer) prefix for all ANSI escape sequences
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
        return Style.apply(component, text);
    }

    /**
     * Apply formatting to the given text.
     *
     * @param formatter The formatter with combined formatting codes
     * @param text      The text to format
     * @return The formatted text with reset code at the end
     */
    public static String apply(StyleFormatter formatter, String text) {
        return Style.apply(formatter, text);
    }

    /**
     * Safely apply formatting to the given text if the terminal supports ANSI codes.
     *
     * @param component The style component to apply
     * @param text      The text to format
     * @return The formatted text if supported, the original text otherwise
     */
    public static String safeApply(StyleComponent component, String text) {
        return Style.safeApply(component, text);
    }

    /**
     * Safely apply formatting to the given text if the terminal supports ANSI codes.
     *
     * @param formatter The formatter with combined formatting codes
     * @param text      The text to format
     * @return The formatted text if supported, the original text otherwise
     */
    public static String safeApply(StyleFormatter formatter, String text) {
        return Style.safeApply(formatter, text);
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
        return text.replaceAll("\u001B\\[[;\\d]*[ -/]*[@-~]", "");
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

    // Private constructor to prevent instantiation
    private PrettyConsole() {
        // Utility class, prevent instantiation
    }
}
