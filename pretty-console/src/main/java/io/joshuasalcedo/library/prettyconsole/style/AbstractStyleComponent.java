package io.joshuasalcedo.library.prettyconsole.style;

import io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils;

/**
 * Abstract base class for styling components.
 * <p>
 * This class provides common functionality for styling components, including
 * applying styles to text and checking for terminal support. Concrete styling
 * components should extend this class and implement the getCode() and getDescription()
 * methods.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public abstract class AbstractStyleComponent implements StyleComponent {

    /**
     * ANSI reset code that returns terminal to default state.
     */
    protected static final String RESET = "\033[0m";

    /**
     * ANSI CSI (Control Sequence Introducer) prefix for all ANSI escape sequences.
     */
    protected static final String CSI = "\033[";

    @Override
    public String apply(String text) {
        return getCode() + text + RESET;
    }

    @Override
    public String safeApply(String text) {
        return TerminalUtils.isAnsiSupported() ? apply(text) : text;
    }
}