package io.joshuasalcedo.library.prettyconsole.style;

/**
 * Interface for styling components that can be applied to text.
 * <p>
 * This interface defines the contract for styling components that apply visual styling
 * to text, such as colors, backgrounds, and text decorations. It separates styling
 * concerns from layout concerns, allowing for more modular and maintainable code.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public interface StyleComponent {
    /**
     * Gets the ANSI code for this styling component.
     *
     * @return The ANSI code as a string
     */
    String getCode();

    /**
     * Gets the description of this styling component.
     *
     * @return The description as a string
     */
    String getDescription();

    /**
     * Apply this styling component to the given text.
     *
     * @param text The text to style
     * @return The styled text with reset code at the end
     */
    String apply(String text);

    /**
     * Safely apply this styling component to the given text if the terminal supports ANSI codes.
     *
     * @param text The text to style
     * @return The styled text if supported, the original text otherwise
     */
    String safeApply(String text);
}