package com.glowanet.util.reflect;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Utility Helper for accessing class information without the use of Spring's "ReflectionTestUtils".
 *
 * @author Oliver Glowa
 * @since 0.10.000
 */
public class ReflectionHelper {

    private ReflectionHelper() {
    }

    /**
     * @param instance an instantiated object
     *
     * @return list of fields, which have a getter, or an empty list
     *
     * @throws AssertionError when {@code instance} is null
     */
    public static List<PropertyDescriptor> findGetter(final Object instance) throws AssertionError {
        final List<PropertyDescriptor> getterList = new ArrayList<>();
        if (instance == null) {
            failure(null, null, null);
        } else {
            final BeanInfo beanInfo = handleGetBeanInfo(instance.getClass(), Object.class);
            if (beanInfo != null) {
                final PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
                for (final PropertyDescriptor pd : props) {
                    if (pd.getReadMethod() != null) {
                        getterList.add(pd);
                    }
                }
            }
        }
        return getterList;
    }

    /**
     * @param instance an instantiated object
     *
     * @return list of fields, which have a setter, or an empty list
     */
    @SuppressWarnings("java:S1172")
    public static List<PropertyDescriptor> findSetter(final Object instance) {
        return List.of();
    }

    /**
     * @param instanceClazz the type of the instance
     *
     * @return TRUE = the {@code instanceClazz} implements {@link Serializable}, else FALSE
     */
    public static boolean hasSerializableIF(final Class<?> instanceClazz) {
        boolean hasIt = false;
        final List<Class<?>> listIF = ClassUtils.getAllInterfaces(instanceClazz);
        for (final Class<?> clazzIF : listIF) {
            if (Serializable.class.equals(clazzIF)) {
                hasIt = true;
                break;
            }
        }
        return hasIt;
    }

    /**
     * @param fieldName the name of a field
     * @param instance  an instantiated object
     *
     * @return the found field-object
     *
     * @throws AssertionError in case the {@code fieldName} can not be found
     */
    public static Field findField(final String fieldName, final Object instance) {
        isInstanceSet(instance, fieldName);
        return findField(fieldName, instance.getClass());
    }

    /**
     * @param fieldName     the name of a field
     * @param instanceClazz the type of the instance
     *
     * @return the found field-object
     */
    public static Field findField(final String fieldName, final Class<?> instanceClazz) {
        isInstanceSet(instanceClazz, fieldName);
        final Field idField = handleGetField(fieldName, instanceClazz);
        if (idField != null) {
            makeFieldAccessible(idField, null);
        }
        return idField;
    }

    /**
     * @param fieldName     the name of a field
     * @param instanceClazz the type of the instance
     * @param <V>           type of value in {@code fieldName}
     *
     * @return the value of {@code field}
     */
    public static <V> V readField(final String fieldName, final Class<?> instanceClazz) {
        isInstanceSet(instanceClazz, fieldName);
        final Field field = findField(fieldName, instanceClazz);
        return readField(field, instanceClazz);
    }

    /**
     * @param field         an instantiated object of type field
     * @param instanceClazz the type of the instance
     * @param <V>           type of value in {@code field}
     *
     * @return the value of {@code field}
     *
     * @throws AssertionError in case the {@code field} can not be accessed
     */
    @SuppressWarnings("unchecked")
    public static <V> V readField(final Field field, final Class<?> instanceClazz) throws AssertionError {
        isInstanceSet(instanceClazz, field);
        isParamSet(instanceClazz, field);
        V fieldValue = null;
        try {
            makeFieldAccessible(field, null);
            fieldValue = (V) field.get(instanceClazz);
        } catch (final IllegalAccessException | IllegalArgumentException e) {
            failure(instanceClazz, field, e);
        }
        return fieldValue;
    }

    /**
     * @param fieldName the name of a field
     * @param instance  the instance to look in
     * @param <V>       type of value in {@code fieldName}
     *
     * @return the current value
     */
    public static <V> V readField(final String fieldName, final Object instance) {
        isInstanceSet(instance, fieldName);
        final Field field = findField(fieldName, instance);
        return readField(field, instance);
    }

    /**
     * @param field    an instantiated object of type field
     * @param instance the instance to look in
     * @param <V>      type of value in {@code field}
     *
     * @return the current value
     *
     * @throws AssertionError when {@code field} does not exist / is not accessible
     */
    @SuppressWarnings("unchecked")
    public static <V> V readField(final Field field, final Object instance) {
        isInstanceSet(instance, field);
        isParamSet(instance, field);
        V fieldValue = null;
        try {
            makeFieldAccessible(field, instance);
            fieldValue = (V) field.get(instance);
        } catch (final IllegalArgumentException | IllegalAccessException e) {
            failure(instance == null ? null : instance.getClass(), field, e);
        }
        return fieldValue;
    }

    /**
     * @param fieldName the name of a field
     * @param instance  the instance to look in
     * @param newValue  the new value to set
     * @param <V>       type of value in {@code fieldName}
     */
    public static <V> void writeField(final String fieldName, final Object instance, final V newValue) {
        isInstanceSet(instance, fieldName);
        final Field field = findField(fieldName, instance);
        writeField(field, instance, newValue);
    }

    /**
     * @param field    an instantiated object of type field
     * @param instance the instance to look in
     * @param newValue the new value to set
     * @param <V>      type of value in {@code field}
     *
     * @throws AssertionError when {@code field} does not exist / is not accessible / cannot be modified
     */
    public static <V> void writeField(final Field field, final Object instance, final V newValue) throws AssertionError {
        isInstanceSet(instance, field);
        isParamSet(instance, field);
        try {
            makeFieldAccessible(field, instance);
            field.set(instance, newValue);
        } catch (final IllegalArgumentException | IllegalAccessException e) {
            failure(instance == null ? null : instance.getClass(), field, e);
        }
    }

    /**
     * @param fieldName the name of a field
     * @param instance  an instantiated object
     *
     * @return a field object
     */
    @SuppressWarnings("java:S2259")
    public static Field makeFieldAccessible(final String fieldName, final Object instance) {
        final Field field = findField(fieldName, instance);
        return makeFieldAccessible(field, instance);
    }

    /**
     * @param field    an instantiated object of type field
     * @param instance an instantiated object
     *
     * @return a field object
     */
    @SuppressWarnings({"java:S1166", "java:S1696",})
    public static Field makeFieldAccessible(final Field field, final Object instance) {
        isParamSet(instance, field);
        if (instance == null) {
            field.trySetAccessible();
        } else {
            try {
                if (!field.canAccess(instance)) {
                    throw new IllegalArgumentException();
                }
            } catch (final IllegalArgumentException e) {
                field.trySetAccessible();
            }
        }
        return field;
    }

    /**
     * @param fieldName     the name of a field
     * @param newValue      the new value to set
     * @param instanceClazz the type of the instance
     *
     * @return a field object
     */
    public static Field setFinalStaticValue(final String fieldName, final Object newValue, final Class<?> instanceClazz) {
        isInstanceSet(instanceClazz, fieldName);
        final Field field = findField(fieldName, instanceClazz);
        return setFinalStaticValue(field, newValue, instanceClazz);
    }

    /**
     * @param field         an instantiated object of type field
     * @param newValue      the new value to set
     * @param instanceClazz the type of the instance
     *
     * @return a field object
     *
     * @throws AssertionError when {@code field} does not exist / is not accessible / cannot be modified
     */
    @SuppressWarnings("java:S1696")
    public static Field setFinalStaticValue(final Field field, final Object newValue, final Class<?> instanceClazz) throws AssertionError {
        isInstanceSet(instanceClazz, field);
        isParamSet(instanceClazz, field);
        try {
            makeFieldAccessible(field, instanceClazz);
            final Field modifiersField = field.getClass().getDeclaredField("modifiers");
            makeFieldAccessible(modifiersField, field);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(instanceClazz, newValue);
        } catch (final NullPointerException | NoSuchFieldException | IllegalAccessException e) {
            failure(instanceClazz, field, e);
        }
        return field;
    }

    /**
     * @param constantName  the name of the constant
     * @param instanceClazz the type of the instance
     * @param <V>           type of field value
     *
     * @return the current value of the constant
     *
     * @throws AssertionError when {@code constantName} does not exist / is not accessible
     */
    @SuppressWarnings({"unchecked", "java:S1696"})
    public static <V> V readStaticValue(final String constantName, final Class<?> instanceClazz) throws AssertionError {
        isInstanceSet(instanceClazz, constantName);
        V staticValue = null;
        try {
            staticValue = (V) FieldUtils.readStaticField(instanceClazz, constantName, true);
        } catch (final NullPointerException | IllegalArgumentException | IllegalAccessException e) {
            failure(instanceClazz, constantName, e);
        }
        return staticValue;
    }

    /**
     * @param getterName a name of a getter of {@code instance}
     * @param instance   an instance of a type
     *
     * @return the value of {@code getterName}
     */
    public static Object readGetterValue(final String getterName, final Object instance) {
        isInstanceSet(instance, getterName);
        final List<PropertyDescriptor> getterList = findGetter(instance);
        final PropertyDescriptor getter = getterList.stream().filter(item -> item.getReadMethod().getName().equals(getterName)).findFirst().orElse(null);
        return readGetterValue(getter, instance);
    }

    /**
     * @param getter   a getter of {@code instance}
     * @param instance an instance of a type
     * @param <V>      the type of the value of {@code getter}
     *
     * @return the value of {@code getterName}
     */
    public static <V> V readGetterValue(final PropertyDescriptor getter, final Object instance) {
        isInstanceSet(instance, getter);
        isParamSet(instance, getter);
        return handleInvokeMethod(getter, instance);
    }

    /**
     * @param propertyDescriptor a propertyDescriptor of {@code instance}
     * @param instance           an instance of a type
     * @param <V>                the type of the value of {@code propertyDescriptor}
     *
     * @return the return value of the {@code propertyDescriptor}
     *
     * @throws AssertionError when {@code propertyDescriptor} does not exist / is not accessible
     */
    @SuppressWarnings({"unchecked", "java:S1696", "java:S2583"})
    static <V> V handleInvokeMethod(final PropertyDescriptor propertyDescriptor, final Object instance) throws AssertionError {
        V value = null;
        try {
            value = (V) MethodUtils.invokeMethod(instance, propertyDescriptor.getReadMethod().getName());
        } catch (final NullPointerException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            failure((instance == null ? null : instance.getClass()), (propertyDescriptor == null ? null : propertyDescriptor.getName()), e);
        }
        return value;
    }

    /**
     * @param fieldName     the name of a field
     * @param instanceClazz the type of the instance
     *
     * @return field or null
     *
     * @throws AssertionError when {@code fieldName} does not exist / is not accessible
     */
    @SuppressWarnings({"java:S1696"})
    static Field handleGetField(final String fieldName, final Class<?> instanceClazz) throws AssertionError {
        Field idField = null;
        try {
            idField = FieldUtils.getField(instanceClazz, fieldName, true);
        } catch (final NullPointerException | SecurityException | IllegalArgumentException e) {
            failure(instanceClazz, fieldName, e);
        }
        return idField;
    }

    /**
     * @param instanceClazz the type of the instance
     * @param stopClazz     the type when to stop the search
     *
     * @return beanInfo or null
     *
     * @throws AssertionError when {@code instanceClazz} does not exist / is accessible
     */
    static BeanInfo handleGetBeanInfo(final Class<?> instanceClazz, final Class<?> stopClazz) throws AssertionError {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(instanceClazz, stopClazz);
        } catch (final IntrospectionException e) {
            failure(instanceClazz, null, e);
        }
        return beanInfo;
    }

    /**
     * @param instance an instance of a type
     * @param suffix   an additional text for the {@link AssertionError}
     *
     * @throws AssertionError when {@code instance} is null
     */
    private static void isInstanceSet(final Object instance, final Object suffix) throws AssertionError {
        if (instance == null) {
            failure(instance, suffix, null);
        }
    }

    /**
     * @param prefix an additional text for the {@link AssertionError}
     * @param param  an instance of a parameter
     *
     * @throws AssertionError when {@code param} is null
     */
    private static void isParamSet(final Object prefix, final Object param) throws AssertionError {
        if (param == null) {
            failure(prefix, param, null);
        }
    }

    /**
     * @param paramClazz the type of the parameter
     * @param suffix     an additional text for the {@link AssertionError}
     * @param throwable  the catched throwable
     *
     * @throws AssertionError raises always this error
     */
    private static void failure(final Class<?> paramClazz, final Object suffix, final Throwable throwable) throws AssertionError {
        failure(paramClazz == null ? null : paramClazz.getName(), suffix, throwable);
    }

    /**
     * @param param     an instance of a parameter
     * @param suffix    an additional text for the {@link AssertionError}
     * @param throwable the catched throwable
     *
     * @throws AssertionError raises always this error
     */
    @SuppressWarnings("java:S5960")
    private static void failure(final Object param, final Object suffix, final Throwable throwable) throws AssertionError {
        final String msg = String.format("Can't find or access '%s%s' %s",
                (param == null ? "NULL" : param),
                (suffix == null || suffix.toString().isEmpty() ? "#NULL" : ("#" + suffix)), //
                (throwable == null ? "!" : (":" + System.lineSeparator() + ExceptionUtils.getStackTrace(throwable))) //
        );
        fail(msg);
    }
}
