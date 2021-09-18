package org.hamcrest;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("FieldCanBeLocal")
public class AnnotationMatchersTest {

    private final String   methodName               = "";
    private final Class<?> annotationClazz          = null;
    private final String   annotationParameterKey   = "";
    private final String   annotationParameterValue = "";

    @Test
    public void testHasMethodAnnotationReturnsAMatcher() {
        Matcher<? super Matcher<?>> expected = Matchers.instanceOf(Matcher.class);

        Matcher<?> actual = AnnotationMatchers.hasMethodAnnotation(methodName, annotationClazz);

        // if the matcher works correct, is irrelevant for this test.
        assertThat(actual, expected);
    }

    @Test
    public void testHasMethodAnnotationParameterReturnsAMatcher() {
        Matcher<? super Matcher<?>> expected = Matchers.instanceOf(Matcher.class);

        Matcher<?> actual = AnnotationMatchers.hasMethodAnnotationParameter(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);

        // if the matcher works correct, is irrelevant for this test.
        assertThat(actual, expected);
    }
}
