package com.sun.javafx.runtime.location;

/**
 * WeakMeLocation
 *
 * @author Brian Goetz
 */
class WeakMeLocation extends WeakLocation {
    // Simple linked list; no need to add at end, just push the new one at the head
    WeakMeLocation nextWeakMe;

    WeakMeLocation(Location referent) {
        super(referent);
    }

    public WeakMeLocation getNextWeakMe() {
        return nextWeakMe;
    }

    public void setNextWeakMe(WeakMeLocation nextWeakMe) {
        this.nextWeakMe = nextWeakMe;
    }
}
