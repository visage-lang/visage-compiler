package com.sun.javafx.ideaplugin;

import org.antlr.runtime.RecognitionException;

/**
 * RecognitionExceptionSignal
 *
 * @author Brian Goetz
 */ // This is a gross, hackish way to do a nonlocal return.  But other attempts have resulted in infinite loops, and this _seems_ to work...blech.
class RecognitionExceptionSignal extends RuntimeException {
    public final RecognitionException exception;

    RecognitionExceptionSignal(RecognitionException exception) {
        this.exception = exception;
    }
}
