package io.joshuasalcedo.library.prettyconsole.format.text;

import lombok.Getter;

/**
 * Enumeration of message types for console output formatting.
 * <p>
 * This enumeration provides a comprehensive set of message types for various
 * use cases in console applications, including system status messages, database
 * operations, network communications, security alerts, API interactions, UI elements,
 * documentation, development notes, testing results, performance metrics, validation,
 * and configuration.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
@Getter
public enum MessageType {
    //-------------------------------------------------------------------------
    // System Status Messages
    //-------------------------------------------------------------------------

    /**
     * Plain text without any prefix or special formatting.
     * Only applies minimal styling (color) without adding any prefix.
     */
    PLAIN("Plain text without any prefix or special formatting"),

    /**
     * Indicates a standard error condition that doesn't prevent the application from continuing.
     * Styled with red text.
     */
    ERROR("Standard error condition styled with red text"),

    /**
     * Indicates a severe error condition that might prevent the application from continuing.
     * Styled with white text on a red background for high visibility.
     */
    CRITICAL_ERROR("Severe error condition styled with white text on a red background for high visibility"),

    /**
     * Indicates a potential issue that doesn't prevent the application from functioning
     * but might lead to problems or unexpected behavior.
     * Styled with yellow text.
     */
    WARNING("Potential issue styled with yellow text"),

    /**
     * Provides general information about the system's operation.
     * Styled with blue text.
     */
    INFO("General information styled with blue text"),

    /**
     * Indicates a successful operation or action completion.
     * Styled with green text.
     */
    SUCCESS("Successful operation styled with green text"),

    /**
     * Provides detailed information for debugging purposes.
     * Styled with purple text.
     */
    DEBUG("Detailed debugging information styled with purple text"),

    //-------------------------------------------------------------------------
    // Database Related
    //-------------------------------------------------------------------------

    /**
     * Indicates an error related to database operations.
     * Styled with red text with a "DATABASE ERROR" prefix.
     */
    DB_ERROR("Database error styled with red text and a 'DATABASE ERROR' prefix"),

    /**
     * Indicates a successful database operation.
     * Styled with green text with a "DATABASE" prefix.
     */
    DB_SUCCESS("Successful database operation styled with green text and a 'DATABASE' prefix"),

    //-------------------------------------------------------------------------
    // Network Related
    //-------------------------------------------------------------------------

    /**
     * Indicates an error related to network operations.
     * Styled with red text with a "NETWORK ERROR" prefix.
     */
    NETWORK_ERROR("Network error styled with red text and a 'NETWORK ERROR' prefix"),

    /**
     * Indicates a successful network operation.
     * Styled with green text with a "NETWORK" prefix.
     */
    NETWORK_SUCCESS("Successful network operation styled with green text and a 'NETWORK' prefix"),

    //-------------------------------------------------------------------------
    // Security Related
    //-------------------------------------------------------------------------

    /**
     * Indicates a critical security issue requiring immediate attention.
     * Styled with white text on a bright red background for maximum visibility.
     */
    SECURITY_ALERT("Critical security alert styled with white text on a bright red background"),

    //-------------------------------------------------------------------------
    // Development
    //-------------------------------------------------------------------------

    /**
     * Formats text as a TODO item.
     * Styled with yellow text with a "TODO" prefix.
     */
    DEV_TODO("Development TODO styled with yellow text and a 'TODO' prefix"),

    /**
     * Formats text as a FIXME item.
     * Styled with red text with a "FIXME" prefix.
     */
    DEV_FIXME("Development FIXME styled with red text and a 'FIXME' prefix"),

    //-------------------------------------------------------------------------
    // Testing
    //-------------------------------------------------------------------------

    /**
     * Indicates a passed test.
     * Styled with green text with a "✓" prefix.
     */
    TEST_PASSED("Passed test styled with green text and a '✓' prefix"),

    /**
     * Indicates a failed test.
     * Styled with red text with a "✗" prefix.
     */
    TEST_FAILED("Failed test styled with red text and a '✗' prefix"),

    //-------------------------------------------------------------------------
    // Custom Styles
    //-------------------------------------------------------------------------

    /**
     * Highlights text for emphasis.
     * Styled with bold black text on a yellow background.
     */
    HIGHLIGHT("Highlighted text styled with bold black text on a yellow background"),

    /**
     * Adds emphasis to text.
     * Styled with italic text.
     */
    EMPHASIS("Emphasized text styled with italic formatting"),

    /**
     * Formats text as important.
     * Styled with bold red text.
     */
    IMPORTANT("Important text styled with bold red formatting");

    /**
     * -- GETTER --
     *  Returns the description of this message type.
     *
     * @return A detailed description of the message type and its styling
     */
    private final String description;

    /**
     * Constructor for MessageType enum.
     *
     * @param description A detailed description of the message type and its styling
     */
    MessageType(String description) {
        this.description = description;
    }

}
