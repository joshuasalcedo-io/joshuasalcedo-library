package io.joshuasalcedo.library.prettyconsole.style;

/**
 * Text styles for console output.
 * <p>
 * This enum defines various text styles that can be applied to console output,
 * such as bold, italic, underline, etc. Each style corresponds to an ANSI
 * formatting code.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public enum TextStyle implements StyleComponent {
    NORMAL(0, "Normal/default text style"),
    BOLD(1, "Bold/bright text"),
    DIM(2, "Dimmed text (not widely supported)"),
    ITALIC(3, "Italic text (not widely supported)"),
    UNDERLINE(4, "Underlined text"),
    BLINK(5, "Blinking text (not widely supported)"),
    REVERSE(7, "Reversed colors (swaps foreground/background)"),
    HIDDEN(8, "Hidden/invisible text (not widely supported)"),
    STRIKETHROUGH(9, "Strikethrough text (not widely supported)");

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

    TextStyle(int value, String description) {
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
}