package io.joshuasalcedo.pretty.core.model.progress.threads;

import io.joshuasalcedo.pretty.core.model.progress.AbstractProgressRunner;
import io.joshuasalcedo.pretty.core.model.progress.ColoredProgressBar;
import io.joshuasalcedo.pretty.core.model.progress.CounterProgressRunner;
import io.joshuasalcedo.pretty.core.model.progress.DotsAnimationRunner;
import io.joshuasalcedo.pretty.core.model.progress.TextAnimationRunner;
import io.joshuasalcedo.pretty.core.properties.RGBColor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A static factory for creating and managing different types of ThreadedProgressRunners.
 * This factory provides various methods to create progress runners with different
 * animation styles and execution strategies for methods, consumers, functions, and suppliers.
 *
 * <p>Usage example:</p>
 * <pre>
 * // Create a progress runner for processing a list of items
 * List<String> items = Arrays.asList("Item1", "Item2", "Item3");
 * ProgressRunnerFactory.forItems(items)
 *     .withCounter("Processing items")
 *     .execute(item -> {
 *         // Process each item
 *         System.out.println("Processing: " + item);
 *         Thread.sleep(500);
 *     });
 *
 * // Execute multiple methods with progress tracking
 * ProgressRunnerFactory.forMethods()
 *     .withColoredBar("Executing methods")
 *     .execute(
 *         this::step1,
 *         this::step2,
 *         this::step3
 *     );
 * </pre>
 */
public class ProgressRunnerFactory {

    // Prevent instantiation
    private ProgressRunnerFactory() {}

    /**
     * Creates a builder for a progress runner that executes a list of methods.
     *
     * @return A builder for methods execution
     */
    public static MethodsProgressBuilder forMethods() {
        return new MethodsProgressBuilder();
    }

    /**
     * Creates a builder for a progress runner that processes a list of items.
     *
     * @param <T> The type of items to process
     * @param items The items to process
     * @return A builder for item processing
     */
    public static <T> ItemsProgressBuilder<T> forItems(List<T> items) {
        return new ItemsProgressBuilder<>(items);
    }

    /**
     * Creates a builder for a progress runner that processes items from suppliers.
     *
     * @param <T> The type of items to produce
     * @param count The number of items to produce
     * @return A builder for supplier processing
     */
    public static <T> SupplierProgressBuilder<T> forSuppliers(int count) {
        return new SupplierProgressBuilder<>(count);
    }

    /**
     * Creates a builder for a progress runner that executes a list of functions.
     *
     * @param <I> The input type for functions
     * @param <O> The output type for functions
     * @param inputs The inputs to the functions
     * @return A builder for function execution
     */
    public static <I, O> FunctionProgressBuilder<I, O> forFunctions(List<I> inputs) {
        return new FunctionProgressBuilder<>(inputs);
    }

    /**
     * Creates a builder for a progress runner that executes parallel tasks.
     *
     * @param taskCount The number of tasks to execute in parallel
     * @return A builder for parallel task execution
     */
    public static ParallelTasksBuilder forParallelTasks(int taskCount) {
        return new ParallelTasksBuilder(taskCount);
    }

    /**
     * Creates a progress runner for an indeterminate task where the progress
     * cannot be measured.
     *
     * @param message The message to display
     * @param task The task to execute
     * @return The threaded progress runner
     */
    public static ThreadedProgressRunner forIndeterminateTask(String message, Runnable task) {
        DotsAnimationRunner dots = new DotsAnimationRunner(message);
        dots.withIndeterminate(true);

        return new ThreadedProgressRunner(dots, task);
    }

    /**
     * Creates a progress runner for a long-running task with text animation.
     *
     * @param message The message to display
     * @param animationType The type of text animation
     * @param task The task to execute
     * @return The threaded progress runner
     */
    public static ThreadedProgressRunner forAnimatedTask(
            String message,
            TextAnimationRunner.AnimationType animationType,
            Runnable task) {

        TextAnimationRunner textAnim = new TextAnimationRunner(message);
        textAnim.setAnimationType(animationType);

        return new ThreadedProgressRunner(textAnim, task);
    }

    //-------------------------------------------------------------------------
    // Builder classes for different types of progress runners
    //-------------------------------------------------------------------------

    /**
     * Base builder class with common functionality for all progress runner builders.
     */
    public static abstract class AbstractProgressBuilder<T extends AbstractProgressBuilder<T>> {
        protected String message = "Processing...";
        protected int updateIntervalMs = 100;
        protected boolean indeterminate = false;
        protected RGBColor messageColor = null;
        protected RGBColor progressColor = null;
        protected RGBColor remainingColor = null;
        protected RGBColor percentageColor = null;

        /**
         * Sets the message for the progress runner.
         *
         * @param message The message to display
         * @return This builder instance
         */
        @SuppressWarnings("unchecked")
        public T withMessage(String message) {
            this.message = message;
            return (T) this;
        }

        /**
         * Sets the update interval for the progress runner.
         *
         * @param updateIntervalMs The update interval in milliseconds
         * @return This builder instance
         */
        @SuppressWarnings("unchecked")
        public T withUpdateInterval(int updateIntervalMs) {
            this.updateIntervalMs = updateIntervalMs;
            return (T) this;
        }

        /**
         * Sets the progress runner to indeterminate mode.
         *
         * @param indeterminate Whether to use indeterminate mode
         * @return This builder instance
         */
        @SuppressWarnings("unchecked")
        public T withIndeterminate(boolean indeterminate) {
            this.indeterminate = indeterminate;
            return (T) this;
        }

        /**
         * Sets the message color for the progress runner.
         *
         * @param color The color for the message
         * @return This builder instance
         */
        @SuppressWarnings("unchecked")
        public T withMessageColor(RGBColor color) {
            this.messageColor = color;
            return (T) this;
        }

        /**
         * Sets the progress color for the progress runner.
         *
         * @param color The color for the progress
         * @return This builder instance
         */
        @SuppressWarnings("unchecked")
        public T withProgressColor(RGBColor color) {
            this.progressColor = color;
            return (T) this;
        }

        /**
         * Sets the remaining color for the progress runner.
         *
         * @param color The color for the remaining part
         * @return This builder instance
         */
        @SuppressWarnings("unchecked")
        public T withRemainingColor(RGBColor color) {
            this.remainingColor = color;
            return (T) this;
        }

        /**
         * Sets the percentage color for the progress runner.
         *
         * @param color The color for the percentage
         * @return This builder instance
         */
        @SuppressWarnings("unchecked")
        public T withPercentageColor(RGBColor color) {
            this.percentageColor = color;
            return (T) this;
        }

        /**
         * Sets all colors for the progress runner.
         *
         * @param messageColor The color for the message
         * @param progressColor The color for the progress
         * @param remainingColor The color for the remaining part
         * @param percentageColor The color for the percentage
         * @return This builder instance
         */
        @SuppressWarnings("unchecked")
        public T withColors(
                RGBColor messageColor,
                RGBColor progressColor,
                RGBColor remainingColor,
                RGBColor percentageColor) {
            this.messageColor = messageColor;
            this.progressColor = progressColor;
            this.remainingColor = remainingColor;
            this.percentageColor = percentageColor;
            return (T) this;
        }

        /**
         * Creates a builder with a dots animation.
         *
         * @return A builder for a dots animation
         */
        public abstract AnimationTypeBuilder withDots();

        /**
         * Creates a builder with a counter display.
         *
         * @return A builder for a counter display
         */
        public abstract AnimationTypeBuilder withCounter();

        /**
         * Creates a builder with a colored progress bar.
         *
         * @return A builder for a colored progress bar
         */
        public abstract AnimationTypeBuilder withColoredBar();

        /**
         * Creates a builder with a text animation.
         *
         * @param animationType The type of text animation
         * @return A builder for a text animation
         */
        public abstract AnimationTypeBuilder withTextAnimation(TextAnimationRunner.AnimationType animationType);

        /**
         * Applies common configuration to a progress runner.
         *
         * @param runner The progress runner to configure
         */
        protected void applyCommonConfig(AbstractProgressRunner runner) {
            runner.withIndeterminate(indeterminate);

            if (messageColor != null) {
                runner.withMessageColor(messageColor);
            }

            if (progressColor != null) {
                runner.withProgressColor(progressColor);
            }

            if (remainingColor != null) {
                runner.withRemainingColor(remainingColor);
            }

            if (percentageColor != null) {
                runner.withPercentageColor(percentageColor);
            }
        }
    }

    /**
     * Builder for progress runners that execute a list of methods.
     */
    public static class MethodsProgressBuilder extends AbstractProgressBuilder<MethodsProgressBuilder> {

        @Override
        public MethodsAnimationBuilder withDots() {
            return new MethodsAnimationBuilder(AnimationType.DOTS, message, updateIntervalMs, this);
        }

        @Override
        public MethodsAnimationBuilder withCounter() {
            return new MethodsAnimationBuilder(AnimationType.COUNTER, message, updateIntervalMs, this);
        }

        @Override
        public MethodsAnimationBuilder withColoredBar() {
            return new MethodsAnimationBuilder(AnimationType.BAR, message, updateIntervalMs, this);
        }

        @Override
        public MethodsAnimationBuilder withTextAnimation(TextAnimationRunner.AnimationType animationType) {
            return new MethodsAnimationBuilder(AnimationType.TEXT, message, updateIntervalMs, this)
                    .setTextAnimationType(animationType);
        }

        /**
         * Convenience method for creating a dots animation with the current message.
         *
         * @param message The message to display
         * @return A builder for a dots animation
         */
        public MethodsAnimationBuilder withDots(String message) {
            return withMessage(message).withDots();
        }

        /**
         * Convenience method for creating a counter with the current message.
         *
         * @param message The message to display
         * @return A builder for a counter
         */
        public MethodsAnimationBuilder withCounter(String message) {
            return withMessage(message).withCounter();
        }

        /**
         * Convenience method for creating a colored bar with the current message.
         *
         * @param message The message to display
         * @return A builder for a colored bar
         */
        public MethodsAnimationBuilder withColoredBar(String message) {
            return withMessage(message).withColoredBar();
        }

        /**
         * Convenience method for creating a text animation with the current message.
         *
         * @param message The message to display
         * @param animationType The type of text animation
         * @return A builder for a text animation
         */
        public MethodsAnimationBuilder withTextAnimation(String message, TextAnimationRunner.AnimationType animationType) {
            return withMessage(message).withTextAnimation(animationType);
        }
    }

    /**
     * Builder for progress runners that process a list of items.
     *
     * @param <T> The type of items to process
     */
    public static class ItemsProgressBuilder<T> extends AbstractProgressBuilder<ItemsProgressBuilder<T>> {
        private final List<T> items;

        /**
         * Creates a builder for processing a list of items.
         *
         * @param items The items to process
         */
        public ItemsProgressBuilder(List<T> items) {
            this.items = items;
        }

        @Override
        public ItemsAnimationBuilder<T> withDots() {
            return new ItemsAnimationBuilder<>(AnimationType.DOTS, message, updateIntervalMs, items, this);
        }

        @Override
        public ItemsAnimationBuilder<T> withCounter() {
            return new ItemsAnimationBuilder<>(AnimationType.COUNTER, message, updateIntervalMs, items, this);
        }

        @Override
        public ItemsAnimationBuilder<T> withColoredBar() {
            return new ItemsAnimationBuilder<>(AnimationType.BAR, message, updateIntervalMs, items, this);
        }

        @Override
        public ItemsAnimationBuilder<T> withTextAnimation(TextAnimationRunner.AnimationType animationType) {
            return new ItemsAnimationBuilder<>(AnimationType.TEXT, message, updateIntervalMs, items, this)
                    .setTextAnimationType(animationType);
        }

        /**
         * Convenience method for creating a dots animation with the current message.
         *
         * @param message The message to display
         * @return A builder for a dots animation
         */
        public ItemsAnimationBuilder<T> withDots(String message) {
            return withMessage(message).withDots();
        }

        /**
         * Convenience method for creating a counter with the current message.
         *
         * @param message The message to display
         * @return A builder for a counter
         */
        public ItemsAnimationBuilder<T> withCounter(String message) {
            return withMessage(message).withCounter();
        }

        /**
         * Convenience method for creating a colored bar with the current message.
         *
         * @param message The message to display
         * @return A builder for a colored bar
         */
        public ItemsAnimationBuilder<T> withColoredBar(String message) {
            return withMessage(message).withColoredBar();
        }

        /**
         * Convenience method for creating a text animation with the current message.
         *
         * @param message The message to display
         * @param animationType The type of text animation
         * @return A builder for a text animation
         */
        public ItemsAnimationBuilder<T> withTextAnimation(String message, TextAnimationRunner.AnimationType animationType) {
            return withMessage(message).withTextAnimation(animationType);
        }
    }

    /**
     * Builder for progress runners that produce items from suppliers.
     *
     * @param <T> The type of items to produce
     */
    public static class SupplierProgressBuilder<T> extends AbstractProgressBuilder<SupplierProgressBuilder<T>> {
        private final int count;

        /**
         * Creates a builder for producing items from suppliers.
         *
         * @param count The number of items to produce
         */
        public SupplierProgressBuilder(int count) {
            this.count = count;
        }

        @Override
        public SupplierAnimationBuilder<T> withDots() {
            return new SupplierAnimationBuilder<>(AnimationType.DOTS, message, updateIntervalMs, count, this);
        }

        @Override
        public SupplierAnimationBuilder<T> withCounter() {
            return new SupplierAnimationBuilder<>(AnimationType.COUNTER, message, updateIntervalMs, count, this);
        }

        @Override
        public SupplierAnimationBuilder<T> withColoredBar() {
            return new SupplierAnimationBuilder<>(AnimationType.BAR, message, updateIntervalMs, count, this);
        }

        @Override
        public SupplierAnimationBuilder<T> withTextAnimation(TextAnimationRunner.AnimationType animationType) {
            return new SupplierAnimationBuilder<>(AnimationType.TEXT, message, updateIntervalMs, count, this)
                    .setTextAnimationType(animationType);
        }

        /**
         * Convenience method for creating a dots animation with the current message.
         *
         * @param message The message to display
         * @return A builder for a dots animation
         */
        public SupplierAnimationBuilder<T> withDots(String message) {
            return withMessage(message).withDots();
        }

        /**
         * Convenience method for creating a counter with the current message.
         *
         * @param message The message to display
         * @return A builder for a counter
         */
        public SupplierAnimationBuilder<T> withCounter(String message) {
            return withMessage(message).withCounter();
        }

        /**
         * Convenience method for creating a colored bar with the current message.
         *
         * @param message The message to display
         * @return A builder for a colored bar
         */
        public SupplierAnimationBuilder<T> withColoredBar(String message) {
            return withMessage(message).withColoredBar();
        }

        /**
         * Convenience method for creating a text animation with the current message.
         *
         * @param message The message to display
         * @param animationType The type of text animation
         * @return A builder for a text animation
         */
        public SupplierAnimationBuilder<T> withTextAnimation(String message, TextAnimationRunner.AnimationType animationType) {
            return withMessage(message).withTextAnimation(animationType);
        }
    }

    /**
     * Builder for progress runners that execute a list of functions.
     *
     * @param <I> The input type for functions
     * @param <O> The output type for functions
     */
    public static class FunctionProgressBuilder<I, O> extends AbstractProgressBuilder<FunctionProgressBuilder<I, O>> {
        private final List<I> inputs;

        /**
         * Creates a builder for executing a list of functions.
         *
         * @param inputs The inputs to the functions
         */
        public FunctionProgressBuilder(List<I> inputs) {
            this.inputs = inputs;
        }

        @Override
        public FunctionAnimationBuilder<I, O> withDots() {
            return new FunctionAnimationBuilder<>(AnimationType.DOTS, message, updateIntervalMs, inputs, this);
        }

        @Override
        public FunctionAnimationBuilder<I, O> withCounter() {
            return new FunctionAnimationBuilder<>(AnimationType.COUNTER, message, updateIntervalMs, inputs, this);
        }

        @Override
        public FunctionAnimationBuilder<I, O> withColoredBar() {
            return new FunctionAnimationBuilder<>(AnimationType.BAR, message, updateIntervalMs, inputs, this);
        }

        @Override
        public FunctionAnimationBuilder<I, O> withTextAnimation(TextAnimationRunner.AnimationType animationType) {
            return new FunctionAnimationBuilder<>(AnimationType.TEXT, message, updateIntervalMs, inputs, this)
                    .setTextAnimationType(animationType);
        }

        /**
         * Convenience method for creating a dots animation with the current message.
         *
         * @param message The message to display
         * @return A builder for a dots animation
         */
        public FunctionAnimationBuilder<I, O> withDots(String message) {
            return withMessage(message).withDots();
        }

        /**
         * Convenience method for creating a counter with the current message.
         *
         * @param message The message to display
         * @return A builder for a counter
         */
        public FunctionAnimationBuilder<I, O> withCounter(String message) {
            return withMessage(message).withCounter();
        }

        /**
         * Convenience method for creating a colored bar with the current message.
         *
         * @param message The message to display
         * @return A builder for a colored bar
         */
        public FunctionAnimationBuilder<I, O> withColoredBar(String message) {
            return withMessage(message).withColoredBar();
        }

        /**
         * Convenience method for creating a text animation with the current message.
         *
         * @param message The message to display
         * @param animationType The type of text animation
         * @return A builder for a text animation
         */
        public FunctionAnimationBuilder<I, O> withTextAnimation(String message, TextAnimationRunner.AnimationType animationType) {
            return withMessage(message).withTextAnimation(animationType);
        }
    }

    /**
     * Builder for progress runners that execute parallel tasks.
     */
    public static class ParallelTasksBuilder extends AbstractProgressBuilder<ParallelTasksBuilder> {
        private final int taskCount;

        /**
         * Creates a builder for executing parallel tasks.
         *
         * @param taskCount The number of tasks to execute in parallel
         */
        public ParallelTasksBuilder(int taskCount) {
            this.taskCount = taskCount;
        }

        @Override
        public ParallelAnimationBuilder withDots() {
            return new ParallelAnimationBuilder(AnimationType.DOTS, message, updateIntervalMs, taskCount, this);
        }

        @Override
        public ParallelAnimationBuilder withCounter() {
            return new ParallelAnimationBuilder(AnimationType.COUNTER, message, updateIntervalMs, taskCount, this);
        }

        @Override
        public ParallelAnimationBuilder withColoredBar() {
            return new ParallelAnimationBuilder(AnimationType.BAR, message, updateIntervalMs, taskCount, this);
        }

        @Override
        public ParallelAnimationBuilder withTextAnimation(TextAnimationRunner.AnimationType animationType) {
            return new ParallelAnimationBuilder(AnimationType.TEXT, message, updateIntervalMs, taskCount, this)
                    .setTextAnimationType(animationType);
        }

        /**
         * Convenience method for creating a dots animation with the current message.
         *
         * @param message The message to display
         * @return A builder for a dots animation
         */
        public ParallelAnimationBuilder withDots(String message) {
            return withMessage(message).withDots();
        }

        /**
         * Convenience method for creating a counter with the current message.
         *
         * @param message The message to display
         * @return A builder for a counter
         */
        public ParallelAnimationBuilder withCounter(String message) {
            return withMessage(message).withCounter();
        }

        /**
         * Convenience method for creating a colored bar with the current message.
         *
         * @param message The message to display
         * @return A builder for a colored bar
         */
        public ParallelAnimationBuilder withColoredBar(String message) {
            return withMessage(message).withColoredBar();
        }

        /**
         * Convenience method for creating a text animation with the current message.
         *
         * @param message The message to display
         * @param animationType The type of text animation
         * @return A builder for a text animation
         */
        public ParallelAnimationBuilder withTextAnimation(String message, TextAnimationRunner.AnimationType animationType) {
            return withMessage(message).withTextAnimation(animationType);
        }
    }

    //-------------------------------------------------------------------------
    // Animation type builders
    //-------------------------------------------------------------------------

    /**
     * Enum for supported animation types.
     */
    public enum AnimationType {
        DOTS,
        COUNTER,
        BAR,
        TEXT
    }

    /**
     * Base interface for animation type builders.
     */
    public interface AnimationTypeBuilder {
        /**
         * Creates a threaded progress runner for the configured animation type.
         *
         * @param task The task to execute
         * @return The threaded progress runner
         */
        ThreadedProgressRunner execute(Runnable task);
    }

    /**
     * Base class for animation type builders.
     */
    public static abstract class AbstractAnimationBuilder implements AnimationTypeBuilder {
        protected final AnimationType animationType;
        protected final String message;
        protected final int updateIntervalMs;
        protected TextAnimationRunner.AnimationType textAnimationType = TextAnimationRunner.AnimationType.TYPING;
        protected AbstractProgressBuilder<?> parent;

        /**
         * Creates an animation builder with the specified parameters.
         *
         * @param animationType The type of animation
         * @param message The message to display
         * @param updateIntervalMs The update interval in milliseconds
         * @param parent The parent builder
         */
        public AbstractAnimationBuilder(
                AnimationType animationType,
                String message,
                int updateIntervalMs,
                AbstractProgressBuilder<?> parent) {
            this.animationType = animationType;
            this.message = message;
            this.updateIntervalMs = updateIntervalMs;
            this.parent = parent;
        }

        /**
         * Sets the text animation type.
         *
         * @param textAnimationType The type of text animation
         * @return This builder instance
         */
        @SuppressWarnings("unchecked")
        public <T extends AbstractAnimationBuilder> T setTextAnimationType(TextAnimationRunner.AnimationType textAnimationType) {
            this.textAnimationType = textAnimationType;
            return (T) this;
        }

        /**
         * Creates the appropriate progress runner based on the animation type.
         *
         * @param total The total number of items or steps
         * @return The progress runner
         */
        protected AbstractProgressRunner createProgressRunner(int total) {
            AbstractProgressRunner runner;

            switch (animationType) {
                case DOTS:
                    runner = new DotsAnimationRunner(message, updateIntervalMs);
                    break;
                case COUNTER:
                    runner = new CounterProgressRunner(message, total, updateIntervalMs);
                    break;
                case BAR:
                    runner = new ColoredProgressBar(message, 30, updateIntervalMs);
                    break;
                case TEXT:
                    TextAnimationRunner textRunner = new TextAnimationRunner(message, updateIntervalMs);
                    textRunner.setAnimationType(textAnimationType);
                    runner = textRunner;
                    break;
                default:
                    runner = new DotsAnimationRunner(message, updateIntervalMs);
            }

            parent.applyCommonConfig(runner);
            return runner;
        }

        /**
         * Executes a single task with the configured progress runner.
         *
         * @param task The task to execute
         * @return The threaded progress runner
         */
        @Override
        public ThreadedProgressRunner execute(Runnable task) {
            AbstractProgressRunner runner = createProgressRunner(1);
            ThreadedProgressRunner threadedRunner = new ThreadedProgressRunner(runner, task);
            threadedRunner.start();
            return threadedRunner;
        }
    }

    /**
     * Animation builder for executing methods.
     */
    public static class MethodsAnimationBuilder extends AbstractAnimationBuilder {

        /**
         * Creates a methods animation builder.
         *
         * @param animationType The type of animation
         * @param message The message to display
         * @param updateIntervalMs The update interval in milliseconds
         * @param parent The parent builder
         */
        public MethodsAnimationBuilder(
                AnimationType animationType,
                String message,
                int updateIntervalMs,
                AbstractProgressBuilder<?> parent) {
            super(animationType, message, updateIntervalMs, parent);
        }

        /**
         * Executes a list of methods with progress tracking.
         *
         * @param methods The methods to execute
         * @return The threaded progress runner
         */
        public ThreadedProgressRunner execute(Method... methods) {
            int total = methods.length;
            AbstractProgressRunner runner = createProgressRunner(total);

            Runnable task = () -> {
                try {
                    for (int i = 0; i < methods.length; i++) {
                        Method method = methods[i];
                        String methodName = method.getName();

                        // Update progress
                        double progress = (double) i / total;
                        runner.withProgress(progress);
                        runner.withMessage("Executing " + methodName + "...");

                        // Invoke the method
                        try {
                            method.invoke(null);
                        } catch (Exception e) {
                            System.err.println("Error executing method " + methodName + ": " + e.getMessage());
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Methods executed successfully");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunner threadedRunner = new ThreadedProgressRunner(runner, task);
            threadedRunner.start();
            return threadedRunner;
        }

        /**
         * Executes a list of runnables with progress tracking.
         *
         * @param runnables The runnables to execute
         * @return The threaded progress runner
         */
        public ThreadedProgressRunner execute(Runnable... runnables) {
            int total = runnables.length;
            AbstractProgressRunner runner = createProgressRunner(total);

            Runnable task = () -> {
                try {
                    for (int i = 0; i < runnables.length; i++) {
                        Runnable runnable = runnables[i];

                        // Update progress
                        double progress = (double) i / total;
                        runner.withProgress(progress);
                        runner.withMessage("Executing task " + (i + 1) + " of " + total + "...");

                        // Execute the runnable
                        try {
                            runnable.run();
                        } catch (Exception e) {
                            System.err.println("Error executing task " + (i + 1) + ": " + e.getMessage());
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Tasks executed successfully");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunner threadedRunner = new ThreadedProgressRunner(runner, task);
            threadedRunner.start();
            return threadedRunner;
        }

        /**
         * Executes a list of consumers with progress tracking.
         *
         * @param consumers The consumers to execute
         * @return The threaded progress runner
         */
        @SafeVarargs
        public final ThreadedProgressRunner execute(Consumer<Void>... consumers) {
            int total = consumers.length;
            AbstractProgressRunner runner = createProgressRunner(total);

            Runnable task = () -> {
                try {
                    for (int i = 0; i < consumers.length; i++) {
                        Consumer<Void> consumer = consumers[i];

                        // Update progress
                        double progress = (double) i / total;
                        runner.withProgress(progress);
                        runner.withMessage("Executing consumer " + (i + 1) + " of " + total + "...");

                        // Execute the consumer
                        try {
                            consumer.accept(null);
                        } catch (Exception e) {
                            System.err.println("Error executing consumer " + (i + 1) + ": " + e.getMessage());
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Consumers executed successfully");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunner threadedRunner = new ThreadedProgressRunner(runner, task);
            threadedRunner.start();
            return threadedRunner;
        }
    }

    /**
     * Animation builder for processing items.
     *
     * @param <T> The type of items to process
     */
    public static class ItemsAnimationBuilder<T> extends AbstractAnimationBuilder {
        private final List<T> items;

        /**
         * Creates an items animation builder.
         *
         * @param animationType The type of animation
         * @param message The message to display
         * @param updateIntervalMs The update interval in milliseconds
         * @param items The items to process
         * @param parent The parent builder
         */
        public ItemsAnimationBuilder(
                AnimationType animationType,
                String message,
                int updateIntervalMs,
                List<T> items,
                AbstractProgressBuilder<?> parent) {
            super(animationType, message, updateIntervalMs, parent);
            this.items = items;
        }

        /**
         * Executes a consumer for each item with progress tracking.
         *
         * @param consumer The consumer to execute for each item
         * @return The threaded progress runner
         */
        public ThreadedProgressRunner execute(Consumer<T> consumer) {
            int total = items.size();
            AbstractProgressRunner runner = createProgressRunner(total);

            Runnable task = () -> {
                try {
                    for (int i = 0; i < items.size(); i++) {
                        T item = items.get(i);

                        // Update progress
                        double progress = (double) i / total;
                        runner.withProgress(progress);
                        runner.withMessage("Processing item " + (i + 1) + " of " + total + "...");

                        // Process the item
                        try {
                            consumer.accept(item);
                        } catch (Exception e) {
                            System.err.println("Error processing item " + (i + 1) + ": " + e.getMessage());
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Items processed successfully");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunner threadedRunner = new ThreadedProgressRunner(runner, task);
            threadedRunner.start();
            return threadedRunner;
        }

        /**
         * Executes a function for each item with progress tracking and collects the results.
         *
         * @param <R> The type of results
         * @param function The function to execute for each item
         * @return The threaded progress runner
         */
        public <R> ThreadedProgressRunnerWithResults<R> execute(Function<T, R> function) {
            int total = items.size();
            AbstractProgressRunner runner = createProgressRunner(total);
            List<R> results = new ArrayList<>(total);

            Runnable task = () -> {
                try {
                    for (int i = 0; i < items.size(); i++) {
                        T item = items.get(i);

                        // Update progress
                        double progress = (double) i / total;
                        runner.withProgress(progress);
                        runner.withMessage("Processing item " + (i + 1) + " of " + total + "...");

                        // Process the item and collect the result
                        try {
                            R result = function.apply(item);
                            synchronized (results) {
                                results.add(result);
                            }
                        } catch (Exception e) {
                            System.err.println("Error processing item " + (i + 1) + ": " + e.getMessage());
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Items processed successfully");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunnerWithResults<R> threadedRunner =
                    new ThreadedProgressRunnerWithResults<>(runner, task, results);
            threadedRunner.start();
            return threadedRunner;
        }

        /**
         * Executes a predicate for each item with progress tracking and collects the matching items.
         *
         * @param predicate The predicate to test each item
         * @return The threaded progress runner
         */
        public ThreadedProgressRunnerWithResults<T> filter(Predicate<T> predicate) {
            int total = items.size();
            AbstractProgressRunner runner = createProgressRunner(total);
            List<T> filteredItems = new ArrayList<>();

            Runnable task = () -> {
                try {
                    for (int i = 0; i < items.size(); i++) {
                        T item = items.get(i);

                        // Update progress
                        double progress = (double) i / total;
                        runner.withProgress(progress);
                        runner.withMessage("Filtering item " + (i + 1) + " of " + total + "...");

                        // Test the item and collect if it matches
                        try {
                            if (predicate.test(item)) {
                                synchronized (filteredItems) {
                                    filteredItems.add(item);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Error filtering item " + (i + 1) + ": " + e.getMessage());
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Filtering complete. Found " + filteredItems.size() + " matching items.");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunnerWithResults<T> threadedRunner =
                    new ThreadedProgressRunnerWithResults<>(runner, task, filteredItems);
            threadedRunner.start();
            return threadedRunner;
        }
    }

    /**
     * Animation builder for producing items from suppliers.
     *
     * @param <T> The type of items to produce
     */
    public static class SupplierAnimationBuilder<T> extends AbstractAnimationBuilder {
        private final int count;

        /**
         * Creates a supplier animation builder.
         *
         * @param animationType The type of animation
         * @param message The message to display
         * @param updateIntervalMs The update interval in milliseconds
         * @param count The number of items to produce
         * @param parent The parent builder
         */
        public SupplierAnimationBuilder(
                AnimationType animationType,
                String message,
                int updateIntervalMs,
                int count,
                AbstractProgressBuilder<?> parent) {
            super(animationType, message, updateIntervalMs, parent);
            this.count = count;
        }

        /**
         * Executes a supplier the specified number of times with progress tracking.
         *
         * @param supplier The supplier to execute
         * @return The threaded progress runner
         */
        public ThreadedProgressRunnerWithResults<T> execute(Supplier<T> supplier) {
            AbstractProgressRunner runner = createProgressRunner(count);
            List<T> results = new ArrayList<>(count);

            Runnable task = () -> {
                try {
                    for (int i = 0; i < count; i++) {
                        // Update progress
                        double progress = (double) i / count;
                        runner.withProgress(progress);
                        runner.withMessage("Generating item " + (i + 1) + " of " + count + "...");

                        // Get the next item
                        try {
                            T item = supplier.get();
                            synchronized (results) {
                                results.add(item);
                            }
                        } catch (Exception e) {
                            System.err.println("Error generating item " + (i + 1) + ": " + e.getMessage());
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Item generation complete");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunnerWithResults<T> threadedRunner =
                    new ThreadedProgressRunnerWithResults<>(runner, task, results);
            threadedRunner.start();
            return threadedRunner;
        }

        /**
         * Executes a list of suppliers and collects the results.
         *
         * @param suppliers The suppliers to execute
         * @return The threaded progress runner
         */
        @SafeVarargs
        public final ThreadedProgressRunnerWithResults<T> execute(Supplier<T>... suppliers) {
            int total = suppliers.length;
            AbstractProgressRunner runner = createProgressRunner(total);
            List<T> results = new ArrayList<>(total);

            Runnable task = () -> {
                try {
                    for (int i = 0; i < suppliers.length; i++) {
                        Supplier<T> supplier = suppliers[i];

                        // Update progress
                        double progress = (double) i / total;
                        runner.withProgress(progress);
                        runner.withMessage("Executing supplier " + (i + 1) + " of " + total + "...");

                        // Execute the supplier and collect the result
                        try {
                            T result = supplier.get();
                            synchronized (results) {
                                results.add(result);
                            }
                        } catch (Exception e) {
                            System.err.println("Error executing supplier " + (i + 1) + ": " + e.getMessage());
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Suppliers executed successfully");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunnerWithResults<T> threadedRunner =
                    new ThreadedProgressRunnerWithResults<>(runner, task, results);
            threadedRunner.start();
            return threadedRunner;
        }

        /**
         * Executes a list of suppliers provided as a list and collects the results.
         *
         * @param suppliers The list of suppliers to execute
         * @return The threaded progress runner
         */
        public ThreadedProgressRunnerWithResults<T> execute(List<Supplier<T>> suppliers) {
            int total = suppliers.size();
            AbstractProgressRunner runner = createProgressRunner(total);
            List<T> results = new ArrayList<>(total);

            Runnable task = () -> {
                try {
                    for (int i = 0; i < suppliers.size(); i++) {
                        Supplier<T> supplier = suppliers.get(i);

                        // Update progress
                        double progress = (double) i / total;
                        runner.withProgress(progress);
                        runner.withMessage("Executing supplier " + (i + 1) + " of " + total + "...");

                        // Execute the supplier and collect the result
                        try {
                            T result = supplier.get();
                            synchronized (results) {
                                results.add(result);
                            }
                        } catch (Exception e) {
                            System.err.println("Error executing supplier " + (i + 1) + ": " + e.getMessage());
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Suppliers executed successfully");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunnerWithResults<T> threadedRunner =
                    new ThreadedProgressRunnerWithResults<>(runner, task, results);
            threadedRunner.start();
            return threadedRunner;
        }
    }

    /**
     * Animation builder for executing functions with inputs.
     *
     * @param <I> The input type for functions
     * @param <O> The output type for functions
     */
    public static class FunctionAnimationBuilder<I, O> extends AbstractAnimationBuilder {
        private final List<I> inputs;

        /**
         * Creates a function animation builder.
         *
         * @param animationType The type of animation
         * @param message The message to display
         * @param updateIntervalMs The update interval in milliseconds
         * @param inputs The inputs to the functions
         * @param parent The parent builder
         */
        public FunctionAnimationBuilder(
                AnimationType animationType,
                String message,
                int updateIntervalMs,
                List<I> inputs,
                AbstractProgressBuilder<?> parent) {
            super(animationType, message, updateIntervalMs, parent);
            this.inputs = inputs;
        }

        /**
         * Executes a function for each input with progress tracking.
         *
         * @param function The function to execute
         * @return The threaded progress runner
         */
        public ThreadedProgressRunnerWithResults<O> execute(Function<I, O> function) {
            int total = inputs.size();
            AbstractProgressRunner runner = createProgressRunner(total);
            List<O> results = new ArrayList<>(total);

            Runnable task = () -> {
                try {
                    for (int i = 0; i < inputs.size(); i++) {
                        I input = inputs.get(i);

                        // Update progress
                        double progress = (double) i / total;
                        runner.withProgress(progress);
                        runner.withMessage("Processing input " + (i + 1) + " of " + total + "...");

                        // Process the input
                        try {
                            O result = function.apply(input);
                            synchronized (results) {
                                results.add(result);
                            }
                        } catch (Exception e) {
                            System.err.println("Error processing input " + (i + 1) + ": " + e.getMessage());
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Processing complete");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunnerWithResults<O> threadedRunner =
                    new ThreadedProgressRunnerWithResults<>(runner, task, results);
            threadedRunner.start();
            return threadedRunner;
        }

        /**
         * Executes a list of functions with the same input with progress tracking.
         *
         * @param functions The functions to execute
         * @return The threaded progress runner
         */
        @SafeVarargs
        public final ThreadedProgressRunnerWithResults<O> executeAll(Function<I, O>... functions) {
            int total = functions.length * inputs.size();
            AbstractProgressRunner runner = createProgressRunner(total);
            Map<I, List<O>> resultsMap = new ConcurrentHashMap<>();
            List<O> flatResults = new ArrayList<>(total);

            Runnable task = () -> {
                try {
                    int counter = 0;
                    for (int i = 0; i < inputs.size(); i++) {
                        I input = inputs.get(i);
                        List<O> inputResults = new ArrayList<>(functions.length);
                        resultsMap.put(input, inputResults);

                        for (int j = 0; j < functions.length; j++) {
                            Function<I, O> function = functions[j];

                            // Update progress
                            double progress = (double) counter++ / total;
                            runner.withProgress(progress);
                            runner.withMessage("Processing input " + (i + 1) + " with function " + (j + 1) + "...");

                            // Process the input with the function
                            try {
                                O result = function.apply(input);
                                inputResults.add(result);
                                flatResults.add(result);
                            } catch (Exception e) {
                                System.err.println("Error processing input " + (i + 1) +
                                        " with function " + (j + 1) + ": " + e.getMessage());
                            }
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Processing complete");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunnerWithResults<O> threadedRunner =
                    new ThreadedProgressRunnerWithResults<>(runner, task, flatResults);
            threadedRunner.setResultsMap(resultsMap);
            threadedRunner.start();
            return threadedRunner;
        }

        /**
         * Executes a list of functions provided as a list with progress tracking.
         *
         * @param functions The list of functions to execute
         * @return The threaded progress runner
         */
        public ThreadedProgressRunnerWithResults<O> executeAll(List<Function<I, O>> functions) {
            int total = functions.size() * inputs.size();
            AbstractProgressRunner runner = createProgressRunner(total);
            Map<I, List<O>> resultsMap = new ConcurrentHashMap<>();
            List<O> flatResults = new ArrayList<>(total);

            Runnable task = () -> {
                try {
                    int counter = 0;
                    for (int i = 0; i < inputs.size(); i++) {
                        I input = inputs.get(i);
                        List<O> inputResults = new ArrayList<>(functions.size());
                        resultsMap.put(input, inputResults);

                        for (int j = 0; j < functions.size(); j++) {
                            Function<I, O> function = functions.get(j);

                            // Update progress
                            double progress = (double) counter++ / total;
                            runner.withProgress(progress);
                            runner.withMessage("Processing input " + (i + 1) + " with function " + (j + 1) + "...");

                            // Process the input with the function
                            try {
                                O result = function.apply(input);
                                inputResults.add(result);
                                flatResults.add(result);
                            } catch (Exception e) {
                                System.err.println("Error processing input " + (i + 1) +
                                        " with function " + (j + 1) + ": " + e.getMessage());
                            }
                        }
                    }

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("Processing complete");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            };

            ThreadedProgressRunnerWithResults<O> threadedRunner =
                    new ThreadedProgressRunnerWithResults<>(runner, task, flatResults);
            threadedRunner.setResultsMap(resultsMap);
            threadedRunner.start();
            return threadedRunner;
        }
    }

    /**
     * Animation builder for executing parallel tasks.
     */
    public static class ParallelAnimationBuilder extends AbstractAnimationBuilder {
        private final int taskCount;

        /**
         * Creates a parallel animation builder.
         *
         * @param animationType The type of animation
         * @param message The message to display
         * @param updateIntervalMs The update interval in milliseconds
         * @param taskCount The number of tasks to execute in parallel
         * @param parent The parent builder
         */
        public ParallelAnimationBuilder(
                AnimationType animationType,
                String message,
                int updateIntervalMs,
                int taskCount,
                AbstractProgressBuilder<?> parent) {
            super(animationType, message, updateIntervalMs, parent);
            this.taskCount = taskCount;
        }

        /**
         * Executes a list of runnables in parallel with progress tracking.
         *
         * @param runnables The runnables to execute
         * @return The threaded progress runner
         */
        public ThreadedProgressRunner executeParallel(Runnable... runnables) {
            int total = runnables.length;
            AbstractProgressRunner runner = createProgressRunner(total);
            ExecutorService executor = Executors.newFixedThreadPool(
                    Math.min(taskCount, runnables.length),
                    r -> {
                        Thread t = new Thread(r);
                        t.setDaemon(true);
                        return t;
                    }
            );
            AtomicInteger completedCount = new AtomicInteger(0);
            CountDownLatch latch = new CountDownLatch(runnables.length);

            Runnable task = () -> {
                try {
                    // Submit all tasks to the executor
                    for (int i = 0; i < runnables.length; i++) {
                        final int taskIndex = i;
                        Runnable runnable = runnables[i];

                        executor.submit(() -> {
                            try {
                                runner.withMessage("Executing task " + (taskIndex + 1) + " of " + total + "...");
                                runnable.run();
                            } catch (Exception e) {
                                System.err.println("Error executing task " + (taskIndex + 1) + ": " + e.getMessage());
                            } finally {
                                // Update progress
                                int completed = completedCount.incrementAndGet();
                                double progress = (double) completed / total;
                                runner.withProgress(progress);
                                latch.countDown();
                            }
                        });
                    }

                    // Wait for all tasks to complete
                    latch.await();

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("All tasks completed");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                } finally {
                    executor.shutdown();
                }
            };

            ThreadedProgressRunner threadedRunner = new ThreadedProgressRunner(runner, task);
            threadedRunner.start();
            return threadedRunner;
        }

        /**
         * Executes a list of runnables provided as a list in parallel with progress tracking.
         *
         * @param runnables The list of runnables to execute
         * @return The threaded progress runner
         */
        public ThreadedProgressRunner executeParallel(List<Runnable> runnables) {
            int total = runnables.size();
            AbstractProgressRunner runner = createProgressRunner(total);
            ExecutorService executor = Executors.newFixedThreadPool(
                    Math.min(taskCount, runnables.size()),
                    r -> {
                        Thread t = new Thread(r);
                        t.setDaemon(true);
                        return t;
                    }
            );
            AtomicInteger completedCount = new AtomicInteger(0);
            CountDownLatch latch = new CountDownLatch(runnables.size());

            Runnable task = () -> {
                try {
                    // Submit all tasks to the executor
                    for (int i = 0; i < runnables.size(); i++) {
                        final int taskIndex = i;
                        Runnable runnable = runnables.get(i);

                        executor.submit(() -> {
                            try {
                                runner.withMessage("Executing task " + (taskIndex + 1) + " of " + total + "...");
                                runnable.run();
                            } catch (Exception e) {
                                System.err.println("Error executing task " + (taskIndex + 1) + ": " + e.getMessage());
                            } finally {
                                // Update progress
                                int completed = completedCount.incrementAndGet();
                                double progress = (double) completed / total;
                                runner.withProgress(progress);
                                latch.countDown();
                            }
                        });
                    }

                    // Wait for all tasks to complete
                    latch.await();

                    // Complete progress
                    runner.withProgress(1.0);
                    runner.withMessage("All tasks completed");
                } catch (Exception e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                } finally {
                    executor.shutdown();
                }
            };

            ThreadedProgressRunner threadedRunner = new ThreadedProgressRunner(runner, task);
            threadedRunner.start();
            return threadedRunner;
        }
    }

    //-------------------------------------------------------------------------
    // Result collection classes
    //-------------------------------------------------------------------------

    /**
     * A ThreadedProgressRunner that also collects results from the execution.
     *
     * @param <T> The type of results
     */
    public static class ThreadedProgressRunnerWithResults<T> extends ThreadedProgressRunner {
        private final List<T> results;
        private Map<?, List<T>> resultsMap;

        /**
         * Creates a ThreadedProgressRunnerWithResults.
         *
         * @param progressRunner The progress runner
         * @param task The task to execute
         * @param results The list to collect results in
         */
        public ThreadedProgressRunnerWithResults(
                AbstractProgressRunner progressRunner,
                Runnable task,
                List<T> results) {
            super(progressRunner, task);
            this.results = results;
        }

        /**
         * Gets the collected results.
         *
         * @return The results
         */
        public List<T> getResults() {
            return results;
        }

        /**
         * Sets the results map for more complex result collection.
         *
         * @param resultsMap The map to collect results in
         */
        public void setResultsMap(Map<?, List<T>> resultsMap) {
            this.resultsMap = resultsMap;
        }

        /**
         * Gets the results map.
         *
         * @param <K> The key type
         * @return The results map
         */
        @SuppressWarnings("unchecked")
        public <K> Map<K, List<T>> getResultsMap() {
            return (Map<K, List<T>>) resultsMap;
        }
    }
}
