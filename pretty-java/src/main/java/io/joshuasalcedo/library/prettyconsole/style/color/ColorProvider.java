package io.joshuasalcedo.library.prettyconsole.style.color;

/**
 * A unified color interface that provides common methods for all color types.
 */
public interface ColorProvider {
    /**
     * Get the ANSI code for this color.
     * 
     * @return The ANSI code as a string
     */
    String getCode();
    
    /**
     * Check if this is a background color.
     * 
     * @return true if this is a background color, false otherwise
     */
    boolean isBackground();
    
    /**
     * Convert this color to a background color.
     * 
     * @return A new color configured as a background color
     */
    ColorProvider asBackground();
    
    /**
     * Convert this color to a foreground color.
     * 
     * @return A new color configured as a foreground color
     */
    ColorProvider asForeground();
}