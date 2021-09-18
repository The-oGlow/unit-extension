package org.hamcrest.annotation;

import com.glowa_net.util.validator.AnnotationParameterValidator;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

;

/**
 * A matcher, which verifies if a class has a specific annotation-parameter and value.
 *
 * @param <T> the type of the class which will be checked
 *
 * @author Oliver Glowa
 * @see org.hamcrest.MatchersExtend
 * @see HasMethodAnnotation
 * @since 0.02.000
 */
public class HasMethodAnnotationParameter<T> extends BaseMatcher<T> {

    private final String                       methodName;
    private final Class<T>                     annotationClazz;
    private final AnnotationParameterValidator validator;
    private final String                       annotationParameterKey;
    private final Object                       annotationParameterValue;

    /**
     * Creates a matcher that matches if the examined {@link Object} has the
     * specified method with the specific annotation and a specfic annotation
     * parameter. For example:
     * <p>
     * <pre>assertThat(objectCheese, hasMethodAnnotationParameter("getCheese", CheeseAnnotation.class, "country", java.util.Locale.FRANCE.getCountry()))</pre>
     *
     * @param methodName               the name of the method to check for the annotation
     * @param annotationClazz          the class of the {@code methodName}
     * @param annotationParameterKey   the name of the annotation-parameter
     * @param annotationParameterValue the value of the annotation-parameter
     * @param <T>
     *
     * @return a matcher
     */
    public static <T> HasMethodAnnotationParameter<T> hasMethodAnnotationParameter(String methodName, Class<T> annotationClazz, String annotationParameterKey,
                                                                                   Object annotationParameterValue) {
        return new HasMethodAnnotationParameter<>(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
    }

    /**
     * @param methodName               the name of the method to check for the annotation
     * @param annotationClazz          the class of the {@code methodName}
     * @param annotationParameterKey   the name of the annotation-parameter
     * @param annotationParameterValue the value of the annotation-parameter
     */
    private HasMethodAnnotationParameter(String methodName, Class<T> annotationClazz, String annotationParameterKey, Object annotationParameterValue) {
        this.methodName = methodName;
        this.annotationClazz = annotationClazz;
        this.annotationParameterKey = annotationParameterKey;
        this.annotationParameterValue = annotationParameterValue;
        this.validator = new AnnotationParameterValidator();
    }

    @Override
    public boolean matches(Object arg) {
        boolean isMatches = false;
        if (arg != null && annotationParameterValue != null) {
            isMatches = validator
                    .isAnnotationParameter(arg.getClass(), methodName, annotationClazz, annotationParameterKey, annotationParameterValue.toString());
        }
        return isMatches;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("methodName=").appendValue(methodName).appendText(", annotationClazz=").appendValue(annotationClazz)
                .appendText(", annotationParameterKey=").appendValue(annotationParameterKey).appendText(", annotationParameterValue=")
                .appendValue(annotationParameterValue);
    }

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("actual=NULL");
        } else {
            mismatchDescription.appendText("actual=").appendValue(item.getClass())
                    .appendText(" doesn't have that methodName, that annotation or that annotation-parameter");
        }

    }

}
