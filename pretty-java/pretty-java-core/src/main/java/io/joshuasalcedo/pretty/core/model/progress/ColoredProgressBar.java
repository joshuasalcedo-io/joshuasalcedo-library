package io.joshuasalcedo.pretty.core.model.progress;

import io.joshuasalcedo.pretty.core.model.stream.PrettyPrintStream;
import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.theme.TerminalStyle;

/**
 * A colored progress bar implementation using the AbstractProgressRunner.
 * <p>
 * This class provides a horizontal progress bar with customizable colors for
 * different parts of the progress display. It takes advantage of RGB color
 * support to create visually appealing progress indicators.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a colored progress bar
 * ColoredProgressBar progressBar = new ColoredProgressBar("Loading data...", 30)
 *     .setProgressColor(RGBColor.of(50, 205, 50))
 *     .setMessageColor(RGBColor.of(135, 206, 250));
 *
 * // Start the progress animation
 * progressBar.start();
 *
 * // Update progress as your operation proceeds
 * for (int i = 0; i < 100; i++) {
 *     progressBar.setProgress(i / 100.0);
 *     progressBar.setMessage("Processing item " + i);
 *     Thread.sleep(100);
 * }
 *
 * // Stop the progress animation
 * progressBar.stop();
 * </pre>
 */
public class ColoredProgressBar extends AbstractProgressRunner {

    // Width of the progress bar in characters
    private final int width;

    // Characters to use for different parts of the progress bar
    private char completedChar = '█';
    private char remainingChar = '░';
    private char leadingChar = '▓';

    // Whether to display a percentage
    private boolean showPercentage = true;

    /**
     * Creates a colored progress bar with the given initial message and width.
     *
     * @param initialMessage The initial message to display
     * @param width The width of the progress bar in characters
     */
    public ColoredProgressBar(String initialMessage, int width) {
        super(initialMessage);
        this.width = Math.max(5, width); // Ensure a minimum width
    }

    /**
     * Creates a colored progress bar with the given initial message, width, and update interval.
     *
     * @param initialMessage The initial message to display
     * @param width The width of the progress bar in characters
     * @param updateIntervalMs The update interval in milliseconds
     */
    public ColoredProgressBar(String initialMessage, int width, int updateIntervalMs) {
        super(initialMessage, updateIntervalMs);
        this.width = Math.max(5, width); // Ensure a minimum width
    }

    /**
     * Set the characters used for the progress bar.
     *
     * @param completedChar The character for completed progress
     * @param remainingChar The character for remaining progress
     * @param leadingChar The character for the leading edge of progress
     * @return This instance for method chaining
     */
    public ColoredProgressBar setProgressChars(char completedChar, char remainingChar, char leadingChar) {
        this.completedChar = completedChar;
        this.remainingChar = remainingChar;
        this.leadingChar = leadingChar;
        return this;
    }

    /**
     * Set whether to show percentage alongside the progress bar.
     *
     * @param showPercentage Whether to show percentage
     * @return This instance for method chaining
     */
    public ColoredProgressBar setShowPercentage(boolean showPercentage) {
        this.showPercentage = showPercentage;
        return this;
    }

    /**
     * Use ASCII characters instead of Unicode for the progress bar.
     * This is useful for terminals that don't support Unicode characters.
     *
     * @return This instance for method chaining
     */
    public ColoredProgressBar useAsciiChars() {
        this.completedChar = '=';
        this.remainingChar = ' ';
        this.leadingChar = '>';
        return this;
    }

    /**
     * Set the message color for the progress bar.
     *
     * @param color The RGB color for the message
     * @return This instance for method chaining
     */
    public ColoredProgressBar setMessageColor(RGBColor color) {
        withMessageColor(color);
        return this;
    }

    /**
     * Set the progress color for the progress bar.
     *
     * @param color The RGB color for the progress
     * @return This instance for method chaining
     */
    public ColoredProgressBar setProgressColor(RGBColor color) {
        withProgressColor(color);
        return this;
    }

    /**
     * Set the remaining color for the progress bar.
     *
     * @param color The RGB color for the remaining part
     * @return This instance for method chaining
     */
    public ColoredProgressBar setRemainingColor(RGBColor color) {
        withRemainingColor(color);
        return this;
    }

    /**
     * Set the percentage color for the progress bar.
     *
     * @param color The RGB color for the percentage
     * @return This instance for method chaining
     */
    public ColoredProgressBar setPercentageColor(RGBColor color) {
        withPercentageColor(color);
        return this;
    }

    /**
     * Set all colors for the progress bar at once.
     *
     * @param messageColor The RGB color for the message
     * @param progressColor The RGB color for the progress
     * @param remainingColor The RGB color for the remaining part
     * @param percentageColor The RGB color for the percentage
     * @return This instance for method chaining
     */
    public ColoredProgressBar setAllColors(
            RGBColor messageColor,
            RGBColor progressColor,
            RGBColor remainingColor,
            RGBColor percentageColor) {
        withAllColors(messageColor, progressColor, remainingColor, percentageColor);
        return this;
    }

    /**
     * Set a terminal style for the progress bar.
     *
     * @param style The terminal style to use
     * @return This instance for method chaining
     */
    public ColoredProgressBar setStyle(TerminalStyle style) {
        withStyle(style);
        return this;
    }

    /**
     * Update the progress value.
     *
     * @param value The progress value between 0.0 and 1.0
     * @return This instance for method chaining
     */
    public ColoredProgressBar setProgress(double value) {
        withProgress(value);
        return this;
    }

    /**
     * Update the message displayed with the progress bar.
     *
     * @param newMessage The new message to display
     * @return This instance for method chaining
     */
    public ColoredProgressBar setMessage(String newMessage) {
        withMessage(newMessage);
        return this;
    }

    /**
     * Set whether the progress is in indeterminate mode.
     *
     * @param indeterminate Whether the progress is indeterminate
     * @return This instance for method chaining
     */
    public ColoredProgressBar withIndeterminate(boolean indeterminate) {
        withIndeterminate(indeterminate);
        return this;
    }

    /**
     * Set a PrettyPrintStream for the progress bar.
     *
     * @param prettyOut The PrettyPrintStream to use
     * @return This instance for method chaining
     */
    public ColoredProgressBar setPrettyOutputStream(PrettyPrintStream prettyOut) {
        withPrettyOutputStream(prettyOut);
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
     * Render an indeterminate progress bar with animation.
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
     * Render a determinate progress bar showing actual progress.
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

    /**
     * Main method demonstrating the ColoredProgressBar.
     */
    public static void main(String[] args) {
        // Create a PrettyPrintStream for output
        PrettyPrintStream out = new PrettyPrintStream(System.out);

        // Print a header
        out.foreground(RGBColor.fromHex("#FFD700")).bold(true)
                .println("=== RGB Color Progress Bar Demo ===\n");

        try {
            // Basic usage
            out.println("Basic Progress Bar:");
            ColoredProgressBar basic = new ColoredProgressBar("Loading", 30);
            basic.setPrettyOutputStream(out);

            simulateProgress(basic, 1500);

            // Custom colors
            out.println("\nCustom Colored Progress Bar:");
            ColoredProgressBar custom = new ColoredProgressBar("Processing", 40)
                    .setProgressColor(RGBColor.of(255, 69, 0))  // Red-Orange
                    .setRemainingColor(RGBColor.of(30, 30, 30)) // Dark Gray
                    .setMessageColor(RGBColor.of(135, 206, 250)) // Light Sky Blue
                    .setPercentageColor(RGBColor.of(255, 215, 0)); // Gold
            custom.setPrettyOutputStream(out);

            simulateProgress(custom, 1500);

            // ASCII-only progress bar
            out.println("\nASCII Progress Bar (for older terminals):");
            ColoredProgressBar ascii = new ColoredProgressBar("Installing", 50)
                    .useAsciiChars()
                    .setProgressColor(RGBColor.of(0, 191, 255))  // Deep Sky Blue
                    .setMessageColor(RGBColor.of(152, 251, 152)); // Pale Green
            ascii.setPrettyOutputStream(out);

            simulateProgress(ascii, 1500);

            // Terminal Style based progress bar
            out.println("\nTerminal Style Progress Bar:");
            ColoredProgressBar styled = new ColoredProgressBar("Downloading", 35)
                    .setStyle(TerminalStyle.INFO)
                    .setProgressChars('■', '□', '◆');
            styled.setPrettyOutputStream(out);

            simulateProgress(styled, 1500);

            // Indeterminate progress bar
            out.println("\nIndeterminate Progress Bar:");
            ColoredProgressBar indeterminate = new ColoredProgressBar("Searching", 20)
                    .withIndeterminate(true)
                    .setProgressColor(RGBColor.of(138, 43, 226))  // Purple
                    .setMessageColor(RGBColor.of(255, 182, 193)); // Light Pink
            indeterminate.setPrettyOutputStream(out);

            indeterminate.start();
            Thread.sleep(3000);
            indeterminate.stop();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            out.println("Demo interrupted!");
        }

        // Reset formatting
        out.reset();
    }

    /**
     * Helper method to simulate progress for the demo.
     */
    private static void simulateProgress(ColoredProgressBar bar, long duration) throws InterruptedException {
        bar.start();

        long startTime = System.currentTimeMillis();
        long endTime = startTime + duration;

        while (System.currentTimeMillis() < endTime) {
            double elapsed = System.currentTimeMillis() - startTime;
            double progress = elapsed / duration;
            bar.setProgress(progress);

            // Update message occasionally
            if ((int)(progress * 100) % 25 == 0) {
                bar.setMessage(bar.getMessage() + ".");
            }

            Thread.sleep(50);
        }

        bar.setProgress(1.0);
        Thread.sleep(100);
        bar.stop();
    }
}
