package io.joshuasalcedo.pretty.core.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * An interface for interactive and animated terminal components that require
 * continuous updates or threading.
 * <p>
 * This interface defines methods for terminal UI elements that change over time
 * such as progress bars, spinners, and other dynamic components. Implementations
 * of this interface should properly manage threads and resources.
 * </p>
 *
 * @author Joshua Salcedo
 * @version 1.0
 */
public interface InteractivePrintable {
    
    /**
     * Gets the thread pool used for managing animated components.
     * Implementations should provide a suitable thread pool for running animations.
     *
     * @return the ExecutorService used for animations
     */
    ExecutorService getAnimationExecutor();
    
    /**
     * Starts an animated progress bar that updates until completion or cancellation.
     *
     * @param width Width of the progress bar in characters
     * @param style Style of the progress bar
     * @param message Optional message to display alongside the bar
     * @param progressSupplier A supplier function that returns the current progress (0.0-1.0)
     * @param onComplete Action to run when progress reaches 1.0
     * @return A ProgressHandle to control the progress bar
     */
    ProgressHandle startProgressBar(int width, ProgressStyle style, String message, 
                                   java.util.function.Supplier<Double> progressSupplier,
                                   Runnable onComplete);

    /**
     * Starts a simple progress bar with default style.
     *
     * @param progressSupplier A supplier function that returns the current progress (0.0-1.0)
     * @return A ProgressHandle to control the progress bar
     */
    ProgressHandle startProgressBar(java.util.function.Supplier<Double> progressSupplier);
    
    /**
     * Starts an animated indeterminate progress bar (activity indicator).
     *
     * @param message The message to display alongside the bar
     * @param style The style of indicator to use
     * @return A ProgressHandle to control the progress bar
     */
    ProgressHandle startIndeterminateProgress(String message, ProgressStyle style);
    
    /**
     * Starts an animated spinner that can be updated to show progress.
     *
     * @param message The message to display alongside the spinner
     * @param style The style of spinner to use
     * @return A SpinnerHandle to control the spinner
     */
    SpinnerHandle startSpinner(String message, SpinnerStyle style);
    
    /**
     * Starts a simple spinner with default style.
     *
     * @param message The message to display alongside the spinner
     * @return A SpinnerHandle to control the spinner
     */
    SpinnerHandle startSpinner(String message);
    
    /**
     * Starts an animated countdown timer.
     *
     * @param seconds The number of seconds to count down from
     * @param message The message to display alongside the timer
     * @param onComplete Action to run when countdown completes
     * @return A TimerHandle to control the countdown
     */
    TimerHandle startCountdown(int seconds, String message, Runnable onComplete);
    
    /**
     * Starts a task with live output that can be updated as the task progresses.
     *
     * @param initialMessage The initial status message for the task
     * @param statusConsumer A consumer that receives a status updater function
     * @return A TaskHandle to interact with the running task
     */
    TaskHandle startTask(String initialMessage, Consumer<StatusUpdater> statusConsumer);
    
    /**
     * Cleanly shuts down any running animations and releases resources.
     * This should be called when animations are no longer needed.
     */
    void shutdownAnimations();
    
    // Nested interfaces for control handles
    
    /**
     * Handle for controlling a progress bar.
     */
    interface ProgressHandle {
        /**
         * Updates the progress value manually.
         *
         * @param progress The new progress value (0.0-1.0)
         * @return this handle for method chaining
         */
        ProgressHandle update(double progress);
        
        /**
         * Updates the message displayed with the progress bar.
         *
         * @param message The new message to display
         * @return this handle for method chaining
         */
        ProgressHandle message(String message);
        
        /**
         * Completes the progress bar successfully.
         *
         * @param finalMessage Optional message to display on completion (can be null)
         */
        void complete(String finalMessage);
        
        /**
         * Cancels the progress bar and marks it as failed.
         *
         * @param errorMessage Optional error message to display (can be null)
         */
        void fail(String errorMessage);
    }
    
    /**
     * Handle for controlling an animated spinner.
     */
    interface SpinnerHandle {
        /**
         * Updates the message displayed with the spinner.
         *
         * @param message The new message to display
         * @return this handle for method chaining
         */
        SpinnerHandle update(String message);
        
        /**
         * Marks the spinner as successful and stops the animation.
         *
         * @param message The success message to display
         */
        void success(String message);
        
        /**
         * Marks the spinner as failed and stops the animation.
         *
         * @param message The error message to display
         */
        void error(String message);
        
        /**
         * Updates the spinner with a warning message but continues spinning.
         *
         * @param message The warning message to display
         * @return this handle for method chaining
         */
        SpinnerHandle warning(String message);
        
        /**
         * Updates the spinner with an info message but continues spinning.
         *
         * @param message The info message to display
         * @return this handle for method chaining
         */
        SpinnerHandle info(String message);
        
        /**
         * Stops the spinner animation without any status indicator.
         */
        void stop();
    }
    
    /**
     * Handle for controlling a countdown timer.
     */
    interface TimerHandle {
        /**
         * Pauses the countdown.
         *
         * @return this handle for method chaining
         */
        TimerHandle pause();
        
        /**
         * Resumes the countdown if paused.
         *
         * @return this handle for method chaining
         */
        TimerHandle resume();
        
        /**
         * Adds additional seconds to the countdown.
         *
         * @param seconds Number of seconds to add
         * @return this handle for method chaining
         */
        TimerHandle addTime(int seconds);
        
        /**
         * Updates the message displayed with the timer.
         *
         * @param message The new message to display
         * @return this handle for method chaining
         */
        TimerHandle message(String message);
        
        /**
         * Cancels the countdown immediately.
         *
         * @param finalMessage Optional message to display (can be null)
         */
        void cancel(String finalMessage);
    }
    
    /**
     * Handle for controlling a live-updating task.
     */
    interface TaskHandle {
        /**
         * Checks if the task is still running.
         *
         * @return true if the task is running, false otherwise
         */
        boolean isRunning();
        
        /**
         * Requests cancellation of the task.
         *
         * @return true if cancellation was requested, false if already completed
         */
        boolean cancel();
        
        /**
         * Waits for the task to complete.
         *
         * @return this handle for method chaining
         * @throws InterruptedException if the wait is interrupted
         */
        TaskHandle await() throws InterruptedException;
    }
    
    /**
     * Interface for updating task status.
     */
    interface StatusUpdater {
        /**
         * Updates the status message of a running task.
         *
         * @param message The new status message
         * @return this updater for method chaining
         */
        StatusUpdater status(String message);
        
        /**
         * Updates the progress percentage of a running task.
         *
         * @param progress The progress value (0.0-1.0)
         * @return this updater for method chaining
         */
        StatusUpdater progress(double progress);
        
        /**
         * Adds a detail line below the main status.
         *
         * @param detail The detail message to add
         * @return this updater for method chaining
         */
        StatusUpdater detail(String detail);
        
        /**
         * Checks if cancellation has been requested.
         *
         * @return true if cancellation was requested
         */
        boolean isCancellationRequested();
    }
    
    // Enum definitions specific to interactive elements
    
    /**
     * Styles for progress bars.
     */
    enum ProgressStyle {
        BAR, BLOCK, CIRCLE, GRADIENT, INDETERMINATE, MINIMAL
    }
    
    /**
     * Styles for spinners.
     */
    enum SpinnerStyle {
        DOTS, LINE, BOUNCE, CIRCLE, CLOCK, FLIP, PULSE
    }
}