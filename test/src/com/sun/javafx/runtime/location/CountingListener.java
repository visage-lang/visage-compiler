package com.sun.javafx.runtime.location;

/**
 * CountingListener
 *
 * @author Brian Goetz
 */
class CountingListener implements ChangeListener {
    public int count;

    public boolean onChange() {
        ++count;
        return true;
    }
}
