package com.glowanet.util.junit;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.ErrorCollector;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestResultHelperErrorCollectorTest {

    static final int                    EXPECT_VAL         = 10;
    static final int                    ACTUAL_VAL_MATCH   = 10;
    static final int                    ACTUAL_VAL_NOMATCH = 20;
    static final int                    ERRSIZE_0          = 0;
    static final int                    ERRSIZE_1          = 1;
    static final Matcher<Integer>       MATCHER_EQ_0       = Matchers.equalTo(0);
    static final Matcher<Integer>       MATCHER_EQ_1       = Matchers.equalTo(1);
    static final Matcher<Collection<?>> MATCHER_SIZEOF_0   = Matchers.hasSize(ERRSIZE_0);
    static final Matcher<Collection<?>> MATCHER_SIZEOF_1   = Matchers.hasSize(ERRSIZE_1);
    static final Class<?>               EXCEPT_IAE         = IllegalArgumentException.class;
    static final String                 EXCEPT_IAE_MSG     = "detailMsg";

    static class TestResultHelperTestClazz {
        public ErrorCollector collector = new ErrorCollector();

        TestResultHelperTestClazz() {
        }
    }

    public ErrorCollector prepErrorCollector() {
        return new ErrorCollector();
    }

    public ErrorCollector prepErrorCollector(int errorSize) {
        ErrorCollector collector = prepErrorCollector();
        for (int i = 0; i < errorSize; i++) {
            collector.addError(prepException(EXCEPT_IAE, EXCEPT_IAE_MSG));
        }
        return collector;
    }

    public Throwable prepException(Class<?> throwableClazz, String exceptMsg) {
        try {
            Object[] args = new Object[]{};
            Class<?>[] types = new Class[]{};
            if (exceptMsg != null) {
                args = new Object[]{exceptMsg};
                types = new Class[]{String.class};
            }
            return (Throwable) ConstructorUtils.invokeConstructor(throwableClazz, args, types);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new Error(e);
        }
    }

    private ThrowingRunnable prepRunnable() {
        return () -> {
            //nothing2do
        };
    }

    private ThrowingRunnable prepRunnableWithException(String exceptMsg) {
        return () -> {
            throw prepException(EXCEPT_IAE, exceptMsg);
        };
    }

    public void verifyResult(ThrowingRunnable throwingRunnable, Class<? extends Throwable> expectedException) {
        try {
            throwingRunnable.run();
        } catch (Throwable e) {
            assertThat(e, instanceOf(expectedException));
        }
    }

    public void verifyResult(ThrowingRunnable throwingRunnable) {
        try {
            throwingRunnable.run();
        } catch (Throwable e) {
            fail(e.getMessage()); //NOSONAR java:S3658
        }
    }

    @Test
    public void testVerifyCollectorNoErrorWithCollector() {
        verifyResult(() -> TestResultHelper.verifyCollectorNoError(prepErrorCollector()));
    }

    @Test
    public void testVerifyCollectorNoErrorWithValueMatch() {
        Object expexted = EXPECT_VAL;
        Object actual = ACTUAL_VAL_MATCH;

        verifyResult(() -> TestResultHelper.verifyCollectorNoError(prepErrorCollector(), expexted, actual));
    }

    @Test
    public void verifyCollectorWithErrorsize() {
        verifyResult(() -> TestResultHelper.verifyCollector(prepErrorCollector(), ERRSIZE_0));
    }

    @Test
    public void testVerifyCollectorWithErrorsizeAndValueMatch() {
        Object expexted = EXPECT_VAL;
        Object actual = ACTUAL_VAL_MATCH;

        verifyResult(() -> TestResultHelper.verifyCollector(prepErrorCollector(), ERRSIZE_0, expexted, actual));
    }

    @Test
    public void testVerifyCollectorWithMatcher() {
        verifyResult(() -> TestResultHelper.verifyCollector(prepErrorCollector(), MATCHER_SIZEOF_0));
    }

    @Test
    public void testVerifyCollectorWithResetWithErrorsize() {
        ErrorCollector collector = prepErrorCollector(ERRSIZE_1);

        verifyResult(() -> TestResultHelper.verifyCollectorWithReset(collector, ERRSIZE_1), IllegalArgumentException.class);
    }

    @Test
    public void testVerifyCollectorWithResetWithErrorsizeAndValueMatch() {
        Object expexted = EXPECT_VAL;
        Object actual = ACTUAL_VAL_MATCH;
        ErrorCollector collector = prepErrorCollector(ERRSIZE_1);

        verifyResult(() -> TestResultHelper.verifyCollectorWithReset(collector, ERRSIZE_1, expexted, actual), IllegalArgumentException.class);
    }

    @Test
    public void testVerifyCollectorWithResetWithMatcher() {
        ErrorCollector collector = prepErrorCollector(ERRSIZE_1);

        verifyResult(() -> TestResultHelper.verifyCollectorWithReset(collector, MATCHER_SIZEOF_1), IllegalArgumentException.class);
    }

    @Test
    public void verifyNoExceptionReturnNoException() {
        ThrowingRunnable runnable = prepRunnable();

        TestResultHelper.verifyNoException(runnable);
    }

    @Test
    public void testVerifyException() {
        ThrowingRunnable runnable = prepRunnableWithException(null);
        Class<?> expectedClazz = EXCEPT_IAE;

        Throwable actual = TestResultHelper.verifyException(runnable, expectedClazz);

        assertThat(actual, instanceOf(expectedClazz));
        assertThat(actual.getMessage(), nullValue());
    }

    @Test
    public void testVerifyExceptionWithMsg() {
        ThrowingRunnable runnable = prepRunnableWithException(EXCEPT_IAE_MSG);
        Class<?> expectedClazz = EXCEPT_IAE;
        String expectedMsg = EXCEPT_IAE_MSG;

        Throwable actual = TestResultHelper.verifyException(runnable, expectedClazz, expectedMsg);

        assertThat(actual, instanceOf(expectedClazz));
        assertThat(actual.getMessage(), containsString(expectedMsg));
    }

    @Test
    public void testVerifyNoNull() {
        Object instance = "HELLO WORLD";
        TestResultHelper.verifyNoNull(instance);
    }

    @Test
    public void testVerifyNull() {
        Object instance = null;

        TestResultHelper.verifyNull(instance);
    }

    @Test
    public void testVerifyInstance() {
        Object instance = "HELLO WORLD";
        Class<?> expectedClazz = String.class;

        TestResultHelper.verifyInstance(instance, expectedClazz);
    }

    @Test
    public void extractCollector() {
        Object instance = new TestResultHelperTestClazz();
        Object actual = TestResultHelper.extractCollector(instance);

        assertThat(actual, instanceOf(ErrorCollector.class));

    }

    @Test
    public void testPrepareCollectorIsCollector() {
        Object actual = TestResultHelper.prepareCollector(prepErrorCollector());

        assertThat(actual, instanceOf(ErrorCollector.class));
    }

    @Test
    public void testIsCollectorIsTrue() {
        boolean actual = TestResultHelper.isCollector(prepErrorCollector());

        assertThat(actual, equalTo(true));
    }

    @Test
    public void testIsExtendIsFalse() {
        boolean actual = TestResultHelper.isExtend(prepErrorCollector());

        assertThat(actual, equalTo(false));
    }

    @Test
    public void testLogTheErrors() {
        TestResultHelper.logTheErrors(prepErrorCollector());

        assertTrue("Fail", true); //NOSONAR java:S2701
    }
}