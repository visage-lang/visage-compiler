package com.sun.javafx.runtime.sequence;

import java.util.Iterator;

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

    public DumbMutableSequence(int initialSize) {
        this.array = Util.<T>newObjectArray(Util.powerOfTwo(1, initialSize));
    }

    public DumbMutableSequence() {
        this(8);
    }

    public void replaceSlice(int startPos, int endPos, Sequence<? extends T> newElements) {
        int insertedCount = Sequences.size(newElements);
        int deletedCount = endPos - startPos + 1;
        int netAdded = insertedCount - deletedCount;
        if (netAdded == 0) {
            for (int i = startPos; i <= endPos; i++)
                array[i] = newElements.get(i - startPos);
        }
        else if (size + netAdded < array.length) {
            System.arraycopy(array, endPos + 1, array, endPos + 1 + netAdded, size - (endPos + 1));
            for (int i = 0; i < insertedCount; i++)
                array[startPos + i] = newElements.get(i);
            if (netAdded < 0) {
                for (int i = 0; i < -netAdded; i++)
                    array[size + netAdded + i] = null;
            }
            size += netAdded;
        }
        else {
            int newSize = size + netAdded;
            T[] temp = Util.<T>newObjectArray(Util.powerOfTwo(size, newSize));
            System.arraycopy(array, 0, temp, 0, startPos);
            for (int i = 0; i < insertedCount; i++)
                temp[startPos + i] = newElements.get(i);
            System.arraycopy(array, endPos + 1, temp, startPos + insertedCount, size - (endPos + 1));
            array = temp;
            size = newSize;
        }
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
                return array[index++];
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
