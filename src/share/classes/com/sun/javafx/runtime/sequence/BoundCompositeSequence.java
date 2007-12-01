package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.Location;
import com.sun.javafx.runtime.location.SequenceChangeListener;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundCompositeSequence
 *
 * @author Brian Goetz
 */
public class BoundCompositeSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final SequenceLocation<? extends T>[] locations;
    private final Class<? extends T> clazz;
    private final int[] startPositions;

    private BoundCompositeSequence(Class<? extends T> clazz, boolean lazy, SequenceLocation<? extends T>... locations) {
        super(false, lazy);
        this.clazz = clazz;
        this.locations = locations;
        this.startPositions = new int[locations.length];
    }

    public static<T> SequenceLocation<? extends T> make(Class<? extends T> clazz, SequenceLocation<? extends T>... locations) {
        return new BoundCompositeSequence<T>(clazz, false, locations);
    }

    protected void computeInitial() {
        Sequence<T>[] sequences = (Sequence<T>[]) new Sequence[locations.length];
        for (int i = 0, offset = 0; i < locations.length; i++) {
            locations[i].addChangeListener(new MyListener(i));
            sequences[i] = (Sequence<T>) locations[i].get();
            Class eClass = locations[i].get().getElementType();
            if (!clazz.isAssignableFrom(eClass))
                throw new ClassCastException("cannot cast "+eClass.getName()
                                             +" segment to "+clazz.getName()+" sequence");
            startPositions[i] = offset;
            offset += sequences[i].size();
        }
        value = Sequences.concatenate(clazz, sequences);
        setValid(true);
    }


    private class MyListener implements SequenceChangeListener<T> {
        private final int index;

        private MyListener(int index) {
            this.index = index;
        }

        public void onInsert(int position, T element) {
            int actualPos = startPositions[index] + position;
            value = Sequences.insertBefore(value, element, actualPos);
            for (int i=index+1; i<startPositions.length; i++)
                ++startPositions[i];
            BoundCompositeSequence.this.onInsert(actualPos, element);
        }

        public void onDelete(int position, T element) {
            int actualPos = startPositions[index] + position;
            value = Sequences.delete(value, actualPos);
            for (int i=index+1; i<startPositions.length; i++)
                --startPositions[i];
            BoundCompositeSequence.this.onDelete(actualPos, element);
        }

        public void onReplace(int position, T oldValue, T newValue) {
            int actualPos = startPositions[index] + position;
            T old = value.get(actualPos);
            value = ((Sequence<T>) value).set(actualPos, newValue);
            onReplaceElement(actualPos, old, newValue);
        }

        public boolean onChange(Location location) {
            return true;
        }
    }
}

