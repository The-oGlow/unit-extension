package org.hamcrest.annotation;

import org.junit.Test;

public class HasMethodAnnotationIT extends HasMethodAnnotationTest {

    @Test
    public void testMatches_noClazzGiven_return_false() {
        verifyMatches(false, null, methodName, annotationClazz);
    }

    @Test
    public void testMatches_methodNameNotExists_return_false() {
        verifyMatches(false, o2tClazz, wrongMethodName, annotationClazz);
    }

    @Test
    public void testMatches_methodNameWithoutAnnotation_return_false() {
        verifyMatches(false, o2tClazz, methodNameWithoutAnnotation, annotationClazz);
    }

    @Test
    public void testMatches_noAnnotionClazzGiven_return_false() {
        verifyMatches(false, o2tClazz, methodName, null);
    }

    @Test
    public void testMatches_annotionClazzIsWrong_return_false() {
        verifyMatches(false, o2tClazz, methodName, String.class);
    }
}
