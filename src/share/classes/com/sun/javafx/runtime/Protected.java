package com.sun.javafx.runtime;

import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Protected
 */
@Retention(CLASS)
@Documented
@Target({METHOD, TYPE})
public @interface Protected {
}
