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
    public ObjectLocation<Pointer> make$bound(IntLocation location);

    public Pointer make(double value);
    public ObjectLocation<Pointer> make$bound(DoubleLocation location);

    public Pointer make(boolean value);
    public ObjectLocation<Pointer> make$bound(BooleanLocation location);

    public Pointer make(Object value);
    public ObjectLocation<Pointer> make$bound(ObjectLocation location);

    public Pointer make(Sequence value);
    public ObjectLocation<Pointer> make$bound(SequenceLocation location);
}
