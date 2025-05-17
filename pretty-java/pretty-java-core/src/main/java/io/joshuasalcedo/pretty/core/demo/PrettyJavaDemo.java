package io.joshuasalcedo.pretty.core.demo;

import io.joshuasalcedo.pretty.core.model.*;
import io.joshuasalcedo.pretty.core.model.progress.CounterProgressRunner;
import io.joshuasalcedo.pretty.core.model.progress.DotsAnimationRunner;
import io.joshuasalcedo.pretty.core.model.progress.TextAnimationRunner;
import io.joshuasalcedo.pretty.core.model.stream.PrettyPrintStream;
import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.theme.TerminalStyle;

import java.util.Random;
import java.util.Scanner;

/**
 * Interactive demo for Pretty Java Core library.
 * <p>
 * This demo showcases the various features of the Pretty Java Core library,
 * including progress bars, spinners, task lists, and styled text output.
 * </p>
 * <p>
 * Run this demo to see the library in action and explore its capabilities.
 * </p>
 */
public class PrettyJavaDemo {

    private static final PrettyPrintStream out = new PrettyPrintStream(System.out);
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static void main(String[] args) {
        printHeader("Pretty Java Core Demo");
        
        boolean exit = false;
        while (!exit) {
            showMenu();
            int choice = getUserChoice();
            
            try {
                switch (choice) {
                    case 1:
                        demoProgressBar();
                        break;
                    case 2:
                        demoSpinner();
                        break;
                    case 3:
                        demoTaskList();
                        break;
                    case 4:
                        demoCounter();
                        break;
                    case 5:
                        demoTextAnimation();
                        break;
                    case 6:
                        demoStyledText();
                        break;
                    case 7:
                        demoAllFeatures();
                        break;
                    case 0:
                        exit = true;
                        break;
                    default:
                        out.println(TerminalStyle.ERROR, "Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                out.println(TerminalStyle.ERROR, "Error during demo: " + e.getMessage());
            }
            
            if (!exit) {
                out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        printHeader("Thank you for trying Pretty Java Core!");
        out.println("Visit https://github.com/joshuasalcedo/pretty-java for more information.");
    }
    
    private static void showMenu() {
        out.println(TerminalStyle.UI_HEADER, "\nSelect a demo to run:");
        out.println("1. Progress Bar");
        out.println("2. Spinner");
        out.println("3. Task List");
        out.println("4. Counter");
        out.println("5. Text Animation");
        out.println("6. Styled Text");
        out.println("7. All Features");
        out.println("0. Exit");
        out.print("> ");
    }
    
    private static int getUserChoice() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void printHeader(String text) {
        out.println();
        out.foreground(RGBColor.fromHex("#FFD700")).bold(true)
                .println("=".repeat(text.length() + 8));
        out.foreground(RGBColor.fromHex("#FFD700")).bold(true)
                .println("=== " + text + " ===");
        out.foreground(RGBColor.fromHex("#FFD700")).bold(true)
                .println("=".repeat(text.length() + 8));
        out.println();
    }
    
    /**
     * Demonstrates the progress bar feature.
     */
    private static void demoProgressBar() throws InterruptedException {
        printHeader("Progress Bar Demo");
        out.println("This demo shows a progress bar for a file download operation.");
        
        // Create a progress bar
        ProgressBarRunner progressBar = (ProgressBarRunner) ProgressRunnerFactory.createProgressBar(
                "Downloading file...", 
                50,
                RGBColor.of(0, 191, 255),  // Progress color (blue)
                RGBColor.of(40, 40, 40)     // Remaining color (dark gray)
        );
        progressBar.withPrettyOutputStream(out);
        
        // Start the progress bar
        progressBar.start();
        
        try {
            // Simulate file download with occasional speed changes
            long startTime = System.currentTimeMillis();
            long totalTime = 5000; // 5 seconds for the download
            
            while (true) {
                long elapsed = System.currentTimeMillis() - startTime;
                if (elapsed >= totalTime) break;
                
                // Calculate progress with some random variation
                double baseProgress = (double) elapsed / totalTime;
                double progress = Math.min(baseProgress * (0.8 + 0.4 * random.nextDouble()), 1.0);
                
                // Update progress
                progressBar.withProgress(progress);
                
                // Update message with download speed
                int speed = 5 + random.nextInt(15); // Random speed between 5-20 MB/s
                int totalSize = 128; // MB
                int downloadedMB = (int) (totalSize * progress);
                progressBar.withMessage(String.format("Downloading large-file.zip (%d/%d MB) at %d MB/s",
                        downloadedMB, totalSize, speed));
                
                Thread.sleep(100);
            }
            
            // Download complete
            progressBar.withProgress(1.0);
            progressBar.withMessage("Download complete: large-file.zip (128 MB)");
            Thread.sleep(200);
            progressBar.stop();
            
            // Display success message
            out.println(TerminalStyle.SUCCESS, "✓ Download complete - Checksum verified");
            
        } catch (Exception e) {
            // Stop progress bar and show error on exception
            progressBar.stop();
            out.println(TerminalStyle.ERROR, "✗ Download failed: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Demonstrates the spinner feature.
     */
    private static void demoSpinner() throws InterruptedException {
        printHeader("Spinner Demo");
        out.println("This demo shows a spinner for an API request operation.");
        
        // Create a spinner
        SpinnerProgressRunner spinner = (SpinnerProgressRunner) ProgressRunnerFactory.createSpinner(
                "Connecting to API...",
                SpinnerProgressRunner.SpinnerType.DOTS
        );
        spinner.withPrettyOutputStream(out);
        
        // Start the spinner
        spinner.start();
        
        try {
            // Simulate API request
            Thread.sleep(800);
            spinner.withMessage("Connected to API endpoint");
            Thread.sleep(800);
            spinner.withMessage("Sending request");
            Thread.sleep(1200);
            spinner.withMessage("Receiving response");
            Thread.sleep(1000);
            spinner.withMessage("Processing data");
            Thread.sleep(1200);
            
            // Request complete
            spinner.stop();
            
            // Display result
            out.println(TerminalStyle.SUCCESS, "✓ API request successful: Received 256 items");
            
        } catch (Exception e) {
            // Stop spinner and show error on exception
            spinner.stop();
            out.println(TerminalStyle.ERROR, "✗ API request failed: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Demonstrates the task list feature.
     */
    private static void demoTaskList() throws InterruptedException {
        printHeader("Task List Demo");
        out.println("This demo shows a task list for a deployment process.");
        
        // Create a task list
        TaskListProgressRunner taskList = (TaskListProgressRunner) ProgressRunnerFactory.createTaskList(
                "Deploying application to production:",
                "Run unit tests",
                "Build application",
                "Deploy to staging environment",
                "Run integration tests",
                "Deploy to production",
                "Verify production deployment",
                "Update documentation"
        );
        taskList.withPrettyOutputStream(out);
        
        // Start the task list
        taskList.start();
        
        try {
            // Step 1: Run unit tests
            taskList.markTaskInProgress(0);
            Thread.sleep(1000);
            taskList.markTaskComplete(0);
            
            // Step 2: Build application
            taskList.markTaskInProgress(1);
            Thread.sleep(1500);
            taskList.markTaskComplete(1);
            
            // Step 3: Deploy to staging
            taskList.markTaskInProgress(2);
            Thread.sleep(1200);
            taskList.markTaskComplete(2);
            
            // Step 4: Run integration tests
            taskList.markTaskInProgress(3);
            Thread.sleep(1800);
            taskList.markTaskComplete(3);
            
            // Step 5: Deploy to production
            taskList.markTaskInProgress(4);
            Thread.sleep(1500);
            taskList.markTaskComplete(4);
            
            // Step 6: Verify production
            taskList.markTaskInProgress(5);
            Thread.sleep(1000);
            taskList.markTaskComplete(5);
            
            // Step 7: Update documentation
            taskList.markTaskInProgress(6);
            Thread.sleep(1200);
            taskList.markTaskComplete(6);
            
            // Deployment complete
            Thread.sleep(500);
            taskList.stop();
            
            // Display summary
            out.println(TerminalStyle.SUCCESS, "✓ Application successfully deployed to production");
            
        } catch (Exception e) {
            // Stop task list and show error on exception
            taskList.stop();
            out.println(TerminalStyle.ERROR, "✗ Deployment process error: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Demonstrates the counter feature.
     */
    private static void demoCounter() throws InterruptedException {
        printHeader("Counter Demo");
        out.println("This demo shows a counter for a database migration operation.");
        
        final int TOTAL_RECORDS = 500;
        
        // Create a counter
        CounterProgressRunner counter = (CounterProgressRunner) ProgressRunnerFactory.createCounter(
                "Migrating database records",
                TOTAL_RECORDS,
                RGBColor.of(50, 205, 50)  // Lime Green
        );
        counter.setFormat("Record %d of %d");
        counter.withPrettyOutputStream(out);
        
        // Start the counter
        counter.start();
        
        try {
            // Simulate database operation with batch processing
            int batchSize = 25;
            int totalProcessed = 0;
            
            while (totalProcessed < TOTAL_RECORDS) {
                // Process a batch
                int currentBatch = Math.min(batchSize, TOTAL_RECORDS - totalProcessed);
                
                // Update message with current table
                String[] tables = {"users", "orders", "products", "customers", "transactions"};
                String currentTable = tables[totalProcessed / (TOTAL_RECORDS / tables.length)];
                counter.withMessage("Migrating " + currentTable + " records");
                
                // Process records in batch
                for (int i = 0; i < currentBatch; i++) {
                    totalProcessed++;
                    counter.withProgress((double) totalProcessed / TOTAL_RECORDS);
                    
                    // Simulate varying processing times
                    Thread.sleep(10 + (int)(Math.random() * 20));
                }
                
                // Simulate batch commit
                Thread.sleep(100);
            }
            
            // Migration complete
            counter.withProgress(1.0);
            counter.withMessage("Database migration complete");
            Thread.sleep(200);
            counter.stop();
            
            // Display summary
            out.println(TerminalStyle.SUCCESS, "✓ Successfully migrated " + TOTAL_RECORDS + " records");
            
        } catch (Exception e) {
            // Stop counter and show error on exception
            counter.stop();
            out.println(TerminalStyle.ERROR, "✗ Database migration failed: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Demonstrates the text animation feature.
     */
    private static void demoTextAnimation() throws InterruptedException {
        printHeader("Text Animation Demo");
        out.println("This demo shows text animation for a search operation.");
        
        // Create a text animation
        TextAnimationRunner textAnim = (TextAnimationRunner) ProgressRunnerFactory.createTextAnimation(
                "Searching for 'performance optimization'",
                TextAnimationRunner.AnimationType.TYPING
        );
        textAnim.withPrettyOutputStream(out);
        
        // Start the animation
        textAnim.start();
        
        try {
            // Simulate search operation
            Thread.sleep(2000);
            
            // Switch to a dots animation for indexing
            textAnim.stop();
            
            DotsAnimationRunner dotsAnim = (DotsAnimationRunner) ProgressRunnerFactory.createDotsAnimation(
                    "Indexing search results"
            );
            dotsAnim.withPrettyOutputStream(out);
            dotsAnim.start();
            
            Thread.sleep(1500);
            dotsAnim.stop();
            
            // Show results with a counter
            final int TOTAL_RESULTS = 128;
            CounterProgressRunner resultCounter = (CounterProgressRunner) ProgressRunnerFactory.createCounter(
                    "Processing search results",
                    TOTAL_RESULTS
            );
            resultCounter.withPrettyOutputStream(out);
            resultCounter.start();
            
            // Process results
            for (int i = 0; i < TOTAL_RESULTS; i++) {
                resultCounter.withProgress((double) i / TOTAL_RESULTS);
                Thread.sleep(10);
            }
            
            resultCounter.withProgress(1.0);
            Thread.sleep(200);
            resultCounter.stop();
            
            // Display summary
            out.println(TerminalStyle.SUCCESS, "✓ Search complete: Found " + TOTAL_RESULTS + " results");
            
            // Display a few sample results
            out.println("\nTop results:");
            out.foreground(RGBColor.of(135, 206, 250)).println("1. 10 Ways to Optimize Java Performance");
            out.foreground(RGBColor.of(135, 206, 250)).println("2. Performance Optimization Techniques for Web Applications");
            out.foreground(RGBColor.of(135, 206, 250)).println("3. Database Query Optimization Guide");
            
        } catch (Exception e) {
            // Show error on exception
            out.println(TerminalStyle.ERROR, "✗ Search failed: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Demonstrates the styled text feature.
     */
    private static void demoStyledText() {
        printHeader("Styled Text Demo");
        out.println("This demo shows various text styling options.");
        
        // Basic styling
        out.println("\nBasic styling:");
        out.bold(true).println("Bold text");
        out.bold(false).italic(true).println("Italic text");
        out.italic(false).underline(true).println("Underlined text");
        out.underline(false).strikethrough(true).println("Strikethrough text");
        out.reset();
        
        // Colors
        out.println("\nColors:");
        out.foreground(RGBColor.of(255, 0, 0)).println("Red text");
        out.foreground(RGBColor.of(0, 255, 0)).println("Green text");
        out.foreground(RGBColor.of(0, 0, 255)).println("Blue text");
        out.foreground(RGBColor.of(255, 255, 255)).background(RGBColor.of(0, 0, 0)).println("White text on black background");
        out.reset();
        
        // Hex colors
        out.println("\nHex colors:");
        out.foreground(RGBColor.fromHex("#FF5733")).println("Custom hex color #FF5733");
        out.foreground(RGBColor.fromHex("#33FF57")).println("Custom hex color #33FF57");
        out.foreground(RGBColor.fromHex("#3357FF")).println("Custom hex color #3357FF");
        out.reset();
        
        // Predefined styles
        out.println("\nPredefined styles:");
        out.println(TerminalStyle.SUCCESS, "Success message");
        out.println(TerminalStyle.ERROR, "Error message");
        out.println(TerminalStyle.WARNING, "Warning message");
        out.println(TerminalStyle.INFO, "Info message");
        out.println(TerminalStyle.HIGHLIGHT, "Highlighted message");
        out.println(TerminalStyle.UI_HEADER, "UI Header");
        out.reset();
        
        // Combined styling
        out.println("\nCombined styling:");
        out.foreground(RGBColor.of(255, 215, 0)).bold(true).underline(true)
                .println("Bold, underlined gold text");
        out.reset();
    }
    
    /**
     * Demonstrates all features in a single workflow.
     */
    private static void demoAllFeatures() throws InterruptedException {
        printHeader("Complete Workflow Demo");
        out.println("This demo shows all features working together in a complete workflow.");
        out.println("Scenario: Deploying a web application to production");
        
        // Step 1: Build and test
        out.println(TerminalStyle.UI_HEADER, "\nPhase 1: Build and Test");
        
        // Use a task list for build and test
        TaskListProgressRunner buildTasks = (TaskListProgressRunner) ProgressRunnerFactory.createTaskList(
                "Build and test phase:",
                "Compile source code",
                "Run unit tests",
                "Run integration tests",
                "Generate documentation"
        );
        buildTasks.withPrettyOutputStream(out);
        buildTasks.start();
        
        // Compile source code
        buildTasks.markTaskInProgress(0);
        Thread.sleep(1000);
        buildTasks.markTaskComplete(0);
        
        // Run unit tests
        buildTasks.markTaskInProgress(1);
        
        // Show test progress with a counter
        CounterProgressRunner testCounter = (CounterProgressRunner) ProgressRunnerFactory.createCounter(
                "Running unit tests",
                100
        );
        testCounter.withPrettyOutputStream(out);
        testCounter.start();
        
        for (int i = 0; i <= 100; i++) {
            testCounter.withProgress(i / 100.0);
            if (i % 20 == 0) {
                testCounter.withMessage("Running test suite " + (i / 20 + 1) + " of 5");
            }
            Thread.sleep(20);
        }
        
        testCounter.stop();
        buildTasks.markTaskComplete(1);
        
        // Run integration tests
        buildTasks.markTaskInProgress(2);
        Thread.sleep(1200);
        buildTasks.markTaskComplete(2);
        
        // Generate documentation
        buildTasks.markTaskInProgress(3);
        Thread.sleep(800);
        buildTasks.markTaskComplete(3);
        
        buildTasks.stop();
        out.println(TerminalStyle.SUCCESS, "✓ Build and test phase completed successfully");
        
        // Step 2: Deployment
        out.println(TerminalStyle.UI_HEADER, "\nPhase 2: Deployment");
        
        // Use a spinner for deployment preparation
        SpinnerProgressRunner spinner = (SpinnerProgressRunner) ProgressRunnerFactory.createSpinner(
                "Preparing deployment package",
                SpinnerProgressRunner.SpinnerType.DOTS
        );
        spinner.withPrettyOutputStream(out);
        spinner.start();
        
        Thread.sleep(1500);
        spinner.withMessage("Validating deployment configuration");
        Thread.sleep(1000);
        spinner.withMessage("Connecting to deployment server");
        Thread.sleep(1200);
        
        spinner.stop();
        
        // Use a progress bar for the actual deployment
        ProgressBarRunner deployBar = (ProgressBarRunner) ProgressRunnerFactory.createProgressBar(
                "Uploading deployment package", 
                50,
                RGBColor.of(0, 191, 255),
                RGBColor.of(40, 40, 40)
        );
        deployBar.withPrettyOutputStream(out);
        deployBar.start();
        
        for (int i = 0; i <= 100; i++) {
            deployBar.withProgress(i / 100.0);
            if (i % 10 == 0) {
                deployBar.withMessage(String.format("Uploading deployment package: %d%%", i));
            }
            Thread.sleep(50);
        }
        
        deployBar.stop();
        
        // Step 3: Verification
        out.println(TerminalStyle.UI_HEADER, "\nPhase 3: Verification");
        
        // Use text animation for verification
        TextAnimationRunner textAnim = (TextAnimationRunner) ProgressRunnerFactory.createTextAnimation(
                "Verifying deployment",
                TextAnimationRunner.AnimationType.TYPING
        );
        textAnim.withPrettyOutputStream(out);
        textAnim.start();
        
        Thread.sleep(2000);
        textAnim.stop();
        
        // Final status
        out.println("\nDeployment Status:");
        out.println(TerminalStyle.SUCCESS, "✓ Application deployed successfully");
        out.println(TerminalStyle.SUCCESS, "✓ All services are running");
        out.println(TerminalStyle.SUCCESS, "✓ Health checks passed");
        
        out.foreground(RGBColor.fromHex("#FFD700")).bold(true)
                .println("\n=== Deployment Complete ===");
    }
}