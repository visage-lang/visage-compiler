package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.runtime.JavaFXTestCase;

/**
 * NestedBindTest
 *
 * @author Brian Goetz
 */
public class NestedBindTest extends JavaFXTestCase {
    private static class Foo {
        public int n;

        private Foo(int n) {
            this.n = n;
        }
    }

    private ObjectLocation<Foo> makeFunctionNode(final IntLocation n) {
        return ObjectVariable.make(false, new ObjectBindingExpression<Foo>() {
            public Foo computeValue() {
                clearDynamicDependencies();
                return new Foo(n.get());
            }
        }, n);
    }

    public void testBoundFunction() {
        ObjectLocation<Foo> foo = ObjectVariable.make(false, new ObjectBindingExpression<Foo>() {
            private ObjectLocation<Foo> internal;

            public Foo computeValue() {
                if (internal == null) {
                    internal = makeFunctionNode(IntVariable.make(0));
                    addStaticDependent(internal);
                }
                return internal.get();
            }
        }, new Location[]{});
        assertEquals(0, foo.get().n);
    }

    // Inspired by JFXC-1033
    public void testNestedLiteral() {
        final ObjectLocation<String> name = ObjectVariable.make("Bob");
        CountingListener clName = new CountingListener();
        name.addInvalidationListener(clName);
        final int[] counters = new int[] { 0, 0 };
        final ObjectVariable<List<String>> inner = ObjectVariable.make(false,
                                                                       new ObjectBindingExpression<List<String>>() {
                                                                           protected DependencySource[] getStaticDependents() {
                                                                               return new Location[] { name };
                                                                           }

                                                                           public List<String> computeValue() {
                                                                               ++counters[0];
                                                                               List<String> v = new ArrayList<String>();
                                                                               v.add(name.get());
                                                                               return v;
                                                                           }
                                                                       });
        CountingListener clInner = new CountingListener();
        inner.addInvalidationListener(clInner);
        ObjectVariable<List<List<String>>> outer = ObjectVariable.make(false,
                                                                       new ObjectBindingExpression<List<List<String>>>() {
                                                                           protected DependencySource[] getStaticDependents() {
                                                                               return new Location[] { inner };
                                                                           }

                                                                           public List<List<String>> computeValue() {
                                                                               ++counters[1];
                                                                               List<List<String>> v = new ArrayList<List<String>>();
                                                                               v.add(inner.get());
                                                                               return v;
                                                                           }
                                                                       });
        CountingListener clOuter = new CountingListener();
        outer.addInvalidationListener(clOuter);
        assertEquals(0, clName.count);
        assertEquals(0, clInner.count);
        assertEquals(0, clOuter.count);
        assertEquals(1, counters[0]);
        assertEquals(1, counters[1]);
        assertEquals("[[Bob]]", outer.get().toString());
        name.set("Mary");
        assertEquals("[[Mary]]", outer.get().toString());
        assertEquals(1, clName.count);
        assertEquals(1, clInner.count);
        assertEquals(1, clOuter.count);
        assertEquals(2, counters[0]);
        assertEquals(2, counters[1]);
    }

    // Inspired by JFXC-1033
    public void testNestedIntegers() {
        final IntLocation n = IntVariable.make(1);
        CountingListener clName = new CountingListener();
        n.addInvalidationListener(clName);
        final int[] counters = new int[] { 0, 0, 0 };
        final IntLocation inner = IntVariable.make(false,
                                                   new IntBindingExpression() {
                                                       protected DependencySource[] getStaticDependents() {
                                                           return new Location[] { n };
                                                       }

                                                       public int computeValue() {
                                                           ++counters[0];
                                                           return n.get() * 2;
                                                       }
                                                   });
        CountingListener clInner = new CountingListener();
        inner.addInvalidationListener(clInner);
        final IntVariable middle = IntVariable.make(false,
                                             new IntBindingExpression() {
                                                 protected DependencySource[] getStaticDependents() {
                                                     return new Location[] { inner };
                                                 }

                                                 public int computeValue() {
                                                     ++counters[1];
                                                     return inner.get() * 2;
                                                 }
                                             });
        CountingListener clMiddle = new CountingListener();
        middle.addInvalidationListener(clMiddle);
        IntVariable outer = IntVariable.make(false,
                                             new IntBindingExpression() {
                                                 protected DependencySource[] getStaticDependents() {
                                                     return new Location[] { inner };
                                                 }

                                                 public int computeValue() {
                                                     ++counters[2];
                                                     return middle.get() * 2;
                                                 }
                                             });
        CountingListener clOuter = new CountingListener();
        outer.addInvalidationListener(clOuter);
        assertEquals(0, clName.count);
        assertEquals(0, clInner.count);
        assertEquals(0, clMiddle.count);
        assertEquals(0, clOuter.count);
        assertEquals(1, counters[0]);
        assertEquals(1, counters[1]);
        assertEquals(1, counters[2]);
        assertEquals(4, middle.getAsInt());
        assertEquals(8, outer.getAsInt());
        n.set(2);
        assertEquals(4, inner.getAsInt());
        assertEquals(8, middle.getAsInt());
        assertEquals(16, outer.getAsInt());
        assertEquals(1, clName.count);
        assertEquals(1, clInner.count);
        assertEquals(1, clMiddle.count);
        assertEquals(1, clOuter.count);
        assertEquals(2, counters[0]);
        assertEquals(2, counters[1]);
        assertEquals(2, counters[2]);
    }
}
