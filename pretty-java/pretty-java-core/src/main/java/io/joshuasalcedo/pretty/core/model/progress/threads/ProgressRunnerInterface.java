package io.joshuasalcedo.pretty.core.model.progress.threads;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Interface for a progress runner factory.
 * <p>
 * This interface defines methods for creating progress runners with various
 * configuration options, allowing for flexible progress tracking of different
 * types of tasks.
 * </p>
 * <p>
 * Implementations of this interface create ThreadedProgressRunner instances
 * to track progress and provide visual feedback during long-running operations
 * such as processing items in a list, executing multiple methods, or applying
 * functions to inputs.
 * </p>
 */
public interface ProgressRunnerInterface<T extends ThreadedProgressRunner> extends Runnable, AutoCloseable {

    /**
     * Creates a progress runner for executing a list of methods.
     *
     * @param methods The methods to execute
     * @return A threaded progress runner
     */
    T createProgressRunner(Method... methods);

    /**
     * Creates a progress runner for executing a list of consumers.
     *
     * @param methods The consumers to execute
     * @return A threaded progress runner
     */
    T createProgressRunner(Consumer<?>... methods);

    /**
     * Creates a progress runner for executing a list of functions.
     *
     * @param functions The list of functions to execute
     * @return A threaded progress runner
     */
    T createFunctionProgressRunner(List<Function<?, ?>> functions);

    /**
     * Creates a progress runner for executing a list of suppliers.
     *
     * @param suppliers The list of suppliers to execute
     * @return A threaded progress runner
     */
    T createSupplierProgressRunner(List<Supplier<?>> suppliers);

    /**
     * Creates a progress runner for processing a list of items.
     *
     * @param <E> The type of items
     * @param items The items to process
     * @param processor The consumer to process each item
     * @return A threaded progress runner
     */
    <E> T createItemProcessor(List<E> items, Consumer<E> processor);

    /**
     * Creates a progress runner for task batches.
     *
     * @param tasks The tasks to execute
     * @return A threaded progress runner
     */
    T createBatchProcessor(List<Runnable> tasks);
}