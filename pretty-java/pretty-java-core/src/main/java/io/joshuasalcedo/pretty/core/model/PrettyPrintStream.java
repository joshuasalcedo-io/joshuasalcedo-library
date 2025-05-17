package io.joshuasalcedo.pretty.core.theme;

import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.utils.TerminalUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * A PrintStream implementation that supports RGB color formatting for terminal output.
 * <p>
 * This class extends PrintStream to add support for RGB color formatting,
 * allowing for styled console output with true-color support.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * PrettyPrintStream out = new PrettyPrintStream(System.out);
 * out.foreground(RGBColor.of(255, 0, 0)).println("This text is red!");
 * out.foreground(RGBColor.of(0, 255, 0)).bold(true).println("Bold green text");
 * out.style(TerminalStyle.ERROR).println("This is an error message");
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public class PrettyPrintStream extends PrintStream {

    // ANSI escape code constants
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_ITALIC = "\u001B[3m";
    private static final String ANSI_UNDERLINE = "\u001B[4m";
    private static final String ANSI_BLINK = "\u001B[5m";
    private static final String ANSI_REVERSE = "\u001B[7m";
    private static final String ANSI_STRIKETHROUGH = "\u001B[9m";

    private RGBColor foregroundColor;
    private RGBColor backgroundColor;
    private boolean bold = false;
    private boolean italic = false;
    private boolean underline = false;
    private boolean blink = false;
    private boolean reverse = false;
    private boolean strikethrough = false;
    private boolean useFormatting = true;
    private TerminalStyle terminalStyle = null;

    /**
     * Sets the foreground (text) color.
     *
     * @param color The RGB color to apply to the text
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream foreground(RGBColor color) {
        this.foregroundColor = color;
        this.terminalStyle = null; // Reset terminal style when setting individual color
        return this;
    }

    /**
     * Sets the background color.
     *
     * @param color The RGB color to apply to the background
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream background(RGBColor color) {
        this.backgroundColor = color;
        this.terminalStyle = null; // Reset terminal style when setting individual color
        return this;
    }

    /**
     * Sets a predefined terminal style that includes foreground and possibly background colors.
     *
     * @param style The terminal style to apply
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream style(TerminalStyle style) {
        this.terminalStyle = style;
        // When setting a style, clear individual color settings
        this.foregroundColor = null;
        this.backgroundColor = null;
        return this;
    }

    /**
     * Enables or disables bold text.
     *
     * @param enabled Whether bold formatting should be enabled
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream bold(boolean enabled) {
        this.bold = enabled;
        return this;
    }

    /**
     * Enables or disables italic text.
     *
     * @param enabled Whether italic formatting should be enabled
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream italic(boolean enabled) {
        this.italic = enabled;
        return this;
    }

    /**
     * Enables or disables underlined text.
     *
     * @param enabled Whether underline formatting should be enabled
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream underline(boolean enabled) {
        this.underline = enabled;
        return this;
    }

    /**
     * Enables or disables blinking text.
     *
     * @param enabled Whether blink formatting should be enabled
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream blink(boolean enabled) {
        this.blink = enabled;
        return this;
    }

    /**
     * Enables or disables reverse video (swaps foreground and background colors).
     *
     * @param enabled Whether reverse video should be enabled
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream reverse(boolean enabled) {
        this.reverse = enabled;
        return this;
    }

    /**
     * Enables or disables strikethrough text.
     *
     * @param enabled Whether strikethrough formatting should be enabled
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream strikethrough(boolean enabled) {
        this.strikethrough = enabled;
        return this;
    }

    /**
     * Enables or disables all formatting.
     *
     * @param enabled Whether formatting should be enabled
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream formatting(boolean enabled) {
        this.useFormatting = enabled;
        return this;
    }

    /**
     * Resets all formatting (colors and styles).
     *
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream reset() {
        this.foregroundColor = null;
        this.backgroundColor = null;
        this.bold = false;
        this.italic = false;
        this.underline = false;
        this.blink = false;
        this.reverse = false;
        this.strikethrough = false;
        this.terminalStyle = null;
        return this;
    }

    /**
     * Formats the given text with the current formatting settings.
     *
     * @param text The text to format
     * @return The formatted text
     */
    private String format(String text) {
        // Check if the text is null or empty
        if (text == null) {
            return "";
        }
        
        // Early return if formatting is disabled or no styles are applied
        if (!this.useFormatting || (!hasAnyFormatting() && terminalStyle == null)) {
            return text;
        }

        // Check if terminal supports ANSI
        if (!TerminalUtils.isAnsiSupported()) {
            return text;
        }

        // If a terminal style is set, use it directly
        if (terminalStyle != null) {
            return terminalStyle.apply(text);
        }

        // Use StringBuilder with precalculated capacity for optimal performance
        StringBuilder sb = new StringBuilder(text.length() + 40); // Extra space for ANSI codes
        
        // Apply text styles
        if (bold) sb.append(ANSI_BOLD);
        if (italic) sb.append(ANSI_ITALIC);
        if (underline) sb.append(ANSI_UNDERLINE);
        if (blink) sb.append(ANSI_BLINK);
        if (reverse) sb.append(ANSI_REVERSE);
        if (strikethrough) sb.append(ANSI_STRIKETHROUGH);
        
        // Apply colors
        if (foregroundColor != null) {
            sb.append(foregroundColor.toAnsi());
        }
        
        if (backgroundColor != null) {
            sb.append(backgroundColor.asBackground().toAnsi());
        }
        
        // Append text and reset
        sb.append(text).append(ANSI_RESET);
        
        return sb.toString();
    }

    /**
     * Checks if any formatting is currently enabled.
     *
     * @return true if any formatting is enabled, false otherwise
     */
    private boolean hasAnyFormatting() {
        return foregroundColor != null 
            || backgroundColor != null 
            || bold 
            || italic 
            || underline 
            || blink 
            || reverse 
            || strikethrough;
    }

    // Constructors to match PrintStream

    public PrettyPrintStream(OutputStream out) {
        super(out);
    }

    public PrettyPrintStream(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public PrettyPrintStream(OutputStream out, boolean autoFlush, String encoding) throws UnsupportedEncodingException {
        super(out, autoFlush, encoding);
    }

    public PrettyPrintStream(OutputStream out, boolean autoFlush, Charset charset) {
        super(out, autoFlush, charset);
    }

    public PrettyPrintStream(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public PrettyPrintStream(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public PrettyPrintStream(String fileName, Charset charset) throws IOException {
        super(fileName, charset);
    }

    public PrettyPrintStream(File file) throws FileNotFoundException {
        super(file);
    }

    public PrettyPrintStream(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }

    public PrettyPrintStream(File file, Charset charset) throws IOException {
        super(file, charset);
    }

    // Override all print and println methods to apply formatting

    @Override
    public void println(boolean x) {
        super.println(format(String.valueOf(x)));
    }

    @Override
    public void println(char x) {
        super.println(format(String.valueOf(x)));
    }

    @Override
    public void println(int x) {
        super.println(format(String.valueOf(x)));
    }

    @Override
    public void println(long x) {
        super.println(format(String.valueOf(x)));
    }

    @Override
    public void println(float x) {
        super.println(format(String.valueOf(x)));
    }

    @Override
    public void println(double x) {
        super.println(format(String.valueOf(x)));
    }

    @Override
    public void println(char[] x) {
        super.println(format(new String(x)));
    }

    @Override
    public void println(String x) {
        super.println(format(x != null ? x : "null"));
    }

    @Override
    public void println(Object x) {
        super.println(format(String.valueOf(x)));
    }

    @Override
    public void print(boolean x) {
        super.print(format(String.valueOf(x)));
    }

    @Override
    public void print(char x) {
        super.print(format(String.valueOf(x)));
    }

    @Override
    public void print(int x) {
        super.print(format(String.valueOf(x)));
    }

    @Override
    public void print(long x) {
        super.print(format(String.valueOf(x)));
    }

    @Override
    public void print(float x) {
        super.print(format(String.valueOf(x)));
    }

    @Override
    public void print(double x) {
        super.print(format(String.valueOf(x)));
    }

    @Override
    public void print(char[] x) {
        super.print(format(new String(x)));
    }

    @Override
    public void print(String x) {
        super.print(format(x != null ? x : "null"));
    }

    @Override
    public void print(Object x) {
        super.print(format(String.valueOf(x)));
    }

    // Add convenience methods to print directly with a specific style

    /**
     * Prints a line with the specified style.
     *
     * @param style The style to apply
     * @param text The text to print
     */
    public void println(TerminalStyle style, String text) {
        TerminalStyle oldStyle = this.terminalStyle;
        this.style(style);
        println(text);
        this.terminalStyle = oldStyle;
    }

    /**
     * Prints text with the specified style.
     *
     * @param style The style to apply
     * @param text The text to print
     */
    public void print(TerminalStyle style, String text) {
        TerminalStyle oldStyle = this.terminalStyle;
        this.style(style);
        print(text);
        this.terminalStyle = oldStyle;
    }

    /**
     * Prints a line with the specified color.
     *
     * @param color The color to apply
     * @param text The text to print
     */
    public void println(RGBColor color, String text) {
        RGBColor oldColor = this.foregroundColor;
        this.foreground(color);
        println(text);
        this.foregroundColor = oldColor;
    }

    /**
     * Prints text with the specified color.
     *
     * @param color The color to apply
     * @param text The text to print
     */
    public void print(RGBColor color, String text) {
        RGBColor oldColor = this.foregroundColor;
        this.foreground(color);
        print(text);
        this.foregroundColor = oldColor;
    }

    /**
     * Prints text with the specified foreground and background colors.
     *
     * @param fg The foreground color
     * @param bg The background color
     * @param text The text to print
     */
    public void println(RGBColor fg, RGBColor bg, String text) {
        RGBColor oldFg = this.foregroundColor;
        RGBColor oldBg = this.backgroundColor;
        this.foreground(fg).background(bg);
        println(text);
        this.foreground(oldFg).background(oldBg);
    }

    /**
     * Prints text with the specified foreground and background colors.
     *
     * @param fg The foreground color
     * @param bg The background color
     * @param text The text to print
     */
    public void print(RGBColor fg, RGBColor bg, String text) {
        RGBColor oldFg = this.foregroundColor;
        RGBColor oldBg = this.backgroundColor;
        this.foreground(fg).background(bg);
        print(text);
        this.foreground(oldFg).background(oldBg);
    }

    // Main method to demonstrate the PrettyPrintStream
    public static void main(String[] args) {
        PrettyPrintStream out = new PrettyPrintStream(System.out);
        
        out.println("=== PrettyPrintStream Demo ===");
        
        // Demonstrate RGB colors
        out.println("\nRGB Colors:");
        out.println(RGBColor.of(255, 0, 0), "Pure Red (255, 0, 0)");
        out.println(RGBColor.of(0, 255, 0), "Pure Green (0, 255, 0)");
        out.println(RGBColor.of(0, 0, 255), "Pure Blue (0, 0, 255)");
        out.println(RGBColor.of(255, 165, 0), "Orange (255, 165, 0)");
        out.println(RGBColor.of(128, 0, 128), "Purple (128, 0, 128)");
        
        // Demonstrate background colors
        out.println("\nBackground Colors:");
        out.println(RGBColor.of(255, 255, 255), RGBColor.of(255, 0, 0), "White text on red background");
        out.println(RGBColor.of(0, 0, 0), RGBColor.of(0, 255, 0), "Black text on green background");
        out.println(RGBColor.of(255, 255, 0), RGBColor.of(0, 0, 128), "Yellow text on navy background");
        
        // Demonstrate text styles
        out.println("\nText Styles:");
        out.bold(true).println("Bold text");
        out.reset().italic(true).println("Italic text");
        out.reset().underline(true).println("Underlined text");
        out.reset().strikethrough(true).println("Strikethrough text");
        out.reset().bold(true).foreground(RGBColor.of(255, 0, 255)).println("Bold magenta text");
        out.reset().italic(true).underline(true).foreground(RGBColor.of(0, 200, 200)).println("Italic and underlined cyan text");
        
        // Demonstrate terminal styles if available
        if (TerminalStyle.values().length > 0) {
            out.println("\nPredefined Terminal Styles:");
            out.println(TerminalStyle.ERROR, "This is an error message");
            out.println(TerminalStyle.WARNING, "This is a warning message");
            out.println(TerminalStyle.SUCCESS, "This is a success message");
            out.println(TerminalStyle.INFO, "This is an info message");
        }
        
        // Demonstrate gradients
        out.println("\nColor Gradients:");
        for (int i = 0; i < 255; i += 10) {
            out.print(RGBColor.of(255 - i, 0, i), "■");
        }
        out.println();
        
        for (int i = 0; i < 255; i += 10) {
            out.print(RGBColor.of(255, i, 0), "■");
        }
        out.println();
        
        // Reset at the end
        out.reset();
    }
}