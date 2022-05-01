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

    public static <T> void verifyCollectorNoError(Object collectorOrInstance, T expected, T actual) {
        assertThat(actual, equalTo(expected));
        verifyCollectorNoError(collectorOrInstance);
    }

    public static void verifyCollectorNoError(Object collectorOrInstance) {
        verifyCollector(collectorOrInstance, NO_ERROR);
    }

    public static <T> void verifyCollector(Object collectorOrInstance, int errorSize, T expected, T actual) {
        if (!equalTo(expected).matches(actual)) {
            logTheErrors(collectorOrInstance);
        }
        assertThat(actual, equalTo(expected));
        verifyCollector(collectorOrInstance, errorSize);
    }

    public static void verifyCollector(Object collectorOrInstance, Matcher<?> matcher) {
        ErrorCollector collector = prepareCollector(collectorOrInstance);
        if (isExtend(collector)) {
            ErrorCollectorExt collectorExt = (ErrorCollectorExt) collector;
            if (!matcher.matches(collectorExt.getErrorSize())) {
                logTheErrors(collectorExt);
            }
            assertThat(collectorExt.getErrorSize(), (Matcher<Number>) matcher);
        } else {
            // legacy mode
            Object actualThrows = ReflectionHelper.readField(ERRORS_NAME, collector);
            assertThat(actualThrows, notNullValue());
            assertThat(actualThrows, instanceOf(Collection.class));
            assertThat(((Collection<?>) actualThrows), (Matcher<Collection<?>>) matcher);
        }
    }

    public static void verifyCollector(Object collectorOrInstance, int errorSize) {
        ErrorCollector collector = prepareCollector(collectorOrInstance);
        if (isExtend(collector)) {
            verifyCollector(collector, equalTo(errorSize));
        } else {
            // legacy mode
            verifyCollector(collector, hasSize(errorSize));
        }
    }

    public static <T> void verifyCollectorWithReset(Object collectorOrInstance, int errorSize, T expected, T actual) {
        assertThat(actual, equalTo(expected));
        verifyCollectorWithReset(collectorOrInstance, errorSize);
    }

    public static void verifyCollectorWithReset(Object collectorOrInstance, Matcher<?> matcher) {
        ErrorCollector collector = prepareCollector(collectorOrInstance);
        verifyCollector(collector, matcher);
        if (isExtend(collector)) {
            ((ErrorCollectorExt) collector).reset();
        }
    }

    public static void verifyCollectorWithReset(Object collectorOrInstance, int errorSize) {
        ErrorCollector collector = prepareCollector(collectorOrInstance);
        verifyCollector(collector, errorSize);
        if (isExtend(collector)) {
            ((ErrorCollectorExt) collector).reset();
        }
    }

    public static void verifyNoException(ThrowingRunnable runnable) {
        AssertionError error = assertThrows(AssertionError.class, () -> assertThrows(Throwable.class, runnable));

        assertThat(error, notNullValue());
        assertThat(error.toString(), containsString(String.format(NOT_THROWN, Throwable.class.getName())));
    }

    public static Throwable verifyException(ThrowingRunnable runnable, Class<?> expectedClazz) {
        Throwable throwable = assertThrows(Throwable.class, runnable);

        assertThat(throwable, notNullValue());
        assertThat(throwable, instanceOf(expectedClazz));
        return throwable;
    }

    public static void verifyException(ThrowingRunnable runnable, Class<?> expectedClazz, String expectedMsg) {
        Throwable throwable = verifyException(runnable, expectedClazz);

        assertThat(throwable.getMessage(), containsString(expectedMsg));
    }

    public static void verifyNoNull(Object instance) {
        assertThat(instance, notNullValue());
    }

    public static void verifyNull(Object instance) {
        assertThat(instance, nullValue());
    }

    public static void verifyInstance(Object instance, Class<?> expected) {
        assertThat(instance, instanceOf(expected));
    }

    protected static ErrorCollector extractCollector(Object instance) {
        Object field = ReflectionHelper.readField(COLLECTOR_NAME, instance);

        assertThat(field, notNullValue());
        assertThat(field, instanceOf(ErrorCollector.class));
        return (ErrorCollector) field;
    }

    protected static ErrorCollector prepareCollector(Object collectorOrInstance) {
        ErrorCollector collector;
        if (isCollector(collectorOrInstance)) {
            collector = (ErrorCollector) collectorOrInstance;
        } else {
            collector = extractCollector(collectorOrInstance);
        }
        return collector;
    }

    protected static boolean isCollector(Object collectorOrInstance) {
        return (instanceOf(ErrorCollector.class).matches(collectorOrInstance));
    }

    protected static boolean isExtend(ErrorCollector collector) {
        return (instanceOf(ErrorCollectorExt.class).matches(collector));
    }

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
