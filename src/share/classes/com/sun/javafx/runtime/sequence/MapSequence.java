package com.sun.javafx.runtime.sequence;

/**
 * Provides a view of an underlying sequence by applying a mapping function to each element of the underlying
 * sequence. The mapping is done lazily, on each call to get(), rather than eagerly, so the time and space
 * construction costs of a MapSequence are O(1).  Mapped sequences should be constructed with the factory method
 * Sequences.map(), rather than with the MapSequence constructor.
 *
 * @author Brian Goetz
 */
class MapSequence<T, U> extends AbstractSequence<U> implements Sequence<U> {

    private final Sequence<T> sequence;
    private final SequenceMapper<T, U> mapper;

    public MapSequence(Class<U> clazz, Sequence<T> sequence, SequenceMapper<T, U> mapper) {
        super(clazz);
        this.sequence = sequence;
        this.mapper = mapper;
    }

    @Override
    public int size() {
        return sequence.size();
    }

    @Override
    public int getDepth() {
        return sequence.getDepth() + 1;
    }

    @Override
    public U get(int position) {
        return mapper.map(sequence, position, sequence.get(position));
    }
}
