package com.sun.javafx.runtime.location;

/**
 * CountingListener
 *
 * @author Brian Goetz
 */
class CountingListener implements ChangeListener {
    public int count;

    public void onChange() {
        ++count;
    }
}
