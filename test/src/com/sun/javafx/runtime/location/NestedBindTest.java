package com.sun.javafx.runtime.location;

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
        return ObjectVariable.make(new ObjectBindingExpression<Foo>() {
            public Foo computeValue() {
                clearDynamicDependencies();
                return new Foo(n.get());
            }
        }, n);
    }

    public void testBoundFunction() {
        ObjectLocation<Foo> foo = ObjectVariable.make(new ObjectBindingExpression<Foo>() {
            private ObjectLocation<Foo> internal;

            public Foo computeValue() {
                if (internal == null) {
                    internal = makeFunctionNode(IntVariable.make(0));
                    addStaticDependent(internal);
                }
                return internal.get();
            }
        }, new Location[] { });
        assertEquals(0, foo.get().n);
    }
}
