package io.joshuasalcedo.pretty.core.utils;

/**
 * RGB Color Utility for ANSI 256-color terminal colors
 * Allows calculation of RGB values for ANSI 256-color palette (0-255)
 * and visualizes them in the terminal
 */
public class RGBColorCalculator {

    // ANSI escape code constants
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_FG_256 = "\u001B[38;5;%dm";
    private static final String ANSI_BG_256 = "\u001B[48;5;%dm";
    
    /**
     * Main method to demonstrate the RGB color calculation for the 256-color ANSI palette
     * and display the colors in the terminal
     */
    public static void main(String[] args) {
        // Display standard ANSI colors (0-15)
        System.out.println("Colors 0-15 correspond to the ANSI and aixterm naming");
        displayBasicColors();
        
        // Display the 6x6x6 color cube (16-231)
        System.out.println("\nColors 16-231 are a 6x6x6 color cube");
        displayColorCube();
        
        // Display the grayscale ramp (232-255)
        System.out.println("\nColors 232-255 are a grayscale ramp, intentionally leaving out black and white");
        displayGrayscale();
    }
    
    /**
     * Displays the basic ANSI colors (0-15) with color swatches and RGB values
     */
    private static void displayBasicColors() {
        System.out.println(" Code  RGB Values  Color Swatch  Color Swatch (Text)");
        System.out.println("------ ----------  ------------  -----------------");
        
        for (int code = 0; code < 16; code++) {
            int[] rgb = calculateBasicColorRGB(code);
            
            // Determine if we need light text on dark background for readability
            boolean isDark = rgb[0] + rgb[1] + rgb[2] < 300;
            int textColor = isDark ? 15 : 0; // white text on dark, black text on light
            
            System.out.printf("%3d    %02X %02X %02X     ", code, rgb[0], rgb[1], rgb[2]);
            
            // Display color swatch as background
            System.out.printf(ANSI_BG_256 + "            " + ANSI_RESET + "  ", code);
            
            // Display text on colored background
            System.out.printf(ANSI_BG_256 + ANSI_FG_256 + " Color %3d " + ANSI_RESET + "%n", 
                             code, textColor, code);
        }
    }
    
    /**
     * Displays the 6x6x6 color cube (16-231) with color swatches
     */
    private static void displayColorCube() {
        // Display each color plane (grouped by red value)
        for (int red = 0; red < 6; red++) {
            System.out.printf("%nRed Level %d (values %d-%d)%n", 
                             red, 16 + (red * 36), 16 + (red * 36) + 35);
            
            // Header for columns (blue values)
            System.out.print("Green\\Blue|");
            for (int blue = 0; blue < 6; blue++) {
                System.out.printf(" %d ", blue);
            }
            System.out.println();
            
            // Print separator
            System.out.print("---------|");
            for (int blue = 0; blue < 6; blue++) {
                System.out.print("---");
            }
            System.out.println();
            
            // Print each row (green values)
            for (int green = 0; green < 6; green++) {
                System.out.printf("   %d     |", green);
                
                // Print each color in the row
                for (int blue = 0; blue < 6; blue++) {
                    int code = 16 + (red * 36) + (green * 6) + blue;
                    System.out.printf(ANSI_BG_256 + " %d " + ANSI_RESET, code, code);
                }
                System.out.println();
            }
        }
        
        // Display all colors in a single grid for easier reference
        System.out.println("\nAll cube colors (16-231) in a compact grid:");
        int count = 0;
        for (int code = 16; code < 232; code++) {
            System.out.printf(ANSI_BG_256 + "%3d" + ANSI_RESET + " ", code, code);
            count++;
            if (count % 18 == 0) System.out.println();
        }
        System.out.println();
    }
    
    /**
     * Displays the grayscale ramp (232-255) with color swatches
     */
    private static void displayGrayscale() {
        System.out.println(" Code  Level  Color Swatch");
        System.out.println("------ -----  ------------");
        
        for (int gray = 0; gray < 24; gray++) {
            int code = 232 + gray;
            int level = gray * 10 + 8;
            
            // Determine text color for readability
            int textColor = gray < 12 ? 15 : 0; // white text on dark, black text on light
            
            System.out.printf("%3d    %3d    ", code, level);
            
            // Display color swatch as background with code number
            System.out.printf(ANSI_BG_256 + ANSI_FG_256 + " %3d (%3d) " + ANSI_RESET + "%n", 
                             code, textColor, code, level);
        }
    }

    /**
     * Calculate RGB values for basic ANSI colors (0-15)
     * @param code ANSI color code (0-15)
     * @return int array with [r, g, b] values
     */
    public static int[] calculateBasicColorRGB(int code) {
        int level;
        if (code > 8) {
            level = 255;
        } else if (code == 7) {
            level = 229;
        } else {
            level = 205;
        }

        int r = (code == 8) ? 127 : ((code & 1) != 0) ? level : (code == 12) ? 92 : 0;
        int g = (code == 8) ? 127 : ((code & 2) != 0) ? level : (code == 12) ? 92 : 0;
        int b = (code == 8) ? 127 : (code == 4) ? 238 : ((code & 4) != 0) ? level : 0;

        return new int[] { r, g, b };
    }

    /**
     * Calculate RGB values for the 6x6x6 color cube (codes 16-231)
     * @param red Red component (0-5)
     * @param green Green component (0-5)
     * @param blue Blue component (0-5)
     * @return int array with [r, g, b] values
     */
    public static int[] calculateCubeColorRGB(int red, int green, int blue) {
        int r = (red != 0) ? red * 40 + 55 : 0;
        int g = (green != 0) ? green * 40 + 55 : 0;
        int b = (blue != 0) ? blue * 40 + 55 : 0;

        return new int[] { r, g, b };
    }

    /**
     * Calculate RGB values for the grayscale ramp (codes 232-255)
     * @param gray Gray level (0-23)
     * @return int array with [r, g, b] values (all components are the same)
     */
    public static int[] calculateGrayscaleRGB(int gray) {
        int level = gray * 10 + 8;
        return new int[] { level, level, level };
    }

    /**
     * Get RGB values for any ANSI 256-color code (0-255)
     * @param code ANSI color code (0-255)
     * @return int array with [r, g, b] values
     */
    public static int[] getANSIColorRGB(int code) {
        if (code < 0 || code > 255) {
            throw new IllegalArgumentException("Color code must be between 0 and 255");
        }

        if (code < 16) {
            // Basic ANSI colors (0-15)
            return calculateBasicColorRGB(code);
        } else if (code < 232) {
            // 6x6x6 color cube (16-231)
            int adjustedCode = code - 16;
            int red = adjustedCode / 36;
            int green = (adjustedCode % 36) / 6;
            int blue = adjustedCode % 6;
            return calculateCubeColorRGB(red, green, blue);
        } else {
            // Grayscale ramp (232-255)
            int gray = code - 232;
            return calculateGrayscaleRGB(gray);
        }
    }

    /**
     * Converts RGB values to ANSI 256-color code
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @return Closest ANSI 256-color code
     */
    public static int getRGBToANSICode(int r, int g, int b) {
        // Check for grayscale first (if r, g, and b are close to each other)
        if (Math.abs(r - g) < 10 && Math.abs(g - b) < 10) {
            // It's probably grayscale
            if (r < 8) return 0;  // black
            if (r > 247) return 15; // white
            
            // Find closest grayscale value
            int gray = Math.round((r - 8) / 10.0f);
            return 232 + Math.min(gray, 23);
        }
        
        // Calculate the closest color in the 6x6x6 cube
        int red = r > 0 ? Math.round((r - 55) / 40.0f) : 0;
        int green = g > 0 ? Math.round((g - 55) / 40.0f) : 0;
        int blue = b > 0 ? Math.round((b - 55) / 40.0f) : 0;
        
        // Clamp values
        red = Math.max(0, Math.min(5, red));
        green = Math.max(0, Math.min(5, green));
        blue = Math.max(0, Math.min(5, blue));
        
        // Calculate color code
        return 16 + (red * 36) + (green * 6) + blue;
    }
}