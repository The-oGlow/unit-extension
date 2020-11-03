package com.glowa_net.tools.random;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.ClassUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RandomValueFactory {

    private static final Logger LOGGER         = LogManager.getLogger();
    private static final String PACKAGE_PREFIX = "com.glowa_net.tools.random.RandomValue";

    private RandomValueFactory() {
    }

    public static <T> IRandomValue<T> createRandomValue(Class<T> valueClazz) {
        return createRandomValue(valueClazz, new Class[0]);
    }

    private static <T> IRandomValue<T> createRandomValue(Class<T> valueClazz, Class<?>... valueParameters) {
        IRandomValue<T> newRandomValue = null;
        if (valueClazz != null) {
            String nameRandomValue = PACKAGE_PREFIX + valueClazz.getSimpleName();
            Class<?> clazzRandomValue = null;
            try {
                clazzRandomValue = ClassUtils.getClass(nameRandomValue);
            } catch (ClassNotFoundException e) {
                LOGGER.error("Can't find '{}' : {}", nameRandomValue, e);
            }

            if (clazzRandomValue != null && ClassUtils.isAssignable(clazzRandomValue, IRandomValue.class)) {
                try {
                    if (valueParameters.length > 0) {
                        newRandomValue = (IRandomValue<T>) clazzRandomValue.getDeclaredConstructor(valueParameters).newInstance(valueClazz);
                    } else {
                        newRandomValue = (IRandomValue<T>) clazzRandomValue.getDeclaredConstructor().newInstance();
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException e) {
                    LOGGER.error("Can't create instance '{}' : {}", clazzRandomValue, e);
                }
            }
        }
        return newRandomValue;
    }

    public static <V> RandomValueObject<V> createLegacyRandomValue() {
        return (RandomValueObject<V>) createRandomValue(Object.class, Class.class);
    }
}
