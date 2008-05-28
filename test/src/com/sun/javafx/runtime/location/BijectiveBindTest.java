/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.CircularBindingException;
import com.sun.javafx.runtime.BindingException;

/**
 * BijectiveBindTest
 *
 * @author Brian Goetz
 */
public class BijectiveBindTest extends JavaFXTestCase {
    public void testFailures() {
        final IntLocation i = IntVariable.make(0);
        final IntLocation ie = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return 0;
            }
        });
        final ObjectLocation<String> s = ObjectVariable.make("");
        final ObjectLocation<String> se = ObjectVariable.make(new ObjectBindingExpression<String>() {
            public String computeValue() {
                return "";
            }
        }, new Location[] {});
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(ie, ie, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(i, ie, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(ie, i, new Bijection<Integer, Integer>() {
                    public Integer mapForwards(Integer a) { return 0; }
                    public Integer mapBackwards(Integer b) { return 0; }
                });
            }
        });
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(se, se, new Bijection<String, String>() {
                    public String mapForwards(String a) { return ""; }
                    public String mapBackwards(String b) { return ""; }
                });
            }
        });
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(s, se, new Bijection<String, String>() {
                    public String mapForwards(String a) { return ""; }
                    public String mapBackwards(String b) { return ""; }
                });
            }
        });
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                Bindings.bijectiveBind(se, s, new Bijection<String, String>() {
                    public String mapForwards(String a) { return ""; }
                    public String mapBackwards(String b) { return ""; }
                });
            }
        });
    }

    public void testIntBijection() {
        final IntLocation i = IntVariable.make(0);
        final IntLocation j = IntVariable.make(7);
        Bindings.bijectiveBind(i, j, new Bijection<Integer, Integer>() {
            public Integer mapForwards(Integer a) { return a + 1; }
            public Integer mapBackwards(Integer b) { return b - 1; }
        });
        assertEquals(6, i);
        j.setAsInt(9);
        assertEquals(8, i);
        i.setAsInt(4);
        assertEquals(5, j);

        assertEquals(Bindings.getPeerLocations(i), new Location[] { j });
        assertEquals(Bindings.getPeerLocations(j), new Location[] { i });
    }

    public void testIntStringBijection() {
        final IntLocation i = IntVariable.make(0);
        final ObjectLocation<String> s = ObjectVariable.make("7");
        Bindings.bijectiveBind(i, s, new Bijection<Integer, String>() {
            public String mapForwards(Integer a) {
                return Integer.toString(a);
            }

            public Integer mapBackwards(String b) {
                return Integer.parseInt(b);
            }
        });

        assertEquals(7, i.getAsInt());
        i.setAsInt(9);
        assertEquals("9", s.get());
        s.set("11");
        assertEquals(11, i.getAsInt());

        assertEquals(Bindings.getPeerLocations(i), new Location[] { s });
        assertEquals(Bindings.getPeerLocations(s), new Location[] { i });
    }

    public void testGarbageCollection() {
        final IntLocation i = IntVariable.make(0);
        if (i.getAsInt() == 0) {
            final IntLocation j = IntVariable.make(7);
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
        i.setAsInt(3);
        assertEquals(0, ((AbstractLocation) i).getListenerCount());            

        assertEquals(Bindings.getPeerLocations(i) /* empty */);
    }

    public void testChainedBijection() {
        final IntLocation i = IntVariable.make(0);
        final IntLocation j = IntVariable.make(7);
        final IntLocation k = IntVariable.make(9);
        Bindings.bijectiveBind(i, j, new Bijection<Integer, Integer>() {
            public Integer mapForwards(Integer a) { return a + 1; }
            public Integer mapBackwards(Integer b) { return b - 1; }
        });
        Bindings.bijectiveBind(j, k, new Bijection<Integer, Integer>() {
            public Integer mapForwards(Integer a) { return a + 1; }
            public Integer mapBackwards(Integer b) { return b - 1; }
        });
        assertEquals(7, i.getAsInt());
        assertEquals(8, j.getAsInt());
        assertEquals(9, k.getAsInt());

        i.setAsInt(0);
        assertEquals(0, i.getAsInt());
        assertEquals(1, j.getAsInt());
        assertEquals(2, k.getAsInt());

        j.setAsInt(5);
        assertEquals(4, i.getAsInt());
        assertEquals(5, j.getAsInt());
        assertEquals(6, k.getAsInt());

        k.setAsInt(11);
        assertEquals(9, i.getAsInt());
        assertEquals(10, j.getAsInt());
        assertEquals(11, k.getAsInt());

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
        final IntLocation i = IntVariable.make(0);
        final IntLocation j = IntVariable.make(7);
        assertFalse(Bindings.isPeerLocation(i, j));

        Bindings.bijectiveBind(i, j, new Bijection<Integer, Integer>() {
            public Integer mapForwards(Integer a) { return a + 1; }
            public Integer mapBackwards(Integer b) { return b - 1; }
        });
        assertTrue(Bindings.isPeerLocation(i, j));

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

        final IntLocation k = IntVariable.make(9);
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


    public void testIdentity() {
        IntVariable a = IntVariable.make();
        IntVariable b = IntVariable.make();
        b.bijectiveBind(a);
        assertEquals(0, a.getAsInt());
        assertEquals(0, b.getAsInt());
        b.set(1);
        assertEquals(1, a.getAsInt());
        assertEquals(1, b.getAsInt());
    }
}
