/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
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
import java.util.*;

/** A run-time representation of a JavaFX class.
 * Corresponds to {@code java.lang.Class}.
 */

public abstract class ClassRef extends TypeRef implements MemberRef {
    String name;
    ReflectionContext context;
    protected int modifiers;
    protected static final int COMPOUND_CLASS = 1;
    protected static final int FX_CLASS = 2;

    public static final String SEQUENCE_CLASSNAME =
            "com.sun.javafx.runtime.sequence.Sequence";
    public static final String OBJECT_VARIABLE_CLASSNAME =
            "com.sun.javafx.runtime.location.ObjectVariable";
    public static final String SEQUENCE_VARIABLE_CLASSNAME =
            "com.sun.javafx.runtime.location.SequenceVariable";
    public static final String DOUBLE_VARIABLE_CLASSNAME =
            "com.sun.javafx.runtime.location.DoubleVariable";
    public static final String INT_VARIABLE_CLASSNAME =
            "com.sun.javafx.runtime.location.IntVariable";
    public static final String FUNCTION_CLASSNAME_PREFIX =
            "com.sun.javafx.functions.Function";
    public static final String LOCATION_GETTER_PREFIX = "get$";

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

    public List<MemberRef> getMembers(MemberFilter filter, boolean all) {
        SortedMemberArray<MemberRef> result = new SortedMemberArray<MemberRef>();
        if (all) {
            List<ClassRef> supers = getSuperClasses(all);
            for (ClassRef cl : supers)
                cl.getMembers(filter, result);
        }
        else
            getMembers(filter, result);
        return result;
    }
    public List<MemberRef> getMembers(boolean all) {
        return getMembers(new MemberFilter(), all);
    }
    protected void getMembers(MemberFilter filter, SortedMemberArray<MemberRef> result) {
        getAttributes(filter, result);
        getMethods(filter, result);
    }
    
    public List<MethodRef> getMethods(MemberFilter filter, boolean all) {
        SortedMemberArray<MethodRef> result = new SortedMemberArray<MethodRef>();
        if (all) {
            List<ClassRef> supers = getSuperClasses(all);
            for (ClassRef cl : supers)
                cl.getMethods(filter, result);
        }
        else
            getMethods(filter, result);
        return result;
    }
    public List<MethodRef> getMethods(boolean all) {
        return getMethods(MemberFilter.acceptMethods(), all);
    }
    protected abstract void getMethods(MemberFilter filter, SortedMemberArray<? super MethodRef> result);
    
    public List<AttributeRef> getAttributes(MemberFilter filter, boolean all) {
        SortedMemberArray<AttributeRef> result = new SortedMemberArray<AttributeRef>();
        if (all) {
            List<ClassRef> supers = getSuperClasses(all);
            for (ClassRef cl : supers)
                cl.getAttributes(filter, result);
        }
        else
            getAttributes(filter, result);
        return result;
    }
    public List<AttributeRef> getAttributes(boolean all) {
        return getAttributes(MemberFilter.acceptAttributes(), all);
    }
    protected abstract void getAttributes(MemberFilter filter, SortedMemberArray<? super AttributeRef> result);

    public ReflectionContext getReflectionContext() {
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
    
    protected static class SortedMemberArray<T extends MemberRef> extends AbstractList<T> {
        MemberRef[] buffer = new MemberRef[4];
        int sz;
        public T get(int index) {
            if (index >= sz)
                throw new IndexOutOfBoundsException();
            return (T) buffer[index];
        }
        public int size() { return sz; }
        // This is basically 'add' under a different non-public name.
        boolean insert(T cl) {
            String clname = cl.getName();
            // We could use binary search, but the lack of a total order
            // for ClassLoaders complicates that.  Linear search should be ok.
            int i = 0;
            for (; i < sz; i++) {
                MemberRef c = buffer[i];
                int cmp = c.getName().compareTo(clname);
                if (cmp > 0)
                    break;
                if (cmp == 0) {
                    // Arbitrary order.  FIXME
                    break;
                }
            }
            if (sz == buffer.length) {
                MemberRef[] tmp = new MemberRef[2*sz];
                System.arraycopy(buffer, 0, tmp, 0, sz);
                buffer = tmp;
            }
            System.arraycopy(buffer, i, buffer, i+1, sz-i);
            buffer[i] = cl;
            sz++;
            return true;
        }
    }
}
