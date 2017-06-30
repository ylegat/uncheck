package com.github.ylegat.uncheck;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Function;

public class Uncheck {

    /**
     * Represents a supplier of results.
     *
     *  * <p>There is no requirement that a new or distinct result be returned each
     * time the supplier is invoked.
     *
     * @param <T> the type of results supplied by this supplier
     */
    @FunctionalInterface
    public interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    /**
     * Represents an operation that returns no result.
     */
    @FunctionalInterface
    public interface CheckedOperation {
        void execute() throws Exception;
    }

    /**
     * Represents an operation that returns no result.
     */
    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R apply(T param) throws Exception;
    }

    /**
     * <p>Execute the given operation.</p>
     * <p>Exceptions are handled this way:</p>
     * <ul>
     *     <li>RuntimeException are just propagated</li>
     *     <li>IOException are wrapped inside UncheckedIOException</li>
     *     <li>Other exceptions are wrapped inside RuntimeException</li>
     * </ul>
     *
     * @param operation the operation to execute
     */
    public static void uncheck(CheckedOperation operation) {
        uncheck(() -> {
            operation.execute();
            return null;
        });
    }

    /**
     * <p>Recover the given supplier result and returns it.</p>
     * <p>Exceptions are handled this way:</p>
     * <ul>
     *     <li>RuntimeException are just propagated</li>
     *     <li>IOException are wrapped inside UncheckedIOException</li>
     *     <li>Other exceptions are wrapped inside RuntimeException</li>
     * </ul>
     *
     * @param supplier the supplier containing the value to recover
     * @param <T> the type of results supplied by the given supplier
     * @return the value recovered from the supplier
     */
    public static <T> T uncheck(CheckedSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (RuntimeException e) {
            throw e;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Wrap a transformation function.</p>
     * <p>Exceptions are handled this way:</p>
     * <ul>
     * <li>RuntimeException are just propagated</li>
     * <li>IOException are wrapped inside UncheckedIOException</li>
     * <li>Other exceptions are wrapped inside RuntimeException</li>
     * </ul>
     *
     * @param transformer the transformation function to apply
     * @param <T>      the parameter type of the transformation function
     * @param <R>      the result type of the transformation function
     * @return a wrapper around the transformation function that handles exceptions in a lambda-friendly way.
     */
    public static <T, R> Function<T, R> uncheck(CheckedFunction<T, R> transformer) {
        return value -> uncheck(() -> transformer.apply(value));
    }
}
