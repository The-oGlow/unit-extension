package org.hamcrest.annotation;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.glowa_net.util.validator.AnnotationParameterValidator;

/**
 * A matcher, which verifies if a class has a specific annotation.
 *
 * @param <T> the type of the class which will be checked
 *
 * @author Oliver Glowa
 * @see org.hamcrest.MatchersExtend
 * @see HasMethodAnnotationParameter
 * @since 0.02.000
 */
public class HasMethodAnnotation<T> extends BaseMatcher<T> {

    private final String                       methodName;
    private final Class<?>                     annotationClazz;
    private final AnnotationParameterValidator validator;

    public HasMethodAnnotation(String methodName, Class<?> annotationClazz) {
        this.methodName = methodName;
        this.annotationClazz = annotationClazz;
        this.validator = new AnnotationParameterValidator();
    }

    @Override
    public boolean matches(Object arg) {
        boolean isMatches = false;
        if (arg != null) {
            isMatches = validator.isAnnotation(arg.getClass(), methodName, annotationClazz);
        }
        return isMatches;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("methodName=").appendValue(methodName).appendText(", annotationClazz=").appendValue(annotationClazz);
    }

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("actual=NULL");
        } else {
            mismatchDescription.appendText("actual=").appendValue(item.getClass()).appendText(" doesn't have that methodName or that annotation.");
        }

    }

    /**
     * Creates a matcher that matches if the examined {@link Object} has the
     * specified method with the specific annotation. For example:
     * <p>
     * <pre>assertThat(objectCheese, hasMethodAnnotation("getCheese", CheeseAnnotation.class))</pre>
     *
     * @param methodName      the name of the method to look for
     * @param annotationClazz the class of the annotation
     *
     * @return a matcher
     */
    public static Matcher<Object> hasMethodAnnotation(String methodName, Class<?> annotationClazz) {
        return new HasMethodAnnotation<>(methodName, annotationClazz);
    }

}
