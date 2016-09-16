package com.github.ylegat.uncheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.catchThrowable;
import static com.github.ylegat.uncheck.Uncheck.uncheck;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import com.github.ylegat.uncheck.Uncheck.CheckedFunction;
import org.junit.Test;
import com.github.ylegat.uncheck.Uncheck.CheckedOperation;
import com.github.ylegat.uncheck.Uncheck.CheckedSupplier;

public class UncheckTest {

    @Test
    public void should_return_supplier_value() {
        // When
        Integer result = uncheck(() -> 1);

        // Then
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void should_execute_operation() {
        // Given
        AtomicBoolean atomicBoolean = new AtomicBoolean();

        // When
        uncheck(() -> atomicBoolean.set(true));

        // Then
        assertThat(atomicBoolean.get()).isTrue();
    }

    @Test
    public void should_transform_value() {
        // Given
        Integer value = 42;
        Function<Integer, String> uncheckedTransform = uncheck(String::valueOf);

        // When
        String transformedValue = uncheckedTransform.apply(value);

        // Then
        assertThat(transformedValue).isEqualTo(String.valueOf(value));
    }

    @Test
    public void should_propagate_runtime_exception_from_operation_execution() {
        // Given
        RuntimeException runtimeException = new RuntimeException();
        CheckedOperation operation = () -> {
            throw runtimeException;
        };

        // When
        Throwable throwable = catchThrowable(() -> uncheck(operation));

        // Then
        assertThat(throwable).isSameAs(runtimeException);
    }

    @Test
    public void should_create_unchecked_io_exception_from_operation_execution_io_exception() {
        // Given
        IOException ioException = new IOException();
        CheckedOperation operation = () -> {
            throw ioException;
        };

        // When
        Throwable throwable = catchThrowable(() -> uncheck(operation));

        // Then
        assertThat(throwable).isInstanceOf(UncheckedIOException.class)
                             .hasCause(ioException);
    }

    @Test
    public void should_create_runtime_exception_from_operation_execution_checked_exception() {
        // Given
        Exception exception = new Exception();
        CheckedOperation operation = () -> {
            throw exception;
        };

        // When
        Throwable throwable = catchThrowable(() -> uncheck(operation));

        // Then
        assertThat(throwable).isInstanceOf(RuntimeException.class)
                             .hasCause(exception);
    }

    @Test
    public void should_propagate_runtime_exception_from_transformation_application() {
        // Given
        RuntimeException runtimeException = new RuntimeException();
        CheckedFunction<Integer, Object> transform = any -> {
            throw runtimeException;
        };

        // When
        Throwable throwable = catchThrowable(() -> uncheck(transform).apply(42));

        // Then
        assertThat(throwable).isSameAs(runtimeException);
    }

    @Test
    public void should_create_unchecked_io_exception_from_transformation_application_io_exception() {
        // Given
        IOException ioException = new IOException();
        CheckedFunction<Integer, Object> transform = any -> {
            throw ioException;
        };

        // When
        Throwable throwable = catchThrowable(() -> uncheck(transform).apply(42));

        // Then
        assertThat(throwable).isInstanceOf(UncheckedIOException.class).hasCause(ioException);
    }

    @Test
    public void should_create_runtime_exception_from_transformation_application_checked_exception() {
        // Given
        Exception exception = new Exception();
        CheckedFunction<Integer, Object> transform = any -> {
            throw exception;
        };

        // When
        Throwable throwable = catchThrowable(() -> uncheck(transform).apply(42));

        // Then
        assertThat(throwable).isInstanceOf(RuntimeException.class).hasCause(exception);
    }
}
