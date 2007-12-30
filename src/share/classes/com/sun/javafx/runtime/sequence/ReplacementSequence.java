package com.sun.javafx.runtime.sequence;

/**
 * ReplacementSequence
 *
 * @author Brian Goetz
 */
class ReplacementSequence<T> extends AbstractSequence<T> implements Sequence<T> {
    private final Sequence<T> sequence;
    private final int newIndex;
    private final T newValue;

    public ReplacementSequence(Sequence<T> sequence, int newIndex, T newValue) {
        super(sequence.getElementType());
        this.sequence = sequence;
        this.newIndex = newIndex;
        this.newValue = newValue;
    }

    public int size() {
        return sequence.size();
    }

    public T get(int position) {
        return (position == newIndex) ? newValue : sequence.get(position);
    }

    @Override
    public int getDepth() {
        return sequence.getDepth() + 1;
    }
}
