package org.hamcrest.beans;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.beans.PropertyUtil.NO_ARGUMENTS;

class PropertyMatcher<T> extends DiagnosingMatcher<T> {
    static final String COULD_NOT_INVOKE = "Could not invoke %s on %s";
    static final String LIST_EQU         = "=";

    private final Method          readMethod;
    private final Matcher<Object> matcher;
    private final String          propertyName;

    private PropertyMatcher(PropertyDescriptor descriptor, Object expectedObject) {
        this.propertyName = descriptor.getDisplayName();
        this.readMethod = descriptor.getReadMethod();
        if (this.readMethod != null) {
            this.matcher = equalTo(readProperty(this.readMethod, expectedObject));
        } else {
            this.matcher = not(Matchers.anything());
        }
    }

    /**
     * @param descriptor     the descriptor for this property
     * @param expectedObject the expected value for this property
     *
     * @return a new created matcher
     */
    public static PropertyMatcher<Object> matchProperty(PropertyDescriptor descriptor, Object expectedObject) {
        return new PropertyMatcher<>(descriptor, expectedObject);
    }

    @Override
    public boolean matches(Object actual, Description mismatch) {
        final Object actualValue;
        if (readMethod == null) {
            actualValue = null;
        } else {
            actualValue = readProperty(readMethod, actual);
        }
        if (!matcher.matches(actualValue)) {
            mismatch.appendText(propertyName + " ");
            matcher.describeMismatch(actualValue, mismatch);
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(propertyName + LIST_EQU).appendDescriptionOf(matcher);
    }

    protected Object readProperty(Method method, Object target) {
        try {
            return method.invoke(target, NO_ARGUMENTS);
        } catch (Exception e) { //NOSONAR java:S2221
            throw new IllegalArgumentException(String.format(COULD_NOT_INVOKE, method, target), e);
        }
    }
}
