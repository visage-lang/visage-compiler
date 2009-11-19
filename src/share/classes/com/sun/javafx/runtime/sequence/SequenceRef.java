/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.runtime.sequence;
import com.sun.javafx.runtime.*;

/**
 *
 * @author Per Bothner
 */
public class SequenceRef<T> extends AbstractSequence<T> {
    // We could save space (at the cost of extra casting) by
    // using a single field for saved and instance.
    Sequence<T> saved;
    FXObject instance;
    int varNum;

    public SequenceRef(TypeInfo<T> ti, FXObject instance, int varNum) {
        super(ti);
        this.instance = instance;
        this.varNum = varNum;
    }
    
    public static <T> void save(Sequence<T> seq) {
        if (seq instanceof SequenceRef) {
            ((SequenceRef<T>) seq).save();
        }
    }

    public Sequence<T> save () {
        if (saved == null) {
            ArraySequence<T> arr = getElementType().emptySequence.makeNew(instance.size$(varNum));
            arr.incrementSharing();
            arr.add(this);
            saved = arr;
            instance = null;
        }
        return saved;
    }

    public int size() {
        return saved != null ? saved.size() : instance.size$(varNum);
    }

    public T get(int position) {
        return saved != null ? saved.get(position) : (T) instance.elem$(varNum, position);
    }

    @Override
    public boolean getAsBoolean(int position) {
        return saved != null ? saved.getAsBoolean(position) : instance.getAsBoolean$(varNum, position);
    }

    @Override
    public char getAsChar(int position) {
        return saved != null ? saved.getAsChar(position) : instance.getAsChar$(varNum, position);
    }

    @Override
    public byte getAsByte(int position) {
        return saved != null ? saved.getAsByte(position) : instance.getAsByte$(varNum, position);
    }

    @Override
    public short getAsShort(int position) {
        return saved != null ? saved.getAsShort(position) : instance.getAsShort$(varNum, position);
    }

    @Override
    public int getAsInt(int position) {
        return saved != null ? saved.getAsInt(position) : instance.getAsInt$(varNum, position);
    }

    @Override
    public long getAsLong(int position) {
        return saved != null ? saved.getAsLong(position) : instance.getAsLong$(varNum, position);
    }

    @Override
    public float getAsFloat(int position) {
        return saved != null ? saved.getAsFloat(position) : instance.getAsFloat$(varNum, position);
    }

    @Override
    public double getAsDouble(int position) {
        return saved != null ? saved.getAsDouble(position) : instance.getAsDouble$(varNum, position);
    }
}
