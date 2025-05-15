package io.joshuasalcedo.library.prettyconsole.api;


import io.joshuasalcedo.library.prettyconsole.format.text.MessageType;

/**
 * Interface for formatters that apply styling based on message types.
 * <p>
 * Message type formatters apply styling according to specific message types like
 * errors, warnings, info, etc. They typically add prefixes, colors, and other
 * styling based on the semantic meaning of the message.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public interface MessageFormat extends TextFormat {
    /**
     * Gets the message type associated with this formatter.
     *
     * @return The message type this formatter is designed for
     */
    MessageType getMessageType();

    /**
     * Returns whether this formatter adds a prefix to the message.
     *
     * @return true if the formatter adds a prefix, false otherwise
     */
    boolean hasPrefixEnabled();

    /**
     * Creates a new formatter with the specified prefix setting.
     *
     * @param enablePrefix true to enable the prefix, false to disable it
     * @return A new formatter with the updated prefix setting
     */
    MessageFormat withPrefix(boolean enablePrefix);
}
