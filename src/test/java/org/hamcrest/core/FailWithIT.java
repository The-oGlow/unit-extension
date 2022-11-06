package org.hamcrest.core;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.function.IThrowingRunnable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class FailWithIT {

    @Test
    public void testFailWith() {
        Matcher<IThrowingRunnable<Throwable>> expected = FailWith.failWith(FailWithHelper.FailWithValidException.class);
        assertThat(() -> FailWithHelper.getInstance().failWithValidException(), expected);
    }

    @Test
    public void testFailWithInvalidException() {
        Matcher<IThrowingRunnable<Throwable>> expected = FailWith.failWith(FailWithHelper.FailWithValidException.class);
        assertThat(() -> FailWithHelper.getInstance().failWithInValidException(), not(expected));
    }

    @Test
    public void testFailWithNoException() {
        Matcher<IThrowingRunnable<Throwable>> expected = FailWith.failWith(FailWithHelper.FailWithValidException.class);
        assertThat(() -> FailWithHelper.getInstance().failWithNoException(), not(expected));
    }

}
