package com.sun.javafx.runtime.location;

import java.util.List;
import java.util.ArrayList;

import com.sun.javafx.runtime.sequence.*;

/**
 * HistoryReplaceListener
 *
 * @author Brian Goetz
 */
public class HistoryReplaceListener<T> extends ChangeListener<T> {
    private List<String> elements = new ArrayList<String>();

    public void onChange(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
        newElements = Sequences.getNewElements(buffer, startPos, newElements);
        int lastPos = endPos-1;
        if (lastPos==-2) new Error("onChange startP:"+startPos+" endP:"+endPos+" old:"+oldValue+" newEl:"+newElements).printStackTrace();
        elements.add(String.format("[%d, %d] => %s", startPos, lastPos, newElements == null ? "[ ]" : newElements.toString()));
    }

    public List<String> get() { return elements; }

    public void clear() { elements.clear(); }
}
