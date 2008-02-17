package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.sequence.SequenceElementLocation;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * ElementLocationTest
 *
 * @author Brian Goetz
 */
public class ElementLocationTest extends JavaFXTestCase {
    public void testElementLocation() {
        SequenceLocation<Integer> seq = SequenceVariable.make(Sequences.range(1, 3));
        IntLocation index = IntVariable.make(1);
        ObjectLocation<Integer> second = new SequenceElementLocation<Integer>(seq, index);
        CountingListener cl = new CountingListener();
        second.addChangeListener(cl);

        assertEquals(cl.count, 0);
        assertEquals((int) second.get(), 2);
        index.setAsInt(0);
        assertEquals(cl.count, 2);
        assertEquals((int) second.get(), 1);
        seq.insertBefore(-1, 0);
        assertEquals(cl.count, 3);
        assertEquals(seq.getAsSequence(), -1, 1, 2, 3);
        assertEquals((int) second.get(), -1);
        seq.delete(0);
        assertEquals(cl.count, 4);
        assertEquals((int) second.get(), 1);

        seq.insert(4);
        assertEquals(cl.count, 4);
        assertEquals(seq.getAsSequence(), 1, 2, 3, 4);
        assertEquals((int) second.get(), 1);
        seq.delete(3);
        assertEquals(cl.count, 4);
        assertEquals((int) second.get(), 1);
    }
}
