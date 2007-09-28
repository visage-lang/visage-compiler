package com.sun.javafx.runtime.location;

import junit.framework.Assert;

import java.util.List;
import java.util.ArrayList;

/**
 * SequenceHistoryListener
 *
 * @author Brian Goetz
 */
public class HistorySequenceListener<T> implements SequenceChangeListener<T> {
    private List<String> elements = new ArrayList<String>();

    public void onInsert(int position, T element) {
        elements.add(String.format("i-%d-%s", position, element.toString()));
    }

    public void onDelete(int position, T element) {
        elements.add(String.format("d-%d-%s", position, element.toString()));
    }

    public void onReplace(int position, T oldValue, T newValue) {
        elements.add(String.format("r-%d-%s-%s", position, oldValue.toString(), newValue.toString()));
    }

    public List<String> get() { return elements; }

    public void clear() { elements.clear(); }

    public boolean onChange() {
        elements.clear();
        return true;
    }
}
