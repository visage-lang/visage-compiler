package com.sun.javafx.runtime.location;

/**
 * SimpleIntVariable
 *
 * @author Brian Goetz
 */
public class SimpleIntVariable extends AbstractSimpleVariable implements IntLocation {
    private int value;

    public SimpleIntVariable(int value) {
        this.value = value;
    }

    public int getAsInt() {
        return value;
    }

    public int setAsInt(int value) {
        int oldValue = this.value;
        this.value = value;
        if (dependencies != null && oldValue != value)
            notifyDependencies();
        return value;
    }

    public void setDefault() {
        setAsInt(0);
    }

    public Integer get() {
        return getAsInt();
    }

    public Integer set(Integer value) {
        return setAsInt(value);
    }

    public void addChangeListener(IntChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public int setAsIntFromLiteral(int value) {
        throw new UnsupportedOperationException();
    }

    public Integer setFromLiteral(Integer value) {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(ObjectChangeListener<Integer> listener) {
        throw new UnsupportedOperationException();
    }
}
