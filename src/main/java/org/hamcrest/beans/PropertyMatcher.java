package org.hamcrest.beans;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import static org.hamcrest.beans.SamePropertiesValuesAs.readProperty;
import static org.hamcrest.core.IsEqual.equalTo;

@SuppressWarnings("WeakerAccess")
class PropertyMatcher extends DiagnosingMatcher<Object> {
    private final Method          readMethod;
    private final Matcher<Object> matcher;
    private final String          propertyName;

    public PropertyMatcher(PropertyDescriptor descriptor, Object expectedObject) {
        this.propertyName = descriptor.getDisplayName();
        this.readMethod = descriptor.getReadMethod();
        if (this.readMethod != null) {
            this.matcher = equalTo(readProperty(this.readMethod, expectedObject));
        } else {
            this.matcher = Matchers.not(Matchers.anything());
        }
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
            mismatch.appendText(",").appendText(System.lineSeparator());
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(propertyName + ": ").appendDescriptionOf(matcher);
    }
}
