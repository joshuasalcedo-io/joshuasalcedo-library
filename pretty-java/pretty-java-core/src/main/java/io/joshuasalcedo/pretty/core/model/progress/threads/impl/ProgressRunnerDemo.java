package io.joshuasalcedo.pretty.core.model.progress.threads.impl;

import io.joshuasalcedo.pretty.core.model.progress.*;
import io.joshuasalcedo.pretty.core.model.progress.threads.*;

import java.util.Arrays;
import java.util.List;


public class ProgressRunnerDemo {

    public static void main(String[] args) {
        System.out.println("\n===== Progress Runner Factory Demo =====\n");

        try {
            // Demo 1: Basic usage - process a list of items
            demoBasicUsage();

            System.out.println("\nAll demos completed successfully!");

        } catch (Exception e) {
            System.err.println("Error in demo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void demoBasicUsage() throws InterruptedException {
        System.out.println("\n===== Demo 1: Basic Usage =====");

        // Create a list of items to process
        List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4", "Item 5");

        // Process items with dots animation
        System.out.println("\n> Process items with dots animation:");
        ThreadedProgressRunner runner = ProgressRunnerFactory.forItems(items)
                .withDots("Processing items")
                .execute(item -> {
                    try {
                        // Simulate processing
                        System.out.println("  Processing: " + item);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

        // Wait for completion manually instead of using waitForCompletion()
        while (!runner.isComplete()) {
            Thread.sleep(100);
        }

        // Demo 2: Execute multiple tasks with a colored bar
        System.out.println("\n> Execute multiple tasks with a colored bar:");
        ThreadedProgressRunner methodRunner = ProgressRunnerFactory.forMethods()
                .withColoredBar("Executing tasks")
                .execute(
                        () -> simulateTask("Task 1", 800),
                        () -> simulateTask("Task 2", 600),
                        () -> simulateTask("Task 3", 700)
                );

        // Wait for completion manually
        while (!methodRunner.isComplete()) {
            Thread.sleep(100);
        }

        // Demo 3: Indeterminate task
        System.out.println("\n> Indeterminate task example:");
        ThreadedProgressRunner indeterminateRunner = ProgressRunnerFactory
                .forIndeterminateTask("Searching...", () -> {
                    try {
                        System.out.println("  Performing search operation...");
                        Thread.sleep(3000);
                        System.out.println("  Search completed");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

        // Wait for completion manually
        while (!indeterminateRunner.isComplete()) {
            Thread.sleep(100);
        }

        // Demo 4: Text animation
        System.out.println("\n> Text animation example:");
        ThreadedProgressRunner textRunner = ProgressRunnerFactory
                .forAnimatedTask("Processing data", TextAnimationRunner.AnimationType.TYPING, () -> {
                    try {
                        System.out.println("  Performing data analysis...");
                        Thread.sleep(3000);
                        System.out.println("  Analysis completed");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

        // Wait for completion manually
        while (!textRunner.isComplete()) {
            Thread.sleep(100);
        }
    }

    private static void simulateTask(String taskName, long durationMs) {
        try {
            System.out.println("  Executing: " + taskName);
            Thread.sleep(durationMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}