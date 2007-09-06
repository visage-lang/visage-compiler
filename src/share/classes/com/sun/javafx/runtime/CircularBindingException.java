package com.sun.javafx.runtime;

/**
 * CircularBindingException
 *
 * @author Brian Goetz
 */
public class CircularBindingException extends RuntimeException {

    public CircularBindingException() {
    }

    public CircularBindingException(String message) {
        super(message);
    }

    public CircularBindingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CircularBindingException(Throwable cause) {
        super(cause);
    }
}
