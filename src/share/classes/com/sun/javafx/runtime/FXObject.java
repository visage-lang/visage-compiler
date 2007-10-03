package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.Location;

import java.util.List;

/**
 * All FX interfaces must extend FXObject; it acts as a marker interface, and also includes methods required for
 * object lifecyle.
 *
 * @author Brian Goetz
 */
public interface FXObject {
    /** Set default values, set up change triggers, and run init blocks */
    public void initialize$();
}
