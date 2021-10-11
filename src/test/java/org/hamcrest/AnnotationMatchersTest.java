package org.hamcrest;

import com.glowa_net.util.hamcrest.AbstractPublicTest;
import org.hamcrest.annotation.HasMethodAnnotation;
import org.hamcrest.annotation.HasMethodAnnotationParameter;
import org.junit.Test;

/**
 * @see AnnotationMatchers
 */
public class AnnotationMatchersTest extends AbstractPublicTest {

    @Test
    public void testHasMethodAnnotation_return_aMatcher() {
        actual = AnnotationMatchers.hasMethodAnnotation(methodName, annotationClazz);
        verifyMatcher(HasMethodAnnotation.class);
    }

    @Test
    public void testHasMethodAnnotationParameter_return_aMatcher() {
        String annotationParameterKey = "";
        String annotationParameterValue = "";

        actual = AnnotationMatchers.hasMethodAnnotationParameter(methodName, annotationClazz, annotationParameterKey, annotationParameterValue);
        verifyMatcher(HasMethodAnnotationParameter.class);
    }
}
