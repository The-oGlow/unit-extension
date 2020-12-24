package org.hamcrest.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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

    private       HasMethodAnnotation<?>       o2t;
    private       HasMethodAnnotationTestClazz o2tClazz;
    private final String                       methodName                  = "methodWithAnnnotation";
    private final String                       methodNameWithoutAnnotation = "methodWithoutAnnotation";
    private final String                       wrongMethodName             = "wrongMethodName";
    private final Class<?>                     annotationClazz             = Ignore.class;

    @Before
    public void setUp() {
        o2tClazz = new HasMethodAnnotationTestClazz();
    }

    @Test
    public void testCreateHasMethodAnnotation() {
        o2t = prepareMatcher(methodName, annotationClazz);
        assertThat(o2t, notNullValue());
    }

    @Test
    public void testMatchesAnnotationFound() {
        matches(true, o2tClazz, methodName, annotationClazz);
    }

    @Test
    public void testMatchesNoClazzGiven() {
        matches(false, null, methodName, annotationClazz);
    }

    @Test
    public void testMatchesMethodNameNotExists() {
        matches(false, o2tClazz, wrongMethodName, annotationClazz);
    }

    @Test
    public void testMatchesMethodNameWithoutAnnotation() {
        matches(false, o2tClazz, methodNameWithoutAnnotation, annotationClazz);
    }

    @Test
    public void testMatchesNoAnnotionClazzGiven() {
        matches(false, o2tClazz, methodName, null);
    }

    @Test
    public void testMatchesAnnotionClazzIsWrong() {
        matches(false, o2tClazz, methodName, String.class);
    }

    private Description prepareDescription() {
        return new StringDescription();
    }

    @Test
    public void testDescribeToIsChanged() {
        o2t = prepareMatcher(methodName, annotationClazz);
        Description description = prepareDescription();
        String desciptionBefore = description.toString();
        o2t.describeTo(description);

        assertThat(description.toString(), not(equalTo(desciptionBefore)));
    }

    @Test
    public void testDescribeMismatchWithNullItemIsChanged() {
        o2t = prepareMatcher(methodName, annotationClazz);
        Description mismatchDescription = prepareDescription();
        String desciptionBefore = mismatchDescription.toString();
        Object item = null;
        o2t.describeMismatch(item, mismatchDescription);

        assertThat(mismatchDescription.toString(), not(equalTo(desciptionBefore)));
    }

    @Test
    public void testDescribeMismatchWithItemIsChanged() {
        o2t = prepareMatcher(methodName, annotationClazz);
        Description mismatchDescription = prepareDescription();
        String desciptionBefore = mismatchDescription.toString();
        Object item = "Hello";
        o2t.describeMismatch(item, mismatchDescription);

        assertThat(mismatchDescription.toString(), not(equalTo(desciptionBefore)));
    }

    private HasMethodAnnotation<?> prepareMatcher(String matchMethodName, Class<?> matchAnnotationClazz) {
        return new HasMethodAnnotation<>(matchMethodName, matchAnnotationClazz);
    }

    private void matches(boolean expected, Object matchClazz, String matchMethodName, Class<?> matchAnnotationClazz) {
        o2t = prepareMatcher(matchMethodName, matchAnnotationClazz);
        assertThat(o2t.matches(matchClazz), equalTo(expected));
    }

}
