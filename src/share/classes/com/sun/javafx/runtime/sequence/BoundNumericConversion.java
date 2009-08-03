package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.ChangeListener;
import com.sun.javafx.runtime.NumericTypeInfo;
import com.sun.javafx.runtime.location.InvalidateMeListener;

/**
 * BoundNumericConversion
 *
 * @author Brian Goetz
 */
class BoundNumericConversion<T extends Number, V extends Number> extends AbstractBoundSequence<T> {
    private final NumericTypeInfo<T, ?> toType;
    private final NumericTypeInfo<V, ?> fromType;
    SequenceLocation<V> sequence;

    public BoundNumericConversion(boolean lazy, NumericTypeInfo<T, ?> toType, NumericTypeInfo<V, ?> fromType, SequenceLocation<V> sequence) {
        super(lazy, toType);
        this.toType = toType;
        this.fromType = fromType;
        this.sequence = sequence;

        if (!lazy) {
            setInitialValue(convert(sequence.get()));
            sequence.addSequenceChangeListener(new ChangeListener<V>() {
                public void onChange(ArraySequence<V> buffer, Sequence<? extends V> oldValue, int startPos, int endPos, Sequence<? extends V> newElements) {
                    newElements = Sequences.getNewElements(buffer, startPos, newElements);
                    updateSlice(startPos, endPos, convert(newElements));
                }
            });
        }
        else {
            sequence.addInvalidationListener(new InvalidateMeListener(this));
        }
    }

    @Override
    protected Sequence<? extends T> computeValue() {
      return convert(sequence.get());
    }

    private Sequence<T> convert(Sequence<? extends V> seq) {
        return Sequences.<T, V>convertNumberSequence(toType, fromType, seq);
    }
}
