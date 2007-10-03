package com.sun.javafx.runtime;

import java.lang.annotation.*;
import static java.lang.annotation.RetentionPolicy.*;
import static java.lang.annotation.ElementType.*;

/**
 * Public
 *
 * @author Brian Goetz
 */
@Retention(CLASS)
@Documented
@Target({METHOD, TYPE})
public @interface Public {
}
