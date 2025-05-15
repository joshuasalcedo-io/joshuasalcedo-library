package io.joshuasalcedo.library.prettyconsole.format;

import lombok.Getter;

/**
 * Enumeration of visualization types for console text formatting.
 * <p>
 * This enum defines the different types of visual structures that can be
 * applied to text in the console. Each type represents a distinct visual
 * presentation style that enhances the organization and readability of output.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
@Getter
public enum VisualizationType {
    /**
     * Text surrounded by a box drawn with Unicode box-drawing characters.
     * Useful for highlighting important messages or grouping related content.
     */
    BOX("Text surrounded by a Unicode box for emphasis"),

    /**
     * Horizontal divider line with optional centered text.
     * Useful for separating sections of console output.
     */
    DIVIDER("Horizontal divider with centered text"),

    /**
     * Horizontal progress bar showing completion status.
     * Useful for displaying task progress or percentage-based metrics.
     */
    PROGRESS_BAR("Horizontal progress bar showing completion status"),

    /**
     * Table cell with padding for aligned columns.
     * Useful for creating tabular data displays in the console.
     */
    TABLE_CELL("Table cell with padding for aligned columns"),

    /**
     * Indented text with customizable indentation level.
     * Useful for creating hierarchical displays or code-like formatting.
     */
    INDENTED("Text with customizable indentation level"),

    /**
     * Text centered within a given width.
     * Useful for titles and headings.
     */
    CENTERED("Text centered within a given width"),

    /**
     * Tree-like structure for displaying hierarchical data.
     * Useful for directory trees, organizational charts, etc.
     */
    TREE("Tree-like structure for displaying hierarchical data"),

    /**
     * Text with padding on both sides.
     * Useful for creating consistent spacing.
     */
    PADDED("Text with padding on both sides"),

    /**
     * Table with customizable column widths and alignment.
     * Useful for displaying structured data in a tabular format.
     */
    TABLE("Table with customizable column widths and alignment");

    /**
     * -- GETTER --
     *  Returns the description of this visualization type.
     *
     * @return A detailed description of the visualization type
     */
    private final String description;

    /**
     * Constructor for VisualizationType enum.
     *
     * @param description A detailed description of the visualization type
     */
    VisualizationType(String description) {
        this.description = description;
    }
}
