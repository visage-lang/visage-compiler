package com.sun.javafx.runtime.location;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * AbstractLocation is a base class for Location implementations, handling change listener notification and lazy updates.
 *
 * @author Brian Goetz
 */
public abstract class AbstractLocation implements Location {
    private boolean isValid;
    private final boolean isLazy;
    protected List<ChangeListener> listeners;

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
            for (Iterator<ChangeListener> iterator = listeners.iterator(); iterator.hasNext();) {
                ChangeListener listener = iterator.next();
                if (!listener.onChange())
                    iterator.remove();
            }
        }
    }

    public void addChangeListener(ChangeListener listener) {
        if (listeners == null)
            listeners = new LinkedList<ChangeListener>();
        listeners.add(listener);
    }

    public void addWeakListener(ChangeListener listener) {
        addChangeListener(new WeakListener(listener));
    }

    public ChangeListener getWeakChangeListener() {
        return new WeakLocationListener(this);
    }

    public Collection<ChangeListener> getListeners() {
        if (listeners == null)
            return Collections.emptySet();
        else
            return Collections.unmodifiableCollection(listeners);
    }

    public void update() {
    }

    private static class WeakLocationListener extends WeakReference<Location> implements ChangeListener {
        public WeakLocationListener(Location referent) {
            super(referent);
        }

        public boolean onChange() {
            Location loc = get();
            if (loc == null)
                return false;
            else {
                loc.invalidate();
                return true;
            }
        }
    }
    
    private static class WeakListener extends WeakReference<ChangeListener> implements ChangeListener {

        public WeakListener(ChangeListener referent) {
            super(referent);
        }

        public boolean onChange() {
            ChangeListener listener = get();
            return listener == null ? false : listener.onChange();
        }
    }

    // For testing
    int getListenerCount() {
        return listeners == null ? 0 : listeners.size();
    }
}

