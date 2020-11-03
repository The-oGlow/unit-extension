package com.glowa_net.tools.unit;

import static com.glowa_net.tools.random.RandomValueFactory.createLegacyRandomValue;
import static com.glowa_net.tools.random.RandomValueFactory.createRandomValue;
import static org.junit.Assert.fail;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.beans.PropertyUtil;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;

public abstract class AbstractBaseUnitTester<T> {

    private static final Logger LOGGER    = LogManager.getLogger();

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    private final Class<T>      typeOfT;

    private T                   entity;

    protected static void setFinalStatic(Class<?> clazz, String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    public AbstractBaseUnitTester(Class<T> typeOfT) {
        this.typeOfT = typeOfT;
    }

    protected void setUp() {
        setEntity(createEntity());
    }

    /**
     * Create instance (with default constructor, if available).
     *
     * @return instance of the type.
     * @see #setUp()
     */
    protected abstract T createEntity();

    protected Class<T> getTypeOfT() {
        return this.typeOfT;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    private List<PropertyDescriptor> getAllPropertyDescriptors(T entityObject) {
        return Arrays.stream(PropertyUtil.propertyDescriptorsFor(entityObject, null)).collect(Collectors.toList());
    }

    /**
     * @return list of fields, which have a getter
     */
    protected List<PropertyDescriptor> findGetter() {
        List<PropertyDescriptor> getterList = new ArrayList<>();
        getAllPropertyDescriptors(entity).forEach(pd -> {
            if (pd.getReadMethod() != null) {
                getterList.add(pd);
            }
        });
        return getterList;
    }

    /**
     * @return list of fields, which have a setter
     */
    protected List<PropertyDescriptor> findSetter() {
        List<PropertyDescriptor> setterList = new ArrayList<>();
        getAllPropertyDescriptors(entity).forEach(pd -> {
            if (pd.getWriteMethod() != null) {
                setterList.add(pd);
            }
        });
        return setterList;
    }

    protected boolean hasSerializableIF(Class<?> clazz) {
        boolean hasIt = false;
        List<Class<?>> listIF = ClassUtils.getAllInterfaces(clazz);
        for (Class<?> clazzIF : listIF) {
            if (Serializable.class.equals(clazzIF)) {
                hasIt = true;
                break;
            }
        }
        return hasIt;
    }

    protected Field findField(T instance, String fieldName) {
        Field idField = null;
        try {
            idField = instance.getClass().getDeclaredField(fieldName);
            makeFieldAccessible(idField, instance);
        } catch (NoSuchFieldException e) {
            fail("No " + fieldName + " defined : " + e.getMessage());
        } catch (SecurityException e) {
            fail(fieldName + " not accessible : " + e.getMessage());
        }
        return idField;
    }

    protected void makeFieldAccessible(Field idField, T instance) {
        try {
            if (!idField.canAccess(instance)) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            idField.trySetAccessible();
        }
    }

    /**
     * Retrieves all public constants from a class.
     *
     * @param clazzT the class from which to retrieve the constants
     * @return a list of constants or an empty list.
     */
    protected List<Field> retrievePublicConstantsfromClass(Class<T> clazzT) {
        List<Field> publicConsts = new ArrayList<>();
        for (Field field : clazzT.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                publicConsts.add(field);
            }
        }
        return publicConsts;
    }

    /**
     * In case we have a "special interpretation" on a number extraction, you can
     * override this method.
     *
     * @param textWithNumber the text with the number
     * @param numberAsText   the currently extracted numeric values from the text
     * @return the extracted "special" number for this text
     */
    protected String retrieveNumberFromTextSpecialized(String textWithNumber, String numberAsText) {
        return numberAsText;
    }

    /**
     * Extracts a number from an string.
     *
     * @param textWithNumber the text with the number
     * @return the extracted number or NULL
     */
    protected Number retrieveNumberFromText(String textWithNumber) {
        String numberAsText = StringUtils.getDigits(textWithNumber);
        if (NumberUtils.isParsable(numberAsText)) {
            numberAsText = retrieveNumberFromTextSpecialized(textWithNumber, numberAsText);
            return NumberUtils.createNumber(numberAsText);
        } else {
            return null;
        }
    }

    /**
     * Generating the map of values for the method parameters.
     *
     * @param inspectMethod the method to inspect
     * @return map of types and values
     */
    protected Map<Class<?>, Object> retrieveMethodParameters(Method inspectMethod) {
        Map<Class<?>, Object> setterParams = new LinkedHashMap<>();
        if (inspectMethod != null) {
            for (Parameter param : inspectMethod.getParameters()) {
                Object paramValue = retrieveDefaultValue(param.getType());
                if (Object.class.isAssignableFrom(param.getType())) {
                    setterParams.put(param.getType(), paramValue);
                } else if (param.getType().isPrimitive()) {
                    setterParams.put(param.getType(), paramValue);
                } else {
                    setterParams.put(param.getType(), null);
                }
            }
        }
        return setterParams;
    }

    /**
     * Generating a value based on the class-type.
     *
     * @param type the class
     * @param <V>  the type of of the value
     * @return the generated value or null
     */
    private <V> Object retrieveDefaultValue(Class<V> type) {
        Object result = null;
        try {
            result = createRandomValue(type).randomValue();
        } catch (NullPointerException e) {
            result = createLegacyRandomValue().randomValue(type);
        }
        return result;
    }

}
