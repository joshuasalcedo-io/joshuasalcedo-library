package io.joshuasalcedo.library.prettyconsole.style;

/**
 * Background colors for console output.
 * <p>
 * This enum defines various background colors that can be applied to console text,
 * including standard colors and bright variants. Each color corresponds to an ANSI
 * color code.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public enum BackgroundColor implements Color {
    // Standard backgrounds
    BLACK(40, "Black background"),
    RED(41, "Red background"),
    GREEN(42, "Green background"),
    YELLOW(43, "Yellow background"),
    BLUE(44, "Blue background"),
    PURPLE(45, "Purple/magenta background"),
    CYAN(46, "Cyan background"),
    WHITE(47, "White background"),
    DEFAULT(49, "Default terminal background color"),

    // Bright backgrounds
    BRIGHT_BLACK(100, "Bright black (gray) background"),
    BRIGHT_RED(101, "Bright red background"),
    BRIGHT_GREEN(102, "Bright green background"),
    BRIGHT_YELLOW(103, "Bright yellow background"),
    BRIGHT_BLUE(104, "Bright blue background"),
    BRIGHT_PURPLE(105, "Bright purple/magenta background"),
    BRIGHT_CYAN(106, "Bright cyan background"),
    BRIGHT_WHITE(107, "Bright white background");

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

    BackgroundColor(int value, String description) {
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
     * Get a BackgroundColor by its ANSI value.
     *
     * @param value The ANSI value
     * @return The matching BackgroundColor or null if not found
     */
    public static BackgroundColor fromValue(int value) {
        for (BackgroundColor bg : values()) {
            if (bg.getValue() == value) {
                return bg;
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
     * @return true (always a background color)
     */
    @Override
    public boolean isBackground() {
        return true;
    }

    /**
     * Convert this color to a background color.
     *
     * @return this (already a background color)
     */
    @Override
    public Color asBackground() {
        return this;
    }

    /**
     * Convert this color to a foreground color.
     *
     * @return the corresponding ForegroundColor
     */
    @Override
    public Color asForeground() {
        // Convert background color to foreground color
        // Foreground colors are typically background - 10
        return ForegroundColor.fromValue(value - 10);
    }

    /**
     * Convert this color to an RGB color.
     *
     * @return an RGB representation of this color
     */
    @Override
    public Color toRgb() {
        // Convert ANSI color to RGB with background=true
        switch (this) {
            case BLACK: return new RgbColor(0, 0, 0, true);
            case RED: return new RgbColor(170, 0, 0, true);
            case GREEN: return new RgbColor(0, 170, 0, true);
            case YELLOW: return new RgbColor(170, 170, 0, true);
            case BLUE: return new RgbColor(0, 0, 170, true);
            case PURPLE: return new RgbColor(170, 0, 170, true);
            case CYAN: return new RgbColor(0, 170, 170, true);
            case WHITE: return new RgbColor(170, 170, 170, true);
            case BRIGHT_BLACK: return new RgbColor(85, 85, 85, true);
            case BRIGHT_RED: return new RgbColor(255, 85, 85, true);
            case BRIGHT_GREEN: return new RgbColor(85, 255, 85, true);
            case BRIGHT_YELLOW: return new RgbColor(255, 255, 85, true);
            case BRIGHT_BLUE: return new RgbColor(85, 85, 255, true);
            case BRIGHT_PURPLE: return new RgbColor(255, 85, 255, true);
            case BRIGHT_CYAN: return new RgbColor(85, 255, 255, true);
            case BRIGHT_WHITE: return new RgbColor(255, 255, 255, true);
            default: return new RgbColor(0, 0, 0, true); // Default to black
        }
    }

    /**
     * Convert this color to a 256-color.
     *
     * @return a 256-color representation of this color
     */
    @Override
    public Color to256Color() {
        // Convert ANSI color to 256-color with background=true
        switch (this) {
            case BLACK: return new Color256(0, true);
            case RED: return new Color256(1, true);
            case GREEN: return new Color256(2, true);
            case YELLOW: return new Color256(3, true);
            case BLUE: return new Color256(4, true);
            case PURPLE: return new Color256(5, true);
            case CYAN: return new Color256(6, true);
            case WHITE: return new Color256(7, true);
            case BRIGHT_BLACK: return new Color256(8, true);
            case BRIGHT_RED: return new Color256(9, true);
            case BRIGHT_GREEN: return new Color256(10, true);
            case BRIGHT_YELLOW: return new Color256(11, true);
            case BRIGHT_BLUE: return new Color256(12, true);
            case BRIGHT_PURPLE: return new Color256(13, true);
            case BRIGHT_CYAN: return new Color256(14, true);
            case BRIGHT_WHITE: return new Color256(15, true);
            default: return new Color256(0, true); // Default to black
        }
    }
}
