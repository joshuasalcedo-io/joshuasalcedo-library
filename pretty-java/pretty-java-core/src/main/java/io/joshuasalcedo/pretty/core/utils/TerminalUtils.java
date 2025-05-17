package io.joshuasalcedo.pretty.core.utils;

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
                return majorVersion >= 10;
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
                return 80; // Default for Windows if can't determine
            }

            // On Unix-like systems, try to use stty command
            // This is a simplified approach - in a real implementation,
            // you might use ProcessBuilder to execute 'stty size'
            return 80; // Default for Unix if can't determine
        } catch (Exception e) {
            return 80; // Default fallback
        }
    }
}
