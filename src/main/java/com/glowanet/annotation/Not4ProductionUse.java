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
 * Annotation to indicate, that an element <strong>must not</strong> be used in production code.
 *
 * @since 0.1.0
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value = {CONSTRUCTOR, METHOD, PACKAGE, MODULE, TYPE})
public @interface Not4ProductionUse {

    /**
     * @return the reason, why this element must not be used in production code
     */
    String reason();

    /**
     * @return the author of this element
     */
    String author();
}
