package io.joshuasalcedo.pretty.core.api;

import java.util.List;
import java.util.Map;

/**
 * An interface for printing collections and data structures in a rich, formatted way.
 * <p>
 * This interface defines methods for rendering collections such as lists, tables,
 * trees, charts, and other data structures in a visually appealing manner for
 * terminal output.
 * </p>
 *
 * @author Joshua Salcedo
 * @version 1.0
 */
public interface CollectionPrintable {

    /**
     * Prints a table from a list of objects or arrays.
     *
     * @param data The data to display in the table
     * @param headers Column headers (can be null for auto-generation)
     * @param borderStyle Style for table borders
     * @return this object for method chaining
     */
    Object printTable(List<?> data, String[] headers, TableBorderStyle borderStyle);
    
    /**
     * Prints a simple table from data.
     *
     * @param data The data to display in the table
     * @return this object for method chaining
     */
    Object printTable(List<?> data);
    
    /**
     * Prints a table from a two-dimensional array.
     *
     * @param data The 2D array of data to display
     * @param headers Column headers (can be null for auto-generation)
     * @return this object for method chaining
     */
    Object printTable(Object[][] data, String[] headers);
    
    /**
     * Prints a tree structure for hierarchical data.
     *
     * @param rootNode The root node of the tree
     * @param maxDepth Maximum depth to display (negative for unlimited)
     * @return this object for method chaining
     */
    Object printTree(TreeNode rootNode, int maxDepth);
    
    /**
     * Prints a bulleted list of items.
     *
     * @param items The items to print as a bulleted list
     * @param bulletStyle The style of bullets to use
     * @return this object for method chaining
     */
    Object printBulletList(List<String> items, BulletStyle bulletStyle);
    
    /**
     * Prints a simple bulleted list with default bullet style.
     *
     * @param items The items to print as a bulleted list
     * @return this object for method chaining
     */
    Object printBulletList(List<String> items);
    
    /**
     * Prints a numbered list of items.
     *
     * @param items The items to print as a numbered list
     * @param startNumber The number to start counting from
     * @return this object for method chaining
     */
    Object printNumberedList(List<String> items, int startNumber);
    
    /**
     * Prints a simple numbered list starting from 1.
     *
     * @param items The items to print as a numbered list
     * @return this object for method chaining
     */
    Object printNumberedList(List<String> items);
    
    /**
     * Prints multiple key-value pairs formatted in a consistent way.
     *
     * @param data Map of key-value pairs to display
     * @return this object for method chaining
     */
    Object printKeyValues(Map<String, Object> data);
    
    /**
     * Prints a definition list with terms and their descriptions.
     *
     * @param terms Map of terms to their descriptions
     * @return this object for method chaining
     */
    Object printDefinitionList(Map<String, String> terms);
    
    /**
     * Prints a simple bar chart using ASCII/Unicode characters.
     *
     * @param data The data points for the chart
     * @param labels The labels for each data point
     * @param title Optional title for the chart
     * @return this object for method chaining
     */
    Object printBarChart(double[] data, String[] labels, String title);
    
    /**
     * Prints a horizontal bar chart.
     *
     * @param data The data points for the chart
     * @param labels The labels for each data point
     * @param maxWidth Maximum width for the bars in characters
     * @return this object for method chaining
     */
    Object printHorizontalBarChart(double[] data, String[] labels, int maxWidth);
    
    /**
     * Prints a heatmap visualization of a 2D array of values.
     *
     * @param data The 2D array of data to visualize
     * @param rowLabels Optional labels for rows (can be null)
     * @param colLabels Optional labels for columns (can be null)
     * @return this object for method chaining
     */
    Object printHeatmap(double[][] data, String[] rowLabels, String[] colLabels);
    
    /**
     * Prints a Venn diagram of set relationships.
     *
     * @param sets Array of set names
     * @param sizes Array of set sizes (must match sets length)
     * @param intersections Map of intersection descriptions to sizes
     * @return this object for method chaining
     */
    Object printVennDiagram(String[] sets, int[] sizes, Map<String, Integer> intersections);
    
    /**
     * Prints a columnar list with multiple parallel data streams.
     *
     * @param columns Array of lists representing each column
     * @param headers Column headers (can be null)
     * @param columnWidths Array of column widths (can be null for auto-sizing)
     * @return this object for method chaining
     */
    Object printColumns(List<?>[] columns, String[] headers, int[] columnWidths);
    
    /**
     * Prints a grid layout of items.
     *
     * @param items The items to arrange in a grid
     * @param columns Number of columns in the grid
     * @return this object for method chaining
     */
    Object printGrid(List<?> items, int columns);
    
    // Enum definitions specific to collection printing
    
    /**
     * Styles for table borders.
     */
    enum TableBorderStyle {
        SINGLE, DOUBLE, ROUNDED, BOLD, ASCII, MINIMAL, MARKDOWN, NONE
    }
    
    /**
     * Styles for bullets in lists.
     */
    enum BulletStyle {
        DOT, CIRCLE, SQUARE, ARROW, STAR, DASH, PLUS, CUSTOM
    }
    
    /**
     * Types of data visualizations.
     */
    enum ChartType {
        BAR, LINE, PIE, SCATTER, HEATMAP, HISTOGRAM
    }
    
    /**
     * A node in a tree structure for printTree method.
     */
    interface TreeNode {
        /**
         * Gets the label for this node.
         *
         * @return the node label
         */
        String getLabel();
        
        /**
         * Gets the children of this node.
         *
         * @return list of child nodes
         */
        List<TreeNode> getChildren();
        
        /**
         * Optional method to get additional data about this node.
         *
         * @return additional node data or null if none
         */
        default Object getData() {
            return null;
        }
    }
}