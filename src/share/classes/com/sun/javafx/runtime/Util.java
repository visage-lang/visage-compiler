package com.sun.javafx.runtime;

import com.sun.javafx.runtime.sequence.Sequence;
import java.net.MalformedURLException;
import java.net.URL;

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
        int capacity = current;
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
}
