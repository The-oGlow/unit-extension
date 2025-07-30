package org.hamcrest.beans;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.beans.PropertyUtil.propertyDescriptorsFor;

public class SamePropertiesValuesAs<T> extends DiagnosingMatcher<T> {

    static final Description DESC_DESCRIPTION = new StringDescription();

    static final String HAS_EXTRA_PROPERTIES_CALLED = "has extra properties called %s "; // trailing space is needed
    static final String IS_INCOMPATIBLE_TYPE        = "is incompatible type: %s";
    static final String SAME_PROPERTY_VALUES_AS     = "same property values as %s";
    static final String SAME_PROPERTY_IGNORING      = " ignoring ";
    static final String LIST_START                  = "{";
    static final String LIST_END                    = "}";
    static final String LIST_DELIMITER              = ", ";

    private final T                        expectedBean;
    private final Set<String>              propertyNames;
    private final List<PropertyMatcher<?>> propertyMatchers;
    private final List<String>             ignoredFields;

    private SamePropertiesValuesAs(T expectedBean, List<String> ignoredProperties) {
        verifyInput(expectedBean);

        PropertyDescriptor[] descriptors = propertyDescriptorsFor(expectedBean, Object.class);
        this.expectedBean = expectedBean;
        this.ignoredFields = Collections.unmodifiableList(ignoredProperties);
        this.propertyNames = propertyNamesFrom(descriptors, ignoredProperties);
        this.propertyMatchers = propertyMatchersFor(expectedBean, descriptors, ignoredProperties);
    }

    public static <T> Matcher<T> samePropertiesValuesAs(T expectedBean, String... ignoredProperties) {
        return new SamePropertiesValuesAs<>(expectedBean, asList(ignoredProperties));
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
                .appendList(LIST_START, LIST_DELIMITER, LIST_END, propertyMatchers);

        if (!ignoredFields.isEmpty()) {
            description.appendText(SAME_PROPERTY_IGNORING) //
                    .appendValueList(LIST_START, LIST_DELIMITER, LIST_END, ignoredFields);
        }
    }

    boolean hasAllMatchingValues(Object actual, Description mismatchDescription) {
        boolean result = true;
        int idxLoop = 0;
        for (Matcher<?> propertyMatcher : propertyMatchers) {
            if (!propertyMatcher.matches(actual)) {
                if (idxLoop > 0) {
                    mismatchDescription.appendText(LIST_DELIMITER);
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
        Set<String> ignoredPropertiesInThisObject = propertyNamesFrom(propertyDescriptorsFor(actual, Object.class), ignoredFields);
        ignoredPropertiesInThisObject.removeAll(propertyNames);
        if (!ignoredPropertiesInThisObject.isEmpty()) {
            // Found properties, which are not in the ignore list from object construction
            mismatchDescription.appendText(String.format(HAS_EXTRA_PROPERTIES_CALLED, ignoredPropertiesInThisObject));
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

    /**
     * @param descriptors   array of property descriptors
     * @param ignoredFields list of ignoring fields
     *
     * @return set of the property descriptor names which are ignored
     */
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
}
