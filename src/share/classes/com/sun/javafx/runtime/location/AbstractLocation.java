package com.sun.javafx.runtime.location;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.lang.ref.WeakReference;

/**
 * AbstractLocation is a base class for Location implementations, handling change listener notification and lazy updates.
 *
 * @author Brian Goetz
 */
public abstract class AbstractLocation implements Location, ChangeListener {
    private boolean isValid;
    private final boolean isLazy;
    protected List<WeakReference<ChangeListener>> listeners;

    protected AbstractLocation(boolean valid, boolean lazy) {
        isValid = valid;
        isLazy = lazy;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isLazy() {
        return isLazy;
    }

    protected void setValid() {
        isValid = true;
    }

    public void invalidate() {
        isValid = false;
        if (!isLazy())
            update();
        valueChanged();
    }

    protected void valueChanged() {
        if (listeners != null) {
            for (Iterator<WeakReference<ChangeListener>> iterator = listeners.iterator(); iterator.hasNext();) {
                WeakReference<ChangeListener> ref = iterator.next();
                ChangeListener listener = ref.get();
                if (listener == null) {
                    iterator.remove();
                    continue;
                }
                listener.onChange();
            }
        }
    }

    public void onChange() {
        invalidate();
    }

    public void addChangeListener(ChangeListener listener) {
        if (listeners == null)
            listeners = new LinkedList<WeakReference<ChangeListener>>();
        listeners.add(new WeakReference<ChangeListener>(listener));
    }

    public void update() {
    }

    // For testing 
    int getListenerCount() {
        return listeners == null ? 0 : listeners.size();
    }
}
