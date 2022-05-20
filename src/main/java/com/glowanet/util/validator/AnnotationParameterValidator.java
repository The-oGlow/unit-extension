package com.glowanet.util.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Verifies if an annotation has a specific parameter and parameter-value.
 *
 * @author Oliver Glowa
 * @see org.hamcrest.AnnotationMatchers
 * @since 0.02.000
 */
//@SuppressWarnings({"rawtypes", "java:S3740"})
public class AnnotationParameterValidator {

    private final Class<?>[] noparams = {};

    /**
     * @param clazz           a type
     * @param methodName      the name of the method in {@code clazz}
     * @param annotationClazz the type of the annotation at {@code methodName}
     *
     * @return TRUE=the {@code annotationClazz} is found, else FALSE
     */
    public boolean isAnnotation(Class<?> clazz, String methodName, Class<? extends Annotation> annotationClazz) {
        boolean isAnnotation = false;

        Annotation annotation2t = getAnnotation(clazz, methodName, annotationClazz);
        if (annotation2t != null) {
            isAnnotation = true;
        }
        return isAnnotation;

    }

    /**
     * @param clazz                    a type
     * @param methodName               the name of the method in {@code clazz}
     * @param annotationClazz          the type of the annotation at {@code methodName}
     * @param annotationParameterKey   the name of the parameter of the annotation
     * @param annotationParameterValue the value of the parameter of the annotation
     *
     * @return TRUE=the {@code annotationParameterKey} and {@code annotationParameterValue} is found, else FALSE
     */
    public boolean isAnnotationParameter(Class<?> clazz, String methodName, Class<? extends Annotation> annotationClazz, String annotationParameterKey,
                                         String annotationParameterValue) {
        boolean isAnnotationParameter = false;

        Annotation annotation2t = getAnnotation(clazz, methodName, annotationClazz);
        if (annotation2t != null && annotationParameterKey != null && annotationParameterKey.length() > 0) {
            String annotationUsing = annotationParameterKey + "=" + annotationParameterValue;
            String actualString = annotation2t.toString();
            isAnnotationParameter = actualString.contains(annotationUsing);
        }
        return isAnnotationParameter;
    }

    /**
     * @param clazz           a type
     * @param methodName      the name of the method in {@code clazz}
     * @param annotationClazz the type of the annotation at {@code methodName}
     * @param <T>             a type of {@link Annotation}
     *
     * @return an {@code Annotation} or null
     */
    <T extends Annotation> Annotation getAnnotation(Class<?> clazz, String methodName, Class<T> annotationClazz) {
        Method method2t;
        T annotation2t = null;
        try {
            method2t = clazz.getDeclaredMethod(methodName, noparams);
            annotation2t = method2t.getDeclaredAnnotation(annotationClazz);
        } catch (NullPointerException | NoSuchMethodException | SecurityException e) {
            // exceptions can be ignored, nothing must be done
        }

        return annotation2t;
    }
}
