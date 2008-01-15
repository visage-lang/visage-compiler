package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.*;

/**
 * Location representing bind(x[i]), where x and i are locations.  Does not fire change triggers for modifications that
 * do not affect the value of x[i], such as inserts after i or modifications to elements other than i.  
 *
 * @author Brian Goetz
 */
public class SequenceElementLocation<T> extends AbstractLocation implements ObjectLocation<T> {
    private SequenceLocation<T> seq;
    private IntLocation index;
    private T value, previousValue;
    private int indexValue;

    public SequenceElementLocation(SequenceLocation<T> seq, IntLocation index) {
        super(false, false);
        this.seq = seq;
        this.index = index;
        addDependencies(index);
        seq.addChangeListener(new MySequenceListener());
        update();
    }

    public T get() {
        if (isValid())
            update();
        return value;
    }

    public T getPrevious() {
        return previousValue;
    }

    public T set(T value) {
        return seq.set(index.getAsInt(), value);
    }

    @Override
    public void update() {
        if (!isValid()) {
            indexValue = index.getAsInt();
            value = seq.getAsSequence().get(indexValue);
            // @@@ Should this be .equals() ?
            setValid(previousValue != value);
        }
    }

    public boolean isNull() {
        return get() == null;
    }

    @Override
    public void invalidate() {
        if (isValid())
            previousValue = value;
        super.invalidate();
    }

    private class MySequenceListener implements SequenceChangeListener<T> {
        public void onInsert(int position, T element) {
            if (position <= indexValue) {
                invalidate();
                update();
            }
        }

        public void onDelete(int position, T element) {
            if (position <= indexValue) {
                invalidate();
                update();
            }
        }

        public void onReplace(int position, T oldValue, T newValue) {
            if (position == indexValue) {
                invalidate();
                update();
            }
        }
    }
}
