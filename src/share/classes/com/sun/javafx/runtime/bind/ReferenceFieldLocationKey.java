package com.sun.javafx.runtime.bind;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * ReferenceFieldLocation
 *
 * @author Brian Goetz
 */
public class ReferenceFieldLocationKey implements FieldLocationKey {
    private final static Unsafe unsafe = UnsafeUtil.getUnsafe();
    private final Field field;                           // @@@ OPT: Needed only for assertions
    protected final long offset;
    protected final int sequence;

    public ReferenceFieldLocationKey(Field field, int sequence) {
        offset = unsafe.objectFieldOffset(field);
        this.field = field;
        this.sequence = sequence;
    }

    public int getInt(Object base) {
        throw new UnsupportedOperationException();
    }

    public void setInt(Object base, int value) {
        throw new UnsupportedOperationException();
    }

    public double getDouble(Object base) {
        throw new UnsupportedOperationException();
    }

    public void setDouble(Object base, double value) {
        throw new UnsupportedOperationException();
    }

    public void setReference(Object base, Object value) {
        assert field.getType().isAssignableFrom(value.getClass());
        assert field.getDeclaringClass().isAssignableFrom(base.getClass());
        unsafe.putObject(base, offset, value);
    }

    public Object getReference(Object base) {
        assert Object.class.isAssignableFrom(field.getType());
        assert field.getDeclaringClass().isAssignableFrom(base.getClass());
        return unsafe.getObject(base, offset);
    }

    public void update(Object base, BindingClosure closure) {
        setReference(base, closure.asReference());
    }

    public int getSequence() {
        return sequence;
    }

}
