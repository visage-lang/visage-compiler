package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.Sequence;

/**
 * PointerFactory$Intf
 *
 * @author Brian Goetz
 */
public interface PointerFactory$Intf extends FXObject {

    public ObjectVariable<Pointer> make$$bound$int(IntLocation location);

    public ObjectVariable<Pointer> make$$bound$double(DoubleLocation location);

    public ObjectVariable<Pointer> make$$bound$boolean(BooleanLocation location);

    public <T> ObjectVariable<Pointer> make$$bound$java_lang_Object(ObjectLocation<T> location);

    public <T> ObjectVariable<Pointer> make$$bound$com_sun_javafx_runtime_sequence_Sequence(SequenceLocation<T> location);
}
