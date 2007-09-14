package com.sun.javafx.runtime.sequence;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents an empty sequence of a specific type.  Empty sequences are cached using the "Flyweight" pattern.
 * To construct an empty sequence, use the Sequences.emptySequence() factory, not the EmptySequence constructor.
 * O(1) space and time construction costs.  
 *
 * @author Brian Goetz
 */
class EmptySequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private static final Map<Class<?>, Sequence<?>> map = new HashMap<Class<?>, Sequence<?>>();

    public EmptySequence(Class<T> clazz) {
        super(clazz);
    }

    @SuppressWarnings("unchecked")
    public static<T> Sequence<T> get(Class<T> clazz) {
        Sequence<?> e = map.get(clazz);
        if (e == null) {
            e = new EmptySequence<T>(clazz);
            map.put(clazz, e);
        }
        return (Sequence<T>) e;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public T get(int position) {
        return nullValue;
    }
}
