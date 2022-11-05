package org.junit.function;

/**
 * This interface is need to evaluate the exception in an {@code ThrowingRunnable}
 *
 * @param <E> the type of the exception
 *
 * @see org.hamcrest.core.FailWith
 */
@FunctionalInterface
public interface IThrowingRunnable<E extends Throwable> extends ThrowingRunnable {

    @Override
    void run() throws E;
}
