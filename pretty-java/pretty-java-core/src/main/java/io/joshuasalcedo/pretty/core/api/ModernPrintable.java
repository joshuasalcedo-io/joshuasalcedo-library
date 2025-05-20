package io.joshuasalcedo.pretty.core.api;

import java.io.File;
import java.util.Map;

/**
 * An interface for modern terminal output capabilities beyond basic printing.
 * <p>
 * This interface defines methods for rich terminal UI elements such as boxes,
 * dividers, formatted JSON, and other advanced static output formats
 * commonly used in modern CLI applications.
 * </p>
 *
 * @author Joshua Salcedo
 * @version 1.2
 */
public interface ModernPrintable {

    /**
     * Prints a box around text with customizable border styles.
     *
     * @param content The text to display inside the box
     * @param title Optional title for the box (can be null)
     * @param borderStyle The style to use for the border (single, double, rounded, etc.)
     * @return this object for method chaining
     */
    Object printBox(String content, String title, BorderStyle borderStyle);

    /**
     * Prints a simple box around text with default border style.
     *
     * @param content The text to display inside the box
     * @return this object for method chaining
     */
    Object printBox(String content);

    /**
     * Prints a horizontal divider with optional centered text.
     *
     * @param centerText Text to display in the center of the divider (can be null)
     * @param width Width of the divider in characters
     * @param style The style of divider to use
     * @return this object for method chaining
     */
    Object printDivider(String centerText, int width, DividerStyle style);

    /**
     * Prints a simple horizontal divider using the default style.
     *
     * @return this object for method chaining
     */
    Object printDivider();

    /**
     * Prints the contents of a file with optional syntax highlighting.
     *
     * @param file The file to print
     * @param language Language for syntax highlighting (can be null for auto-detection)
     * @param showLineNumbers Whether to show line numbers
     * @return this object for method chaining
     */
    Object printFile(File file, String language, boolean showLineNumbers);

    /**
     * Prints a code block with syntax highlighting.
     *
     * @param code The code to print
     * @param language The programming language for syntax highlighting
     * @return this object for method chaining
     */
    Object printCode(String code, String language);

    /**
     * Prints formatted JSON with syntax highlighting and proper indentation.
     *
     * @param jsonString The JSON string to format and print
     * @param colorized Whether to apply color syntax highlighting
     * @return this object for method chaining
     */
    Object printJson(String jsonString, boolean colorized);

    /**
     * Prints a Java object as formatted JSON.
     *
     * @param object The object to convert to JSON and print
     * @return this object for method chaining
     */
    Object printJson(Object object);

    /**
     * Prints a Map as formatted JSON.
     *
     * @param map The map to convert to JSON and print
     * @return this object for method chaining
     */
    Object printJson(Map<?, ?> map);

    /**
     * Prints a titled section header with optional styling.
     *
     * @param title The section title
     * @param level The heading level (1 for main heading, 2 for subheading, etc.)
     * @return this object for method chaining
     */
    Object printSection(String title, int level);

    /**
     * Prints a simple section header.
     *
     * @param title The section title
     * @return this object for method chaining
     */
    Object printSection(String title);

    /**
     * Prints a static snapshot of a progress bar.
     * Note: This prints a non-animated progress bar at the current state.
     * For animated progress bars, use the InteractivePrintable interface.
     *
     * @param progress Value between 0.0 and 1.0 representing completion
     * @return this object for method chaining
     */
    Object printProgressSnapshot(double progress);

    /**
     * Prints a clickable hyperlink using OSC 8 escape sequences.
     *
     * @param text The text to display
     * @param url The URL to link to
     * @return this object for method chaining
     */
    Object printHyperlink(String text, String url);

    /**
     * Prints a clickable file link for IDEs and supported terminals.
     *
     * @param filePath The path to the file
     * @param lineNumber The line number to highlight
     * @param columnNumber The column number (optional, can be 0)
     * @return this object for method chaining
     */
    Object printFileLink(String filePath, int lineNumber, int columnNumber);

    /**
     * Prints a simple file link with just line number (column defaults to 0).
     *
     * @param filePath The path to the file
     * @param lineNumber The line number to highlight
     * @return this object for method chaining
     */
    Object printFileLink(String filePath, int lineNumber);

    /**
     * Prints a notification or callout with appropriate styling.
     *
     * @param message The message to display
     * @param type The type of notification (info, warning, error, success)
     * @return this object for method chaining
     */
    Object printNotification(String message, NotificationType type);

    /**
     * Prints a large banner text for prominent headers.
     *
     * @param text The text to display as a banner
     * @param style The style of the banner
     * @return this object for method chaining
     */
    Object printBanner(String text, BannerStyle style);

    /**
     * Prints a simple banner with default style.
     *
     * @param text The text to display as a banner
     * @return this object for method chaining
     */
    Object printBanner(String text);

    /**
     * Prints simple key-value pairs formatted in a consistent way.
     *
     * @param key The key label
     * @param value The value to display
     * @return this object for method chaining
     */
    Object printKeyValue(String key, Object value);

    /**
     * Prints a collapsible section that can be expanded in terminals that support it.
     *
     * @param title The section title
     * @param content The content inside the collapsible section
     * @return this object for method chaining
     */
    Object printCollapsible(String title, String content);

    /**
     * Prints elapsed time in a human-readable format.
     *
     * @param startTimeMillis The start time in milliseconds
     * @return this object for method chaining
     */
    Object printElapsedTime(long startTimeMillis);

    /**
     * Prints a beautifully formatted stack trace for an exception.
     *
     * @param throwable The exception to print
     * @return this object for method chaining
     */
    Object printException(Throwable throwable);

    // Enum definitions for the various styles

    /**
     * Styles for box borders.
     */
    enum BorderStyle {
        SINGLE, DOUBLE, ROUNDED, BOLD, ASCII, MINIMAL
    }

    /**
     * Styles for dividers.
     */
    enum DividerStyle {
        SINGLE, DOUBLE, DASHED, DOTTED, STAR, WAVE
    }

    /**
     * Types of notifications.
     */
    enum NotificationType {
        INFO, WARNING, ERROR, SUCCESS, DEBUG
    }

    /**
     * Styles for banner text.
     */
    enum BannerStyle {
        ASCII_ART, BLOCK, SLANT, SHADOW, SIMPLE, ANSI
    }
}