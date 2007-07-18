package com.sun.javafx.runtime.bind;

/**
 * BindingException
 *
 * @author Brian Goetz
 */
public class BindingException extends RuntimeException {

    public BindingException() {
    }

    public BindingException(String message) {
        super(message);
    }

    public BindingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BindingException(Throwable cause) {
        super(cause);
    }
}
