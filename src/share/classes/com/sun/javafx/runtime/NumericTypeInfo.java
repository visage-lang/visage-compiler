package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;

/**
 * NumericTypeInfo
 *
 * @author Brian Goetz
 */
public class NumericTypeInfo<T extends Number, L extends ObjectLocation<T>> extends TypeInfo<T, L> {
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

    // This ugly and not typesafe construct eliminates lots of small classes, which add a lot to our static footprint.
    // Such optimizations are ugly but needed for smaller-memory platforms.
    @SuppressWarnings("unchecked")
    public L makeLocation() {
        switch (type) {
            case INT: return (L) IntVariable.make();
            case SHORT: return (L) ShortVariable.make();
            case BYTE: return (L) ByteVariable.make();
            case LONG: return (L) LongVariable.make();
            case FLOAT: return (L) FloatVariable.make();
            case DOUBLE: return (L) DoubleVariable.make();
            default: return super.makeLocation();

        }
    }

    @SuppressWarnings("unchecked")
    public L makeDefaultConstant() {
        switch (type) {
            case INT: return (L) IntConstant.make(IntVariable.DEFAULT);
            case SHORT: return (L) ShortConstant.make(ShortVariable.DEFAULT);
            case BYTE: return (L) ByteConstant.make(ByteVariable.DEFAULT);
            case LONG: return (L) LongConstant.make(LongVariable.DEFAULT);
            case FLOAT: return (L) FloatConstant.make(FloatVariable.DEFAULT);
            case DOUBLE: return (L) DoubleConstant.make(DoubleVariable.DEFAULT);
            default: return super.makeLocation();

        }
    }

    // This ugly and not typesafe construct eliminates lots of small classes, which add a lot to our static footprint.
    // Such optimizations are ugly but needed for smaller-memory platforms.
    @SuppressWarnings("unchecked")
    public<V extends Number> T asPreferred(NumericTypeInfo<V, ?> otherType, V otherValue) {
        switch (type) {
            case INT: return (T) (Integer) otherType.intValue(otherValue);
            case SHORT: return (T) (Short) otherType.shortValue(otherValue);
            case BYTE: return (T) (Byte) otherType.byteValue(otherValue);
            case LONG: return (T) (Long) otherType.longValue(otherValue);
            case FLOAT: return (T) (Float) otherType.floatValue(otherValue);
            case DOUBLE: return (T) (Double) otherType.doubleValue(otherValue);
            default:
                throw new IllegalArgumentException();
        }
    }
}
