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

    protected       HasMethodAnnotationParameter<?>       o2t;
    protected       HasMethodAnnotationParameterTestClazz o2tClazz;
    protected final String                                methodName                    = "methodWithAnnnotationTest";
    protected final String                                methodWithoutAnnotation       = "methodWithoutAnnotation";
    protected final String                                wrongMethodName               = "wrongMethodName";
    protected final String                                wrongAnnotationParameterKey   = "wrongAnnotationParameterKey";
    protected final Class<?>                              annotationClazz               = HasMethodAnnotationParameterTestAnnotation.class;
    protected final String                                annotationParameterKey        = "expected";
    protected final int                                   annotationParameterValue      = 1;
    protected final Long                                  annotationParameterValueWrong = 2L;

    @Before
    public void setUp() throws Exception {
        o2tClazz = new HasMethodAnnotationParameterTestClazz();

    }

    protected void verifyMatches(boolean expected, Object matchClazz, String matchMethodName, Class<?> matchAnnotationClazz, String matchAnnotationParameterKey,
                                 Object matchAnnotationParameterValue) {
        o2t = prepareMatcher(matchMethodName, matchAnnotationClazz, matchAnnotationParameterKey, matchAnnotationParameterValue);
        assertThat(o2t.matches(matchClazz), equalTo(expected));
    }

    protected <T> HasMethodAnnotationParameter<T> prepareMatcher(String matchMethodName, Class<T> matchAnnotationClazz, String matchAnnotationParameterKey,
                                                                 Object matchAnnotationParameterValue) {
        return (HasMethodAnnotationParameter<T>) HasMethodAnnotationParameter.hasMethodAnnotationParameter(matchMethodName, matchAnnotationClazz, matchAnnotationParameterKey, matchAnnotationParameterValue);

    }

    protected Description prepareDescription() {
        return new StringDescription();
    }

    @Test
    public void testCreate_matcher_created() {
        o2t = prepareMatcher(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
        assertThat(o2t, notNullValue());
    }

    @Test
    public void testMatches_annotationParameter_found() {
        verifyMatches(true, o2tClazz, methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testDescribeTo_description_isChanged() {
        o2t = prepareMatcher(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
        Description description = prepareDescription();
        String desciptionBefore = description.toString();
        o2t.describeTo(description);

        assertThat(description.toString(), not(equalTo(desciptionBefore)));
    }

    @Test
    public void testDescribeMismatch_withNullItem_description_IsChanged() {
        o2t = prepareMatcher(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
        Description mismatchDescription = prepareDescription();
        String desciptionBefore = mismatchDescription.toString();
        Object item = null;
        o2t.describeMismatch(item, mismatchDescription);

        assertThat(mismatchDescription.toString(), not(equalTo(desciptionBefore)));
    }

    @Test
    public void testDescribeMismatch_withItem_description_isChanged() {
        o2t = prepareMatcher(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
        Description mismatchDescription = prepareDescription();
        String desciptionBefore = mismatchDescription.toString();
        Object item = "Hello";
        o2t.describeMismatch(item, mismatchDescription);

        assertThat(mismatchDescription.toString(), not(equalTo(desciptionBefore)));
    }
}
