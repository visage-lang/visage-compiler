package com.sun.javafx.runtime.sequence;

/**
 * SubSequence
 *
 * @author Brian Goetz
 */
public class SubSequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private final Sequence<T> sequence;
    private final int start;
    private final int end;

    public SubSequence(Sequence<T> sequence, int start, int end) {
        super(sequence.getElementType());
        this.sequence = sequence;
        this.start = Math.max(start, 0);
        this.end = Math.min(end, sequence.size());
    }

    @Override
    public int size() {
        return (start <= end) ? end - start : 0;
    }

    @Override
    public int getDepth() {
        return sequence.getDepth() + 1;
    }

    @Override
    public T get(int position) {
        if (position < 0 || position + start >= end)
            return nullValue;
        else
            return sequence.get(position + start);
    }
}
