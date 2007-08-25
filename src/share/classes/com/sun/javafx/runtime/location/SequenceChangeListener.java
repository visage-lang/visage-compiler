package com.sun.javafx.runtime.location;

/**
 * SequenceChangeListener
 *
 * @author Brian Goetz
 */
public interface SequenceChangeListener<T> extends ChangeListener {
    public void onInsert(T element);
    public void onDelete(T element);
    public void onReplace(int position, T oldValue, T newValue);
}
