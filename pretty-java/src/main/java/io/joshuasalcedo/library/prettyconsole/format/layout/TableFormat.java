package io.joshuasalcedo.library.prettyconsole.format.layout;

import io.joshuasalcedo.library.prettyconsole.api.VisualFormat;
import io.joshuasalcedo.library.prettyconsole.format.VisualizationType;
import io.joshuasalcedo.library.prettyconsole.style.ForegroundColor;
import io.joshuasalcedo.library.prettyconsole.style.Style;
import io.joshuasalcedo.library.prettyconsole.utils.TextUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * Formatter that creates a table with customizable column widths and alignment.
 * <p>
 * The TableFormat creates a table for displaying structured data in a tabular format.
 * It supports customizing column widths, alignment, borders, and colors.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a simple table
 * TableFormat table = new TableFormat();
 * String[][] data = {
 *     {"Name", "Age", "City"},
 *     {"John", "30", "New York"},
 *     {"Alice", "25", "London"},
 *     {"Bob", "35", "Paris"}
 * };
 * System.out.println(table.formatTable(data));
 *
 * // Create a customized table
 * TableFormat customTable = new TableFormat()
 *     .withColor(ForegroundColor.BLUE)
 *     .withBorders(true)
 *     .withColumnWidths(20, 5, 15);
 * System.out.println(customTable.formatTable(data));
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public class TableFormat implements VisualFormat {

    // Table border characters
    private static final char TOP_LEFT = '┌';
    private static final char TOP_RIGHT = '┐';
    private static final char BOTTOM_LEFT = '└';
    private static final char BOTTOM_RIGHT = '┘';
    private static final char HORIZONTAL = '─';
    private static final char VERTICAL = '│';
    private static final char TOP_TEE = '┬';
    private static final char BOTTOM_TEE = '┴';
    private static final char LEFT_TEE = '├';
    private static final char RIGHT_TEE = '┤';
    private static final char CROSS = '┼';

    /**
     * Default width for tables
     */
    private static final int DEFAULT_TABLE_WIDTH = 80;

    /**
     * Padding to add to each cell (1 space on each side)
     */
    private static final int CELL_PADDING = 2;

    /**
     * Minimum width for a column after scaling
     */
    private static final int MIN_COLUMN_WIDTH = 3;

    /**
     * Enum for column alignment options.
     */
    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT,
    }

    private final int width;
    private final ForegroundColor color;
    private final boolean borders;
    private final int[] columnWidths;
    private final Alignment[] columnAlignments;

    /**
     * Creates a TableFormat with default settings (80 characters width, white color, no borders).
     */
    public TableFormat() {
        this(DEFAULT_TABLE_WIDTH, ForegroundColor.WHITE, false, null, null);
    }

    /**
     * Creates a TableFormat with the specified width.
     *
     * @param width The width of the table in characters
     */
    public TableFormat(int width) {
        this(width, ForegroundColor.WHITE, false, null, null);
    }

    /**
     * Creates a TableFormat with the specified settings.
     *
     * @param width            The width of the table in characters
     * @param color            The color of the table
     * @param borders          Whether to show borders between cells
     * @param columnWidths     The widths of each column, or null for auto-sizing
     * @param columnAlignments The alignment of each column, or null for left alignment
     */
    public TableFormat(
        int width,
        ForegroundColor color,
        boolean borders,
        int[] columnWidths,
        Alignment[] columnAlignments
    ) {
        this.width = width;
        this.color = color;
        this.borders = borders;
        this.columnWidths = columnWidths;
        this.columnAlignments = columnAlignments;
    }

    @Override
    public VisualizationType getVisualizationType() {
        return VisualizationType.TABLE;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public VisualFormat withWidth(int width) {
        return new TableFormat(width, this.color, this.borders, this.columnWidths, this.columnAlignments);
    }

    /**
     * Creates a new formatter with the specified color.
     *
     * @param color The color of the table
     * @return A new formatter with the updated color
     */
    public TableFormat withColor(ForegroundColor color) {
        return new TableFormat(this.width, color, this.borders, this.columnWidths, this.columnAlignments);
    }

    /**
     * Creates a new formatter with the specified border setting.
     *
     * @param borders Whether to show borders between cells
     * @return A new formatter with the updated border setting
     */
    public TableFormat withBorders(boolean borders) {
        return new TableFormat(this.width, this.color, borders, this.columnWidths, this.columnAlignments);
    }

    /**
     * Creates a new formatter with the specified column widths.
     *
     * @param columnWidths The widths of each column
     * @return A new formatter with the updated column widths
     */
    public TableFormat withColumnWidths(int... columnWidths) {
        return new TableFormat(this.width, this.color, this.borders, columnWidths, this.columnAlignments);
    }

    /**
     * Creates a new formatter with the specified column alignments.
     *
     * @param columnAlignments The alignment of each column
     * @return A new formatter with the updated column alignments
     */
    public TableFormat withColumnAlignments(Alignment... columnAlignments) {
        return new TableFormat(this.width, this.color, this.borders, this.columnWidths, columnAlignments);
    }

    @Override
    public String format(String text) {
        // This method is required by the VisualFormat interface, but tables typically
        // format structured data rather than plain text. This implementation creates
        // a simple one-cell table containing the text.
        String[][] data = { { text } };
        return formatTable(data);
    }

    /**
     * Formats a 2D array of data as a table.
     *
     * @param data The data to format as a table
     * @return The formatted table
     */
    public String formatTable(Object[][] data) {
        if (data == null || data.length == 0) {
            return "";
        }

        // Convert all data to strings
        String[][] stringData = new String[data.length][];
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                stringData[i] = new String[0];
            } else {
                stringData[i] = new String[data[i].length];
                for (int j = 0; j < data[i].length; j++) {
                    stringData[i][j] = data[i][j] == null ? "" : data[i][j].toString();
                }
            }
        }

        return formatStringTable(stringData);
    }

    /**
     * Formats a collection of objects as a table.
     *
     * @param collection The collection to format as a table
     * @return The formatted table
     */
    public String formatTable(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }

        // Convert collection to array
        Object[] array = collection.toArray();
        return formatTable(new Object[][] { array });
    }

    /**
     * Formats a string array as a table.
     *
     * @param data The string data to format as a table
     * @return The formatted table
     */
    private String formatStringTable(String[][] data) {
        if (data == null || data.length == 0) {
            return "";
        }

        // Determine the number of columns
        int numColumns = 0;
        for (String[] row : data) {
            if (row != null) {
                numColumns = Math.max(numColumns, row.length);
            }
        }

        if (numColumns == 0) {
            return "";
        }

        // Determine column widths
        int[] actualColumnWidths = calculateColumnWidths(data, numColumns);

        // Build the table
        StringBuilder result = new StringBuilder();

        // Top border
        if (borders) {
            result.append(createTopBorder(actualColumnWidths));
        }

        // Rows
        for (int i = 0; i < data.length; i++) {
            result.append(formatRow(data[i], actualColumnWidths));

            // Add row separator if not the last row and borders are enabled
            if (borders && i < data.length - 1) {
                result.append(createRowSeparator(actualColumnWidths));
            }
        }

        // Bottom border
        if (borders) {
            result.append(createBottomBorder(actualColumnWidths));
        }

        return result.toString();
    }

    /**
     * Calculates the width of each column based on the data and any specified column widths.
     *
     * @param data       The table data
     * @param numColumns The number of columns
     * @return An array of column widths
     */
    private int[] calculateColumnWidths(String[][] data, int numColumns) {
        int[] result = new int[numColumns];

        // If column widths are specified, use them
        if (columnWidths != null && columnWidths.length > 0) {
            return calculateSpecifiedColumnWidths(numColumns);
        }

        // Calculate column widths based on content
        calculateContentBasedColumnWidths(data, result);

        // Add padding
        addPaddingToColumnWidths(result);

        // Adjust column widths to fit within the total width
        adjustColumnWidthsToFitTotalWidth(result);

        return result;
    }

    /**
     * Calculates column widths based on specified column widths.
     *
     * @param numColumns The number of columns
     * @return An array of column widths
     */
    private int[] calculateSpecifiedColumnWidths(int numColumns) {
        int[] result = new int[numColumns];

        for (int i = 0; i < numColumns; i++) {
            if (i < columnWidths.length) {
                result[i] = columnWidths[i];
            } else {
                // For columns without specified width, use a default
                result[i] = (width - Arrays.stream(result, 0, i).sum()) / (numColumns - i);
            }
        }

        return result;
    }

    /**
     * Calculates column widths based on content.
     *
     * @param data   The table data
     * @param result The array to store the calculated column widths
     */
    private void calculateContentBasedColumnWidths(String[][] data, int[] result) {
        for (String[] row : data) {
            if (row != null) {
                for (int i = 0; i < row.length; i++) {
                    if (row[i] != null) {
                        result[i] = Math.max(result[i], TextUtils.visibleLength(row[i]));
                    }
                }
            }
        }
    }

    /**
     * Adds padding to column widths.
     *
     * @param columnWidths The array of column widths
     */
    private void addPaddingToColumnWidths(int[] columnWidths) {
        for (int i = 0; i < columnWidths.length; i++) {
            columnWidths[i] += CELL_PADDING; // Add 1 space of padding on each side
        }
    }

    /**
     * Adjusts column widths to fit within the total width.
     *
     * @param columnWidths The array of column widths
     */
    private void adjustColumnWidthsToFitTotalWidth(int[] columnWidths) {
        int totalWidth = Arrays.stream(columnWidths).sum();
        if (totalWidth > width) {
            // Scale down columns proportionally
            double scale = (double) width / totalWidth;
            for (int i = 0; i < columnWidths.length; i++) {
                columnWidths[i] = Math.max(MIN_COLUMN_WIDTH, (int) (columnWidths[i] * scale)); // Minimum column width
            }
        }
    }

    /**
     * Creates a horizontal border for the table.
     *
     * @param columnWidths The width of each column
     * @param leftChar The character for the left edge
     * @param middleChar The character for the column separators
     * @param rightChar The character for the right edge
     * @return The border string
     */
    private String createBorder(int[] columnWidths, char leftChar, char middleChar, char rightChar) {
        StringBuilder result = new StringBuilder();
        result.append(Style.apply(color, String.valueOf(leftChar)));

        for (int i = 0; i < columnWidths.length; i++) {
            result.append(Style.apply(color, TextUtils.repeat(HORIZONTAL, columnWidths[i])));
            if (i < columnWidths.length - 1) {
                result.append(Style.apply(color, String.valueOf(middleChar)));
            }
        }

        result.append(Style.apply(color, String.valueOf(rightChar)));
        result.append('\n');
        return result.toString();
    }

    /**
     * Creates the top border of the table.
     *
     * @param columnWidths The width of each column
     * @return The top border string
     */
    private String createTopBorder(int[] columnWidths) {
        return createBorder(columnWidths, TOP_LEFT, TOP_TEE, TOP_RIGHT);
    }

    /**
     * Creates a row separator for the table.
     *
     * @param columnWidths The width of each column
     * @return The row separator string
     */
    private String createRowSeparator(int[] columnWidths) {
        return createBorder(columnWidths, LEFT_TEE, CROSS, RIGHT_TEE);
    }

    /**
     * Creates the bottom border of the table.
     *
     * @param columnWidths The width of each column
     * @return The bottom border string
     */
    private String createBottomBorder(int[] columnWidths) {
        return createBorder(columnWidths, BOTTOM_LEFT, BOTTOM_TEE, BOTTOM_RIGHT);
    }

    /**
     * Formats a row of the table.
     *
     * @param row          The row data
     * @param columnWidths The width of each column
     * @return The formatted row string
     */
    private String formatRow(String[] row, int[] columnWidths) {
        StringBuilder result = new StringBuilder();
        result.append(Style.apply(color, String.valueOf(VERTICAL)));

        for (int i = 0; i < columnWidths.length; i++) {
            String cell = getCellContent(row, i);
            Alignment alignment = getCellAlignment(i);
            String formattedCell = formatCell(cell, columnWidths[i], alignment);

            result.append(formattedCell);
            result.append(Style.apply(color, String.valueOf(VERTICAL)));
        }

        result.append('\n');
        return result.toString();
    }

    /**
     * Gets the content of a cell.
     *
     * @param row The row data
     * @param columnIndex The index of the column
     * @return The cell content
     */
    private String getCellContent(String[] row, int columnIndex) {
        String cell = (row != null && columnIndex < row.length) ? row[columnIndex] : "";
        return cell != null ? cell : "";
    }

    /**
     * Gets the alignment for a cell.
     *
     * @param columnIndex The index of the column
     * @return The alignment for the cell
     */
    private Alignment getCellAlignment(int columnIndex) {
        return (columnAlignments != null && columnIndex < columnAlignments.length)
            ? columnAlignments[columnIndex]
            : Alignment.LEFT;
    }

    /**
     * Formats a cell with the specified alignment and width.
     *
     * @param text      The cell text
     * @param width     The cell width
     * @param alignment The cell alignment
     * @return The formatted cell string
     */
    private String formatCell(String text, int width, Alignment alignment) {
        return switch (alignment) {
            case LEFT -> TextUtils.padRight(text, width, ' ');
            case CENTER -> TextUtils.center(text, width, ' ');
            case RIGHT -> TextUtils.padLeft(text, width, ' ');
            default -> TextUtils.padRight(text, width, ' ');
        };
    }
}
