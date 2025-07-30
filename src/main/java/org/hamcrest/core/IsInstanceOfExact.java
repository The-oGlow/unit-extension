package org.hamcrest.core;

import com.glowanet.util.reflect.ReflectionHelper;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class IsInstanceOfExact extends IsInstanceOf {

    private final Class<?> matchableClassExact;

    /**
     * Creates a new instance of IsInstanceOfExact
     *
     * @param expectedClass The predicate evaluates to true for instances of this class.
     */
    public IsInstanceOfExact(Class<?> expectedClass) {
        super(expectedClass);
        this.matchableClassExact = ReflectionHelper.readField("matchableClass", this);
    }

    @Override
    protected boolean matches(Object item, Description mismatch) {
        if (null == item) {
            mismatch.appendText("null");
            return false;
        }
        if (!matchableClassExact.equals(item.getClass())) {
            mismatch.appendValue(item).appendText(" is a " + item.getClass().getName());
            return false;
        }
        return true;
    }

    /**
     * Creates a matcher that matches when the examined object is an instance of the specified <code>type</code>,
     * as determined by calling the {@link java.lang.Class#equals(Object)} method on that type, passing the
     * the examined object.
     *
     * <p>The created matcher assumes no relationship between specified type and the examined object.</p>
     * For example:
     * <pre>assertThat(new Canoe(), instanceOfExact(Paddlable.class));</pre>
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> instanceOfExact(Class<?> type) {
        return (Matcher<T>) new IsInstanceOfExact(type);
    }

    /**
     * Creates a matcher that matches when the examined object is an instance of the specified <code>type</code>,
     * as determined by calling the {@link java.lang.Class#equals(Object)} method on that type, passing the
     * the examined object.
     *
     * <p>The created matcher forces a relationship between specified type and the examined object, and should be
     * used when it is necessary to make generics conform, for example in the JMock clause
     * <code>with(any(Thing.class))</code></p>
     * For example:
     * <pre>assertThat(new Canoe(), any(Canoe.class));</pre>
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> anyExact(Class<T> type) {
        return (Matcher<T>) new IsInstanceOfExact(type);
    }
}
