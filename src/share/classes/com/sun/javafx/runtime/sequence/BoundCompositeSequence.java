package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.location.SequenceChangeListener;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundCompositeSequence
 *
 * @author Brian Goetz
 * @author Per Bothner
 */
public class BoundCompositeSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final SequenceLocation<? extends T>[] locations;
    private final int[] startPositions;
    private final Sequence<? extends T>[] sequences;
    private int size, depth;


    public static<T> SequenceLocation<T> make(Class<T> clazz, SequenceLocation<? extends T>... locations) {
        return new BoundCompositeSequence<T>(clazz, false, locations);
    }

    private BoundCompositeSequence(Class<T> clazz, boolean lazy, SequenceLocation<? extends T>... locations) {
        super(clazz, false, lazy);
        this.locations = locations;
        this.startPositions = new int[locations.length];
        sequences = new Sequence[locations.length];
    }

    protected void computeInitial() {
        int depth = 0;
        int offset = 0;
        for (int i = 0; i < locations.length; i++) {
            locations[i].addChangeListener(new MyListener(i));
            sequences[i] = locations[i].get();
            Class eClass = locations[i].get().getElementType();
            if (!clazz.isAssignableFrom(eClass))
                throw new ClassCastException("cannot cast "+eClass.getName()
                                             +" segment to "+clazz.getName()+" sequence");
            startPositions[i] = offset;
            int size = sequences[i].size();
            offset += size;
            depth = Math.max(depth, sequences[i].getDepth());
        }
        this.size = offset;
        this.depth = depth + 1;
        value = new MyCompositeSequence(clazz);
        setValid(true);
    }

    class MyCompositeSequence extends AbstractSequence<T> implements Sequence<T> {
        public MyCompositeSequence(Class<T> clazz) {
            super(clazz);
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
            if (position < 0 || position >= size || size == 0) {
                throw new IndexOutOfBoundsException(Integer.toString(position));
            }
            // Linear search should be good enough for now
            int chunk = 0;
            while (chunk < sequences.length - 1 && (position >= startPositions[chunk + 1] || sequences[chunk].size() == 0)) {
                ++chunk;
            }
            return sequences[chunk].get(position - startPositions[chunk]);
        }
    }

    private class MyListener implements SequenceChangeListener<T> {
        private final int index;

        private MyListener(int index) {
            this.index = index;
        }

        public void onInsert(int position, T element) {
            int actualPos = startPositions[index] + position;
            sequences[index] = locations[index].get();
            for (int i=index+1; i<startPositions.length; i++)
                ++startPositions[i];
            size++;
            valueChanged();
            BoundCompositeSequence.this.onInsert(actualPos, element);
        }

        public void onDelete(int position, T element) {
            int actualPos = startPositions[index] + position;
            sequences[index] = locations[index].get();
            for (int i=index+1; i<startPositions.length; i++)
                --startPositions[i];
            valueChanged();
            BoundCompositeSequence.this.onDelete(actualPos, element);
        }

        public void onReplace(int position, T oldValue, T newValue) {
            int actualPos = startPositions[index] + position;
            T old = value.get(actualPos);
            sequences[index] = locations[index].get();
            valueChanged();
            BoundCompositeSequence.this.onReplaceElement(actualPos, old, newValue);
        }
    }
}

