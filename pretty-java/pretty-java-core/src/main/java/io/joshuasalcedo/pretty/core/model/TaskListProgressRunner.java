package io.joshuasalcedo.pretty.core.model;

import io.joshuasalcedo.pretty.core.model.progress.AbstractProgressRunner;
import io.joshuasalcedo.pretty.core.properties.RGBColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A progress runner that displays a list of tasks with their progress status.
 * <p>
 * This class displays a set of tasks and their status (pending, in progress, complete, or failed),
 * useful for operations that involve multiple distinct steps.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a task list with a main message and tasks
 * TaskListProgressRunner tasks = new TaskListProgressRunner("Deployment steps:",
 *     "Building application",
 *     "Running tests",
 *     "Creating deployment package",
 *     "Uploading to server"
 * );
 *
 * // Start the task list display
 * tasks.start();
 *
 * // Update task status as progress occurs
 * tasks.markTaskInProgress(0);
 * // ... perform task 0 ...
 * tasks.markTaskComplete(0);
 *
 * tasks.markTaskInProgress(1);
 * // ... perform task 1 ...
 * tasks.markTaskComplete(1);
 *
 * // Stop the task list display when all tasks are complete
 * tasks.stop();
 * </pre>
 */
public class TaskListProgressRunner extends AbstractProgressRunner {

    /**
     * Enum defining possible task statuses.
     */
    public enum TaskStatus {
        /**
         * Task has not started yet
         */
        PENDING("○", RGBColor.of(150, 150, 150)),

        /**
         * Task is currently in progress
         */
        IN_PROGRESS("◔", RGBColor.of(0, 191, 255)),

        /**
         * Task has completed successfully
         */
        COMPLETE("●", RGBColor.of(50, 205, 50)),

        /**
         * Task has failed
         */
        FAILED("✗", RGBColor.of(255, 69, 0));

        private final String symbol;
        private final RGBColor color;

        TaskStatus(String symbol, RGBColor color) {
            this.symbol = symbol;
            this.color = color;
        }

        /**
         * Gets the symbol for this task status.
         *
         * @return The status symbol
         */
        public String getSymbol() {
            return symbol;
        }

        /**
         * Gets the color for this task status.
         *
         * @return The status color
         */
        public RGBColor getColor() {
            return color;
        }
    }

    // List of tasks
    private final List<String> tasks;

    // Status of each task
    private final List<TaskStatus> taskStatuses;

    // Index of the currently active task (for animation)
    private int activeTaskIndex = -1;

    // Whether to use compact mode (all tasks on one line)
    private boolean compactMode = false;

    // Custom symbols for task statuses
    private String pendingSymbol = TaskStatus.PENDING.getSymbol();
    private String inProgressSymbol = TaskStatus.IN_PROGRESS.getSymbol();
    private String completeSymbol = TaskStatus.COMPLETE.getSymbol();
    private String failedSymbol = TaskStatus.FAILED.getSymbol();

    // Track the number of lines in the previous render for proper clearing
    private int previousRenderLines = 0;

    // Animation step counter - specific to this implementation
    private int animationStep = 0;

    // Custom executor and task for multi-line animation
    private ScheduledExecutorService customScheduler;
    private ScheduledFuture<?> customAnimationTask;

    /**
     * Creates a task list progress runner with the given message and tasks.
     *
     * @param message The main message to display
     * @param tasks The list of tasks
     */
    public TaskListProgressRunner(String message, String... tasks) {
        super(message, 100);

        this.tasks = new ArrayList<>();
        this.taskStatuses = new ArrayList<>();

        if (tasks != null) {
            for (String task : tasks) {
                if (task != null && !task.isEmpty()) {
                    this.tasks.add(task);
                    this.taskStatuses.add(TaskStatus.PENDING);
                }
            }
        }

        withIndeterminate(true);
    }

    /**
     * Adds a task to the list.
     *
     * @param taskDescription The task description
     * @return This instance for method chaining
     */
    public TaskListProgressRunner addTask(String taskDescription) {
        if (taskDescription != null && !taskDescription.isEmpty()) {
            tasks.add(taskDescription);
            taskStatuses.add(TaskStatus.PENDING);
        }
        return this;
    }

    /**
     * Sets the status of a task.
     *
     * @param taskIndex The index of the task
     * @param status The new status for the task
     * @return This instance for method chaining
     */
    public TaskListProgressRunner setTaskStatus(int taskIndex, TaskStatus status) {
        if (taskIndex >= 0 && taskIndex < taskStatuses.size() && status != null) {
            taskStatuses.set(taskIndex, status);

            // Update active task index if necessary
            if (status == TaskStatus.IN_PROGRESS) {
                activeTaskIndex = taskIndex;
            } else if (activeTaskIndex == taskIndex) {
                // Find next in-progress task if this one is no longer in progress
                updateActiveTaskIndex();
            }

            // Update overall progress based on completed tasks
            updateProgress();
        }
        return this;
    }

    /**
     * Marks a task as in progress.
     *
     * @param taskIndex The index of the task
     * @return This instance for method chaining
     */
    public TaskListProgressRunner markTaskInProgress(int taskIndex) {
        return setTaskStatus(taskIndex, TaskStatus.IN_PROGRESS);
    }

    /**
     * Marks a task as complete.
     *
     * @param taskIndex The index of the task
     * @return This instance for method chaining
     */
    public TaskListProgressRunner markTaskComplete(int taskIndex) {
        return setTaskStatus(taskIndex, TaskStatus.COMPLETE);
    }

    /**
     * Marks a task as failed.
     *
     * @param taskIndex The index of the task
     * @return This instance for method chaining
     */
    public TaskListProgressRunner markTaskFailed(int taskIndex) {
        return setTaskStatus(taskIndex, TaskStatus.FAILED);
    }

    /**
     * Sets whether to use compact mode (all tasks on one line).
     *
     * @param compactMode Whether to use compact mode
     * @return This instance for method chaining
     */
    public TaskListProgressRunner setCompactMode(boolean compactMode) {
        this.compactMode = compactMode;
        return this;
    }

    /**
     * Sets custom symbols for task statuses.
     *
     * @param pendingSymbol The symbol for pending tasks
     * @param inProgressSymbol The symbol for in-progress tasks
     * @param completeSymbol The symbol for completed tasks
     * @param failedSymbol The symbol for failed tasks
     * @return This instance for method chaining
     */
    public TaskListProgressRunner setCustomSymbols(
            String pendingSymbol,
            String inProgressSymbol,
            String completeSymbol,
            String failedSymbol) {

        if (pendingSymbol != null && !pendingSymbol.isEmpty()) {
            this.pendingSymbol = pendingSymbol;
        }
        if (inProgressSymbol != null && !inProgressSymbol.isEmpty()) {
            this.inProgressSymbol = inProgressSymbol;
        }
        if (completeSymbol != null && !completeSymbol.isEmpty()) {
            this.completeSymbol = completeSymbol;
        }
        if (failedSymbol != null && !failedSymbol.isEmpty()) {
            this.failedSymbol = failedSymbol;
        }

        return this;
    }

    /**
     * Uses ASCII symbols instead of Unicode for task statuses.
     *
     * @return This instance for method chaining
     */
    public TaskListProgressRunner useAsciiSymbols() {
        this.pendingSymbol = "-";
        this.inProgressSymbol = ">";
        this.completeSymbol = "+";
        this.failedSymbol = "x";
        return this;
    }

    /**
     * Gets the count of completed tasks.
     *
     * @return The number of completed tasks
     */
    public int getCompletedTaskCount() {
        int count = 0;
        for (TaskStatus status : taskStatuses) {
            if (status == TaskStatus.COMPLETE) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the count of failed tasks.
     *
     * @return The number of failed tasks
     */
    public int getFailedTaskCount() {
        int count = 0;
        for (TaskStatus status : taskStatuses) {
            if (status == TaskStatus.FAILED) {
                count++;
            }
        }
        return count;
    }

    /**
     * Updates the active task index based on in-progress tasks.
     */
    private void updateActiveTaskIndex() {
        activeTaskIndex = -1;
        for (int i = 0; i < taskStatuses.size(); i++) {
            if (taskStatuses.get(i) == TaskStatus.IN_PROGRESS) {
                activeTaskIndex = i;
                break;
            }
        }
    }

    /**
     * Updates the overall progress based on completed tasks.
     */
    private void updateProgress() {
        if (tasks.isEmpty()) {
            return;
        }

        int completed = getCompletedTaskCount();
        withProgress((double) completed / tasks.size());
    }


    /**
     * Custom animation update for multi-line task list.
     */
    private void customAnimationUpdate() {
        // Only proceed if we're still running
        if (!isRunning()) {
            return;
        }

        // Get current state (thread-safe)
        String currentMessage = getMessage();
        double currentProgress = getProgress();
        boolean isIndeterminate = isIndeterminate();

        // Build the progress display
        StringBuilder display = new StringBuilder();

        // Add ANSI escape sequences to clear previous render
        if (previousRenderLines > 0) {
            // Move cursor up to the start of the previous render
            display.append("\u001B[").append(previousRenderLines).append("A");
            // Clear from cursor to the end of the screen
            display.append("\u001B[J");
        }

        // Let the render method build the display
        if (isIndeterminate) {
            render(display, currentMessage, -1, animationStep);
        } else {
            render(display, currentMessage, currentProgress, animationStep);
        }

        // Count the number of lines for next clear operation
        previousRenderLines = 0;
        for (int i = 0; i < display.length(); i++) {
            if (display.charAt(i) == '\n') {
                previousRenderLines++;
            }
        }
        // Add 1 for the last line (which may not end with a newline)
        previousRenderLines++;

        // Print the current state
        System.out.print(display);

        // Increment animation step
        animationStep++;
    }

    /**
     * Start the task list animation with a custom animation handler.
     * This overrides the parent start method to use a custom animation function.
     *
     * @return This instance for method chaining
     */
    @Override
    public TaskListProgressRunner start() {
        // Don't use the parent's animation logic - implement our own

        // Only start if not already running
        if (!isRunning()) {
            // Set running state (uses parent's method)
            super.withRunner(true);

            // Create a new scheduler
            customScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });

            // Schedule the custom animation task to run at fixed intervals
            customAnimationTask = customScheduler.scheduleAtFixedRate(
                    this::customAnimationUpdate,
                    0,
                    100, // Use fixed 100ms interval for task list animations
                    TimeUnit.MILLISECONDS
            );
        }

        return this;
    }

    /**
     * Stop the task list animation and clean up resources.
     * This overrides the parent stop method to handle custom animation cleanup.
     *
     * @return This instance for method chaining
     */
    @Override
    public TaskListProgressRunner stop() {
        // Only stop if running
        if (isRunning()) {
            // Set running state (uses parent's method)
            super.withRunner(false);

            try {
                if (customAnimationTask != null) {
                    customAnimationTask.cancel(false);
                }
                if (customScheduler != null) {
                    customScheduler.shutdown();
                    // Wait for tasks to complete
                    if (!customScheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                        customScheduler.shutdownNow();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                customScheduler.shutdownNow();
            }

            // Print a newline to ensure next output starts on a fresh line
            System.out.println();
        }

        return this;
    }

    @Override
    protected void render(StringBuilder display, String message, double progress, int animationStep) {
        // Display the main message if available
        if (message != null && !message.isEmpty()) {
            display.append(applyColor(message, getMessageColor())).append("\n");
        }

        if (compactMode) {
            renderCompact(display, animationStep);
        } else {
            renderDetailed(display, animationStep);
        }
    }

    /**
     * Renders tasks in compact mode (all on one line).
     */
    private void renderCompact(StringBuilder display, int animationStep) {
        for (int i = 0; i < tasks.size(); i++) {
            // Display task status symbol
            String symbol = getSymbolForStatus(taskStatuses.get(i), i, animationStep);
            RGBColor color = getColorForStatus(taskStatuses.get(i));

            display.append(applyColor(symbol, color));

            // Add spacing between tasks
            if (i < tasks.size() - 1) {
                display.append(" ");
            }
        }
    }

    /**
     * Renders tasks in detailed mode (one per line).
     */
    private void renderDetailed(StringBuilder display, int animationStep) {
        for (int i = 0; i < tasks.size(); i++) {
            // Get symbol and color for current status
            String symbol = getSymbolForStatus(taskStatuses.get(i), i, animationStep);
            RGBColor color = getColorForStatus(taskStatuses.get(i));

            // Format and display each task
            display.append(applyColor(symbol, color))
                    .append(" ")
                    .append(applyColor(tasks.get(i), getMessageColor()));

            // Add newline except for the last task
            if (i < tasks.size() - 1) {
                display.append("\n");
            }
        }
    }

    /**
     * Gets the appropriate symbol for a task status.
     */
    private String getSymbolForStatus(TaskStatus status, int taskIndex, int animationStep) {
        if (status == TaskStatus.IN_PROGRESS) {
            // Animate all in-progress tasks
            String[] spinnerFrames = {"|", "/", "-", "\\"};
            return spinnerFrames[animationStep % spinnerFrames.length];
        } else {
            // Use the appropriate static symbol
            switch (status) {
                case PENDING:
                    return pendingSymbol;
                case COMPLETE:
                    return completeSymbol;
                case FAILED:
                    return failedSymbol;
                default:
                    return pendingSymbol;
            }
        }
    }

    /**
     * Gets the appropriate color for a task status.
     */
    private RGBColor getColorForStatus(TaskStatus status) {
        return status.getColor();
    }
}
