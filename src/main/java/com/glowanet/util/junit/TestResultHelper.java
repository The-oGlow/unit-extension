package com.glowanet.util.junit;

import com.glowanet.util.junit.rules.ErrorCollectorExt;
import com.glowanet.util.reflect.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.ErrorCollector;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThrows;

/**
 * A helper to verify unit tests in a common way.
 */
public class TestResultHelper {

    public static final String                 COLLECTOR_NAME = "collector";
    public static final String                 ERRORS_NAME    = "errors";
    public static final String                 NOT_THROWN     = "expected %s to be thrown, but nothing was thrown";
    public static final Matcher<Collection<?>> EMPTY_LIST     = Matchers.hasSize(0);
    public static final Matcher<Collection<?>> SINGLE_LIST    = Matchers.hasSize(1);
    public static final int                    NO_ERROR       = 0;
    public static final int                    WITH_ERROR     = 1;
    public static final int                    TWO_ERROR      = 2;

    private static final Logger LOGGER = LogManager.getLogger();

    private TestResultHelper() {
        // static helper
    }

    /**
     * @param collectorOrInstance an instance of a collector
     * @param expected            the expected result for this unit test
     * @param actual              the current result of this unit test
     * @param <T>                 the type of {@code expected} and {@code actual}
     *
     * @throws AssertionError is thrown, if the {@code collectorOrInstance} is not empty or {@code actual} differs from {@code expected}
     */
    public static <T> void verifyCollectorNoError(Object collectorOrInstance, T expected, T actual) {
        assertThat(actual, equalTo(expected));
        verifyCollectorNoError(collectorOrInstance);
    }

    /**
     * @param collectorOrInstance an instance of a collector
     *
     * @throws AssertionError is thrown, if the {@code collectorOrInstance} is not empty
     */
    public static void verifyCollectorNoError(Object collectorOrInstance) {
        verifyCollector(collectorOrInstance, NO_ERROR);
    }

    /**
     * @param collectorOrInstance an instance of a collector
     * @param errorSize           number of errors in the collector
     * @param expected            the expected result for this unit test
     * @param actual              the current result of this unit test
     * @param <T>                 the type of {@code expected} and {@code actual}
     *
     * @throws AssertionError is thrown, if the {@code collectorOrInstance} has not the {@code errorSize} or {@code actual} differs from {@code expected}
     */
    public static <T> void verifyCollector(Object collectorOrInstance, int errorSize, T expected, T actual) {
        if (!equalTo(expected).matches(actual)) {
            logTheErrors(collectorOrInstance);
        }
        assertThat(actual, equalTo(expected));
        verifyCollector(collectorOrInstance, errorSize);
    }

    /**
     * @param collectorOrInstance an instance of a collector
     * @param errorSizeMatcher    verifies the size of errors
     *
     * @throws AssertionError is thrown, if the {@code collectorOrInstance} does not matches {@code errorSizeMatcher}
     */
    @SuppressWarnings("unchecked")
    public static void verifyCollector(Object collectorOrInstance, Matcher<?> errorSizeMatcher) {
        ErrorCollector collector = prepareCollector(collectorOrInstance);
        if (isExtend(collector)) {
            ErrorCollectorExt collectorExt = (ErrorCollectorExt) collector;
            if (!errorSizeMatcher.matches(collectorExt.getErrorSize())) {
                logTheErrors(collectorExt);
            }
            assertThat(collectorExt.getErrorSize(), (Matcher<Number>) errorSizeMatcher);
        } else {
            // legacy mode
            Object actualThrows = ReflectionHelper.readField(ERRORS_NAME, collector);
            assertThat(actualThrows, notNullValue());
            assertThat(actualThrows, instanceOf(Collection.class));
            assertThat(((Collection<?>) actualThrows), (Matcher<Collection<?>>) errorSizeMatcher);
        }
    }

    /**
     * @param collectorOrInstance an instance of a collector
     * @param errorSize           number of errors in the collector
     *
     * @throws AssertionError is thrown, if the {@code collectorOrInstance} has not the {@code errorSize}
     */
    public static void verifyCollector(Object collectorOrInstance, int errorSize) {
        ErrorCollector collector = prepareCollector(collectorOrInstance);
        if (isExtend(collector)) {
            verifyCollector(collector, equalTo(errorSize));
        } else {
            // legacy mode
            verifyCollector(collector, hasSize(errorSize));
        }
    }

    /**
     * The verification will be processed, afterwards the collector is reset to zero.
     *
     * @param collectorOrInstance an instance of a collector
     * @param errorSize           number of errors in the collector
     * @param expected            the expected result for this unit test
     * @param actual              the current result of this unit test
     * @param <T>                 the type of {@code expected} and {@code actual}
     *
     * @throws AssertionError is thrown, if the {@code collectorOrInstance} has not the {@code errorSize} or {@code actual} differs from {@code expected}
     */
    public static <T> void verifyCollectorWithReset(Object collectorOrInstance, int errorSize, T expected, T actual) {
        assertThat(actual, equalTo(expected));
        verifyCollectorWithReset(collectorOrInstance, errorSize);
    }

    /**
     * The verification will be processed, afterwards the collected errors are reset to zero.
     *
     * @param collectorOrInstance an instance of a collector
     * @param errorSizeMatcher    verifies the size of errors
     *
     * @throws AssertionError is thrown, if the {@code collectorOrInstance} does not matches {@code errorSizeMatcher}
     */
    public static void verifyCollectorWithReset(Object collectorOrInstance, Matcher<?> errorSizeMatcher) {
        ErrorCollector collector = prepareCollector(collectorOrInstance);
        verifyCollector(collector, errorSizeMatcher);
        if (isExtend(collector)) {
            ((ErrorCollectorExt) collector).reset();
        }
    }

    /**
     * The verification will be processed, afterwards the collected errors are reset to zero.
     *
     * @param collectorOrInstance an instance of a collector
     * @param errorSize           number of errors in the collector
     *
     * @throws AssertionError is thrown, if the {@code collectorOrInstance} has not the {@code errorSize}
     */
    public static void verifyCollectorWithReset(Object collectorOrInstance, int errorSize) {
        ErrorCollector collector = prepareCollector(collectorOrInstance);
        verifyCollector(collector, errorSize);
        if (isExtend(collector)) {
            ((ErrorCollectorExt) collector).reset();
        }
    }

    /**
     * @param runnable the {@code ThrowingRunnable} to verify
     *
     * @throws AssertionError is thrown, if the {@code runnable} has raised an exception
     */
    public static void verifyNoException(ThrowingRunnable runnable) {
        AssertionError error = assertThrows(AssertionError.class, () -> assertThrows(Throwable.class, runnable));

        assertThat(error, notNullValue());
        assertThat(error.toString(), containsString(String.format(NOT_THROWN, Throwable.class.getName())));
    }

    /**
     * @param runnable      the {@code ThrowingRunnable} to verify
     * @param expectedClazz the type of the expected {@code Throwable}
     *
     * @return instance of {@code expectedClazz}
     *
     * @throws AssertionError is thrown, if the {@code runnable} has raised no exception or a different exception
     */
    public static Throwable verifyException(ThrowingRunnable runnable, Class<?> expectedClazz) {
        Throwable throwable = assertThrows(Throwable.class, runnable);

        assertThat(throwable, notNullValue());
        assertThat(throwable, instanceOf(expectedClazz));
        return throwable;
    }

    /**
     * @param runnable      the {@code ThrowingRunnable} to verify
     * @param expectedClazz the type of the expected {@code Throwable}
     * @param expectedMsg   the message of the expected {@code Throwable}
     *
     * @throws AssertionError is thrown, if the {@code runnable} has raised no exception or a different exception or a different error message
     */
    public static void verifyException(ThrowingRunnable runnable, Class<?> expectedClazz, String expectedMsg) {
        Throwable throwable = verifyException(runnable, expectedClazz);

        assertThat(throwable.getMessage(), containsString(expectedMsg));
    }

    /**
     * @param instance an instantiated object
     *
     * @throws AssertionError is thrown, if the {@code instance} is null
     */
    public static void verifyNoNull(Object instance) {
        assertThat(instance, notNullValue());
    }

    /**
     * @param instance an instantiated object
     *
     * @throws AssertionError is thrown, if the {@code instance} is NOT null
     */
    public static void verifyNull(Object instance) {
        assertThat(instance, nullValue());
    }

    /**
     * @param instance      an instantiated object
     * @param expectedClazz the expected type of {@code instance}
     *
     * @throws AssertionError is thrown, if the {@code instance} is NOT the type {@code expectedClazz}
     */
    public static void verifyInstance(Object instance, Class<?> expectedClazz) {
        assertThat(instance, instanceOf(expectedClazz));
    }

    /**
     * @param instance an instantiated object
     *
     * @return a collector instance
     *
     * @throws AssertionError is thrown, if the {@code instance} does not contain a {@link ErrorCollector}
     */
    protected static ErrorCollector extractCollector(Object instance) {
        Object field = ReflectionHelper.readField(COLLECTOR_NAME, instance);

        assertThat(field, notNullValue());
        assertThat(field, instanceOf(ErrorCollector.class));
        return (ErrorCollector) field;
    }

    /**
     * @param collectorOrInstance an instance of a collector
     *
     * @return a collector instance
     *
     * @throws AssertionError is thrown, if the {@code collectorOrInstance} is no {@link ErrorCollector) or }does not contain a {@link ErrorCollector}
     */
    protected static ErrorCollector prepareCollector(Object collectorOrInstance) {
        ErrorCollector collector;
        if (isCollector(collectorOrInstance)) {
            collector = (ErrorCollector) collectorOrInstance;
        } else {
            collector = extractCollector(collectorOrInstance);
        }
        return collector;
    }

    /**
     * @param collectorOrInstance an instance of a collector
     *
     * @return TRUE=the {@code collectorOrInstance} is an {@link ErrorCollector}, else FALSE
     */
    protected static boolean isCollector(Object collectorOrInstance) {
        return (instanceOf(ErrorCollector.class).matches(collectorOrInstance));
    }

    /**
     * @param collector an instance of a collector
     *
     * @return TRUE=the {@code collector} is an {@link ErrorCollectorExt}, else FALSE
     */
    protected static boolean isExtend(ErrorCollector collector) {
        return (instanceOf(ErrorCollectorExt.class).matches(collector));
    }

    /**
     * Write the current size of collected errors to the log.
     *
     * @param collectorOrInstance an instance of a collector
     */
    protected static void logTheErrors(Object collectorOrInstance) {
        ErrorCollector collector = prepareCollector(collectorOrInstance);
        if (isExtend(collector)) {
            ErrorCollectorExt collectorExt = (ErrorCollectorExt) collector;
            if (collectorExt.getErrorSize() > 0) {
                LOGGER.error(String.format("These are the collected errors :\n%s", collectorExt.getErrorTextsToString()));
            } else {
                LOGGER.error("No errors collected!");
            }
        }
    }
}
