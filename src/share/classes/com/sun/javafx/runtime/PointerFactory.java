package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.Sequence;

/**
 * PointerFactory
 *
 * @author Brian Goetz
 */
public class PointerFactory implements PointerFactory$Intf, FXObject {

    public void initialize$() { }

    public Pointer make(int value) {
        throw new UnsupportedOperationException("PointerFactory.make() should only be called in a bind context");
    }

    public ObjectVariable<Pointer> make$$bound$int(IntLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public Pointer make(double value) {
        throw new UnsupportedOperationException("PointerFactory.make() should only be called in a bind context");
    }

    public ObjectVariable<Pointer> make$$bound$double(DoubleLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public Pointer make(boolean value) {
        throw new UnsupportedOperationException("PointerFactory.make() should only be called in a bind context");
    }

    public ObjectVariable<Pointer> make$$bound$boolean(BooleanLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public Pointer make(Object value) {
        throw new UnsupportedOperationException("PointerFactory.make() should only be called in a bind context");
    }

    public ObjectVariable<Pointer> make$$bound$java_lang_Object(ObjectLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public Pointer make(Sequence value) {
        throw new UnsupportedOperationException("PointerFactory.make() should only be called in a bind context");
    }

    public ObjectVariable<Pointer> make$$bound$com_sun_javafx_runtime_sequence_Sequence(SequenceLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }
}
