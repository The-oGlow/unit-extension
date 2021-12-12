package com.glowanet.util.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Important Notice:
 * <p>
 * This test must be done with Mockito,
 * due to limits on Easymock (no spy-ing, can't mock toString()-Method)!
 */
public class AnnotationParameterValidatorTest {

    private AnnotationParameterValidator apv;

    private Class<?>                    clazzToCheck;
    private String                      findMethodName;
    private Class<? extends Annotation> findAnnotation;
    private String                      findAnnotationParameterKey;
    private String                      findAnnotationParameterValue;

    @Before
    public void setUp() {
        apv = new AnnotationParameterValidator();
        prepareSuccessfullTestData();

    }

    private void prepareSuccessfullTestData() {
        clazzToCheck = AnnotationParameterValidatorTestSample.class;
        findMethodName = AnnotationParameterValidatorTestSample.METHOD_WITH_ANNOTATION;
        findAnnotation = AnnotationParameterValidatorTestAnnotation.class;
        findAnnotationParameterKey = AnnotationParameterValidatorTestSample.parameterKey;
        findAnnotationParameterValue = AnnotationParameterValidatorTestSample.parameterValue;
    }

    private void prepareMock(Class<? extends Annotation> returnAnnotation) {
        apv = spy(AnnotationParameterValidator.class);

        Annotation mockAnnotation = returnAnnotation == null ? null : mock(returnAnnotation);
        if (mockAnnotation != null) {
            Answer<?> toStringAnswer = new Answer<Object>() {

                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    Object newAnswer = invocation.callRealMethod();
                    newAnswer = newAnswer + findAnnotationParameterKey + "=" + findAnnotationParameterValue;
                    return newAnswer;
                }
            };
            when(mockAnnotation.toString()).then(toStringAnswer);
        }
        when(apv.getAnnotation(any(), anyString(), any())).thenReturn(mockAnnotation);
    }

    @Test
    public void testIsAnnotationCorrect() {
        boolean expectedResult = true;
        // Simulating, we have found an annotation
        Class<? extends Annotation> returnAnnotation = AnnotationParameterValidatorTestAnnotation.class;

        verifyIsAnnotation(expectedResult, returnAnnotation);
    }

    @Test
    public void testIsAnnotationNotFound() {
        findMethodName = AnnotationParameterValidatorTestSample.METHOD_NO_ANNOTATION;
        boolean expectedResult = false;
        // Simulating, we have found nothing
        Class<? extends Annotation> returnAnnotation = null;

        verifyIsAnnotation(expectedResult, returnAnnotation);
    }

    private void verifyIsAnnotation(boolean expectedResult, Class<? extends Annotation> returnAnnotation) {
        prepareMock(returnAnnotation);

        boolean actual = apv.isAnnotation(clazzToCheck, findMethodName, findAnnotation);
        assertThat(actual, Matchers.equalTo(expectedResult));
    }

    @Test
    public void testIsAnnotationParameterCorrect() {
        String overrideParameterKey = null;
        boolean expectedResult = true;
        // Simulating, we have found an annotation
        Class<? extends Annotation> returnAnnotation = AnnotationParameterValidatorTestAnnotation.class;

        verifyTestIsAnnotationParameter(expectedResult, returnAnnotation, overrideParameterKey);
    }

    @Test
    public void testIsAnnotationParameterAnnotationNotFound() {
        String overrideParameterKey = null;
        findMethodName = AnnotationParameterValidatorTestSample.METHOD_NO_ANNOTATION;
        boolean expectedResult = false;
        // Simulating, we have found nothing
        Class<? extends Annotation> returnAnnotation = null;

        verifyTestIsAnnotationParameter(expectedResult, returnAnnotation, overrideParameterKey);
    }

    @Test
    public void testIsAnnotationParameterAnnotationParameterNotFound() {
        String overrideParameterKey = "DUMMY";
        boolean expectedResult = false;
        // Simulating, we have found an annotation
        Class<? extends Annotation> returnAnnotation = AnnotationParameterValidatorTestAnnotation.class;

        verifyTestIsAnnotationParameter(expectedResult, returnAnnotation, overrideParameterKey);
    }

    private void verifyTestIsAnnotationParameter(boolean expectedResult, Class<? extends Annotation> returnAnnotation, String overrideParameterKey) {
        prepareMock(returnAnnotation);

        boolean actual = apv.isAnnotationParameter(clazzToCheck, findMethodName, findAnnotation,
                (overrideParameterKey == null ? findAnnotationParameterKey : overrideParameterKey), findAnnotationParameterValue);
        assertThat(actual, Matchers.equalTo(expectedResult));
    }

    @Test
    public void testGetAnnotationFoundTheCorrectAnnotation() {
        findMethodName = AnnotationParameterValidatorTestSample.METHOD_WITH_ANNOTATION;
        Matcher<Annotation> expectedMatcher = instanceOf(findAnnotation);

        verifyGetAnnotation(expectedMatcher);
    }

    @Test
    public void testGetAnnotationNotFoundAnnotation() {
        findMethodName = AnnotationParameterValidatorTestSample.METHOD_NO_ANNOTATION;
        Matcher<Annotation> expectedMatcher = nullValue(Annotation.class);

        verifyGetAnnotation(expectedMatcher);
    }

    @Test
    public void testGetAnnotationNotFoundInvisibleAnnotation() {
        findMethodName = AnnotationParameterValidatorTestSample.METHOD_WITH_INVISIBLE_ANNOTATION;
        Matcher<Annotation> expectedMatcher = nullValue(Annotation.class);

        verifyGetAnnotation(expectedMatcher);
    }

    private void verifyGetAnnotation(Matcher<Annotation> matcher) {
        Annotation actual = apv.getAnnotation(clazzToCheck, findMethodName, findAnnotation);
        assertThat(actual, matcher);
    }
}
