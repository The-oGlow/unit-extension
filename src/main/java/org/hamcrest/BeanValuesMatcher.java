package org.hamcrest;

import org.hamcrest.beans.HasSameValues;

public class BeanValuesMatcher {

    private BeanValuesMatcher() {
    }

    public static <T> Matcher<T> haseSameValues(final T expectedBean) {
        return HasSameValues.hasSameValues(expectedBean);
    }

    /**
     * <strong> This matchers checks every property and list all failing
     * properties</strong>
     * <p>
     * Creates a matcher that matches when the examined object has values for all of
     * its JavaBean properties that are equal to the corresponding values of the
     * specified bean. If any properties are marked as ignored, they will be dropped
     * from both the expected and actual bean. Note that the ignored properties use
     * JavaBean display names, for example <pre>age</pre> rather than method names
     * such as <pre>getAge</pre>. For example:
     * <pre>assertThat(myBean, samePropertyValuesAs(myExpectedBean))</pre>
     * <pre>assertThat(myBean, samePropertyValuesAs(myExpectedBean), "age", "height")</pre>
     *
     * @param expectedBean      the bean against which examined beans are compared
     * @param ignoredProperties do not check any of these named properties.
     */
    public static <B> Matcher<B> samePropertyValuesAs(B expectedBean, String... ignoredProperties) {
        return org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs(expectedBean, ignoredProperties);
    }

}
