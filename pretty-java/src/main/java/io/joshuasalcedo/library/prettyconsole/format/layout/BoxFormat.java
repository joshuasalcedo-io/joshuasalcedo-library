package io.joshuasalcedo.library.prettyconsole.format.layout;

import io.joshuasalcedo.library.prettyconsole.api.VisualFormat;
import io.joshuasalcedo.library.prettyconsole.format.VisualizationType;
import io.joshuasalcedo.library.prettyconsole.style.ForegroundColor;
import io.joshuasalcedo.library.prettyconsole.style.Style;
import io.joshuasalcedo.library.prettyconsole.utils.TextUtils;

/**
 * Formatter that creates a box around text using Unicode box-drawing characters.
 * <p>
 * The BoxFormat creates a rectangular box around text to highlight important messages
 * or group related content. The box can be customized with different styles, colors,
 * and widths.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a simple box
 * BoxFormat box = new BoxFormat();
 * System.out.println(box.format("Important message"));
 *
 * // Create a customized box
 * BoxFormat customBox = new BoxFormat()
 *     .withWidth(50)
 *     .withColor(ForegroundColor.BLUE)
 *     .withPadding(1);
 * System.out.println(customBox.format("Custom box with padding"));
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public class BoxFormat implements VisualFormat {

    // Box drawing characters
    private static final char TOP_LEFT = '┌';
    private static final char TOP_RIGHT = '┐';
    private static final char BOTTOM_LEFT = '└';
    private static final char BOTTOM_RIGHT = '┘';
    private static final char HORIZONTAL = '─';
    private static final char VERTICAL = '│';

    // Default values
    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_PADDING = 0;
    private static final int BORDER_WIDTH = 2;

    private final int width;
    private final ForegroundColor color;
    private final int padding;

    /**
     * Creates a BoxFormat with default settings (80 characters width, white color, no padding).
     */
    public BoxFormat() {
        this(DEFAULT_WIDTH, ForegroundColor.WHITE, DEFAULT_PADDING);
    }

    /**
     * Creates a BoxFormat with the specified width.
     *
     * @param width The width of the box in characters
     */
    public BoxFormat(int width) {
        this(width, ForegroundColor.WHITE, DEFAULT_PADDING);
    }

    /**
     * Creates a BoxFormat with the specified settings.
     *
     * @param width   The width of the box in characters
     * @param color   The color of the box
     * @param padding The padding inside the box (number of spaces around the text)
     */
    public BoxFormat(int width, ForegroundColor color, int padding) {
        this.width = width;
        this.color = color;
        this.padding = padding;
    }

    @Override
    public VisualizationType getVisualizationType() {
        return VisualizationType.BOX;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public VisualFormat withWidth(int width) {
        return new BoxFormat(width, this.color, this.padding);
    }

    /**
     * Creates a new formatter with the specified color.
     *
     * @param color The color of the box
     * @return A new formatter with the updated color
     */
    public BoxFormat withColor(ForegroundColor color) {
        return new BoxFormat(this.width, color, this.padding);
    }

    /**
     * Creates a new formatter with the specified padding.
     *
     * @param padding The padding inside the box (number of spaces around the text)
     * @return A new formatter with the updated padding
     */
    public BoxFormat withPadding(int padding) {
        return new BoxFormat(this.width, this.color, padding);
    }

    @Override
    public String format(String text) {
        if (text == null) {
            text = "";
        }

        // Calculate the content width (box width minus borders)
        int contentWidth = width - BORDER_WIDTH;

        // Split the text into lines that fit within the content width
        String[] lines = splitTextIntoLines(text, contentWidth);

        StringBuilder result = new StringBuilder();

        // Top border
        result.append(Style.apply(color, TOP_LEFT + TextUtils.repeat(HORIZONTAL, contentWidth) + TOP_RIGHT));
        result.append('\n');

        // Add top padding if needed
        for (int i = 0; i < padding; i++) {
            result.append(Style.apply(color, VERTICAL + TextUtils.repeat(' ', contentWidth) + VERTICAL));
            result.append('\n');
        }

        // Content lines
        for (String line : lines) {
            // Pad the line to fill the content width
            String paddedLine = TextUtils.padRight(line, contentWidth, ' ');
            result.append(Style.apply(color, VERTICAL + paddedLine + VERTICAL));
            result.append('\n');
        }

        // Add bottom padding if needed
        for (int i = 0; i < padding; i++) {
            result.append(Style.apply(color, VERTICAL + TextUtils.repeat(' ', contentWidth) + VERTICAL));
            result.append('\n');
        }

        // Bottom border
        result.append(
            Style.apply(color, BOTTOM_LEFT + TextUtils.repeat(HORIZONTAL, contentWidth) + BOTTOM_RIGHT)
        );

        return result.toString();
    }

    /**
     * Splits text into lines that fit within the specified width.
     *
     * @param text  The text to split
     * @param width The maximum width of each line
     * @return An array of lines
     */
    private String[] splitTextIntoLines(String text, int width) {
        if (text == null || text.isEmpty()) {
            return new String[] { "" };
        }

        // Simple implementation - in a real-world scenario, you might want to
        // implement word wrapping logic that doesn't break words
        int textLength = TextUtils.visibleLength(text);
        if (textLength <= width) {
            return new String[] { text };
        }

        // Count how many lines we need
        int lineCount = (textLength + width - 1) / width;
        String[] lines = new String[lineCount];

        int startIndex = 0;
        for (int i = 0; i < lineCount; i++) {
            int endIndex = Math.min(startIndex + width, text.length());
            lines[i] = text.substring(startIndex, endIndex);
            startIndex = endIndex;
        }

        return lines;
    }
}
