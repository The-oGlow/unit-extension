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

public class SamePropertiesValuesAs<T> extends DiagnosingMatcher<T> { // extends SamePropertyValuesAs<T> {

    //    static final Description            DESC_MISMATCH    = new StringDescription();
    static final Description DESC_DESCRIPTION = new StringDescription();

    private final T                     expectedBean;
    private final Set<String>           propertyNames;
    private final List<PropertyMatcher> propertyMatchers;
    private final List<String>          ignoredFields;

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

        initDescription();
    }

    private void verifyInput(final T expectedBean) {
        assertThat(expectedBean, notNullValue());
    }

    private void initDescription() {
        DESC_DESCRIPTION.appendText(System.lineSeparator()).appendText("The bean must have the total same content");
    }

    @Override
    protected boolean matches(Object actual, Description mismatch) {
        return isNotNull(actual, mismatch) && isCompatibleType(actual, mismatch) && hasNoExtraProperties(actual, mismatch)
                && hasAllMatchingValues(actual, mismatch);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("same property values as " //
                + expectedBean.getClass().getSimpleName()) //
                .appendList(" [", ", ", "]", propertyMatchers);

        if (!ignoredFields.isEmpty()) {
            description.appendText(" ignoring ") //
                    .appendValueList("[", ", ", "]", ignoredFields);
        }
    }

    protected boolean hasAllMatchingValues(Object actual, Description mismatchDescription) {
        boolean result = true;
        for (Matcher<?> propertyMatcher : propertyMatchers) {
            if (!propertyMatcher.matches(actual)) {
                propertyMatcher.describeMismatch(actual, mismatchDescription);
                result = false;
            }
        }
        return result;
    }

    private boolean isCompatibleType(Object actual, Description mismatchDescription) {
        if (expectedBean.getClass().isAssignableFrom(actual.getClass())) {
            return true;
        }

        mismatchDescription.appendText("is incompatible type: " + actual.getClass().getSimpleName());
        return false;
    }

    private boolean hasNoExtraProperties(Object actual, Description mismatchDescription) {
        Set<String> actualPropertyNames = propertyNamesFrom(propertyDescriptorsFor(actual, Object.class), ignoredFields);
        actualPropertyNames.removeAll(propertyNames);
        if (!actualPropertyNames.isEmpty()) {
            mismatchDescription.appendText("has extra properties called " + actualPropertyNames);
            return false;
        }
        return true;
    }

    private static <T> List<PropertyMatcher> propertyMatchersFor(T bean, PropertyDescriptor[] descriptors, List<String> ignoredFields) {
        List<PropertyMatcher> result = new ArrayList<>(descriptors.length);
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
            throw new IllegalArgumentException("Could not invoke " + method + " on " + target, e);
        }
    }
}
