package io.joshuasalcedo.pretty.core.model;

import io.joshuasalcedo.pretty.core.model.progress.AbstractProgressRunner;
import io.joshuasalcedo.pretty.core.model.progress.CounterProgressRunner;
import io.joshuasalcedo.pretty.core.model.progress.DotsAnimationRunner;
import io.joshuasalcedo.pretty.core.model.progress.TextAnimationRunner;
import io.joshuasalcedo.pretty.core.model.stream.PrettyPrintStream;
import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.theme.TerminalStyle;

import java.util.Random;

/**
 * Examples of how to use progress runners in real-world applications.
 * <p>
 * This class demonstrates practical examples of using progress runners in
 * typical application scenarios like file operations, HTTP requests,
 * and batch processing.
 * </p>
 */
public class ProgressRunnerExamples {

    public static void main(String[] args) {
        // Create a PrettyPrintStream for output
        PrettyPrintStream out = new PrettyPrintStream(System.out);

        // Print header
        out.foreground(RGBColor.fromHex("#FFD700")).bold(true)
                .println("=== Progress Runner Practical Examples ===\n");

        try {
            // HTTP request example
            httpRequestExample(out);

            // File operation example
            fileOperationExample(out);

            // Database operation example
            databaseOperationExample(out);

            // Command execution example
            commandExecutionExample(out);

            // Search operation example
            searchOperationExample(out);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            out.println(TerminalStyle.ERROR, "Examples interrupted!");
        }

        // Final message
        out.foreground(RGBColor.fromHex("#FFD700")).bold(true)
                .println("\n=== Examples Complete ===");
    }

    /**
     * Example of using a spinner for an HTTP request.
     */
    private static void httpRequestExample(PrettyPrintStream out) throws InterruptedException {
        out.println(TerminalStyle.UI_HEADER, "HTTP Request Example");
        out.println("Using a spinner to indicate an ongoing API request.");

        // Create a spinner for the HTTP request
        AbstractProgressRunner spinner = ProgressRunnerFactory.createSpinner(
                "Fetching data from API...",
                SpinnerProgressRunner.SpinnerType.DOTS
        );
        spinner.withPrettyOutputStream(out);

        // Start the spinner
        spinner.start();

        try {
            // Simulate HTTP request
            Thread.sleep(500);
            spinner.withMessage("Connected to API endpoint");
            Thread.sleep(500);
            spinner.withMessage("Sending request");
            Thread.sleep(1000);
            spinner.withMessage("Receiving response");
            Thread.sleep(800);
            spinner.withMessage("Processing data");
            Thread.sleep(700);

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
     * Example of using a progress bar for a file operation.
     */
    private static void fileOperationExample(PrettyPrintStream out) throws InterruptedException {
        out.println(TerminalStyle.UI_HEADER, "\nFile Operation Example");
        out.println("Using a progress bar to show file upload/download progress.");

        // Create a progress bar for the file operation
        ProgressBarRunner progressBar = (ProgressBarRunner) ProgressRunnerFactory.createProgressBar(
                "Downloading large-file.zip (128MB)",
                50,
                RGBColor.of(0, 191, 255),  // Deep Sky Blue
                RGBColor.of(40, 40, 40)    // Dark Gray
        );
        progressBar.withPrettyOutputStream(out);

        // Start the progress bar
        progressBar.start();

        try {
            // Simulate file download with occasional speed changes
            Random random = new Random();
            long startTime = System.currentTimeMillis();
            long totalTime = 3000; // 3 seconds for the download

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
                int downloadedMB = (int) (128 * progress);
                progressBar.withMessage(String.format("Downloading large-file.zip (%d/%d MB) at %d MB/s",
                        downloadedMB, 128, speed));

                Thread.sleep(100);
            }

            // Download complete
            progressBar.withProgress(1.0);
            progressBar.withMessage("Download complete: large-file.zip (128 MB)");
            Thread.sleep(200);
            progressBar.stop();

            // Display checksums
            out.println(TerminalStyle.SUCCESS, "✓ Download complete - Checksum verified");

        } catch (Exception e) {
            // Stop progress bar and show error on exception
            progressBar.stop();
            out.println(TerminalStyle.ERROR, "✗ Download failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Example of using a counter for database operations.
     */
    private static void databaseOperationExample(PrettyPrintStream out) throws InterruptedException {
        out.println(TerminalStyle.UI_HEADER, "\nDatabase Operation Example");
        out.println("Using a counter to track progress through database records.");

        final int TOTAL_RECORDS = 1000;

        // Create a counter for the database operation
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
            int batchSize = 50;
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
                    Thread.sleep(5 + (int)(Math.random() * 15));
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
     * Example of using task list for command execution.
     */
    private static void commandExecutionExample(PrettyPrintStream out) throws InterruptedException {
        out.println(TerminalStyle.UI_HEADER, "\nCommand Execution Example");
        out.println("Using a task list to track steps in a deployment process.");

        // Create a task list for the deployment process
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
            // Simulate deployment process with occasional failures
            Random random = new Random();

            // Step 1: Run unit tests
            taskList.markTaskInProgress(0);
            Thread.sleep(800);
            taskList.markTaskComplete(0);

            // Step 2: Build application
            taskList.markTaskInProgress(1);
            Thread.sleep(1200);
            taskList.markTaskComplete(1);

            // Step 3: Deploy to staging
            taskList.markTaskInProgress(2);
            Thread.sleep(1000);

            // Simulate a random failure in staging deployment
            if (random.nextDouble() < 0.3) {
                taskList.markTaskFailed(2);
                taskList.stop();
                out.println(TerminalStyle.ERROR, "✗ Deployment failed: Staging deployment error");
                return;
            } else {
                taskList.markTaskComplete(2);
            }

            // Step 4: Run integration tests
            taskList.markTaskInProgress(3);
            Thread.sleep(1500);
            taskList.markTaskComplete(3);

            // Step 5: Deploy to production
            taskList.markTaskInProgress(4);
            Thread.sleep(1200);
            taskList.markTaskComplete(4);

            // Step 6: Verify production
            taskList.markTaskInProgress(5);
            Thread.sleep(800);
            taskList.markTaskComplete(5);

            // Step 7: Update documentation
            taskList.markTaskInProgress(6);
            Thread.sleep(700);
            taskList.markTaskComplete(6);

            // Deployment complete
            Thread.sleep(200);
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
     * Example of using text animation for a search operation.
     */
    private static void searchOperationExample(PrettyPrintStream out) throws InterruptedException {
        out.println(TerminalStyle.UI_HEADER, "\nSearch Operation Example");
        out.println("Using text animation to indicate an ongoing search.");

        // Create a text animation for the search
        TextAnimationRunner textAnim = (TextAnimationRunner) ProgressRunnerFactory.createTextAnimation(
                "Searching for 'performance optimization'",
                TextAnimationRunner.AnimationType.TYPING
        );
        textAnim.withPrettyOutputStream(out);

        // Start the animation
        textAnim.start();

        try {
            // Simulate search operation
            Thread.sleep(1500);

            // Switch to a dots animation for indexing
            textAnim.stop();

            DotsAnimationRunner dotsAnim = (DotsAnimationRunner) ProgressRunnerFactory.createDotsAnimation(
                    "Indexing search results"
            );
            dotsAnim.withPrettyOutputStream(out);
            dotsAnim.start();

            Thread.sleep(1200);
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
}