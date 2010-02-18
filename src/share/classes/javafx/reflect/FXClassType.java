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
import java.util.*;

/** A run-time representation of a JavaFX class.
 * Corresponds to {@code java.lang.Class}.
 *
 * @author Per Bothner
 * @profile desktop
 */

public abstract class FXClassType extends FXType implements FXMember {
    String name;
    FXContext context;
    protected int modifiers;
    protected static final int FX_MIXIN = 1;
    protected static final int FX_CLASS = 2;

    public static final String SEQUENCE_CLASSNAME =
            "com.sun.javafx.runtime.sequence.Sequence";
    public static final String OBJECT_VARIABLE_CLASSNAME =
            "com.sun.javafx.runtime.location.ObjectVariable";
    public static final String SEQUENCE_VARIABLE_CLASSNAME =
            "com.sun.javafx.runtime.location.SequenceVariable";
    public static final String FUNCTION_CLASSNAME_PREFIX =
            "com.sun.javafx.functions.Function";
    public static final String GETTER_PREFIX = "get$";
    public static final String SETTER_PREFIX = "set$";
    public static final String LOCATION_GETTER_PREFIX = "loc$";

    protected FXClassType(FXContext context, int modifiers) {
        this.context = context;
        this.modifiers = modifiers;
    }

    public String getName() {
        return name;
    }
    
    public String toString() {
        String n = getName();
        if (n == null)
            n = "<anonymous>";
        return "class "+n;
    }

    public boolean equals (FXClassType other) {
        return context.equals(other.context) && name.equals(other.name);
    }

    public int hashCode() {
        return name.hashCode();
    }

    /** Get list of super-classes.
     * Note we don't distinguish between classes and interfaces.
     * @param all if true include all ancestor classes (including this class).
     * @return the list of super-classes.  It sorted by class name for
     *   convenience and consistency.
     */
    public abstract List<FXClassType> getSuperClasses(boolean all);
    
    public boolean isMixin() {
        return (modifiers & FX_MIXIN) != 0;
    }

    @Override
    public boolean isJfxType() {
        return (modifiers & FX_CLASS) != 0;
    }

    public boolean isAssignableFrom(FXClassType cls) {
        if (this.equals(cls))
            return true;
        List<FXClassType> supers = cls.getSuperClasses(false);
        for (FXClassType s : supers) {
            if (isAssignableFrom(s))
                return true;
        }
        return false;
    }

    public List<FXMember> getMembers(FXMemberFilter filter, boolean all) {
        SortedMemberArray<FXMember> result = new SortedMemberArray<FXMember>();
        if (all) {
            List<FXClassType> supers = getSuperClasses(all);
            for (FXClassType cl : supers)
                cl.getMembers(filter, result);
        }
        else
            getMembers(filter, result);
        return result;
    }
    public List<FXMember> getMembers(boolean all) {
        return getMembers(new FXMemberFilter(), all);
    }
    protected void getMembers(FXMemberFilter filter, SortedMemberArray<FXMember> result) {
        getVariables(filter, result);
        getFunctions(filter, result);
    }
    
    public List<FXFunctionMember> getFunctions(FXMemberFilter filter, boolean all) {
        SortedMemberArray<FXFunctionMember> result = new SortedMemberArray<FXFunctionMember>();
        if (all) {
            List<FXClassType> supers = getSuperClasses(all);
            for (FXClassType cl : supers)
                cl.getFunctions(filter, result);
        }
        else
            getFunctions(filter, result);
        return result;
    }
    public List<FXFunctionMember> getFunctions(boolean all) {
        return getFunctions(FXMemberFilter.acceptMethods(), all);
    }
    protected abstract void getFunctions(FXMemberFilter filter, SortedMemberArray<? super FXFunctionMember> result);
    
    public List<FXVarMember> getVariables(FXMemberFilter filter, boolean all) {
        SortedMemberArray<FXVarMember> result = new SortedMemberArray<FXVarMember>();
        if (all) {
            List<FXClassType> supers = getSuperClasses(all);
            boolean isMixin = isMixin();
            for (FXClassType cl : supers) {
                if (isMixin || !cl.isMixin())
                    cl.getVariables(filter, result);
            }
        }
        else
            getVariables(filter, result);
        return result;
    }
    public List<FXVarMember> getVariables(boolean all) {
        return getVariables(FXMemberFilter.acceptAttributes(), all);
    }
    protected abstract void getVariables(FXMemberFilter filter, SortedMemberArray<? super FXVarMember> result);

    /** Get a member with the matching name and type - NOT IMPLEMENTED YET.
     * (A method has a FunctionType.)
     * (Unimplemented because it requires type matching.)
     */
    public FXMember getMember(String name, FXType type) {
        throw new UnsupportedOperationException("getMember not implemented yet.");
    }

    /** Get the attribute (field) of this class with a given name. */
    public FXVarMember getVariable(String name) {
        FXMemberFilter filter = new FXMemberFilter();
        filter.setAttributesAccepted(true);
        filter.setRequiredName(name);
        List<FXVarMember> attrs = getVariables(filter, true);
        return attrs.isEmpty() ? null : attrs.get(attrs.size() - 1);
    }

    /** Find the function that (best) matches the name and argument types. */
    public abstract FXFunctionMember getFunction(String name, FXType... argType);

    public FXContext getReflectionContext() {
        return context;
    }

    /** Return raw uninitialized object. */
    public abstract FXObjectValue allocate ();

    /** Create a new initialized object.
     * This is just {@code allocate}+{@code FXObjectValue.initialize}.
     */
    public FXObjectValue newInstance() {
        return allocate().initialize();
    }

    static class SortedMemberArray<T extends FXMember> extends AbstractList<T> {
        FXMember[] buffer = new FXMember[4];
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
                FXMember c = buffer[i];
                // First compare by name.
                int cmp = c.getName().compareToIgnoreCase(clname);
                if (cmp == 0)
                    cmp = c.getName().compareTo(clname);
                if (cmp > 0)
                    break;
                if (cmp < 0)
                    continue;
                // Next compare by owner. Inherited members go earlier.
                FXClassType clowner = cl.getDeclaringClass();
                FXClassType cowner = c.getDeclaringClass();
                boolean clAssignableFromC = clowner.isAssignableFrom(cowner);
                boolean cAssignableFromCl = cowner.isAssignableFrom(clowner);
                if (clAssignableFromC && ! cAssignableFromCl)
                    break;
                if (cAssignableFromCl && ! clAssignableFromC)
                    continue;
                // Next compare by owner name.
                String clownerName = clowner.getName();
                String cownerName = cowner.getName();
                cmp = cownerName.compareToIgnoreCase(clownerName);
                if (cmp == 0)
                    cmp = cownerName.compareTo(clownerName);
                if (cmp > 0)
                    break;
                if (cmp < 0)
                    continue;
                // Sort member classes before other members.
                if (cl instanceof FXClassType)
                    break;
                if (c instanceof FXClassType)
                    continue;
                // Sort var after member classes, but before other members.
                if (cl instanceof FXVarMember)
                    break;
                if (c instanceof FXVarMember)
                    continue;
                if (cl instanceof FXFunctionMember && c instanceof FXFunctionMember) {
                    String scl = ((FXFunctionMember) cl).getType().toString();
                    String sc = ((FXFunctionMember) c).getType().toString();
                    cmp = sc.compareToIgnoreCase(scl);
                    if (cmp == 0)
                        cmp = sc.compareTo(scl);
                    if (cmp < 0)
                        continue;
                }
                // Otherwise arbitrary order.
                break;
            }
            if (sz == buffer.length) {
                FXMember[] tmp = new FXMember[2*sz];
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
