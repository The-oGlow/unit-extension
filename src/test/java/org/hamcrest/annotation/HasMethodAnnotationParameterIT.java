package org.hamcrest.annotation;

import org.junit.Test;

public class HasMethodAnnotationParameterIT extends HasMethodAnnotationParameterTest {
    @Test
    public void testMatches_noClazzGiven_return_false() {
        verifyMatches(false, null, methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatches_methodNameNotExists_return_false() {
        verifyMatches(false, o2tClazz, wrongMethodName, annotationClazz, annotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatches_methodNameWithoutAnnotation_return_false() {
        verifyMatches(false, o2tClazz, methodWithoutAnnotation, annotationClazz, annotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatches_noAnnotionClazzGiven_return_false() {
        verifyMatches(false, o2tClazz, methodName, null, annotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatches_wrongAnnotationParameterKeyGiven_return_false() {
        verifyMatches(false, o2tClazz, methodName, annotationClazz, wrongAnnotationParameterKey, annotationParameterValue);
    }

    @Test
    public void testMatches_noAnnotationParameterKeyGiven_return_false() {
        verifyMatches(false, o2tClazz, methodName, annotationClazz, "", annotationParameterValue);
    }

    @Test
    public void testMatches_wrongAnnotationParameterValueGiven_return_false() {
        verifyMatches(false, o2tClazz, methodName, annotationClazz, annotationParameterKey, annotationParameterValueWrong);
    }

    @Test
    public void testMatches_noAnnotationParameterValueGiven_return_false() {
        verifyMatches(false, o2tClazz, methodName, annotationClazz, annotationParameterKey, null);
    }
}
