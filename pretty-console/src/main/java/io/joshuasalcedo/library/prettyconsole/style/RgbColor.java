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
public class RgbColor extends AbstractStyleComponent {
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
        this.r = Math.max(0, Math.min(255, r));
        this.g = Math.max(0, Math.min(255, g));
        this.b = Math.max(0, Math.min(255, b));
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
    public boolean isBackground() {
        return background;
    }
}