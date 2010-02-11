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

import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.Name;
import static com.sun.tools.mjavac.code.Flags.*;

import static com.sun.tools.javafx.code.JavafxFlags.*;

/**
 * Class to hold and access variable information.
 *
 * @author Robert Field
 */
public class JavafxVarSymbol extends VarSymbol {

    private JavafxTypeRepresentation typeRepresentation;
    private Type elementType = null;
    private final boolean isDotClass;
    private boolean isExternallySeen;
    private int varIndex = -1;

    private Type lastSeenType;
    private final JavafxTypes types;

    /** Construct a variable symbol, given its flags, name, type and owner.
     */
    public JavafxVarSymbol(JavafxTypes types, Name.Table names, long flags, Name name, Type type, Symbol owner) {
        super(flags, name, type, owner);
        this.types = types;
        this.isDotClass = name == names._class;
        this.isExternallySeen = false; 
    }

    private void syncType() {
        if (lastSeenType != type) {
            typeRepresentation = types.typeRep(type);
            switch (typeRepresentation) {
                case TYPE_REPRESENTATION_SEQUENCE:
                    elementType = types.elementType(type);
                    break;
                case TYPE_REPRESENTATION_OBJECT:
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
        return typeRepresentation.isSequence();
    }

    public Type getElementType() {
        syncType();
        return elementType;
    }

    public JavafxTypeRepresentation getTypeRepresentation() {
        syncType();
        return typeRepresentation;
    }

    public long instanceVarAccessFlags() {
        return flags_field & JavafxAllInstanceVarFlags;
    }

    public boolean hasScriptOnlyAccess() {
        long access = instanceVarAccessFlags();
        return access == SCRIPT_PRIVATE || access == PRIVATE;
    }

    public boolean isSpecial() {
        return (flags_field & JavafxFlags.VARUSE_SPECIAL) != 0;
    }
    
    public boolean isBindAccess() {
        return (flags_field & JavafxFlags.VARUSE_BIND_ACCESS) != 0;
    }
    
    public boolean useAccessors() {
        return isFXMember() && !isSpecial() &&
                (!hasScriptOnlyAccess() ||
                (flags_field & VARUSE_NEED_ACCESSOR) != 0 ||
                (isBindAccess() && isAssignedTo()) ||
                (owner.flags_field & MIXIN) != 0);
    }

    public boolean useGetters() {
        return !isSpecial() && (useAccessors() || (flags_field & VARUSE_NON_LITERAL) != 0);
    }

    /** Either has a trigger or a sub-class may have a trigger. */
    public boolean useTrigger() {
        return ! hasScriptOnlyAccess() || (flags_field & VARUSE_HAS_TRIGGER) != 0;
    }

    // Predicate for def (constant) var.
    public boolean isDef() {
        return (flags_field & IS_DEF) != 0;
    }

    public boolean isParameter() {
        return (flags_field & PARAMETER) != 0;
    }

    public void setIsExternallySeen() {
        isExternallySeen = true;
    }

    public boolean isExternallySeen() {
        return isExternallySeen;
    }

    // Predicate for self-reference in init.
    public boolean hasSelfReference() {
        return (flags_field & VARUSE_SELF_REFERENCE) != 0;
    }

    public boolean hasForwardReference() {
        return (flags_field & VARUSE_FORWARD_REFERENCE) != 0;
    }

    public boolean isAssignedTo() {
        return (flags_field & VARUSE_ASSIGNED_TO) != 0;
    }

    public boolean isDefinedBound() {
        //TODO: this bit, it would appear, is not see for shreds
        return (flags_field & VARUSE_BOUND_INIT) != 0;
    }

    public boolean isWritableOutsideScript() {
        return !isDef() && (flags_field & PUBLIC | PROTECTED | PACKAGE_ACCESS) != 0;
    }

    public boolean isMutatedWithinScript() {
        return (flags_field & (VARUSE_ASSIGNED_TO | VARUSE_SELF_REFERENCE | VARUSE_FORWARD_REFERENCE)) != 0L;
    }

    public boolean canChange() {
        return isWritableOutsideScript() || isMutatedWithinScript() || isDefinedBound();
    }

    public boolean isMutatedLocal() {
        return !isMember() && isMutatedWithinScript();
    }

    public int getVarIndex() {
        return varIndex;
    }

    public void setVarIndex(int varIndex) {
        this.varIndex = varIndex;
    }

    public int getAbsoluteIndex(Type site) {
        types.supertypesClosure(site);
        return isLocal() || isStatic() ?
            getVarIndex() :
            getVarIndex() + baseIndex((TypeSymbol)owner, site);
    }
    //where
    private int baseIndex(TypeSymbol tsym, Type site) {
        List<Type> closure = types.supertypesClosure(site, false, true);
        int baseIdx = 0;
        for (Type t : closure) {
            if (types.isSameType(t, tsym.type)) break;
            baseIdx += ((JavafxClassSymbol)t.tsym).getMemberVarCount();
        }
        return baseIdx;
    }
}
