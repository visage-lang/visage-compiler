package com.sun.javafx.runtime.location;

import com.sun.javafx.functions.Function0;
import com.sun.javafx.runtime.JavaFXTestCase;

/**
 * BoundOperatorsTest
 *
 * @author Brian Goetz
 */
public class BoundOperatorsTest extends JavaFXTestCase {

    public void testIndirectIf() {
        BooleanLocation b = BooleanVariable.make(true);
        final IntVariable i = IntVariable.make(1);
        IntLocation ifLoc = BoundOperators.makeBoundIf(false, b,
                                                       new Function0<IntLocation>() {
                                                           public IntLocation invoke() {
                                                               return IntVariable.make(new IntBindingExpression() {
                                                                   public int computeValue() {
                                                                       return i.get();
                                                                   }
                                                               }, i);
                                                           }
                                                       },
                                                       new Function0<IntLocation>() {
                                                           public IntLocation invoke() {
                                                               return IntVariable.make(new IntBindingExpression() {
                                                                   public int computeValue() {
                                                                       return -i.get();
                                                                   }
                                                               }, i);
                                                           }
                                                       });
        CountingListener cl = new CountingListener();
        ifLoc.addChangeListener(cl);
        assertEquals(1, ifLoc.getAsInt());
        assertEquals(0, cl.count);

        b.set(true);
        assertEquals(1, ifLoc.getAsInt());
        assertEquals(0, cl.count);

        b.set(false);
        assertEquals(-1, ifLoc.getAsInt());
        assertEquals(1, cl.count);

        i.set(3);
        assertEquals(-3, ifLoc.getAsInt());
        assertEquals(2, cl.count);

        i.set(3);
        assertEquals(-3, ifLoc.getAsInt());
        assertEquals(2, cl.count);

        b.set(true);
        assertEquals(3, ifLoc.getAsInt());
        assertEquals(3, cl.count);

        i.set(3);
        assertEquals(3, ifLoc.getAsInt());
        assertEquals(3, cl.count);

        i.set(5);
        assertEquals(5, ifLoc.getAsInt());
        assertEquals(4, cl.count);
    }

}
