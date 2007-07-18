package com.sun.javafx.runtime.bind;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * IntLocalLocationKey
 *
 * @author Brian Goetz
 */
public class IntLocalLocationKey implements LocalsLocationKey {
    private final static Unsafe unsafe = UnsafeUtil.getUnsafe();
    private final static long longArrayBase = unsafe.arrayBaseOffset(long[].class);
    private final static long longArrayScale = unsafe.arrayIndexScale(long[].class);
    protected final int sequence;

    public IntLocalLocationKey(int sequence) {
        this.sequence = sequence;
    }

    public int getInt(Object base) {
        return unsafe.getInt(base, longArrayBase + longArrayScale*sequence);
    }

    public void setInt(Object base, int value) {
        unsafe.putInt(base, longArrayBase + longArrayScale*sequence, value);
    }

    public double getDouble(Object base) {
        throw new UnsupportedOperationException();
    }

    public void setDouble(Object base, double value) {
        throw new UnsupportedOperationException();
    }

    public void setReference(Object base, Object value) {
        throw new UnsupportedOperationException();
    }

    public Object getReference(Object base) {
        throw new UnsupportedOperationException();
    }

    public void update(Object base, BindingClosure closure) {
        setInt(base, closure.asInt());
    }


    public int getSequence() {
        return sequence;
    }

}
