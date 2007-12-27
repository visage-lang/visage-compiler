package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.location.SequenceChangeListener;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundCompositeSequence
 *
 * @author Brian Goetz
 */
public class BoundCompositeSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final SequenceLocation<? extends T>[] locations;
    private final int[] startPositions;

    public static<T> SequenceLocation<T> make(Class<T> clazz, SequenceLocation<? extends T>... locations) {
        return new BoundCompositeSequence<T>(clazz, false, locations);
    }

    private BoundCompositeSequence(Class<T> clazz, boolean lazy, SequenceLocation<? extends T>... locations) {
        super(clazz, false, lazy);
        this.locations = locations;
        this.startPositions = new int[locations.length];
    }

    protected void computeInitial() {
        Sequence<? extends T>[] sequences = Util.newSequenceArray(locations.length);
        for (int i = 0, offset = 0; i < locations.length; i++) {
            locations[i].addChangeListener(new MyListener(i));
            sequences[i] = locations[i].get();
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
            value = actualPos == value.size() ? value.insert(element) : value.insertBefore(element, actualPos);
            for (int i=index+1; i<startPositions.length; i++)
                ++startPositions[i];
            valueChanged();
            BoundCompositeSequence.this.onInsert(actualPos, element);
        }

        public void onDelete(int position, T element) {
            int actualPos = startPositions[index] + position;
            value = value.delete(actualPos);
            for (int i=index+1; i<startPositions.length; i++)
                --startPositions[i];
            valueChanged();
            BoundCompositeSequence.this.onDelete(actualPos, element);
        }

        public void onReplace(int position, T oldValue, T newValue) {
            int actualPos = startPositions[index] + position;
            T old = value.get(actualPos);
            value = value.set(actualPos, newValue);
            valueChanged();
            BoundCompositeSequence.this.onReplaceElement(actualPos, old, newValue);
        }
    }
}

