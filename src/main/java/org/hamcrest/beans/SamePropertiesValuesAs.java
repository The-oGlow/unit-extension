package org.hamcrest.beans;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.beans.PropertyUtil.NO_ARGUMENTS;
import static org.hamcrest.beans.PropertyUtil.propertyDescriptorsFor;

public class SamePropertiesValuesAs<T> extends DiagnosingMatcher<T> {

    private static final String      DELIMITER        = ", ";
    static final         Description DESC_DESCRIPTION = new StringDescription();

    protected static final String HAS_EXTRA_PROPERTIES_CALLED = "has extra properties called %s "; // trailing space is needed
    protected static final String IS_INCOMPATIBLE_TYPE        = "is incompatible type: %s";
    protected static final String COULD_NOT_INVOKE            = "Could not invoke %s on %s";
    protected static final String SAME_PROPERTY_VALUES_AS     = "same property values as %s";
    protected static final String LIST_START                  = "{";
    protected static final String LIST_END                    = "}";

    private final T                        expectedBean;
    private final Set<String>              propertyNames;
    private final List<PropertyMatcher<?>> propertyMatchers;
    private final List<String>             ignoredFields;

    public static <T> Matcher<T> samePropertiesValuesAs(T expectedBean, String... ignoredProperties) {
        return new SamePropertiesValuesAs<>(expectedBean, asList(ignoredProperties));
    }

    private SamePropertiesValuesAs(T expectedBean, List<String> ignoredProperties) {
        verifyInput(expectedBean);

        PropertyDescriptor[] descriptors = propertyDescriptorsFor(expectedBean, Object.class);
        this.expectedBean = expectedBean;
        this.ignoredFields = ignoredProperties;
        this.propertyNames = propertyNamesFrom(descriptors, ignoredProperties);
        this.propertyMatchers = propertyMatchersFor(expectedBean, descriptors, ignoredProperties);
    }

    private void verifyInput(final T expectedBean) {
        assertThat(expectedBean, notNullValue());
    }

    @Override
    protected boolean matches(Object actual, Description mismatch) {
        return isNotNull(actual, mismatch) && isCompatibleType(actual, mismatch) && hasNoExtraProperties(actual, mismatch)
                && hasAllMatchingValues(actual, mismatch);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format(SAME_PROPERTY_VALUES_AS, expectedBean.getClass().getSimpleName())) //
                .appendList(LIST_START, DELIMITER, LIST_END, propertyMatchers);

        if (!ignoredFields.isEmpty()) {
            description.appendText(" ignoring ") //
                    .appendValueList(LIST_START, DELIMITER, LIST_END, ignoredFields);
        }
    }

    protected boolean hasAllMatchingValues(Object actual, Description mismatchDescription) {
        boolean result = true;
        int idxLoop = 0;
        for (Matcher<?> propertyMatcher : propertyMatchers) {
            if (!propertyMatcher.matches(actual)) {
                if (idxLoop > 0) {
                    mismatchDescription.appendText(DELIMITER);
                }
                propertyMatcher.describeMismatch(actual, mismatchDescription);
                idxLoop++;
                result = false;
            }
        }
        return result;
    }

    private boolean isCompatibleType(Object actual, Description mismatchDescription) {
        if (expectedBean.getClass().isAssignableFrom(actual.getClass())) {
            return true;
        }

        mismatchDescription.appendText(String.format(IS_INCOMPATIBLE_TYPE, actual.getClass().getSimpleName()));
        return false;
    }

    private boolean hasNoExtraProperties(Object actual, Description mismatchDescription) {
        Set<String> actualPropertyNames = propertyNamesFrom(propertyDescriptorsFor(actual, Object.class), ignoredFields);
        actualPropertyNames.removeAll(propertyNames);
        if (!actualPropertyNames.isEmpty()) {
            mismatchDescription.appendText(String.format(HAS_EXTRA_PROPERTIES_CALLED, actualPropertyNames));
            return false;
        }
        return true;
    }

    private static <T> List<PropertyMatcher<?>> propertyMatchersFor(T bean, PropertyDescriptor[] descriptors, List<String> ignoredFields) {
        List<PropertyMatcher<?>> result = new ArrayList<>(descriptors.length);
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            if (isIgnored(ignoredFields, propertyDescriptor)) {
                result.add(PropertyMatcher.matchProperty(propertyDescriptor, bean));
            }
        }
        return result;
    }

    private static Set<String> propertyNamesFrom(PropertyDescriptor[] descriptors, List<String> ignoredFields) {
        HashSet<String> result = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            if (isIgnored(ignoredFields, propertyDescriptor)) {
                result.add(propertyDescriptor.getDisplayName());
            }
        }
        return result;
    }

    private static boolean isIgnored(List<String> ignoredFields, PropertyDescriptor propertyDescriptor) {
        return !ignoredFields.contains(propertyDescriptor.getDisplayName());
    }

    private Object readProperty(Method method, Object target) {
        try {
            return method.invoke(target, NO_ARGUMENTS);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(COULD_NOT_INVOKE, method, target), e);
        }
    }
}
