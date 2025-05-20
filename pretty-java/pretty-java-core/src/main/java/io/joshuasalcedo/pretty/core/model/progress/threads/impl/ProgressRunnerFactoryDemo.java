//package io.joshuasalcedo.pretty.core.model.progress.threads.impl;
//
//import io.joshuasalcedo.pretty.core.model.progress.TextAnimationRunner;
//import io.joshuasalcedo.pretty.core.model.progress.threads.ProgressRunnerFactory;
//import io.joshuasalcedo.pretty.core.model.progress.threads.ThreadedProgressRunner;
//import io.joshuasalcedo.pretty.core.properties.RGBColor;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.concurrent.TimeUnit;
//import java.util.function.Function;
//import java.util.function.Supplier;
//
///**
// * A comprehensive demo showcasing the various capabilities of the ProgressRunnerFactory.
// * This demo illustrates different progress animations, task types, and result collection.
// */
//public class ProgressRunnerFactoryDemo {
//
//    private static final Random RANDOM = new Random();
//
//    public static void main(String[] args) {
//        // Display a header for the demo
//        System.out.println("=================================================================");
//        System.out.println("          ProgressRunnerFactory Comprehensive Demo");
//        System.out.println("=================================================================");
//        System.out.println();
//
//        try {
//            // Run each demo scenario in sequence
//            demoSimpleItemProcessing();
//            Thread.sleep(500);
//
//            demoColoredBar();
//            Thread.sleep(500);
//
//            demoParallelTasks();
//            Thread.sleep(500);
//
//            demoIndeterminateTask();
//            Thread.sleep(500);
//
//            demoTextAnimations();
//            Thread.sleep(500);
//
//            demoFunctionsWithResults();
//            Thread.sleep(500);
//
//            demoSuppliers();
//            Thread.sleep(500);
//
//            demoFilteringWithResults();
//            Thread.sleep(500);
//
//            demoMultipleFunctionsOnInputs();
//            Thread.sleep(500);
//
//            demoCustomColors();
//            Thread.sleep(500);
//
//            // Display a completion message
//            System.out.println("\n=================================================================");
//            System.out.println("                    Demo completed successfully");
//            System.out.println("=================================================================");
//
//        } catch (InterruptedException e) {
//            System.err.println("Demo interrupted: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Demonstrates processing a list of items with dots animation.
//     */
//    private static void demoSimpleItemProcessing() throws InterruptedException {
//        System.out.println("DEMO 1: Simple Item Processing with Dots Animation");
//        System.out.println("------------------------------------------------");
//
//        List<String> items = Arrays.asList(
//                "First item", "Second item", "Third item",
//                "Fourth item", "Fifth item"
//        );
//
//        ThreadedProgressRunner runner = ProgressRunnerFactory.forItems(items)
//                .withDots("Processing list items")
//                .execute(item -> {
//                    try {
//                        // Simulate processing time
//                        Thread.sleep(300 + RANDOM.nextInt(200));
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    }
//                });
//
//        // Wait for the processing to complete
//        runner.waitForCompletion();
//        System.out.println("\nDone processing items.\n");
//    }
//
//    /**
//     * Demonstrates using a colored progress bar.
//     */
//    private static void demoColoredBar() throws InterruptedException {
//        System.out.println("DEMO 2: Colored Progress Bar");
//        System.out.println("---------------------------");
//
//        // Create sequential tasks
//        Runnable[] tasks = new Runnable[10];
//        for (int i = 0; i < tasks.length; i++) {
//            final int taskId = i + 1;
//            tasks[i] = () -> {
//                try {
//                    // Simulate varying processing times
//                    Thread.sleep(200 + RANDOM.nextInt(300));
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            };
//        }
//
//        ThreadedProgressRunner runner = ProgressRunnerFactory.forMethods()
//                .withColoredBar("Executing sequential tasks")
//                .execute(tasks);
//
//        // Wait for the tasks to complete
//        runner.waitForCompletion();
//        System.out.println("\nSequential tasks completed.\n");
//    }
//
//    /**
//     * Demonstrates executing tasks in parallel with a counter.
//     */
//    private static void demoParallelTasks() throws InterruptedException {
//        System.out.println("DEMO 3: Parallel Tasks with Counter");
//        System.out.println("---------------------------------");
//
//        // Create tasks for parallel execution
//        List<Runnable> tasks = new ArrayList<>();
//        for (int i = 0; i < 8; i++) {
//            final int taskId = i + 1;
//            tasks.add(() -> {
//                try {
//                    // Parallel tasks with varying durations
//                    Thread.sleep(500 + RANDOM.nextInt(1000));
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            });
//        }
//
//        // Execute with 3 threads in parallel
//        ThreadedProgressRunner runner = ProgressRunnerFactory.forParallelTasks(3)
//                .withCounter("Running tasks in parallel (3 threads)")
//                .executeParallel(tasks);
//
//        // Wait for all parallel tasks to complete
//        runner.waitForCompletion();
//        System.out.println("\nParallel tasks completed.\n");
//    }
//
//    /**
//     * Demonstrates an indeterminate task with dots animation.
//     */
//    private static void demoIndeterminateTask() throws InterruptedException {
//        System.out.println("DEMO 4: Indeterminate Task");
//        System.out.println("-------------------------");
//
//        ThreadedProgressRunner runner = ProgressRunnerFactory.forIndeterminateTask(
//                "Performing background operation",
//                () -> {
//                    try {
//                        // Simulate a longer operation with unknown progress
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    }
//                });
//
//        // Start and wait for completion
//        runner.start();
//        runner.waitForCompletion();
//        System.out.println("\nIndeterminate task completed.\n");
//    }
//
//    /**
//     * Demonstrates different text animation types.
//     */
//    private static void demoTextAnimations() throws InterruptedException {
//        System.out.println("DEMO 5: Text Animations");
//        System.out.println("----------------------");
//
//        // Demo different text animation types
//        TextAnimationRunner.AnimationType[] animTypes = {
//                TextAnimationRunner.AnimationType.TYPING,
//                TextAnimationRunner.AnimationType.BOUNCING,
//                TextAnimationRunner.AnimationType.EXPANDING,
//                TextAnimationRunner.AnimationType.FADING
//        };
//
//        for (TextAnimationRunner.AnimationType animType : animTypes) {
//            System.out.println("Animation type: " + animType);
//
//            ThreadedProgressRunner runner = ProgressRunnerFactory.forAnimatedTask(
//                    "Processing with " + animType + " animation",
//                    animType,
//                    () -> {
//                        try {
//                            Thread.sleep(2500);
//                        } catch (InterruptedException e) {
//                            Thread.currentThread().interrupt();
//                        }
//                    });
//
//            runner.start();
//            runner.waitForCompletion();
//            System.out.println();
//        }
//    }
//
//    /**
//     * Demonstrates executing functions with result collection.
//     */
//    private static void demoFunctionsWithResults() throws InterruptedException {
//        System.out.println("DEMO 6: Functions with Result Collection");
//        System.out.println("--------------------------------------");
//
//        // Create a list of integers to process
//        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//
//        // Process the numbers and collect squared results
//        ProgressRunnerFactory.ThreadedProgressRunnerWithResults<Integer> runner = ProgressRunnerFactory.forFunctions(numbers)
//                .withColoredBar("Calculating squares")
//                .execute(num -> {
//                    try {
//                        Thread.sleep(300);
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    }
//                    return num * num;
//                });
//
//        // Wait for processing to complete
//        runner.waitForCompletion();
//
//        // Get and display the results
//        List<Integer> squaredNumbers = runner.getResults();
//        System.out.println("\nSquared results: " + squaredNumbers + "\n");
//    }
//
//    /**
//     * Demonstrates using suppliers to generate values.
//     */
//    private static void demoSuppliers() throws InterruptedException {
//        System.out.println("DEMO 7: Suppliers for Value Generation");
//        System.out.println("------------------------------------");
//
//        // Create a supplier that generates random strings
//        Supplier<String> randomStringSupplier = () -> {
//            try {
//                Thread.sleep(400);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//
//            char[] chars = new char[5];
//            for (int i = 0; i < chars.length; i++) {
//                chars[i] = (char)('A' + RANDOM.nextInt(26));
//            }
//            return new String(chars);
//        };
//
//        // Generate 6 random strings with progress tracking
//        ThreadedProgressRunnerWithResults<String> runner = ProgressRunnerFactory.forSuppliers(6)
//                .withCounter("Generating random strings")
//                .execute(randomStringSupplier);
//
//        // Wait for generation to complete
//        runner.waitForCompletion();
//
//        // Get and display the generated strings
//        List<String> generatedStrings = runner.getResults();
//        System.out.println("\nGenerated strings: " + generatedStrings + "\n");
//    }
//
//    /**
//     * Demonstrates filtering items with result collection.
//     */
//    private static void demoFilteringWithResults() throws InterruptedException {
//        System.out.println("DEMO 8: Filtering with Results");
//        System.out.println("-----------------------------");
//
//        // Create a list of strings to filter
//        List<String> words = Arrays.asList(
//                "apple", "banana", "cherry", "date", "elderberry",
//                "fig", "grape", "honeydew", "kiwi", "lemon"
//        );
//
//        // Filter words starting with letters in the first half of the alphabet
//        ThreadedProgressRunnerWithResults<String> runner = ProgressRunnerFactory.forItems(words)
//                .withCounter("Filtering words")
//                .filter(word -> {
//                    try {
//                        Thread.sleep(300);
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    }
//
//                    char firstChar = word.charAt(0);
//                    return firstChar >= 'a' && firstChar <= 'm';
//                });
//
//        // Wait for filtering to complete
//        runner.waitForCompletion();
//
//        // Get and display the filtered words
//        List<String> filteredWords = runner.getResults();
//        System.out.println("\nWords starting with a-m: " + filteredWords + "\n");
//    }
//
//    /**
//     * Demonstrates applying multiple functions to the same inputs.
//     */
//    private static void demoMultipleFunctionsOnInputs() throws InterruptedException {
//        System.out.println("DEMO 9: Multiple Functions on Same Inputs");
//        System.out.println("-----------------------------------------");
//
//        // Create a list of integers to process
//        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
//
//        // Define three functions to apply to each number
//        Function<Integer, Integer> square = n -> n * n;
//        Function<Integer, Integer> double_ = n -> n * 2;
//        Function<Integer, Integer> addTen = n -> n + 10;
//
//        // Execute all three functions on each input
//        ThreadedProgressRunnerWithResults<Integer> runner = ProgressRunnerFactory.forFunctions(numbers)
//                .withColoredBar("Applying multiple transformations")
//                .executeAll(square, double_, addTen);
//
//        // Wait for processing to complete
//        runner.waitForCompletion();
//
//        // Get the flat list of all results
//        List<Integer> allResults = runner.getResults();
//
//        // Get the structured map of input -> results
//        Map<Integer, List<Integer>> resultsMap = runner.getResultsMap();
//
//        System.out.println("\nAll results: " + allResults);
//        System.out.println("Results by input:");
//        resultsMap.forEach((input, results) -> {
//            System.out.println("  " + input + " -> " + results);
//        });
//        System.out.println();
//    }
//
//    /**
//     * Demonstrates customizing colors for the progress display.
//     */
//    private static void demoCustomColors() throws InterruptedException {
//        System.out.println("DEMO 10: Custom Colors");
//        System.out.println("---------------------");
//
//        // Create some tasks to execute
//        Runnable[] tasks = new Runnable[8];
//        for (int i = 0; i < tasks.length; i++) {
//            tasks[i] = () -> {
//                try {
//                    Thread.sleep(400 + RANDOM.nextInt(200));
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            };
//        }
//
//        // Create a progress runner with custom colors
//        ThreadedProgressRunner runner = ProgressRunnerFactory.forMethods()
//                .withMessageColor(new RGBColor(50, 200, 50))  // Green message
//                .withProgressColor(new RGBColor(100, 100, 255))  // Blue progress
//                .withRemainingColor(new RGBColor(50, 50, 50))  // Dark gray remaining
//                .withPercentageColor(new RGBColor(255, 100, 100))  // Red percentage
//                .withColoredBar("Executing with custom colors")
//                .execute(tasks);
//
//        // Wait for tasks to complete
//        runner.waitForCompletion();
//        System.out.println("\nColored tasks completed.\n");
//    }
//}