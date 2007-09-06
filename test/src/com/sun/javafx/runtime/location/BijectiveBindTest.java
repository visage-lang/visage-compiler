package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.CircularBindingException;

/**
 * BijectiveBindTest
 *
 * @author Brian Goetz
 */
public class BijectiveBindTest extends JavaFXTestCase {
    public void testFailures() {
        final IntLocation i = IntVar.make(0);
        final IntLocation ie = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return 0;
            }
        });
        final ObjectLocation<String> s = ObjectVar.make("");
        final ObjectLocation<String> se = ObjectExpression.make(new ObjectBindingExpression<String>() {
            public String get() {
                return "";
            }
        });
        assertThrows(IllegalArgumentException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(ie, ie, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });
        assertThrows(IllegalArgumentException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(i, ie, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });
        assertThrows(IllegalArgumentException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(ie, i, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });
        assertThrows(IllegalArgumentException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(se, se, new Bijection<String, String>() {
                    public String mapForwards(String a) { return ""; }
                    public String mapBackwards(String b) { return ""; }
                });
            }
        });
        assertThrows(IllegalArgumentException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(s, se, new Bijection<String, String>() {
                    public String mapForwards(String a) { return ""; }
                    public String mapBackwards(String b) { return ""; }
                });
            }
        });
        assertThrows(IllegalArgumentException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(se, s, new Bijection<String, String>() {
                    public String mapForwards(String a) { return ""; }
                    public String mapBackwards(String b) { return ""; }
                });
            }
        });
    }

    public void testIntBijection() {
        final IntLocation i = IntVar.make(0);
        final IntLocation j = IntVar.make(7);
        Bindings.bijectiveBind(i, j, new Bijection<Integer, Integer>() {
            public Integer mapForwards(Integer a) { return a + 1; }
            public Integer mapBackwards(Integer b) { return b - 1; }
        });
        assertEquals(6, i);
        j.set(9);
        assertEquals(8, i);
        i.set(4);
        assertEquals(5, j);

        assertEquals(Bindings.getPeerLocations(i), new Location[] { j });
        assertEquals(Bindings.getPeerLocations(j), new Location[] { i });
    }

    public void testIntStringBijection() {
        final IntLocation i = IntVar.make(0);
        final ObjectLocation<String> s = ObjectVar.make("7");
        Bindings.bijectiveBind(i, s, new Bijection<Integer, String>() {
            public String mapForwards(Integer a) {
                return Integer.toString(a);
            }

            public Integer mapBackwards(String b) {
                return Integer.parseInt(b);
            }
        });

        assertEquals(7, i.get());
        i.set(9);
        assertEquals("9", s.get());
        s.set("11");
        assertEquals(11, i.get());

        assertEquals(Bindings.getPeerLocations(i), new Location[] { s });
        assertEquals(Bindings.getPeerLocations(s), new Location[] { i });
    }

    public void testGarbageCollection() {
        final IntLocation i = IntVar.make(0);
        if (i.get() == 0) {
            final IntLocation j = IntVar.make(7);
            Bindings.bijectiveBind(i, j, new Bijection<Integer, Integer>() {
                public Integer mapForwards(Integer a) { return a + 1; }
                public Integer mapBackwards(Integer b) { return b - 1; }
            });
            assertEquals(1, ((AbstractLocation) i).getListenerCount());
            assertEquals(1, ((AbstractLocation) j).getListenerCount());
            assertEquals(Bindings.getPeerLocations(i), new Location[] { j });
        }

        System.gc();
        assertEquals(1, ((AbstractLocation) i).getListenerCount());
        i.set(3);
        assertEquals(0, ((AbstractLocation) i).getListenerCount());            

        assertEquals(Bindings.getPeerLocations(i) /* empty */);
    }

    public void testChainedBijection() {
        final IntLocation i = IntVar.make(0);
        final IntLocation j = IntVar.make(7);
        final IntLocation k = IntVar.make(9);
        Bindings.bijectiveBind(i, j, new Bijection<Integer, Integer>() {
            public Integer mapForwards(Integer a) { return a + 1; }
            public Integer mapBackwards(Integer b) { return b - 1; }
        });
        Bindings.bijectiveBind(j, k, new Bijection<Integer, Integer>() {
            public Integer mapForwards(Integer a) { return a + 1; }
            public Integer mapBackwards(Integer b) { return b - 1; }
        });
        assertEquals(7, i.get());
        assertEquals(8, j.get());
        assertEquals(9, k.get());

        i.set(0);
        assertEquals(0, i.get());
        assertEquals(1, j.get());
        assertEquals(2, k.get());

        j.set(5);
        assertEquals(4, i.get());
        assertEquals(5, j.get());
        assertEquals(6, k.get());

        k.set(11);
        assertEquals(9, i.get());
        assertEquals(10, j.get());
        assertEquals(11, k.get());

        assertEquals(Bindings.getPeerLocations(i), j, k);
        assertEquals(Bindings.getPeerLocations(j), i, k);
        assertEquals(Bindings.getPeerLocations(k), i, j);
        assertTrue(Bindings.isPeerLocation(i, j));
        assertTrue(Bindings.isPeerLocation(j, k));
        assertTrue(Bindings.isPeerLocation(i, k));
        assertTrue(Bindings.isPeerLocation(j, i));
        assertTrue(Bindings.isPeerLocation(k, j));
        assertTrue(Bindings.isPeerLocation(k, i));
    }

    public void testCircularBijection() {
        final IntLocation i = IntVar.make(0);
        final IntLocation j = IntVar.make(7);

        Bindings.bijectiveBind(i, j, new Bijection<Integer, Integer>() {
            public Integer mapForwards(Integer a) { return a + 1; }
            public Integer mapBackwards(Integer b) { return b - 1; }
        });

        assertEquals(Bindings.getPeerLocations(i), new Location[] { j });
        assertEquals(Bindings.getPeerLocations(j), new Location[] { i });

        assertThrows(CircularBindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(i, j, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });

        assertThrows(CircularBindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(j, i, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });

        final IntLocation k = IntVar.make(9);
        Bindings.bijectiveBind(j, k, new Bijection<Integer, Integer>() {
            public Integer mapForwards(Integer a) { return a + 1; }
            public Integer mapBackwards(Integer b) { return b - 1; }
        });

        assertThrows(CircularBindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(i, k, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });

        assertThrows(CircularBindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(k, i, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });

        assertThrows(CircularBindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(k, j, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });

        assertThrows(CircularBindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(j, k, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });
    }
}
