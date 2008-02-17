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

    public void setInitialized(Location loc) { }

    public static void userInit$(final PointerFactory$Intf receiver$) {
    }

    public Pointer make(int value) {
        throw new UnsupportedOperationException("PointerFactory.make() should only be called in a bind context");
    }

    public ObjectVariable<Pointer> make$$bound$int(IntVariable location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public Pointer make(double value) {
        throw new UnsupportedOperationException("PointerFactory.make() should only be called in a bind context");
    }

    public ObjectVariable<Pointer> make$$bound$double(DoubleVariable location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public Pointer make(boolean value) {
        throw new UnsupportedOperationException("PointerFactory.make() should only be called in a bind context");
    }

    public ObjectVariable<Pointer> make$$bound$boolean(BooleanVariable location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public Pointer make(Object value) {
        throw new UnsupportedOperationException("PointerFactory.make() should only be called in a bind context");
    }

    public ObjectVariable<Pointer> make$$bound$java_lang_Object(ObjectVariable location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public Pointer make(Sequence value) {
        throw new UnsupportedOperationException("PointerFactory.make() should only be called in a bind context");
    }

    public ObjectVariable<Pointer> make$$bound$com_sun_javafx_runtime_sequence_Sequence(SequenceVariable location) {
        return ObjectVariable.make(Pointer.make(location));
    }
}
