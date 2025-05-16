package io.joshuasalcedo.library.prettyconsole.format;

import io.joshuasalcedo.library.prettyconsole.PrettyStyle;
import io.joshuasalcedo.library.prettyconsole.api.MessageFormat;
import io.joshuasalcedo.library.prettyconsole.api.TextFormat;
import io.joshuasalcedo.library.prettyconsole.format.layout.BoxFormat;
import io.joshuasalcedo.library.prettyconsole.format.layout.DividerFormat;
import io.joshuasalcedo.library.prettyconsole.format.layout.FileTreeFormat;
import io.joshuasalcedo.library.prettyconsole.format.progress.ProgressBarFormat;
import io.joshuasalcedo.library.prettyconsole.format.progress.ProgressBarRunner;
import io.joshuasalcedo.library.prettyconsole.format.text.BasicMessageFormat;
import io.joshuasalcedo.library.prettyconsole.format.text.MessageType;
import io.joshuasalcedo.library.prettyconsole.style.ForegroundColor;

import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * Factory class for creating formatters.
 * <p>
 * This class provides static methods for creating various types of formatters
 * with default or custom settings. It serves as a convenient entry point for
 * accessing the formatting capabilities of the library.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a divider with default settings
 * System.out.println(FormatFactory.divider().format("Section Title"));
 *
 * // Create a divider with custom width
 * System.out.println(FormatFactory.divider(100).format("Wide Section"));
 *
 * // Create a message formatter for errors
 * System.out.println(FormatFactory.message(MessageType.ERROR).format("Something went wrong!"));
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public final class FormatFactory {

    private FormatFactory() {
        // Utility class, prevent instantiation
    }

    //-------------------------------------------------------------------------
    // Divider Formatters
    //-------------------------------------------------------------------------

    /**
     * Creates a divider formatter with default settings.
     *
     * @return A new divider formatter
     */
    public static DividerFormat divider() {
        return new DividerFormat();
    }

    /**
     * Creates a divider formatter with the specified width.
     *
     * @param width The width of the divider in characters
     * @return A new divider formatter
     */
    public static DividerFormat divider(int width) {
        return new DividerFormat(width);
    }

    /**
     * Creates a divider formatter with the specified width and line character.
     *
     * @param width The width of the divider in characters
     * @param lineChar The character to use for the divider line
     * @return A new divider formatter
     */
    public static DividerFormat divider(int width, char lineChar) {
        return new DividerFormat(width, lineChar, ForegroundColor.WHITE, true);
    }

    /**
     * Creates a divider formatter with the specified settings.
     *
     * @param width The width of the divider in characters
     * @param lineChar The character to use for the divider line
     * @param color The color of the divider
     * @param withSpacing Whether to add blank lines before and after the divider
     * @return A new divider formatter
     */
    public static DividerFormat divider(int width, char lineChar, ForegroundColor color, boolean withSpacing) {
        return new DividerFormat(width, lineChar, color, withSpacing);
    }

    //-------------------------------------------------------------------------
    // Box Formatters
    //-------------------------------------------------------------------------

    /**
     * Creates a box formatter with default settings.
     *
     * @return A new box formatter
     */
    public static BoxFormat box() {
        return new BoxFormat();
    }

    /**
     * Creates a box formatter with the specified width.
     *
     * @param width The width of the box in characters
     * @return A new box formatter
     */
    public static BoxFormat box(int width) {
        return new BoxFormat(width);
    }

    /**
     * Creates a box formatter with the specified settings.
     *
     * @param width   The width of the box in characters
     * @param color   The color of the box
     * @param padding The padding inside the box (number of spaces around the text)
     * @return A new box formatter
     */
    public static BoxFormat box(int width, ForegroundColor color, int padding) {
        return new BoxFormat(width, color, padding);
    }

    //-------------------------------------------------------------------------
    // Progress Bar Formatters
    //-------------------------------------------------------------------------

    /**
     * Creates a progress bar formatter with the specified progress.
     *
     * @param progress The progress value between 0.0 and 1.0
     * @return A new progress bar formatter
     */
    public static ProgressBarFormat progressBar(double progress) {
        return new ProgressBarFormat(progress);
    }

    /**
     * Creates a progress bar formatter with the specified progress and width.
     *
     * @param progress The progress value between 0.0 and 1.0
     * @param width    The width of the progress bar in characters
     * @return A new progress bar formatter
     */
    public static ProgressBarFormat progressBar(double progress, int width) {
        return new ProgressBarFormat(progress, width);
    }

    /**
     * Creates a progress bar formatter with the specified settings.
     *
     * @param progress       The progress value between 0.0 and 1.0
     * @param width          The width of the progress bar in characters
     * @param color          The color of the progress bar
     * @param showPercentage Whether to show the percentage value
     * @return A new progress bar formatter
     */
    public static ProgressBarFormat progressBar(
        double progress,
        int width,
        PrettyStyle.TextColor  color,
        boolean showPercentage
    ) {
        return new ProgressBarFormat(
            progress,
            width,
            color,
            ProgressBarFormat.DEFAULT_COMPLETE_CHAR,
            ProgressBarFormat.DEFAULT_INCOMPLETE_CHAR,
            ProgressBarFormat.DEFAULT_START_CHAR,
            ProgressBarFormat.DEFAULT_END_CHAR,
            showPercentage
        );
    }

    //-------------------------------------------------------------------------
    // Animated Progress Bar Runners
    //-------------------------------------------------------------------------

    /**
     * Creates a progress bar runner with default settings.
     *
     * @param message The initial message to display
     * @return A new progress bar runner
     */
    public static ProgressBarRunner progressBarRunner(String message) {
        return new ProgressBarRunner(message);
    }

    /**
     * Creates a progress bar runner with custom width and color.
     *
     * @param message The initial message to display
     * @param width The width of the progress bar in characters
     * @param color The color of the progress bar
     * @return A new progress bar runner
     */
    public static ProgressBarRunner progressBarRunner(String message, int width, PrettyStyle.TextColor color) {
        return new ProgressBarRunner(message, width, color);
    }

    /**
     * Creates a progress bar runner with custom animation type.
     *
     * @param message The initial message to display
     * @param animationType The type of animation to use
     * @return A new progress bar runner
     */
    public static ProgressBarRunner progressBarRunner(String message, ProgressBarRunner.AnimationType animationType) {
        return new ProgressBarRunner(message, animationType);
    }

    /**
     * Creates a builder for constructing a customized progress bar runner.
     *
     * @return A new progress bar runner builder
     */
    public static ProgressBarRunner.Builder progressBarRunnerBuilder() {
        return ProgressBarRunner.builder();
    }

    //-------------------------------------------------------------------------
    // Message Formatters
    //-------------------------------------------------------------------------

    /**
     * Creates a message formatter for the specified message type.
     *
     * @param messageType The message type to format
     * @return A new message formatter
     */
    public static MessageFormat message(MessageType messageType) {
        return new BasicMessageFormat(messageType);
    }

    /**
     * Creates a message formatter for the specified message type without a prefix.
     *
     * @param messageType The message type to format
     * @return A new message formatter without a prefix
     */
    public static MessageFormat messageWithoutPrefix(MessageType messageType) {
        return new BasicMessageFormat(messageType).withPrefix(false);
    }

    /**
     * Creates a boxed message formatter for the specified message type.
     * This combines a message formatter with a box formatter.
     *
     * @param messageType The message type to format
     * @return A new boxed message formatter
     */
    public static TextFormat boxedMessage(MessageType messageType) {
        // This is a placeholder implementation
        // In a complete implementation, this would create a box formatter
        // and combine it with a message formatter
        return message(messageType);
    }

    //-------------------------------------------------------------------------
    // File Tree Formatters
    //-------------------------------------------------------------------------

    /**
     * Creates a file tree formatter with default settings.
     *
     * @return A new file tree formatter
     */
    public static FileTreeFormat fileTree() {
        return new FileTreeFormat();
    }

    /**
     * Creates a file tree formatter with the specified width.
     *
     * @param width The width of the tree display in characters
     * @return A new file tree formatter
     */
    public static FileTreeFormat fileTree(int width) {
        return new FileTreeFormat(width);
    }

    /**
     * Creates a file tree formatter with the specified color.
     *
     * @param color The color of the tree structure
     * @return A new file tree formatter
     */
    public static FileTreeFormat fileTree(ForegroundColor color) {
        return new FileTreeFormat().withColor(color);
    }

    /**
     * Creates a file tree formatter with the specified maximum depth.
     *
     * @param maxDepth The maximum depth of the tree to display
     * @return A new file tree formatter
     */
    public static FileTreeFormat fileTreeWithDepth(int maxDepth) {
        return new FileTreeFormat().withMaxDepth(maxDepth);
    }

    /**
     * Creates a file tree formatter with the specified file filter.
     *
     * @param filter The filter to apply to files
     * @return A new file tree formatter
     */
    public static FileTreeFormat fileTreeWithFilter(FileFilter filter) {
        return new FileTreeFormat().withFileFilter(filter);
    }

    /**
     * Creates a file tree formatter with the specified filename filter.
     *
     * @param filter The filter to apply to filenames
     * @return A new file tree formatter
     */
    public static FileTreeFormat fileTreeWithFilter(FilenameFilter filter) {
        return new FileTreeFormat().withFilenameFilter(filter);
    }

    /**
     * Creates a file tree formatter with custom settings.
     *
     * @param width    The width of the tree display in characters
     * @param color    The color of the tree structure
     * @param maxDepth The maximum depth of the tree to display
     * @return A new file tree formatter
     */
    public static FileTreeFormat fileTree(int width, ForegroundColor color, int maxDepth) {
        return new FileTreeFormat(width, color, maxDepth, null, null);
    }
}
