package io.joshuasalcedo.library.prettyconsole.format.progress;

import io.joshuasalcedo.library.prettyconsole.PrettyStyle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A reusable animated progress bar that runs in a separate thread.
 * <p>
 * This class provides a way to display an animated progress bar in the console
 * that updates automatically in a background thread. It supports different animation
 * styles including progress bars, spinners, and changing text.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a progress bar runner
 * ProgressBarRunner runner = new ProgressBarRunner("Processing data");
 *
 * // Start the animation
 * runner.start();
 *
 * // Update progress as your task progresses
 * for (int i = 0; i <= 100; i++) {
 *     // Do some work...
 *     Thread.sleep(100);
 *
 *     // Update progress (0.0 to 1.0)
 *     runner.setProgress(i / 100.0);
 *
 *     // Optionally update the message
 *     if (i == 50) {
 *         runner.setMessage("Halfway there...");
 *     }
 * }
 *
 * // Stop the animation when done
 * runner.stop();
 *
 * // Print completion message
 * System.out.println("Task completed!");
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public class ProgressBarRunner {

    // Animation types
    public enum AnimationType {
        /** Standard progress bar with percentage */
        PROGRESS_BAR,

        /** Spinning character animation for indeterminate progress */
        SPINNER,

        /** Progress bar with spinner for combined effect */
        PROGRESS_WITH_SPINNER,
    }

    // Default spinner characters
    private static final char[] DEFAULT_SPINNER_CHARS = { '|', '/', '-', '\\' };

    // Thread that handles the animation
    private Thread animationThread;

    // Atomic variables for thread-safe state management
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean indeterminate = new AtomicBoolean(false);
    private final AtomicReference<String> message = new AtomicReference<>("");
    private final AtomicReference<Double> progress = new AtomicReference<>(0.0);

    // Configuration
    private final int width;
    private final int updateIntervalMs;
    private final AnimationType animationType;
    private final PrettyStyle.Color color;
    private final char[] spinnerChars;
    private final boolean showPercentage;
    private final char completeChar;
    private final char incompleteChar;
    private final char startChar;
    private final char endChar;

    /**
     * Creates a progress bar runner with default settings.
     *
     * @param initialMessage The initial message to display
     */
    public ProgressBarRunner(String initialMessage) {
        this(
            initialMessage,
            40,
            100,
            AnimationType.PROGRESS_BAR,
            PrettyStyle.Color.GREEN,
            true,
            DEFAULT_SPINNER_CHARS,
            ProgressBarFormat.DEFAULT_COMPLETE_CHAR,
            ProgressBarFormat.DEFAULT_INCOMPLETE_CHAR,
            ProgressBarFormat.DEFAULT_START_CHAR,
            ProgressBarFormat.DEFAULT_END_CHAR
        );
    }

    /**
     * Creates a progress bar runner with custom width and color.
     *
     * @param initialMessage The initial message to display
     * @param width The width of the progress bar in characters
     * @param color The color of the progress bar
     */
    public ProgressBarRunner(String initialMessage, int width, PrettyStyle.Color color) {
        this(
            initialMessage,
            width,
            100,
            AnimationType.PROGRESS_BAR,
            color,
            true,
            DEFAULT_SPINNER_CHARS,
            ProgressBarFormat.DEFAULT_COMPLETE_CHAR,
            ProgressBarFormat.DEFAULT_INCOMPLETE_CHAR,
            ProgressBarFormat.DEFAULT_START_CHAR,
            ProgressBarFormat.DEFAULT_END_CHAR
        );
    }

    /**
     * Creates a progress bar runner with custom animation type.
     *
     * @param initialMessage The initial message to display
     * @param animationType The type of animation to use
     */
    public ProgressBarRunner(String initialMessage, AnimationType animationType) {
        this(
            initialMessage,
            40,
            100,
            animationType,
            PrettyStyle.Color.GREEN,
            true,
            DEFAULT_SPINNER_CHARS,
            ProgressBarFormat.DEFAULT_COMPLETE_CHAR,
            ProgressBarFormat.DEFAULT_INCOMPLETE_CHAR,
            ProgressBarFormat.DEFAULT_START_CHAR,
            ProgressBarFormat.DEFAULT_END_CHAR
        );
    }

    /**
     * Creates a fully customized progress bar runner.
     *
     * @param initialMessage The initial message to display
     * @param width The width of the progress bar in characters
     * @param updateIntervalMs The update interval in milliseconds
     * @param animationType The type of animation to use
     * @param color The color of the progress bar
     * @param showPercentage Whether to show the percentage
     * @param spinnerChars The characters to use for spinner animation
     * @param completeChar The character to use for completed progress
     * @param incompleteChar The character to use for incomplete progress
     * @param startChar The character to use for the start of the progress bar
     * @param endChar The character to use for the end of the progress bar
     */
    public ProgressBarRunner(
        String initialMessage,
        int width,
        int updateIntervalMs,
        AnimationType animationType,
        PrettyStyle.Color color,
        boolean showPercentage,
        char[] spinnerChars,
        char completeChar,
        char incompleteChar,
        char startChar,
        char endChar
    ) {
        this.message.set(initialMessage);
        this.width = width;
        this.updateIntervalMs = updateIntervalMs;
        this.animationType = animationType;
        this.color = color;
        this.showPercentage = showPercentage;
        this.spinnerChars = spinnerChars;
        this.completeChar = completeChar;
        this.incompleteChar = incompleteChar;
        this.startChar = startChar;
        this.endChar = endChar;

        // Set indeterminate mode for spinner animation
        this.indeterminate.set(animationType == AnimationType.SPINNER);
    }

    /**
     * Starts the progress bar animation in a separate thread.
     */
    public void start() {
        if (running.compareAndSet(false, true)) {
            animationThread = new Thread(this::runAnimation);
            animationThread.setDaemon(true);
            animationThread.start();
        }
    }

    /**
     * Stops the progress bar animation.
     */
    public void stop() {
        if (running.compareAndSet(true, false)) {
            try {
                if (animationThread != null) {
                    animationThread.join(1000); // Wait up to 1 second for thread to finish
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Print a newline to ensure next output starts on a fresh line
            System.out.println();
        }
    }

    /**
     * Updates the progress value.
     *
     * @param value The progress value between 0.0 and 1.0
     */
    public void setProgress(double value) {
        // Clamp value between 0 and 1
        double clampedValue = Math.max(0.0, Math.min(1.0, value));
        progress.set(clampedValue);
    }

    /**
     * Updates the message displayed with the progress bar.
     *
     * @param newMessage The new message to display
     */
    public void setMessage(String newMessage) {
        message.set(newMessage != null ? newMessage : "");
    }

    /**
     * Sets whether the progress bar is in indeterminate mode.
     * In indeterminate mode, a spinner is shown instead of progress.
     *
     * @param indeterminate Whether the progress is indeterminate
     */
    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate.set(indeterminate);
    }

    /**
     * Gets the current progress value.
     *
     * @return The current progress value between 0.0 and 1.0
     */
    public double getProgress() {
        return progress.get();
    }

    /**
     * Gets the current message.
     *
     * @return The current message
     */
    public String getMessage() {
        return message.get();
    }

    /**
     * Checks if the progress bar is currently running.
     *
     * @return true if the progress bar is running, false otherwise
     */
    public boolean isRunning() {
        return running.get();
    }

    /**
     * Checks if the progress bar is in indeterminate mode.
     *
     * @return true if the progress bar is indeterminate, false otherwise
     */
    public boolean isIndeterminate() {
        return indeterminate.get();
    }

    /**
     * Main animation loop that runs in a separate thread.
     */
    private void runAnimation() {
        int spinnerIndex = 0;

        while (running.get()) {
            // Clear the previous line
            System.out.print("\r");

            // Get current state (thread-safe)
            String currentMessage = message.get();
            double currentProgress = progress.get();
            boolean isIndeterminate = indeterminate.get();

            // Build the progress display based on animation type
            StringBuilder display = new StringBuilder();

            // Add the message if present
            if (currentMessage != null && !currentMessage.isEmpty()) {
                display.append(currentMessage).append(" ");
            }

            // Choose animation based on type and indeterminate state
            if (isIndeterminate || animationType == AnimationType.SPINNER) {
                // Spinner animation
                char spinChar = spinnerChars[spinnerIndex % spinnerChars.length];
                display.append(PrettyStyle.apply(color, String.valueOf(spinChar)));
                spinnerIndex++;
            } else {
                // Progress bar animation
                ProgressBarFormat progressBar = new ProgressBarFormat(
                    currentProgress,
                    width,
                    color,
                    completeChar,
                    incompleteChar,
                    startChar,
                    endChar,
                    showPercentage
                );

                // For combined animation, add a spinner character
                if (animationType == AnimationType.PROGRESS_WITH_SPINNER) {
                    char spinChar = spinnerChars[spinnerIndex % spinnerChars.length];
                    display
                        .append(progressBar.format(""))
                        .append(" ")
                        .append(PrettyStyle.apply(color, String.valueOf(spinChar)));
                    spinnerIndex++;
                } else {
                    display.append(progressBar.format(""));
                }
            }

            // Print the current state
            System.out.print(display);

            // Sleep for the update interval
            try {
                Thread.sleep(updateIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Builder class for creating ProgressBarRunner instances with a fluent API.
     */
    public static class Builder {

        private String initialMessage = "";
        private int width = 40;
        private int updateIntervalMs = 100;
        private AnimationType animationType = AnimationType.PROGRESS_BAR;
        private PrettyStyle.Color color = PrettyStyle.Color.GREEN;
        private boolean showPercentage = true;
        private char[] spinnerChars = DEFAULT_SPINNER_CHARS;
        private char completeChar = ProgressBarFormat.DEFAULT_COMPLETE_CHAR;
        private char incompleteChar = ProgressBarFormat.DEFAULT_INCOMPLETE_CHAR;
        private char startChar = ProgressBarFormat.DEFAULT_START_CHAR;
        private char endChar = ProgressBarFormat.DEFAULT_END_CHAR;

        /**
         * Sets the initial message.
         *
         * @param message The initial message to display
         * @return This builder for method chaining
         */
        public Builder message(String message) {
            this.initialMessage = message;
            return this;
        }

        /**
         * Sets the width of the progress bar.
         *
         * @param width The width in characters
         * @return This builder for method chaining
         */
        public Builder width(int width) {
            this.width = width;
            return this;
        }

        /**
         * Sets the update interval.
         *
         * @param intervalMs The update interval in milliseconds
         * @return This builder for method chaining
         */
        public Builder updateInterval(int intervalMs) {
            this.updateIntervalMs = intervalMs;
            return this;
        }

        /**
         * Sets the animation type.
         *
         * @param type The animation type
         * @return This builder for method chaining
         */
        public Builder animationType(AnimationType type) {
            this.animationType = type;
            return this;
        }

        /**
         * Sets the color of the progress bar.
         *
         * @param color The color to use
         * @return This builder for method chaining
         */
        public Builder color(PrettyStyle.Color color) {
            this.color = color;
            return this;
        }

        /**
         * Sets whether to show the percentage.
         *
         * @param show Whether to show the percentage
         * @return This builder for method chaining
         */
        public Builder showPercentage(boolean show) {
            this.showPercentage = show;
            return this;
        }

        /**
         * Sets the spinner characters.
         *
         * @param chars The characters to use for spinner animation
         * @return This builder for method chaining
         */
        public Builder spinnerChars(char[] chars) {
            this.spinnerChars = chars;
            return this;
        }

        /**
         * Sets the characters used for the progress bar.
         *
         * @param complete The character for completed progress
         * @param incomplete The character for incomplete progress
         * @return This builder for method chaining
         */
        public Builder progressChars(char complete, char incomplete) {
            this.completeChar = complete;
            this.incompleteChar = incomplete;
            return this;
        }

        /**
         * Sets the border characters for the progress bar.
         *
         * @param start The character for the start of the progress bar
         * @param end The character for the end of the progress bar
         * @return This builder for method chaining
         */
        public Builder borderChars(char start, char end) {
            this.startChar = start;
            this.endChar = end;
            return this;
        }

        /**
         * Builds a new ProgressBarRunner with the configured settings.
         *
         * @return A new ProgressBarRunner instance
         */
        public ProgressBarRunner build() {
            return new ProgressBarRunner(
                initialMessage,
                width,
                updateIntervalMs,
                animationType,
                color,
                showPercentage,
                spinnerChars,
                completeChar,
                incompleteChar,
                startChar,
                endChar
            );
        }
    }

    /**
     * Creates a new builder for constructing a ProgressBarRunner.
     *
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
}
