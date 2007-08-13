package com.sun.javafx.runtime.bind;

import junit.framework.TestCase;

/**
 * BindingTest
 *
 * @author Brian Goetz
 */
public abstract class BindingTestCase extends TestCase {

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

    protected static class Holder {
        public final static LocationKey KEY_A;
        public final static LocationKey KEY_B;
        public final static LocationKey KEY_C;
        public final static LocationKey KEY_D;
        public final static LocationKey KEY_E;

        public int a;
        public int b;
        public int c;
        public double d;
        public String e;

        static {
            try {
                KEY_A = new IntFieldLocationKey(Holder.class.getField("a"), 0);
                KEY_B = new IntFieldLocationKey(Holder.class.getField("b"), 1);
                KEY_C = new IntFieldLocationKey(Holder.class.getField("c"), 2);
                KEY_D = new DoubleFieldLocationKey(Holder.class.getField("d"), 3);
                KEY_E = new ReferenceFieldLocationKey(Holder.class.getField("e"), 4);
            } catch (NoSuchFieldException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        Container container = new InstanceContainer(toString(), this);

        public int getA() {
            return container.getIntValue(KEY_A);
        }

        public int getB() {
            return container.getIntValue(KEY_B);
        }

        public int getC() {
            return container.getIntValue(KEY_C);
        }

        public double getD() {
            return container.getDoubleValue(KEY_D);
        }

        public String getE() {
            return (String) container.getReferenceValue(KEY_E);
        }

        public void setA(int value) {
            container.setIntValue(KEY_A, value);
        }

        public void setB(int value) {
            container.setIntValue(KEY_B, value);
        }

        public void setC(int value) {
            container.setIntValue(KEY_C, value);
        }

        public void setD(double value) {
            container.setDoubleValue(KEY_D, value);
        }

        public void setE(String value) {
            container.setReferenceValue(KEY_E, value);
        }
    }
}

