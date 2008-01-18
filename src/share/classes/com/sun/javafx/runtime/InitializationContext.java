package com.sun.javafx.runtime;

/**
 * InitializationContext
 *
 * @author Brian Goetz
 */
public class InitializationContext {
    private int deferralDepth;
    private DeferredTrigger head, tail;

    public void deferTriggers() {
        ++deferralDepth;
    }

    public void undeferTriggers() {
        if (deferralDepth == 1) {
            try {
                while (head != null) {
                    DeferredTrigger first = head;
                    head = head.next;
                    first.run();
                }
                tail = null;
            }
            finally {
                --deferralDepth;
            }
        }
    }

    public boolean isDeferred() {
        return (deferralDepth > 0);
    }

    public void finished() {
        if (deferralDepth != 0)
            throw new IllegalStateException("InitializationContext.finish() called, triggers still deferred");
        else if (head != null || tail != null)
            throw new IllegalStateException("InitializationContext.finish() called, trigger list nonempty");
    }

    public void defer(DeferredTrigger trigger) {
        if (head == null)
            tail = head = trigger;
        else
            tail = tail.next = trigger;
    }
}
