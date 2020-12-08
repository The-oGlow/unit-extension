package com.glowa_net.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Annotation to indicate, that an element <strong>must not</strong> be used in production code.
 *
 * @author Oliver Glowa
 * @since 0.10.000
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
