package org.hamcrest;

import org.hamcrest.beans.HasSameValues;

public class BeanValuesMatcher {

    /**
     * Singleton with only static methods has no public constructor.
     */
    private BeanValuesMatcher() {
    }

    /**
     * @param expectedBean the bean against which examined beans are compared
     * @param <B>          the type of {@code expectedBean}
     *
     * @return newly created matcher
     */
    public static <B> Matcher<B> hasSameValues(B expectedBean) {
        return HasSameValues.hasSameValues(expectedBean);
    }

    /**
     * <strong> This matchers checks every property and list all failing properties</strong>
     * <p>
     * Creates a matcher that matches when the examined object has values for all of its JavaBean properties
     * that are equal to the corresponding values of the specified bean. If any properties are marked as ignored,
     * they will be dropped from both the expected and actual bean. Note that the ignored properties use JavaBean
     * display names, for example <pre>age</pre> rather than method names such as <pre>getAge</pre>.
     * <p>
     * For example:
     * <pre>assertThat(myBean, samePropertiesValuesAs(myExpectedBean))</pre>
     * <pre>assertThat(myBean, samePropertiesValuesAs(myExpectedBean), "age", "height")</pre>
     *
     * @param expectedBean      the bean against which examined beans are compared
     * @param ignoredProperties do not check any of these named properties.
     * @param <B>               the type of {@code expectedBean}
     *
     * @return newly created matcher
     */
    public static <B> Matcher<B> samePropertiesValuesAs(B expectedBean, String... ignoredProperties) {
        return org.hamcrest.beans.SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean, ignoredProperties);
    }

}
