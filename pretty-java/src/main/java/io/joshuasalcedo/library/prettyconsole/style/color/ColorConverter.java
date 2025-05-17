package io.joshuasalcedo.library.prettyconsole.style.color;

import io.joshuasalcedo.library.prettyconsole.style.RgbColor;

/**
 * Utility class for color conversions between different formats.
 */
public final class ColorConverter {
    private static final int HEX_COLOR_LENGTH = 6;
    private static final int RGB_MIN_VALUE = 0;
    private static final int RGB_MAX_VALUE = 255;
    
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
        if (hexColor.length() != HEX_COLOR_LENGTH) {
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
                Math.clamp(r, RGB_MIN_VALUE, RGB_MAX_VALUE),
                Math.clamp(g, RGB_MIN_VALUE, RGB_MAX_VALUE),
                Math.clamp(b, RGB_MIN_VALUE, RGB_MAX_VALUE));
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
     * Interpolate between two RGB colors based on a ratio.
     *
     * @param startColor The starting color
     * @param endColor The ending color
     * @param ratio The interpolation ratio (0.0 to 1.0)
     * @return The interpolated RGB color
     */
    public static RgbColor interpolateColor(RgbColor startColor, RgbColor endColor, double ratio) {
        int r = (int) (startColor.getRed() + ratio * (endColor.getRed() - startColor.getRed()));
        int g = (int) (startColor.getGreen() + ratio * (endColor.getGreen() - startColor.getGreen()));
        int b = (int) (startColor.getBlue() + ratio * (endColor.getBlue() - startColor.getBlue()));
        return new RgbColor(r, g, b);
    }
    
    private ColorConverter() {
        // Prevent instantiation
    }
}