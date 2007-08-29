package com.sun.javafx.runtime.sequence;

/**
 * Helper classes for building sequences.
 *
 * @author Brian Goetz
 */
public class SequenceBuilder<T> {
    private final static int DEFAULT_SIZE = 16;

    private final Class<T> clazz;
    private T[] array;
    private int size;

    public SequenceBuilder(Class<T> clazz) {
        this(clazz, DEFAULT_SIZE);
    }

    @SuppressWarnings("unchecked")
    public SequenceBuilder(Class<T> clazz, int initialSize) {
        this.clazz = clazz;
        array = (T[]) new Object[powerOfTwo(1, initialSize)];
    }

    private int powerOfTwo(int current, int desired) {
        int capacity = current;
        while (capacity < desired)
            capacity <<= 1;
        return capacity;
    }

    @SuppressWarnings("unchecked")
    private void ensureSize(int newSize) {
        if (array.length < newSize) {
            int newCapacity = powerOfTwo(array.length, newSize);
            T[] newArray = (T[]) new Object[newCapacity];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    public void add(T element) {
        ensureSize(size + 1);
        array[size++] = element;
    }

    public void add(Sequence<T> elements) {
        ensureSize(size + elements.size());
        elements.toArray(array, size);
        size += elements.size();
    }

    public Sequence<T> toSequence() {
        return Sequences.make(clazz, array, size);
    }
}
