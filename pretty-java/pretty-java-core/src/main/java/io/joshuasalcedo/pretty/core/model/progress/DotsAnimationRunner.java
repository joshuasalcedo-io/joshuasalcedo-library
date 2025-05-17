package io.joshuasalcedo.pretty.core.model.progress;

import io.joshuasalcedo.pretty.core.properties.RGBColor;

/**
 * A progress runner that displays a simple dots animation.
 * <p>
 * This class displays a message followed by animated dots (.), creating a simple
 * but effective loading indicator. It's particularly useful for indicating ongoing
 * activity where a spinner might be too flashy.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a dots animation
 * DotsAnimationRunner dots = new DotsAnimationRunner("Loading");
 * 
 * // Start the animation
 * dots.start();
 * 
 * // Perform the operation
 * // ...
 * 
 * // Stop the animation
 * dots.stop();
 * </pre>
 */
public class DotsAnimationRunner extends AbstractProgressRunner {

    private RGBColor dotsColor = RGBColor.of(0, 191, 255).withName("dots-blue");
    private int maxDots = 3;
    private char dotChar = '.';
    private int dotsSpacing = 0;

    /**
     * Creates a dots animation progress runner with the given message.
     * 
     * @param message The message to display
     */
    public DotsAnimationRunner(String message) {
        super(message, 300); // Slower update for dots animation
        withIndeterminate(true);
    }

    /**
     * Creates a dots animation progress runner with the given message and update interval.
     * 
     * @param message The message to display
     * @param updateIntervalMs The update interval in milliseconds
     */
    public DotsAnimationRunner(String message, int updateIntervalMs) {
        super(message, updateIntervalMs);
        withIndeterminate(true);
    }

    /**
     * Sets the color for the animated dots.
     * 
     * @param color The RGB color for dots
     * @return This instance for method chaining
     */
    public DotsAnimationRunner setDotsColor(RGBColor color) {
        if (color != null) {
            this.dotsColor = color;
        }
        return this;
    }

    /**
     * Sets the maximum number of dots to display.
     * 
     * @param maxDots The maximum number of dots
     * @return This instance for method chaining
     */
    public DotsAnimationRunner setMaxDots(int maxDots) {
        this.maxDots = Math.max(1, maxDots);
        return this;
    }

    /**
     * Sets the character to use for dots.
     * 
     * @param dotChar The character for dots
     * @return This instance for method chaining
     */
    public DotsAnimationRunner setDotChar(char dotChar) {
        this.dotChar = dotChar;
        return this;
    }

    /**
     * Sets the spacing between dots.
     * 
     * @param spacing The number of spaces between dots
     * @return This instance for method chaining
     */
    public DotsAnimationRunner setDotsSpacing(int spacing) {
        this.dotsSpacing = Math.max(0, spacing);
        return this;
    }

    @Override
    protected void render(StringBuilder display, String message, double progress, int animationStep) {
        // Process message first
        if (message != null && !message.isEmpty()) {
            display.append(applyColor(message, getMessageColor()));
        }

        // Calculate number of dots to show
        int dotsCount = (animationStep % (maxDots + 1));

        // Create the dots string with spacing
        StringBuilder dots = new StringBuilder();
        for (int i = 0; i < dotsCount; i++) {
            dots.append(dotChar);
            if (dotsSpacing > 0 && i < dotsCount - 1) {
                dots.append(" ".repeat(dotsSpacing));
            }
        }

        // Add the dots with color
        display.append(applyColor(dots.toString(), dotsColor));

        // Add padding for stable width
        int maxWidth = maxDots;
        if (dotsSpacing > 0) {
            maxWidth += dotsSpacing * (maxDots - 1);
        }

        // Add spaces to maintain consistent width
        int padding = maxWidth - dots.length();
        if (padding > 0) {
            display.append(" ".repeat(padding));
        }
    }
}
