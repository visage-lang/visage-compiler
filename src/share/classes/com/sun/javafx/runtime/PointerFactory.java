/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.Sequence;

/**
 * PointerFactory
 *
 * @author Brian Goetz
 */
public class PointerFactory implements FXObject {
    
    public PointerFactory() {
        this(false);
        initialize$();
    }

    public PointerFactory(boolean dummy) {
    }

    public void initialize$() { }

    public ObjectVariable<Pointer> make$$bound$byte(ByteLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public ObjectVariable<Pointer> make$$bound$short(ShortLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public ObjectVariable<Pointer> make$$bound$int(IntLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public ObjectVariable<Pointer> make$$bound$long(LongLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public ObjectVariable<Pointer> make$$bound$float(FloatLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public ObjectVariable<Pointer> make$$bound$double(DoubleLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public ObjectVariable<Pointer> make$$bound$boolean(BooleanLocation location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public <T> ObjectVariable<Pointer> make$$bound$java_lang_Object(ObjectLocation<T> location) {
        return ObjectVariable.make(Pointer.make(location));
    }

    public <T> ObjectVariable<Pointer> make$$bound$com_sun_javafx_runtime_sequence_Sequence(SequenceLocation<T> location) {
        return ObjectVariable.make(Pointer.make(location));
    }
}
