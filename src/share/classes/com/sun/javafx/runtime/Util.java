package com.sun.javafx.runtime;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;

import com.sun.javafx.runtime.sequence.Sequence;

/**
 * Utility class for various static utility methods, such as methods that launder generic type errors that are
 * known to be safe.
 *
 * @author Brian Goetz
 */
public class Util {
    @SuppressWarnings("unchecked")
    public static<T> T[] newObjectArray(int size) {
        return (T[]) new Object[size];
    }

    @SuppressWarnings("unchecked")
    public static<T> Sequence<T>[] newSequenceArray(int size) {
        return (Sequence<T>[]) new Sequence[size];

    }

    public static int powerOfTwo(int current, int desired) {
        int capacity = current == 0 ? 1 : current;
        while (capacity < desired)
            capacity <<= 1;
        return capacity;
    }

    @SuppressWarnings("unchecked")
    public static<T> T defaultValue(Class<T> clazz) {
        if (clazz == Integer.class)
            return (T) Integer.valueOf(0);
        else if (clazz == Double.class)
            return (T) Double.valueOf(0.0);
        else if (clazz == Boolean.class)
            return (T) Boolean.FALSE;
        else
            return null;
    }

    @SuppressWarnings("unchecked")
    public static<T> T[] newArray(Class<?> clazz, int size) {
        return (T[]) Array.newInstance(clazz, size);
    }

    @SuppressWarnings("unchecked")
    public static<T> T[] replaceSlice(T[] array, int startPos, int endPos, T[] newElements) {
        int insertedCount = newElements.length;
        int deletedCount = endPos - startPos + 1;
        int netAdded = insertedCount - deletedCount;
        if (netAdded == 0) {
            System.arraycopy(newElements, 0, array, startPos, insertedCount);
            return array;
        }
        else {
            T[] temp = (T[]) newArray(array.getClass().getComponentType(), array.length + netAdded);
            System.arraycopy(array, 0, temp, 0, startPos);
            System.arraycopy(newElements, 0, temp, startPos, insertedCount);
            System.arraycopy(array, endPos + 1, temp, startPos + insertedCount, array.length - (endPos + 1));
            return temp;
        }
    }

    /**
     * Returns the __FILE__ pseudo-variable for a module.
     * 
     * @param moduleClass the fully-qualified name of the module class
     * @return the resource URL to the module's class
     */
    public static URL get__FILE__(Class<?> moduleClass) {
        try {
            String resource = moduleClass.getName().replace(".", "/") + ".class";
            return moduleClass.getClassLoader().getResource(resource);
        } catch (Throwable t) {
            return null;
        }
    }
    
    /**
     * Returns the __DIR__ pseudo-variable for a module.
     * @param __FILE__ the module's __FILE__ pseudo-variable
     * @return the module's __DIR__ URL
     */
    public static URL get__DIR__(URL __FILE__) {
        try {
            return __FILE__ == null ? null : new java.net.URL(__FILE__, ".");
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public static<T> boolean isEqual(T oldValue, T newValue) {
        if (oldValue == null) {
            return newValue == null;
        } else
            return oldValue.equals(newValue);
    }
}
