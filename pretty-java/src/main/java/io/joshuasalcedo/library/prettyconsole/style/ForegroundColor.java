package io.joshuasalcedo.library.prettyconsole.style;

/**
 * Foreground text colors for console output.
 * <p>
 * This enum defines various foreground colors that can be applied to console text,
 * including standard colors and bright variants. Each color corresponds to an ANSI
 * color code.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public enum ForegroundColor implements Color {
    // Standard colors
    BLACK(30, "Black text"),
    RED(31, "Red text"),
    GREEN(32, "Green text"),
    YELLOW(33, "Yellow text"),
    BLUE(34, "Blue text"),
    PURPLE(35, "Purple/magenta text"),
    CYAN(36, "Cyan text"),
    WHITE(37, "White text"),
    DEFAULT(39, "Default terminal foreground color"),

    // Bright colors
    BRIGHT_BLACK(90, "Bright black (gray) text"),
    BRIGHT_RED(91, "Bright red text"),
    BRIGHT_GREEN(92, "Bright green text"),
    BRIGHT_YELLOW(93, "Bright yellow text"),
    BRIGHT_BLUE(94, "Bright blue text"),
    BRIGHT_PURPLE(95, "Bright purple/magenta text"),
    BRIGHT_CYAN(96, "Bright cyan text"),
    BRIGHT_WHITE(97, "Bright white text");

    private final String code;
    private final String description;
    private final int value;

    /**
     * ANSI reset code that returns terminal to default state.
     */
    private static final String RESET = "\033[0m";

    /**
     * ANSI CSI (Control Sequence Introducer) prefix for all ANSI escape sequences.
     */
    private static final String CSI = "\033[";

    ForegroundColor(int value, String description) {
        this.value = value;
        this.code = CSI + value + "m";
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String apply(String text) {
        return getCode() + text + RESET;
    }

    @Override
    public String safeApply(String text) {
        return io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils.isAnsiSupported() ? apply(text) : text;
    }

    /**
     * Get the ANSI code value as an integer.
     *
     * @return The ANSI code value
     */
    public int getValue() {
        return value;
    }

    /**
     * Get a ForegroundColor by its ANSI value.
     *
     * @param value The ANSI value
     * @return The matching ForegroundColor or null if not found
     */
    public static ForegroundColor fromValue(int value) {
        for (ForegroundColor color : values()) {
            if (color.getValue() == value) {
                return color;
            }
        }
        return null;
    }

    /**
     * Get the color type of this color.
     *
     * @return ColorType.ANSI
     */
    @Override
    public ColorType getColorType() {
        return ColorType.ANSI;
    }

    /**
     * Check if this is a background color.
     *
     * @return false (always a foreground color)
     */
    @Override
    public boolean isBackground() {
        return false;
    }

    /**
     * Convert this color to a background color.
     *
     * @return the corresponding BackgroundColor
     */
    @Override
    public Color asBackground() {
        // Convert foreground color to background color
        // Background colors are typically foreground + 10
        return BackgroundColor.fromValue(value + 10);
    }

    /**
     * Convert this color to a foreground color.
     *
     * @return this (already a foreground color)
     */
    @Override
    public Color asForeground() {
        return this;
    }

    /**
     * Convert this color to an RGB color.
     *
     * @return an RGB representation of this color
     */
    @Override
    public Color toRgb() {
        // Convert ANSI color to RGB
        switch (this) {
            case BLACK: return new RgbColor(0, 0, 0);
            case RED: return new RgbColor(170, 0, 0);
            case GREEN: return new RgbColor(0, 170, 0);
            case YELLOW: return new RgbColor(170, 170, 0);
            case BLUE: return new RgbColor(0, 0, 170);
            case PURPLE: return new RgbColor(170, 0, 170);
            case CYAN: return new RgbColor(0, 170, 170);
            case WHITE: return new RgbColor(170, 170, 170);
            case BRIGHT_BLACK: return new RgbColor(85, 85, 85);
            case BRIGHT_RED: return new RgbColor(255, 85, 85);
            case BRIGHT_GREEN: return new RgbColor(85, 255, 85);
            case BRIGHT_YELLOW: return new RgbColor(255, 255, 85);
            case BRIGHT_BLUE: return new RgbColor(85, 85, 255);
            case BRIGHT_PURPLE: return new RgbColor(255, 85, 255);
            case BRIGHT_CYAN: return new RgbColor(85, 255, 255);
            case BRIGHT_WHITE: return new RgbColor(255, 255, 255);
            default: return new RgbColor(255, 255, 255); // Default to white
        }
    }

    /**
     * Convert this color to a 256-color.
     *
     * @return a 256-color representation of this color
     */
    @Override
    public Color to256Color() {
        // Convert ANSI color to 256-color
        switch (this) {
            case BLACK: return new Color256(0);
            case RED: return new Color256(1);
            case GREEN: return new Color256(2);
            case YELLOW: return new Color256(3);
            case BLUE: return new Color256(4);
            case PURPLE: return new Color256(5);
            case CYAN: return new Color256(6);
            case WHITE: return new Color256(7);
            case BRIGHT_BLACK: return new Color256(8);
            case BRIGHT_RED: return new Color256(9);
            case BRIGHT_GREEN: return new Color256(10);
            case BRIGHT_YELLOW: return new Color256(11);
            case BRIGHT_BLUE: return new Color256(12);
            case BRIGHT_PURPLE: return new Color256(13);
            case BRIGHT_CYAN: return new Color256(14);
            case BRIGHT_WHITE: return new Color256(15);
            default: return new Color256(7); // Default to white
        }
    }
}
