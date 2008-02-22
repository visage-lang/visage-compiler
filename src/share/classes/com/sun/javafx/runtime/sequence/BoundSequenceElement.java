package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.*;

/**
 * Location representing bind(x[i]), where x and i are locations.  Does not fire change triggers for modifications that
 * do not affect the value of x[i], such as inserts after i or modifications to elements other than i.  
 *
 * @author Brian Goetz
 */
class BoundSequenceElement<T> extends ObjectVariable<T> implements ObjectLocation<T> {
    private final SequenceLocation<T> seq;
    private final IntLocation index;
    private int lastIndex;

    public BoundSequenceElement(SequenceLocation<T> seq, IntLocation index) {
        super();
        this.seq = seq;
        this.index = index;
        lastIndex = index.get();
        bind(false, new ObjectBindingExpression<T>() {
            public T computeValue() {
                lastIndex = BoundSequenceElement.this.index.get();
                return BoundSequenceElement.this.seq.getAsSequence().get(lastIndex);
            }
        }, index);
        seq.addChangeListener(new MySequenceListener());
    }

    private class MySequenceListener implements SequenceReplaceListener<T> {
        public void onReplace(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
            int deltaSize = (endPos-startPos+1) - Sequences.size(newElements);
            if (deltaSize != 0) {
                if (startPos <= lastIndex)
                    invalidate();
            }
            else {
                if (startPos <= lastIndex && lastIndex <= endPos)
                    invalidate();
            }
        }
    }
}
