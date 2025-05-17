package io.joshuasalcedo.pretty.core.model;

import io.joshuasalcedo.pretty.core.model.progress.AbstractProgressRunner;

/**
 * A progress runner that displays a horizontal progress bar.
 * <p>
 * This class provides a horizontal progress bar with customizable appearance,
 * including colors, characters, and optional percentage display.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a progress bar
 * ProgressBarRunner bar = new ProgressBarRunner("Downloading file", 40);
 * 
 * // Start the animation
 * bar.start();
 * 
 * // Update progress as the operation progresses
 * bar.setProgress(0.25);
 * bar.setMessage("Downloading: file.zip");
 * 
 * // Stop the animation when complete
 * bar.stop();
 * </pre>
 */
public class ProgressBarRunner extends AbstractProgressRunner {
    
    // Width of the progress bar in characters
    private final int width;
    
    // Characters to use for different parts of the progress bar
    private char completedChar = '█';
    private char remainingChar = '░';
    private char leadingChar = '▓';
    
    // Whether to display a percentage
    private boolean showPercentage = true;
    
    /**
     * Creates a progress bar with the given message and width.
     * 
     * @param message The message to display
     * @param width The width of the progress bar in characters
     */
    public ProgressBarRunner(String message, int width) {
        super(message);
        this.width = Math.max(5, width); // Ensure a minimum width
    }
    
    /**
     * Creates a progress bar with the given message, width, and update interval.
     * 
     * @param message The message to display
     * @param width The width of the progress bar in characters
     * @param updateIntervalMs The update interval in milliseconds
     */
    public ProgressBarRunner(String message, int width, int updateIntervalMs) {
        super(message, updateIntervalMs);
        this.width = Math.max(5, width); // Ensure a minimum width
    }
    
    /**
     * Sets the characters used for the progress bar.
     * 
     * @param completedChar The character for completed progress
     * @param remainingChar The character for remaining progress
     * @param leadingChar The character for the leading edge of progress
     * @return This instance for method chaining
     */
    public ProgressBarRunner setProgressChars(char completedChar, char remainingChar, char leadingChar) {
        this.completedChar = completedChar;
        this.remainingChar = remainingChar;
        this.leadingChar = leadingChar;
        return this;
    }
    
    /**
     * Sets whether to show percentage alongside the progress bar.
     * 
     * @param showPercentage Whether to show percentage
     * @return This instance for method chaining
     */
    public ProgressBarRunner setShowPercentage(boolean showPercentage) {
        this.showPercentage = showPercentage;
        return this;
    }
    
    /**
     * Uses ASCII characters instead of Unicode for the progress bar.
     * This is useful for terminals that don't support Unicode characters.
     * 
     * @return This instance for method chaining
     */
    public ProgressBarRunner useAsciiChars() {
        this.completedChar = '=';
        this.remainingChar = ' ';
        this.leadingChar = '>';
        return this;
    }
    
    @Override
    protected void render(StringBuilder display, String message, double progress, int animationStep) {
        // Process message first
        if (message != null && !message.isEmpty()) {
            display.append(applyColor(message, getMessageColor())).append(" ");
        }
        
        // Start progress bar
        display.append("[");
        
        if (progress < 0) {
            // Indeterminate mode - show an animated pattern
            renderIndeterminate(display, animationStep);
        } else {
            // Determinate mode - show actual progress
            renderDeterminate(display, progress);
        }
        
        // End progress bar
        display.append("]");
        
        // Show percentage if enabled
        if (showPercentage && progress >= 0) {
            int percent = (int) (progress * 100);
            display.append(" ").append(applyColor(percent + "%", getPercentageColor()));
        }
    }
    
    /**
     * Renders an indeterminate progress bar with animation.
     * 
     * @param display The StringBuilder to append to
     * @param animationStep The current animation step
     */
    private void renderIndeterminate(StringBuilder display, int animationStep) {
        int position = animationStep % (width * 2);
        if (position >= width) {
            position = width * 2 - position; // Reverse direction for bounce effect
        }
        
        for (int i = 0; i < width; i++) {
            if (i == position) {
                display.append(applyColor(String.valueOf(leadingChar), getProgressColor()));
            } else {
                display.append(applyColor(String.valueOf(remainingChar), getRemainingColor()));
            }
        }
    }
    
    /**
     * Renders a determinate progress bar showing actual progress.
     * 
     * @param display The StringBuilder to append to
     * @param progress The current progress (0.0 to 1.0)
     */
    private void renderDeterminate(StringBuilder display, double progress) {
        int completedWidth = (int) (width * progress);
        
        // Ensure the progress bar shows something when progress is more than 0
        if (progress > 0 && completedWidth == 0) {
            completedWidth = 1;
        }
        
        // Draw completed portion
        for (int i = 0; i < completedWidth; i++) {
            display.append(applyColor(String.valueOf(completedChar), getProgressColor()));
        }
        
        // Draw leading character at the edge if we're not at 0% or 100%
        if (progress > 0 && progress < 1.0 && completedWidth < width) {
            display.append(applyColor(String.valueOf(leadingChar), getProgressColor()));
            completedWidth++;
        }
        
        // Draw remaining portion
        for (int i = completedWidth; i < width; i++) {
            display.append(applyColor(String.valueOf(remainingChar), getRemainingColor()));
        }
    }
}