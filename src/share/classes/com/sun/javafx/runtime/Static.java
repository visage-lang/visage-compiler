package com.sun.javafx.runtime;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.CLASS;
import java.lang.annotation.Target;

/**
 * Static
 */
@Retention(CLASS)
@Documented
@Target({METHOD, TYPE})
public @interface Static {
}
