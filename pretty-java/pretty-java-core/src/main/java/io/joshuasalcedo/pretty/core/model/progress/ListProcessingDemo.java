package io.joshuasalcedo.pretty.core.model.progress;

import io.joshuasalcedo.pretty.core.model.progress.CounterProgressRunner;
import io.joshuasalcedo.pretty.core.model.time.PrettyTimeStamp;
import io.joshuasalcedo.pretty.core.model.time.TimeStampFactory;
import io.joshuasalcedo.pretty.core.properties.RGBColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * Demo showcasing the CounterProgressRunner with list processing animation
 * Using the correct method calls from AbstractProgressRunner
 */
public class ListProcessingDemo {

    // A list of items to process
    private static List<String> createSampleItems(int count) {
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            items.add("Item-" + i);
        }
        return items;
    }

    // Simulate processing an item with variable time
    private static void processItem(String item) throws InterruptedException {
        Random random = new Random();

        // Random processing time between 50-200ms
        int processingTime = 50 + random.nextInt(150);

        // Simulate work
        TimeUnit.MILLISECONDS.sleep(processingTime);
    }

    public static void main(String[] args) {
        try {
            // Create sample data
            int totalItems = 25;
            List<String> items = createSampleItems(totalItems);

            System.out.println("=== LIST PROCESSING DEMO ===");
            PrettyTimeStamp startTime = TimeStampFactory.createPrettyTimeStamp();
            System.out.println(startTime + " Starting to process " + totalItems + " items...\n");

            // Create a progress counter
            demoBasicCounter(items);

            // Indeterminate mode demo
            Thread.sleep(1000); // Pause between demos
            demoIndeterminateCounter(items);

            // Animation tricks
            Thread.sleep(1000); // Pause between demos
            demoAnimatedCounter(items);

            PrettyTimeStamp endTime = TimeStampFactory.createPrettyTimeStamp();
            System.out.println("\n" + endTime + " All demos completed!");

        } catch (InterruptedException e) {
            System.err.println("Demo interrupted: " + e.getMessage());
        }
    }

    /**
     * Demo using a basic counter with default settings
     */
    private static void demoBasicCounter(List<String> items) throws InterruptedException {
        System.out.println("DEMO 1: Basic Counter");

        // Create a counter with default settings
        CounterProgressRunner counter = new CounterProgressRunner("Processing items", items.size());

        // Start the animation
        counter.start();

        // Process each item
        for (int i = 0; i < items.size(); i++) {
            // Update progress before processing
            double progress = (double) i / items.size();
            counter.withProgress(progress);
            counter.withMessage("Processing item " + (i + 1) + " of " + items.size());

            // Process the item
            processItem(items.get(i));

            // Show logs between progress updates for a more realistic animation
            if ((i + 1) % 5 == 0) {
                PrettyTimeStamp timestamp = TimeStampFactory.createPrettyTimeStamp();
                System.out.println("\n" + timestamp + " Completed " + (i + 1) + " items");
                counter.withMessage("Processing items"); // Reset message
            }
        }

        // Complete the progress
        counter.withProgress(1.0).withMessage("Processing complete");
        Thread.sleep(500);
        counter.stop();

        System.out.println("Basic processing complete.");
    }

    /**
     * Demo using indeterminate mode for when progress can't be calculated
     */
    private static void demoIndeterminateCounter(List<String> items) throws InterruptedException {
        System.out.println("\nDEMO 2: Indeterminate Counter (unknown progress)");

        // Create a counter with indeterminate mode
        CounterProgressRunner counter = new CounterProgressRunner("Analyzing data", items.size(), 50);
        counter.setCounterColor(RGBColor.named("cyan"))
                .setFormat("Processing %d of %d")
                .setShowBrackets(false);

        // Start in indeterminate mode
        counter.withIndeterminate(true).start();

        // Process items without knowing progress
        for (int i = 0; i < items.size(); i++) {
            // Process the item - progress unknown
            processItem(items.get(i));

            // Periodically update message
            if ((i + 1) % 5 == 0) {
                counter.withMessage("Analyzing batch " + ((i + 1) / 5));
                Thread.sleep(100);
            }
        }

        // Switch to determinate mode for completion
        counter.withIndeterminate(false).withProgress(1.0).withMessage("Analysis complete");
        Thread.sleep(500);
        counter.stop();

        System.out.println("Indeterminate processing complete.");
    }

    /**
     * Demo with advanced animation techniques
     */
    private static void demoAnimatedCounter(List<String> items) throws InterruptedException {
        System.out.println("\nDEMO 3: Animated Counter with Effects");

        // Create an animated counter
        CounterProgressRunner counter = new CounterProgressRunner("Processing with effects", items.size(), 30);

        // Start with animation
        counter.start();

        // Process with animation effects
        String[] spinnerFrames = {"|", "/", "-", "\\"};
        String[] verbFrames = {"Processing", "Analyzing", "Calculating", "Computing"};

        for (int i = 0; i < items.size(); i++) {
            // Calculate progress
            double progress = (double) i / items.size();

            // Dynamic color based on progress percentage
            int red = 255 - (int)(255 * progress);
            int green = (int)(255 * progress);
            RGBColor dynamicColor = RGBColor.of(red, green, 100);

            // Update counter with dynamic formatting
            counter.withProgress(progress);


            // Animate the message with spinner
            for (int frame = 0; frame < 4; frame++) {
                // Get current animation frame
                String spinner = spinnerFrames[frame % spinnerFrames.length];
                String verb = verbFrames[frame % verbFrames.length];

                // Update with animated message
                counter.withMessage(spinner + " " + verb + " item " + (i + 1));

                // Short sleep for animation effect
                Thread.sleep(25);
            }

            // Process the actual item
            processItem(items.get(i));

            // Pause at checkpoints
            if ((i + 1) % 5 == 0) {
                counter.withMessage("✓ " + (i + 1) + " items completed");
                counter.setFormat("■".repeat((i + 1) / 5) + "□".repeat(5 - ((i + 1) / 5)) + " %d/%d");
                Thread.sleep(300);
            }
        }

        // Completion animation
        for (int i = 0; i < 5; i++) {
            counter.withProgress(1.0)
                    .withMessage((i % 2 == 0 ? "✨ " : "🎉 ") + "Processing complete!");
            counter.setCounterColor(i % 2 == 0 ? RGBColor.named("green") : RGBColor.named("cyan"));
            Thread.sleep(200);
        }

        counter.stop();

        System.out.println("Animated processing complete.");
    }
}