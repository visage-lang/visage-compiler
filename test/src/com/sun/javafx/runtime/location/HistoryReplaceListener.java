package com.sun.javafx.runtime.location;

import java.util.List;
import java.util.ArrayList;

import com.sun.javafx.runtime.sequence.Sequence;

/**
 * HistoryReplaceListener
 *
 * @author Brian Goetz
 */
public class HistoryReplaceListener<T> extends SequenceChangeListener<T> {
    private List<String> elements = new ArrayList<String>();

    public void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
        elements.add(String.format("[%d, %d] => %s", startPos, endPos, newElements == null ? "[ ]" : newElements.toString()));
    }

    public List<String> get() { return elements; }

    public void clear() { elements.clear(); }
}
