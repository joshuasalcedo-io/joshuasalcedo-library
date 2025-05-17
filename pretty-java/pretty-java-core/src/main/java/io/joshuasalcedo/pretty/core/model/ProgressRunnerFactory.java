package io.joshuasalcedo.pretty.core.model;

import io.joshuasalcedo.pretty.core.model.progress.AbstractProgressRunner;
import io.joshuasalcedo.pretty.core.model.progress.CounterProgressRunner;
import io.joshuasalcedo.pretty.core.model.progress.DotsAnimationRunner;
import io.joshuasalcedo.pretty.core.model.progress.TextAnimationRunner;
import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.theme.TerminalStyle;

/**
 * A factory for creating various types of progress runners.
 * <p>
 * This factory provides methods to create different kinds of progress indicators,
 * such as spinners, progress bars, counters, and text animations. All progress
 * indicators are implementations of the AbstractProgressRunner.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a spinner for an HTTP call
 * AbstractProgressRunner spinner = ProgressRunnerFactory.createSpinner("Fetching data from API...");
 * spinner.start();
 * 
 * // Perform the operation
 * // ...
 * 
 * // Stop the spinner
 * spinner.stop();
 * 
 * // Create a progress bar for a file operation
 * AbstractProgressRunner progressBar = ProgressRunnerFactory.createProgressBar("Uploading file", 40);
 * progressBar.start();
 * 
 * // Update progress as the operation progresses
 * progressBar.withProgress(0.5).withMessage("Uploading: 50% complete");
 * 
 * // Stop the progress bar when done
 * progressBar.stop();
 * </pre>
 */
public class ProgressRunnerFactory {
    
    /**
     * Creates a simple spinner progress indicator.
     * 
     * @param message The message to display
     * @return A spinner progress runner
     */
    public static AbstractProgressRunner createSpinner(String message) {
        return new SpinnerProgressRunner(message);
    }
    
    /**
     * Creates a spinner progress indicator with custom style.
     * 
     * @param message The message to display
     * @param style The terminal style to use
     * @return A spinner progress runner
     */
    public static AbstractProgressRunner createSpinner(String message, TerminalStyle style) {
        SpinnerProgressRunner spinner = new SpinnerProgressRunner(message);
        spinner.withStyle(style);
        return spinner;
    }
    
    /**
     * Creates a spinner progress indicator with custom spinner type.
     * 
     * @param message The message to display
     * @param spinnerType The spinner type to use
     * @return A spinner progress runner
     */
    public static AbstractProgressRunner createSpinner(String message, SpinnerProgressRunner.SpinnerType spinnerType) {
        SpinnerProgressRunner spinner = new SpinnerProgressRunner(message);
        spinner.setSpinnerType(spinnerType);
        return spinner;
    }
    
    /**
     * Creates a horizontal progress bar.
     * 
     * @param message The message to display
     * @param width The width of the progress bar in characters
     * @return A progress bar runner
     */
    public static AbstractProgressRunner createProgressBar(String message, int width) {
        return new ProgressBarRunner(message, width);
    }
    
    /**
     * Creates a horizontal progress bar with custom colors.
     * 
     * @param message The message to display
     * @param width The width of the progress bar in characters
     * @param progressColor The color for the completed portion
     * @param remainingColor The color for the remaining portion
     * @return A progress bar runner
     */
    public static AbstractProgressRunner createProgressBar(
            String message, int width, RGBColor progressColor, RGBColor remainingColor) {
        ProgressBarRunner progressBar = new ProgressBarRunner(message, width);
        progressBar.withProgressColor(progressColor);
        progressBar.withRemainingColor(remainingColor);
        return progressBar;
    }
    
    /**
     * Creates a counter progress indicator.
     * 
     * @param message The message to display
     * @param total The total count to reach
     * @return A counter progress runner
     */
    public static AbstractProgressRunner createCounter(String message, int total) {
        return new CounterProgressRunner(message, total);
    }
    
    /**
     * Creates a counter progress indicator with custom color.
     * 
     * @param message The message to display
     * @param total The total count to reach
     * @param counterColor The color for the counter
     * @return A counter progress runner
     */
    public static AbstractProgressRunner createCounter(String message, int total, RGBColor counterColor) {
        CounterProgressRunner counter = new CounterProgressRunner(message, total);
        counter.setCounterColor(counterColor);
        return counter;
    }
    
    /**
     * Creates a text animation progress indicator.
     * 
     * @param message The base message to display
     * @return A text animation progress runner
     */
    public static AbstractProgressRunner createTextAnimation(String message) {
        return new TextAnimationRunner(message);
    }
    
    /**
     * Creates a text animation progress indicator with custom animation type.
     * 
     * @param message The base message to display
     * @param animationType The animation type to use
     * @return A text animation progress runner
     */
    public static AbstractProgressRunner createTextAnimation(
            String message, TextAnimationRunner.AnimationType animationType) {
        TextAnimationRunner textAnimation = new TextAnimationRunner(message);
        textAnimation.setAnimationType(animationType);
        return textAnimation;
    }
    
    /**
     * Creates a dots animation progress indicator.
     * 
     * @param message The message to display
     * @return A dots animation progress runner
     */
    public static AbstractProgressRunner createDotsAnimation(String message) {
        return new DotsAnimationRunner(message);
    }
    
    /**
     * Creates a dots animation progress indicator with custom color.
     * 
     * @param message The message to display
     * @param dotsColor The color for the dots
     * @return A dots animation progress runner
     */
    public static AbstractProgressRunner createDotsAnimation(String message, RGBColor dotsColor) {
        DotsAnimationRunner dotsAnimation = new DotsAnimationRunner(message);
        dotsAnimation.setDotsColor(dotsColor);
        return dotsAnimation;
    }
    
    /**
     * Creates a task list progress indicator for multiple tasks.
     * 
     * @param message The main message to display
     * @param tasks The list of tasks to display
     * @return A task list progress runner
     */
    public static AbstractProgressRunner createTaskList(String message, String... tasks) {
        return new TaskListProgressRunner(message, tasks);
    }
}