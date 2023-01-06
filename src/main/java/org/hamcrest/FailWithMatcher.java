package org.hamcrest;

import org.hamcrest.core.FailWith;
import org.junit.function.IThrowingRunnable;

/**
 * @deprecated
 */
@Deprecated(forRemoval = true, since = "0.1.0")
public class FailWithMatcher {

    private FailWithMatcher() {
        //nothing2do
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
    public static <E extends Throwable> Matcher<IThrowingRunnable<E>> failWith(Class<E> expectedException) {
        return FailWith.failWith(expectedException);
    }
}
