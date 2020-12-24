package org.hamcrest;

/**
 * Contains matchers, which are missing in the original class of {@link org.hamcrest.Matchers}.
 *
 * @author Oliver Glowa
 * @see org.hamcrest.Matchers
 * @since 0.10.000
 */
public class MatchersExtend extends org.hamcrest.Matchers {

    private MatchersExtend() {
    }

    /**
     * Creates a matcher for {@code T}s that matches, if the value is between a given range.
     * <p>
     * Range start and end <strong>NOT</strong>included.
     *
     * @param from start value for the range
     * @param to   end value for the range
     * @param <T>  type of the values
     *
     * @return newly created matcher
     */
    public static <T extends Comparable<T>> org.hamcrest.Matcher<T> between(final T from, final T to) {
        return org.hamcrest.BetweenMatcher.between(from, to);
    }

    /**
     * Creates a matcher for {@code T}s that matches, if the value is between a given range.
     * <p>
     * Range start and end <strong>IS</strong>included.
     *
     * @param from start value for the range
     * @param to   end value for the range
     * @param <T>  type of the values
     *
     * @return newly created matcher
     */
    public static <T extends Comparable<T>> org.hamcrest.Matcher<T> betweenWithBound(final T from, final T to) {
        return org.hamcrest.BetweenMatcher.betweenWithBound(from, to);
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
        return org.hamcrest.AnnotationMatchers.hasMethodAnnotation(methodName, annotationClazz);
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
        return org.hamcrest.AnnotationMatchers.hasMethodAnnotationParameter(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
    }

    /**
     * Creates a matcher for {@code B}, that matches when the examined {@link Object} has values for all of
     * its JavaBean properties that are equal to the corresponding values of the
     * specified bean.
     * <p>
     * On the contrairy to {@linkplain org.hamcrest.beans.SamePropertyValuesAs#samePropertyValuesAs(Object, String...)}
     * this matcher will report <strong>EVERY</strong> not matching property to the output.
     *
     * @param expectedBean the bean against which examined beans are compared
     * @param <B>          the type of the bean
     *
     * @return a matcher
     */
    public static <B> Matcher<B> hasSameValues(final B expectedBean) {
        return org.hamcrest.beans.HasSameValues.hasSameValues(expectedBean);
    }

}
