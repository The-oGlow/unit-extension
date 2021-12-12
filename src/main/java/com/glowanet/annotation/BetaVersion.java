package com.glowanet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Annotation to specify, that this class is implemented, but not completed.
 * <p>
 * <strong>You can use it, but it may change without notification.</strong>
 *
 * @author Oliver Glowa
 * @since 0.10.000
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value = {PACKAGE, MODULE, TYPE})
public @interface BetaVersion {

    /**
     * @return some additional information
     */
    String notice() default "";

    /**
     * @return the author of this element
     */
    String author();
}
