/*
 * Copyright 2010 Sun Microsystems, Inc.  All Rights Reserved.
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

package org.visage.jdi;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.Value;
import java.util.List;

/**
 *
 * @author sundar
 */
public class VisageArrayReference extends VisageObjectReference implements ArrayReference {
    public VisageArrayReference(VisageVirtualMachine visagevm, ArrayReference underlying) {
        super(visagevm, underlying);
    }

    public VisageValue getValue(int index) {
        return VisageWrapper.wrap(virtualMachine(), underlying().getValue(index));
    }

    public List<Value> getValues() {
        return VisageWrapper.wrapValues(virtualMachine(), underlying().getValues());
    }

    public List<Value> getValues(int index, int length) {
        return VisageWrapper.wrapValues(virtualMachine(), underlying().getValues(index, length));
    }

    public int length() {
        return underlying().length();
    }

    public void setValue(int index, Value value)
            throws InvalidTypeException, ClassNotLoadedException {
        underlying().setValue(index, VisageWrapper.unwrap(value));
    }

    public void setValues(List<? extends Value> values)
            throws InvalidTypeException, ClassNotLoadedException {
        underlying().setValues(VisageWrapper.unwrapValues(values));
    }

    public void setValues(int index, List<? extends Value> values, int srcIndex, int length)
            throws InvalidTypeException, ClassNotLoadedException {
        underlying().setValues(index, VisageWrapper.unwrapValues(values), srcIndex, length);
    }

    @Override
    protected ArrayReference underlying() {
        return (ArrayReference) super.underlying();
    }
}
