package com.sun.javafx.runtime.sequence;

/**
 * SubSequence
 *
 * @author Brian Goetz
 */
public class SubSequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private final Sequence<T> sequence;
    private final int offset;
    private final int length;

    public SubSequence(Sequence<T> sequence, int offset, int length) {
        super(sequence.getElementType());
        this.sequence = sequence;
        this.offset = offset;
        this.length = length;
    }

    public int size() {
        return length;
    }

    public T get(int position) {
        return sequence.get(position+offset);
    }
}
