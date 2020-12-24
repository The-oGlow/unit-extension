package com.glowa_net.util.validator;

class AnnotationParameterValidatorTestSample {

    public static final String METHOD_NO_ANNOTATION             = "methodNoAnnotation";
    public static final String METHOD_WITH_ANNOTATION           = "methodWithAnnotation";
    public static final String METHOD_WITH_INVISIBLE_ANNOTATION = "methodWithInvisibleAnnotation";
    public static final String parameterKey                     = "annotationKey";
    public static final String parameterValue                   = "annotationValue";

    public void methodNoAnnotation() {
        // Do nothing.
    }

    @AnnotationParameterValidatorTestAnnotation(annotationKey = parameterValue)
    public void methodWithAnnotation() {
        // Do still nothing.
    }

    @AnnotationParameterValidatorTestAnnotationInvisible
    public void methodWithInvisibleAnnotation() {
        // And still nothing to do.
    }
}
