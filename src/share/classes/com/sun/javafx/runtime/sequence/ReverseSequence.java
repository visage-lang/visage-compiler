package com.sun.javafx.runtime.sequence;

/**
 * Provides a view of another sequence with the elements in reverse order.  Reversed sequences should be created
 * with the static factory Sequences.reverse() rather than with the ReverseSequence constructor.  O(1) space and time
 * construction costs.
 *
 * @author Brian Goetz
 */
class ReverseSequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private final Sequence<T> sequence;
    private final int size;

    public ReverseSequence(Sequence<T> sequence) {
        super(sequence.getElementType());
        this.sequence = sequence;
        this.size = sequence.size();
    }

    @Override
    public int size() {
        return size;
    }


    @Override
    public int getDepth() {
        return sequence.getDepth() + 1;
    }

    @Override
    public T get(int position) {
        return sequence.get(size - 1 - position);
    }
}

