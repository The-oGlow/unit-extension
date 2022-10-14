package com.glowanet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used for test methods ONLY.
 * Marking a test method with this annotation will not execute this method AND
 * does not count the method as skipped.
 *
 * @see org.junit.Ignore
 * @see org.junit.Assume
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface ExcludeFromTesting {

    String assumeTrue() default "";
}
