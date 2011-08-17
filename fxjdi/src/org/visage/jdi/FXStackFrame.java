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

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.StackFrame;
import com.sun.jdi.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sundar
 */
public class FXStackFrame extends FXMirror implements StackFrame {
    public FXStackFrame(FXVirtualMachine fxvm, StackFrame underlying) {
        super(fxvm, underlying);
    }

    // Is this frame executing JavaFX code?
    public boolean isJavaFXFrame() {
        return location().declaringType().isJavaFXType();
    }

    public List<Value> getArgumentValues() {
        return FXWrapper.wrapValues(virtualMachine(), underlying().getArgumentValues());
    }

    public FXValue getValue(LocalVariable var) {
        if (isJavaFXSyntheticLocalVar(var.name())) {
            throw new IllegalArgumentException("invalid var: " + var.name());
        }
        return FXWrapper.wrap(virtualMachine(), underlying().getValue(FXWrapper.unwrap(var)));
    }

    public Map<LocalVariable, Value> getValues(List<? extends LocalVariable> vars) {
        Map<LocalVariable, LocalVariable> fieldMap = new HashMap<LocalVariable, LocalVariable>();
        List<LocalVariable> unwrappedLocalVariables = new ArrayList<LocalVariable>();
        for (LocalVariable var : vars) {
            LocalVariable unwrapped = FXWrapper.unwrap(var);
            if (isJavaFXSyntheticLocalVar(unwrapped.name())) {
                throw new IllegalArgumentException("invalid var: " + var.name());
            }
            unwrappedLocalVariables.add(unwrapped);
            fieldMap.put(unwrapped, var);
        }
        Map<LocalVariable, Value> fieldValues = underlying().getValues(unwrappedLocalVariables);
        Map<LocalVariable, Value> result = new HashMap<LocalVariable, Value>();
        for (Map.Entry<LocalVariable, Value> entry: fieldValues.entrySet()) {
            result.put(fieldMap.get(entry.getKey()), entry.getValue());
        }
        return result;
    }

    public FXLocation location() {
        return FXWrapper.wrap(virtualMachine(), underlying().location());
    }

    public void setValue(LocalVariable var, Value value) throws InvalidTypeException, ClassNotLoadedException {
        if (isJavaFXSyntheticLocalVar(var.name())) {
            throw new IllegalArgumentException("invalid var: " + var.name());
        }
        underlying().setValue(FXWrapper.unwrap(var), FXWrapper.unwrap(value));
    }

    public FXObjectReference thisObject() {
        return FXWrapper.wrap(virtualMachine(), underlying().thisObject());
    }

    public FXThreadReference thread() {
        return FXWrapper.wrap(virtualMachine(), underlying().thread());
    }

    public FXLocalVariable visibleVariableByName(String name) throws AbsentInformationException {
        if (isJavaFXSyntheticLocalVar(name)) {
            return null;
        } else {
            return FXWrapper.wrap(virtualMachine(), underlying().visibleVariableByName(name));
        }
    }

    public List<LocalVariable> visibleVariables() throws AbsentInformationException {
        List<LocalVariable> locals = underlying().visibleVariables();
        List<LocalVariable> result = new ArrayList<LocalVariable>();
        for (LocalVariable var : locals) {
            if (! isJavaFXSyntheticLocalVar(var.name())) {
                result.add(FXWrapper.wrap(virtualMachine(), var));
            }
        }
        return result;
    }

    @Override
    protected StackFrame underlying() {
        return (StackFrame) super.underlying();
    }

    public boolean isJavaFXSyntheticLocalVar(String name) {
        // FIXME: can we have a better test for JavaFX synthetic local var?
        return isJavaFXFrame() && name.indexOf('$') != -1;
    }
}
