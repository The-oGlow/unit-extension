package org.hamcrest;

import org.hamcrest.annotation.HasMethodAnnotation;
import org.hamcrest.annotation.HasMethodAnnotationParameter;

/**
 * Contains matchers, which checks if a class has an annotation and annotation-parameters.
 *
 * @author Oliver Glowa
 * @since 0.02.000
 */
public class AnnotationMatchers {

    private AnnotationMatchers() {
    }

    /**
     * Creates a matcher that matches if the examined {@link Object} has the specified method with the specific annotation.
     * For example:
     * <p>
     * <pre>assertThat(objectCheese, hasMethodAnnotation("getCheese", CheeseAnnotation.class))</pre>
     *
     * @param methodName      the name of the method to look for
     * @param annotationClazz the class of the annotation
     *
     * @return a matcher
     */
    public static org.hamcrest.Matcher<Object> hasMethodAnnotation(String methodName, Class<?> annotationClazz) {
        return HasMethodAnnotation.hasMethodAnnotation(methodName, annotationClazz);
    }

    /**
     * Creates a matcher that matches if the examined {@link Object} has the specified method with the specific annotation and a specfic annotation parameter.
     * For example:
     * <p>
     * <pre>assertThat(objectCheese, hasMethodAnnotationParameter("getCheese", CheeseAnnotation.class, "country", java.util.Locale.FRANCE.getClass()))</pre>
     *
     * @param methodName               the name of the method to look for
     * @param annotationClazz          the class of the annotation
     * @param annotationParameterKey   the name of key for that annotation parameter
     * @param annotationParameterValue the value of the annotation parameter
     *
     * @return a matcher
     */
    public static org.hamcrest.Matcher<Object> hasMethodAnnotationParameter(String methodName, Class<?> annotationClazz, String annotationParameterKey,
                                                                            Object annotationParameterValue) {
        return HasMethodAnnotationParameter.hasMethodAnnotationParameter(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
    }
}
