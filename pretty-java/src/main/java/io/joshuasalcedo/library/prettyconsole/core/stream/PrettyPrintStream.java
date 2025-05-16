package io.joshuasalcedo.library.prettyconsole.core.stream;

import io.joshuasalcedo.library.prettyconsole.style.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * A PrintStream implementation that supports Pretty formatting.
 * <p>
 * This class extends PrintStream to add support for Pretty formatting,
 * allowing for styled console output with colors, styles, and backgrounds.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * PrettyPrintStream out = new PrettyPrintStream(System.out);
 * out.color(ForegroundColor.RED).println("This text is red!");
 * out.style(TextStyle.BOLD).color(ForegroundColor.GREEN).println("Bold green text");
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public class PrettyPrintStream extends PrintStream {

    private ForegroundColor color;
    private TextStyle style;
    private BackgroundColor background;
    private boolean useFormatting = true;

    /**
     * Sets the text color.
     *
     * @param color The color to apply
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream color(ForegroundColor color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the text style.
     *
     * @param style The style to apply
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream style(TextStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the background color.
     *
     * @param background The background color to apply
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream background(BackgroundColor background) {
        this.background = background;
        return this;
    }

    /**
     * Enables or disables formatting.
     *
     * @param enabled Whether formatting should be enabled
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream formatting(boolean enabled) {
        this.useFormatting = enabled;
        return this;
    }

    /**
     * Resets all formatting (color, style, background).
     *
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream reset() {
        this.color = null;
        this.style = null;
        this.background = null;
        return this;
    }

    /**
     * Formats the given text with the current formatting settings.
     *
     * @param text The text to format
     * @return The formatted text
     */
    private String format(String text) {
        if (isABoolean()) {
            return text;
        }

        StyleFormatter formatter = new StyleFormatter();

        if (style != null) {
            formatter.withStyle(style);
        }

        if (color != null) {
            formatter.withColor(color);
        }

        if (background != null) {
            formatter.withBackground(background);
        }

        return Style.safeApply(formatter, text);
    }

    private boolean isABoolean() {
        return !this.useFormatting || (color == null && style == null && background == null);
    }

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
        super.println(format(x));
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
        super.print(format(x));
    }

    @Override
    public void print(Object x) {
        super.print(format(String.valueOf(x)));
    }

    @Override
    public PrintStream printf(String format, Object... args) {
        return super.printf(format, args);
    }

    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        return super.printf(l, format, args);
    }

    @Override
    public PrintStream format(String format, Object... args) {
        return super.format(format, args);
    }

    @Override
    public PrintStream format(Locale l, String format, Object... args) {
        return super.format(l, format, args);
    }

    @Override
    public PrintStream append(CharSequence csq) {
        return super.append(csq);
    }

    @Override
    public PrintStream append(CharSequence csq, int start, int end) {
        return super.append(csq, start, end);
    }

    @Override
    public PrintStream append(char c) {
        return super.append(c);
    }
}