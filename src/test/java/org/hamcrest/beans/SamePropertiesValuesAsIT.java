package org.hamcrest.beans;

import com.glowanet.util.reflect.ReflectionHelper;
import org.junit.Test;

public class SamePropertiesValuesAsIT<T extends SamePropertiesValuesAsTest.ExampleBean> extends SamePropertiesValuesAsTest<T> {

    protected static final String IGNORED_1 = "ignored1";
    protected static final String IGNORED_2 = "ignored2";

    @Test
    public void test_allProperties_match_matches() {
        assertMatches(SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), ACTUAL_BEAN_SAME);
    }

    @Test
    public void test_oneProperty_missmatch_notMatches() {
        assertDoesNotMatch(SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), ACTUAL_BEAN);
    }

    @Test
    public void test_actualType_notAssignableToExpectedType_notMatches() {
        assertMismatchDescription(String.format(MSG_IS_INCOMPATIBLE_TYPE, ExampleValue.class.getSimpleName()),
                SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), DEFAULT_OBJECT);
    }

    @Test
    public void test_firstProperty_differ_notMatches() {
        final ExampleBean differentBean = EXPECTED_BEAN.clone();
        ReflectionHelper.writeField(ExampleBean.FIELD_STRING, differentBean, DIFFERENT_STRING);

        assertMismatchDescription(String.format(MSG_WAS_STRING, ExampleBean.FIELD_STRING, DIFFERENT_STRING),
                SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), differentBean);
    }

    @Test
    public void test_secondProperty_differ_notMatches() {
        final ExampleBean differentBean = EXPECTED_BEAN.clone();
        ReflectionHelper.writeField(ExampleBean.FIELD_INT, differentBean, DIFFERENT_INT);

        assertMismatchDescription(String.format(MSG_WAS_OBJECT, ExampleBean.FIELD_INT, DIFFERENT_INT),
                SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), differentBean);
    }

    @Test
    public void test_thirdProperty_differ_notMatches() {
        final ExampleBean differentBean = EXPECTED_BEAN.clone();
        ReflectionHelper.writeField(ExampleBean.FIELD_VALUE, differentBean, DIFFERENT_OBJECT);

        assertMismatchDescription(String.format(MSG_WAS_OBJECT, ExampleBean.FIELD_VALUE, DIFFERENT_OBJECT),
                SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN), differentBean);
    }

    @Test
    public void test_subTypeBeanNoExtraProperty_matches() {
        assertMatches(SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN),
                new SubBeanWithNoExtraProperties(DEFAULT_STRING, DEFAULT_INT, DEFAULT_OBJECT));
    }

    @Test
    public void test_subTypeBeanWithExtraProperty_notMatches() {
        assertMismatchDescription(
                String.format(MSG_HAS_EXTRA_PROPERTIES_CALLED, SubBeanWithExtraProperty.FIELD_EXTRA),
                SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN),
                new SubBeanWithExtraProperty(DEFAULT_STRING, DEFAULT_INT, DEFAULT_OBJECT));
    }

    @Test
    public void test_subTypeBeanWithExtraPropertyIsIgnored_notMatches() {
        assertMatches(SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN, SubBeanWithExtraProperty.FIELD_EXTRA),
                new SubBeanWithExtraProperty(DEFAULT_STRING, DEFAULT_INT, DEFAULT_OBJECT));
    }

    @Test
    public void test_ignoreOneDifferentField_matches() {
        final ExampleBean differentBean = EXPECTED_BEAN.clone();
        ReflectionHelper.writeField(ExampleBean.FIELD_STRING, differentBean, DIFFERENT_STRING);
        assertMatches(SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN, ExampleBean.FIELD_STRING), differentBean);
    }

    @Test
    public void test_ignoreAllDifferentFields_matches() {
        String[] ignoreFields = new String[]{ExampleBean.FIELD_STRING, ExampleBean.FIELD_INT, ExampleBean.FIELD_VALUE};
        assertMatches(SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN, ignoreFields), ACTUAL_BEAN);
    }

    @Test
    public void test_ignoreUnknownField_notMatches() {
        String[] ignoreFields = new String[]{ExampleBean.FIELD_UNKNOWN};
        assertDoesNotMatch(SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN, ignoreFields), ACTUAL_BEAN);
    }

    @Test
    public void test_describesTo_itself_matches() {
        String expectedText = MSG_SAME_PROPERTY_VALUES_AS + VAL_EXAMPLE_BEAN_TO_STRING;

        assertDescription(expectedText, SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN));

        String ignoringText = SamePropertiesValuesAs.SAME_PROPERTY_IGNORING +
                SamePropertiesValuesAs.LIST_START + DH + IGNORED_1 + DH + SamePropertiesValuesAs.LIST_DELIMITER +
                DH + IGNORED_2 + DH + SamePropertiesValuesAs.LIST_END;

        assertDescription(expectedText + ignoringText,
                SamePropertiesValuesAs.samePropertiesValuesAs(EXPECTED_BEAN, IGNORED_1, IGNORED_2));
    }
}
