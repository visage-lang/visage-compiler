package com.sun.javafx.runtime.sequence;

/**
 * Helper classes for building sequences, much like StringBuilder assists in building Strings.  SequenceBuilder
 * stores the sequence building built in an array, which is automatically resized as needed.  It can be converted
 * to a Sequence by calling toSequence().
 *
 * @author Brian Goetz
 */
public class SequenceBuilder<T> {
    private final static int DEFAULT_SIZE = 16;

    private final Class<T> clazz;
    private T[] array;
    private int size;

    /** Create a SequenceBuilder for a Sequence of type T */
    public SequenceBuilder(Class<T> clazz) {
        this(clazz, DEFAULT_SIZE);
    }

    /** Create a SequenceBuilder for a Sequence of type T, ensuring that there is initially room for at least
     * initialSize elements. */
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

    /** Add a single element to the sequence */
    public void add(T element) {
        ensureSize(size + 1);
        array[size++] = element;
    }

    /** Add the contents of an existing sequence to the sequence */
    public void add(Sequence<T> elements) {
        ensureSize(size + elements.size());
        elements.toArray(array, size);
        size += elements.size();
    }

    /** Get the current size of the sequence being constructed */
    public int size() {
        return size;
    }

    /** Get the nth element of the sequence being constructed, returning the null sequence value if n is out of range */
    public T get(int n) {
        if (n < 0 || n >= size)
            return AbstractSequence.nullValue(clazz);
        else
            return array[n];
    }

    /** Erase the current contents of the SequenceBuilder */
    @SuppressWarnings("unchecked")
    public void clear() {
        array = (T[]) new Object[powerOfTwo(1, DEFAULT_SIZE)];
        size = 0;
    }

    /** Convert the SequenceBuilder to a Sequence.  The elements will be copied to a new sequence, and will remain
     * in the SequenceBuilder, so it can continue to be used to make more sequences */
    public Sequence<T> toSequence() {
        // OPT: This causes the array to be copied; we can do the same trick as in StringBuilder to transfer
        // ownership of the array instead and avoid the copy
        return Sequences.make(clazz, array, size);
    }
}
