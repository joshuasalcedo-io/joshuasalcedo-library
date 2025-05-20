package io.joshuasalcedo.pretty.core.model.stream;

import io.joshuasalcedo.pretty.core.model.file.PrettyFile;
import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.theme.TerminalStyle;
import io.joshuasalcedo.pretty.core.utils.TerminalUtils;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static io.joshuasalcedo.pretty.core.utils.RGBColorCalculator.ANSI_RESET;

/**
 * A static utility class that provides access to PrettyPrintStream functionality
 * without requiring manual instantiation.
 * <p>
 * This class follows a similar pattern to Java's System class, providing
 * static methods and fields for standard output and error streams with
 * RGB color formatting capabilities.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Print with colors
 * PrettySystem.out.println(RGBColor.of(255, 0, 0), "This text is red!");
 *
 * // Print with styles
 * PrettySystem.out.bold(true).println("This text is bold");
 *
 * // Print with terminal styles
 * PrettySystem.out.println(TerminalStyle.ERROR, "This is an error message");
 *
 * // Or use direct static methods
 * PrettySystem.println(RGBColor.of(0, 255, 0), "This is green text");
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.2.0
 */
public final class PrettySystem {
    /**
     * The standard PrettyPrintStream instance for output
     */
    public static final PrettyPrintStream out;

    /**
     * The standard PrettyPrintStream instance for error output
     */
    public static final PrettyPrintStream err;

    // Static initialization block to set up the streams
    static {
        out = new PrettyPrintStream(System.out);
        err = new PrettyPrintStream(System.err);
    }

    // Private constructor to prevent instantiation
    private PrettySystem() {
        throw new UnsupportedOperationException("PrettySystem is a utility class and cannot be instantiated");
    }

    // Static methods for convenience

    /**
     * Prints a formatted string to the standard output stream.
     *
     * @param text The text to print
     */
    public static void print(String text) {
        out.print(text);
    }

    /**
     * Prints a formatted string followed by a line break to the standard output stream.
     *
     * @param text The text to print
     */
    public static void println(String text) {
        out.println(text);
    }

    /**
     * Prints an empty line to the standard output stream.
     */
    public static void println() {
        out.println();
    }

    /**
     * Prints a formatted string using the specified color to the standard output stream.
     *
     * @param color The RGB color to apply
     * @param text The text to print
     */
    public static void print(RGBColor color, String text) {
        out.print(color, text);
    }

    /**
     * Prints a formatted string using the specified color followed by a line break
     * to the standard output stream.
     *
     * @param color The RGB color to apply
     * @param text The text to print
     */
    public static void println(RGBColor color, String text) {
        out.println(color, text);
    }

    /**
     * Prints a formatted string using the specified terminal style to the standard output stream.
     *
     * @param style The terminal style to apply
     * @param text The text to print
     */
    public static void print(TerminalStyle style, String text) {
        out.print(style, text);
    }

    /**
     * Prints a formatted string using the specified terminal style followed by a line break
     * to the standard output stream.
     *
     * @param style The terminal style to apply
     * @param text The text to print
     */
    public static void println(TerminalStyle style, String text) {
        out.println(style, text);
    }

    /**
     * Prints a formatted string with the specified foreground and background colors.
     *
     * @param fg The foreground color
     * @param bg The background color
     * @param text The text to print
     */
    public static void print(RGBColor fg, RGBColor bg, String text) {
        out.print(fg, bg, text);
    }

    /**
     * Prints a formatted string with the specified foreground and background colors
     * followed by a line break.
     *
     * @param fg The foreground color
     * @param bg The background color
     * @param text The text to print
     */
    public static void println(RGBColor fg, RGBColor bg, String text) {
        out.println(fg, bg, text);
    }


    // Additional static methods for error stream

    /**
     * Prints a formatted string to the standard error stream.
     *
     * @param text The text to print
     */
    public static void printErr(String text) {
        err.print(text);
    }

    /**
     * Prints a formatted string followed by a line break to the standard error stream.
     *
     * @param text The text to print
     */
    public static void printlnErr(String text) {
        err.println(text);
    }

    /**
     * Prints a formatted string using the specified color to the standard error stream.
     *
     * @param color The RGB color to apply
     * @param text The text to print
     */
    public static void printErr(RGBColor color, String text) {
        err.print(color, text);
    }

    /**
     * Prints a formatted string using the specified color followed by a line break
     * to the standard error stream.
     *
     * @param color The RGB color to apply
     * @param text The text to print
     */
    public static void printlnErr(RGBColor color, String text) {
        err.println(color, text);
    }

    /**
     * Prints a formatted string using the specified terminal style to the standard error stream.
     *
     * @param style The terminal style to apply
     * @param text The text to print
     */
    public static void printErr(TerminalStyle style, String text) {
        err.print(style, text);
    }

    /**
     * Prints a formatted string using the specified terminal style followed by a line break
     * to the standard error stream.
     *
     * @param style The terminal style to apply
     * @param text The text to print
     */
    public static void printlnErr(TerminalStyle style, String text) {
        err.println(style, text);
    }

    // Styling methods that return the stream for method chaining

    /**
     * Sets the foreground (text) color for subsequent output.
     *
     * @param color The RGB color to apply to the text
     * @return The standard output stream for method chaining
     */
    public static PrettyPrintStream foreground(RGBColor color) {
        return out.foreground(color);
    }

    /**
     * Sets the background color for subsequent output.
     *
     * @param color The RGB color to apply to the background
     * @return The standard output stream for method chaining
     */
    public static PrettyPrintStream background(RGBColor color) {
        return out.background(color);
    }

    /**
     * Sets a predefined terminal style for subsequent output.
     *
     * @param style The terminal style to apply
     * @return The standard output stream for method chaining
     */
    public static PrettyPrintStream style(TerminalStyle style) {
        return out.style(style);
    }

    /**
     * Enables or disables bold text for subsequent output.
     *
     * @param enabled Whether bold formatting should be enabled
     * @return The standard output stream for method chaining
     */
    public static PrettyPrintStream bold(boolean enabled) {
        return out.bold(enabled);
    }

    /**
     * Enables or disables italic text for subsequent output.
     *
     * @param enabled Whether italic formatting should be enabled
     * @return The standard output stream for method chaining
     */
    public static PrettyPrintStream italic(boolean enabled) {
        return out.italic(enabled);
    }

    /**
     * Enables or disables underlined text for subsequent output.
     *
     * @param enabled Whether underline formatting should be enabled
     * @return The standard output stream for method chaining
     */
    public static PrettyPrintStream underline(boolean enabled) {
        return out.underline(enabled);
    }

    // Utility methods for common formatting tasks

    /**
     * Returns a PrettyPrintStream wrapper for the provided OutputStream.
     *
     * @param out The OutputStream to wrap
     * @return A new PrettyPrintStream instance
     */
    public static PrettyPrintStream wrap(OutputStream out) {
        return new PrettyPrintStream(out);
    }

    /**
     * Resets the formatting of the standard output stream.
     *
     * @return The standard output stream for method chaining
     */
    public static PrettyPrintStream reset() {
        return out.reset();
    }

    /**
     * Resets the formatting of the standard error stream.
     *
     * @return The standard error stream for method chaining
     */
    public static PrettyPrintStream resetErr() {
        return err.reset();
    }

    /**
     * Demonstrates the PrettySystem functionality.
     */
    public static void main(String[] args) {
        System.out.println("=== PrettySystem Demo ===\n");

        // Demonstrate static methods
        PrettySystem.println("Standard text using PrettySystem");
        PrettySystem.println(RGBColor.of(255, 0, 0), "Red text using PrettySystem");
        PrettySystem.println(TerminalStyle.SUCCESS, "Success message using PrettySystem");

        // Demonstrate static methods with method chaining
        PrettySystem.bold(true).foreground(RGBColor.of(0, 128, 255)).println("Bold blue text using method chaining");
        PrettySystem.reset();

        // Demonstrate static 'out' field with method chaining
        PrettySystem.out.bold(true).foreground(RGBColor.of(0, 255, 128)).println("Bold green text using PrettySystem.out");
        PrettySystem.out.reset();

        // Demonstrate background colors
        PrettySystem.println(RGBColor.of(255, 255, 255), RGBColor.of(0, 0, 128), "White text on navy background");

        // Demonstrate error output
        PrettySystem.printlnErr(TerminalStyle.ERROR, "Error message using PrettySystem error stream");

        // Demonstrate hyperlinks
        PrettySystem.println("\nHyperlink example:");
        PrettySystem.printHyperlink("https://github.com/joshuasalcedo-io/joshuasalcedo-library");

        // Show file tree demo using PrettySystem
        PrettySystem.println("\nFile Tree Demo with PrettySystem:");
        PrettySystem.style(TerminalStyle.UI_HEADER).println("== PROJECT STRUCTURE ==");
        PrettySystem.reset();

        PrettySystem.println("└── 📁 project");
        PrettySystem.foreground(RGBColor.of(50, 150, 255)).println("    ├── 📁 src");
        PrettySystem.foreground(RGBColor.of(50, 150, 255)).println("    │   ├── 📁 main");
        PrettySystem.foreground(RGBColor.of(50, 150, 255)).println("    │   │   ├── 📁 java");
        PrettySystem.foreground(RGBColor.of(0, 230, 64)).println("    │   │   │   ├── ☕ Main.java");
        PrettySystem.foreground(RGBColor.of(0, 230, 64)).println("    │   │   │   └── ☕ Utils.java");
        PrettySystem.foreground(RGBColor.of(50, 150, 255)).println("    │   │   └── 📁 resources");
        PrettySystem.foreground(RGBColor.of(0, 230, 64)).println("    │   │       └── 🔧 config.yaml");
        PrettySystem.foreground(RGBColor.of(50, 150, 255)).println("    │   └── 📁 test");
        PrettySystem.foreground(RGBColor.of(0, 230, 64)).println("    │       └── ☕ MainTest.java");
        PrettySystem.foreground(RGBColor.of(0, 230, 64)).println("    ├── 🏗️ build.gradle");
        PrettySystem.foreground(RGBColor.of(0, 230, 64)).println("    └── 📚 README.md");

        // Reset formatting at the end
        PrettySystem.reset();

        demoFileLinks();
    }

    /**
     * Demonstrates the file hyperlink functionality.
     */
    public static void demoFileLinks() {
        System.out.println("=== File Hyperlink Demo ===\n");

        // Create some sample files
        File homeDir = new File(System.getProperty("user.home"));
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File userDir = new File(System.getProperty("user.dir"));

        // Create PrettyFile objects
        PrettyFile homeFile = new PrettyFile(homeDir);
        PrettyFile tempFile = new PrettyFile(tempDir);
        PrettyFile userFile = new PrettyFile(userDir);

        // Example 1: Basic PrettySystem link to files
        System.out.println("1. Basic system links to directories:\n");
        PrettySystem.println("Your home directory: ");
        PrettySystem.printlnClickableFileLink(homeDir);

        PrettySystem.println("Your temp directory: ");
        PrettySystem.printlnClickableFileLink(tempDir);

        PrettySystem.println("Your current directory: ");
        PrettySystem.printlnClickableFileLink(userDir);

        // Example 2: Using absolute paths
        System.out.println("\n2. Links with absolute paths:\n");
        PrettySystem.println("Home directory (absolute): ");
        PrettySystem.printlnClickableFileLink(homeDir);

        // Example 3: Using PrettyFile with default formatting
        System.out.println("\n3. PrettyFile with default formatting:\n");

        // Apply some formatting to the PrettyFile
        homeFile.withIcon().withColor(RGBColor.of(0, 128, 255));
        tempFile.withIcon().withColor(RGBColor.of(255, 128, 0));
        userFile.withIcon().withColor(RGBColor.of(0, 200, 100));

        System.out.println("Formatted directories:");
        System.out.println(homeFile.getFormattedName());
        System.out.println(tempFile.getFormattedName());
        System.out.println(userFile.getFormattedName());

        // Example 4: Using PrettyFile with hyperlinks
        System.out.println("\n4. PrettyFile with hyperlinks:\n");

        System.out.println("Clickable directory links:");
        homeFile.printlnAsLink();
        tempFile.printlnAsLink();
        userFile.printlnAsLink();

        // Example 5: Using PrettyFile with formatting and hyperlinks in a file tree
        System.out.println("\n5. File tree with formatted links:\n");

        // Get some files from the current directory
        PrettyFile currentDir = new PrettyFile(userDir);
        PrettyFile[] files = currentDir.listPrettyFiles();

        if (files.length > 0) {
            System.out.println("Current directory contents (clickable):");
            PrettyPrintStream pps = new PrettyPrintStream(System.out);

            for (int i = 0; i < Math.min(files.length, 5); i++) {
                PrettyFile file = files[i];
                file.withIcon();

                // Show file name with formatting
                pps.print(file.isDirectory() ? RGBColor.of(50, 150, 255) : RGBColor.of(0, 230, 64), file.getIcon() + " ");

                // Make the file name a hyperlink
                file.printlnAsLink(pps);
            }
        }

        // Example 6: Integrating with file tree display
        System.out.println("\n6. Integrating file links with tree display:\n");

        // Create a list for the demo
        List<File> sampleFiles = new ArrayList<>();
        sampleFiles.add(homeDir);
        sampleFiles.add(tempDir);
        sampleFiles.add(userDir);

        // Add a few files from the current directory if they exist
        if (files.length > 0) {
            for (int i = 0; i < Math.min(files.length, 3); i++) {
                sampleFiles.add(files[i]);
            }
        }

        // Show file tree
        System.out.println("File Tree (non-clickable):");
        PrettyFile.showFileTree(sampleFiles);

        // Show file tree with links
        System.out.println("\nFile List with Links:");

        // Custom display to show file tree with links
        for (int i = 0; i < sampleFiles.size(); i++) {
            File file = sampleFiles.get(i);
            PrettyFile prettyFile = new PrettyFile(file);
            prettyFile.withIcon();

            String prefix = (i == sampleFiles.size() - 1) ? "└── " : "├── ";
            System.out.print(prefix);
            prettyFile.printlnAsLink();
        }

        System.out.println("\n=== Demo Complete ===");
    }


    // In PrettySystem class
    public static void printHyperlink(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }

        // Apply link style (blue and underlined)
        // Use standard ANSI escape codes, no OSC
        String blue = "\u001B[34m";
        String underline = "\u001B[4m";
        String reset = "\u001B[0m";

        System.out.print(blue + underline + url + reset);
    }

    public static void printFileLink(File file, boolean useAbsolutePath) {
        if (file == null) {
            return;
        }

        // Determine what text to display
        String displayText = useAbsolutePath ? file.getAbsolutePath() : file.getName();

        // Apply link style (blue and underlined)
        // Use standard ANSI escape codes, no OSC
        String blue = "\u001B[34m";
        String underline = "\u001B[4m";
        String reset = "\u001B[0m";

        System.out.print(blue + underline + displayText + reset);
    }
    /**
     * For PrettySystem: Prints a clickable hyperlink to a local file, showing only the file name.
     *
     * @param file The File object to link to
     */
    public static void printFileLink(File file) {
        printFileLink(file, false);
    }

    /**
     * For PrettySystem: Prints a clickable hyperlink to a local file followed by a line break.
     *
     * @param file The File object to link to
     * @param useAbsolutePath Whether to display the absolute path or just the file name
     */
    public static void printlnFileLink(File file, boolean useAbsolutePath) {
        printFileLink(file, useAbsolutePath);
        System.out.println();
    }

    /**
     * For PrettySystem: Prints a clickable hyperlink to a local file followed by a line break.
     * Shows only the file name.
     *
     * @param file The File object to link to
     */
    public static void printlnFileLink(File file) {
        printFileLink(file, false);
        System.out.println();
    }

    /**
     * For PrettySystem: Prints a clickable file link followed by a line break.
     *
     * @param file The File object to link to
     */
    public static void printlnClickableFileLink(File file) {
        out.printClickableFileLink(file);
        System.out.println();
    }

    /**
     * For PrettySystem: Prints a clickable file link to the standard output.
     *
     * @param file The File object to link to
     */
    public static void printClickableFileLink(File file) {
        if (file == null) {
            return;
        }

        // Convert File to URI, then to URL string
        String fileUrl = file.toURI().toString();

        // Get absolute path for display
        String displayText = file.getAbsolutePath();

        // Apply styling (blue and underlined)
        String blue = "\u001B[34m";
        String underline = "\u001B[4m";

        if (TerminalUtils.isAnsiSupported()) {
            // OSC 8 escape sequence for clickable links
            System.out.print(blue + underline +
                    "\u001B]8;;" + fileUrl + "\u001B\\" +
                    displayText +
                    "\u001B]8;;\u001B\\" + "\u001B[0m");
        } else {
            // Fallback for terminals without support
            System.out.print(blue + underline + displayText + "\u001B[0m");
        }
    }
}