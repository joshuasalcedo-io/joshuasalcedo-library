package io.joshuasalcedo.library.prettyconsole.core.stream;

import io.joshuasalcedo.library.prettyconsole.PrettyStyle;
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
 * out.color(PrettyStyle.Color.RED).println("This text is red!");
 * out.style(PrettyStyle.Style.BOLD).color(PrettyStyle.Color.GREEN).println("Bold green text");
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public class PrettyPrintStream extends PrintStream {

    private PrettyStyle.Color color;
    private PrettyStyle.Style style;
    private PrettyStyle.Background background;
    private boolean useFormatting = true;

    /**
     * Sets the text color.
     *
     * @param color The color to apply
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream color(PrettyStyle.Color color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the text style.
     *
     * @param style The style to apply
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream style(PrettyStyle.Style style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the background color.
     *
     * @param background The background color to apply
     * @return This PrettyPrintStream for method chaining
     */
    public PrettyPrintStream background(PrettyStyle.Background background) {
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
        if (!useFormatting || (color == null && style == null && background == null)) {
            return text;
        }

        PrettyStyle.Formatter formatter = new PrettyStyle.Formatter();

        if (style != null) {
            formatter.withStyle(style);
        }

        if (color != null) {
            formatter.withColor(color);
        }

        if (background != null) {
            formatter.withBackground(background);
        }

        return PrettyStyle.safeApply(formatter, text);
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
    public void flush() {
        super.flush();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean checkError() {
        return super.checkError();
    }

    @Override
    protected void setError() {
        super.setError();
    }

    @Override
    protected void clearError() {
        super.clearError();
    }

    @Override
    public void write(int b) {
        super.write(b);
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
    }

    @Override
    public void write(byte[] buf) throws IOException {
        super.write(buf);
    }

    @Override
    public void writeBytes(byte[] buf) {
        super.writeBytes(buf);
    }

    @Override
    public void println() {
        super.println();
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

    @Override
    public Charset charset() {
        return super.charset();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
