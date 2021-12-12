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
@SuppressWarnings({"rawtypes", "java:S3740"})
public class AnnotationParameterValidator {

    private final Class<?>[] noparams = {};

    public boolean isAnnotation(Class<?> clazz, String methodName, Class annotationClazz) {
        boolean isAnnotation = false;

        Annotation annotation2t = getAnnotation(clazz, methodName, annotationClazz);
        if (annotation2t != null) {
            isAnnotation = true;
        }
        return isAnnotation;

    }

    public boolean isAnnotationParameter(Class<?> clazz, String methodName, Class annotationClazz, String annotationParameterKey,
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

    Annotation getAnnotation(Class<?> clazz, String methodName, Class annotationClazz) {
        Method method2t;
        Annotation annotation2t = null;
        try {
            method2t = clazz.getDeclaredMethod(methodName, noparams);
            //noinspection unchecked
            annotation2t = method2t.getDeclaredAnnotation(annotationClazz);
        } catch (NullPointerException | NoSuchMethodException | SecurityException e) {
            // exceptions can be ignored, nothing must be done
        }

        return annotation2t;
    }

}
