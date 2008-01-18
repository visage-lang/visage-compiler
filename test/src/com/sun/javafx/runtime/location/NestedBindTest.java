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
        return new ObjectExpression<Foo>(false, n) {
            protected Foo computeValue() {
                clearDynamicDependencies();
                return new Foo(n.get());
            }
        };
    }

    public void testBoundFunction() {
        ObjectLocation<Foo> foo = new ObjectExpression<Foo>(false) {
            private ObjectLocation<Foo> internal;

            protected Foo computeValue() {
                if (internal == null) {
                    internal = makeFunctionNode(IntVar.make(0));
                    addStaticDependent(internal);
                }
                return internal.get();
            }
        };
        assertEquals(0, foo.get().n);
    }
}
