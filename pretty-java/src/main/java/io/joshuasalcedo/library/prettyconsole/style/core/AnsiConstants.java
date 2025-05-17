package io.joshuasalcedo.library.prettyconsole.style.core;

/**
 * Core ANSI escape sequence constants for console styling.
 * This class centralizes all ANSI-related constants for the library.
 */
public final class AnsiConstants {
    /**
     * ANSI reset code that returns terminal to default state.
     */
    public static final String RESET = "\033[0m";

    /**
     * ANSI CSI (Control Sequence Introducer) prefix for all ANSI escape sequences.
     */
    public static final String CSI = "\033[";
    
    /**
     * ANSI escape character used in escape sequences.
     */
    public static final char ESC = '\u001B';
    
    /**
     * Regular expression pattern to match ANSI escape sequences.
     */
    public static final String ANSI_PATTERN = ESC + "\\[[;\\d]*[ -/]*[@-~]";
    
    private AnsiConstants() {
        // Prevent instantiation
    }
}