package io.joshuasalcedo.library.prettyconsole.format.layout;

import io.joshuasalcedo.library.prettyconsole.api.VisualFormat;
import io.joshuasalcedo.library.prettyconsole.format.VisualizationType;
import io.joshuasalcedo.library.prettyconsole.style.ForegroundColor;
import io.joshuasalcedo.library.prettyconsole.style.Style;
import io.joshuasalcedo.library.prettyconsole.utils.TextUtils;

/**
 * Formatter that creates a horizontal divider line with optional centered text.
 * <p>
 * The DividerFormat creates a horizontal divider line to separate sections of console
 * output. The divider can optionally include centered text and can be customized with
 * different line characters, colors, and widths.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a simple divider
 * DividerFormat divider = new DividerFormat();
 * System.out.println(divider.format("Section Title"));
 *
 * // Create a customized divider
 * DividerFormat customDivider = new DividerFormat()
 *     .withWidth(100)
 *     .withLineChar('=')
 *     .withColor(ForegroundColor.BLUE)
 *     .withSpacing(true);
 * System.out.println(customDivider.format("Important Section"));
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public class DividerFormat implements VisualFormat {

    private final int width;
    private final char lineChar;
    private final ForegroundColor color;
    private final boolean withSpacing;

    /**
     * Creates a DividerFormat with default settings (80 characters width, '─' character).
     */
    public DividerFormat() {
        this(80, '─', ForegroundColor.WHITE, true);
    }

    /**
     * Creates a DividerFormat with the specified width.
     *
     * @param width The width of the divider in characters
     */
    public DividerFormat(int width) {
        this(width, '─', ForegroundColor.WHITE, true);
    }

    /**
     * Creates a DividerFormat with the specified settings.
     *
     * @param width The width of the divider in characters
     * @param lineChar The character to use for the divider line
     * @param color The color of the divider
     * @param withSpacing Whether to add blank lines before and after the divider
     */
    public DividerFormat(int width, char lineChar, ForegroundColor color, boolean withSpacing) {
        this.width = width;
        this.lineChar = lineChar;
        this.color = color;
        this.withSpacing = withSpacing;
    }

    @Override
    public VisualizationType getVisualizationType() {
        return VisualizationType.DIVIDER;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public VisualFormat withWidth(int width) {
        return new DividerFormat(width, this.lineChar, this.color, this.withSpacing);
    }

    /**
     * Creates a new formatter with the specified line character.
     *
     * @param lineChar The character to use for the divider line
     * @return A new formatter with the updated line character
     */
    public DividerFormat withLineChar(char lineChar) {
        return new DividerFormat(this.width, lineChar, this.color, this.withSpacing);
    }

    /**
     * Creates a new formatter with the specified color.
     *
     * @param color The color of the divider
     * @return A new formatter with the updated color
     */
    public DividerFormat withColor(ForegroundColor color) {
        return new DividerFormat(this.width, this.lineChar, color, this.withSpacing);
    }

    /**
     * Creates a new formatter with the specified spacing setting.
     *
     * @param withSpacing Whether to add blank lines before and after the divider
     * @return A new formatter with the updated spacing setting
     */
    public DividerFormat withSpacing(boolean withSpacing) {
        return new DividerFormat(this.width, this.lineChar, this.color, withSpacing);
    }

    @Override
    public String format(String text) {
        StringBuilder result = new StringBuilder();

        // Add spacing before the divider if enabled
        if (withSpacing) {
            result.append('\n');
        }

        if (text == null || text.isEmpty()) {
            // Simple divider line with no text
            result.append(Style.apply(color, TextUtils.repeat(lineChar, width)));
        } else {
            // Divider with centered text
            int textVisibleLength = TextUtils.visibleLength(text);

            // Ensure there's enough space for the text and at least 4 line characters
            if (textVisibleLength + 4 > width) {
                // Text is too long, truncate it
                text = TextUtils.truncate(text, width - 6);
                textVisibleLength = TextUtils.visibleLength(text);
            }

            // Calculate the number of line characters on each side of the text
            int lineCharsPerSide = (width - textVisibleLength - 2) / 2;
            int leftLineChars = lineCharsPerSide;
            int rightLineChars = width - textVisibleLength - 2 - leftLineChars;

            result.append(
                Style.apply(
                    color,
                    TextUtils.repeat(lineChar, leftLineChars) +
                    ' ' +
                    text +
                    ' ' +
                    TextUtils.repeat(lineChar, rightLineChars)
                )
            );
        }

        // Add spacing after the divider if enabled
        if (withSpacing) {
            result.append('\n');
        }

        return result.toString();
    }
}
