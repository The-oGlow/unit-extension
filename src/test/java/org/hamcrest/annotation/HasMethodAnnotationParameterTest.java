package org.hamcrest.annotation;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@SuppressWarnings({"FieldCanBeLocal"})
public class HasMethodAnnotationParameterTest {

    @Retention(RetentionPolicy.RUNTIME)
    @interface HasMethodAnnotationParameterTestAnnotation {

        int expected() default 0;

    }

    protected static class HasMethodAnnotationParameterTestClazz {

        boolean methodWithoutAnnotation() {
            return true;
        }

        @HasMethodAnnotationParameterTestAnnotation(expected = 1)
        public boolean methodWithAnnnotationTest() {
            return false;
        }
    }

    private       HasMethodAnnotationParameter<?>       o2t;
    private       HasMethodAnnotationParameterTestClazz o2tClazz;
    private final String                                methodName                    = "methodWithAnnnotationTest";
    private final String                                methodWithoutAnnotation       = "methodWithoutAnnotation";
    private final String                                wrongMethodName               = "wrongMethodName";
    private final String                                wrongAnnotationParameterKey   = "wrongAnnotationParameterKey";
    private final Class<?>                              annotationClazz               = HasMethodAnnotationParameterTestAnnotation.class;
    private final String                                annotationParameterKey        = "expected";
    private final int                                   annotationParameterValue      = 1;
    private final Long                                  annotationParameterValueWrong = 2L;

    @Before
    public void setUp() throws Exception {
        o2tClazz = new HasMethodAnnotationParameterTestClazz();

    }

    @Test
    public void testCreateHasMethodAnnotationParameter() {
        o2t = prepareMatcher(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
        assertThat(o2t, notNullValue());
    }

    @Test
    public void testMatchesAnnotationParameterFound() {
        matches(true, o2tClazz, methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatchesNoClazzGiven() {
        matches(false, null, methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatchesMethodNameNotExists() {
        matches(false, o2tClazz, wrongMethodName, annotationClazz, annotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatchesMethodNameWithoutAnnotation() {
        matches(false, o2tClazz, methodWithoutAnnotation, annotationClazz, annotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatchesNoAnnotionClazzGiven() {
        matches(false, o2tClazz, methodName, null, annotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatchesWrongAnnotionClazzGiven() {
        matches(false, o2tClazz, methodName, String.class, annotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatchesWrongAnnotationParameterKeyGiven() {
        matches(false, o2tClazz, methodName, annotationClazz, wrongAnnotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatchesNoAnnotationParameterKeyGiven() {
        matches(false, o2tClazz, methodName, annotationClazz, "", annotationParameterValue);
    }

    @Test
    public void testMatchesWrongAnnotationParameterValueGiven() {
        matches(false, o2tClazz, methodName, annotationClazz, annotationParameterKey, annotationParameterValueWrong);
    }

    @Test
    public void testMatchesNoAnnotationParameterValueGiven() {
        matches(false, o2tClazz, methodName, annotationClazz, annotationParameterKey, null);
    }

    private Description prepareDescription() {
        return new StringDescription();
    }

    @Test
    public void testDescribeToIsChanged() {
        o2t = prepareMatcher(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
        Description description = prepareDescription();
        String desciptionBefore = description.toString();
        o2t.describeTo(description);

        assertThat(description.toString(), not(equalTo(desciptionBefore)));
    }

    @Test
    public void testDescribeMismatchWithNullItemIsChanged() {
        o2t = prepareMatcher(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
        Description mismatchDescription = prepareDescription();
        String desciptionBefore = mismatchDescription.toString();
        Object item = null;
        o2t.describeMismatch(item, mismatchDescription);

        assertThat(mismatchDescription.toString(), not(equalTo(desciptionBefore)));
    }

    @Test
    public void testDescribeMismatchWithItemIsChanged() {
        o2t = prepareMatcher(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
        Description mismatchDescription = prepareDescription();
        String desciptionBefore = mismatchDescription.toString();
        Object item = "Hello";
        o2t.describeMismatch(item, mismatchDescription);

        assertThat(mismatchDescription.toString(), not(equalTo(desciptionBefore)));
    }

    private void matches(boolean expected, Object matchClazz, String matchMethodName, Class<?> matchAnnotationClazz, String matchAnnotationParameterKey,
                         Object matchAnnotationParameterValue) {
        o2t = prepareMatcher(matchMethodName, matchAnnotationClazz, matchAnnotationParameterKey, matchAnnotationParameterValue);
        assertThat(o2t.matches(matchClazz), equalTo(expected));
    }

    private HasMethodAnnotationParameter<?> prepareMatcher(String matchMethodName, Class<?> matchAnnotationClazz, String matchAnnotationParameterKey,
                                                           Object matchAnnotationParameterValue) {
        return new HasMethodAnnotationParameter(matchMethodName, matchAnnotationClazz, matchAnnotationParameterKey, matchAnnotationParameterValue);

    }

}
