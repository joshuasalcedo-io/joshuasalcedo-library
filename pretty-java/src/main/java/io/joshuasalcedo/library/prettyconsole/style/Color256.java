package io.joshuasalcedo.library.prettyconsole.style;

/**
 * 8-bit (256 color) wrapper for text styling.
 * <p>
 * This class provides support for 8-bit (256 color) colors in terminals that support them.
 * It can be used for both foreground and background colors.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public class Color256 extends AbstractStyleComponent implements Color {
    private final int colorCode;
    private final boolean background;

    /**
     * Create a new foreground 8-bit color.
     *
     * @param colorCode Color code (0-255)
     */
    public Color256(int colorCode) {
        this(colorCode, false);
    }

    /**
     * Create a new 8-bit color.
     *
     * @param colorCode  Color code (0-255)
     * @param background Whether this is a background color
     */
    public Color256(int colorCode, boolean background) {
        this.colorCode = Math.clamp(colorCode, 0, 255);
        this.background = background;
    }

    @Override
    public String getCode() {
        return CSI + (background ? "48" : "38") + ";5;" + colorCode + "m";
    }

    @Override
    public String getDescription() {
        return (background ? "Background" : "Foreground") + " 8-bit color (" + colorCode + ")";
    }

    /**
     * Convert this 8-bit color to a background color.
     *
     * @return A new Color256 configured as a background color
     */
    public Color256 asBackground() {
        return new Color256(colorCode, true);
    }

    /**
     * Get the color code.
     *
     * @return Color code (0-255)
     */
    public int getColorCode() {
        return colorCode;
    }

    /**
     * Check if this is a background color.
     *
     * @return true if this is a background color, false if it's a foreground color
     */
    @Override
    public boolean isBackground() {
        return background;
    }

    /**
     * Get the color type of this color.
     *
     * @return ColorType.COLOR_256
     */
    @Override
    public ColorType getColorType() {
        return ColorType.COLOR_256;
    }

    /**
     * Convert this color to a foreground color.
     *
     * @return A new Color256 configured as a foreground color
     */
    @Override
    public Color asForeground() {
        return background ? new Color256(colorCode, false) : this;
    }

    /**
     * Convert this 256-color to an RGB color.
     * <p>
     * This is an approximation based on the standard 256-color palette.
     * </p>
     *
     * @return an RGB representation of this 256-color
     */
    @Override
    public Color toRgb() {
        // Convert 256-color to RGB
        // This is a simple approximation based on the standard 256-color palette

        if (colorCode < 16) {
            // Standard ANSI colors (0-15)
            switch (colorCode) {
                case 0: return new RgbColor(0, 0, 0, background); // Black
                case 1: return new RgbColor(128, 0, 0, background); // Red
                case 2: return new RgbColor(0, 128, 0, background); // Green
                case 3: return new RgbColor(128, 128, 0, background); // Yellow
                case 4: return new RgbColor(0, 0, 128, background); // Blue
                case 5: return new RgbColor(128, 0, 128, background); // Magenta
                case 6: return new RgbColor(0, 128, 128, background); // Cyan
                case 7: return new RgbColor(192, 192, 192, background); // White
                case 8: return new RgbColor(128, 128, 128, background); // Bright Black (Gray)
                case 9: return new RgbColor(255, 0, 0, background); // Bright Red
                case 10: return new RgbColor(0, 255, 0, background); // Bright Green
                case 11: return new RgbColor(255, 255, 0, background); // Bright Yellow
                case 12: return new RgbColor(0, 0, 255, background); // Bright Blue
                case 13: return new RgbColor(255, 0, 255, background); // Bright Magenta
                case 14: return new RgbColor(0, 255, 255, background); // Bright Cyan
                case 15: return new RgbColor(255, 255, 255, background); // Bright White
                default: return new RgbColor(0, 0, 0, background); // Fallback
            }
        } else if (colorCode < 232) {
            // 6x6x6 color cube (16-231)
            int index = colorCode - 16;
            int r = (index / 36) * 51;
            int g = ((index % 36) / 6) * 51;
            int b = (index % 6) * 51;

            // Adjust for the fact that the values are 0, 95, 135, 175, 215, 255
            if (r > 0) r = 55 + r;
            if (g > 0) g = 55 + g;
            if (b > 0) b = 55 + b;

            return new RgbColor(r, g, b, background);
        } else {
            // Grayscale (232-255)
            int gray = (colorCode - 232) * 10 + 8;
            return new RgbColor(gray, gray, gray, background);
        }
    }

    /**
     * Convert this color to a 256-color.
     * <p>
     * Since this is already a 256-color, it returns itself.
     * </p>
     *
     * @return this 256-color
     */
    @Override
    public Color to256Color() {
        return this;
    }
}
