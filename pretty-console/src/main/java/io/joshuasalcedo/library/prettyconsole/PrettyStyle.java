package io.joshuasalcedo.library.prettyconsole;

import io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Enhanced terminal formatting system for console output styling.
 * <p>
 * This system provides a modular and flexible approach to console formatting
 * with support for basic ANSI codes, RGB colors, and common console patterns.
 * </p>
 * <p>
 * Usage examples:
 * </p>
 * <pre>
 * // Apply a single style
 * System.out.println(PrettyStyle.apply(PrettyStyle.Color.RED, "This text is red!"));
 *
 * // Combine multiple styles
 * PrettyStyle.Formatter formatter = new PrettyStyle.Formatter()
 *     .withStyle(PrettyStyle.Style.BOLD)
 *     .withColor(PrettyStyle.Color.BLUE)
 *     .withBackground(PrettyStyle.Background.BRIGHT_WHITE);
 * System.out.println(PrettyStyle.apply(formatter, "Bold blue text on bright white background"));
 *
 * // Using RGB colors
 * System.out.println(PrettyStyle.rgb(255, 100, 50).apply("Custom orange text"));
 *
 * // Using fluent string extension methods
 * String result = "Warning:".style(PrettyStyle.Style.BOLD)
 *                          .color(PrettyStyle.Color.YELLOW)
 *                          .bg(PrettyStyle.Background.BLACK)
 *                          .text();
 *
 * // Print a progress bar
 * PrettyStyle.printProgressBar(67, 100, 40, "Loading...");
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
@Data
public final class PrettyStyle {

    /**
     * Resets all formatting and returns to default console output.
     * Should be used after any formatting sequence.
     */
    public static final String RESET = "\033[0m";

    /**
     * ANSI CSI (Control Sequence Introducer) prefix for all ANSI escape sequences
     */
    public static final String CSI = "\033[";

    /**
     * Common interface for all formatting elements
     */
    public interface FormatElement {
        /**
         * Get the ANSI code for this formatting element
         *
         * @return The ANSI code as a string
         */
        String getCode();

        /**
         * Get the description of this formatting element
         *
         * @return The description as a string
         */
        String getDescription();

        /**
         * Apply this formatting element to the given text
         *
         * @param text The text to format
         * @return The formatted text with reset code at the end
         */
        default String apply(String text) {
            return getCode() + text + RESET;
        }

        /**
         * Safely apply this formatting element to the given text if the terminal supports ANSI codes
         *
         * @param text The text to format
         * @return The formatted text if supported, the original text otherwise
         */
        default String safeApply(String text) {
            return TerminalUtils.isAnsiSupported() ? apply(text) : text;
        }
    }

    /**
     * Text styles for console output.
     */
    @Getter
    public enum Style implements FormatElement {
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
        /**
         * -- GETTER --
         *  Get the ANSI code value as an integer
         *
         * @return The ANSI code value
         */
        private final int value;

        Style(int value, String description) {
            this.value = value;
            this.code = CSI + value + "m";
            this.description = description;
        }

    }

    /**
     * Foreground text colors for console output.
     */
    @Getter
    public enum Color implements FormatElement {
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
        /**
         * -- GETTER --
         *  Get the ANSI code value as an integer
         *
         * @return The ANSI code value
         */
        private final int value;

        Color(int value, String description) {
            this.value = value;
            this.code = CSI + value + "m";
            this.description = description;
        }

        /**
         * Get a Color by its ANSI value
         *
         * @param value The ANSI value
         * @return The matching Color or null if not found
         */
        public static Color fromValue(int value) {
            for (Color color : values()) {
                if (color.getValue() == value) {
                    return color;
                }
            }
            return null;
        }
    }

    /**
     * Background colors for console output.
     */
    @Getter
    public enum Background implements FormatElement {
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
        /**
         * -- GETTER --
         *  Get the ANSI code value as an integer
         *
         * @return The ANSI code value
         */
        private final int value;

        Background(int value, String description) {
            this.value = value;
            this.code = CSI + value + "m";
            this.description = description;
        }

        /**
         * Get a Background by its ANSI value
         *
         * @param value The ANSI value
         * @return The matching Background or null if not found
         */
        public static Background fromValue(int value) {
            for (Background bg : values()) {
                if (bg.getValue() == value) {
                    return bg;
                }
            }
            return null;
        }
    }

    /**
     * 24-bit RGB color wrapper for foreground text.
     */
    public static class RgbColor implements FormatElement {
        private final int r;
        private final int g;
        private final int b;
        /**
         * -- GETTER --
         *  Check if this is a background color
         *
         * @return true if this is a background color, false if it's a foreground color
         */
        @Getter
        private final boolean background;

        /**
         * Create a new foreground RGB color
         *
         * @param r Red component (0-255)
         * @param g Green component (0-255)
         * @param b Blue component (0-255)
         */
        public RgbColor(int r, int g, int b) {
            this(r, g, b, false);
        }

        /**
         * Create a new RGB color
         *
         * @param r          Red component (0-255)
         * @param g          Green component (0-255)
         * @param b          Blue component (0-255)
         * @param background Whether this is a background color
         */
        public RgbColor(int r, int g, int b, boolean background) {
            this.r = Math.max(0, Math.min(255, r));
            this.g = Math.max(0, Math.min(255, g));
            this.b = Math.max(0, Math.min(255, b));
            this.background = background;
        }

        @Override
        public String getCode() {
            return CSI + (background ? "48" : "38") + ";2;" + r + ";" + g + ";" + b + "m";
        }

        @Override
        public String getDescription() {
            return (background ? "Background" : "Foreground") + " RGB color (" + r + "," + g + "," + b + ")";
        }

        /**
         * Convert this RGB color to a background color
         *
         * @return A new RgbColor configured as a background color
         */
        public RgbColor asBackground() {
            return new RgbColor(r, g, b, true);
        }

        /**
         * Get the red component
         *
         * @return Red value (0-255)
         */
        public int getRed() {
            return r;
        }

        /**
         * Get the green component
         *
         * @return Green value (0-255)
         */
        public int getGreen() {
            return g;
        }

        /**
         * Get the blue component
         *
         * @return Blue value (0-255)
         */
        public int getBlue() {
            return b;
        }

    }

    /**
     * 8-bit (256 color) wrapper for foreground and background colors.
     */
    public static class Color256 implements FormatElement {
        private final int colorCode;
        private final boolean background;

        /**
         * Create a new foreground 8-bit color
         *
         * @param colorCode Color code (0-255)
         */
        public Color256(int colorCode) {
            this(colorCode, false);
        }

        /**
         * Create a new 8-bit color
         *
         * @param colorCode  Color code (0-255)
         * @param background Whether this is a background color
         */
        public Color256(int colorCode, boolean background) {
            this.colorCode = Math.max(0, Math.min(255, colorCode));
            this.background = background;
        }

        @Override
        public String getCode() {
            return CSI + (background ? "48" : "38") + ";5;" + colorCode + "m";
        }

        @Override
        public String getDescription() {
            return (background ? "Background" : "Foreground") + " 8-bit color (" + colorCode + ")";
        }

        /**
         * Convert this 8-bit color to a background color
         *
         * @return A new Color256 configured as a background color
         */
        public Color256 asBackground() {
            return new Color256(colorCode, true);
        }

        /**
         * Get the color code
         *
         * @return Color code (0-255)
         */
        public int getColorCode() {
            return colorCode;
        }

        /**
         * Check if this is a background color
         *
         * @return true if this is a background color, false if it's a foreground color
         */
        public boolean isBackground() {
            return background;
        }
    }

    /**
     * Utility class for creating combinations of styles and colors.
     */
    public static class Formatter {
        private final List<FormatElement> elements = new ArrayList<>();

        /**
         * Add a style to the formatter.
         *
         * @param style The style to add
         * @return This formatter for method chaining
         */
        public Formatter withStyle(Style style) {
            elements.add(style);
            return this;
        }

        /**
         * Add a text color to the formatter.
         *
         * @param color The color to add
         * @return This formatter for method chaining
         */
        public Formatter withColor(Color color) {
            elements.add(color);
            return this;
        }

        /**
         * Add a background color to the formatter.
         *
         * @param background The background color to add
         * @return This formatter for method chaining
         */
        public Formatter withBackground(Background background) {
            elements.add(background);
            return this;
        }

        /**
         * Add an RGB text color to the formatter.
         *
         * @param r Red component (0-255)
         * @param g Green component (0-255)
         * @param b Blue component (0-255)
         * @return This formatter for method chaining
         */
        public Formatter withRgbColor(int r, int g, int b) {
            elements.add(new RgbColor(r, g, b));
            return this;
        }

        /**
         * Add an RGB background color to the formatter.
         *
         * @param r Red component (0-255)
         * @param g Green component (0-255)
         * @param b Blue component (0-255)
         * @return This formatter for method chaining
         */
        public Formatter withRgbBackground(int r, int g, int b) {
            elements.add(new RgbColor(r, g, b, true));
            return this;
        }

        /**
         * Add an 8-bit (256 color) text color to the formatter.
         *
         * @param colorCode Color code (0-255)
         * @return This formatter for method chaining
         */
        public Formatter with256Color(int colorCode) {
            elements.add(new Color256(colorCode));
            return this;
        }

        /**
         * Add an 8-bit (256 color) background color to the formatter.
         *
         * @param colorCode Color code (0-255)
         * @return This formatter for method chaining
         */
        public Formatter with256Background(int colorCode) {
            elements.add(new Color256(colorCode, true));
            return this;
        }

        /**
         * Add a custom format element to the formatter.
         *
         * @param element The format element to add
         * @return This formatter for method chaining
         */
        public Formatter withElement(FormatElement element) {
            elements.add(element);
            return this;
        }

        /**
         * Get the combined ANSI code sequence.
         *
         * @return The combined ANSI codes as a string
         */
        public String getCode() {
            StringBuilder sb = new StringBuilder();
            for (FormatElement element : elements) {
                sb.append(element.getCode());
            }
            return sb.toString();
        }

        /**
         * Apply the formatter to the given text.
         *
         * @param text The text to format
         * @return The formatted text with reset code at the end
         */
        public String apply(String text) {
            return getCode() + text + RESET;
        }

        /**
         * Safely apply the formatter to the given text if the terminal supports ANSI codes.
         *
         * @param text The text to format
         * @return The formatted text if supported, the original text otherwise
         */
        public String safeApply(String text) {
            return TerminalUtils.isAnsiSupported() ? apply(text) : text;
        }
    }

    /**
     * String decorator class for fluent formatting.
     */
    public static class StringDecorator {
        private final String original;
        private final Formatter formatter = new Formatter();

        /**
         * Create a new string decorator for the given text.
         *
         * @param text The text to decorate
         */
        public StringDecorator(String text) {
            this.original = text;
        }

        /**
         * Apply a style to the text.
         *
         * @param style The style to apply
         * @return This decorator for method chaining
         */
        public StringDecorator style(Style style) {
            formatter.withStyle(style);
            return this;
        }

        /**
         * Apply a foreground color to the text.
         *
         * @param color The color to apply
         * @return This decorator for method chaining
         */
        public StringDecorator color(Color color) {
            formatter.withColor(color);
            return this;
        }

        /**
         * Apply a background color to the text.
         *
         * @param background The background color to apply
         * @return This decorator for method chaining
         */
        public StringDecorator bg(Background background) {
            formatter.withBackground(background);
            return this;
        }

        /**
         * Apply an RGB foreground color to the text.
         *
         * @param r Red component (0-255)
         * @param g Green component (0-255)
         * @param b Blue component (0-255)
         * @return This decorator for method chaining
         */
        public StringDecorator rgb(int r, int g, int b) {
            formatter.withRgbColor(r, g, b);
            return this;
        }

        /**
         * Apply an RGB background color to the text.
         *
         * @param r Red component (0-255)
         * @param g Green component (0-255)
         * @param b Blue component (0-255)
         * @return This decorator for method chaining
         */
        public StringDecorator bgRgb(int r, int g, int b) {
            formatter.withRgbBackground(r, g, b);
            return this;
        }

        /**
         * Apply an 8-bit (256 color) foreground color to the text.
         *
         * @param colorCode Color code (0-255)
         * @return This decorator for method chaining
         */
        public StringDecorator color256(int colorCode) {
            formatter.with256Color(colorCode);
            return this;
        }

        /**
         * Apply an 8-bit (256 color) background color to the text.
         *
         * @param colorCode Color code (0-255)
         * @return This decorator for method chaining
         */
        public StringDecorator bg256(int colorCode) {
            formatter.with256Background(colorCode);
            return this;
        }

        /**
         * Apply a custom format element to the text.
         *
         * @param element The format element to apply
         * @return This decorator for method chaining
         */
        public StringDecorator format(FormatElement element) {
            formatter.withElement(element);
            return this;
        }

        /**
         * Get the formatted text.
         *
         * @return The text with all formatting applied
         */
        public String text() {
            return formatter.apply(original);
        }

        /**
         * Get the formatted text if the terminal supports ANSI codes.
         *
         * @return The formatted text if supported, the original text otherwise
         */
        public String safeText() {
            return formatter.safeApply(original);
        }

        /**
         * Get the string representation of this decorator (formatted text).
         *
         * @return The formatted text
         */
        @Override
        public String toString() {
            return text();
        }
    }

    /**
     * Create an RGB foreground color.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @return A new RGB color
     */
    public static RgbColor rgb(int r, int g, int b) {
        return new RgbColor(r, g, b);
    }

    /**
     * Create an RGB background color.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @return A new RGB background color
     */
    public static RgbColor rgbBackground(int r, int g, int b) {
        return new RgbColor(r, g, b, true);
    }

    /**
     * Create an 8-bit (256 color) foreground color.
     *
     * @param colorCode Color code (0-255)
     * @return A new 8-bit color
     */
    public static Color256 color256(int colorCode) {
        return new Color256(colorCode);
    }

    /**
     * Create an 8-bit (256 color) background color.
     *
     * @param colorCode Color code (0-255)
     * @return A new 8-bit background color
     */
    public static Color256 background256(int colorCode) {
        return new Color256(colorCode, true);
    }

    /**
     * Extend a string with formatting capabilities.
     *
     * @param text The text to format
     * @return A string decorator for the text
     */
    public static StringDecorator decorate(String text) {
        return new StringDecorator(text);
    }

    /**
     * Apply formatting to the given text.
     *
     * @param format The formatting element to apply
     * @param text   The text to format
     * @return The formatted text with reset code at the end
     */
    public static String apply(FormatElement format, String text) {
        return format.getCode() + text + RESET;
    }

    /**
     * Apply formatting to the given text.
     *
     * @param formatter The formatter with combined formatting codes
     * @param text      The text to format
     * @return The formatted text with reset code at the end
     */
    public static String apply(Formatter formatter, String text) {
        return formatter.getCode() + text + RESET;
    }

    /**
     * Safely apply formatting to the given text if the terminal supports ANSI codes.
     *
     * @param format The formatting element to apply
     * @param text   The text to format
     * @return The formatted text if supported, the original text otherwise
     */
    public static String safeApply(FormatElement format, String text) {
        return TerminalUtils.isAnsiSupported() ? apply(format, text) : text;
    }

    /**
     * Safely apply formatting to the given text if the terminal supports ANSI codes.
     *
     * @param formatter The formatter with combined formatting codes
     * @param text      The text to format
     * @return The formatted text if supported, the original text otherwise
     */
    public static String safeApply(Formatter formatter, String text) {
        return TerminalUtils.isAnsiSupported() ? apply(formatter, text) : text;
    }

    /**
     * Remove all ANSI escape sequences from a string.
     *
     * @param text The text with ANSI codes
     * @return The text without any ANSI codes
     */
    public static String stripAnsi(String text) {
        if (text == null) {
            return null;
        }
        // Pattern to match ANSI escape sequences
        Pattern pattern = Pattern.compile("\u001B\\[[;\\d]*[ -/]*[@-~]");
        return pattern.matcher(text).replaceAll("");
    }

    /**
     * Print a progress bar to the console.
     *
     * @param current Current progress value
     * @param total   Total progress value
     * @param width   Width of the progress bar in characters
     * @param label   Optional label to display before the progress bar
     */
    public static void printProgressBar(int current, int total, int width, String label) {
        // Calculate percentage
        int percent = (int) (100.0 * current / total);

        // Calculate completed width
        int completedWidth = width * current / total;

        // Build the progress bar
        StringBuilder progressBar = new StringBuilder();

        // Add label if provided
        if (label != null && !label.isEmpty()) {
            progressBar.append(label).append(" ");
        }

        // Add percentage
        progressBar.append("[");

        // Add completed portion
        Formatter completedFormatter = new Formatter()
                .withColor(Color.GREEN);

        String completed = completedFormatter.apply("=".repeat(completedWidth));
        progressBar.append(completed);

        // Add remaining portion
        String remaining = " ".repeat(Math.max(0, width - completedWidth));
        progressBar.append(remaining);

        // Add percentage
        progressBar.append("] ").append(percent).append("%");

        // Print the progress bar
        System.out.print("\r" + progressBar);

        // Add newline if completed
        if (current >= total) {
            System.out.println();
        }
    }

    /**
     * Create a table with colored headers and rows.
     *
     * @param headers               Table headers
     * @param rows                  Table data rows
     * @param headerFormatter       Formatter for headers (optional, defaults to bold cyan)
     * @param alternateRowFormatter Formatter for alternate rows (optional, defaults to dim)
     * @return Formatted table as a string
     */
    public static String createTable(String[] headers, String[][] rows,
                                     Formatter headerFormatter, Formatter alternateRowFormatter) {
        if (headers == null || rows == null || headers.length == 0) {
            return "";
        }

        // Default formatters if not provided
        if (headerFormatter == null) {
            headerFormatter = new Formatter()
                    .withStyle(Style.BOLD)
                    .withColor(Color.CYAN);
        }

        if (alternateRowFormatter == null) {
            alternateRowFormatter = new Formatter()
                    .withStyle(Style.DIM);
        }

        // Calculate column widths
        int[] columnWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = stripAnsi(headers[i]).length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < Math.min(row.length, headers.length); i++) {
                columnWidths[i] = Math.max(columnWidths[i], stripAnsi(row[i]).length());
            }
        }

        // Build the table
        StringBuilder table = new StringBuilder();

        // Add headers
        for (int i = 0; i < headers.length; i++) {
            table.append(headerFormatter.apply(padRight(headers[i], columnWidths[i])));
            if (i < headers.length - 1) {
                table.append(" | ");
            }
        }
        table.append("\n");

        // Add separator
        for (int i = 0; i < headers.length; i++) {
            table.append("-".repeat(columnWidths[i]));
            if (i < headers.length - 1) {
                table.append("-+-");
            }
        }
        table.append("\n");

        // Add rows
        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
            String[] row = rows[rowIndex];
            boolean alternate = rowIndex % 2 == 1;

            for (int i = 0; i < headers.length; i++) {
                String cell = i < row.length ? row[i] : "";
                String formatted = alternate ?
                        alternateRowFormatter.apply(padRight(cell, columnWidths[i])) :
                        padRight(cell, columnWidths[i]);
                table.append(formatted);
                if (i < headers.length - 1) {
                    table.append(" | ");
                }
            }
            table.append("\n");
        }

        return table.toString();
    }

    /**
     * Pad a string to the right with spaces.
     *
     * @param str   The string to pad
     * @param width The target width
     * @return The padded string
     */
    private static String padRight(String str, int width) {
        String stripped = stripAnsi(str);
        int visibleLength = stripped.length();
        if (visibleLength >= width) {
            return str;
        }
        return str + " ".repeat(width - visibleLength);
    }

    /**
     * Create a hyperlink in supported terminals.
     *
     * @param url  The URL to link to
     * @param text The visible text (if null, will show the URL)
     * @return A string with the hyperlink escape sequences
     */
    public static String hyperlink(String url, String text) {
        String displayText = text != null ? text : url;
        return CSI + "]8;;" + url + CSI + "\\" + displayText + CSI + "]8;;" + CSI + "\\";
    }

    /**
     * Create a colorized banner with a message.
     *
     * @param message   The message to display
     * @param formatter The formatter to apply (optional)
     * @return A formatted banner
     */
    public static String createBanner(String message, Formatter formatter) {
        if (formatter == null) {
            formatter = new Formatter()
                    .withStyle(Style.BOLD)
                    .withColor(Color.WHITE)
                    .withBackground(Background.BLUE);
        }

        String stripped = stripAnsi(message);
        int length = stripped.length();
        StringBuilder banner = new StringBuilder();

        String line = "═".repeat(length + 4);
        banner.append(formatter.apply("╔" + line + "╗\n"));
        banner.append(formatter.apply("║  " + message + "  ║\n"));
        banner.append(formatter.apply("╚" + line + "╝"));

        return banner.toString();
    }

    /**
     * Animate text with a typing effect.
     *
     * @param text    The text to animate
     * @param delayMs Delay between characters in milliseconds
     * @throws InterruptedException If thread is interrupted during animation
     */
    public static void typeText(String text, int delayMs) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(delayMs);
        }
        System.out.println();
    }

    /**
     * Animate text with a typing effect and formatting.
     *
     * @param text      The text to animate
     * @param formatter The formatter to apply
     * @param delayMs   Delay between characters in milliseconds
     * @throws InterruptedException If thread is interrupted during animation
     */
    public static void typeText(String text, Formatter formatter, int delayMs) throws InterruptedException {
        System.out.print(formatter.getCode());
        typeText(text, delayMs);
        System.out.print(RESET);
    }

    /**
     * Print colored text based on a condition.
     *
     * @param text       The text to print
     * @param condition  The condition to evaluate
     * @param trueColor  The color to use if condition is true
     * @param falseColor The color to use if condition is false
     * @return The formatted text
     */
    public static String conditional(String text, boolean condition, Color trueColor, Color falseColor) {
        return condition ? apply(trueColor, text) : apply(falseColor, text);
    }

    /**
     * Convert a hex color code to an RGB color.
     *
     * @param hexColor The hex color code (e.g., "#FF5500" or "FF5500")
     * @return A new RGB color object, or null if the input is invalid
     */
    public static RgbColor hexToRgb(String hexColor) {
        if (hexColor == null || hexColor.isEmpty()) {
            return null;
        }

        // Remove # if present
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }

        // Check length
        if (hexColor.length() != 6) {
            return null;
        }

        try {
            int r = Integer.parseInt(hexColor.substring(0, 2), 16);
            int g = Integer.parseInt(hexColor.substring(2, 4), 16);
            int b = Integer.parseInt(hexColor.substring(4, 6), 16);
            return new RgbColor(r, g, b);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Convert RGB values to a hex color code.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @return A hex color code string
     */
    public static String rgbToHex(int r, int g, int b) {
        return String.format("#%02X%02X%02X",
                Math.max(0, Math.min(255, r)),
                Math.max(0, Math.min(255, g)),
                Math.max(0, Math.min(255, b)));
    }

    /**
     * Convert RGB values to a hex color code.
     *
     * @param color The RGB color object
     * @return A hex color code string
     */
    public static String rgbToHex(RgbColor color) {
        return rgbToHex(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Create a gradient between two colors.
     *
     * @param text       The text to apply the gradient to
     * @param startColor The starting color
     * @param endColor   The ending color
     * @return Text with gradient applied
     */
    public static String gradient(String text, RgbColor startColor, RgbColor endColor) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        int length = text.length();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            double ratio = (double) i / (length - 1);
            int r = (int) (startColor.getRed() + ratio * (endColor.getRed() - startColor.getRed()));
            int g = (int) (startColor.getGreen() + ratio * (endColor.getGreen() - startColor.getGreen()));
            int b = (int) (startColor.getBlue() + ratio * (endColor.getBlue() - startColor.getBlue()));

            RgbColor color = new RgbColor(r, g, b);
            result.append(color.apply(Character.toString(text.charAt(i))));
        }

        return result.toString();
    }

    /**
     * Create a rainbow gradient across text.
     *
     * @param text The text to apply the rainbow to
     * @return Text with rainbow colors applied
     */
    public static String rainbow(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Rainbow colors
        RgbColor[] rainbow = {
                new RgbColor(255, 0, 0),      // Red
                new RgbColor(255, 127, 0),    // Orange
                new RgbColor(255, 255, 0),    // Yellow
                new RgbColor(0, 255, 0),      // Green
                new RgbColor(0, 0, 255),      // Blue
                new RgbColor(75, 0, 130),     // Indigo
                new RgbColor(143, 0, 255)     // Violet
        };

        int length = text.length();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            // Calculate which color to use
            double position = (double) i / length * rainbow.length;
            int index = (int) position;

            // Use interpolation between colors for smoother transition
            if (index < rainbow.length - 1) {
                double ratio = position - index;
                RgbColor start = rainbow[index];
                RgbColor end = rainbow[index + 1];

                int r = (int) (start.getRed() + ratio * (end.getRed() - start.getRed()));
                int g = (int) (start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
                int b = (int) (start.getBlue() + ratio * (end.getBlue() - start.getBlue()));

                RgbColor color = new RgbColor(r, g, b);
                result.append(color.apply(Character.toString(text.charAt(i))));
            } else {
                // Last color
                result.append(rainbow[rainbow.length - 1].apply(Character.toString(text.charAt(i))));
            }
        }

        return result.toString();
    }

    /**
     * Add a flashing effect to text (toggle between two formatters).
     * <p>
     * Note: This creates a sequence that will flash when printed, but actual flashing
     * depends on terminal support. Many terminals don't support this well.
     *
     * @param text       The text to make flash
     * @param formatter1 The first formatter
     * @param formatter2 The second formatter
     * @return Text with flashing effect
     */
    public static String flash(String text, Formatter formatter1, Formatter formatter2) {
        return Style.BLINK.apply(formatter1.apply(text));
    }

    /**
     * Create a spinner animation with multiple frames.
     *
     * @param message    The message to display
     * @param frames     The animation frames to cycle through
     * @param durationMs Total animation duration in milliseconds
     * @param frameMs    Time between frame changes in milliseconds
     * @throws InterruptedException If thread is interrupted during animation
     */
    public static void spinner(String message, String[] frames, int durationMs, int frameMs)
            throws InterruptedException {
        if (frames == null || frames.length == 0) {
            frames = new String[]{"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"};
        }

        long startTime = System.currentTimeMillis();
        long endTime = startTime + durationMs;
        int frameIndex = 0;

        while (System.currentTimeMillis() < endTime) {
            String frame = frames[frameIndex];
            System.out.print("\r" + frame + " " + message);
            Thread.sleep(frameMs);

            frameIndex = (frameIndex + 1) % frames.length;
        }

        // Clear the line
        System.out.print("\r" + " ".repeat(message.length() + 2) + "\r");
    }

    /**
     * Generate dynamic text that changes based on a function.
     *
     * @param baseText    The base text
     * @param transformFn A function that takes a character position and returns a formatter
     * @return The dynamically formatted text
     */
    public static String dynamicFormat(String baseText, Function<Integer, Formatter> transformFn) {
        if (baseText == null || baseText.isEmpty() || transformFn == null) {
            return baseText;
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < baseText.length(); i++) {
            Formatter formatter = transformFn.apply(i);
            result.append(formatter.apply(Character.toString(baseText.charAt(i))));
        }

        return result.toString();
    }

    /**
     * Truncate text with an indicator if it exceeds a maximum length.
     *
     * @param text      The text to truncate
     * @param maxLength The maximum length
     * @param indicator The indicator to append (e.g., "...")
     * @return The truncated text
     */
    public static String truncate(String text, int maxLength, String indicator) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }

        if (indicator == null) {
            indicator = "...";
        }

        int truncateLength = maxLength - indicator.length();
        if (truncateLength <= 0) {
            return indicator.substring(0, maxLength);
        }

        return text.substring(0, truncateLength) + indicator;
    }

    // Private constructor to prevent instantiation
    private PrettyStyle() {
        // Utility class, prevent instantiation
    }
}