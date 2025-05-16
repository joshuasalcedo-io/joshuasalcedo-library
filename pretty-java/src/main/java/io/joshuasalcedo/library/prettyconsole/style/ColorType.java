package io.joshuasalcedo.library.prettyconsole.style;

import io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils;
import io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils.TerminalCapability;

/**
 * Enum representing different types of color support in terminals.
 * <p>
 * This enum is used to categorize colors by their implementation type and
 * to help with conversion between different color formats based on terminal capabilities.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public enum ColorType {
    /**
     * Basic ANSI colors (16 colors: 8 standard + 8 bright).
     * These are the most widely supported colors.
     */
    ANSI(TerminalCapability.ANSI_COLOR),

    /**
     * 256-color mode (8-bit colors).
     * Supported by most modern terminals.
     */
    COLOR_256(TerminalCapability.COLOR_256),

    /**
     * 24-bit true color (RGB).
     * The most advanced color support, allowing for 16.7 million colors.
     */
    RGB(TerminalCapability.RGB_COLOR);

    private final TerminalCapability requiredCapability;

    ColorType(TerminalCapability requiredCapability) {
        this.requiredCapability = requiredCapability;
    }

    /**
     * Check if this color type is supported by the current terminal.
     *
     * @return true if the color type is supported, false otherwise
     */
    public boolean isSupported() {
        return TerminalUtils.hasCapability(requiredCapability);
    }

    /**
     * Get the most advanced color type supported by the current terminal.
     *
     * @return the most advanced supported color type, defaulting to ANSI if nothing else is supported
     */
    public static ColorType getBestSupported() {
        if (TerminalUtils.hasCapability(TerminalCapability.RGB_COLOR)) {
            return RGB;
        } else if (TerminalUtils.hasCapability(TerminalCapability.COLOR_256)) {
            return COLOR_256;
        } else {
            return ANSI;
        }
    }
}