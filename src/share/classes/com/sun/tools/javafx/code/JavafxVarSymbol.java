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

package com.sun.tools.javafx.code;

import com.sun.tools.javafx.comp.JavafxDefs;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.util.Name;

/**
 * Class to hold and access variable information.
 *
 * @author Robert Field
 */
public class JavafxVarSymbol extends VarSymbol {

    private int typeKind = -1;
    private Type elementType = null;
    private final boolean isDotClass;

    private Type lastSeenType;
    private final JavafxTypes types;

    /** Construct a variable symbol, given its flags, name, type and owner.
     */
    public JavafxVarSymbol(JavafxTypes types, Name.Table names, long flags, Name name, Type type, Symbol owner) {
        super(flags, name, type, owner);
        this.types = types;
        this.isDotClass = name == names._class;
    }

    private void syncType() {
        if (lastSeenType != type) {
            typeKind = types.typeKind(type);
            switch (typeKind) {
                case JavafxDefs.TYPE_KIND_SEQUENCE:
                    elementType = types.elementType(type);
                    break;
                case JavafxDefs.TYPE_KIND_OBJECT:
                    elementType = type;
                    break;
                default:
                    elementType = null;
                    break;
            }
            lastSeenType = type;
        }
    }

    public boolean isMember() {
            return owner.kind == Kinds.TYP && !isDotClass;
    }

    public boolean isFXMember() {
        return isMember() && types.isJFXClass(owner);
    }

    public boolean isSequence() {
        syncType();
        return typeKind == JavafxDefs.TYPE_KIND_SEQUENCE;
    }

    public Type getElementType() {
        syncType();
        return elementType;
    }

    public int getTypeKind() {
        syncType();
        return typeKind;
    }

    public long instanceVarAccessFlags() {
        return flags_field & JavafxFlags.JavafxAllInstanceVarFlags;
    }

    public boolean useAccessors() {
        return isFXMember() &&
                (instanceVarAccessFlags() != JavafxFlags.SCRIPT_PRIVATE ||
                (flags_field & JavafxFlags.VARUSE_NEED_ACCESSOR) != 0 ||
                (owner.flags_field & JavafxFlags.MIXIN) != 0);
    }

    public boolean useGetters() {
        return useAccessors() || (flags_field & JavafxFlags.VARUSE_NON_LITERAL) != 0;
    }
}
