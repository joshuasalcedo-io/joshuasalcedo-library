package io.joshuasalcedo.pretty.core.demo;

/**
 * Demonstration of a status line that updates in place.
 * This maintains a context/status line at the bottom that updates in place,
 * while allowing new content to be added above it.
 */
public class PrettyPrinterDemo {

    private static boolean hasStatusLine = false;

    /**
     * Updates the status line at the bottom, keeping it in the same place.
     *
     * @param statusText The new text for the status line
     */
    public static void updateStatusLine(String statusText) {
        if (hasStatusLine) {
            // Move up one line, clear it, and write new status
            System.out.print("\033[1A"); // Move up one line
            System.out.print("\033[2K"); // Clear the line
            System.out.println(statusText); // Print new status
        } else {
            // First time - just print it
            System.out.println(statusText);
            hasStatusLine = true;
        }
        System.out.flush(); // Ensure output is visible immediately
    }

    /**
     * Prints content above the status line.
     *
     * @param content The content to print
     */
    public static void printContent(String content) {
        if (hasStatusLine) {
            // Move up one line to position cursor before status line
            System.out.print("\033[1A");

            // Print the content
            System.out.println(content);

            // Reprint the status line (which was effectively pushed down)
            // This is handled by leaving the cursor at the right position
        } else {
            // No status line yet, just print normally
            System.out.println(content);
        }
    }

    /**
     * Main method demonstrating the status line functionality.
     */
    public static void main(String[] args) {
        try {
            System.out.println("Example 1: Basic fixed position printing");

            // Initial status line
            updateStatusLine("Hello count is 1.");
            Thread.sleep(1000);

            // Add content above status line
            printContent("This print was called.");
            Thread.sleep(1000);

            // Update the status line
            updateStatusLine("Hello count is 2.");
            Thread.sleep(1000);

            // Add more content above status line
            printContent("This print was called.");
            Thread.sleep(1000);
            printContent("This is the latest print.");
            Thread.sleep(1000);

            // Update the status line again
            updateStatusLine("Hello count is 3.");
            Thread.sleep(3000);

        } catch (InterruptedException e) {
            System.err.println("Demo interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}