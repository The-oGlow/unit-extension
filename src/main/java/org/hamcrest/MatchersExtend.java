package org.hamcrest;

import org.hamcrest.core.IsBetween;
import org.junit.function.IThrowingRunnable;

import java.lang.annotation.Annotation;

/**
 * Contains matchers, which are missing in the original clazz of {@link org.hamcrest.Matchers}.
 *
 * @see org.hamcrest.Matchers
 * @since 0.1.0
 */
public class MatchersExtend extends org.hamcrest.Matchers {

    /**
     * Singleton with only static methods has no public constructor.
     */
    private MatchersExtend() {
    }

    /**
     * Creates a matcher for {@code T}s that matches, if the value is between the given range.
     * <p>
     * Range start and end <strong>NOT</strong>included.
     *
     * @param from start value for the range
     * @param to   end value for the range
     * @param <T>  type of the values
     * @return newly created matcher
     */
    public static <T extends Comparable<T>> org.hamcrest.Matcher<T> between(T from, T to) {
        return IsBetween.between(from, to);
    }

    /**
     * Creates a matcher for {@code T}s that matches, if the value is between the given range.
     * <p>
     * Range start and end <strong>NOT</strong>included.
     *
     * @param fromTo start and end value as {@code Range}
     * @param <T>    type of the values
     * @return newly created matcher
     */
    public static <T extends Comparable<T>> org.hamcrest.Matcher<T> between(IsBetween.Range<T> fromTo) {
        return IsBetween.between(fromTo);
    }

    /**
     * Creates a matcher for {@code T}s that matches, if the value is between a given range.
     * <p>
     * Range start and end <strong>IS</strong>included.
     *
     * @param from start value for the range
     * @param to   end value for the range
     * @param <T>  type of the values
     * @return newly created matcher
     */
    public static <T extends Comparable<T>> org.hamcrest.Matcher<T> betweenWithBound(T from, T to) {
        return org.hamcrest.core.IsBetweenWithBound.between(from, to);
    }

    /**
     * Creates a matcher for {@code T}s that matches, if the value is between a given range.
     * <p>
     * Range start and end <strong>IS</strong>included.
     *
     * @param fromTo start and end value as {@code Range}
     * @param <T>    type of the values
     * @return newly created matcher
     */
    public static <T extends Comparable<T>> org.hamcrest.Matcher<T> betweenWithBound(IsBetween.Range<T> fromTo) {
        return IsBetween.between(fromTo);
    }

    /**
     * Creates a matcher that matches if the examined {@link Object} has the specified method with the specific annotation.
     * <p>
     * For example:
     * <pre>assertThat(objectCheese, hasMethodAnnotation("getCheese", CheeseAnnotation.class))</pre>
     *
     * @param methodName      the name of the method to look for
     * @param annotationClazz the clazz of the annotation
     * @param <T>             type of the values
     * @return newly created matcher
     */
    public static <T extends Annotation> org.hamcrest.Matcher<T> hasMethodAnnotation(String methodName, Class<T> annotationClazz) {
        return org.hamcrest.annotation.HasMethodAnnotation.hasMethodAnnotation(methodName, annotationClazz);
    }

    /**
     * Creates a matcher that matches if the examined {@link Object} has the specified method with the specific annotation
     * and a specfic annotation parameter.
     * <p>
     * For example:
     * <pre>assertThat(objectCheese, hasMethodAnnotationParameter("getCheese", CheeseAnnotation.class, "country", java.util.Locale.FRANCE.getClass()))</pre>
     *
     * @param methodName               the name of the method to look for
     * @param annotationClazz          the clazz of the annotation
     * @param annotationParameterKey   the name of key for that annotation parameter
     * @param annotationParameterValue the value of the annotation parameter
     * @param <T>                      type of the values
     * @return newly created matcher
     */
    public static <T extends Annotation> org.hamcrest.Matcher<T> hasMethodAnnotationParameter(
            String methodName, Class<T> annotationClazz, String annotationParameterKey,
            Object annotationParameterValue) {
        return org.hamcrest.annotation.HasMethodAnnotationParameter.hasMethodAnnotationParameter(
                methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
    }

    /**
     * Creates a matcher for {@code B}, that matches when the examined {@link Object} has values for all of
     * its JavaBean properties that are equal to the corresponding values of the specified bean.
     * <p>
     * On the contrairy to {@linkplain org.hamcrest.beans.SamePropertyValuesAs#samePropertyValuesAs(Object, String...)}
     * this matcher will report <strong>EVERY</strong> not matching property to the output.
     *
     * @param expectedBean the bean against which examined beans are compared
     * @param <B>          the type of the bean
     * @return newly created matcher
     */
    public static <B> Matcher<B> hasSameValues(B expectedBean) {
        return org.hamcrest.beans.HasSameValues.hasSameValues(expectedBean);
    }

    /**
     * Creates a matcher, that matches when the examined {@link org.junit.function.ThrowingRunnable} has raised the {@code expectedException}.
     * <p>
     * For example:
     * <pre>assertThat(()-&gt;myFunction(), failWith(MyException.class))</pre>
     *
     * @param expectedException the clazz of the expected exception
     * @param <E>               type of the expected exception
     * @return newly created matcher
     */
    public static <E extends Throwable> Matcher<IThrowingRunnable<E>> failWith(Class<E> expectedException) {
        return org.hamcrest.core.FailWith.failWith(expectedException);
    }

    /**
     * Creates a matcher that matches when the examined object is an instance of the specified {@code type},
     * as determined by calling the {@link java.lang.Class#equals(Object)} method on that type, passing the
     * the examined object.
     *
     * <p>The created matcher forces a relationship between specified type and the examined object, and should be
     * used when it is necessary to make generics conform, for example in the JMock clause
     * {@code with(any(Thing.class))}</p>
     *
     * @param expectedClass the clazz against the object is compared
     * @return newly created matcher
     */
    public static <T> Matcher<T> anyExact(Class<T> expectedClass) {
        return org.hamcrest.core.IsInstanceOfExact.anyExact(expectedClass);
    }

    /**
     * Creates a matcher that matches when the examined object is an instance of the specified {@code type},
     * as determined by calling the {@link java.lang.Class#equals(Object)} method on that type, passing the
     * the examined object.
     *
     * @param expectedClass the clazz against the object is compared
     * @return newly created matcher
     */
    public static <T> Matcher<T> isInstanceOfExact(Class<?> expectedClass) {
        return org.hamcrest.core.IsInstanceOfExact.instanceOfExact(expectedClass);
    }
}
