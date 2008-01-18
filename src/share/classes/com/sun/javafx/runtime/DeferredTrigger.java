package com.sun.javafx.runtime;

/**
 * DeferredTrigger
 *
 * @author Brian Goetz
 */
public abstract class DeferredTrigger implements Runnable {
    DeferredTrigger next;
}
