package org.hamcrest.beans;

import static org.hamcrest.beans.SamePropertiesValuesAs.samePropertiesValuesAs;

import org.hamcrest.AbstractMatcherTest;
import org.hamcrest.Matcher;

@SuppressWarnings("WeakerAccess")
public class SamePropertiesValuesAsTest2 extends AbstractMatcherTest {
    private static final Value       aValue       = new Value("expected");
    private static final ExampleBean expectedBean = new ExampleBean("same", 1, aValue);
    private static final ExampleBean actualBean   = new ExampleBean("same", 1, aValue);

    @Override
    protected Matcher<?> createMatcher() {
        return samePropertiesValuesAs(expectedBean);
    }

    public void test_reports_match_when_all_properties_match() {
        assertMatches("matched properties", samePropertiesValuesAs(expectedBean), actualBean);
    }

    public void test_reports_mismatch_when_actual_type_is_not_assignable_to_expected_type() {
        assertMismatchDescription("is incompatible type: ExampleBean", samePropertiesValuesAs((Object) aValue), actualBean);
    }

    public void test_reports_mismatch_on_two_properties_difference() {
        assertMismatchDescription("stringProperty was \"different\"", samePropertiesValuesAs(expectedBean), new ExampleBean("different", 1, aValue));
        assertMismatchDescription("intProperty was <2>", samePropertiesValuesAs(expectedBean), new ExampleBean("Notsame", 3, aValue));
        assertMismatchDescription("valueProperty was <Value other>", samePropertiesValuesAs(expectedBean), new ExampleBean("same", 1, new Value("other")));
    }

    public void test_reports_mismatch_on_first_property_difference() {
        assertMismatchDescription("stringProperty was \"different\"", samePropertiesValuesAs(expectedBean), new ExampleBean("different", 1, aValue));
        assertMismatchDescription("intProperty was <2>", samePropertiesValuesAs(expectedBean), new ExampleBean("same", 2, aValue));
        assertMismatchDescription("valueProperty was <Value other>", samePropertiesValuesAs(expectedBean), new ExampleBean("same", 1, new Value("other")));
    }

    public void test_matches_beans_with_inheritance_but_no_extra_properties() {
        assertMatches("sub type with same properties", samePropertiesValuesAs(expectedBean), new SubBeanWithNoExtraProperties("same", 1, aValue));
    }

    public void test_rejects_subtype_that_has_extra_properties() {
        assertMismatchDescription("has extra properties called [extraProperty]", samePropertiesValuesAs(expectedBean),
                new SubBeanWithExtraProperty("same", 1, aValue));
    }

    public void test_ignores_extra_subtype_properties() {
        final SubBeanWithExtraProperty withExtraProperty = new SubBeanWithExtraProperty("same", 1, aValue);
        assertMatches("extra property", samePropertiesValuesAs(expectedBean, "extraProperty"), withExtraProperty);
    }

    public void test_ignores_different_properties() {
        final ExampleBean differentBean = new ExampleBean("different", 1, aValue);
        assertMatches("different property", samePropertiesValuesAs(expectedBean, "stringProperty"), differentBean);
    }

    public void test_accepts_missing_properties_to_ignore() {
        assertMatches("ignored property", samePropertiesValuesAs(expectedBean, "notAProperty"), actualBean);
    }

    public void test_can_ignore_all_properties() {
        final ExampleBean differentBean = new ExampleBean("different", 2, new Value("not expected"));
        assertMatches("different property", samePropertiesValuesAs(expectedBean, "stringProperty", "intProperty", "valueProperty"), differentBean);
    }

    public void testDescribesItself() {
        assertDescription("same property values as ExampleBean [intProperty: <1>, stringProperty: \"same\", valueProperty: <Value expected>]",
                samePropertiesValuesAs(expectedBean));

        assertDescription(
                "same property values as ExampleBean [intProperty: <1>, stringProperty: \"same\", valueProperty: <Value expected>] ignoring [\"ignored1\", \"ignored2\"]",
                samePropertiesValuesAs(expectedBean, "ignored1", "ignored2"));
    }

    public static class Value {
        public Value(Object value) {
            this.value = value;
        }

        public final Object value;

        @Override
        public String toString() {
            return "Value " + value;
        }
    }

    @SuppressWarnings("unused")
    public static class ExampleBean {
        private String stringProperty;
        private int    intProperty;
        private Value  valueProperty;

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
            return "an ExampleBean";
        }
    }

    public static class SubBeanWithNoExtraProperties extends ExampleBean {
        public SubBeanWithNoExtraProperties(String stringProperty, int intProperty, Value valueProperty) {
            super(stringProperty, intProperty, valueProperty);
        }
    }

    public static class SubBeanWithExtraProperty extends ExampleBean {
        public SubBeanWithExtraProperty(String stringProperty, int intProperty, Value valueProperty) {
            super(stringProperty, intProperty, valueProperty);
        }

        @SuppressWarnings("unused")
        public String getExtraProperty() {
            return "extra";
        }
    }
}
