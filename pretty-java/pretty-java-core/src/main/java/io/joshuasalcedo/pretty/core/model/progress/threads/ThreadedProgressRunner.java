package io.joshuasalcedo.pretty.core.model.progress.threads;





import io.joshuasalcedo.pretty.core.model.progress.AbstractProgressRunner;
import io.joshuasalcedo.pretty.core.properties.RGBColor;

import java.util.concurrent.*;

/**
 * A ThreadedProgressRunner that wraps a progress runner and runs it in its own thread.
 * This allows for managing multiple progress indicators concurrently.
 */
public class ThreadedProgressRunner implements Runnable, AutoCloseable {
    private final AbstractProgressRunner progressRunner;
    private final ExecutorService executor;
    private final Runnable task;
    private Future<?> future;
    private boolean isComplete = false;
    private final CountDownLatch completionLatch = new CountDownLatch(1);

    /**
     * Creates a ThreadedProgressRunner wrapping the provided progress runner.
     *
     * @param progressRunner The progress runner to execute in a thread
     * @param task           The task to execute while showing progress
     */
    public ThreadedProgressRunner(AbstractProgressRunner progressRunner, Runnable task) {
        this.progressRunner = progressRunner;
        this.task = task;
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("ProgressRunner-" + t.getId());
            return t;
        });
    }

    /**
     * Starts the progress runner and the task in separate threads.
     */
    public void start() {
        // Start the progress runner
        progressRunner.start();

        // Execute the task in a thread
        future = executor.submit(() -> {
            try {
                // Execute the provided task
                task.run();
            } finally {
                // When task completes, mark as complete and update progress
                isComplete = true;
                progressRunner.withProgress(1.0);
                completionLatch.countDown();
            }
        });
    }

    /**
     * Updates progress of the underlying progress runner.
     *
     * @param progress The progress value (0.0 to 1.0)
     * @return This instance for method chaining
     */
    public ThreadedProgressRunner withProgress(double progress) {
        progressRunner.withProgress(progress);
        return this;
    }

    /**
     * Updates message of the underlying progress runner.
     *
     * @param message The message to display
     * @return This instance for method chaining
     */
    public ThreadedProgressRunner withMessage(String message) {
        progressRunner.withMessage(message);
        return this;
    }

    /**
     * Sets the indeterminate mode of the underlying progress runner.
     *
     * @param indeterminate Whether the progress is indeterminate
     * @return This instance for method chaining
     */
    public ThreadedProgressRunner withIndeterminate(boolean indeterminate) {
        progressRunner.withIndeterminate(indeterminate);
        return this;
    }

    /**
     * Waits for the task to complete.
     *
     * @param timeout The maximum time to wait
     * @param unit    The time unit of the timeout
     * @return true if the task completed, false if it timed out
     * @throws InterruptedException if the wait is interrupted
     */
    public boolean awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
        return completionLatch.await(timeout, unit);
    }

    /**
     * Waits indefinitely for the task to complete.
     *
     * @throws InterruptedException if the wait is interrupted
     */
    public void awaitCompletion() throws InterruptedException {
        completionLatch.await();
    }

    /**
     * Stops both the task and the progress runner.
     */
    public void stop() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }

        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }

        if (progressRunner.isRunning()) {
            progressRunner.stop();
        }
    }

    /**
     * Checks if the underlying task is complete.
     *
     * @return true if the task is complete, false otherwise
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Gets the underlying progress runner.
     *
     * @return The progress runner
     */
    public AbstractProgressRunner getProgressRunner() {
        return progressRunner;
    }

    @Override
    public void run() {
        start();
    }

    @Override
    public void close() {
        stop();
    }
}