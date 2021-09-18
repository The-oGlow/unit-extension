package org.hamcrest.annotation;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class HasMethodAnnotationTest {

    protected static class HasMethodAnnotationTestClazz {

        boolean methodWithoutAnnotation() {
            return true;
        }

        @SuppressWarnings("java:S1607")
        @Ignore
        boolean methodWithAnnnotation() {
            return false;
        }
    }

    protected       HasMethodAnnotation<?>       o2t;
    protected       HasMethodAnnotationTestClazz o2tClazz;
    protected final String                       methodName                  = "methodWithAnnnotation";
    protected final String                       methodNameWithoutAnnotation = "methodWithoutAnnotation";
    protected final String                       wrongMethodName             = "wrongMethodName";
    protected final Class<?>                     annotationClazz             = Ignore.class;

    @Before
    public void setUp() {
        o2tClazz = new HasMethodAnnotationTestClazz();
    }

    protected Description prepareDescription() {
        return new StringDescription();
    }

    protected <T> HasMethodAnnotation<T> prepareMatcher(String matchMethodName, Class<T> matchAnnotationClazz) {
        return (HasMethodAnnotation<T>) HasMethodAnnotation.hasMethodAnnotation(matchMethodName, matchAnnotationClazz);
    }

    protected void verifyMatches(boolean expected, Object matchClazz, String matchMethodName, Class<?> matchAnnotationClazz) {
        o2t = prepareMatcher(matchMethodName, matchAnnotationClazz);
        assertThat(o2t.matches(matchClazz), equalTo(expected));
    }

    @Test
    public void testCreate_matcher_created() {
        o2t = (HasMethodAnnotation<?>) HasMethodAnnotation.hasMethodAnnotation(methodName, annotationClazz);
        assertThat(o2t, notNullValue());
    }

    @Test
    public void testMatches_annotation_found() {
        verifyMatches(true, o2tClazz, methodName, annotationClazz);
    }

    @Test
    public void testDescribeTo_description_isChanged() {
        o2t = prepareMatcher(methodName, annotationClazz);
        Description description = prepareDescription();
        String desciptionBefore = description.toString();
        o2t.describeTo(description);

        assertThat(description.toString(), not(equalTo(desciptionBefore)));
    }

    @Test
    public void testDescribeMismatch_withNullItem_description_isChanged() {
        o2t = prepareMatcher(methodName, annotationClazz);
        Description mismatchDescription = prepareDescription();
        String desciptionBefore = mismatchDescription.toString();
        Object item = null;
        o2t.describeMismatch(item, mismatchDescription);

        assertThat(mismatchDescription.toString(), not(equalTo(desciptionBefore)));
    }


    @Test
    public void testDescribeMismatch_withItem_desctiption_isChanged() {
        o2t = prepareMatcher(methodName, annotationClazz);
        Description mismatchDescription = prepareDescription();
        String desciptionBefore = mismatchDescription.toString();
        Object item = "Hello";
        o2t.describeMismatch(item, mismatchDescription);

        assertThat(mismatchDescription.toString(), not(equalTo(desciptionBefore)));
    }
}
