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
public class Color256 extends AbstractStyleComponent {
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
        this.colorCode = Math.max(0, Math.min(255, colorCode));
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
    public boolean isBackground() {
        return background;
    }
}