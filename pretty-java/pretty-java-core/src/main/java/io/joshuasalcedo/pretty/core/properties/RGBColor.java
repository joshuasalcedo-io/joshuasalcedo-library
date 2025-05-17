package io.joshuasalcedo.pretty.core.properties;

import io.joshuasalcedo.pretty.core.utils.TerminalUtils;

/**
 * Represents an RGB color with a fluent API for easy creation and manipulation.
 * Handles color values in the range of 0-255 for each component.
 */
public class RGBColor {
    private final int red;
    private final int green;
    private final int blue;
    private final String name;
    private final boolean isBackground;

    /**
     * Private constructor used by the builder methods
     */
    private RGBColor(int red, int green, int blue, String name, boolean isBackground) {
        this.red = clamp(red, 0, 255);
        this.green = clamp(green, 0, 255);
        this.blue = clamp(blue, 0, 255);
        this.name = name;
        this.isBackground = isBackground;
    }

    /**
     * Creates a new RGB foreground color
     *
     * @param red Red component (0-255)
     * @param green Green component (0-255)
     * @param blue Blue component (0-255)
     * @return A new RGBColor instance
     */
    public static RGBColor of(int red, int green, int blue) {
        return new RGBColor(red, green, blue, null, false);
    }

    /**
     * Creates a new RGB color from a hex string
     *
     * @param hexColor Hex color string (e.g. "#FF5500" or "FF5500")
     * @return A new RGBColor instance
     * @throws IllegalArgumentException if the hex string is invalid
     */
    public static RGBColor fromHex(String hexColor) {
        if (hexColor == null || hexColor.isEmpty()) {
            throw new IllegalArgumentException("Hex color string cannot be null or empty");
        }

        // Remove # if present
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }

        // Validate hex string
        if (hexColor.length() != 6 || !hexColor.matches("[0-9A-Fa-f]{6}")) {
            throw new IllegalArgumentException("Invalid hex color format: " + hexColor);
        }

        // Parse RGB components
        int r = Integer.parseInt(hexColor.substring(0, 2), 16);
        int g = Integer.parseInt(hexColor.substring(2, 4), 16);
        int b = Integer.parseInt(hexColor.substring(4, 6), 16);

        return of(r, g, b);
    }

    /**
     * Creates a predefined color by name
     *
     * @param colorName One of the predefined color names
     * @return A new RGBColor instance for the named color
     * @throws IllegalArgumentException if the color name is not recognized
     */
    public static RGBColor named(String colorName) {
        if (colorName == null || colorName.isEmpty()) {
            throw new IllegalArgumentException("Color name cannot be null or empty");
        }

        // A simple map of common colors - in a real implementation,
        // you might use an enum or static map
        switch (colorName.toLowerCase()) {
            case "red": return of(255, 0, 0).withName("red");
            case "green": return of(0, 255, 0).withName("green");
            case "blue": return of(0, 0, 255).withName("blue");
            case "yellow": return of(255, 255, 0).withName("yellow");
            case "cyan": return of(0, 255, 255).withName("cyan");
            case "magenta": return of(255, 0, 255).withName("magenta");
            case "white": return of(255, 255, 255).withName("white");
            case "black": return of(0, 0, 0).withName("black");
            case "orange": return of(255, 165, 0).withName("orange");
            case "purple": return of(128, 0, 128).withName("purple");
            case "pink": return of(255, 192, 203).withName("pink");
            case "gray": return of(128, 128, 128).withName("gray");
            case "lightgray": return of(211, 211, 211).withName("lightgray");
            case "darkgray": return of(169, 169, 169).withName("darkgray");
            case "brown": return of(165, 42, 42).withName("brown");
            case "navy": return of(0, 0, 128).withName("navy");
            default:
                throw new IllegalArgumentException("Unknown color name: " + colorName);
        }
    }

    /**
     * Creates a new color with the specified name
     *
     * @param name The name for this color
     * @return A new RGBColor with the specified name
     */
    public RGBColor withName(String name) {
        return new RGBColor(red, green, blue, name, isBackground);
    }

    /**
     * Converts this color to a background color
     *
     * @return A new RGBColor configured as a background color
     */
    public RGBColor asBackground() {
        return new RGBColor(red, green, blue, name, true);
    }

    /**
     * Converts this color to a foreground color
     *
     * @return A new RGBColor configured as a foreground color
     */
    public RGBColor asForeground() {
        return new RGBColor(red, green, blue, name, false);
    }

    /**
     * Creates a new color with adjusted brightness
     *
     * @param factor Brightness factor (0.0 to darken, 1.0 to keep the same, >1.0 to brighten)
     * @return A new RGBColor with adjusted brightness
     */
    public RGBColor brightness(double factor) {
        int r = clamp((int)(red * factor), 0, 255);
        int g = clamp((int)(green * factor), 0, 255);
        int b = clamp((int)(blue * factor), 0, 255);
        return new RGBColor(r, g, b, name, isBackground);
    }

    /**
     * Creates a darker version of this color
     *
     * @return A new RGBColor that is 30% darker
     */
    public RGBColor darker() {
        return brightness(0.7);
    }

    /**
     * Creates a brighter version of this color
     *
     * @return A new RGBColor that is 30% brighter
     */
    public RGBColor brighter() {
        return brightness(1.3);
    }

    /**
     * Blends this color with another color
     *
     * @param other The color to blend with
     * @param ratio The blend ratio (0.0 = this color, 1.0 = other color)
     * @return A new RGBColor representing the blended color
     */
    public RGBColor blend(RGBColor other, double ratio) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot blend with null color");
        }

        double inverseRatio = 1.0 - ratio;
        int r = (int)(red * inverseRatio + other.red * ratio);
        int g = (int)(green * inverseRatio + other.green * ratio);
        int b = (int)(blue * inverseRatio + other.blue * ratio);

        return new RGBColor(r, g, b, null, isBackground);
    }

    /**
     * Converts this color to a hex string
     *
     * @return The color as a hex string (e.g., "#FF5500")
     */
    public String toHex() {
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    /**
     * Returns ANSI escape code for this color
     *
     * @return ANSI code for the color
     */
    public String toAnsi() {
        return "\u001B[" + (isBackground ? "48" : "38") + ";2;" +
                red + ";" + green + ";" + blue + "m";
    }

    /**
     * Safely applies this color to the given text, checking if terminal supports ANSI colors.
     *
     * @param text The text to color
     * @return The colored text if terminal supports ANSI, otherwise the original text
     */
    public String apply(String text) {
        return apply(text, TerminalUtils.isAnsiSupported());
    }

    /**
     * Internal method to apply color based on terminal support
     *
     * @param text The text to color
     * @param isSupported Whether ANSI colors are supported
     * @return The colored text if supported, otherwise the original text
     */
    private String apply(String text, boolean isSupported) {
        if (text == null) {
            return "";
        }

        if (!isSupported) {
            return text;
        }

        return toAnsi() + text + "\u001B[0m";
    }

    /**
     * Helper method to clamp a value between min and max
     */
    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    // Getters

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public String getName() {
        return name;
    }

    public boolean isBackground() {
        return isBackground;
    }

    @Override
    public String toString() {
        return (name != null ? name + " " : "") +
                "RGB(" + red + "," + green + "," + blue + ")" +
                (isBackground ? " [background]" : "");
    }
}