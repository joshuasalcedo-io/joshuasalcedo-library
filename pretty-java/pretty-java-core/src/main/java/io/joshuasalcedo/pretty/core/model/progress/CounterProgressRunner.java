package io.joshuasalcedo.pretty.core.model.progress;

import io.joshuasalcedo.pretty.core.properties.RGBColor;

/**
 * A progress runner that displays a counter.
 * <p>
 * This class displays a counter that increments based on progress, useful for tasks 
 * where you want to show progress as a count of items processed out of a total.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a counter progress indicator for 100 items
 * CounterProgressRunner counter = new CounterProgressRunner("Processing files", 100);
 * 
 * // Start the animation
 * counter.start();
 * 
 * // Update as items are processed
 * counter.setProgress(0.25); // Will show 25/100
 * 
 * // Stop the animation when complete
 * counter.stop();
 * </pre>
 */
public class CounterProgressRunner extends AbstractProgressRunner {
    
    // Total count to reach
    private final int total;
    
    // Color for the counter numbers
    private RGBColor counterColor = RGBColor.of(255, 215, 0).withName("counter-gold");
    
    // Format for the counter display
    private String format = "\n%d/%d";
    
    // Whether to show brackets around the counter
    private boolean showBrackets = true;
    
    /**
     * Creates a counter progress runner with the given message and total.
     * 
     * @param message The message to display
     * @param total The total count to reach
     */
    public CounterProgressRunner(String message, int total) {
        super(message);
        this.total = Math.max(1, total); // Ensure a minimum of 1
    }
    
    /**
     * Creates a counter progress runner with the given message, total, and update interval.
     * 
     * @param message The message to display
     * @param total The total count to reach
     * @param updateIntervalMs The update interval in milliseconds
     */
    public CounterProgressRunner(String message, int total, int updateIntervalMs) {
        super(message, updateIntervalMs);
        this.total = Math.max(1, total); // Ensure a minimum of 1
    }
    
    /**
     * Sets the color for the counter numbers.
     * 
     * @param color The RGB color for the counter
     * @return This instance for method chaining
     */
    public CounterProgressRunner setCounterColor(RGBColor color) {
        if (color != null) {
            this.counterColor = color;
        }
        return this;
    }
    
    /**
     * Sets the format for the counter display.
     * The format should include two placeholders for current and total values.
     * Default is "%d/%d".
     * 
     * @param format The format string
     * @return This instance for method chaining
     */
    public CounterProgressRunner setFormat(String format) {
        if (format != null && format.contains("%d")) {
            this.format = format;
        }
        return this;
    }
    
    /**
     * Sets whether to show brackets around the counter.
     * 
     * @param showBrackets Whether to show brackets
     * @return This instance for method chaining
     */
    public CounterProgressRunner setShowBrackets(boolean showBrackets) {
        this.showBrackets = showBrackets;
        return this;
    }
    
    @Override
    protected void render(StringBuilder display, String message, double progress, int animationStep) {
        // Process message first
        if (message != null && !message.isEmpty()) {
            display.append(applyColor(message, getMessageColor())).append(" ");
        }
        
        int current;
        if (progress < 0) {
            // Indeterminate mode - show an animated count
            current = animationStep % (total + 1);
        } else {
            // Determinate mode - show actual progress
            current = (int) (progress * total);
            // Ensure current doesn't exceed total
            current = Math.min(current, total);
        }
        
        // Format the counter
        String counterText = String.format(format, current, total);
        
        // Add brackets if enabled
        if (showBrackets) {
            counterText = "[" + counterText + "]";
        }
        
        // Apply color to the counter
        display.append(applyColor(counterText, counterColor));
        
        // Show percentage if progress is determinate
        if (progress >= 0) {
            int percent = (int) (progress * 100);
            display.append(" ").append(applyColor("(" + percent + "%)", getPercentageColor()));
        }
    }
}