package org.easymock.internal.matchers;

import com.glowa_net.util.reflect.ReflectionHelper;
import org.mockito.ArgumentMatcher;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * An easy-mock matcher to verify, if two object instances have the same primary key (id).
 *
 * @param <T> type of the method argument to match
 *
 * @author Oliver Glowa
 * @see org.easymock.EasyMockMatcher
 * @since 0.10.000
 */
@SuppressWarnings("unchecked")
public class PrimaryId<T> implements ArgumentMatcher {

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
            result = true;
        } else if ((expectedInstance != null && actual != null) //
                && (expectClazz.isAssignableFrom(actual.getClass()))) {
            final Object actualValue = ReflectionHelper.readField(expectedField, (T) actual);
            result = Objects.equals(actualValue, expectedValue);
        }
        return result;
    }

    public void appendTo(final StringBuffer buffer) {
        buffer.append("eq").append(expectedInstance == null ? "null" : expectClazz.getSimpleName()).append("(");
        buffer.append(" with id=<");
        buffer.append(expectedValue);
        buffer.append(">)");
    }
}
