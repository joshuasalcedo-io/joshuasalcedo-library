package io.joshuasalcedo.library.prettyconsole.utils;

/**
 * Utility class for terminal capability detection.
 * <p>
 * This class provides methods to detect terminal capabilities such as ANSI color support,
 * terminal type, and operating system information. It helps the library gracefully degrade
 * when certain features aren't supported.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public final class TerminalUtils {

    /**
     * Default terminal width used when the actual width cannot be determined
     */
    private static final int DEFAULT_TERMINAL_WIDTH = 80;

    /**
     * Major version number for Windows 10
     */
    private static final int WINDOWS_10_VERSION = 10;

    private TerminalUtils() {
        // Utility class, prevent instantiation
    }

    /**
     * Utility method to check if the current terminal supports ANSI colors.
     *
     * @return true if ANSI formatting is supported, false otherwise
     * @since 1.0.0
     */
    public static boolean isAnsiSupported() {
        // Check operating system
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isWindows = osName.contains("win");

        // Environment variables for color support
        String term = System.getenv("TERM");
        String colorterm = System.getenv("COLORTERM");
        String ansicon = System.getenv("ANSICON");
        String conEmuANSI = System.getenv("ConEmuANSI");
        String wt = System.getenv("WT_SESSION"); // Windows Terminal
        String vscodeTerminal = System.getenv("VSCODE_INJECTION"); // VSCode terminal

        // Check for WSL (Windows Subsystem for Linux)
        boolean isWsl =
            System.getProperty("os.version").toLowerCase().contains("microsoft") ||
            (term != null && term.contains("wsl"));

        // Modern Windows terminals with ANSI support
        if (isWindows) {
            // Windows Terminal, ConEmu, ANSICON, VSCode integrated terminal, or recent Windows 10+
            if (wt != null || conEmuANSI != null || ansicon != null || vscodeTerminal != null || isWindows10OrLater()) {
                return true;
            }
        }

        // WSL always has color support
        if (isWsl) {
            return true;
        }

        // Check for explicit color support via COLORTERM
        if (colorterm != null) {
            return true; // Any value in COLORTERM indicates support
        }

        // Check for terminals known to support colors
        if (term != null) {
            return (
                term.contains("color") ||
                term.contains("xterm") ||
                term.contains("ansi") ||
                term.contains("linux") ||
                term.contains("cygwin") ||
                term.contains("screen") ||
                term.contains("rxvt") ||
                term.contains("konsole") ||
                term.contains("gnome") ||
                term.contains("vt100") ||
                term.contains("vt220") ||
                term.contains("tmux")
            );
        }

        // For non-Windows systems, assume modern terminals support colors by default
        return !isWindows;
    }

    /**
     * Check if the current terminal supports 256 colors (8-bit color).
     * <p>
     * This is a more advanced color capability than basic ANSI colors.
     * </p>
     *
     * @return true if 256-color support is available, false otherwise
     * @since 2.0.0
     */
    public static boolean is256ColorSupported() {
        if (!isAnsiSupported()) {
            return false;
        }

        String term = System.getenv("TERM");
        String colorterm = System.getenv("COLORTERM");

        // Check for terminals known to support 256 colors
        if (term != null) {
            return term.contains("256color") || 
                   term.contains("xterm") || 
                   term.contains("screen") || 
                   term.contains("tmux");
        }

        // Check for explicit color support via COLORTERM
        if (colorterm != null) {
            return colorterm.contains("256color") || 
                   colorterm.contains("truecolor") || 
                   colorterm.contains("24bit");
        }

        // Default to false for older terminals
        return false;
    }

    /**
     * Check if the current terminal supports 24-bit true color (RGB).
     * <p>
     * This is the most advanced color capability, allowing for 16.7 million colors.
     * </p>
     *
     * @return true if RGB color support is available, false otherwise
     * @since 2.0.0
     */
    public static boolean isRgbColorSupported() {
        if (!isAnsiSupported()) {
            return false;
        }

        String colorterm = System.getenv("COLORTERM");

        // Check for explicit truecolor support
        if (colorterm != null) {
            return colorterm.contains("truecolor") || colorterm.contains("24bit");
        }

        // Check for terminals known to support truecolor
        String term = System.getenv("TERM");
        if (term != null) {
            return term.contains("truecolor") || term.contains("24bit");
        }

        // Modern Windows Terminal supports RGB
        String wt = System.getenv("WT_SESSION");
        if (wt != null) {
            return true;
        }

        // Default to false for older terminals
        return false;
    }

    /**
     * Check if the operating system is Windows 10 or later, which has improved ANSI color support.
     *
     * @return true if Windows 10 or later
     * @since 1.0.0
     */
    private static boolean isWindows10OrLater() {
        String osVersion = System.getProperty("os.version");
        try {
            String[] versionParts = osVersion.split("\\.");
            if (versionParts.length > 0) {
                int majorVersion = Integer.parseInt(versionParts[0]);
                return majorVersion >= WINDOWS_10_VERSION;
            }
        } catch (NumberFormatException e) {
            // Ignore error
        }
        return false;
    }

    /**
     * Gets the terminal width if it can be determined.
     *
     * @return the terminal width in characters, or 80 if it cannot be determined
     * @since 1.0.0
     */
    public static int getTerminalWidth() {
        // Try to get terminal width from environment
        try {
            String columns = System.getenv("COLUMNS");
            if (columns != null && !columns.isEmpty()) {
                return Integer.parseInt(columns);
            }

            // On Windows, try to use the console API
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // This is a simplified approach - in a real implementation,
                // you might use JNA to call the Windows Console API
                return DEFAULT_TERMINAL_WIDTH; // Default for Windows if can't determine
            }

            // On Unix-like systems, try to use stty command
            // This is a simplified approach - in a real implementation,
            // you might use ProcessBuilder to execute 'stty size'
            return DEFAULT_TERMINAL_WIDTH; // Default for Unix if can't determine
        } catch (Exception e) {
            return DEFAULT_TERMINAL_WIDTH; // Default fallback
        }
    }

    /**
     * Enum representing different terminal capability types.
     * <p>
     * This enum provides a convenient way to check for specific terminal capabilities.
     * </p>
     *
     * @since 2.0.0
     */
    public enum TerminalCapability {
        /**
         * Basic ANSI color and formatting support.
         */
        ANSI_COLOR,

        /**
         * 256-color (8-bit) support.
         */
        COLOR_256,

        /**
         * 24-bit true color (RGB) support.
         */
        RGB_COLOR
    }

    /**
     * Check if the terminal has a specific capability.
     * <p>
     * This method provides a unified API for checking terminal capabilities.
     * </p>
     *
     * @param capability The capability to check for
     * @return true if the capability is supported, false otherwise
     * @since 2.0.0
     */
    public static boolean hasCapability(TerminalCapability capability) {
        return switch (capability) {
            case ANSI_COLOR -> isAnsiSupported();
            case COLOR_256 -> is256ColorSupported();
            case RGB_COLOR -> isRgbColorSupported();
        };
    }
}
