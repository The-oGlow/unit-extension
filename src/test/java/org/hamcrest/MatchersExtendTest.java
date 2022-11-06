package org.hamcrest;

import com.glowanet.util.hamcrest.AbstractPublicTest;
import org.hamcrest.annotation.HasMethodAnnotation;
import org.hamcrest.annotation.HasMethodAnnotationParameter;
import org.hamcrest.beans.HasSameValues;
import org.hamcrest.core.FailWith;
import org.hamcrest.core.IsBetween;
import org.hamcrest.core.IsBetweenWithBound;
import org.junit.Test;

/**
 * @see MatchersExtend
 */
public class MatchersExtendTest extends AbstractPublicTest {

    @Test
    public void testBetween_return_aMatcher() {
        actual = MatchersExtend.between(from, to);
        verifyMatcher(IsBetween.class);
    }

    @Test
    public void testBetweenRange_return_aMatcher() {
        actual = MatchersExtend.between(rangeFromTo);
        verifyMatcher(IsBetween.class);
    }

    @Test
    public void testBetweenWithBound_return_aMatcher() {
        actual = MatchersExtend.betweenWithBound(from, to);
        verifyMatcher(IsBetweenWithBound.class);
    }

    @Test
    public void testBetweenWithBoundRange_return_aMatcher() {
        actual = MatchersExtend.betweenWithBound(rangeFromTo);
        verifyMatcher(IsBetweenWithBound.class);
    }

    @Test
    public void testHasMethodAnnotation_return_aMatcher() {
        actual = MatchersExtend.hasMethodAnnotation(methodName, annotationClazz);
        verifyMatcher(HasMethodAnnotation.class);
    }

    @Test
    public void testHasMethodAnnotationParameter_return_aMatcher() {
        actual = MatchersExtend.hasMethodAnnotationParameter(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
        verifyMatcher(HasMethodAnnotationParameter.class);
    }

    @Test
    public void testHasSameValues_return_aMatcher() {
        actual = MatchersExtend.hasSameValues(expectedBean);
        verifyMatcher(HasSameValues.class);
    }

    @Test
    public void testFailWith_return_aMatcher() {
        actual = MatchersExtend.failWith(IllegalArgumentException.class);
        verifyMatcher(FailWith.class);
    }
}
