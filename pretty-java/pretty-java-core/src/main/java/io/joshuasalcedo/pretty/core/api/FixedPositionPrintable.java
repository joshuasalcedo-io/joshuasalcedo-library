package io.joshuasalcedo.pretty.core.api;

/**
 * Interface for managing fixed-position terminal output areas.
 * <p>
 * This interface defines methods for creating and updating content in fixed
 * screen positions, enabling dynamic updates without scrolling previous content.
 * </p>
 *
 * @author Joshua Salcedo
 * @version 1.0
 */
public interface FixedPositionPrintable {

    /**
     * Begins a fixed position output area with a context line that remains at the top.
     * This method should be called once to establish the fixed position area.
     *
     * @param contextLine The persistent context line to display at the top
     * @return this object for method chaining
     */
    Object beginFixedArea(String contextLine);

    /**
     * Updates the content in the fixed position area while maintaining the original context line.
     * Previous content in this area will be cleared/overwritten.
     *
     * @param content The new content to display (can be multi-line)
     * @return this object for method chaining
     */
    Object updateFixedArea(String content);

    /**
     * Updates just the context line while preserving the content below.
     *
     * @param newContextLine The new context line text
     * @return this object for method chaining
     */
    Object updateContextLine(String newContextLine);

    /**
     * Appends content to the existing content in the fixed area.
     *
     * @param additionalContent Content to append without clearing previous content
     * @return this object for method chaining
     */
    Object appendToFixedArea(String additionalContent);

    /**
     * Ends the fixed position area and returns to normal printing mode.
     * Subsequent prints will continue from the current position.
     *
     * @return this object for method chaining
     */
    Object endFixedArea();
    
    /**
     * Creates a progress indicator in the fixed area that can be updated.
     *
     * @param initialMessage The initial message to display
     * @return A handle for updating the progress display
     */
    ProgressDisplay createFixedProgress(String initialMessage);
    
    /**
     * Interface for controlling a progress display in a fixed position.
     */
    interface ProgressDisplay {
        /**
         * Updates the progress value.
         *
         * @param progress Value between 0.0 and 1.0
         * @return this display for method chaining
         */
        ProgressDisplay updateProgress(double progress);
        
        /**
         * Updates the message displayed with the progress.
         *
         * @param message The new message
         * @return this display for method chaining
         */
        ProgressDisplay updateMessage(String message);
        
        /**
         * Completes and finalizes the progress display.
         */
        void complete();
    }
}