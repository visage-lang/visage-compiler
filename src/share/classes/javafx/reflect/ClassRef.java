/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
import java.util.*;

/** A run-time representation of a JavaFX class.
 * Corresponds to {@code java.lang.Class}.
 */

public abstract class ClassRef extends TypeRef {
    String name;
    ReflectionContext context;
    protected int modifiers;
    protected static final int COMPOUND_CLASS = 1;
    protected static final int FX_CLASS = 2;

    protected ClassRef(ReflectionContext context, int modifiers) {
        this.context = context;
        this.modifiers = modifiers;
    }

    public String getName() {
        return name;
    }
    
    public String toString() {
        return "class "+getName();
    }

    public boolean equals (ClassRef other) {
        return context.equals(other.context) && name.equals(other.name);
    }

    /** Get list of super-classes.
     * Note we don't distinguish between classes and interfaces.
     * @param all if true include all ancestor classes (including this class).
     * @return the list of super-classes.  It sorted by class name for
     *   convenience and consistency.
     */
    public abstract List<ClassRef> getSuperClasses(boolean all);
    
    public boolean isCompoundClass() {
        return (modifiers & COMPOUND_CLASS) != 0;
    }

    public boolean isJfxType() {
        return (modifiers & FX_CLASS) != 0;
    }

     public boolean isAssignableFrom(ClassRef cls) {
        if (this.equals(cls))
            return true;
        List<ClassRef> supers = cls.getSuperClasses(false);
        for (ClassRef s : supers) {
            if (isAssignableFrom(s))
                return true;
        }
        return false;
    }

    public abstract void getMembers(MemberHandler handler, boolean all);

    public List<MemberRef> getMembers(boolean all) {
        final List<MemberRef> result = new ArrayList<MemberRef>();
        getMembers(new MemberHandler() {
            public boolean handle(MemberRef member) {
                result.add(member);
                return false;
            }
          }, all);
        return result;
    }

    public List<AttributeRef> getAttributes(boolean all) {
        final List<AttributeRef> result = new ArrayList<AttributeRef>();
        getMembers(new MemberHandler() {
            public boolean handle(MemberRef member) {
                if (member instanceof AttributeRef)
                    result.add((AttributeRef) member);
                return false;
            }
          }, all);
        return result;
    }

    public ReflectionContext getReflectionContect() {
        return context;
    }

    /** Return raw uninitialized object. */
    public abstract ObjectRef allocate ();

    /** Create a new initialized object.
     * This is just {@code allocate}+{@code ObjectRef.initialize}.
     */
    public ObjectRef newInstance() {
        return allocate().initialize();
    }

    /** Get a member with the matching name and type. */
    public abstract MemberRef getMember(String name, TypeRef type);

    /** Get the attribute (field) of this class with a given name. */
    public abstract AttributeRef getAttribute(String name);

    /** Find the function that (best) matches the name and argument types. */
    public abstract MethodRef getMethod(String name, TypeRef... argType);

    /* FIXME - move to FieldRef?
    public abstract void setAttribute(AttributeRef field, ValueRef value);
    public abstract void initAttribute(AttributeRef field, ValueRef value);
    public void initAttribute(String field, ValueRef value) {
      initAttribute(getAttribute(field), value);
    }
    //  public void initAttributeBinding(AttributeRef field, LocationRef value);
    */
}
