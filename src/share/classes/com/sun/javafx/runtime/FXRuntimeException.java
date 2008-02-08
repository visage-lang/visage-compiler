package com.sun.javafx.runtime;

/**
 * FXRuntimeException
 *
 * @author Brian Goetz
 */
public class FXRuntimeException extends RuntimeException {
    public FXRuntimeException() {
    }

    public FXRuntimeException(String message) {
        super(message);
    }

    public FXRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FXRuntimeException(Throwable cause) {
        super(cause);
    }
}
