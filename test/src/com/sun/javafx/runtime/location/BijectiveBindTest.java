package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.JavaFXTestCase;

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
        }

        System.gc();
        assertEquals(1, ((AbstractLocation) i).getListenerCount());
        i.set(3);
        assertEquals(0, ((AbstractLocation) i).getListenerCount());            
    }
}
