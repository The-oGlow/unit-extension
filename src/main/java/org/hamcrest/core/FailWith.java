package org.hamcrest.core;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.function.ThrowingRunnable;

import static org.hamcrest.Matchers.instanceOf;

public class FailWith<E extends Throwable> extends TypeSafeMatcher<ThrowingRunnable> {

    //private final Class<E>   exceptionClazz;
    private final Matcher<E> expectedMatcher;

//    @FunctionalInterface
//    public interface FailWithRunnable<E extends Throwable> {
//        void run() throws E;
//    }

    public FailWith(Class<E> exceptionClazz) {
        super();
        //this.exceptionClazz = exceptionClazz;
        this.expectedMatcher = instanceOf(exceptionClazz);
    }

    public static <E extends Throwable> Matcher<E> failWith(Class<E> expectedException) {
        return new FailWith(expectedException);
    }

    protected boolean matchesSafely(ThrowingRunnable runnable) {
        try {
            runnable.run();
            return false;
        } catch (Throwable ex) { //NOSONAR java:S1181
            return expectedMatcher.matches(ex);
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("fails with ").appendDescriptionOf(expectedMatcher);
    }
}
