/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.reflect;

/** Call-back object for use when iterating over a set of members.
 *
 * @author Per Bothner
 * @profile desktop
 */

public class FXMemberFilter {
    String requiredName;
    static final int ACCEPTING_METHODS = 1;
    static final int ACCEPTING_ATTRIBUTES = 2;
    static final int ACCEPTING_CLASSES = 4;
    int flags = ACCEPTING_METHODS|ACCEPTING_ATTRIBUTES|ACCEPTING_CLASSES;

    /** Are (some) methods accepted? */
    public boolean isAcceptingMethods() {
        return (flags & ACCEPTING_METHODS) != 0;
    }

    /** Are (some) attributes accepted? */
    public boolean isAcceptingAttributes() {
        return (flags & ACCEPTING_ATTRIBUTES) != 0;
    }

    /** Are (some) member classes accepte? */
    public boolean isAcceptingClasses() {
        return (flags & ACCEPTING_CLASSES) != 0;
    }

    public void setMethodsAccepted(boolean accept) {
        if (accept) flags |= ACCEPTING_METHODS;
        else flags &= ~ACCEPTING_METHODS;
    }
    public void setAttributesAccepted(boolean accept) {
        if (accept) flags |= ACCEPTING_ATTRIBUTES;
        else flags &= ~ACCEPTING_ATTRIBUTES;
    }
    public void setClassesAccepted(boolean accept) {
        if (accept) flags |= ACCEPTING_CLASSES;
        else flags &= ~ACCEPTING_CLASSES;
    }
    public String getRequiredName() {
        return requiredName;
    }
    public void setRequiredName(String name) { requiredName = name; }

    public boolean accept(FXMember member) {
        if (member instanceof FXFunctionMember) {
            if (! isAcceptingMethods())
                return false;
        }
        else if (member instanceof FXVarMember) {
            if (! isAcceptingAttributes())
                return false;
        }
        else if (member instanceof FXClassType) {
            if (! isAcceptingClasses())
                return false;
        }
        if (requiredName != null)
            return requiredName.equals(member.getName());
        return true;
    }
    private static FXMemberFilter acceptAttributes = new FXMemberFilter();
    static { acceptAttributes.flags = ACCEPTING_ATTRIBUTES; }
    public static FXMemberFilter acceptAttributes() { return acceptAttributes; }
    public static FXMemberFilter acceptAttributes(String requiredName) {
        FXMemberFilter f = new FXMemberFilter();
        f.flags = ACCEPTING_ATTRIBUTES;
        f.requiredName = requiredName;
        return f;
    }

    private static FXMemberFilter acceptMethods = new FXMemberFilter();
    static { acceptMethods.flags = ACCEPTING_METHODS; }
    public static FXMemberFilter acceptMethods() { return acceptMethods; }
    public static FXMemberFilter acceptMethods(String requiredName) {
        FXMemberFilter f = new FXMemberFilter();
        f.flags = ACCEPTING_METHODS;
        f.requiredName = requiredName;
        return f;
    }
}
