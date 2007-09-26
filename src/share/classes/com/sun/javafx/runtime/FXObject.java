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
    /** Set default values for any fields that have not already been assigned a value by the object literal.  For
     * any defaults set, setDefaults$ should call InitHelper.addDefaulted(location) to indicate the default was used.
     */
    public void setDefaults$(InitHelper<?> helper);

    /** Run the user-specified init { } blocks */
    public void userInit$();

}
