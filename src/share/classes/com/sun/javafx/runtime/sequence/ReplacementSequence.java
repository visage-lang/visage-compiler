package com.sun.javafx.runtime.sequence;

/**
 * ReplacementSequence
 *
 * @author Brian Goetz
 */
class ReplacementSequence<T> extends DerivedSequence<T> implements Sequence<T> {
    private final int newIndex;
    private final T newValue;

    public ReplacementSequence(Sequence<T> sequence, int newIndex, T newValue) {
        super(sequence.getElementType(), sequence);
        this.newIndex = newIndex;
        this.newValue = newValue;
    }

    public T get(int position) {
        return (position == newIndex) ? newValue : sequence.get(position);
    }
}
