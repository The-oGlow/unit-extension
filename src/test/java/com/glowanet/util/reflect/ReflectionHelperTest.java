package com.glowanet.util.reflect;

import com.glowanet.data.SimplePojo;
import com.glowanet.data.SimpleSerializable;
import com.glowanet.util.junit.TestResultHelper;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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
    public static final Float    CONST_FLOAT_VALUE = 111f;

    public static final String NOT_FOUND = "notFound";

    private SimplePojo pojo;
    private String     differentPojo;

    @Before
    public void setUp() {
        pojo = new SimplePojo();
        pojo.setSimpleString(SIMPLE_STRING_VALUE);
        pojo.setSimpleInt(SIMPLE_INT_VALUE);

        differentPojo = "different";
    }

    @After
    public void tearDown() {
        if (SimplePojo.CONST_FLOAT.floatValue() != CONST_FLOAT_VALUE.floatValue()) {
            ReflectionHelper.setFinalStaticValue(CONST_FLOAT_NAME, CONST_FLOAT_VALUE, pojo.getClass());
        }
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
        if (actual instanceof Throwable) {
            assertThat(actual.toString(), (Matcher<String>) expectedMatcher);
        } else {
            assertThat(actual, expectedMatcher);
        }
    }

    private void throwableValid(Class<?> expected, Throwable actual) {
        assertThat(expected, instanceOf(Class.class));
        assertThat(actual, notNullValue());
        assertThat(actual, Matchers.instanceOf(expected));
    }

    private void throwableAssErrValid(ThrowingRunnable actual) {
        throwableValid(AssertionError.class, TestResultHelper.verifyException(actual, Throwable.class));
    }

    @Test
    public void test_findGetter_with_instance_return_listOfFields() {
        final List<PropertyDescriptor> actual = ReflectionHelper.findGetter(pojo);
        assertValid(actual, hasSize(SimplePojo.GETTER_COUNT));
    }

    @Test
    public void test_findGetter_with_null_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.findGetter(null));
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

    @Test
    public void test_findField_with_fieldNameNullAndInstance_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.findField(null, pojo));
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

    @Test
    public void test_findField_with_fieldNameNullAndClazz_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.findField(null, pojo.getClass()));
    }

    @Test
    public void test_findField_with_fieldNameAndInstanceNull_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.findField(SIMPLE_STRING_NAME, (Object) null));
    }

    @Test
    public void test_findField_with_fieldNameAndClazzNull_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.findField(SIMPLE_STRING_NAME, null));
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

    @Test
    public void test_readField_with_fieldNameVariableAndClazz_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.readField(SIMPLE_INT_NAME, pojo.getClass()));
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

    @Test
    public void test_readField_with_fieldNameNullAndInstance_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.readField((String) null, pojo));
    }

    @Test
    public void test_readField_with_fieldNullAndInstance_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.readField((Field) null, pojo));
    }

    @Test
    public void test_readField_with_fieldNotFoundInClazz_throws_failure() {
        final Field field = ReflectionHelper.findField(SIMPLE_STRING_NAME, pojo);
        throwableAssErrValid(() -> ReflectionHelper.readField(field, differentPojo.getClass()));
    }

    @Test
    public void test_readField_with_fieldNotFoundInInstance_throws_failure() {
        final Field field = ReflectionHelper.findField(SIMPLE_STRING_NAME, pojo);
        throwableAssErrValid(() -> ReflectionHelper.readField(field, differentPojo));
    }

    @Test
    public void test_makeFieldAccessible_with_fieldNameAndInstance_makeAccessible() {
        final Field field = ReflectionHelper.makeFieldAccessible(SIMPLE_STRING_NAME, pojo);
        assertValid(field.canAccess(pojo), true);
    }

    @Test
    public void test_makeFieldAccessible_withFieldNameNotFoundAndInstance_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.makeFieldAccessible(NOT_FOUND, pojo));
    }

    @Test
    public void test_makeFieldAccessible_with_fieldNameNullAndInstance_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.makeFieldAccessible((String) null, pojo));
    }

    @Test
    public void test_makeFieldAccessible_with_fieldAndInstance_makeAccessible() throws NoSuchFieldException {
        final Field declaredField = pojo.getClass().getDeclaredField(SIMPLE_STRING_NAME);
        assertValid(declaredField.canAccess(pojo), false);
        final Field field = ReflectionHelper.makeFieldAccessible(declaredField, pojo);
        assertValid(field.canAccess(pojo), true);
    }

    @Test
    public void test_makeFieldAccessible_with_fieldNullAndInstance_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.makeFieldAccessible((Field) null, pojo));
    }

    @Ignore("Doesn't work on github docker")
    @Test
    public void test_setFinalStaticValue_with_fieldNameAndValueAndClazz_replaceValue() throws IllegalAccessException {
        final float valueBefore = CONST_FLOAT_VALUE;
        final float valueAfter = valueBefore + 20f;
        assertValid(SimplePojo.CONST_FLOAT, valueBefore);

        final Field field = ReflectionHelper.setFinalStaticValue(CONST_FLOAT_NAME, valueAfter, pojo.getClass());
        assertValid(field.get(CONST_FLOAT_VALUE), valueAfter);
        assertValid(SimplePojo.CONST_FLOAT, valueAfter);
    }

    @Test
    public void test_setFinalStaticValue_with_fieldNameAndValueAndWrongClazz_throws_failure() throws IllegalAccessException {
        final float valueBefore = CONST_FLOAT_VALUE;
        final float valueAfter = valueBefore + 20f;
        assertValid(SimplePojo.CONST_FLOAT, valueBefore);

        throwableAssErrValid(() ->
                ReflectionHelper.setFinalStaticValue(CONST_FLOAT_NAME, valueAfter, differentPojo.getClass())
        );
    }

    @Test
    public void test_readStaticValue_with_FieldNameConstAndClazz_return_value() {
        final Object actual = ReflectionHelper.readStaticValue(CONST_FLOAT_NAME, pojo.getClass());
        assertValid(actual, CONST_FLOAT_VALUE);
    }

    @Test
    public void test_readStaticValue_with_FieldNameNotFoundAndClazz_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.readStaticValue(NOT_FOUND, pojo.getClass()));
    }

    @Test
    public void test_readGetterValue_with_fieldNameGetterAndInstance_return_value() {
        final Object actual = ReflectionHelper.readGetterValue(GET_SIMPLE_STRING, pojo);
        assertValid(actual, SIMPLE_STRING_VALUE, SIMPLE_STRING_CLAZZ);
    }

    @Test
    public void test_readGetterValue_with_fieldNameNotFoundAndInstance_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.readGetterValue(NOT_FOUND, pojo));
    }

    @Test
    public void test_readGetterValue_with_fieldNameNullAndInstance_throws_failure() {
        throwableAssErrValid(() -> ReflectionHelper.readGetterValue((String) null, pojo));
    }

    @Test
    public void test_handleGetBeanInfo_throws_IntrospectionException() {
        final Class<?> instanceClazz = ArrayList.class;
        final Class<?> stopClazz = BigDecimal.class;

        final Throwable actual = TestResultHelper.verifyException(() -> ReflectionHelper.handleGetBeanInfo(instanceClazz, stopClazz), Throwable.class);
        assertValid(actual, containsString(java.beans.IntrospectionException.class.getName()), AssertionError.class);
    }

    @Test
    public void test_handleInvokeMethod_throws_NoSuchMethodException() throws IntrospectionException {
        PropertyDescriptor getter = new PropertyDescriptor(SIMPLE_STRING_NAME, pojo.getClass());
        final Throwable actual = TestResultHelper.verifyException(() -> ReflectionHelper.handleInvokeMethod(getter, SIMPLE_STRING_CLAZZ), Throwable.class);

        assertValid(actual, containsString(NoSuchMethodException.class.getName()), AssertionError.class);
    }

    @Test
    public void testNewInstance_withClazz_return_instance() {
        Class<?> typeClazz = pojo.getClass();

        Object actual = ReflectionHelper.newInstance(typeClazz);

        assertThat(actual, instanceOf(typeClazz));
    }

    @Test
    public void testNewInstance_withNull_return_null() {
        Object actual = ReflectionHelper.newInstance(null);

        assertThat(actual, nullValue());
    }

    @Test
    public void testNewInstance_withWrongConstructor_return_null() {
        Object actual = ReflectionHelper.newInstance(Long.class);

        assertThat(actual, nullValue());
    }

    @Test
    public void testNewInstance_withClazzAndParameter_return_instance() {
        Class<?> typeClazz = Exception.class;
        Class<?>[] parameterTypes = {String.class};
        String expectedMsg = "MSG";
        Object[] initArgs = {expectedMsg};

        Object actual = ReflectionHelper.newInstance(typeClazz, parameterTypes, initArgs);

        assertThat(actual, instanceOf(typeClazz));
        assertThat(((Exception) actual).getMessage(), equalTo(expectedMsg));
    }

    @Test
    public void testNewInstance_withParameterAllNull_return_null() {
        Class<?> typeClazz = null;
        Class<?>[] parameterTypes = null;
        Object[] initArgs = null;

        Object actual = ReflectionHelper.newInstance(typeClazz, parameterTypes, initArgs);

        assertThat(actual, nullValue());
    }

    @Test
    public void testNewInstance_withParameterWrongConstructor_return_null() {
        Class<?> typeClazz = Long.class;
        Class<?>[] parameterTypes = {Boolean.class};
        Object[] initArgs = {true};

        Object actual = ReflectionHelper.newInstance(typeClazz, parameterTypes, initArgs);

        assertThat(actual, nullValue());
    }
}
