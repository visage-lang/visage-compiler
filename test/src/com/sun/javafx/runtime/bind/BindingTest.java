package com.sun.javafx.runtime.bind;

import junit.framework.TestCase;

/**
 * BindingTest
 *
 * @author Brian Goetz
 */
public class BindingTest extends TestCase {

    /** FIXME:  added simple test so that this test suite doesn't fail due to no tests. */
    public void testInstances() throws Exception {
        Holder h = new Holder();
        Container container = h.container;
        LocationKey key = new LocationKey() {
            @Override public int getSequence() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override public int getInt(Object base) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override public void setInt(Object base, int value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override public double getDouble(Object base) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override public void setDouble(Object base, double value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override public Object getReference(Object base) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override public void setReference(Object base, Object value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override public void update(Object base, BindingClosure closure) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        Var v = new IntVar(container, key);
        v = new DoubleVar(container, key);
        v = new ReferenceVar<String>(container, key);
    }

    protected class Var {
        protected final Container container;
        protected final LocationKey key;

        protected Var(Container container, LocationKey key) {
            this.key = key;
            this.container = container;
        }
    }

    protected class IntVar extends Var {
        public IntVar(Container container, LocationKey key) {
            super(container, key);
        }

        public void set(int value) {
            container.setIntValue(key, value);
        }

        public int get() {
            return container.getIntValue(key);
        }

        public boolean isValid() {
            return container.isValid(key);
        }

        public void bind(BindingClosure closure, Var[] dependencies) {
            container.setBinding(key, false, closure);
            for (Var v : dependencies)
                v.container.addDependency(v.key, container, key);
        }

        public void bindLazy(BindingClosure closure, Var[] dependencies) {
            container.setBinding(key, true, closure);
            for (Var v : dependencies)
                v.container.addDependency(v.key, container, key);
        }
    }

    protected class DoubleVar extends Var {
        public DoubleVar(Container container, LocationKey key) {
            super(container, key);
        }

        public void set(double value) {
            container.setDoubleValue(key, value);
        }

        public double get() {
            return container.getDoubleValue(key);
        }

        public boolean isValid() {
            return container.isValid(key);
        }

        public void bind(BindingClosure closure, Var[] dependencies) {
            container.setBinding(key, false, closure);
            for (Var v : dependencies)
                v.container.addDependency(v.key, container, key);
        }

        public void bindLazy(BindingClosure closure, Var[] dependencies) {
            container.setBinding(key, true, closure);
            for (Var v : dependencies)
                v.container.addDependency(v.key, container, key);
        }
    }

    protected class ReferenceVar<T> extends Var {
        public ReferenceVar(Container container, LocationKey key) {
            super(container, key);
        }

        public void set(T value) {
            container.setReferenceValue(key, value);
        }

        @SuppressWarnings("unchecked")
        public T get() {
            return (T) container.getReferenceValue(key);
        }

        public boolean isValid() {
            return container.isValid(key);
        }

        public void bind(BindingClosure closure, Var[] dependencies) {
            container.setBinding(key, false, closure);
            for (Var v : dependencies)
                v.container.addDependency(v.key, container, key);
        }

        public void bindLazy(BindingClosure closure, Var[] dependencies) {
            container.setBinding(key, true, closure);
            for (Var v : dependencies)
                v.container.addDependency(v.key, container, key);
        }
    }


}
