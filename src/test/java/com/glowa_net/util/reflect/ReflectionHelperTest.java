package com.glowa_net.util.reflect;

import com.glowa_net.data.SimplePojo;
import com.glowa_net.data.SimpleSerializable;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ReflectionHelperTest {


    public static final String   SIMPLE_STRING_NAME  = "simpleString";
    public static final Class<?> SIMPLE_STRING_CLAZZ = String.class;
    public static final String   SIMPLE_STRING_VALUE = "simpleText";
    public static final String   GET_SIMPLE_STRING   = "getSimpleString";

    public static final String   SIMPLE_INT_NAME  = "simpleInt";
    public static final Class<?> SIMPLE_INT_CLAZZ = int.class;
    public static final int      SIMPLE_INT_VALUE = 127;

    public static final String   CONST_FLOAT_NAME  = "CONST_FLOAT";
    public static final Class<?> CONST_FLOAT_CLAZZ = Float.class;
    public static final Float    CONST_FLOAT_VALUE = SimplePojo.CONST_FLOAT;

    public static final String NOT_FOUND = "notFound";

    private SimplePojo pojo;

    @Before
    public void setUp() {
        pojo = new SimplePojo();
        pojo.setSimpleString(SIMPLE_STRING_VALUE);
        pojo.setSimpleInt(SIMPLE_INT_VALUE);
    }


    private <V> void assertNullValid(final V actual) {
        assertThat(actual, nullValue());
    }

    private <V> void assertValid(final V actual, final V expected) {
        assertValid(actual, expected, actual == null ? null : actual.getClass());
    }

    @SuppressWarnings("unchecked")
    private <V> void assertValid(final V actual, final V expected, final Class<?> actualClazz) {
        final Matcher<V> expectedMatcher = expected instanceof Matcher ? (Matcher<V>) expected : equalTo(expected);
        assertThat(actual, notNullValue());
        assertThat(actual, instanceOf(actualClazz));
        assertThat(actual, expectedMatcher);
    }

    @Test
    public void test_findGetter_with_instance_return_listOfFields() {
        final List<PropertyDescriptor> actual = ReflectionHelper.findGetter(pojo);
        assertValid(actual, hasSize(SimplePojo.GETTER_COUNT));
    }

    @Test(expected = AssertionError.class)
    public void test_findGetter_with_null_fails() {
        ReflectionHelper.findGetter(null);
    }

    @Test
    public void test_findSetter_with_instance_return_emptyList() {
        final List<PropertyDescriptor> actual = ReflectionHelper.findSetter(pojo);
        assertValid(actual, hasSize(0));
    }

    @Test
    public void test_findSetter_with_null_return_emptyList() {
        final List<PropertyDescriptor> actual = ReflectionHelper.findSetter(null);
        assertValid(actual, hasSize(0));
    }

    @Test
    public void test_hasSerializableIF_with_noneSerializable_return_false() {
        final boolean actual = ReflectionHelper.hasSerializableIF(pojo.getClass());
        assertValid(actual, false);
    }

    @Test
    public void test_hasSerializableIF_with_serializable_return_true() {
        final boolean actual = ReflectionHelper.hasSerializableIF(SimpleSerializable.class);
        assertValid(actual, true);
    }

    @Test
    public void test_findField_with_fieldNameAndInstance_return_field() {
        final String fieldName = SIMPLE_STRING_NAME;
        final Field actual = ReflectionHelper.findField(fieldName, pojo);
        assertValid(actual.getName(), fieldName);
    }

    @Test
    public void test_findField_with_fieldNameNotFoundAndInstance_return_null() {
        final Field actual = ReflectionHelper.findField(NOT_FOUND, pojo);
        assertNullValid(actual);
    }

    @Test(expected = AssertionError.class)
    public void test_findField_with_fieldNameNullAndInstance_fails() {
        ReflectionHelper.findField(null, pojo);
    }

    @Test
    public void test_findField_with_fieldNameAndClazz_return_field() {
        final String fieldName = SIMPLE_STRING_NAME;
        final Field actual = ReflectionHelper.findField(fieldName, pojo.getClass());
        assertValid(actual.getName(), fieldName);
    }

    @Test
    public void test_findField_with_fieldNameNotFoundAndClazz_return_null() {
        final Field actual = ReflectionHelper.findField(NOT_FOUND, pojo.getClass());
        assertNullValid(actual);
    }

    @Test(expected = AssertionError.class)
    public void test_findField_with_fieldNameNullAndClazz_fails() {
        ReflectionHelper.findField(null, pojo.getClass());
    }

    @Test(expected = AssertionError.class)
    public void test_findField_with_fieldNameAndInstanceNull_fails() {
        ReflectionHelper.findField(SIMPLE_STRING_NAME, (Object) null);
    }

    @SuppressWarnings("RedundantCast")
    @Test(expected = AssertionError.class)
    public void test_findField_with_fieldNameAndClazzNull_fails() {
        ReflectionHelper.findField(SIMPLE_STRING_NAME, (Class<?>) null);
    }

    @Test
    public void test_readField_with_fieldNameAndInstance_return_value() {
        final Object actual = ReflectionHelper.readField(SIMPLE_INT_NAME, pojo);
        assertValid(actual, SIMPLE_INT_VALUE, SIMPLE_INT_CLAZZ);
    }

    @Test
    public void test_readField_with_fieldNameConstantAndInstance_return_value() {
        final Object actual = ReflectionHelper.readField(CONST_FLOAT_NAME, pojo);
        assertValid(actual, CONST_FLOAT_VALUE, CONST_FLOAT_CLAZZ);
    }

    @Test(expected = AssertionError.class)
    public void test_readField_with_fieldNameVariableAndClazz_fails() {
        ReflectionHelper.readField(SIMPLE_INT_NAME, pojo.getClass());
    }

    @Test
    public void test_readField_with_fieldNameConstantAndClazz_return_value() {
        final Object actual = ReflectionHelper.readField(CONST_FLOAT_NAME, pojo.getClass());
        assertValid(actual, CONST_FLOAT_VALUE, CONST_FLOAT_CLAZZ);
    }

    @Test
    public void test_readField_with_fieldVariableAndInstance_return_value() {
        final Field field = ReflectionHelper.findField(SIMPLE_STRING_NAME, pojo);
        final Object actual = ReflectionHelper.readField(field, pojo);
        assertValid(actual, SIMPLE_STRING_VALUE, SIMPLE_STRING_CLAZZ);
    }

    @Test
    public void test_readField_with_fieldConstantAndClazz_return_value() {
        final Field field = ReflectionHelper.findField(CONST_FLOAT_NAME, pojo);
        final Object actual = ReflectionHelper.readField(field, pojo.getClass());
        assertValid(actual, SimplePojo.CONST_FLOAT, CONST_FLOAT_CLAZZ);
    }

    @Test(expected = AssertionError.class)
    public void test_readField_with_fieldNameNullAndInstance_fails() {
        ReflectionHelper.readField((String) null, pojo);
    }

    @Test(expected = AssertionError.class)
    public void test_readField_with_fieldNullAndInstance_fails() {
        ReflectionHelper.readField((Field) null, pojo);
    }

    @Test
    public void test_makeFieldAccessible_with_fieldNameAndInstance_makeAccessible() {
        final Field field = ReflectionHelper.makeFieldAccessible(SIMPLE_STRING_NAME, pojo);
        assertValid(field.canAccess(pojo), true);
    }

    @Test(expected = AssertionError.class)
    public void test_makeFieldAccessible_withFieldNameNotFoundAndInstance_fails() {
        ReflectionHelper.makeFieldAccessible(NOT_FOUND, pojo);
    }

    @Test(expected = AssertionError.class)
    public void test_makeFieldAccessible_with_fieldNameNullAndInstance_fails() {
        ReflectionHelper.makeFieldAccessible((String) null, pojo);
    }

    @Test
    public void test_makeFieldAccessible_with_fieldAndInstance_makeAccessible() throws NoSuchFieldException {
        final Field declaredField = pojo.getClass().getDeclaredField(SIMPLE_STRING_NAME);
        assertValid(declaredField.canAccess(pojo), false);
        final Field field = ReflectionHelper.makeFieldAccessible(declaredField, pojo);
        assertValid(field.canAccess(pojo), true);
    }

    @Test(expected = AssertionError.class)
    public void test_makeFieldAccessible_with_fieldNullAndInstance_fails() {
        ReflectionHelper.makeFieldAccessible((Field) null, pojo);
    }

    @Test
    @Ignore("Fails")
    public void test_setFinalStaticValue_with_FieldNameAndValueAndClazz_replaceValue() throws IllegalAccessException {
        final int valueBefore = 10;
        final int valueAfter = valueBefore + 20;
        assertValid(CONST_FLOAT_VALUE, valueBefore);

        final Field field = ReflectionHelper.setFinalStaticValue(CONST_FLOAT_NAME, valueAfter, pojo.getClass());
        assertValid(field.get(CONST_FLOAT_VALUE), valueAfter);
    }

    @Test
    @Ignore("fails")
    public void test_setFinalStaticValue_with_fieldAndValueAndClazz_replaceValue() throws IllegalAccessException {
        final Float valueBefore = CONST_FLOAT_VALUE;
        final Float valueAfter = valueBefore + 20;
        final Field field = ReflectionHelper.findField(CONST_FLOAT_NAME, pojo.getClass());
        assertValid(field.get(CONST_FLOAT_VALUE), valueBefore);

        ReflectionHelper.setFinalStaticValue(field, valueAfter, pojo.getClass());
        assertValid(field.get(CONST_FLOAT_VALUE), valueAfter);
    }

    @Test
    public void test_readStaticValue_with_FieldNameConstAndClazz_return_value() {
        final Object actual = ReflectionHelper.readStaticValue(CONST_FLOAT_NAME, pojo.getClass());
        assertValid(actual, CONST_FLOAT_VALUE);
    }


    @Test(expected = AssertionError.class)
    public void test_readStaticValue_with_FieldNameNotFoundAndClazz_fails() {
        ReflectionHelper.readStaticValue(NOT_FOUND, pojo.getClass());
    }

    @Test
    public void test_readGetterValue_with_fieldNameGetterAndInstance_return_value() {
        final Object actual = ReflectionHelper.readGetterValue(GET_SIMPLE_STRING, pojo);
        assertValid(actual, SIMPLE_STRING_VALUE, SIMPLE_STRING_CLAZZ);
    }

    @Test(expected = AssertionError.class)
    public void test_readGetterValue_with_fieldNameNotFoundAndInstance_fails() {
        final Object actual = ReflectionHelper.readGetterValue(NOT_FOUND, pojo);
        assertValid(actual, SIMPLE_STRING_VALUE, SIMPLE_STRING_CLAZZ);
    }

    @Test(expected = AssertionError.class)
    public void test_readGetterValue_with_fieldNameNullAndInstance_fails() {
        final Object actual = ReflectionHelper.readGetterValue((String) null, pojo);
        assertValid(actual, SIMPLE_STRING_VALUE, SIMPLE_STRING_CLAZZ);
    }
}
