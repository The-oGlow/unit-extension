package org.hamcrest.beans;

import org.hamcrest.AbstractExtendedMatcherTest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

/**
 * @param <T> type of the values
 *
 * @see SamePropertiesValuesAs
 */
public class SamePropertiesValuesAsTest<T extends SamePropertiesValuesAsTest.ExampleBean> extends AbstractExtendedMatcherTest<T> {

    protected static class ExampleValue {
        public ExampleValue(Object value) {
            this.value = value;
        }

        public final Object value;

        @Override
        public String toString() {
            return "ExampleValue{" +
                    "value=<" + value +
                    ">}";
        }
    }

    protected static class ExampleBean implements Cloneable {
        protected static final String FIELD_INT     = "intProperty";
        protected static final String FIELD_STRING  = "stringProperty";
        protected static final String FIELD_VALUE   = "valueProperty";
        protected static final String FIELD_UNKNOWN = "notAProperty";

        private final String       stringProperty;
        private final int          intProperty;
        private final ExampleValue valueProperty;

        public ExampleBean(String stringProperty, int intProperty, ExampleValue valueProperty) {
            this.stringProperty = stringProperty;
            this.intProperty = intProperty;
            this.valueProperty = valueProperty;
        }

        @Override
        public ExampleBean clone() {
            try {
                ExampleBean clone = (ExampleBean) super.clone();
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

        public String getStringProperty() {
            return stringProperty;
        }

        public int getIntProperty() {
            return intProperty;
        }

        public ExampleValue getValueProperty() {
            return valueProperty;
        }

        @Override
        public String toString() {
            return "ExampleBean{" +
                    "intProperty=<" + intProperty +
                    ">, stringProperty=\"" + stringProperty + '\"' +
                    ", valueProperty=<" + valueProperty +
                    ">}";
        }
    }

    protected static class SubBeanWithNoExtraProperties extends ExampleBean {
        public SubBeanWithNoExtraProperties(String stringProperty, int intProperty, ExampleValue valueProperty) {
            super(stringProperty, intProperty, valueProperty);
        }
    }

    protected static class SubBeanWithExtraProperty extends ExampleBean {
        protected static final String FIELD_EXTRA = "extraProperty";

        public SubBeanWithExtraProperty(String stringProperty, int intProperty, ExampleValue valueProperty) {
            super(stringProperty, intProperty, valueProperty);
        }

        @SuppressWarnings("unused")
        public String getExtraProperty() {
            return VAL_EXTRA;
        }
    }

    protected static final String DH                              = "\"";
    protected static final String MSG_WAS_OBJECT                  = "%s was <%s>";
    protected static final String MSG_WAS_STRING                  = "%s was \"%s\"";
    protected static final String MSG_IS_INCOMPATIBLE_TYPE        = "is incompatible type: %s";
    protected static final String MSG_HAS_EXTRA_PROPERTIES_CALLED = "has extra properties called [%s]";
    protected static final String MSG_SAME_PROPERTY_VALUES_AS     = "same property values as ";

    public static final    String VAL_EXPECTED               = "expected";
    public static final    String VAL_ACTUAL                 = "actual";
    public static final    String VAL_EXTRA                  = "extra";
    protected static final String VAL_EXAMPLE_BEAN_TO_STRING = "ExampleBean{intProperty=<1>, stringProperty=\"same\", valueProperty=<ExampleValue{value=<expected>}>}";

    protected static final ExampleValue DEFAULT_OBJECT = new ExampleValue(VAL_EXPECTED);
    protected static final String       DEFAULT_STRING = "same";
    protected static final int          DEFAULT_INT    = 1;

    protected static final ExampleValue DIFFERENT_OBJECT = new ExampleValue(VAL_ACTUAL);
    protected static final String       DIFFERENT_STRING = "DIFFERENT";
    protected static final int          DIFFERENT_INT    = 2;

    protected static final ExampleBean EXPECTED_BEAN    = new ExampleBean(DEFAULT_STRING, DEFAULT_INT, DEFAULT_OBJECT);
    protected static final ExampleBean ACTUAL_BEAN_SAME = new ExampleBean(DEFAULT_STRING, DEFAULT_INT, DEFAULT_OBJECT);
    protected static final ExampleBean ACTUAL_BEAN      = new ExampleBean(DIFFERENT_STRING, DIFFERENT_INT, DIFFERENT_OBJECT);

    @Override
    protected Matcher<T> createMatcher() {
        return SamePropertiesValuesAs.samePropertiesValuesAs(prepareArgumentInMatcher());
    }

    @Override
    protected T prepareArgumentInMatcher() {
        return (T) EXPECTED_BEAN;
    }

    @Override
    protected T prepareArgumentToCompareWith() {
        return (T) ACTUAL_BEAN;
    }

    /* Section for {@link org.hamcrest.BaseMatcher} unit tests */

    @Override
    protected Matcher<String> prepareMatcher_objectsAreDifferent_check() {
        return allOf( //
                containsString(String.format(MSG_WAS_OBJECT, ExampleBean.FIELD_INT, DIFFERENT_INT)), //
                containsString(String.format(MSG_WAS_STRING, ExampleBean.FIELD_STRING, DIFFERENT_STRING)), //
                containsString(String.format(MSG_WAS_OBJECT, ExampleBean.FIELD_VALUE, DIFFERENT_OBJECT)) //
        );
    }

    @Override
    protected Matcher<String> prepareMatcherDescriptionText_defaultDescription_check() {
        return containsString(String.format(SamePropertiesValuesAs.SAME_PROPERTY_VALUES_AS, EXPECTED_BEAN));
    }

    @Override
    protected Matcher<String> prepareDescriptionText_missmatchContent_check() {
        return containsString(DESCRIPTION_DEFAULT);
    }

    @Override
    protected Matcher<String> prepareDescriptionText_missmatchType_check() {
        return containsString(String.format(MSG_IS_INCOMPATIBLE_TYPE, DIFFERENT_CLAZZ_SIMPLE_NAME));
    }

    /* Section for {@link org.hamcrest.TypeSafeMatcher} unit tests */

    @Override
    protected Matcher<String> prepareMatcherDescriptionText_missmatchSafely_sameObject_check() {
        return null;
    }

    @Override
    protected Matcher<String> prepareMatcherDescriptionText_missmatchSafely_differentObject_check() {
        return null;
    }

    @Override
    protected Matcher<String> prepareMatcherDescriptionText_missmatchSafely_nullObject_check() {
        return null;
    }

    /* Section for individual unit tests */

    @Test
    public void test_describeTo_with_IgnoringFields_hasIgnoringField() {
        String[] ignoredProperies = new String[]{ExampleBean.FIELD_INT};
        Matcher<T> matcher = SamePropertiesValuesAs.samePropertiesValuesAs(prepareArgumentInMatcher(), ignoredProperies);

        Description description = prepareDefaultDescription();
        matcher.describeTo(description);
        assertThat(description.toString(),
                allOf(containsString(SamePropertiesValuesAs.SAME_PROPERTY_IGNORING), containsString(ExampleBean.FIELD_INT)));
    }
}
