package io.joshuasalcedo.library.prettyconsole.api;

/**
 * Base interface for all formatters in the Pretty Console library.
 * <p>
 * This interface defines the fundamental contract for any formatter
 * that transforms input of a specific type into a styled string representation.
 * All formatter types in the library implement this interface to allow for
 * flexible composition and consistent API design.
 * </p>
 *
 * @param <T> The type of input to be formatted
 * @author JoshuaSalcedo
 * @since 1.0.0
 */
public interface Format<T> {
    /**
     * Formats the input into a styled string representation.
     *
     * @param input The input to format
     * @return A formatted string representation
     */
    String format(T input);
}
