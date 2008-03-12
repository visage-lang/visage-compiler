package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.Sequence;

/**
 * PointerFactory$Intf
 *
 * @author Brian Goetz
 */
public interface PointerFactory$Intf extends FXObject {
    public Pointer make(int value);
    public ObjectVariable<Pointer> make$$bound$int(IntLocation location);

    public Pointer make(double value);
    public ObjectVariable<Pointer> make$$bound$double(DoubleLocation location);

    public Pointer make(boolean value);
    public ObjectVariable<Pointer> make$$bound$boolean(BooleanLocation location);

    public Pointer make(Object value);
    public ObjectVariable<Pointer> make$$bound$java_lang_Object(ObjectLocation location);

    public Pointer make(Sequence value);
    public ObjectVariable<Pointer> make$$bound$com_sun_javafx_runtime_sequence_Sequence(SequenceLocation location);
}
