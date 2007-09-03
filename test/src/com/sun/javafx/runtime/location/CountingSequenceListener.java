package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * CountingSequenceListener
 *
 * @author Brian Goetz
 */
class CountingSequenceListener implements SequenceChangeListener<Integer> {
    int changeCount, insertCount, deleteCount, replaceCount;
    Sequence<Integer> inserted = Sequences.emptySequence(Integer.class);
    Sequence<Integer> deleted = Sequences.emptySequence(Integer.class);

    public boolean onChange() {
        ++changeCount;
        return true;
    }

    public void onInsert(Integer element) {
        ++insertCount;
        inserted = inserted.insert(element);
    }

    public void onDelete(Integer element) {
        ++deleteCount;
        deleted = deleted.insert(element);
    }

    public void onReplace(int position, Integer oldValue, Integer newValue) {
        ++replaceCount;
    }
}
