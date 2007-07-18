package com.sun.javafx.runtime.bind;

import junit.framework.TestCase;
import com.sun.javafx.runtime.bind.BindingClosure;
import com.sun.javafx.runtime.bind.Container;
import com.sun.javafx.runtime.bind.LocationKey;

/**
 * BindingTestCase
 *
 * @author Brian Goetz
 */
public class BindingTestCase extends TestCase {
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
