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
public enum ForegroundColor implements StyleComponent {
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
}