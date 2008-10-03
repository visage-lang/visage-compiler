package com.sun.javafx.runtime.location;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import com.sun.javafx.runtime.util.Linkable;

/**
 * WeakLink
 *
 * @author Brian Goetz
 */
class WeakLocation extends WeakReference<Location> implements Linkable<WeakLocation, AbstractLocation> {
    static ReferenceQueue<Location> refQ = new ReferenceQueue<Location>();
    WeakLocation next;
    AbstractLocation host;

    WeakLocation(Location referent) {
        super(referent, refQ);
    }

    public WeakLocation getNext() {
        return next;
    }

    public void setNext(WeakLocation next) {
        this.next = next;
    }

    public AbstractLocation getHost() {
        return host;
    }

    public void setHost(AbstractLocation host) {
        this.host = host;
    }
}
