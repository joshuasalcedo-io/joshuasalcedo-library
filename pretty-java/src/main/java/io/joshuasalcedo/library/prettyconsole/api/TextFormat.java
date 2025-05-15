package io.joshuasalcedo.library.prettyconsole.api;

/**
 * Interface for formatters that transform text input.
 * <p>
 * This interface specializes the {@link Format} interface for string inputs,
 * providing additional methods for text-specific formatting operations.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public interface TextFormat extends Format<String> {
    /**
     * Creates a new formatter that applies this formatter and then the other formatter.
     * <p>
     * This method allows for the composition of formatters, enabling complex
     * formatting operations to be built from simpler ones.
     * </p>
     *
     * @param other The formatter to apply after this one
     * @return A new formatter that applies both formatters in sequence
     */
    default TextFormat andThen(TextFormat other) {
        return text -> other.format(this.format(text));
    }
}
