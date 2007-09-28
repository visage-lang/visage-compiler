package com.sun.javafx.runtime.location;

/**
 * SequenceChangeListener extends ChangeListener, adding sequence-specific notification methods 
 *
 * @author Brian Goetz
 */
public interface SequenceChangeListener<T> extends ChangeListener {
    public void onInsert(int position, T element);
    public void onDelete(int position, T element);
    public void onReplace(int position, T oldValue, T newValue);
}
