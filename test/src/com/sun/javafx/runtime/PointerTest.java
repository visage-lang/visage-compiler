package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * PointerTest
 *
 * @author Brian Goetz
 */
public class PointerTest extends JavaFXTestCase {
    public void testPointers() {
        final SequenceLocation<Integer> seqvar = SequenceVar.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> seqexp = SequenceExpression.make(Integer.class,
                new SequenceBindingExpression<Integer>() {
                    public Sequence<? extends Integer> get() {
                        return seqvar.get();
                    }
                }, seqvar);
        final IntLocation intvar1 = IntVar.make(3);
        final IntLocation intvar2 = IntVar.make(3);

        Pointer pseqvar = Pointer.make(seqvar);
        Pointer pseqexp = Pointer.make(seqexp);
        Pointer pintvar1 = Pointer.make(intvar1);
        Pointer pintvar2 = Pointer.make(intvar2);
        Pointer[] pointers = new Pointer[] { pseqvar, pseqexp, pintvar1, pintvar2 };
        for (int i=0; i<pointers.length; i++)
            for (int j=0; j<pointers.length; j++)
                if (i == j)
                    assertEquals(pointers[i], pointers[j]);
                else
                    assertFalse(pointers[i].equals(pointers[j]));

        Pointer anotherPintvar2 = Pointer.make(intvar2);
        assertEquals(pintvar2,  anotherPintvar2);

        assertEquals((Integer) 3, pintvar2.get());
        assertEquals((Integer) 3, anotherPintvar2.get());
        pintvar2.set(5);
        assertEquals(5, intvar2.get());
        assertEquals((Integer) 5, pintvar2.get());
        assertEquals((Integer) 5, anotherPintvar2.get());

        assertEquals((Sequence<Integer>) pseqvar.get(), 1, 2, 3);
        pseqvar.set(Sequences.range(1, 5));
        assertEquals(seqvar, 1, 2, 3, 4, 5);
        assertEquals(seqexp, 1, 2, 3, 4, 5);
        assertEquals((Sequence<Integer>) pseqvar.get(), 1, 2, 3, 4, 5);
        assertEquals((Sequence<Integer>) pseqexp.get(), 1, 2, 3, 4, 5);
    }
}
