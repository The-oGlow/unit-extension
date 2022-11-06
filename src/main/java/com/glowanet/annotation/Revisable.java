package com.glowanet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.MODULE;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Annotation to specify, that this element can be used, but should be refactored
 *
 * @since 0.1.0
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value = {CONSTRUCTOR, METHOD, PACKAGE, MODULE, TYPE})
public @interface Revisable {

    /**
     * @return why is this element revisable
     */
    String reason();

    /**
     * @return the creator of this annotation tag
     */
    String creator();
}
