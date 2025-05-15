package io.joshuasalcedo.library.prettyconsole.api;

import io.joshuasalcedo.library.prettyconsole.format.VisualizationType;

/**
 * Interface for formatters that apply visual structure to text.
 * <p>
 * Visual formatters focus on the structural presentation of text rather than
 * semantic styling. They create visual elements like boxes, tables, dividers,
 * and progress bars to enhance the organization and readability of console output.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public interface VisualFormat extends TextFormat {
    /**
     * Gets the visualization type associated with this formatter.
     *
     * @return The visualization type this formatter implements
     */
    VisualizationType getVisualizationType();

    /**
     * Returns the width this formatter will use for the visual element.
     *
     * @return The width in characters, or -1 if auto-width
     */
    int getWidth();

    /**
     * Creates a new formatter with the specified width.
     *
     * @param width The width in characters, or -1 for auto-width
     * @return A new formatter with the updated width
     */
    VisualFormat withWidth(int width);
}
