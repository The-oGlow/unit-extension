package org.hamcrest.beans;

import org.hamcrest.AbstractExtendedMatcherTest;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

/**
 * @param <T> type of the values
 *
 * @see SamePropertiesValuesAs
 */
public class SamePropertiesValuesAsTest<T extends SamePropertiesValuesAsTest.ExampleBean> extends AbstractExtendedMatcherTest<T> {

    static class ExampleBean {
        static class Value {
            public Value(Object value) {
                this.value = value;
            }

            public final Object value;

            @Override
            public String toString() {
                return "Value{" +
                        "value=<" + value +
                        ">}";
            }
        }

        private final String stringProperty;
        private final int    intProperty;
        private final Value  valueProperty;

        public ExampleBean(String stringProperty, int intProperty, Value valueProperty) {
            this.stringProperty = stringProperty;
            this.intProperty = intProperty;
            this.valueProperty = valueProperty;
        }

        public String getStringProperty() {
            return stringProperty;
        }

        public int getIntProperty() {
            return intProperty;
        }

        public Value getValueProperty() {
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

    static class SubBeanWithNoExtraProperties extends ExampleBean {
        public SubBeanWithNoExtraProperties(String stringProperty, int intProperty, Value valueProperty) {
            super(stringProperty, intProperty, valueProperty);
        }
    }

    static class SubBeanWithExtraProperty extends ExampleBean {
        public SubBeanWithExtraProperty(String stringProperty, int intProperty, Value valueProperty) {
            super(stringProperty, intProperty, valueProperty);
        }

        @SuppressWarnings("unused")
        public String getExtraProperty() {
            return "extra";
        }
    }

    //FIXME: This must be unified
    protected static final String FIELD_INT    = "intProperty";
    protected static final String FIELD_STRING = "stringProperty";
    protected static final String FIELD_VALUE  = "valueProperty";

    private static final ExampleBean.Value DEFAULT_OBJECT = new ExampleBean.Value("expected");
    private static final String            DEFAULT_STRING = "same";
    private static final int               DEFAULT_INT    = 1;
    private static final ExampleBean       EXPECTED_BEAN  = new ExampleBean(DEFAULT_STRING, DEFAULT_INT, DEFAULT_OBJECT);

    private static final ExampleBean.Value DIFFERENT_OBJECT = new ExampleBean.Value("actual");
    private static final String            DIFFERENT_STRING = "DIFFERENT";
    private static final int               DIFFERENT_INT    = 2;
    private static final ExampleBean       ACTUAL_BEAN      = new ExampleBean(DIFFERENT_STRING, DIFFERENT_INT, DIFFERENT_OBJECT);

    protected static final String TXT_MATCHED_PROPERTIES          = "matched properties";
    protected static final String TXT_IS_INCOMPATIBLE_TYPE        = "is incompatible type:";
    protected static final String TXT_DIFFERENT_PROPERTY          = "different property";
    protected static final String TXT_IGNORED_PROPERTY            = "ignored property";
    protected static final String TXT_SAME_PROPERTY_VALUES_AS     = "same property values as ";
    protected static final String TXT_EXTRA_PROPERTY              = "extra property";
    protected static final String TXT_HAS_EXTRA_PROPERTIES_CALLED = "has extra properties called";

    protected static final String VAL_NOT_EXPECTED           = "not expected";
    protected static final String VAL_NOT_A_PROPERTY         = "notAProperty";
    protected static final String VAL_DIFFERENT              = "different";
    protected static final String VAL_OTHER                  = "other";
    protected static final String VAL_NOTSAME                = "Notsame";
    protected static final String VAL_EXTRA_PROPERTY         = "extraProperty";
    protected static final String VAL_EXAMPLE_BEAN           = "ExampleBean";
    protected static final String VAL_EXAMPLE_BEAN_TO_STRING = "ExampleBean [intProperty: <1>, stringProperty: \"same\", valueProperty: <Value expected>]";

    //FIXME: This must be unnified

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
                containsString(String.format("%s was <%s>,", FIELD_INT, DIFFERENT_INT)), //
                containsString(String.format("%s was \"%s\",", FIELD_STRING, DIFFERENT_STRING)), //
                containsString(String.format("%s was <%s>", FIELD_VALUE, DIFFERENT_OBJECT)) //
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
        return allOf( //
                containsString(TXT_IS_INCOMPATIBLE_TYPE), //
                containsString(DIFFERENT_CLAZZ_SIMPLE_NAME) //
        );
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

    public void test_reports_match_when_all_properties_match() {
        assertMatches(TXT_MATCHED_PROPERTIES, SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), ACTUAL_BEAN);
    }

    public void test_reports_mismatch_when_actual_type_is_not_assignable_to_expected_type() {
        assertMismatchDescription(TXT_IS_INCOMPATIBLE_TYPE + " " + VAL_EXAMPLE_BEAN, SamePropertiesValuesAs.samePropertiesValuesAs((Object) DEFAULT_OBJECT), ACTUAL_BEAN);
    }

    public void test_reports_mismatch_on_two_properties_difference() {
        assertMismatchDescription(FIELD_STRING + " was \"different\"", SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), new ExampleBean(VAL_DIFFERENT, DEFAULT_INT, DEFAULT_OBJECT));
        assertMismatchDescription(FIELD_INT + " was <3>", SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), new ExampleBean(VAL_NOTSAME, DIFFERENT_INT, DEFAULT_OBJECT));
        assertMismatchDescription(FIELD_VALUE + " was <Value other>", SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), new ExampleBean(DEFAULT_STRING, DEFAULT_INT, new ExampleBean.Value(VAL_OTHER)));
    }

    public void test_reports_mismatch_on_first_property_difference() {
        assertMismatchDescription(FIELD_STRING + " was \"different\"", SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), new ExampleBean(VAL_DIFFERENT, DEFAULT_INT, DEFAULT_OBJECT));
        assertMismatchDescription(FIELD_INT + " was <2>", SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), new ExampleBean(DEFAULT_STRING, DIFFERENT_INT, DEFAULT_OBJECT));
        assertMismatchDescription(FIELD_VALUE + " was <Value other>", SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), new ExampleBean(DEFAULT_STRING, DEFAULT_INT, new ExampleBean.Value(VAL_OTHER)));
    }

    public void test_matches_beans_with_inheritance_but_no_extra_properties() {
        assertMatches("sub type with same properties", SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), new SubBeanWithNoExtraProperties(DEFAULT_STRING, DEFAULT_INT, DEFAULT_OBJECT));
    }

    public void test_rejects_subtype_that_has_extra_properties() {
        assertMismatchDescription(TXT_HAS_EXTRA_PROPERTIES_CALLED + " [" + VAL_EXTRA_PROPERTY + "]", SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN),
                new SubBeanWithExtraProperty(DEFAULT_STRING, DEFAULT_INT, DEFAULT_OBJECT));
    }

    @Test
    public void test_ignores_extra_subtype_properties() {
        final SubBeanWithExtraProperty withExtraProperty = new SubBeanWithExtraProperty(DEFAULT_STRING, DEFAULT_INT, DEFAULT_OBJECT);
        assertMatches(TXT_EXTRA_PROPERTY, SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN, VAL_EXTRA_PROPERTY), withExtraProperty);
    }

    public void test_ignores_different_properties() {
        final ExampleBean differentBean = new ExampleBean(VAL_DIFFERENT, DEFAULT_INT, DEFAULT_OBJECT);
        assertMatches(TXT_DIFFERENT_PROPERTY, SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN, FIELD_STRING), differentBean);
    }

    public void test_accepts_missing_properties_to_ignore() {
        assertMatches(TXT_IGNORED_PROPERTY, SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN, VAL_NOT_A_PROPERTY), ACTUAL_BEAN);
    }

    public void test_can_ignore_all_properties() {
        final ExampleBean differentBean = new ExampleBean(VAL_DIFFERENT, DIFFERENT_INT, new ExampleBean.Value(VAL_NOT_EXPECTED));
        assertMatches(TXT_DIFFERENT_PROPERTY, SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN, FIELD_STRING, FIELD_INT, FIELD_VALUE), differentBean);
    }

    public void testDescribesItself() {
        assertDescription(TXT_SAME_PROPERTY_VALUES_AS + VAL_EXAMPLE_BEAN_TO_STRING,
                SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN));

        assertDescription(
                TXT_SAME_PROPERTY_VALUES_AS + VAL_EXAMPLE_BEAN_TO_STRING + " ignoring [\"ignored1\", \"ignored2\"]",
                SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN, "ignored1", "ignored2"));
    }
}
