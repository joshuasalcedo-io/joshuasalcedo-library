//package io.joshuasalcedo.pretty.core.model.progress.threads;
//
//import io.joshuasalcedo.pretty.core.model.progress.CounterProgressRunner;
//import io.joshuasalcedo.pretty.core.model.time.PrettyTimeStamp;
//import io.joshuasalcedo.pretty.core.model.time.TimeStampFactory;
//import io.joshuasalcedo.pretty.core.properties.RGBColor;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.TimeUnit;
//
///**
// * Simplified demo that clearly shows one thread at a time
// * with proper thread management to avoid lingering threads
// */
//public class SimpleThreadedProgressDemo {
//
//    // A list of items to process
//    private static List<String> createSampleItems(int count, String prefix) {
//        List<String> items = new ArrayList<>();
//        for (int i = 1; i <= count; i++) {
//            items.add(prefix + "-" + i);
//        }
//        return items;
//    }
//
//    // Simulate processing an item with variable time
//    private static void processItem(String item) throws InterruptedException {
//        Random random = new Random();
//        // Shorter processing time for demo clarity
//        int processingTime = 25 + random.nextInt(75);
//        TimeUnit.MILLISECONDS.sleep(processingTime);
//    }
//
//    /**
//     * Main demo method
//     */
//    public static void main(String[] args) {
//        // Use try-with-resources to ensure all resources are properly closed
//        try {
//            System.out.println("=== SIMPLE THREADED PROGRESS DEMO ===");
//
//            // Only run one clear demo
//            runProgressDemo();
//
//            PrettyTimeStamp endTime = TimeStampFactory.createPrettyTimeStamp();
//            System.out.println("\n" + endTime + " Demo completed successfully!");
//
//        } catch (Exception e) {
//            System.err.println("Demo interrupted: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Simple demo showing only one thread at a time with clear progression
//     */
//    private static void runProgressDemo() throws InterruptedException {
//        PrettyTimeStamp startTime = TimeStampFactory.createPrettyTimeStamp();
//        System.out.println(startTime + " Starting progress demo...\n");
//
////        // First demo: Basic document processing
////        System.out.println("=== DEMO 1: BASIC DOCUMENT PROCESSING ===");
////        processDocuments();
////        System.out.println("\nDocument processing complete.");
////        Thread.sleep(500); // Brief pause for readability
//
//        // Second demo: Indeterminate record analysis
//        System.out.println("\n=== DEMO 2: INDETERMINATE RECORD ANALYSIS ===");
//        analyzeRecords();
//        System.out.println("\nRecord analysis complete.");
//        Thread.sleep(500); // Brief pause for readability
//
////        // Third demo: Animated data processing
////        System.out.println("\n=== DEMO 3: ANIMATED DATA PROCESSING ===");
////        processDataWithAnimations();
////        System.out.println("\nData processing complete.");
//    }
//
//    /**
//     * Demo 1: Process documents with basic counter
//     */
//    private static void processDocuments() throws InterruptedException {
//        // Create sample data
//        List<String> documents = createSampleItems(15, "Doc");
//
//        // Create task to process documents
//        Runnable documentTask = () -> {
//            try {
//                // Get a reference to the progress runner to update
//                ThreadedProgressRunner runner = currentRunner;
//
//                for (int i = 0; i < documents.size(); i++) {
//                    // Process the document
//                    processItem(documents.get(i));
//
//                    // Update progress
//                    double progress = (double)(i + 1) / documents.size();
//                    runner.withProgress(progress);
//
//                    // Update message every few documents
//                    if ((i + 1) % 5 == 0) {
//                        runner.withMessage("Processed " + (i + 1) + " documents");
//                    }
//                }
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        // Create and start the progress runner
//        try (ThreadedProgressRunner runner =
//                ProgressRunnerFactory.createThreadedCounter(
//                    "Processing documents", documents.size(), documentTask)) {
//
//            // Store reference for the task to use
//            currentRunner = runner;
//
//            // Start and wait for completion
//            runner.start();
//            runner.awaitCompletion();
//        }
//    }
//
//    /**
//     * Demo 2: Analyze records with indeterminate counter that switches to determinate
//     */
//    private static void analyzeRecords() throws InterruptedException {
//        // Create sample data
//        List<String> records = createSampleItems(15, "Record");
//
//        // Create task to analyze records
//        Runnable recordTask = () -> {
//            try {
//                // Get a reference to the progress runner to update
//                ThreadedProgressRunner runner = currentRunner;
//
//                for (int i = 0; i < records.size(); i++) {
//                    // Process the record
//                    processItem(records.get(i));
//
//                    // Switch mode halfway through
//                    if (i == records.size() / 2) {
//                        runner.withIndeterminate(false)
//                              .withMessage("Finalizing analysis");
//                    }
//
//                    // Update progress after switching to determinate mode
//                    if (i >= records.size() / 2) {
//                        double progress = (double)(i - records.size()/2) /
//                                         (records.size() - records.size()/2);
//                        runner.withProgress(progress);
//                    }
//                }
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        // Create and start the progress runner with try-with-resources
//        try (ThreadedProgressRunner runner =
//                ProgressRunnerFactory.createThreadedIndeterminateCounter(
//                    "Analyzing records", records.size(), recordTask)) {
//
//            // Store reference for the task to use
//            currentRunner = runner;
//
//            // Start and wait for completion
//            runner.start();
//            runner.awaitCompletion();
//        }
//    }
//
//    /**
//     * Demo 3: Process data with animated counter
//     */
//    private static void processDataWithAnimations() throws InterruptedException {
//        // Create sample data
//        List<String> dataItems = createSampleItems(20, "Data");
//
//        // Create task to process data with animations
//        Runnable dataTask = () -> {
//            try {
//                // Get a reference to the progress runner to update
//                ThreadedProgressRunner runner = currentRunner;
//
//                // Animation frames
//                String[] spinnerFrames = {"|", "/", "-", "\\"};
//
//                for (int i = 0; i < dataItems.size(); i++) {
//                    // Process the data item
//                    processItem(dataItems.get(i));
//
//                    // Update progress
//                    double progress = (double)(i + 1) / dataItems.size();
//                    runner.withProgress(progress);
//
//                    // Animate with spinner and dynamic color
//                    String spinner = spinnerFrames[i % spinnerFrames.length];
//                    runner.withMessage(spinner + " Processing with visual effects");
//
//                    // Change color based on progress
//                    int red = 255 - (int)(255 * progress);
//                    int green = (int)(255 * progress);
//                    RGBColor dynamicColor = RGBColor.of(red, green, 100);
//
//                    // We need to cast to access the counter's color setting method
//                    if (runner.getProgressRunner() instanceof CounterProgressRunner) {
//                        ((CounterProgressRunner)runner.getProgressRunner())
//                            .setCounterColor(dynamicColor);
//                    }
//                }
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        // Create and start the progress runner with try-with-resources
//        try (ThreadedProgressRunner runner =
//                ProgressRunnerFactory.createThreadedAnimatedCounter(
//                    "Processing data", dataItems.size(), dataTask)) {
//
//            // Store reference for the task to use
//            currentRunner = runner;
//
//            // Start and wait for completion
//            runner.start();
//            runner.awaitCompletion();
//        }
//    }
//
//    // Shared reference to the current runner
//    private static ThreadedProgressRunner currentRunner;
//}