package io.joshuasalcedo.library.prettyconsole.style;

import io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils;
import io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils.TerminalCapability;

/**
 * Utility class for color conversion and optimization.
 * <p>
 * This class provides methods to convert between different color formats
 * and to optimize colors based on terminal capabilities.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public final class ColorConverter {

    private ColorConverter() {
        // Utility class, prevent instantiation
    }

    /**
     * Get the best color representation for the current terminal.
     * <p>
     * This method automatically selects the most appropriate color format
     * based on the terminal's capabilities.
     * </p>
     *
     * @param color The color to optimize
     * @return The best color representation for the current terminal
     */
    public static Color getBestColorForTerminal(Color color) {
        if (color == null) {
            return null;
        }

        // If the terminal doesn't support ANSI colors at all, return null
        if (!TerminalUtils.hasCapability(TerminalCapability.ANSI_COLOR)) {
            return null;
        }

        // Get the best color type supported by the terminal
        ColorType bestType = ColorType.getBestSupported();
        
        // Convert the color to the best supported format
        switch (bestType) {
            case RGB:
                return color.toRgb();
            case COLOR_256:
                return color.to256Color();
            default:
                // For ANSI, we need to find the closest named color
                // This depends on the color type
                if (color.getColorType() == ColorType.RGB || color.getColorType() == ColorType.COLOR_256) {
                    // Convert to the closest ANSI color
                    // This is a simplification - a real implementation would find the closest match
                    return color.isBackground() ? BackgroundColor.WHITE : ForegroundColor.WHITE;
                }
                return color;
        }
    }

    /**
     * Find the closest ANSI color to the given RGB values.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @param isBackground Whether to return a background color
     * @return The closest ANSI color
     */
    public static Color findClosestAnsiColor(int r, int g, int b, boolean isBackground) {
        // Simple algorithm to find the closest ANSI color
        // This is a simplification - a real implementation would use color distance formulas
        
        if (r > 192 && g > 192 && b > 192) {
            return isBackground ? BackgroundColor.WHITE : ForegroundColor.WHITE;
        } else if (r > 192 && g > 192) {
            return isBackground ? BackgroundColor.YELLOW : ForegroundColor.YELLOW;
        } else if (r > 192 && b > 192) {
            return isBackground ? BackgroundColor.PURPLE : ForegroundColor.PURPLE;
        } else if (g > 192 && b > 192) {
            return isBackground ? BackgroundColor.CYAN : ForegroundColor.CYAN;
        } else if (r > 192) {
            return isBackground ? BackgroundColor.RED : ForegroundColor.RED;
        } else if (g > 192) {
            return isBackground ? BackgroundColor.GREEN : ForegroundColor.GREEN;
        } else if (b > 192) {
            return isBackground ? BackgroundColor.BLUE : ForegroundColor.BLUE;
        } else {
            return isBackground ? BackgroundColor.BLACK : ForegroundColor.BLACK;
        }
    }
}