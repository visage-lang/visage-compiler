package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.DoubleLocation;
import com.sun.javafx.runtime.location.IntLocation;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;
import junit.framework.TestCase;

import java.util.Collection;
import java.util.HashSet;

/**
 * JavaFXTestCase
 *
 * @author Brian Goetz
 */
public abstract class JavaFXTestCase extends TestCase {
    /**
     * Helper method for asserting that a sequence contains a specific set of values; tests via Object.equals(),
     * equality of toString(), by iterating the elements, and by toArray
     */
    protected <T> void assertEquals(Sequence<T> sequence, T... values) {
        Sequence<T> newSeq = Sequences.make(sequence.getElementType(), values);
        assertEquals(sequence, newSeq);

        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        for (int i = 0; i < values.length; i++) {
            if (i != 0)
                sb.append(", ");
            sb.append(values[i]);
        }
        sb.append(" ]");
        assertEquals(sb.toString(), sequence.toString());

        int index = 0;
        for (T t : sequence) {
            assertEquals(t, values[index++]);
        }

        T[] array = (T[]) new Object[sequence.size()];
        sequence.toArray(array, 0);
        assertEquals(array.length, values.length);
        for (int i = 0; i < array.length; i++)
            assertEquals(array[i], values[i]);
    }

    protected <T> void assertEquals(Sequence<T> sequence, T value) {
        assertEquals(sequence, Sequences.singleton(sequence.getElementType(), value));
    }

    protected <T> void assertEquals(SequenceLocation<T> sequence, T... values) {
        assertEquals(sequence.get(), values);
    }

    protected void assertEquals(int value, IntLocation loc) {
        assertTrue(loc.isValid());
        assertEquals(value, loc.get());
    }

    protected void assertEqualsLazy(int value, IntLocation loc) {
        assertFalse(loc.isValid());
        assertEquals(value, loc.get());
        assertTrue(loc.isValid());
    }

    protected void assertEquals(double value, DoubleLocation loc) {
        assertTrue(loc.isValid());
        assertEquals(value, loc.get());
    }

    protected void assertEqualsLazy(double value, DoubleLocation loc) {
        assertFalse(loc.isValid());
        assertEquals(value, loc.get());
        assertTrue(loc.isValid());
    }

    protected<T> void assertEquals(T value, ObjectLocation<T> loc) {
        assertTrue(loc.isValid());
        assertEquals(value, loc.get());
    }

    protected<T> void assertEqualsLazy(T value, ObjectLocation<T> loc) {
        assertFalse(loc.isValid());
        assertEquals(value, loc.get());
        assertTrue(loc.isValid());
    }

    protected<T> void assertEquals(Collection<T> collection, T... values) {
        Collection<T> newCollection = new HashSet<T>();
        for (T val : values)
            newCollection.add(val);
        assertEquals(collection, newCollection);
    }

    protected interface VoidCallable {
        public void call() throws Exception;
    }

    protected static void assertThrows(Class<? extends Exception> exceptionClass, VoidCallable closure) {
        try {
            closure.call();
            fail("Expected exception " + exceptionClass.getCanonicalName());
        }
        catch (Exception e) {
            if (exceptionClass.isInstance(e))
                return;
            else
                throw new RuntimeException("Expecting exception " + exceptionClass.getCanonicalName() + "; found exception " + e.getClass().getCanonicalName(), e);
        }
    }

}
