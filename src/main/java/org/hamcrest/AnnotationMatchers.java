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

    /**
     * Singleton with only static methods has no public constructor.
     */
    private AnnotationMatchers() {
    }

    /**
     * Creates a matcher that matches if the examined {@link Object} has the specified method with the specific annotation.
     * <p>
     * For example:
     * <pre>assertThat(objectCheese, hasMethodAnnotation("getCheese", CheeseAnnotation.class))</pre>
     *
     * @param methodName      the name of the method to look for
     * @param annotationClazz the class of the annotation
     * @param <T>             the type of {@code annotationClazz}
     *
     * @return newly created matcher
     */
    public static <T> org.hamcrest.Matcher<T> hasMethodAnnotation(String methodName, Class<T> annotationClazz) {
        return HasMethodAnnotation.hasMethodAnnotation(methodName, annotationClazz);
    }

    /**
     * Creates a matcher that matches if the examined {@link Object} has the specified method with the specific annotation and a specfic annotation parameter.
     * <p>
     * For example:
     * <pre>assertThat(objectCheese, hasMethodAnnotationParameter("getCheese", CheeseAnnotation.class, "country", java.util.Locale.FRANCE.getClass()))</pre>
     *
     * @param methodName               the name of the method to look for
     * @param annotationClazz          the class of the annotation
     * @param annotationParameterKey   the name of key for that annotation parameter
     * @param annotationParameterValue the value of the annotation parameter
     * @param <T>                      type of the values
     *
     * @return newly created matcher
     */
    public static <T> org.hamcrest.Matcher<T> hasMethodAnnotationParameter(
            String methodName, Class<T> annotationClazz, String annotationParameterKey,
            Object annotationParameterValue) {
        return HasMethodAnnotationParameter.hasMethodAnnotationParameter(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
    }
}
