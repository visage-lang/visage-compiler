package com.sun.javafx.runtime.sequence;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.javafx.runtime.Util;

/**
 * Relatively inefficient implementation of a mutable sequence with a slice-replace operation.  This is used internally
 * in sequence binding implementations.
 *
 * @author Brian Goetz
 */
public class DumbMutableSequence<T> implements Iterable<T> {
    private T[] array;
    private int size;

    public DumbMutableSequence(T[] initialValues) {
        this(initialValues.length);
        System.arraycopy(initialValues, 0, array, 0, initialValues.length);
        size = initialValues.length;
    }

    public DumbMutableSequence(int initialSize) {
        this.array = Util.<T>newObjectArray(Util.powerOfTwo(1, initialSize));
        size = 0;
    }

    public DumbMutableSequence() {
        this(8);
    }

    public T get(int i) {
        return (i < 0 || i > size)
                ? null
                : array[i];
    }

    public void replaceSlice(int startPos, int endPos, T[] newElements) {
        int insertedCount = newElements.length;
        int deletedCount = endPos - startPos + 1;
        int netAdded = insertedCount - deletedCount;
        if (netAdded == 0)
            System.arraycopy(newElements, 0, array, startPos, insertedCount);
        else if (size + netAdded < array.length) {
            System.arraycopy(array, endPos + 1, array, endPos + 1 + netAdded, size - (endPos + 1));
            System.arraycopy(newElements, 0, array, startPos, insertedCount);
            if (netAdded < 0)
                Arrays.fill(array, size + netAdded, size, null);
            size += netAdded;
        }
        else {
            int newSize = size + netAdded;
            T[] temp = Util.<T>newObjectArray(Util.powerOfTwo(size, newSize));
            System.arraycopy(array, 0, temp, 0, startPos);
            System.arraycopy(newElements, 0, temp, startPos, insertedCount);
            System.arraycopy(array, endPos + 1, temp, startPos + insertedCount, size - (endPos + 1));
            array = temp;
            size = newSize;
        }
    }

    public Sequence<? extends T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newElements) {
        T[] temp = Util.<T>newObjectArray(Sequences.size(newElements));
        newElements.toArray(temp, 0);
        replaceSlice(startPos, endPos, temp);
        return newElements;
    }

    public Sequence<T> get(Class<T> clazz) {
        return Sequences.make(clazz, array, size);
    }

    public int size() {
        return size;
    }
    
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            public boolean hasNext() {
                return index < size;
            }

            public T next() {
                if (hasNext())
                    return array[index++];
                else
                    throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    void testValid() {
        for (int i=0; i<size; i++)
            if (array[i] == null)
                throw new AssertionError("Null element at " + i);
        for (int i=size; i<array.length; i++)
            if (array[i] != null)
                throw new AssertionError("Non-null element at " + i);
    }
}
