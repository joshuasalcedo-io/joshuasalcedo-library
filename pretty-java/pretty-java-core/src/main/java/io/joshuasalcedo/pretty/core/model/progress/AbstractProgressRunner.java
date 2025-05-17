package io.joshuasalcedo.pretty.core.model.progress;

import io.joshuasalcedo.pretty.core.model.stream.PrettyPrintStream;
import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.theme.TerminalStyle;
import io.joshuasalcedo.pretty.core.utils.TerminalUtils;

import java.io.PrintStream;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An abstract base class for progress indicators in CLI applications with RGB color support.
 * <p>
 * This class provides the core functionality for running and managing a progress
 * indicator in a background thread, with support for updating progress values
 * and messages. It uses RGBColor for true color terminal output and supports
 * output to PrettyPrintStream for enhanced visualization.
 * </p>
 * <p>
 * Example implementation:
 * </p>
 * <pre>
 * public class ColoredProgressBar extends AbstractProgressRunner {
 *     private final int width;
 *     private final RGBColor completedColor;
 *     private final RGBColor remainingColor;
 *
 *     public ColoredProgressBar(String initialMessage, int width) {
 *         super(initialMessage, 100);
 *         this.width = width;
 *         this.completedColor = RGBColor.of(50, 205, 50);  // Green
 *         this.remainingColor = RGBColor.of(100, 100, 100); // Gray
 *     }
 *
 *     @Override
 *     protected void render(StringBuilder display, String message, double progress, int animationStep) {
 *         if (!message.isEmpty()) {
 *             display.append(message).append(" ");
 *         }
 *
 *         display.append("[");
 *         int completedChars = (int) (width * progress);
 *         for (int i = 0; i < width; i++) {
 *             if (i < completedChars) {
 *                 display.append(completedColor.apply("="));
 *             } else {
 *                 display.append(remainingColor.apply(" "));
 *             }
 *         }
 *         display.append("] ").append((int)(progress * 100)).append("%");
 *     }
 * }
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */

public abstract class AbstractProgressRunner implements AutoCloseable {

    // Default colors for progress indicators
    private static final RGBColor DEFAULT_MESSAGE_COLOR = RGBColor.of(255, 255, 255).withName("message-white");
    private static final RGBColor DEFAULT_PROGRESS_COLOR = RGBColor.of(50, 205, 50).withName("progress-green");
    private static final RGBColor DEFAULT_REMAINING_COLOR = RGBColor.of(100, 100, 100).withName("remaining-gray");
    private static final RGBColor DEFAULT_PERCENTAGE_COLOR = RGBColor.of(255, 215, 0).withName("percentage-gold");

    // Executor service that handles the animation
    private ScheduledExecutorService scheduler;

    // Future for the animation task
    private ScheduledFuture<?> animationTask;

    // Atomic variables for thread-safe state management
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean indeterminate = new AtomicBoolean(false);
    private final AtomicReference<String> message = new AtomicReference<>("");
    private final AtomicReference<Double> progress = new AtomicReference<>(0.0);

    // Configuration
    private final int updateIntervalMs;

    // Animation step counter
    private int animationStep = 0;

    // Colors for styling the progress indicator
    private RGBColor messageColor = DEFAULT_MESSAGE_COLOR;
    private RGBColor progressColor = DEFAULT_PROGRESS_COLOR;
    private RGBColor remainingColor = DEFAULT_REMAINING_COLOR;
    private RGBColor percentageColor = DEFAULT_PERCENTAGE_COLOR;

    // The output stream to use for displaying the progress
    private PrintStream outputStream = System.out;
    private PrettyPrintStream prettyOutput = null;

    // Terminal style for predefined styling
    private TerminalStyle terminalStyle = null;

    // Whether to use ANSI colors (auto-detected by default)
    private boolean useColors = TerminalUtils.isAnsiSupported();

    // Whether to clear the line before rendering (useful for terminals that don't support carriage return)
    private boolean clearLine = true;

    /**
     * Creates a progress runner with the given initial message and update interval.
     *
     * @param initialMessage The initial message to display
     * @param updateIntervalMs The update interval in milliseconds
     */
    public AbstractProgressRunner(String initialMessage, int updateIntervalMs) {
        this.message.set(initialMessage != null ? initialMessage : "");
        this.updateIntervalMs = updateIntervalMs;
    }

    /**
     * Creates a progress runner with the given initial message and default update interval.
     *
     * @param initialMessage The initial message to display
     */
    public AbstractProgressRunner(String initialMessage) {
        this(initialMessage, 100);
    }

    /**
     * Set the output stream for the progress indicator.
     * This will create a new PrettyPrintStream wrapper around the provided stream.
     *
     * @param out The output stream to use
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withOutputStream(PrintStream out) {
        if (out != null) {
            this.outputStream = out;

            // If the stream is already a PrettyPrintStream, use it directly
            if (out instanceof PrettyPrintStream) {
                this.prettyOutput = (PrettyPrintStream) out;
            } else {
                // Otherwise, create a new PrettyPrintStream wrapper
                this.prettyOutput = new PrettyPrintStream(out);
            }
        }
        return this;
    }

    /**
     * Set a PrettyPrintStream for the progress indicator.
     *
     * @param prettyOut The PrettyPrintStream to use
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withPrettyOutputStream(PrettyPrintStream prettyOut) {
        if (prettyOut != null) {
            this.prettyOutput = prettyOut;
            this.outputStream = prettyOut;
        }
        return this;
    }

    /**
     * Set whether to use ANSI colors for the progress indicator.
     *
     * @param useColors Whether to use ANSI colors
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withColors(boolean useColors) {
        this.useColors = useColors;
        return this;
    }

    /**
     * Set whether to clear the line before rendering.
     *
     * @param clearLine Whether to clear the line
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withLineClear(boolean clearLine) {
        this.clearLine = clearLine;
        return this;
    }

    /**
     * Set the color for the message part of the progress indicator.
     *
     * @param color The RGB color for the message
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withMessageColor(RGBColor color) {
        if (color != null) {
            this.messageColor = color;
        }
        return this;
    }

    /**
     * Set the color for the progress part of the indicator.
     *
     * @param color The RGB color for the progress
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withProgressColor(RGBColor color) {
        if (color != null) {
            this.progressColor = color;
        }
        return this;
    }

    /**
     * Set the color for the remaining part of the progress indicator.
     *
     * @param color The RGB color for the remaining part
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withRemainingColor(RGBColor color) {
        if (color != null) {
            this.remainingColor = color;
        }
        return this;
    }

    /**
     * Set the color for the percentage part of the progress indicator.
     *
     * @param color The RGB color for the percentage
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withPercentageColor(RGBColor color) {
        if (color != null) {
            this.percentageColor = color;
        }
        return this;
    }

    /**
     * Set all colors for the progress indicator at once.
     *
     * @param messageColor The RGB color for the message
     * @param progressColor The RGB color for the progress
     * @param remainingColor The RGB color for the remaining part
     * @param percentageColor The RGB color for the percentage
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withAllColors(
            RGBColor messageColor,
            RGBColor progressColor,
            RGBColor remainingColor,
            RGBColor percentageColor) {

        withMessageColor(messageColor);
        withProgressColor(progressColor);
        withRemainingColor(remainingColor);
        withPercentageColor(percentageColor);

        return this;
    }

    /**
     * Set a terminal style for the progress indicator.
     * This will override individual color settings.
     *
     * @param style The terminal style to use
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withStyle(TerminalStyle style) {
        this.terminalStyle = style;
        return this;
    }

    /**
     * Updates the progress value.
     *
     * @param value The progress value between 0.0 and 1.0
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withProgress(double value) {
        // Clamp value between 0 and 1
        double clampedValue = Math.min(1.0, Math.max(0.0, value));
        progress.set(clampedValue);
        return this;
    }

    /**
     * Updates the message displayed with the progress indicator.
     *
     * @param newMessage The new message to display
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withMessage(String newMessage) {
        message.set(newMessage != null ? newMessage : "");
        return this;
    }

    /**
     * Sets whether the progress is in indeterminate mode.
     * In indeterminate mode, progress value is ignored and an animation is shown.
     *
     * @param indeterminate Whether the progress is indeterminate
     * @return This instance for method chaining
     */
    public AbstractProgressRunner withIndeterminate(boolean indeterminate) {
        this.indeterminate.set(indeterminate);
        return this;
    }

    /**
     * Starts the progress indicator animation using a scheduled executor.
     *
     * @return This instance for method chaining
     */
    public AbstractProgressRunner start() {
        if (running.compareAndSet(false, true)) {
            // Create a new scheduler
            scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });

            // Schedule the animation task to run at fixed intervals
            animationTask = scheduler.scheduleAtFixedRate(
                    this::updateAnimation,
                    0,
                    updateIntervalMs,
                    TimeUnit.MILLISECONDS
            );
        }
        return this;
    }

    /**
     * Stops the progress indicator animation.
     *
     * @return This instance for method chaining
     */
    public AbstractProgressRunner stop() {
        if (running.compareAndSet(true, false)) {
            try {
                if (animationTask != null) {
                    animationTask.cancel(false);
                }
                if (scheduler != null) {
                    scheduler.shutdown();
                    // Wait for tasks to complete
                    if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                        scheduler.shutdownNow();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                scheduler.shutdownNow();
            }
            // Print a newline to ensure next output starts on a fresh line
            outputStream.println();
        }
        return this;
    }

    /**
     * Implements AutoCloseable to allow use in try-with-resources.
     */
    @Override
    public void close() {
        stop();
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
     * Checks if the progress indicator is currently running.
     *
     * @return true if the progress indicator is running, false otherwise
     */
    public boolean isRunning() {
        return running.get();
    }

    public AbstractProgressRunner withRunner(boolean b){
         running.set(Boolean.valueOf(b));
         return this;
    }

    /**
     * Checks if the progress indicator is in indeterminate mode.
     *
     * @return true if the progress indicator is indeterminate, false otherwise
     */
    public boolean isIndeterminate() {
        return indeterminate.get();
    }

    /**
     * Apply color to a string if color usage is enabled.
     *
     * @param text The text to color
     * @param color The color to apply
     * @return The colored text if colors are enabled, the original text otherwise
     */
    protected String applyColor(String text, RGBColor color) {
        if (useColors && color != null) {
            return color.apply(text);
        }
        return text;
    }

    /**
     * Apply a terminal style to a string if color usage is enabled.
     *
     * @param text The text to style
     * @param style The style to apply
     * @return The styled text if colors are enabled, the original text otherwise
     */
    protected String applyStyle(String text, TerminalStyle style) {
        if (useColors && style != null) {
            return style.apply(text);
        }
        return text;
    }

    /**
     * Single animation update that is scheduled to run at fixed intervals.
     */
    private void updateAnimation() {
        // Only proceed if we're still running
        if (!running.get()) {
            return;
        }

        // Clear the previous line
        if (clearLine) {
            outputStream.print("\r");
        }

        // Get current state (thread-safe)
        String currentMessage = message.get();
        double currentProgress = progress.get();
        boolean isIndeterminate = indeterminate.get();

        // Build the progress display
        StringBuilder display = new StringBuilder();

        // Let the subclass render the progress indicator
        if (isIndeterminate) {
            // In indeterminate mode, pass -1 as progress
            render(display, currentMessage, -1, animationStep);
        } else {
            render(display, currentMessage, currentProgress, animationStep);
        }

        // Print the current state
        if (prettyOutput != null) {
            // When using PrettyPrintStream, we need to clear the line and print without a newline
            prettyOutput.print(display.toString());
        } else {
            // When using a regular PrintStream, just print
            outputStream.print(display);
        }

        // Increment animation step for next update
        animationStep++;
    }

    /**
     * Abstract method that concrete subclasses must implement to render the
     * progress indicator to the display.
     *
     * @param display The StringBuilder to append the rendered progress indicator to
     * @param message The current message
     * @param progress The current progress (between 0.0 and 1.0, or -1 if indeterminate)
     * @param animationStep The current animation step (incremented on each update)
     */
    protected abstract void render(StringBuilder display, String message, double progress, int animationStep);

    /**
     * Helper method to get the message color.
     * If a terminal style is set, it uses the foreground color from that style.
     *
     * @return The RGB color for messages
     */
    protected RGBColor getMessageColor() {
        return terminalStyle != null ? terminalStyle.getForeground() : messageColor;
    }

    /**
     * Helper method to get the progress color.
     * If a terminal style is set, it uses the SUCCESS style for progress.
     *
     * @return The RGB color for progress
     */
    protected RGBColor getProgressColor() {
        // For progress, we might want to use a success-related color
        return terminalStyle != null ? TerminalStyle.SUCCESS.getForeground() : progressColor;
    }

    /**
     * Helper method to get the remaining color.
     * If a terminal style is set, it uses a darker version of the foreground.
     *
     * @return The RGB color for the remaining part
     */
    protected RGBColor getRemainingColor() {
        return terminalStyle != null ? terminalStyle.getForeground().darker() : remainingColor;
    }

    /**
     * Helper method to get the percentage color.
     * If a terminal style is set, it uses a highlight color.
     *
     * @return The RGB color for percentages
     */
    protected RGBColor getPercentageColor() {
        return terminalStyle != null ? TerminalStyle.HIGHLIGHT.getForeground() : percentageColor;
    }
}