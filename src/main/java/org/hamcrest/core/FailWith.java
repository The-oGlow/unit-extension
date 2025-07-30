package org.hamcrest.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.function.IThrowingRunnable;

import static org.hamcrest.Matchers.instanceOf;

/**
 * A matcher, which verifies if a function has raised a specific exception.
 *
 * @param <E> the type of the clazz of the exception
 *
 * @since 0.1.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class FailWith<E extends Throwable> extends BaseMatcher<IThrowingRunnable<E>> {

    private final Class<? super E>   exceptionClazz;
    private final Matcher<? super E> expectedMatcher;

    /**
     * @param exceptionClazz the clazz which is expected to be raised
     */
    public FailWith(Class<? super E> exceptionClazz) {
        this.exceptionClazz = exceptionClazz;
        this.expectedMatcher = instanceOf(exceptionClazz);
    }

    /**
     * Creates a matcher, that matches when the examined {@link org.junit.function.ThrowingRunnable} has raised the {@code expectedException}.
     * <p>
     * For example:
     * <pre>assertThat(()-&gt;myFunction(), failWith(MyException.class))</pre>
     *
     * @param expectedException the clazz of the exception
     * @param <E>               type of the exception
     *
     * @return newly created matcher
     */
    public static <E extends Throwable> Matcher<IThrowingRunnable<E>> failWith(Class<? extends E> expectedException) {
        return new FailWith(expectedException);
    }

    /**
     * @return the clazz which is expected to be raised
     */
    public Class<? super E> getExceptionClazz() {
        return exceptionClazz;
    }

    /**
     * @return the matcher which verifies
     */
    public Matcher<? super E> getExpectedMatcher() { //NOSONAR java:S1452
        return expectedMatcher;
    }

    @Override
    public boolean matches(Object actual) {
        try {
            var runnable = (IThrowingRunnable<E>) actual;
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
