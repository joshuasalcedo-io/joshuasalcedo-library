//package io.joshuasalcedo.pretty.core.model.progress.threads;
//
//import io.joshuasalcedo.pretty.core.model.progress.CounterProgressRunner;
//import io.joshuasalcedo.pretty.core.model.progress.threads.ProgressRunnerFactory;
//import io.joshuasalcedo.pretty.core.model.progress.threads.ThreadedProgressRunner;
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
// * Modified demo that runs only one thread at a time
// */
//public class ThreadedProgressDemo {
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
//        // Random processing time between 50-200ms
//        int processingTime = 50 + random.nextInt(150);
//        TimeUnit.MILLISECONDS.sleep(processingTime);
//    }
//
//    /**
//     * Main demo method
//     */
//    public static void main(String[] args) {
//        try {
//            System.out.println("=== SEQUENTIAL THREADED PROGRESS RUNNERS DEMO ===");
//            System.out.println("(Running one thread at a time)");
//
//            // Demo 1: Sequential progress runners
//            demoSequentialProgressRunners();
//
//            // Demo 2: Sequential execution of "concurrent-style" tasks
//            demoSequentialExecution();
//
//            // Demo 3: Factory utility methods for common tasks
//            demoFactoryUtilityMethods();
//
//            PrettyTimeStamp endTime = TimeStampFactory.createPrettyTimeStamp();
//            System.out.println("\n" + endTime + " All demos completed!");
//
//        } catch (Exception e) {
//            System.err.println("Demo interrupted: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Demo showing sequential progress runners with different styles
//     */
//    private static void demoSequentialProgressRunners() throws InterruptedException {
//        PrettyTimeStamp startTime = TimeStampFactory.createPrettyTimeStamp();
//        System.out.println(startTime + " Starting sequential progress runners demo...\n");
//
//        // Create sample data
//        List<String> files = createSampleItems(20, "File");
//
//        // Demo 1: Basic Counter
//        Runnable fileProcessingTask = () -> {
//            try {
//                for (int i = 0; i < files.size(); i++) {
//                    processItem(files.get(i));
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        // Use factory to create a threaded counter
//        ThreadedProgressRunner fileProcessor =
//                ProgressRunnerFactory.createThreadedCounter(
//                        "Processing files", files.size(), fileProcessingTask);
//
//        // Start the processor and wait for completion
//        fileProcessor.start();
//        fileProcessor.awaitCompletion();
//
//        System.out.println("File processing complete.\n");
//        Thread.sleep(500);
//
//        // Demo 2: Indeterminate Counter
//        List<String> records = createSampleItems(15, "Record");
//        Runnable recordAnalysisTask = () -> {
//            try {
//                for (int i = 0; i < records.size(); i++) {
//                    processItem(records.get(i));
//
//                    // After half the records, switch to determinate mode
//                    if (i >= records.size() / 2) {
//                        double progress = (double)(i - records.size()/2) /
//                                (records.size() - records.size()/2);
//                        // Create a local reference to avoid using the fileProcessor
//                        // which is out of scope in this lambda
//                        ThreadedProgressRunner localRef = recordAnalyzer;
//                        localRef.withProgress(progress)
//                                .withIndeterminate(false)
//                                .withMessage("Finalizing analysis");
//                    }
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        // Use factory to create a threaded indeterminate counter
//        ThreadedProgressRunner recordAnalyzer =
//                ProgressRunnerFactory.createThreadedIndeterminateCounter(
//                        "Analyzing records", records.size(), recordAnalysisTask);
//
//        // Start the analyzer and wait for completion
//        recordAnalyzer.start();
//        recordAnalyzer.awaitCompletion();
//
//        System.out.println("Record analysis complete.\n");
//        Thread.sleep(500);
//
//        // Demo 3: Animated Counter
//        List<String> data = createSampleItems(25, "Data");
//        Runnable dataProcessingTask = () -> {
//            try {
//                String[] spinnerFrames = {"|", "/", "-", "\\"};
//
//                for (int i = 0; i < data.size(); i++) {
//                    // Update progress
//                    double progress = (double) i / data.size();
//
//                    // Create a local reference to the current processor
//                    ThreadedProgressRunner localRef = dataProcessor;
//                    localRef.withProgress(progress);
//
//                    // Animate the message
//                    String spinner = spinnerFrames[i % spinnerFrames.length];
//                    localRef.withMessage(spinner + " Processing data with effects");
//
//                    // Process the item
//                    processItem(data.get(i));
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        // Use factory to create a threaded animated counter
//        ThreadedProgressRunner dataProcessor =
//                ProgressRunnerFactory.createThreadedAnimatedCounter(
//                        "Processing data with effects", data.size(), dataProcessingTask);
//
//        // Start the processor and wait for completion
//        dataProcessor.start();
//        dataProcessor.awaitCompletion();
//
//        System.out.println("Data processing complete.\n");
//    }
//
//    /**
//     * Demo running tasks one at a time instead of concurrently
//     */
//    private static void demoSequentialExecution() throws InterruptedException {
//        PrettyTimeStamp startTime = TimeStampFactory.createPrettyTimeStamp();
//        System.out.println("\n" + startTime + " Starting sequential execution demo...\n");
//
//        // Create sample data for three different processes
//        List<String> images = createSampleItems(30, "Image");
//        List<String> documents = createSampleItems(20, "Doc");
//        List<String> videos = createSampleItems(15, "Video");
//
//        System.out.println("Running image processing task:");
//        // Create and run the image processing task
//        Runnable imageTask = () -> {
//            try {
//                for (int i = 0; i < images.size(); i++) {
//                    // Simulate processing
//                    processItem(images.get(i));
//
//                    // Update progress on the runner
//                    double progress = (double) (i + 1) / images.size();
//                    imageProcessor.withProgress(progress);
//
//                    // Periodically update message
//                    if ((i + 1) % 5 == 0) {
//                        imageProcessor.withMessage("Processing images: " + (i + 1) + "/" + images.size());
//                    }
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        ThreadedProgressRunner imageProcessor =
//                ProgressRunnerFactory.createThreadedCounter("Processing images", images.size(), imageTask);
//
//        // Start and wait for completion
//        imageProcessor.start();
//        imageProcessor.awaitCompletion();
//        System.out.println("Image processing complete.\n");
//        Thread.sleep(500);
//
//        System.out.println("Running document processing task:");
//        // Create and run the document processing task
//        Runnable docTask = () -> {
//            try {
//                for (int i = 0; i < documents.size(); i++) {
//                    // Simulate processing
//                    processItem(documents.get(i));
//
//                    // Update progress
//                    double progress = (double) (i + 1) / documents.size();
//                    docProcessor.withProgress(progress);
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        ThreadedProgressRunner docProcessor =
//                ProgressRunnerFactory.createThreadedIndeterminateCounter("Processing documents",
//                        documents.size(), docTask);
//
//        // Start and wait for completion
//        docProcessor.start();
//        docProcessor.awaitCompletion();
//        System.out.println("Document processing complete.\n");
//        Thread.sleep(500);
//
//        System.out.println("Running video processing task:");
//        // Create and run the video processing task
//        Runnable videoTask = () -> {
//            try {
//                for (int i = 0; i < videos.size(); i++) {
//                    // Simulate processing
//                    processItem(videos.get(i));
//
//                    // Update progress
//                    double progress = (double) (i + 1) / videos.size();
//                    videoProcessor.withProgress(progress);
//
//                    // Change color as progress increases
//                    int red = 255 - (int)(255 * progress);
//                    int green = (int)(255 * progress);
//                    RGBColor dynamicColor = RGBColor.of(red, green, 100);
//
//                    // We need to cast to access the counter's color setting method
//                    if (videoProcessor.getProgressRunner() instanceof CounterProgressRunner) {
//                        ((CounterProgressRunner)videoProcessor.getProgressRunner())
//                                .setCounterColor(dynamicColor);
//                    }
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        ThreadedProgressRunner videoProcessor =
//                ProgressRunnerFactory.createThreadedAnimatedCounter("Processing videos",
//                        videos.size(), videoTask);
//
//        // Start and wait for completion
//        videoProcessor.start();
//        videoProcessor.awaitCompletion();
//        System.out.println("Video processing complete.\n");
//    }
//
//    // Reference to the processors
//    private static ThreadedProgressRunner imageProcessor;
//    private static ThreadedProgressRunner docProcessor;
//    private static ThreadedProgressRunner dataProcessor;
//    private static ThreadedProgressRunner videoProcessor;
//    private static ThreadedProgressRunner recordAnalyzer;
//
//    /**
//     * Demo showing the factory utility methods for common tasks
//     */
//    private static void demoFactoryUtilityMethods() throws Exception {
//        PrettyTimeStamp startTime = TimeStampFactory.createPrettyTimeStamp();
//        System.out.println("\n" + startTime + " Starting factory utility methods demo...\n");
//
//        // Create sample data
//        List<String> users = createSampleItems(15, "User");
//
//        System.out.println("Using processWithProgress utility method:");
//        // Using the processWithProgress utility method
//        ProgressRunnerFactory.processWithProgress(
//                "Processing users",
//                users,
//                user -> {
//                    try {
//                        processItem(user);
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    }
//                },
//                true  // Use animated counter
//        );
//
//        System.out.println("Using executeWithProgress utility method:");
//        // Using the executeWithProgress utility method with a progress reporting task
//        String result = ProgressRunnerFactory.executeWithProgress(
//                "Generating report",
//                100,  // 100 units of work
//                progress -> {
//                    StringBuilder report = new StringBuilder();
//                    report.append("Processing Report\n");
//                    report.append("================\n\n");
//
//                    // Simulate work with progress reporting
//                    for (int i = 0; i < 100; i++) {
//                        try {
//                            // Update progress
//                            progress.updateProgress((double) i / 100);
//
//                            // Simulate work
//                            Thread.sleep(20);
//
//                            // Add to report based on milestone
//                            if (i % 20 == 0) {
//                                report.append("Milestone ").append(i / 20 + 1).append(" completed\n");
//                            }
//                        } catch (InterruptedException e) {
//                            Thread.currentThread().interrupt();
//                        }
//                    }
//
//                    report.append("\nReport completed successfully!");
//                    return report.toString();
//                },
//                true  // Use animated counter
//        );
//
//        System.out.println("\nReport generation complete. Result length: " + result.length() + " characters\n");
//    }
//}