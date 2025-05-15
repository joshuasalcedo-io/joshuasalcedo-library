package io.joshuasalcedo.library.prettyconsole.format.progress;

import io.joshuasalcedo.library.prettyconsole.api.VisualFormat;
import io.joshuasalcedo.library.prettyconsole.format.VisualizationType;
import io.joshuasalcedo.library.prettyconsole.PrettyStyle;
import io.joshuasalcedo.library.prettyconsole.utils.TextUtils;

/**
 * Formatter that creates a horizontal progress bar showing completion status.
 * <p>
 * The ProgressBarFormat creates a visual representation of progress for tasks,
 * downloads, or any process with a measurable completion percentage. The progress
 * bar can be customized with different styles, colors, and characters.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a simple progress bar at 50% completion
 * ProgressBarFormat progressBar = new ProgressBarFormat(0.5);
 * System.out.println(progressBar.format("Downloading..."));
 *
 * // Create a customized progress bar
 * ProgressBarFormat customBar = new ProgressBarFormat(0.75)
 *     .withWidth(50)
 *     .withColor(PrettyStyle.TextColor.BLUE)
 *     .withShowPercentage(true);
 * System.out.println(customBar.format("Processing..."));
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public class ProgressBarFormat implements VisualFormat {

    public static final char DEFAULT_COMPLETE_CHAR = '█';
    public static final char DEFAULT_INCOMPLETE_CHAR = '░';
    public static final char DEFAULT_START_CHAR = '[';
    public static final char DEFAULT_END_CHAR = ']';

    /**
     * Default width for progress bars
     */
    private static final int DEFAULT_PROGRESS_BAR_WIDTH = 40;

    /**
     * Minimum progress value (0%)
     */
    private static final double MIN_PROGRESS = 0.0;

    /**
     * Maximum progress value (100%)
     */
    private static final double MAX_PROGRESS = 1.0;

    /**
     * Width taken by border characters (start and end)
     */
    private static final int BORDER_CHARS_WIDTH = 2;

    /**
     * Multiplier to convert progress fraction to percentage
     */
    private static final int PERCENTAGE_MULTIPLIER = 100;

    private final double progress;
    private final int width;
    private final PrettyStyle.TextColor color;
    private final char completeChar;
    private final char incompleteChar;
    private final char startChar;
    private final char endChar;
    private final boolean showPercentage;

    /**
     * Creates a ProgressBarFormat with the specified progress.
     *
     * @param progress The progress value between 0.0 and 1.0
     */
    public ProgressBarFormat(double progress) {
        this(
            progress,
            DEFAULT_PROGRESS_BAR_WIDTH,
            PrettyStyle.TextColor.GREEN,
            DEFAULT_COMPLETE_CHAR,
            DEFAULT_INCOMPLETE_CHAR,
            DEFAULT_START_CHAR,
            DEFAULT_END_CHAR,
            true
        );
    }

    /**
     * Creates a ProgressBarFormat with the specified progress and width.
     *
     * @param progress The progress value between 0.0 and 1.0
     * @param width    The width of the progress bar in characters
     */
    public ProgressBarFormat(double progress, int width) {
        this(
            progress,
            width,
            PrettyStyle.TextColor.GREEN,
            DEFAULT_COMPLETE_CHAR,
            DEFAULT_INCOMPLETE_CHAR,
            DEFAULT_START_CHAR,
            DEFAULT_END_CHAR,
            true
        );
    }

    /**
     * Creates a ProgressBarFormat with the specified settings.
     *
     * @param progress       The progress value between 0.0 and 1.0
     * @param width          The width of the progress bar in characters
     * @param color          The color of the progress bar
     * @param completeChar   The character to use for completed progress
     * @param incompleteChar The character to use for incomplete progress
     * @param startChar      The character to use for the start of the progress bar
     * @param endChar        The character to use for the end of the progress bar
     * @param showPercentage Whether to show the percentage value
     */
    public ProgressBarFormat(
        double progress,
        int width,
        PrettyStyle.TextColor color,
        char completeChar,
        char incompleteChar,
        char startChar,
        char endChar,
        boolean showPercentage
    ) {
        this.progress = Math.max(MIN_PROGRESS, Math.min(MAX_PROGRESS, progress)); // Clamp between 0 and 1
        this.width = width;
        this.color = color;
        this.completeChar = completeChar;
        this.incompleteChar = incompleteChar;
        this.startChar = startChar;
        this.endChar = endChar;
        this.showPercentage = showPercentage;
    }

    @Override
    public VisualizationType getVisualizationType() {
        return VisualizationType.PROGRESS_BAR;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public VisualFormat withWidth(int width) {
        return new ProgressBarFormat(
            this.progress,
            width,
            this.color,
            this.completeChar,
            this.incompleteChar,
            this.startChar,
            this.endChar,
            this.showPercentage
        );
    }

    /**
     * Creates a new formatter with the specified progress.
     *
     * @param progress The progress value between 0.0 and 1.0
     * @return A new formatter with the updated progress
     */
    public ProgressBarFormat withProgress(double progress) {
        return new ProgressBarFormat(
            progress,
            this.width,
            this.color,
            this.completeChar,
            this.incompleteChar,
            this.startChar,
            this.endChar,
            this.showPercentage
        );
    }

    /**
     * Creates a new formatter with the specified color.
     *
     * @param color The color of the progress bar
     * @return A new formatter with the updated color
     */
    public ProgressBarFormat withColor(PrettyStyle.TextColor color) {
        return new ProgressBarFormat(
            this.progress,
            this.width,
            color,
            this.completeChar,
            this.incompleteChar,
            this.startChar,
            this.endChar,
            this.showPercentage
        );
    }

    /**
     * Creates a new formatter with the specified characters.
     *
     * @param completeChar   The character to use for completed progress
     * @param incompleteChar The character to use for incomplete progress
     * @return A new formatter with the updated characters
     */
    public ProgressBarFormat withChars(char completeChar, char incompleteChar) {
        return new ProgressBarFormat(
            this.progress,
            this.width,
            this.color,
            completeChar,
            incompleteChar,
            this.startChar,
            this.endChar,
            this.showPercentage
        );
    }

    /**
     * Creates a new formatter with the specified border characters.
     *
     * @param startChar The character to use for the start of the progress bar
     * @param endChar   The character to use for the end of the progress bar
     * @return A new formatter with the updated border characters
     */
    public ProgressBarFormat withBorderChars(char startChar, char endChar) {
        return new ProgressBarFormat(
            this.progress,
            this.width,
            this.color,
            this.completeChar,
            this.incompleteChar,
            startChar,
            endChar,
            this.showPercentage
        );
    }

    /**
     * Creates a new formatter with the specified percentage display setting.
     *
     * @param showPercentage Whether to show the percentage value
     * @return A new formatter with the updated percentage display setting
     */
    public ProgressBarFormat withShowPercentage(boolean showPercentage) {
        return new ProgressBarFormat(
            this.progress,
            this.width,
            this.color,
            this.completeChar,
            this.incompleteChar,
            this.startChar,
            this.endChar,
            showPercentage
        );
    }

    @Override
    public String format(String text) {
        StringBuilder result = new StringBuilder();

        // Add the label text if provided
        if (text != null && !text.isEmpty()) {
            result.append(text).append(' ');
        }

        // Calculate the number of complete and incomplete characters
        int barWidth = width - BORDER_CHARS_WIDTH; // Subtract width for the start and end characters
        int completeChars = (int) Math.round(progress * barWidth);
        int incompleteChars = barWidth - completeChars;

        // Build the progress bar
        result.append(String.valueOf(startChar));
        result.append(PrettyStyle.apply(color, TextUtils.repeat(completeChar, completeChars)));
        result.append(TextUtils.repeat(incompleteChar, incompleteChars));
        result.append(String.valueOf(endChar));

        // Add the percentage if enabled
        if (showPercentage) {
            int percent = (int) Math.round(progress * PERCENTAGE_MULTIPLIER);
            result.append(' ').append(percent).append('%');
        }

        return result.toString();
    }
}
