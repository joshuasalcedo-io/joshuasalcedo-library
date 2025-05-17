package io.joshuasalcedo.pretty.core.model;

import io.joshuasalcedo.pretty.core.model.progress.AbstractProgressRunner;
import io.joshuasalcedo.pretty.core.properties.RGBColor;

/**
 * A progress runner that displays a spinner animation.
 * <p>
 * This class displays an animated spinner along with a message to indicate ongoing activity.
 * It's particularly useful for tasks where progress percentage is not available,
 * such as API calls, waiting for a service to respond, or other indeterminate operations.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a spinner with default spinner type
 * SpinnerProgressRunner spinner = new SpinnerProgressRunner("Loading data...");
 * 
 * // Start the animation
 * spinner.start();
 * 
 * // Perform the operation
 * // ...
 * 
 * // Stop the animation
 * spinner.stop();
 * </pre>
 */
public class SpinnerProgressRunner extends AbstractProgressRunner {

    /**
     * Enum defining different spinner animation types.
     */
    public enum SpinnerType {
        /**
         * Classic line spinner (|, /, -, \)
         */
        LINE(new String[] {"|", "/", "-", "\\"}),

        /**
         * Dots spinner (⠋, ⠙, ⠹, ⠸, ⠼, ⠴, ⠦, ⠧, ⠇, ⠏)
         */
        DOTS(new String[] {"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"}),

        /**
         * Arrow spinner (←, ↖, ↑, ↗, →, ↘, ↓, ↙)
         */
        ARROW(new String[] {"←", "↖", "↑", "↗", "→", "↘", "↓", "↙"}),

        /**
         * Circle spinner (◐, ◓, ◑, ◒)
         */
        CIRCLE(new String[] {"◐", "◓", "◑", "◒"}),

        /**
         * Star spinner (✶, ✸, ✹, ✺, ✹, ✷)
         */
        STAR(new String[] {"✶", "✸", "✹", "✺", "✹", "✷"}),

        /**
         * Box spinner (▖, ▘, ▝, ▗)
         */
        BOX(new String[] {"▖", "▘", "▝", "▗"}),

        /**
         * Flip spinner (_, _, _, _, _, _, )
         */
        FLIP(new String[] {"_", "_", "_", "-", "`", "`", "'", "´", "-", "_", "_", "_"}),

        /**
         * Bar spinner ([    ], [=   ], [==  ], [=== ], [====], [ ===], [  ==], [   =], [    ])
         */
        BAR(new String[] {"[    ]", "[=   ]", "[==  ]", "[=== ]", "[====]", "[ ===]", "[  ==]", "[   =]", "[    ]"}),

        /**
         * Simple ASCII spinner for terminals that don't support Unicode
         */
        ASCII(new String[] {"|", "/", "-", "\\"});

        private final String[] frames;

        SpinnerType(String[] frames) {
            this.frames = frames;
        }

        /**
         * Gets the frame at the specified index.
         * 
         * @param index The frame index
         * @return The frame string
         */
        public String getFrame(int index) {
            return frames[index % frames.length];
        }

        /**
         * Gets the number of frames in this spinner type.
         * 
         * @return The number of frames
         */
        public int getFrameCount() {
            return frames.length;
        }
    }

    private SpinnerType spinnerType = SpinnerType.DOTS;
    private RGBColor spinnerColor = RGBColor.of(50, 205, 50).withName("spinner-green");
    private boolean showMessageFirst = true;

    /**
     * Creates a spinner progress runner with the given message.
     * 
     * @param message The message to display
     */
    public SpinnerProgressRunner(String message) {
        super(message, 100); // Update every 100ms by default
        withIndeterminate(true); // Spinners are always indeterminate
    }

    /**
     * Creates a spinner progress runner with the given message and update interval.
     * 
     * @param message The message to display
     * @param updateIntervalMs The update interval in milliseconds
     */
    public SpinnerProgressRunner(String message, int updateIntervalMs) {
        super(message, updateIntervalMs);
        withIndeterminate(true);
    }

    /**
     * Sets the spinner type.
     * 
     * @param spinnerType The spinner type to use
     * @return This instance for method chaining
     */
    public SpinnerProgressRunner setSpinnerType(SpinnerType spinnerType) {
        this.spinnerType = spinnerType;
        return this;
    }

    /**
     * Sets the spinner color.
     * 
     * @param color The RGB color for the spinner
     * @return This instance for method chaining
     */
    public SpinnerProgressRunner setSpinnerColor(RGBColor color) {
        if (color != null) {
            this.spinnerColor = color;
        }
        return this;
    }

    /**
     * Sets whether to show the message before or after the spinner.
     * 
     * @param messageFirst Whether to show the message first
     * @return This instance for method chaining
     */
    public SpinnerProgressRunner setShowMessageFirst(boolean messageFirst) {
        this.showMessageFirst = messageFirst;
        return this;
    }

    @Override
    protected void render(StringBuilder display, String message, double progress, int animationStep) {
        // Get the current frame of the spinner
        String spinnerFrame = spinnerType.getFrame(animationStep);

        // Apply color to the spinner
        String coloredSpinner = applyColor(spinnerFrame, spinnerColor);

        // Arrange message and spinner based on configuration
        if (showMessageFirst) {
            if (message != null && !message.isEmpty()) {
                display.append(applyColor(message, getMessageColor())).append(" ");
            }
            display.append(coloredSpinner);
        } else {
            display.append(coloredSpinner);
            if (message != null && !message.isEmpty()) {
                display.append(" ").append(applyColor(message, getMessageColor()));
            }
        }
    }
}
