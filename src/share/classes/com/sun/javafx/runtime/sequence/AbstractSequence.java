package com.sun.javafx.runtime.sequence;

import java.util.BitSet;
import java.util.Iterator;

/**
 * AbstractSequence
 *
 * @author Brian Goetz
 */
public abstract class AbstractSequence<T> implements Sequence<T>, SequenceInternal<T> {
    protected final Class<T> clazz;
    protected final T nullValue;

    @SuppressWarnings("unchecked")
    protected AbstractSequence(Class<T> clazz) {
        this.clazz = clazz;
        if (clazz == Integer.class)
            nullValue = (T) Sequences.INTEGER_ZERO;
        else if (clazz == Double.class)
            nullValue = (T) Sequences.DOUBLE_ZERO;
        else if (clazz == Boolean.class)
            nullValue = (T) Sequences.BOOLEAN_ZERO;
        else
            nullValue = null;
    }

    public abstract int size();

    public abstract T get(int position);

    public BitSet getBits(SequencePredicate<T> predicate) {
        int length = size();
        BitSet bits = new BitSet(length);
        for (int i = 0; i < length; i++)
            if (predicate.matches(this, i, get(i)))
                bits.set(i);
        return bits;
    }

    public Class<T> getElementType() {
        return clazz;
    }

    public boolean isEmpty() {
        return (size() == 0);
    }

    public int getDepth() {
        return 0;
    }

    public void foreach(SequenceClosure<T> sequenceClosure) {
        int length = size();
        for (int i = 0; i < length; i++)
            sequenceClosure.call(this, i, get(i));
    }

    @SuppressWarnings("unchecked")
    public Sequence<T> map(SequenceMapper<T> sequenceMapper) {
        int length = size();
        T[] values = (T[]) new Object[length];
        for (int i = 0; i < length; i++)
            values[i] = sequenceMapper.map(this, i, get(i));
        return new ArraySequence<T>(getElementType(), values);
    }

    public void toArray(Object[] array, int destOffset) {
        for (int i = 0; i < size(); i++)
            array[i + destOffset] = get(i);
    }

    public Sequence<T> get(SequencePredicate<T> predicate) {
        return Sequences.filter(this, getBits(predicate));
    }


    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int next = 0;

            public boolean hasNext() {
                return next < size();
            }

            public T next() {
                if (next >= size())
                    throw new IndexOutOfBoundsException();
                else
                    return get(next++);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (!(obj instanceof Sequence))
            return false;
        Sequence other = (Sequence) obj;
        return (other.getElementType().isAssignableFrom(getElementType())) && isEqual((Sequence<T>) other);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < size(); i++) {
            T val = get(i);
            hash = 31 * hash + (val != null ? val.hashCode() : 0);
        }
        return hash;
    }

    public boolean isEqual(Sequence<T> other) {
        if (other == null)
            return false;
        if (size() != other.size())
            return false;
        for (int i = 0; i < size(); i++) {
            T mine = get(i);
            T theirs = other.get(i);
            if (mine == null)
                return (theirs == null);
            else if (!mine.equals(theirs))
                return false;
        }
        return true;
    }


    @Override
    public String toString() {
        if (isEmpty())
            return "[ ]";
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int i = 0; i < size(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(get(i));
        }
        sb.append(" ]");
        return sb.toString();
    }


    public Sequence<T> set(int position, T value) {
        if (position < 0 || position >= size())
            return this;
        else {
            Sequence<T> newElement = Sequences.singleton(getElementType(), value);
            if (position == 0)
                return Sequences.concatenate(getElementType(), newElement, subsequence(1, size()));
            else if (position == size() - 1)
                return Sequences.concatenate(getElementType(), subsequence(0, size() - 1), newElement);
            else
                return Sequences.concatenate(getElementType(),
                        subsequence(0, position), newElement, subsequence(position + 1, size()));
        }
    }

    public Sequence<T> delete(int position) {
        if (position < 0 || position >= size())
            return this;
        BitSet bits = new BitSet(size());
        bits.set(0, size());
        bits.clear(position);
        return Sequences.filter(this, bits);
    }

    public Sequence<T> delete(SequencePredicate<T> predicate) {
        BitSet bits = getBits(predicate);
        bits.flip(0, size());
        return Sequences.filter(this, bits);
    }

    public Sequence<T> subsequence(int start, int end) {
        return Sequences.subsequence(this, start, end);
    }

    public Sequence<T> insert(T value) {
        Class<T> elementType = getElementType();
        return Sequences.concatenate(elementType, this, Sequences.singleton(elementType, value));
    }

    public Sequence<T> insert(Sequence<T> values) {
        return Sequences.concatenate(getElementType(), this, values);
    }

    public Sequence<T> insertFirst(T value) {
        Class<T> elementType = getElementType();
        return Sequences.concatenate(elementType, Sequences.singleton(elementType, value), this);
    }

    public Sequence<T> insertFirst(Sequence<T> values) {
        return Sequences.concatenate(getElementType(), values, this);
    }

    public Sequence<T> insertBefore(T value, int position) {
        if (position <= 0)
            return insertFirst(value);
        else if (position >= size())
            return insert(value);
        else
            return insertBefore(Sequences.singleton(getElementType(), value), position);
    }

    public Sequence<T> insertBefore(Sequence<T> values, int position) {
        if (position <= 0)
            return insertFirst(values);
        else if (position >= size())
            return insert(values);
        else
            return Sequences.concatenate(getElementType(),
                    subsequence(0, position), values, subsequence(position, size()));
    }

    public Sequence<T> insertAfter(T value, int position) {
        if (position >= size())
            return insert(value);
        else if (position < 0)
            return insertFirst(value);
        else
            return insertAfter(Sequences.singleton(getElementType(), value), position);
    }

    public Sequence<T> insertAfter(Sequence<T> values, int position) {
        if (position >= size() - 1)
            return insert(values);
        else if (position < 0)
            return insertFirst(values);
        else {
            return Sequences.concatenate(getElementType(),
                    subsequence(0, position + 1), values, subsequence(position + 1, size()));
        }
    }

    /*
     * Precondition: bits.cardinality() > 1 
     */
    @SuppressWarnings("unchecked")
    private Sequence<T> multiInsertBefore(BitSet bits, Sequence<T> values) {
        assert (bits.cardinality() > 1);
        int firstBit = bits.nextSetBit(0);
        int count = 2 * bits.cardinality() + (firstBit > 0 ? 1 : 0);
        Sequence[] segments = new Sequence[count];
        int n = 0;
        if (firstBit > 0)
            segments[n++] = subsequence(0, firstBit);
        for (int i = firstBit, j = bits.nextSetBit(i + 1); i >= 0; i = j, j = bits.nextSetBit(j + 1)) {
            segments[n++] = values;
            segments[n++] = subsequence(i, (j > 0) ? j : size());
        }
        return Sequences.concatenate(getElementType(), segments);
    }

    /*
     * Precondition: bits.cardinality() > 1
     */
    @SuppressWarnings("unchecked")
    private Sequence<T> multiInsertAfter(BitSet bits, Sequence<T> values) {
        assert (bits.cardinality() > 1);
        int firstBit = bits.nextSetBit(0);
        int lastBit = firstBit;
        for (int i = firstBit; i >= 0; i = bits.nextSetBit(i + 1))
            lastBit = i;
        int count = 2 * bits.cardinality() + (lastBit < size() - 1 ? 1 : 0);
        Sequence[] segments = new Sequence[count];
        int lastWritten = -1, n = 0;
        for (int j = firstBit; j >= 0; j = bits.nextSetBit(j + 1)) {
            segments[n++] = subsequence(lastWritten + 1, j + 1);
            segments[n++] = values;
            lastWritten = j;
        }
        if (lastBit < size() - 1)
            segments[n++] = subsequence(lastBit + 1, size());
        return Sequences.concatenate(getElementType(), segments);
    }

    public Sequence<T> insertBefore(T value, SequencePredicate<T> predicate) {
        BitSet bits = getBits(predicate);
        if (bits.cardinality() == 0)
            return this;
        else if (bits.cardinality() == 1)
            return insertBefore(value, bits.nextSetBit(0));
        else
            return multiInsertBefore(bits, Sequences.singleton(getElementType(), value));
    }

    public Sequence<T> insertBefore(Sequence<T> values, SequencePredicate<T> predicate) {
        BitSet bits = getBits(predicate);
        if (bits.cardinality() == 0)
            return this;
        else if (bits.cardinality() == 1)
            return insertBefore(values, bits.nextSetBit(0));
        else
            return multiInsertBefore(bits, values);
    }

    public Sequence<T> insertAfter(T value, SequencePredicate<T> predicate) {
        BitSet bits = getBits(predicate);
        if (bits.cardinality() == 0)
            return this;
        else if (bits.cardinality() == 1)
            return insertAfter(value, bits.nextSetBit(0));
        else
            return multiInsertAfter(bits, Sequences.singleton(getElementType(), value));
    }

    public Sequence<T> insertAfter(Sequence<T> values, SequencePredicate<T> predicate) {
        BitSet bits = getBits(predicate);
        if (bits.cardinality() == 0)
            return this;
        else if (bits.cardinality() == 1)
            return insertAfter(values, bits.nextSetBit(0));
        else
            return multiInsertAfter(bits, values);
    }
}
