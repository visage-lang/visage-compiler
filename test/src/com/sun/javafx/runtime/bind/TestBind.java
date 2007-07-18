package com.sun.javafx.runtime.bind;

/**
 * TestBind
 *
 * @author Brian Goetz
 */
public class TestBind extends BindingTestCase {

    public void testSetValue() {
        Holder h = new Holder();
        Container container = h.container;
        IntVar a = new IntVar(container, Holder.KEY_A);
        IntVar b = new IntVar(container, Holder.KEY_B);
        a.set(1);
        b.set(2);
        assertEquals(h.a, a.get());
        assertEquals(h.b, b.get());
        assertEquals(h.a, h.getA());
        assertEquals(h.b, h.getB());
        assertEquals(1, a.get());
        assertEquals(2, b.get());
    }

    /*
     * Bind three variables in the same container:
     *   b = bind a + 1
     *   c = bind b
     */
    public void testLocalBind() {
        Holder h = new Holder();
        Container container = h.container;
        final IntVar a = new IntVar(container, Holder.KEY_A);
        final IntVar b = new IntVar(container, Holder.KEY_B);
        final IntVar c = new IntVar(container, Holder.KEY_C);

        a.set(0);
        // bind b = a + 1
        b.bind(new IntBindingClosure() {
            public int asInt() {
                return a.get() + 1;
            }
        }, new IntVar[] { a });
        assertEquals(0, a.get());
        assertEquals(1, b.get());
        a.set(2);
        assertEquals(2, a.get());
        assertEquals(3, b.get());

        // bind c = b
        c.bind(new IntBindingClosure() {
            public int asInt() {
                return b.get();
            }
        }, new IntVar[] { b });

        assertEquals(3, c.get());
        a.set(4);
        assertEquals(5, b.get());
        assertEquals(5, c.get());
    }

    /*
     * Bind three variables in two containers:
     *   c2.b = bind c1.a + 1
     *   c1.c = bind c2.b
     */
    public void testRemoteBind() {
        Holder h1 = new Holder();
        Holder h2 = new Holder();
        final Container c1 = h1.container;
        final Container c2 = h2.container;
        final IntVar a = new IntVar(c1, Holder.KEY_A);
        final IntVar b = new IntVar(c2, Holder.KEY_B);
        final IntVar c = new IntVar(c1, Holder.KEY_C);

        a.set(0);
        // bind b = a + 1
        b.bind(new IntBindingClosure() {
            public int asInt() {
                return a.get() + 1;
            }
        }, new IntVar[] { a });
        assertEquals(0, a.get());
        assertEquals(1, b.get());
        a.set(2);
        assertEquals(2, a.get());
        assertEquals(3, b.get());

        // bind c = b
        c.bind(new IntBindingClosure() {
            public int asInt() {
                return b.get();
            }
        }, new IntVar[] { b });

        assertEquals(3, c.get());
        a.set(4);
        assertEquals(5, b.get());
        assertEquals(5, c.get());
    }

    /*
     * Bind b lazily to a+1; ensure updates to a do not force updates to b, but that b.get() returns the right value
     */
    public void testLazyBind() {
        Holder h = new Holder();
        Container container = h.container;
        final IntVar a = new IntVar(container, Holder.KEY_A);
        final IntVar b = new IntVar(container, Holder.KEY_B);

        a.set(0);
        // bind b = a + 1
        b.bindLazy(new IntBindingClosure() {
            public int asInt() {
                return a.get() + 1;
            }
        }, new IntVar[] { a });
        assertEquals(0, a.get());
        assertFalse(b.isValid());
        assertEquals(1, b.get());
        assertTrue(b.isValid());

        a.set(2);
        assertEquals(2, a.get());
        assertFalse(b.isValid());
        assertEquals(3, b.get());
        assertTrue(b.isValid());
    }

    /*
     * Bind b to a+a, but ensure that b is only recalculated once
     */
    public void testMulti() {
        Holder h = new Holder();
        Container container = h.container;
        final IntVar a = new IntVar(container, Holder.KEY_A);
        final IntVar b = new IntVar(container, Holder.KEY_B);

        class BClosure extends IntBindingClosure {
            public int count;

            public int asInt() {
                ++count;
                return a.get() + a.get();
            }
        }

        a.set(0);
        // bind b = a + a
        BClosure bClosure = new BClosure();
        b.bindLazy(bClosure, new IntVar[] { a, a });
        int count = bClosure.count;

        a.set(2);
        assertEquals(4, b.get());
        assertEquals(count+1, bClosure.count);
    }

    public void testDouble() {
        final Holder h = new Holder();
        Container container = h.container;
        final IntVar a = new IntVar(container, Holder.KEY_A);
        final DoubleVar d = new DoubleVar(container, Holder.KEY_D);

        d.bind(new DoubleBindingClosure() {
            public double asDouble() {
                return a.get() + 1;
            }
        }, new Var[] { a });

        assertEquals(1.0, d.get());
        h.setA(3);
        assertEquals(4.0, d.get());
    }

    public void testString() {
        final Holder h = new Holder();
        Container container = h.container;
        final IntVar a = new IntVar(container, Holder.KEY_A);
        final ReferenceVar e = new ReferenceVar(container, Holder.KEY_E);

        e.bind(new ReferenceBindingClosure() {
            public String asReference() {
                return Integer.toString(a.get() + 1);
            }
        }, new Var[] { a });

        assertEquals("1", e.get());
        h.setA(3);
        assertEquals("4", e.get());
    }
}

