package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.SequenceLocation;
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
            sequence.addSequenceChangeListener(new UnderlyingSequenceChangeListener<T, V>(this));
        }
        else {
            sequence.addInvalidationListener(new InvalidateMeListener(this));
        }
    }

    private static class UnderlyingSequenceChangeListener<T extends Number, V extends Number> extends WeakMeChangeListener<V> {
        UnderlyingSequenceChangeListener(BoundNumericConversion<T, V> bnc) {
            super(bnc);
        }

        @Override
        public void onChange(ArraySequence<V> buffer, Sequence<? extends V> oldValue, int startPos, int endPos, Sequence<? extends V> newElements) {
            onChangeB(buffer, oldValue, startPos, endPos, newElements);
        }

        @Override
        public boolean onChangeB(ArraySequence<V> buffer, Sequence<? extends V> oldValue, int startPos, int endPos, Sequence<? extends V> newElements) {
            BoundNumericConversion<T, V> bnc = (BoundNumericConversion<T, V>) get();
            if (bnc != null) {
                newElements = Sequences.getNewElements(buffer, startPos, newElements);
                bnc.updateSlice(startPos, endPos, bnc.convert(newElements));
                return true;
            } else {
                return false;
            }
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
