package io.joshuasalcedo.library.prettyconsole.style;

/**
 * String decorator class for fluent formatting.
 * <p>
 * This class provides a fluent API for applying styles to strings. It allows
 * for chaining multiple style operations and then retrieving the styled text.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public class StringDecorator {
    private final String original;
    private final StyleFormatter formatter = new StyleFormatter();

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
    public StringDecorator style(TextStyle style) {
        formatter.withStyle(style);
        return this;
    }

    /**
     * Apply a foreground color to the text.
     *
     * @param color The color to apply
     * @return This decorator for method chaining
     */
    public StringDecorator color(ForegroundColor color) {
        formatter.withColor(color);
        return this;
    }

    /**
     * Apply a background color to the text.
     *
     * @param background The background color to apply
     * @return This decorator for method chaining
     */
    public StringDecorator bg(BackgroundColor background) {
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
     * Apply a custom style component to the text.
     *
     * @param component The style component to apply
     * @return This decorator for method chaining
     */
    public StringDecorator format(StyleComponent component) {
        formatter.withComponent(component);
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