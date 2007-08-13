package com.sun.javafx.runtime.sequence;

import java.util.BitSet;
import java.util.Iterator;

/**
 * AbstractSequence
 *
 * @author Brian Goetz
 */
public abstract class AbstractSequence<T> implements Sequence<T> {
    protected final Class<T> clazz;

    protected AbstractSequence(Class<T> clazz) {
        this.clazz = clazz;
    }

    public abstract int size();

    public abstract T get(int position);

    protected BitSet getBits(SequenceSelector<T> predicate) {
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

    public void forEach(SequenceClosure<T> sequenceClosure) {
        int length = size();
        for (int i = 0; i < length; i++)
            sequenceClosure.call(this, i, get(i));
    }


    public void toArray(T[] array, int destOffset) {
        for (int i=0; i<size(); i++)
            array[i+destOffset] = get(i);
    }

    public Sequence<T> get(SequenceSelector<T> predicate) {
        BitSet bits = getBits(predicate);
        return new FilterSequence<T>(this, bits);
    }


    public Sequence<T> get(int position, int length) {
        return new SubSequence<T>(this, position, length);
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
                    return get(next);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (!(obj instanceof Sequence))
            return false;
        Sequence other = (Sequence) obj;
        return (other.getElementType().isAssignableFrom(getElementType())) && isEqual((Sequence<T>) other);
    }

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

    public Sequence<T> delete(int position) {
        if (position < 0 || position >= size())
            return this;
        BitSet bits = new BitSet(size());
        bits.set(0, size());
        bits.clear(position);
        return new FilterSequence<T>(this, bits);
    }

    public Sequence<T> delete(SequenceSelector<T> predicate) {
        BitSet bits = getBits(predicate);
        bits.flip(0, size());
        return new FilterSequence<T>(this, bits);
    }

    public Sequence<T> insert(T value) {
        Class<T> elementType = getElementType();
        return new CompositeSequence<T>(elementType, this, new SingletonSequence<T>(elementType, value));
    }

    public Sequence<T> insert(Sequence<? extends T> values) {
        return new CompositeSequence<T>(getElementType(), this, values);
    }

    public Sequence<T> insertFirst(T value) {
        Class<T> elementType = getElementType();
        return new CompositeSequence<T>(elementType, new SingletonSequence<T>(elementType, value), this);
    }

    public Sequence<T> insertFirst(Sequence<? extends T> values) {
        return new CompositeSequence<T>(getElementType(), values, this);
    }

    public Sequence<T> insertBefore(T value, int position) {
        // @@@Spec: What about negative offsets?
        // @@@Spec: What about offsets greater than length?
        if (position <= 0)
            return insertFirst(value);
        else
            return insertBefore(new SingletonSequence<T>(getElementType(), value), position);
    }

    public Sequence<T> insertBefore(Sequence<? extends T> values, int position) {
        // @@@Spec: What about negative offsets?
        // @@@Spec: What about offsets greater than length?
        if (position <= 0)
            return insertFirst(values);
        else {
            return new CompositeSequence<T>(getElementType(),
                    get(0, position), values, get(position + 1, size() - (position + 1)));
        }
    }

    public Sequence<T> insertBefore(T value, SequenceSelector<T> predicate) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> insertBefore(Sequence<? extends T> values, SequenceSelector<T> predicate) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> insertAfter(T value, int position) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> insertAfter(T value, SequenceSelector<T> predicate) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> insertAfter(Sequence<? extends T> values, SequenceSelector<T> predicate) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> insertAfter(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }
}
