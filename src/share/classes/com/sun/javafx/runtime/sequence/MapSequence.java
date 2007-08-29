package com.sun.javafx.runtime.sequence;

/**
 * MapSequence
 *
 * @author Brian Goetz
 */
public class MapSequence<T,U> extends AbstractSequence<U> implements Sequence<U> {

    private final SequenceInternal<T> sequence;
    private final SequenceMapper<T,U> mapper;

    public MapSequence(Class<U> clazz, Sequence<T> sequence, SequenceMapper<T, U> mapper) {
        super(clazz);
        this.sequence = (SequenceInternal<T>) sequence;
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
