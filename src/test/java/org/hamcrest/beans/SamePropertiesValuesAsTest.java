package org.hamcrest.beans;

import org.hamcrest.ExtendedMatcherTest;
import org.hamcrest.Matcher;

public class SamePropertiesValuesAsTest<T extends ExampleBean> extends ExtendedMatcherTest<T> {

    private static final ExampleBean.Value aValue       = new ExampleBean.Value("expected");
    private static final ExampleBean       expectedBean = new ExampleBean("same", 1, aValue);
    private static final ExampleBean       actualBean   = new ExampleBean("same", 1, aValue);

    @Override
    protected Matcher<T> createMatcher() {
        return SamePropertiesValuesAs.samePropertiesValuesAs(prepareArgumentInMatcher());
    }

    @Override
    protected T prepareArgumentInMatcher() {
        return (T) expectedBean;
    }

    @Override
    protected T prepareArgumentToCompareWith() {
        return (T) actualBean;
    }

    @Override
    protected Matcher<String> prepareMatcher_objectsAreDifferent_check() {
        return null;
    }

    @Override
    protected Matcher<String> prepareMatcherDescriptionText_defaultDescription_check() {
        return null;
    }

    @Override
    protected Matcher<String> prepareDescriptionText_missmatchContent_check() {
        return null;
    }

    @Override
    protected Matcher<String> prepareDescriptionText_missmatchType_check() {
        return null;
    }

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
        assertMatches("matched properties", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean), actualBean);
    }

    public void test_reports_mismatch_when_actual_type_is_not_assignable_to_expected_type() {
        assertMismatchDescription("is incompatible type: ExampleBean", SamePropertiesValuesAs.samePropertiesValuesAs((Object) aValue), actualBean);
    }

    public void test_reports_mismatch_on_two_properties_difference() {
        assertMismatchDescription("stringProperty was \"different\"", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean), new ExampleBean("different", 1, aValue));
        assertMismatchDescription("intProperty was <3>", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean), new ExampleBean("Notsame", 3, aValue));
        assertMismatchDescription("valueProperty was <Value other>", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean), new ExampleBean("same", 1, new ExampleBean.Value("other")));
    }

    public void test_reports_mismatch_on_first_property_difference() {
        assertMismatchDescription("stringProperty was \"different\"", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean), new ExampleBean("different", 1, aValue));
        assertMismatchDescription("intProperty was <2>", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean), new ExampleBean("same", 2, aValue));
        assertMismatchDescription("valueProperty was <Value other>", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean), new ExampleBean("same", 1, new ExampleBean.Value("other")));
    }

    public void test_matches_beans_with_inheritance_but_no_extra_properties() {
        assertMatches("sub type with same properties", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean), new SubBeanWithNoExtraProperties("same", 1, aValue));
    }

    public void test_rejects_subtype_that_has_extra_properties() {
        assertMismatchDescription("has extra properties called [extraProperty]", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean),
                new SubBeanWithExtraProperty("same", 1, aValue));
    }

    public void test_ignores_extra_subtype_properties() {
        final SubBeanWithExtraProperty withExtraProperty = new SubBeanWithExtraProperty("same", 1, aValue);
        assertMatches("extra property", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean, "extraProperty"), withExtraProperty);
    }

    public void test_ignores_different_properties() {
        final ExampleBean differentBean = new ExampleBean("different", 1, aValue);
        assertMatches("different property", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean, "stringProperty"), differentBean);
    }

    public void test_accepts_missing_properties_to_ignore() {
        assertMatches("ignored property", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean, "notAProperty"), actualBean);
    }

    public void test_can_ignore_all_properties() {
        final ExampleBean differentBean = new ExampleBean("different", 2, new ExampleBean.Value("not expected"));
        assertMatches("different property", SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean, "stringProperty", "intProperty", "valueProperty"), differentBean);
    }

    public void testDescribesItself() {
        assertDescription("same property values as ExampleBean [intProperty: <1>, stringProperty: \"same\", valueProperty: <Value expected>]",
                SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean));

        assertDescription(
                "same property values as ExampleBean [intProperty: <1>, stringProperty: \"same\", valueProperty: <Value expected>] ignoring [\"ignored1\", \"ignored2\"]",
                SamePropertiesValuesAs.samePropertiesValuesAs(expectedBean, "ignored1", "ignored2"));
    }
}

class ExampleBean {
    static class Value {
        public Value(Object value) {
            this.value = value;
        }

        public final Object value;

        @Override
        public String toString() {
            return "Value " + value;
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
                "intProperty=" + intProperty +
                ", stringProperty='" + stringProperty + '\'' +
                ", valueProperty=" + valueProperty +
                '}';
    }
}

class SubBeanWithNoExtraProperties extends ExampleBean {
    public SubBeanWithNoExtraProperties(String stringProperty, int intProperty, Value valueProperty) {
        super(stringProperty, intProperty, valueProperty);
    }
}

class SubBeanWithExtraProperty extends ExampleBean {
    public SubBeanWithExtraProperty(String stringProperty, int intProperty, Value valueProperty) {
        super(stringProperty, intProperty, valueProperty);
    }

    @SuppressWarnings("unused")
    public String getExtraProperty() {
        return "extra";
    }
}
