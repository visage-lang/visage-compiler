package com.sun.javafx.runtime;

/**
 * NumericTypeInfo
 *
 * @author Brian Goetz
 */
public abstract class NumericTypeInfo<T extends Number> extends TypeInfo<T> {
    public NumericTypeInfo(T defaultValue, TypeInfo.Types type) {
        super(defaultValue, type);
    }

    public boolean isNumeric() {
        return true;
    }

    public long longValue(T value) {
        return value.longValue();
    }

    public int intValue(T value) {
        return value.intValue();
    }

    public short shortValue(T value) {
        return value.shortValue();
    }

    public byte byteValue(T value) {
        return value.byteValue();
    }

    public double doubleValue(T value) {
        return value.doubleValue();
    }

    public float floatValue(T value) {
        return value.floatValue();
    }

    public T[] makeArray(int size) {
        return Util.newNumberArray(size);
    }

    public abstract<V extends Number> T asPreferred(NumericTypeInfo<V> otherType, V otherValue);
}
