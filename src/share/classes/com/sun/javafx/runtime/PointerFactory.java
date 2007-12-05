package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.Sequence;

/**
 * PointerFactory
 *
 * @author Brian Goetz
 */
public class PointerFactory implements PointerFactory$Intf, FXObject {
    public static void setDefaults$(final PointerFactory$Intf receiver$) {
    }

    public static int getNumFields$() { return 0; }
    private com.sun.javafx.runtime.InitHelper initHelper$ = new com.sun.javafx.runtime.InitHelper(0);

    public void initialize$() {
        setDefaults$(this);
        userInit$(this);
        initHelper$.initialize();
        initHelper$ = null;
    }

    public static void userInit$(final PointerFactory$Intf receiver$) {
    }

    public Pointer make(int value) {
        throw new UnsupportedOperationException();
    }

    public ObjectLocation<Pointer> make$bound(IntLocation location) {
        return ObjectVar.make(Pointer.make(location));
    }

    public Pointer make(double value) {
        throw new UnsupportedOperationException();
    }

    public ObjectLocation<Pointer> make$bound(DoubleLocation location) {
        return ObjectVar.make(Pointer.make(location));
    }

    public Pointer make(boolean value) {
        throw new UnsupportedOperationException();
    }

    public ObjectLocation<Pointer> make$bound(BooleanLocation location) {
        return ObjectVar.make(Pointer.make(location));
    }

    public Pointer make(Object value) {
        throw new UnsupportedOperationException();
    }

    public ObjectLocation<Pointer> make$bound(ObjectLocation location) {
        return ObjectVar.make(Pointer.make(location));
    }

    public Pointer make(Sequence value) {
        throw new UnsupportedOperationException();
    }

    public ObjectLocation<Pointer> make$bound(SequenceLocation location) {
        return ObjectVar.make(Pointer.make(location));
    }
}
