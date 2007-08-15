package com.sun.javafx.runtime.sequence;

/**
 * CompositeSequence
 *
 * @author Brian Goetz
 */
public class CompositeSequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private final Sequence<? extends T>[] sequences;
    private final int[] startPositions;
    private final int size, depth;

    public CompositeSequence(Class<T> clazz, Sequence<? extends T>... sequences) {
        super(clazz);
        this.sequences = sequences;
        this.startPositions = new int[sequences.length];
        int size = 0;
        int depth = 0;
        for (int i = 0, offset = 0; i < sequences.length; i++) {
            if (!sequences[i].getElementType().isAssignableFrom(clazz))
                throw new ClassCastException();
            startPositions[i] = offset;
            size += sequences[i].size();
            offset += sequences[i].size();
            depth = Math.max(depth, ((SequenceInternal) sequences[i]).getDepth());
        }
        this.size = size;
        this.depth = depth + 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public T get(int position) {
        if (position < 0 || position >= size || size == 0)
            return nullValue;
        // Linear search should be good enough for now
        int chunk = 0;
        while (chunk < sequences.length - 1
                && (position >= startPositions[chunk+1] || sequences[chunk].size() == 0))
            ++chunk;
        return sequences[chunk].get(position - startPositions[chunk]);
    }
}
