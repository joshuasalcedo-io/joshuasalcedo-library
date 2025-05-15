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
public enum BackgroundColor implements StyleComponent {
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
}