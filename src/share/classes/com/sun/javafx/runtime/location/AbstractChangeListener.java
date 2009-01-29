package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.NumericTypeInfo;

/**
 * AbstractChangeListener
 *
 * @author Brian Goetz
 */
public abstract class AbstractChangeListener<T> extends AbstractLocationDependency {
    public abstract ObjectChangeListener<T> asObjectListener();
}

interface NumericChangeListener {
    public<V extends Number> ObjectChangeListener<V> asObjectListener(NumericTypeInfo<V, ?> type);
}
