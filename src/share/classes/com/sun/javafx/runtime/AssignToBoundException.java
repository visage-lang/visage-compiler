package com.sun.javafx.runtime;

/**
 * Exception thrown when someone tries to assign a value to a variable that is unidirectionally bound.
 *
 * @author Brian Goetz
 */
public class AssignToBoundException extends BindingException {
    public AssignToBoundException() {
    }

    public AssignToBoundException(String message) {
        super(message);
    }

    public AssignToBoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssignToBoundException(Throwable cause) {
        super(cause);
    }
}
