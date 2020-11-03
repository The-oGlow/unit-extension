package com.glowa_net.tools.unit;

import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Abstract class to use for unit-testing on {@link Object}, beans, pojos.
 * However you name your classes with an amount of getter and setter.
 *
 * @param <T>  the type of the class to test
 */
public abstract class AbstractEnumObjectUnitTester<T extends Object> extends AbstractBaseUnitTester<T> {

    public static enum NAME_CHECK_ENUM {
        /** <strong>C</strong>ase sensitive, <strong>F</strong>ullname Check */ CF, //
        /** <strong>C</strong>ase <strong>I</strong>nsensitive, <strong>F</strong>ullname Check */ CIF, //
        /** <strong>C</strong>ase sensitive, <strong>S</strong>tarts <strong>W</strong>ith Check */ CSW, //
        /** <strong>C</strong>ase<strong>I</strong>nsensitive, <strong>S</strong>tarts <strong>W</strong>ith Check */ CISW;
    }

    private static final Logger LOGGER = LogManager.getLogger();

    private Collection<String> allFieldsToIgnoreForCode = new HashSet<>();
    private NAME_CHECK_ENUM    nameCheckType            = NAME_CHECK_ENUM.CF;
    private boolean            codeCheckEnabled         = true;

    public AbstractEnumObjectUnitTester(Class<T> typeOfT) {
        super(typeOfT);
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        allFieldsToIgnoreForCode.addAll(enumObjectsToIgnoreForCode() == null ? List.of() : enumObjectsToIgnoreForCode());
        allFieldsToIgnoreForCode = allFieldsToIgnoreForCode.stream().map(String::toLowerCase).collect(Collectors.toSet());
    }

    @Test
    public void validateAllEnumObjects() {
        List<Field> allEnumObjects = retrievePublicConstantsfromClass(getTypeOfT());

        for (Field expectedEnumObject : allEnumObjects) {
            collector.checkThat("checking '" + expectedEnumObject.getName() + "'", validateEnumObject(expectedEnumObject, getTypeOfT()), equalTo(true));
        }
    }

    @Override
    protected T createEntity() {
        return null;
    }

    /**
     * Fields in the current class, which should be ignored on testing 'toCode()'.
     *
     * @return list of fieldnames
     * @see #validateEnumObjectCode(Field, Object)
     * @see #validateAllEnumObjects()
     */
    protected abstract List<String> enumObjectsToIgnoreForCode();

    /**
     * Flag, so 'toName()' must match the given {@link NAME_CHECK_ENUM}-check.
     *
     * @return #NAME_CHECK_ENUM
     * @see NAME_CHECK_ENUM
     * @see #validateEnumObjectName(Field, Object)
     * @see #validateAllEnumObjects()
     */
    protected NAME_CHECK_ENUM getNameCheckType() {
        return nameCheckType;
    }

    /**
     * Set the flag, so 'toName()' must match the given {@link NAME_CHECK_ENUM}-check.
     *
     * @param nameCheckType #NAME_CHECK_ENUM
     * @see NAME_CHECK_ENUM
     * @see #validateEnumObjectName(Field, Object)
     * @see #validateAllEnumObjects()
     */
    protected void setNameCheckType(NAME_CHECK_ENUM nameCheckType) {
        this.nameCheckType = nameCheckType;
    }

    /**
     * Flag, if 'toCode()' should be checked.
     *
     * @return TRUE = will be checked, else FALSE
     * @see #validateEnumObjectCode(Field, Object)
     * @see #validateAllEnumObjects()
     */
    protected boolean isCodeCheckEnabled() {
        return codeCheckEnabled;
    }

    /**
     * Sets the flag, if 'toCode()' should be checked.
     *
     * @param codeCheckEnabled TRUE = will be checked, else FALSE
     * @see #validateEnumObjectCode(Field, Object)
     * @see #validateAllEnumObjects()
     */
    protected void setCodeCheckEnabled(boolean codeCheckEnabled) {
        this.codeCheckEnabled = codeCheckEnabled;
    }

    protected boolean isInIgnoreListForCode(String concreteEnumObject) {
        return (allFieldsToIgnoreForCode.contains(concreteEnumObject.toLowerCase()));
    }

    /**
     * Verifies that an enumObject is correct defined.
     *
     * @param expectedField the field-object, containing the expected enumObject
     * @param actualClazz the type of the enumObject to check
     * @return TRUE = is correct declared, else FALSE
     */
    protected final boolean validateEnumObject(Field expectedField, Class<T> actualClazz) {
        boolean isValid = false;

        if (expectedField.getType().isAssignableFrom(actualClazz)) {
            try {
                T actualInstance = (T) ReflectionTestUtils.getField(actualClazz, expectedField.getName());

                boolean resultName = validateEnumObjectName(expectedField, actualInstance);

                boolean resultCode = true;
                if (isCodeCheckEnabled()) {
                    resultCode = validateEnumObjectCode(expectedField, actualInstance);
                } else {
                    LOGGER.warn("Checking getCode() is disabled for '{}' !", expectedField.getName());
                }
                isValid = resultName && resultCode;

            } catch (Exception e) {
                if (isCodeCheckEnabled()) {
                    if (isInIgnoreListForCode(expectedField.getName())) {
                        isValid = true;
                        LOGGER.warn("'{}' is listed to be ignored!", expectedField.getName());
                    } else {
                        collector.addError(e);
                    }
                } else {
                    collector.addError(e);
                }
            }
        }
        return isValid;
    }

    /**
     * Verifies that the declared name of an enumObject matches with the name()-value.
     *
     * @param expectedField the field-object, containing the expected enumObject
     * @param actualInstance an instance of the enumObject to check
     * @return TRUE = is correct declared, else FALSE
     */
    protected boolean validateEnumObjectName(Field expectedField, T actualInstance) {
        boolean isValid = false;

        boolean nameCheck;
        String expected = expectedField.getName();
        String actual = (String) ReflectionTestUtils.getField(actualInstance, "name");

        switch (getNameCheckType()) {
        case CF: {
            nameCheck = expected.equals(actual);
            break;
        }
        case CIF: {
            if (expected.contains("_")) {
                expected = expected.replaceAll("_(.)", "$1");
            }
            nameCheck = expected.equalsIgnoreCase(actual);
            break;
        }
        case CSW: {
            nameCheck = expected.startsWith(actual);
            break;
        }
        case CISW: {
            if (expected.contains("_")) {
                expected = expected.replaceAll("_(.)", "$1");
            }
            expected = expected.toLowerCase();
            nameCheck = expected.equalsIgnoreCase(actual);
            break;
        }
        default: {
            nameCheck = false;
        }
        }

        if (nameCheck) {
            isValid = true;
        } else {
            collector.checkThat("checking name() of '" + expectedField.getName() + "'", actual, equalTo(expectedField.getName()));
        }
        return isValid;
    }

    /**
     * Verifies that the declared name of an enumObject matches with the getCode()-value.
     *
     * @param expectedField the field-object, containing the expected enumObject
     * @param actualInstance an instance of the enumObject to check
     * @return TRUE = is correct declared, else FALSE
     */
    protected boolean validateEnumObjectCode(Field expectedField, T actualInstance) {
        boolean isValid = false;

        int expectedCode = (int) retrieveNumberFromText(expectedField.getName());
        int actualCode = Integer.parseInt(ReflectionTestUtils.getField(actualInstance, "code").toString());

        if (actualCode == expectedCode) {
            isValid = true;
        } else {
            collector.checkThat("checking getCode() of '" + expectedField.getName() + "'", actualCode, equalTo(expectedCode));
        }
        return isValid;
    }
}
