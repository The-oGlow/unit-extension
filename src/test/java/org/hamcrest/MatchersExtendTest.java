package org.hamcrest;

import com.glowa_net.data.SimplePojo;
import org.hamcrest.annotation.HasMethodAnnotation;
import org.hamcrest.annotation.HasMethodAnnotationParameter;
import org.hamcrest.beans.HasSameValues;
import org.hamcrest.core.IsBetween;
import org.hamcrest.core.IsBetweenWithBound;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

public class MatchersExtendTest {

    private final Long     from                     = 1L;
    private final Long     to                       = 10L;
    private final String   methodName               = "fooMethod";
    private final Class<?> annotationClazz          = this.getClass();
    private final String   annotationParameterKey   = "fooParameter";
    private final Object   annotationParameterValue = "fooValue";
    private final Object   expectedBean             = new SimplePojo();

    @Test
    public void testBetween() {
        final Matcher<?> actual = MatchersExtend.between(from, to);
        verifyMatcher(actual, IsBetween.class);
    }

    @Test
    public void testBetweenWithBound() {
        final Matcher<?> actual = MatchersExtend.betweenWithBound(from, to);
        verifyMatcher(actual, IsBetweenWithBound.class);
    }

    @Test
    public void testHasMethodAnnotation() {
        final Matcher<?> actual = MatchersExtend.hasMethodAnnotation(methodName, annotationClazz);
        verifyMatcher(actual, HasMethodAnnotation.class);
    }

    @Test
    public void testHasMethodAnnotationParameter() {
        final Matcher<?> actual = MatchersExtend.hasMethodAnnotationParameter(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
        verifyMatcher(actual, HasMethodAnnotationParameter.class);
    }

    @Test
    public void testHaseSameValues() {
        final Matcher<?> actual = MatchersExtend.hasSameValues(expectedBean);
        verifyMatcher(actual, HasSameValues.class);
    }

    private void verifyMatcher(final Matcher<?> actual, final Class<?> expectedClazz) {
        assertThat(actual, notNullValue());
        assertThat(actual, instanceOf(expectedClazz));
    }
}
