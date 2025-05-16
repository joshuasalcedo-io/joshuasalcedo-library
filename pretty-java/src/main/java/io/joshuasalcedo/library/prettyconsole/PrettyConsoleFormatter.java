package io.joshuasalcedo.library.prettyconsole;

import io.joshuasalcedo.library.prettyconsole.api.MessageFormat;
import io.joshuasalcedo.library.prettyconsole.format.FormatFactory;
import io.joshuasalcedo.library.prettyconsole.format.layout.BoxFormat;
import io.joshuasalcedo.library.prettyconsole.format.layout.DividerFormat;
import io.joshuasalcedo.library.prettyconsole.format.progress.ProgressBarFormat;
import io.joshuasalcedo.library.prettyconsole.format.text.MessageType;
import lombok.Getter;

/**
 * Main entry point for the Pretty Console library.
 * <p>
 * This class provides static methods for accessing the library's functionality.
 * It serves as a facade for the various formatters and styling components.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
@Getter
public final class PrettyConsoleFormatter {

    /**
     * Private constructor to prevent instantiation.
     */
    private PrettyConsoleFormatter() {
        // Utility class, prevent instantiation
    }

    // Divider formatters

    /**
     * Creates a divider formatter with default settings.
     *
     * @return A new divider formatter
     */
    public static DividerFormat divider() {
        return FormatFactory.divider();
    }

    /**
     * Creates a divider formatter with the specified width.
     *
     * @param width The width of the divider in characters
     * @return A new divider formatter
     */
    public static DividerFormat divider(int width) {
        return FormatFactory.divider(width);
    }

    // Box formatters

    /**
     * Creates a box formatter with default settings.
     *
     * @return A new box formatter
     */
    public static BoxFormat box() {
        return FormatFactory.box();
    }

    /**
     * Creates a box formatter with the specified width.
     *
     * @param width The width of the box in characters
     * @return A new box formatter
     */
    public static BoxFormat box(int width) {
        return FormatFactory.box(width);
    }

    // Message formatters

    /**
     * Creates a message formatter for the specified message type.
     *
     * @param messageType The message type to format
     * @return A new message formatter
     */
    public static MessageFormat message(MessageType messageType) {
        return FormatFactory.message(messageType);
    }

    /**
     * Creates a message formatter for the specified message type without a prefix.
     *
     * @param messageType The message type to format
     * @return A new message formatter without a prefix
     */
    public static MessageFormat messageWithoutPrefix(MessageType messageType) {
        return FormatFactory.messageWithoutPrefix(messageType);
    }

    // Progress bar formatters

    /**
     * Creates a progress bar formatter with the specified progress.
     *
     * @param progress The progress value between 0.0 and 1.0
     * @return A new progress bar formatter
     */
    public static ProgressBarFormat progressBar(double progress) {
        return FormatFactory.progressBar(progress);
    }

    /**
     * Creates a progress bar formatter with the specified progress and width.
     *
     * @param progress The progress value between 0.0 and 1.0
     * @param width    The width of the progress bar in characters
     * @return A new progress bar formatter
     */
    public static ProgressBarFormat progressBar(double progress, int width) {
        return FormatFactory.progressBar(progress, width);
    }
}
