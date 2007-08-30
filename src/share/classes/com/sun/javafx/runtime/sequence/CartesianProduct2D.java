package com.sun.javafx.runtime.sequence;

/**
 * Special case for two-dimensional foreach comprehension when there are no where clauses on either list and
 * the foreach body always returns a single instance. The results are computed as needed rather than
 * precomputed, to save space.
 * 
 * @author Brian Goetz
 */
public class CartesianProduct2D<T, U, V> extends AbstractSequence<T> implements Sequence<T> {

    public interface Mapper<T, U, V> {
        public T map(int index1, U value1, int index2, V value2);
    }

    private final Sequence<U> seq1;
    private final Sequence<V> seq2;
    private final Mapper<T, U, V> mapper;

    public CartesianProduct2D(Class<T> clazz, Sequence<U> seq1, Sequence<V> seq2, Mapper<T, U, V> mapper) {
        super(clazz);
        this.seq1 = seq1;
        this.seq2 = seq2;
        this.mapper = mapper;
    }

    public int getDepth() {
        return Math.max(seq1.getDepth(), seq2.getDepth()) + 1;
    }

    public int size() {
        return seq1.size() * seq2.size();
    }

    public T get(int position) {
        int index1 = position / seq2.size();
        int index2 = position % seq2.size();
        return mapper.map(index1, seq1.get(index1), index2, seq2.get(index2));
    }
}
