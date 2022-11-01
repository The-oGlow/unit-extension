package org.hamcrest.core;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

@RunWith(Parameterized.class)
public class FailWithTest {

    public static class FailWithTestException extends RuntimeException {
        public FailWithTestException() {
            super();
        }
    }

    private static class FailWithTestValidException implements ThrowingRunnable {
        @Override
        public void run() {
            throw new FailWithTestException();
        }
    }

    private static class FailWithTestInvalidException implements ThrowingRunnable {
        @Override
        public void run() {
            throw new IllegalArgumentException();
        }
    }

    private static class FailWithTestNoException implements ThrowingRunnable {
        @Override
        public void run() {
            //nothing2do
        }
    }

    @Parameterized.Parameters(name = "{index} exception={0}, expected={1}")
    public static List<Object[]> data() {
        List<Object[]> testData = new ArrayList<>();
        testData.add(new Object[]{new FailWithTestValidException(), true});
        testData.add(new Object[]{new FailWithTestInvalidException(), false});
        testData.add(new Object[]{new FailWithTestNoException(), false});
        testData.add(new Object[]{null, false});
        return testData;
    }

    @Parameterized.Parameter
    public ThrowingRunnable actualThrowingRunnable;

    @Parameterized.Parameter(1)
    public boolean expectedResult;

    private FailWith<?> o2t;

    @Before
    public void setUp() throws Exception {
        o2t = new FailWith<>(FailWithTestException.class);
    }

    @Test
    public void testFailWith() {
        Matcher<?> actual = FailWith.failWith(FailWithTestException.class);

        assertThat(actual, instanceOf(FailWith.class));
    }

    @Test
    public void testMatchesSafely_validException_return_true() {
        boolean actual = o2t.matchesSafely(actualThrowingRunnable);
        assertThat(actual, equalTo(expectedResult));

    }

    @Test
    public void testDescribeTo() {
        Description description = new StringDescription();
        assertThat(description.toString(), emptyString());

        o2t.describeTo(description);

        assertThat(description.toString(), not(emptyString()));
    }
}