package io.joshuasalcedo.library.prettyconsole.style;

import io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for creating combinations of styles and colors.
 * <p>
 * This class allows for combining multiple style components (text styles, colors,
 * backgrounds) into a single formatter that can be applied to text.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public class StyleFormatter implements StyleComponent {
    private final List<StyleComponent> components = new ArrayList<>();

    /**
     * ANSI reset code that returns terminal to default state.
     */
    private static final String RESET = "\033[0m";

    /**
     * Add a text style to the formatter.
     *
     * @param style The style to add
     * @return This formatter for method chaining
     */
    public StyleFormatter withStyle(TextStyle style) {
        components.add(style);
        return this;
    }

    /**
     * Add a text color to the formatter.
     *
     * @param color The color to add
     * @return This formatter for method chaining
     */
    public StyleFormatter withColor(ForegroundColor color) {
        components.add(color);
        return this;
    }

    /**
     * Add a background color to the formatter.
     *
     * @param background The background color to add
     * @return This formatter for method chaining
     */
    public StyleFormatter withBackground(BackgroundColor background) {
        components.add(background);
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
    public StyleFormatter withRgbColor(int r, int g, int b) {
        components.add(new RgbColor(r, g, b));
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
    public StyleFormatter withRgbBackground(int r, int g, int b) {
        components.add(new RgbColor(r, g, b, true));
        return this;
    }

    /**
     * Add an 8-bit (256 color) text color to the formatter.
     *
     * @param colorCode Color code (0-255)
     * @return This formatter for method chaining
     */
    public StyleFormatter with256Color(int colorCode) {
        components.add(new Color256(colorCode));
        return this;
    }

    /**
     * Add an 8-bit (256 color) background color to the formatter.
     *
     * @param colorCode Color code (0-255)
     * @return This formatter for method chaining
     */
    public StyleFormatter with256Background(int colorCode) {
        components.add(new Color256(colorCode, true));
        return this;
    }

    /**
     * Add a custom style component to the formatter.
     *
     * @param component The style component to add
     * @return This formatter for method chaining
     */
    public StyleFormatter withComponent(StyleComponent component) {
        components.add(component);
        return this;
    }

    @Override
    public String getCode() {
        StringBuilder sb = new StringBuilder();
        for (StyleComponent component : components) {
            sb.append(component.getCode());
        }
        return sb.toString();
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder("Combined style: ");
        for (int i = 0; i < components.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(components.get(i).getDescription());
        }
        return sb.toString();
    }

    @Override
    public String apply(String text) {
        return getCode() + text + RESET;
    }

    @Override
    public String safeApply(String text) {
        return TerminalUtils.isAnsiSupported() ? apply(text) : text;
    }
}