package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceChangeListener;
import com.sun.javafx.runtime.NumericTypeInfo;

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
            sequence.addChangeListener(new SequenceChangeListener<V>() {
                public void onChange(int startPos, int endPos, Sequence<? extends V> newElements, Sequence<V> oldValue, Sequence<V> newValue) {
                    updateSlice(startPos, endPos, convert(Sequences.upcast(newElements)), convert(newValue));
                }
            });
        }
        else {
            sequence.addInvalidationListener(new InvalidateMeListener());
        }
    }

    @Override
    protected Sequence<T> computeValue() {
        return convert(sequence.get());
    }

    private Sequence<T> convert(Sequence<V> seq) {
        return Sequences.<T, V>convertNumberSequence(toType, fromType, seq);
    }
}
