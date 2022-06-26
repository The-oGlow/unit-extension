package org.easymock.internal.matchers;

import com.glowanet.util.reflect.ReflectionHelper;
import org.easymock.IArgumentMatcher;
import org.mockito.ArgumentMatcher;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * An easy-mock / hamcrest matcher to verify, if two object instances have the same primary key (id).
 *
 * @param <T> type of the method argument to match
 *
 * @author Oliver Glowa
 * @see org.easymock.EasyMockMatcher
 * @since 0.10.000
 */
public class PrimaryId<T> implements ArgumentMatcher<T>, IArgumentMatcher {

    private final T        expectedInstance;
    private final Field    expectedField;
    private final Class<T> expectClazz;
    private final Object   expectedValue;

    /**
     * @param expectedInstance an instance of the expected type
     * @param primIdFieldName  the field name which is used as primary-id.
     */
    public PrimaryId(final T expectedInstance, final String primIdFieldName) {
        this.expectedInstance = expectedInstance;
        if (expectedInstance != null) {
            this.expectClazz = (Class<T>) expectedInstance.getClass();
            this.expectedField = ReflectionHelper.findField(primIdFieldName, expectedInstance);
            this.expectedValue = ReflectionHelper.readField(expectedField, expectedInstance);
        } else {
            this.expectClazz = null;
            this.expectedField = null;
            this.expectedValue = null;
        }
    }

    @Override
    public boolean matches(final Object actual) {
        boolean result = false;
        if (expectedInstance == null && actual == null) {
            // nothing to do
        } else if ((expectedInstance != null && actual != null) && (expectClazz.isAssignableFrom(actual.getClass()))) {
            final Object actualValue = ReflectionHelper.readField(expectedField, actual);
            result = Objects.equals(actualValue, expectedValue);
        } else {
            // nothing to do
        }
        return result;
    }

    /**
     * @param buffer the previous text to append some new text
     */
    public void appendTo(final StringBuffer buffer) {
        buffer.append(expectClazz == null ? "null" : expectClazz.getName());
        buffer.append(" with ").append(expectedField == null ? "null" : expectedField.getName()).append("=<");
        buffer.append(expectedValue).append(">");
    }

    @Override
    public String toString() {
        return "PrimaryId{" +
                "expectClazz=" + expectClazz +
                ", expectedField=" + expectedField +
                ", expectedInstance=" + expectedInstance +
                ", expectedValue=" + expectedValue +
                '}';
    }
}
