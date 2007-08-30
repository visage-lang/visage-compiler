package com.sun.javafx.runtime.sequence;

/**
 * Special case for n-dimensional foreach comprehension when there are no where clauses on any list and
 * the foreach body always returns a single instance. The results are computed as needed rather than
 * precomputed, to save space.
 *
 * @author Brian Goetz
 */
public class CartesianProduct<T> extends AbstractSequence<T> implements Sequence<T> {

    public interface Mapper<T> {
        public T map(int[] indexes, Object[] values);
    }

    private final Sequence<?>[] sequences;
    private final Mapper<T> mapper;
    private final int size;
    private final int[] sizes;

    public CartesianProduct(Class<T> clazz, Mapper<T> mapper, Sequence<?>... sequences) {
        super(clazz);
        this.sequences = sequences;
        this.mapper = mapper;
        if (sequences.length == 0)
            size = 0;
        else {
            int depth = 1;
            for (Sequence<?> seq : sequences)
                depth = depth * seq.size();
            size = depth;
        }
        sizes = new int[sequences.length];
        for (int i=0; i<sequences.length; i++) {
            int cur = 1;
            for (int j=i+1; j<sequences.length; j++)
                cur *= sequences[j].size();
            sizes[i] = cur;
        }
    }

    public int getDepth() {
        int depth = 0;
        for (Sequence<?> seq : sequences)
            depth = Math.max(depth, seq.getDepth());
        return depth + 1;
    }

    public int size() {
        return size;
    }

    public T get(int position) {
        int[] indices = new int[sequences.length];
        Object[] values = new Object[sequences.length];
        int last = sequences.length-1;
        for (int i=0; i<last; i++) {
            indices[i] = position / sizes[i];
            values[i] = sequences[i].get(indices[i]);
            position -= indices[i]*sizes[i];
        }
        indices[last] = position;
        values[last] = sequences[last].get(indices[last]);
        return mapper.map(indices, values);
    }
}
