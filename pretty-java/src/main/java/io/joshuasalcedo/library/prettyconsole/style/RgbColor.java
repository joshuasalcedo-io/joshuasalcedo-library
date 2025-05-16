package io.joshuasalcedo.library.prettyconsole.style;

/**
 * 24-bit RGB color wrapper for text styling.
 * <p>
 * This class provides support for 24-bit RGB colors in terminals that support them.
 * It can be used for both foreground and background colors.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public class RgbColor extends AbstractStyleComponent implements Color {
    private final int r;
    private final int g;
    private final int b;
    private final boolean background;

    /**
     * Create a new foreground RGB color.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     */
    public RgbColor(int r, int g, int b) {
        this(r, g, b, false);
    }

    /**
     * Create a new RGB color.
     *
     * @param r          Red component (0-255)
     * @param g          Green component (0-255)
     * @param b          Blue component (0-255)
     * @param background Whether this is a background color
     */
    public RgbColor(int r, int g, int b, boolean background) {
        this.r = Math.clamp(r, 0, 255);
        this.g = Math.clamp(g, 0, 255);
        this.b = Math.clamp(b, 0, 255);
        this.background = background;
    }

    @Override
    public String getCode() {
        return CSI + (background ? "48" : "38") + ";2;" + r + ";" + g + ";" + b + "m";
    }

    @Override
    public String getDescription() {
        return (background ? "Background" : "Foreground") + " RGB color (" + r + "," + g + "," + b + ")";
    }

    /**
     * Convert this RGB color to a background color.
     *
     * @return A new RgbColor configured as a background color
     */
    public RgbColor asBackground() {
        return new RgbColor(r, g, b, true);
    }

    /**
     * Get the red component.
     *
     * @return Red value (0-255)
     */
    public int getRed() {
        return r;
    }

    /**
     * Get the green component.
     *
     * @return Green value (0-255)
     */
    public int getGreen() {
        return g;
    }

    /**
     * Get the blue component.
     *
     * @return Blue value (0-255)
     */
    public int getBlue() {
        return b;
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
     * @return ColorType.RGB
     */
    @Override
    public ColorType getColorType() {
        return ColorType.RGB;
    }

    /**
     * Convert this color to a foreground color.
     *
     * @return A new RgbColor configured as a foreground color
     */
    @Override
    public Color asForeground() {
        return background ? new RgbColor(r, g, b, false) : this;
    }

    /**
     * Convert this color to an RGB color.
     * <p>
     * Since this is already an RGB color, it returns itself.
     * </p>
     *
     * @return this RGB color
     */
    @Override
    public Color toRgb() {
        return this;
    }

    /**
     * Convert this RGB color to a 256-color.
     * <p>
     * This is an approximation, as 256-color mode has limited color range.
     * </p>
     *
     * @return a 256-color approximation of this RGB color
     */
    @Override
    public Color to256Color() {
        // Convert RGB to the closest 256-color value
        // This is a simple approximation algorithm

        if (r == g && g == b) {
            // Grayscale
            if (r == 0) return new Color256(16, background); // Black
            if (r == 255) return new Color256(231, background); // White

            // Find closest gray (24 levels of gray from index 232 to 255)
            int grayIndex = 232 + (int)Math.round((r / 255.0) * 23);
            return new Color256(grayIndex, background);
        }

        // Color cube (6x6x6 = 216 colors from index 16 to 231)
        int rIndex = (int)Math.round((r / 255.0) * 5);
        int gIndex = (int)Math.round((g / 255.0) * 5);
        int bIndex = (int)Math.round((b / 255.0) * 5);

        int colorIndex = 16 + (36 * rIndex) + (6 * gIndex) + bIndex;
        return new Color256(colorIndex, background);
    }
}
