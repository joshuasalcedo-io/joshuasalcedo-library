package io.joshuasalcedo.library.prettyconsole.format.text;

import io.joshuasalcedo.library.prettyconsole.api.MessageFormat;
import io.joshuasalcedo.library.prettyconsole.PrettyStyle;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation of MessageFormat that styles text based on message types.
 * <p>
 * This formatter applies appropriate colors and prefixes based on the semantic
 * meaning of different message types. The styling is configurable through a
 * mapping of message types to styles and prefixes.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a formatter for an error message
 * MessageFormat errorFormatter = new BasicMessageFormat(MessageType.ERROR);
 * System.out.println(errorFormatter.format("Something went wrong!"));
 *
 * // Create a formatter without a prefix
 * MessageFormat infoFormatter = new BasicMessageFormat(MessageType.INFO).withPrefix(false);
 * System.out.println(infoFormatter.format("This is some information."));
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public class BasicMessageFormat implements MessageFormat {

    // Static mapping of message types to styles
    private static final Map<MessageType, PrettyStyle.Color> DEFAULT_COLOR_MAP = new HashMap<>();
    private static final Map<MessageType, PrettyStyle.Style> DEFAULT_STYLE_MAP = new HashMap<>();
    private static final Map<MessageType, PrettyStyle.Background> DEFAULT_BACKGROUND_MAP = new HashMap<>();

    // Static mapping of message types to prefixes
    private static final Map<MessageType, String> DEFAULT_PREFIX_MAP = new HashMap<>();

    // Initialize default mappings
    static {
        // Standard log levels
        DEFAULT_COLOR_MAP.put(MessageType.PLAIN, PrettyStyle.Color.WHITE);
        DEFAULT_COLOR_MAP.put(MessageType.ERROR, PrettyStyle.Color.RED);
        DEFAULT_COLOR_MAP.put(MessageType.WARNING, PrettyStyle.Color.YELLOW);
        DEFAULT_COLOR_MAP.put(MessageType.INFO, PrettyStyle.Color.BLUE);
        DEFAULT_COLOR_MAP.put(MessageType.SUCCESS, PrettyStyle.Color.GREEN);
        DEFAULT_COLOR_MAP.put(MessageType.DEBUG, PrettyStyle.Color.PURPLE);

        DEFAULT_STYLE_MAP.put(MessageType.CRITICAL_ERROR, PrettyStyle.Style.BOLD);
        DEFAULT_STYLE_MAP.put(MessageType.IMPORTANT, PrettyStyle.Style.BOLD);
        DEFAULT_STYLE_MAP.put(MessageType.EMPHASIS, PrettyStyle.Style.ITALIC);

        DEFAULT_BACKGROUND_MAP.put(MessageType.CRITICAL_ERROR, PrettyStyle.Background.RED);
        DEFAULT_BACKGROUND_MAP.put(MessageType.SECURITY_ALERT, PrettyStyle.Background.BRIGHT_RED);
        DEFAULT_BACKGROUND_MAP.put(MessageType.HIGHLIGHT, PrettyStyle.Background.YELLOW);

        // Database related
        DEFAULT_COLOR_MAP.put(MessageType.DB_ERROR, PrettyStyle.Color.RED);
        DEFAULT_COLOR_MAP.put(MessageType.DB_SUCCESS, PrettyStyle.Color.GREEN);

        // Network related
        DEFAULT_COLOR_MAP.put(MessageType.NETWORK_ERROR, PrettyStyle.Color.RED);
        DEFAULT_COLOR_MAP.put(MessageType.NETWORK_SUCCESS, PrettyStyle.Color.GREEN);

        // Development related
        DEFAULT_COLOR_MAP.put(MessageType.DEV_TODO, PrettyStyle.Color.YELLOW);
        DEFAULT_COLOR_MAP.put(MessageType.DEV_FIXME, PrettyStyle.Color.RED);

        // Testing related
        DEFAULT_COLOR_MAP.put(MessageType.TEST_PASSED, PrettyStyle.Color.GREEN);
        DEFAULT_COLOR_MAP.put(MessageType.TEST_FAILED, PrettyStyle.Color.RED);

        // Define prefixes
        DEFAULT_PREFIX_MAP.put(MessageType.PLAIN, "");
        DEFAULT_PREFIX_MAP.put(MessageType.ERROR, "[ERROR] ");
        DEFAULT_PREFIX_MAP.put(MessageType.CRITICAL_ERROR, "[CRITICAL] ");
        DEFAULT_PREFIX_MAP.put(MessageType.WARNING, "[WARNING] ");
        DEFAULT_PREFIX_MAP.put(MessageType.INFO, "[INFO] ");
        DEFAULT_PREFIX_MAP.put(MessageType.SUCCESS, "[SUCCESS] ");
        DEFAULT_PREFIX_MAP.put(MessageType.DEBUG, "[DEBUG] ");

        DEFAULT_PREFIX_MAP.put(MessageType.DB_ERROR, "[DATABASE ERROR] ");
        DEFAULT_PREFIX_MAP.put(MessageType.DB_SUCCESS, "[DATABASE] ");

        DEFAULT_PREFIX_MAP.put(MessageType.NETWORK_ERROR, "[NETWORK ERROR] ");
        DEFAULT_PREFIX_MAP.put(MessageType.NETWORK_SUCCESS, "[NETWORK] ");

        DEFAULT_PREFIX_MAP.put(MessageType.SECURITY_ALERT, "[SECURITY ALERT] ");

        DEFAULT_PREFIX_MAP.put(MessageType.DEV_TODO, "[TODO] ");
        DEFAULT_PREFIX_MAP.put(MessageType.DEV_FIXME, "[FIXME] ");

        DEFAULT_PREFIX_MAP.put(MessageType.TEST_PASSED, "✓ ");
        DEFAULT_PREFIX_MAP.put(MessageType.TEST_FAILED, "✗ ");
    }

    private final MessageType messageType;
    private final PrettyStyle.Color color;
    private final PrettyStyle.Style style;
    private final PrettyStyle.Background background;
    private final String prefix;
    private final boolean prefixEnabled;

    /**
     * Creates a new formatter for the specified message type.
     *
     * @param messageType The message type to format
     */
    public BasicMessageFormat(MessageType messageType) {
        this.messageType = messageType;
        this.color = DEFAULT_COLOR_MAP.getOrDefault(messageType, PrettyStyle.Color.WHITE);
        this.style = DEFAULT_STYLE_MAP.getOrDefault(messageType, null);
        this.background = DEFAULT_BACKGROUND_MAP.getOrDefault(messageType, null);
        this.prefix = DEFAULT_PREFIX_MAP.getOrDefault(messageType, "");
        this.prefixEnabled = !prefix.isEmpty();
    }

    /**
     * Creates a new formatter with custom settings.
     *
     * @param messageType The message type to format
     * @param color The color to apply
     * @param style The style to apply
     * @param background The background color to apply
     * @param prefix The prefix to add
     * @param prefixEnabled Whether to enable the prefix
     */
    public BasicMessageFormat(
        MessageType messageType,
        PrettyStyle.Color color,
        PrettyStyle.Style style,
        PrettyStyle.Background background,
        String prefix,
        boolean prefixEnabled
    ) {
        this.messageType = messageType;
        this.color = color;
        this.style = style;
        this.background = background;
        this.prefix = prefix;
        this.prefixEnabled = prefixEnabled;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public boolean hasPrefixEnabled() {
        return prefixEnabled;
    }

    @Override
    public MessageFormat withPrefix(boolean enablePrefix) {
        return new BasicMessageFormat(messageType, color, style, background, prefix, enablePrefix);
    }

    @Override
    public String format(String text) {
        // Handle null text
        if (text == null) {
            text = "";
        }

        // Special case for empty text with no prefix
        if (text.isEmpty() && !prefixEnabled) {
            return "";
        }

        // Special case for PLAIN message type
        if (messageType == MessageType.PLAIN) {
            return text;
        }

        StringBuilder result = new StringBuilder();

        if (prefixEnabled && !prefix.isEmpty()) {
            // Format the prefix with the same style as the message
            PrettyStyle.Formatter prefixFormatter = createFormatter();
            result.append(PrettyStyle.apply(prefixFormatter, prefix));
        }

        // Format the text
        PrettyStyle.Formatter textFormatter = createFormatter();
        result.append(PrettyStyle.apply(textFormatter, text));

        return result.toString();
    }

    /**
     * Creates a formatter with the current style settings.
     *
     * @return A new formatter with the current style settings
     */
    private PrettyStyle.Formatter createFormatter() {
        PrettyStyle.Formatter formatter = new PrettyStyle.Formatter();

        if (color != null) {
            formatter.withColor(color);
        }

        if (style != null) {
            formatter.withStyle(style);
        }

        if (background != null) {
            formatter.withBackground(background);
        }

        return formatter;
    }
}
