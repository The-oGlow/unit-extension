package com.glowa_net.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Annotation to specify, that this element can be used, but should be refactored
 *
 * @author Oliver Glowa
 * @since 0.10.000
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
