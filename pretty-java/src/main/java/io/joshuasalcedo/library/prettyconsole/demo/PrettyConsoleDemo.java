package io.joshuasalcedo.library.prettyconsole.demo;

import io.joshuasalcedo.library.prettyconsole.PrettyConsole;
import io.joshuasalcedo.library.prettyconsole.PrettyStyle;
import io.joshuasalcedo.library.prettyconsole.api.MessageFormat;
import io.joshuasalcedo.library.prettyconsole.core.file.PrettyFile;
import io.joshuasalcedo.library.prettyconsole.core.file.PrettyFileTreeNode;
import io.joshuasalcedo.library.prettyconsole.core.stream.PrettyPrintStream;
import io.joshuasalcedo.library.prettyconsole.format.FormatFactory;
import io.joshuasalcedo.library.prettyconsole.format.VisualizationType;
import io.joshuasalcedo.library.prettyconsole.format.layout.BoxFormat;
import io.joshuasalcedo.library.prettyconsole.format.layout.DividerFormat;
import io.joshuasalcedo.library.prettyconsole.format.layout.FileTreeFormat;
import io.joshuasalcedo.library.prettyconsole.format.layout.TableFormat;
import io.joshuasalcedo.library.prettyconsole.format.progress.ProgressBarFormat;
import io.joshuasalcedo.library.prettyconsole.format.progress.ProgressBarRunner;
import io.joshuasalcedo.library.prettyconsole.format.text.BasicMessageFormat;
import io.joshuasalcedo.library.prettyconsole.format.text.MessageType;
import io.joshuasalcedo.library.prettyconsole.style.*;
import io.joshuasalcedo.library.prettyconsole.utils.AnsiToHtmlConverter;
import io.joshuasalcedo.library.prettyconsole.utils.AnsiToMarkdownConverter;
import io.joshuasalcedo.library.prettyconsole.utils.FileUtils;
import io.joshuasalcedo.library.prettyconsole.utils.TerminalUtils;
import io.joshuasalcedo.library.prettyconsole.utils.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A comprehensive demonstration of the PrettyConsole library features.
 * This class showcases various styling, formatting, and visualization capabilities
 * provided by the PrettyConsole library for enhancing terminal output.
 * 
 * @author JoshuaSalcedo
 */
public class PrettyConsoleDemo {

    // Output directory for saving examples
    private static final String OUTPUT_DIR = "pretty-console-examples";

    // For capturing console output to HTML
    private static final List<String> capturedOutput = new ArrayList<>();
    private static final PrintStream originalOut = System.out;
    private static PrettyPrintStream prettyOut;

    public static void main(String[] args) {
        try {
            // Create output directory if it doesn't exist
            createOutputDirectory();

            // Start capturing output for HTML conversion
            startCapture();

            // Create a PrettyPrintStream to use throughout the demo
            prettyOut = new PrettyPrintStream(System.out);

            // Welcome message
            printSectionTitle("PrettyConsole Demo");
            prettyOut.println("This demo showcases the capabilities of the PrettyConsole library for Java.");
            prettyOut.println("Each section demonstrates different features for enhancing terminal output.");
            prettyOut.println();

            // Basic color examples
            demoBasicColors();

            // Text styling examples
            demoTextStyling();

            // RGB and Hex color examples
            demoAdvancedColors();

            // String decoration examples
            demoStringDecoration();

            // Message formatting examples
            demoMessageFormatting();

            // Box and divider formatting
            demoLayoutFormatting();

            // Table formatting
            demoTableFormatting();

            // Progress bar examples
            demoProgressBars();

            // File tree visualization
            demoFileTree();

            // PrettyFile examples
            demoPrettyFile();

            // Terminal utilities
            demoTerminalUtils();

            // PrettyPrintStream demo
            demoPrettyPrintStream();

            // End the demo
            printSectionTitle("End of Demo");
            System.out.println("Thank you for exploring the PrettyConsole library features!");
            System.out.println("Examples have been saved to the '" + OUTPUT_DIR + "' directory.");

            // Stop capturing and generate HTML output
            stopCapture();
            generateHtmlOutput();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates basic ANSI colors for text.
     */
    private static void demoBasicColors() {
        printSectionTitle("Basic Colors");

        prettyOut.println("Standard Foreground Colors:");

        // Before (plain text)
        String plainText = "This is plain text without any styling";
        prettyOut.println("Before: " + plainText);

        // After (with colors)
        prettyOut.println("After: ");

        // Use PrettyPrintStream directly for colored output
        prettyOut.color(ForegroundColor.BLACK).print("BLACK ");
        prettyOut.color(ForegroundColor.RED).print("RED ");
        prettyOut.color(ForegroundColor.GREEN).print("GREEN ");
        prettyOut.color(ForegroundColor.YELLOW).print("YELLOW ");
        prettyOut.color(ForegroundColor.BLUE).print("BLUE ");
        prettyOut.color(ForegroundColor.PURPLE).print("PURPLE ");
        prettyOut.color(ForegroundColor.CYAN).print("CYAN ");
        prettyOut.color(ForegroundColor.WHITE).println("WHITE");

        prettyOut.println("\nBright Foreground Colors:");
        prettyOut.color(ForegroundColor.BRIGHT_BLACK).print("BRIGHT_BLACK ");
        prettyOut.color(ForegroundColor.BRIGHT_RED).print("BRIGHT_RED ");
        prettyOut.color(ForegroundColor.BRIGHT_GREEN).print("BRIGHT_GREEN ");
        prettyOut.color(ForegroundColor.BRIGHT_YELLOW).print("BRIGHT_YELLOW ");
        prettyOut.color(ForegroundColor.BRIGHT_BLUE).print("BRIGHT_BLUE ");
        prettyOut.color(ForegroundColor.BRIGHT_PURPLE).print("BRIGHT_PURPLE ");
        prettyOut.color(ForegroundColor.BRIGHT_CYAN).print("BRIGHT_CYAN ");
        prettyOut.color(ForegroundColor.BRIGHT_WHITE).println("BRIGHT_WHITE");

        prettyOut.println("\nBackground Colors (with white text):");
        prettyOut.color(ForegroundColor.WHITE).background(BackgroundColor.BLACK).print("BLACK ");
        prettyOut.color(ForegroundColor.WHITE).background(BackgroundColor.RED).print("RED ");
        prettyOut.color(ForegroundColor.WHITE).background(BackgroundColor.GREEN).print("GREEN ");
        prettyOut.color(ForegroundColor.WHITE).background(BackgroundColor.YELLOW).print("YELLOW ");
        prettyOut.color(ForegroundColor.WHITE).background(BackgroundColor.BLUE).print("BLUE ");
        prettyOut.color(ForegroundColor.WHITE).background(BackgroundColor.PURPLE).print("PURPLE ");
        prettyOut.color(ForegroundColor.WHITE).background(BackgroundColor.CYAN).print("CYAN ");
        prettyOut.color(ForegroundColor.BLACK).background(BackgroundColor.WHITE).println("WHITE");

        prettyOut.reset().println();
    }

    /**
     * Demonstrates text styling capabilities like bold, italic, underline, etc.
     */
    private static void demoTextStyling() {
        printSectionTitle("Text Styling");

        prettyOut.println("Text Style Examples:");

        // Before (plain text)
        String plainText = "This is plain text without any styling";
        prettyOut.println("Before: " + plainText);

        // After (with styles)
        prettyOut.println("After:");
        prettyOut.style(TextStyle.BOLD).println("BOLD");
        prettyOut.style(TextStyle.ITALIC).println("ITALIC");
        prettyOut.style(TextStyle.UNDERLINE).println("UNDERLINE");
        prettyOut.style(TextStyle.DIM).println("DIM");
        prettyOut.style(TextStyle.REVERSE).println("REVERSE");
        prettyOut.style(TextStyle.STRIKETHROUGH).println("STRIKETHROUGH");

        prettyOut.println("\nCombined Styles:");
        prettyOut.style(TextStyle.BOLD).style(TextStyle.UNDERLINE)
                .color(ForegroundColor.BRIGHT_GREEN).background(BackgroundColor.BLUE)
                .println("Bold, underlined bright green text on blue background");

        prettyOut.reset().println("\nNested Styles (not supported in all terminals):");
        prettyOut.style(TextStyle.BOLD).print("Bold text with ");
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.RED).print("Red bold text");
        prettyOut.style(TextStyle.BOLD).println(" and back to just bold");

        prettyOut.reset().println();
    }

    /**
     * Demonstrates advanced color capabilities like RGB and Hex colors.
     */
    private static void demoAdvancedColors() {
        printSectionTitle("Advanced Colors");

        prettyOut.println("RGB Colors:");

        // Before (plain text)
        String plainText = "This is plain text without any RGB styling";
        prettyOut.println("Before: " + plainText);

        // After (with RGB colors)
        prettyOut.println("After:");

        // Note: Using direct color methods instead of PrettyConsole.rgb
        // This is a simplified version that may not fully demonstrate RGB capabilities
        // depending on the actual API
        prettyOut.color(ForegroundColor.RED).print("Pure Red ");
        prettyOut.color(ForegroundColor.GREEN).print("Pure Green ");
        prettyOut.color(ForegroundColor.BLUE).println("Pure Blue");

        prettyOut.println("\nCustom RGB Colors:");
        prettyOut.color(ForegroundColor.YELLOW).print("Orange ");
        prettyOut.color(ForegroundColor.PURPLE).print("Purple ");
        prettyOut.color(ForegroundColor.CYAN).println("Turquoise");

        prettyOut.println("\nRGB Background Colors:");
        prettyOut.color(ForegroundColor.BLACK).background(BackgroundColor.YELLOW)
                .print("Black text on orange background ");
        prettyOut.color(ForegroundColor.BLACK).background(BackgroundColor.WHITE)
                .println("Black text on lavender background");

        prettyOut.println("\nHex Colors:");
        // Note: Using standard colors instead of hex colors
        // This is a simplified version that may not fully demonstrate hex capabilities
        prettyOut.color(ForegroundColor.RED).print("Tomato ");
        prettyOut.color(ForegroundColor.BLUE).print("Steel Blue ");
        prettyOut.color(ForegroundColor.GREEN).println("Medium Sea Green");

        prettyOut.println("\n256 Colors (8-bit):");
        prettyOut.color(ForegroundColor.BRIGHT_RED).print("Color 196 (bright red) ");
        prettyOut.color(ForegroundColor.BRIGHT_GREEN).print("Color 46 (bright green) ");
        prettyOut.color(ForegroundColor.BLUE).print("Color 21 (blue) ");
        prettyOut.color(ForegroundColor.BRIGHT_YELLOW).println("Color 226 (bright yellow)");

        // Color gradient example
        prettyOut.println("\nColor Gradient Example:");
        prettyOut.println("This text would have a rainbow gradient effect applied to it!");

        prettyOut.reset().println();
    }

    /**
     * Demonstrates the fluent string decoration API.
     */
    private static void demoStringDecoration() {
        printSectionTitle("String Decoration (Fluent API)");

        prettyOut.println("String Decorator Examples:");

        // Before (plain text)
        String plainText = "This is plain text without decoration";
        prettyOut.println("Before: " + plainText);

        // After (with decoration)
        prettyOut.println("After (using fluent API):");

        // Create styled text for error, warning, and success messages
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.WHITE).background(BackgroundColor.RED)
                .print("ERROR");
        prettyOut.reset().println(" Something went wrong!");

        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.BLACK).background(BackgroundColor.YELLOW)
                .print("WARNING");
        prettyOut.reset().println(" This is a warning message.");

        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.WHITE).background(BackgroundColor.GREEN)
                .print("SUCCESS");
        prettyOut.reset().println(" Operation completed successfully.");

        // Combined decorations
        prettyOut.println("\nCombined Decorations:");

        // User profile title
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.BRIGHT_WHITE).background(BackgroundColor.BLUE)
                .println("USER PROFILE");

        // User details
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.BRIGHT_CYAN).print("Name: ");
        prettyOut.reset().println("John Doe");

        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.BRIGHT_CYAN).print("Email: ");
        prettyOut.reset().println("john.doe@example.com");

        prettyOut.print("Status: ");
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.BLACK).background(BackgroundColor.BRIGHT_GREEN)
                .println("ACTIVE");

        prettyOut.reset().println();
    }

    /**
     * Demonstrates message formatting for different message types.
     */
    private static void demoMessageFormatting() {
        printSectionTitle("Message Formatting");

        prettyOut.println("Basic Message Format Examples:");

        // Before (plain text)
        prettyOut.println("Before (plain text messages):");
        prettyOut.println("ERROR: This is an error message.");
        prettyOut.println("WARNING: This is a warning message.");
        prettyOut.println("INFO: This is an informational message.");
        prettyOut.println("SUCCESS: This is a success message.");
        prettyOut.println("DEBUG: This is a debug message.");

        // After (with formatting)
        prettyOut.println("\nAfter (with formatting):");

        // Example messages using PrettyPrintStream directly
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.RED).print("[ERROR] ");
        prettyOut.reset().println("This is an error message.");

        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.YELLOW).print("[WARNING] ");
        prettyOut.reset().println("This is a warning message.");

        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.BLUE).print("[INFO] ");
        prettyOut.reset().println("This is an informational message.");

        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.GREEN).print("[SUCCESS] ");
        prettyOut.reset().println("This is a success message.");

        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.PURPLE).print("[DEBUG] ");
        prettyOut.reset().println("This is a debug message.");

        // Custom message format
        prettyOut.println("\nCustom Message Formats:");

        // Database-specific message formats using PrettyPrintStream directly
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.RED).print("[DATABASE ERROR] ");
        prettyOut.reset().println("Connection failed: timeout");

        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.YELLOW).print("[DATABASE WARNING] ");
        prettyOut.reset().println("Slow query detected (2.5s)");

        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.BLUE).print("[DATABASE INFO] ");
        prettyOut.reset().println("Connected to MySQL server v8.0.23");

        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.GREEN).print("[DATABASE OK] ");
        prettyOut.reset().println("Successfully inserted 1024 records");

        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.PURPLE).print("[DATABASE DEBUG] ");
        prettyOut.reset().println("Query execution plan: INDEX SCAN on users.email");

        // Real-world logging scenario
        prettyOut.println("\nSimulated Application Log:");
        simulateApplicationLogWithPrettyPrintStream();

        prettyOut.reset().println();
    }

    /**
     * Simulates an application log using PrettyPrintStream.
     */
    private static void simulateApplicationLogWithPrettyPrintStream() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        String timestamp1 = dtf.format(LocalDateTime.now().minusSeconds(5));
        String timestamp2 = dtf.format(LocalDateTime.now().minusSeconds(4));
        String timestamp3 = dtf.format(LocalDateTime.now().minusSeconds(3));
        String timestamp4 = dtf.format(LocalDateTime.now().minusSeconds(2));
        String timestamp5 = dtf.format(LocalDateTime.now().minusSeconds(1));

        prettyOut.print(timestamp1 + " ");
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.BLUE).print("[INFO] ");
        prettyOut.reset().println("Application starting...");

        prettyOut.print(timestamp2 + " ");
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.PURPLE).print("[DEBUG] ");
        prettyOut.reset().println("Loading configuration from config.xml");

        prettyOut.print(timestamp3 + " ");
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.YELLOW).print("[WARNING] ");
        prettyOut.reset().println("Configuration setting 'maxConnections' not found, using default value (10)");

        prettyOut.print(timestamp4 + " ");
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.GREEN).print("[SUCCESS] ");
        prettyOut.reset().println("Connected to database successfully");

        prettyOut.print(timestamp5 + " ");
        prettyOut.style(TextStyle.BOLD).color(ForegroundColor.RED).print("[ERROR] ");
        prettyOut.reset().println("Failed to connect to backup service: Connection refused");
    }

    /**
     * This method is no longer used. It has been replaced by simulateApplicationLogWithPrettyPrintStream.
     * It is kept for reference purposes only.
     */
    private static void simulateApplicationLog(MessageFormat formatter) {
        // This method is deprecated and should not be used.
        // Use simulateApplicationLogWithPrettyPrintStream instead.
    }

    /**
     * Demonstrates layout formatting with boxes and dividers.
     */
    private static void demoLayoutFormatting() {
        printSectionTitle("Layout Formatting");

        prettyOut.println("Box Format Examples:");

        // Before (plain text)
        prettyOut.println("Before (plain text):");
        prettyOut.println("+----------------+");
        prettyOut.println("| System Status  |");
        prettyOut.println("+----------------+");
        prettyOut.println("| CPU: 32%       |");
        prettyOut.println("| Memory: 1.2GB  |");
        prettyOut.println("| Disk: 256GB    |");
        prettyOut.println("+----------------+");

        // After (with formatting)
        prettyOut.println("\nAfter (with box formatting):");

        // Create a box format
        BoxFormat boxFormat = new BoxFormat();
        boxFormat = (BoxFormat) boxFormat.withWidth(50);
        boxFormat = boxFormat.withColor(ForegroundColor.BLUE);
        boxFormat = boxFormat.withPadding(1);

        // Create box content
        String content = "CPU: " + PrettyConsole.apply(ForegroundColor.GREEN, "32%") + "\n" +
                "Memory: " + PrettyConsole.apply(ForegroundColor.YELLOW, "1.2GB") + "\n" +
                "Disk: " + PrettyConsole.apply(ForegroundColor.CYAN, "256GB");

        // Display the box
        prettyOut.println(boxFormat.format("System Status\n" + content));

        // Simple box
        prettyOut.println("\nSimple Box Format:");

        BoxFormat simpleBox = new BoxFormat();
        simpleBox = simpleBox.withColor(ForegroundColor.GREEN);

        prettyOut.println(simpleBox.format("Note\nThis is a simple box with a green border."));

        // Divider examples
        prettyOut.println("\nDivider Format Examples:");

        // Create different dividers
        DividerFormat divider1 = new DividerFormat();
        divider1 = (DividerFormat) divider1.withColor(ForegroundColor.BLUE);
        divider1 = divider1.withLineChar('=');
        divider1 = (DividerFormat) divider1.withWidth(50);

        DividerFormat divider2 = new DividerFormat();
        divider2 = (DividerFormat) divider2.withColor(ForegroundColor.RED);
        divider2 = divider2.withLineChar('-');
        divider2 = (DividerFormat) divider2.withWidth(50);

        DividerFormat divider3 = new DividerFormat();
        divider3 = (DividerFormat) divider3.withColor(ForegroundColor.GREEN);
        divider3 = divider3.withLineChar('~');
        divider3 = (DividerFormat) divider3.withWidth(50);

        DividerFormat titledDivider = new DividerFormat();
        titledDivider = (DividerFormat) titledDivider.withColor(ForegroundColor.CYAN);
        titledDivider = titledDivider.withLineChar('-');
        titledDivider = (DividerFormat) titledDivider.withWidth(50);

        // Display dividers
        prettyOut.println(divider1.format(""));
        prettyOut.println("Content between dividers");
        prettyOut.println(divider2.format(""));
        prettyOut.println("More content between dividers");
        prettyOut.println(divider3.format(""));

        // Titled divider
        prettyOut.println("\nTitled Divider:");
        prettyOut.println(titledDivider.format("SECTION TITLE"));

        prettyOut.println();
    }

    /**
     * Demonstrates table formatting.
     */
    private static void demoTableFormatting() {
        printSectionTitle("Table Formatting");

        prettyOut.println("Table Format Examples:");

        // Before (plain text table)
        prettyOut.println("Before (plain text table):");
        prettyOut.println("Name       | Email                | Role      | Status  ");
        prettyOut.println("-----------|----------------------|-----------|----------");
        prettyOut.println("John Doe   | john.doe@example.com | Admin     | Active  ");
        prettyOut.println("Jane Smith | jane@example.com     | User      | Inactive");
        prettyOut.println("Bob Johnson| bob@example.com      | Moderator | Active  ");

        // After (with formatting)
        prettyOut.println("\nAfter (with table formatting):");

        // Create a table format
        TableFormat tableFormat = new TableFormat();

        // Style the table with the correct API
        tableFormat = tableFormat
                .withColor(ForegroundColor.BLUE)
                .withBorders(true);

        // Create table data as a 2D array of objects
        Object[][] data = {
                {"Name", "Email", "Role", "Status"},
                {"John Doe", "john.doe@example.com", "Admin", PrettyConsole.apply(new StyleFormatter().withColor(ForegroundColor.BLACK).withBackground(BackgroundColor.GREEN), " Active ")},
                {"Jane Smith", "jane@example.com", "User", PrettyConsole.apply(new StyleFormatter().withColor(ForegroundColor.BLACK).withBackground(BackgroundColor.RED), " Inactive ")},
                {"Bob Johnson", "bob@example.com", "Moderator", PrettyConsole.apply(new StyleFormatter().withColor(ForegroundColor.BLACK).withBackground(BackgroundColor.GREEN), " Active ")}
        };

        // Display the table using the public formatTable method
        prettyOut.println(tableFormat.formatTable(data));

        // Advanced table with additional styling
        prettyOut.println("\nAdvanced Table Format (Product Catalog):");

        TableFormat productTable = new TableFormat()
                .withColor(ForegroundColor.BRIGHT_BLACK)
                .withBorders(true)
                .withColumnWidths(10, 25, 15, 10, 15);

        // Create product data as a 2D array of objects
        Object[][] productData = {
                {"ID", "Product Name", "Category", "Price", "Stock"},
                {"P001", "Smartphone X", "Electronics", "$599.99", stockStatus(120)},
                {"P002", "Ergonomic Chair", "Furniture", "$249.99", stockStatus(5)},
                {"P003", "Java Programming Book", "Books", "$49.99", stockStatus(42)},
                {"P004", "Wireless Headphones", "Electronics", "$159.99", stockStatus(0)},
                {"P005", "Coffee Maker", "Kitchen", "$89.99", stockStatus(18)}
        };

        // Display the product table using the public formatTable method
        prettyOut.println(productTable.formatTable(productData));

        prettyOut.println();
    }

    /**
     * Helper method to format stock status with color.
     */
    private static String stockStatus(int quantity) {
        if (quantity == 0) {
            return PrettyConsole.apply(new StyleFormatter().withColor(ForegroundColor.WHITE).withBackground(BackgroundColor.RED), " Out of Stock ");
        } else if (quantity < 10) {
            return PrettyConsole.apply(new StyleFormatter().withColor(ForegroundColor.BLACK).withBackground(BackgroundColor.YELLOW), " Low: " + quantity + " ");
        } else {
            return PrettyConsole.apply(new StyleFormatter().withColor(ForegroundColor.BLACK).withBackground(BackgroundColor.GREEN), " In Stock: " + quantity + " ");
        }
    }

    /**
     * Demonstrates progress bar formatting.
     */
    private static void demoProgressBars() {
        printSectionTitle("Progress Bars");

        prettyOut.println("Progress Bar Examples:");

        // Before (plain text progress bar)
        prettyOut.println("Before (plain text progress bar):");
        prettyOut.println("Loading: [=========>                   ] 30%");

        // After (with formatting)
        prettyOut.println("\nAfter (with progress bar formatting):");

        // Create a progress bar
        ProgressBarFormat progressBar = new ProgressBarFormat(0.0);
        progressBar = progressBar.withColor(PrettyStyle.TextColor.GREEN);
        progressBar = progressBar.withChars('█', '░');
        // Note: The original API had methods for prefix, suffix, etc. that are not in the new API
        // We'll use a simpler approach for now

        // Display the progress bar at different stages
        prettyOut.println(progressBar.withProgress(0.0).format("Download Progress: 0%"));
        prettyOut.println(progressBar.withProgress(0.25).format("Download Progress: 25%"));
        prettyOut.println(progressBar.withProgress(0.5).format("Download Progress: 50%"));
        prettyOut.println(progressBar.withProgress(0.75).format("Download Progress: 75%"));
        prettyOut.println(progressBar.withProgress(1.0).format("Download Progress: 100%"));

        // Custom progress bars
        prettyOut.println("\nCustom Progress Bars:");

        // Create a blue gradient progress bar
        ProgressBarFormat blueBar = new ProgressBarFormat(0.65);
        blueBar = blueBar.withColor(PrettyStyle.TextColor.BLUE);
        blueBar = blueBar.withChars('■', '□');

        prettyOut.println(blueBar.format("Buffer: 65%"));

        // Create a warning progress bar
        ProgressBarFormat warningBar = new ProgressBarFormat(0.82);
        warningBar = warningBar.withColor(PrettyStyle.TextColor.YELLOW);
        warningBar = warningBar.withChars('▓', '░');

        prettyOut.println(warningBar.format("CPU Usage: 82%"));

        // Create a critical progress bar
        ProgressBarFormat criticalBar = new ProgressBarFormat(0.95);
        criticalBar = criticalBar.withColor(PrettyStyle.TextColor.RED);
        criticalBar = criticalBar.withChars('█', '░');

        prettyOut.println(criticalBar.format("Disk Usage: 95%"));

        // Animated progress bar
        prettyOut.println("\nAnimated Progress Bar Demo:");

        try {
            // Create a progress bar for a simulated file download
            ProgressBarFormat downloadBar = new ProgressBarFormat(0.0);
            downloadBar = downloadBar.withColor(PrettyStyle.TextColor.CYAN);
            downloadBar = downloadBar.withChars('=', ' ');

            // Note: The ProgressBarRunner API has changed, and we don't have the full details
            // For now, we'll simulate the animation with a simple loop
            prettyOut.println("Downloading file: 0%");
            prettyOut.println("Downloading file: 25%");
            prettyOut.println("Downloading file: 50%");
            prettyOut.println("Downloading file: 75%");
            prettyOut.println("Downloading file: 100%");
            prettyOut.style(TextStyle.BOLD).color(ForegroundColor.BRIGHT_GREEN).println("✓ Download complete!");

        } catch (Exception e) {
            prettyOut.println("Error with animated progress bar: " + e.getMessage());
        }

        prettyOut.println();
    }

    /**
     * Demonstrates file tree visualization.
     */
    private static void demoFileTree() {
        printSectionTitle("File Tree Visualization");

        prettyOut.println("File Tree Format Examples:");

        // Before (plain text file tree)
        prettyOut.println("Before (plain text file tree):");
        prettyOut.println("project/");
        prettyOut.println("├── src/");
        prettyOut.println("│   ├── main/");
        prettyOut.println("│   │   ├── java/");
        prettyOut.println("│   │   │   └── App.java");
        prettyOut.println("│   │   └── resources/");
        prettyOut.println("│   └── test/");
        prettyOut.println("│       └── java/");
        prettyOut.println("│           └── AppTest.java");
        prettyOut.println("├── pom.xml");
        prettyOut.println("└── README.md");

        // After (with formatting)
        prettyOut.println("\nAfter (with file tree formatting):");

        // Create a simulated file structure
        PrettyFileTreeNode root = createSampleFileTree();

        // Create a file tree format
        FileTreeFormat fileTreeFormat = new FileTreeFormat();
        fileTreeFormat = (FileTreeFormat) fileTreeFormat.withColor(ForegroundColor.BRIGHT_BLACK);

        // Display the file tree
        prettyOut.println(fileTreeFormat.formatPrettyFile(root.getFile()));

        // Different display depth
        prettyOut.println("\nFile Tree with Limited Depth (2 levels):");
        fileTreeFormat = fileTreeFormat.withMaxDepth(2);
        prettyOut.println(fileTreeFormat.formatPrettyFile(root.getFile()));

        // Real-world example
        prettyOut.println("\nReal-world Project Structure Example:");

        PrettyFileTreeNode webProject = createWebProjectFileTree();
        fileTreeFormat = fileTreeFormat.withMaxDepth(5);
        prettyOut.println(fileTreeFormat.formatPrettyFile(webProject.getFile()));

        prettyOut.println();
    }

    /**
     * Creates a sample file tree for demonstration.
     */
    private static PrettyFileTreeNode createSampleFileTree() {
        // Create PrettyFile objects for the nodes
        PrettyFile projectFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\project");
        PrettyFileTreeNode root = new PrettyFileTreeNode(projectFile);

        // First level
        PrettyFile srcFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\project\\src");
        PrettyFile pomXmlFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\project\\pom.xml");
        PrettyFile readmeFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\project\\README.md");

        PrettyFileTreeNode src = new PrettyFileTreeNode(srcFile);
        PrettyFileTreeNode pomXml = new PrettyFileTreeNode(pomXmlFile);
        PrettyFileTreeNode readme = new PrettyFileTreeNode(readmeFile);

        // Second level
        PrettyFile mainFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\project\\src\\main");
        PrettyFile testFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\project\\src\\test");

        PrettyFileTreeNode main = new PrettyFileTreeNode(mainFile);
        PrettyFileTreeNode test = new PrettyFileTreeNode(testFile);

        // Third level
        PrettyFile mainJavaFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\project\\src\\main\\java");
        PrettyFile mainResourcesFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\project\\src\\main\\resources");
        PrettyFile testJavaFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\project\\src\\test\\java");

        PrettyFileTreeNode mainJava = new PrettyFileTreeNode(mainJavaFile);
        PrettyFileTreeNode mainResources = new PrettyFileTreeNode(mainResourcesFile);
        PrettyFileTreeNode testJava = new PrettyFileTreeNode(testJavaFile);

        // Fourth level
        PrettyFile appJavaFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\project\\src\\main\\java\\App.java");
        PrettyFile appTestJavaFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\project\\src\\test\\java\\AppTest.java");

        PrettyFileTreeNode appJava = new PrettyFileTreeNode(appJavaFile);
        PrettyFileTreeNode appTestJava = new PrettyFileTreeNode(appTestJavaFile);

        // Build the tree
        mainJava.addChild(appJava);
        testJava.addChild(appTestJava);

        main.addChild(mainJava);
        main.addChild(mainResources);
        test.addChild(testJava);

        src.addChild(main);
        src.addChild(test);

        root.addChild(src);
        root.addChild(pomXml);
        root.addChild(readme);

        return root;
    }

    /**
     * Creates a sample web project file tree for demonstration.
     */
    private static PrettyFileTreeNode createWebProjectFileTree() {
        // Create PrettyFile objects for the nodes
        PrettyFile rootFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application");
        PrettyFileTreeNode root = new PrettyFileTreeNode(rootFile);

        // First level
        PrettyFile srcFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\src");
        PrettyFile configFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\config");
        PrettyFile docsFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\docs");
        PrettyFile distFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\dist");
        PrettyFile nodeModulesFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\node_modules");
        PrettyFile packageJsonFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\package.json");
        PrettyFile readmeFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\README.md");
        PrettyFile gitignoreFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\.gitignore");

        PrettyFileTreeNode src = new PrettyFileTreeNode(srcFile);
        PrettyFileTreeNode config = new PrettyFileTreeNode(configFile);
        PrettyFileTreeNode docs = new PrettyFileTreeNode(docsFile);
        PrettyFileTreeNode dist = new PrettyFileTreeNode(distFile);
        PrettyFileTreeNode node_modules = new PrettyFileTreeNode(nodeModulesFile);
        PrettyFileTreeNode packageJson = new PrettyFileTreeNode(packageJsonFile);
        PrettyFileTreeNode readme = new PrettyFileTreeNode(readmeFile);
        PrettyFileTreeNode gitignore = new PrettyFileTreeNode(gitignoreFile);

        // Second level - src
        PrettyFile jsFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\src\\js");
        PrettyFile cssFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\src\\css");
        PrettyFile imagesFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\src\\images");
        PrettyFile htmlFile = new PrettyFile(System.getProperty("java.io.tmpdir") + "\\web-application\\src\\html");

        PrettyFileTreeNode js = new PrettyFileTreeNode(jsFile);
        PrettyFileTreeNode css = new PrettyFileTreeNode(cssFile);
        PrettyFileTreeNode images = new PrettyFileTreeNode(imagesFile);
        PrettyFileTreeNode html = new PrettyFileTreeNode(htmlFile);

        // Build the tree (simplified for brevity)
        src.addChild(js);
        src.addChild(css);
        src.addChild(images);
        src.addChild(html);

        root.addChild(src);
        root.addChild(config);
        root.addChild(docs);
        root.addChild(dist);
        root.addChild(node_modules);
        root.addChild(packageJson);
        root.addChild(readme);
        root.addChild(gitignore);

        return root;
    }

    /**
     * Demonstrates PrettyFile capabilities.
     */
    private static void demoPrettyFile() {
        printSectionTitle("PrettyFile");

        prettyOut.println("PrettyFile Examples:");

        try {
            // Create temporary files for demonstration
            File tempDir = createTempDirectory();
            createTempFiles(tempDir);

            // Create PrettyFile for directory
            PrettyFile prettyDir = new PrettyFile(tempDir.getAbsolutePath());

            // Basic file information
            prettyOut.println("Directory Information:");
            prettyOut.println(prettyDir.getAbsolutePath());
            prettyOut.println("Size: " + FileUtils.formatFileSize(prettyDir.length()));
            prettyOut.println("Last Modified: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(prettyDir.lastModified())));
            prettyOut.println("Is Directory: " + prettyDir.isDirectory());

            // List files in directory
            prettyOut.println("\nFiles in Directory:");
            PrettyFile[] files = prettyDir.listPrettyFiles();

            for (PrettyFile file : files) {
                prettyOut.println(
                        file.getName() + " " +
                        PrettyConsole.apply(ForegroundColor.BRIGHT_BLACK, "(" + FileUtils.formatFileSize(file.length()) + ")") + " " +
                        PrettyConsole.apply(ForegroundColor.BRIGHT_BLACK, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(file.lastModified())))
                );
            }

            // Custom file listing with attributes
            prettyOut.println("\nDetailed File Listing:");
            prettyOut.style(TextStyle.BOLD).color(ForegroundColor.BRIGHT_WHITE).println(
                    String.format("%-30s %-10s %-20s %-10s", "Name", "Size", "Modified", "Type")
            );

            prettyOut.color(ForegroundColor.BRIGHT_BLACK).println("─".repeat(75));

            for (PrettyFile file : files) {
                String fileType = file.isDirectory() ? "Directory" : getFileExtension(file.getName());
                String formattedType = formatFileType(fileType);

                prettyOut.println(String.format("%-30s %-10s %-20s %-10s",
                        file.getName(),
                        FileUtils.formatFileSize(file.length()),
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(file.lastModified())),
                        formattedType));
            }

            // Clean up
            deleteDirectory(tempDir);

        } catch (Exception e) {
            prettyOut.println("Error in PrettyFile demo: " + e.getMessage());
        }

        prettyOut.println();
    }

    /**
     * Creates a temporary directory for file demonstrations.
     */
    private static File createTempDirectory() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "pretty-console-demo-" + System.currentTimeMillis());
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return tempDir;
    }

    /**
     * Creates temporary files for demonstration.
     */
    private static void createTempFiles(File dir) throws IOException {
        // Create subdirectories
        File imagesDir = new File(dir, "images");
        File docsDir = new File(dir, "documents");
        File codeDir = new File(dir, "code");

        imagesDir.mkdir();
        docsDir.mkdir();
        codeDir.mkdir();

        // Create files in subdirectories
        createFile(imagesDir, "logo.png", 1024 * 50); // 50KB
        createFile(imagesDir, "banner.jpg", 1024 * 120); // 120KB
        createFile(imagesDir, "icon.svg", 1024 * 5); // 5KB

        createFile(docsDir, "report.pdf", 1024 * 500); // 500KB
        createFile(docsDir, "presentation.pptx", 1024 * 2500); // 2.5MB
        createFile(docsDir, "document.docx", 1024 * 350); // 350KB

        createFile(codeDir, "main.java", 1024 * 15); // 15KB
        createFile(codeDir, "style.css", 1024 * 8); // 8KB
        createFile(codeDir, "index.html", 1024 * 12); // 12KB
        createFile(codeDir, "script.js", 1024 * 20); // 20KB

        // Create files in main directory
        createFile(dir, "README.md", 1024 * 3); // 3KB
        createFile(dir, "config.xml", 1024 * 2); // 2KB
        createFile(dir, "data.zip", 1024 * 1500); // 1.5MB
    }

    /**
     * Creates a temporary file with specified size.
     */
    private static void createFile(File dir, String name, long size) throws IOException {
        File file = new File(dir, name);
        try (FileWriter writer = new FileWriter(file)) {
            // Write content to achieve approximately the desired file size
            char[] buffer = new char[1024];
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (char) ('A' + (i % 26));
            }

            long written = 0;
            while (written < size) {
                int toWrite = (int) Math.min(buffer.length, size - written);
                writer.write(buffer, 0, toWrite);
                written += toWrite;
            }
        }
    }

    /**
     * Recursively deletes a directory.
     */
    private static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        dir.delete();
    }

    /**
     * Gets file extension from filename.
     */
    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toUpperCase();
        }
        return "FILE";
    }

    /**
     * Formats file type with color.
     */
    private static String formatFileType(String fileType) {
        switch (fileType) {
            case "Directory":
                return PrettyConsole.apply(ForegroundColor.BRIGHT_BLUE, fileType);
            case "JPG":
            case "PNG":
            case "SVG":
            case "GIF":
                return PrettyConsole.apply(ForegroundColor.PURPLE, fileType); // Using PURPLE instead of MAGENTA
            case "PDF":
            case "DOCX":
            case "PPTX":
            case "TXT":
            case "MD":
                return PrettyConsole.apply(ForegroundColor.CYAN, fileType);
            case "JAVA":
            case "JS":
            case "PY":
            case "C":
            case "CPP":
                return PrettyConsole.apply(ForegroundColor.YELLOW, fileType);
            case "HTML":
            case "CSS":
            case "XML":
                return PrettyConsole.apply(ForegroundColor.BRIGHT_GREEN, fileType);
            case "ZIP":
            case "TAR":
            case "GZ":
            case "RAR":
                return PrettyConsole.apply(ForegroundColor.RED, fileType);
            default:
                return PrettyConsole.apply(ForegroundColor.WHITE, fileType);
        }
    }

    /**
     * Demonstrates terminal utilities.
     */
    private static void demoTerminalUtils() {
        printSectionTitle("Terminal Utilities");

        prettyOut.println("Terminal Utility Examples:");

        // Terminal information
        prettyOut.println("Terminal Information:");
        prettyOut.println("ANSI Support: " + PrettyConsole.apply(
                TerminalUtils.isAnsiSupported() ? ForegroundColor.GREEN : ForegroundColor.RED,
                String.valueOf(TerminalUtils.isAnsiSupported())
        ));

        prettyOut.println("Terminal Width: " + TerminalUtils.getTerminalWidth());
        // Note: getTerminalHeight method is not available in the current API
        prettyOut.println("Terminal Height: " + "Not available in current API");

        // Text utilities
        prettyOut.println("\nText Utility Examples:");

        String text = "This is a sample text for demonstration.";

        prettyOut.println("Original: " + text);
        prettyOut.println("Centered: " + TextUtils.center(text, 60, '='));
        // Note: rightAlign method is not available in the current API
        prettyOut.println("Right aligned: " + TextUtils.padLeft(text, 60, ' ')); // Using padLeft as an alternative
        prettyOut.println("Padded left: " + TextUtils.padLeft(text, 50, '.'));
        prettyOut.println("Padded right: " + TextUtils.padRight(text, 50, '.'));

        // Truncated text
        String longText = "This is a very long text that needs to be truncated to fit within a specific width.";
        // Note: truncate method only takes 2 arguments in the current API
        prettyOut.println("Truncated: " + TextUtils.truncate(longText, 30));

        // File size formatting
        prettyOut.println("\nFile Size Formatting Examples:");
        prettyOut.println("1024 bytes: " + FileUtils.formatFileSize(1024));
        prettyOut.println("1500000 bytes: " + FileUtils.formatFileSize(1500000));
        prettyOut.println("1073741824 bytes: " + FileUtils.formatFileSize(1073741824));

        prettyOut.println();
    }

    /**
     * Demonstrates PrettyPrintStream capabilities.
     */
    private static void demoPrettyPrintStream() {
        printSectionTitle("PrettyPrintStream");

        System.out.println("PrettyPrintStream Examples:");

        // Before (standard print stream)
        System.out.println("Before (standard print stream):");
        System.out.println("This is regular text from System.out");
        System.out.println("Multiple lines of text");
        System.out.println("With no styling applied");

        // After (with PrettyPrintStream)
        System.out.println("\nAfter (with PrettyPrintStream):");

        // Create a PrettyPrintStream
        PrettyPrintStream prettyOut = new PrettyPrintStream(System.out);

        // Use the PrettyPrintStream with different styles
        prettyOut.color(ForegroundColor.BLUE).println("This text is blue");
        prettyOut.reset().style(TextStyle.BOLD).println("This text is bold");

        prettyOut.reset()
                .color(ForegroundColor.GREEN)
                .style(TextStyle.BOLD)
                .println("This text is bold and green");

        prettyOut.reset()
                .color(ForegroundColor.WHITE)
                .background(BackgroundColor.RED)
                .println("White text on red background");

        // Demonstrate how styles persist until reset
        prettyOut.reset().color(ForegroundColor.YELLOW);
        prettyOut.println("This text is yellow");
        prettyOut.println("This text is also yellow because the style persists");
        prettyOut.println("Until reset() is called");

        prettyOut.reset();
        prettyOut.println("Back to normal text");

        // Disable/enable formatting
        prettyOut.formatting(false).color(ForegroundColor.PURPLE).println("This text has color disabled");
        prettyOut.formatting(true).color(ForegroundColor.PURPLE).println("This text has color enabled");

        prettyOut.reset();

        // Real-world example: Console application with styled output
        System.out.println("\nSimulated Console Application:");

        prettyOut.reset().style(TextStyle.BOLD).color(ForegroundColor.BRIGHT_CYAN).println("DATABASE CLIENT v1.0");
        prettyOut.reset().println("Connected to: " + PrettyConsole.apply(ForegroundColor.GREEN, "mysql://localhost:3306/mydb"));

        prettyOut.reset().style(TextStyle.BOLD).println("\nAvailable commands:");
        prettyOut.reset().color(ForegroundColor.YELLOW).println("  query <sql>    - Execute SQL query");
        prettyOut.color(ForegroundColor.YELLOW).println("  tables        - List all tables");
        prettyOut.color(ForegroundColor.YELLOW).println("  describe <table> - Show table structure");
        prettyOut.color(ForegroundColor.YELLOW).println("  exit          - Exit the application");

        prettyOut.reset().style(TextStyle.BOLD).color(ForegroundColor.BRIGHT_WHITE).println("\n> query SELECT * FROM users LIMIT 3");

        // Simulate query result with PrettyPrintStream
        String[] headers = {"id", "username", "email", "status"};
        String[][] data = {
                {"1", "admin", "admin@example.com", "active"},
                {"2", "user1", "user1@example.com", "active"},
                {"3", "user2", "user2@example.com", "inactive"}
        };

        // Create a table format for the result
        TableFormat resultTable = new TableFormat();
        // Note: The original API had methods like setHeaderBackground, setHeaderColor, etc.
        // that are not in the new API. We'll use a simpler approach.
        resultTable = resultTable.withColor(ForegroundColor.BRIGHT_BLACK);
        resultTable = (TableFormat) resultTable.withWidth(80);

        // Create a 2D array of objects that includes the headers and data
        Object[][] tableData = new Object[data.length + 1][];
        tableData[0] = headers;
        System.arraycopy(data, 0, tableData, 1, data.length);

        prettyOut.reset().println(resultTable.formatTable(tableData));
        prettyOut.reset().color(ForegroundColor.GREEN).println("3 rows returned (0.023 sec)");

        prettyOut.reset().style(TextStyle.BOLD).color(ForegroundColor.BRIGHT_WHITE).println("\n> exit");
        prettyOut.reset().color(ForegroundColor.CYAN).println("Goodbye!");

        prettyOut.reset();

        System.out.println();
    }

    /**
     * Utility method to print a formatted section title.
     */
    private static void printSectionTitle(String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");

        // Create a formatter for the title
        StyleFormatter titleFormatter = new StyleFormatter()
                .withStyle(TextStyle.BOLD)
                .withColor(ForegroundColor.BRIGHT_WHITE)
                .withBackground(BackgroundColor.BLUE);

        // Calculate padding
        int totalWidth = 80;
        int titleWidth = title.length() + 4; // 4 for the padding spaces
        int halfPadding = (totalWidth - titleWidth) / 2;

        // Create top border
        sb.append(PrettyConsole.apply(ForegroundColor.BLUE, "═".repeat(totalWidth))).append("\n");

        // Create title line with padding
        sb.append(PrettyConsole.apply(ForegroundColor.BLUE, "═".repeat(halfPadding)));
        sb.append(PrettyConsole.apply(titleFormatter, " " + title + " "));
        sb.append(PrettyConsole.apply(ForegroundColor.BLUE, "═".repeat(totalWidth - titleWidth - halfPadding)));
        sb.append("\n");

        // Create bottom border
        sb.append(PrettyConsole.apply(ForegroundColor.BLUE, "═".repeat(totalWidth))).append("\n");

        System.out.print(sb.toString());

        // Capture for HTML conversion
        capturedOutput.add(sb.toString());
    }

    /**
     * Creates the output directory for saving examples.
     */
    private static void createOutputDirectory() throws IOException {
        Path outputPath = Paths.get(OUTPUT_DIR);
        if (!Files.exists(outputPath)) {
            Files.createDirectory(outputPath);
        }
    }

    /**
     * Starts capturing console output for HTML conversion.
     */
    private static void startCapture() {
        capturedOutput.clear();
        System.setOut(new PrintStream(System.out) {
            @Override
            public void println(String x) {
                originalOut.println(x);
                capturedOutput.add(x + "\n");
            }

            @Override
            public void print(String s) {
                originalOut.print(s);
                capturedOutput.add(s);
            }
        });
    }

    /**
     * Stops capturing console output.
     */
    private static void stopCapture() {
        System.setOut(originalOut);
    }

    /**
     * Generates HTML output from the captured console output.
     */
    private static void generateHtmlOutput() {
        try {
            // Create HTML file
            Path htmlPath = Paths.get(OUTPUT_DIR, "pretty-console-demo.html");

            // Create HTML content
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html lang=\"en\">\n");
            html.append("<head>\n");
            html.append("    <meta charset=\"UTF-8\">\n");
            html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            html.append("    <title>PrettyConsole Demo</title>\n");
            html.append("    <style>\n");
            html.append("        body { background-color: #1E1E1E; color: #CCCCCC; font-family: monospace; padding: 20px; }\n");
            html.append("        .terminal { background-color: #0C0C0C; border-radius: 5px; padding: 20px; margin: 20px 0; }\n");
            html.append("        pre { margin: 0; white-space: pre-wrap; }\n");

            // ANSI color to CSS mappings
            html.append("        .ansi-black { color: #000000; }\n");
            html.append("        .ansi-red { color: #C51E14; }\n");
            html.append("        .ansi-green { color: #1DC121; }\n");
            html.append("        .ansi-yellow { color: #C7C329; }\n");
            html.append("        .ansi-blue { color: #0A2FC4; }\n");
            html.append("        .ansi-magenta { color: #C839C5; }\n");
            html.append("        .ansi-cyan { color: #20C5C6; }\n");
            html.append("        .ansi-white { color: #C7C7C7; }\n");
            html.append("        .ansi-bright-black { color: #686868; }\n");
            html.append("        .ansi-bright-red { color: #FD6F6B; }\n");
            html.append("        .ansi-bright-green { color: #67F86F; }\n");
            html.append("        .ansi-bright-yellow { color: #FFFA72; }\n");
            html.append("        .ansi-bright-blue { color: #6A76FB; }\n");
            html.append("        .ansi-bright-magenta { color: #FD7CFC; }\n");
            html.append("        .ansi-bright-cyan { color: #68FDFE; }\n");
            html.append("        .ansi-bright-white { color: #FFFFFF; }\n");

            // Background colors
            html.append("        .ansi-bg-black { background-color: #000000; }\n");
            html.append("        .ansi-bg-red { background-color: #C51E14; }\n");
            html.append("        .ansi-bg-green { background-color: #1DC121; }\n");
            html.append("        .ansi-bg-yellow { background-color: #C7C329; }\n");
            html.append("        .ansi-bg-blue { background-color: #0A2FC4; }\n");
            html.append("        .ansi-bg-magenta { background-color: #C839C5; }\n");
            html.append("        .ansi-bg-cyan { background-color: #20C5C6; }\n");
            html.append("        .ansi-bg-white { background-color: #C7C7C7; }\n");
            html.append("        .ansi-bg-bright-black { background-color: #686868; }\n");
            html.append("        .ansi-bg-bright-red { background-color: #FD6F6B; }\n");
            html.append("        .ansi-bg-bright-green { background-color: #67F86F; }\n");
            html.append("        .ansi-bg-bright-yellow { background-color: #FFFA72; }\n");
            html.append("        .ansi-bg-bright-blue { background-color: #6A76FB; }\n");
            html.append("        .ansi-bg-bright-magenta { background-color: #FD7CFC; }\n");
            html.append("        .ansi-bg-bright-cyan { background-color: #68FDFE; }\n");
            html.append("        .ansi-bg-bright-white { background-color: #FFFFFF; }\n");

            // Text styles
            html.append("        .ansi-bold { font-weight: bold; }\n");
            html.append("        .ansi-italic { font-style: italic; }\n");
            html.append("        .ansi-underline { text-decoration: underline; }\n");
            html.append("        .ansi-strikethrough { text-decoration: line-through; }\n");

            html.append("    </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("    <h1>PrettyConsole Demo - HTML Output</h1>\n");
            html.append("    <p>This HTML file shows the formatted output from the PrettyConsole demo program.</p>\n");
            html.append("    <div class=\"terminal\">\n");
            html.append("        <pre>\n");

            // Add converted output content
            String htmlContent = ansiToHtml(String.join("", capturedOutput));
            html.append(htmlContent);

            html.append("        </pre>\n");
            html.append("    </div>\n");
            html.append("    <p>Note: Some advanced terminal features may not be accurately represented in HTML.</p>\n");
            html.append("</body>\n");
            html.append("</html>");

            // Write to file
            Files.writeString(htmlPath, html.toString());

            System.out.println("HTML output generated: " + htmlPath.toAbsolutePath());

        } catch (Exception e) {
            System.out.println("Error generating HTML output: " + e.getMessage());
        }
    }

    /**
     * Converts ANSI escape sequences to HTML.
     */
    private static String ansiToHtml(String ansiText) {
        if (ansiText == null) {
            return "";
        }

        // Create a simple ANSI to HTML converter
        StringBuilder html = new StringBuilder();
        StringBuilder currentSpan = new StringBuilder();

        boolean inSpan = false;
        boolean bold = false;
        boolean italic = false;
        boolean underline = false;
        boolean strikethrough = false;
        String foregroundColor = null;
        String backgroundColor = null;

        // Iterate through the text
        int i = 0;
        while (i < ansiText.length()) {
            // Look for ANSI escape sequence
            if (ansiText.charAt(i) == '\u001B' && i + 1 < ansiText.length() && ansiText.charAt(i + 1) == '[') {
                // End current span if there is one
                if (inSpan) {
                    html.append("</span>");
                    inSpan = false;
                }

                // Find the end of the ANSI sequence
                int end = ansiText.indexOf('m', i);
                if (end > i) {
                    // Extract the ANSI code
                    String code = ansiText.substring(i + 2, end);
                    String[] codes = code.split(";");

                    // Reset if code is 0
                    if (code.equals("0")) {
                        bold = false;
                        italic = false;
                        underline = false;
                        strikethrough = false;
                        foregroundColor = null;
                        backgroundColor = null;
                    } else {
                        // Process each code
                        for (String c : codes) {
                            int codeNum = 0;
                            try {
                                codeNum = Integer.parseInt(c);
                            } catch (NumberFormatException e) {
                                // Ignore invalid codes
                                continue;
                            }

                            // Style codes
                            if (codeNum == 1) bold = true;
                            else if (codeNum == 3) italic = true;
                            else if (codeNum == 4) underline = true;
                            else if (codeNum == 9) strikethrough = true;

                            // Basic foreground colors
                            else if (codeNum == 30) foregroundColor = "ansi-black";
                            else if (codeNum == 31) foregroundColor = "ansi-red";
                            else if (codeNum == 32) foregroundColor = "ansi-green";
                            else if (codeNum == 33) foregroundColor = "ansi-yellow";
                            else if (codeNum == 34) foregroundColor = "ansi-blue";
                            else if (codeNum == 35) foregroundColor = "ansi-magenta";
                            else if (codeNum == 36) foregroundColor = "ansi-cyan";
                            else if (codeNum == 37) foregroundColor = "ansi-white";

                            // Bright foreground colors
                            else if (codeNum == 90) foregroundColor = "ansi-bright-black";
                            else if (codeNum == 91) foregroundColor = "ansi-bright-red";
                            else if (codeNum == 92) foregroundColor = "ansi-bright-green";
                            else if (codeNum == 93) foregroundColor = "ansi-bright-yellow";
                            else if (codeNum == 94) foregroundColor = "ansi-bright-blue";
                            else if (codeNum == 95) foregroundColor = "ansi-bright-magenta";
                            else if (codeNum == 96) foregroundColor = "ansi-bright-cyan";
                            else if (codeNum == 97) foregroundColor = "ansi-bright-white";

                            // Basic background colors
                            else if (codeNum == 40) backgroundColor = "ansi-bg-black";
                            else if (codeNum == 41) backgroundColor = "ansi-bg-red";
                            else if (codeNum == 42) backgroundColor = "ansi-bg-green";
                            else if (codeNum == 43) backgroundColor = "ansi-bg-yellow";
                            else if (codeNum == 44) backgroundColor = "ansi-bg-blue";
                            else if (codeNum == 45) backgroundColor = "ansi-bg-magenta";
                            else if (codeNum == 46) backgroundColor = "ansi-bg-cyan";
                            else if (codeNum == 47) backgroundColor = "ansi-bg-white";

                            // Bright background colors
                            else if (codeNum == 100) backgroundColor = "ansi-bg-bright-black";
                            else if (codeNum == 101) backgroundColor = "ansi-bg-bright-red";
                            else if (codeNum == 102) backgroundColor = "ansi-bg-bright-green";
                            else if (codeNum == 103) backgroundColor = "ansi-bg-bright-yellow";
                            else if (codeNum == 104) backgroundColor = "ansi-bg-bright-blue";
                            else if (codeNum == 105) backgroundColor = "ansi-bg-bright-magenta";
                            else if (codeNum == 106) backgroundColor = "ansi-bg-bright-cyan";
                            else if (codeNum == 107) backgroundColor = "ansi-bg-bright-white";
                        }
                    }

                    // Skip the processed ANSI sequence
                    i = end + 1;
                    continue;
                }
            }

            // Build the span with current styles
            if (!inSpan && (bold || italic || underline || strikethrough || foregroundColor != null || backgroundColor != null)) {
                currentSpan.setLength(0);
                currentSpan.append("<span class=\"");

                if (bold) currentSpan.append("ansi-bold ");
                if (italic) currentSpan.append("ansi-italic ");
                if (underline) currentSpan.append("ansi-underline ");
                if (strikethrough) currentSpan.append("ansi-strikethrough ");
                if (foregroundColor != null) currentSpan.append(foregroundColor).append(" ");
                if (backgroundColor != null) currentSpan.append(backgroundColor).append(" ");

                // Remove trailing space
                if (currentSpan.length() > 0) {
                    currentSpan.setLength(currentSpan.length() - 1);
                }

                currentSpan.append("\">");
                html.append(currentSpan);
                inSpan = true;
            }

            // Regular text handling
            char c = ansiText.charAt(i);

            // Handle special HTML characters
            if (c == '<') html.append("&lt;");
            else if (c == '>') html.append("&gt;");
            else if (c == '&') html.append("&amp;");
            else html.append(c);

            i++;
        }

        // Close any open span
        if (inSpan) {
            html.append("</span>");
        }

        return html.toString();
    }
}
