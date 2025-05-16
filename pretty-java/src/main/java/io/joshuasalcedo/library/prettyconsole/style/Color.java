package io.joshuasalcedo.library.prettyconsole.style;

/**
 * Interface for all color implementations in the library.
 * <p>
 * This interface extends StyleComponent and adds methods specific to colors,
 * such as conversion between different color formats and checking if a color
 * is a background color.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public interface Color extends StyleComponent {

    /**
     * Get the type of this color implementation.
     *
     * @return the ColorType of this color
     */
    ColorType getColorType();

    /**
     * Check if this is a background color.
     *
     * @return true if this is a background color, false if it's a foreground color
     */
    boolean isBackground();

    /**
     * Convert this color to a background color.
     *
     * @return a new Color instance configured as a background color
     */
    Color asBackground();

    /**
     * Convert this color to a foreground color.
     *
     * @return a new Color instance configured as a foreground color
     */
    Color asForeground();

    /**
     * Convert this color to an RGB color.
     * <p>
     * If this is already an RGB color, it returns itself.
     * </p>
     *
     * @return an RGB representation of this color
     */
    Color toRgb();

    /**
     * Convert this color to a 256-color.
     * <p>
     * If this is already a 256-color, it returns itself.
     * </p>
     *
     * @return a 256-color representation of this color
     */
    Color to256Color();

    /**
     * Get the best color representation for the current terminal.
     * <p>
     * This method automatically selects the most appropriate color format
     * based on the terminal's capabilities.
     * </p>
     *
     * @return the best color representation for the current terminal
     */
    default Color getBestColorForTerminal() {
        ColorType bestType = ColorType.getBestSupported();

        switch (bestType) {
            case RGB:
                return toRgb();
            case COLOR_256:
                return to256Color();
            default:
                // For ANSI, we need to find the closest named color
                // This will be implemented in concrete classes
                return this;
        }
    }
}
