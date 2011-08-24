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

package visage.reflect;
import java.util.*;

/** A run-time representation of a Visage class.
 * Corresponds to {@code java.lang.Class}.
 *
 * @author Per Bothner
 * @profile desktop
 */

public abstract class VisageClassType extends VisageType implements VisageMember {
    String name;
    VisageContext context;
    protected int modifiers;
    protected static final int VISAGE_MIXIN = 1;
    protected static final int VISAGE_CLASS = 2;

    public static final String SEQUENCE_CLASSNAME =
            "org.visage.runtime.sequence.Sequence";
    public static final String OBJECT_VARIABLE_CLASSNAME =
            "org.visage.runtime.location.ObjectVariable";
    public static final String SEQUENCE_VARIABLE_CLASSNAME =
            "org.visage.runtime.location.SequenceVariable";
    public static final String FUNCTION_CLASSNAME_PREFIX =
            "org.visage.functions.Function";
    public static final String GETTER_PREFIX = "get$";
    public static final String SETTER_PREFIX = "set$";
    public static final String LOCATION_GETTER_PREFIX = "loc$";

    protected VisageClassType(VisageContext context, int modifiers) {
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

    public boolean equals (VisageClassType other) {
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
    public abstract List<VisageClassType> getSuperClasses(boolean all);
    
    public boolean isMixin() {
        return (modifiers & VISAGE_MIXIN) != 0;
    }

    @Override
    public boolean isVisageType() {
        return (modifiers & VISAGE_CLASS) != 0;
    }

    public boolean isAssignableFrom(VisageClassType cls) {
        if (this.equals(cls))
            return true;
        List<VisageClassType> supers = cls.getSuperClasses(false);
        for (VisageClassType s : supers) {
            if (isAssignableFrom(s))
                return true;
        }
        return false;
    }

    public List<VisageMember> getMembers(VisageMemberFilter filter, boolean all) {
        SortedMemberArray<VisageMember> result = new SortedMemberArray<VisageMember>();
        if (all) {
            List<VisageClassType> supers = getSuperClasses(all);
            for (VisageClassType cl : supers)
                cl.getMembers(filter, result);
        }
        else
            getMembers(filter, result);
        return result;
    }
    public List<VisageMember> getMembers(boolean all) {
        return getMembers(new VisageMemberFilter(), all);
    }
    protected void getMembers(VisageMemberFilter filter, SortedMemberArray<VisageMember> result) {
        getVariables(filter, result);
        getFunctions(filter, result);
    }
    
    public List<VisageFunctionMember> getFunctions(VisageMemberFilter filter, boolean all) {
        SortedMemberArray<VisageFunctionMember> result = new SortedMemberArray<VisageFunctionMember>();
        if (all) {
            List<VisageClassType> supers = getSuperClasses(all);
            for (VisageClassType cl : supers)
                cl.getFunctions(filter, result);
        }
        else
            getFunctions(filter, result);
        return result;
    }
    public List<VisageFunctionMember> getFunctions(boolean all) {
        return getFunctions(VisageMemberFilter.acceptMethods(), all);
    }
    protected abstract void getFunctions(VisageMemberFilter filter, SortedMemberArray<? super VisageFunctionMember> result);
    
    public List<VisageVarMember> getVariables(VisageMemberFilter filter, boolean all) {
        SortedMemberArray<VisageVarMember> result = new SortedMemberArray<VisageVarMember>();
        if (all) {
            List<VisageClassType> supers = getSuperClasses(all);
            boolean isMixin = isMixin();
            for (VisageClassType cl : supers) {
                if (isMixin || !cl.isMixin())
                    cl.getVariables(filter, result);
            }
        }
        else
            getVariables(filter, result);
        return result;
    }
    public List<VisageVarMember> getVariables(boolean all) {
        return getVariables(VisageMemberFilter.acceptAttributes(), all);
    }
    protected abstract void getVariables(VisageMemberFilter filter, SortedMemberArray<? super VisageVarMember> result);

    /** Get a member with the matching name and type - NOT IMPLEMENTED YET.
     * (A method has a FunctionType.)
     * (Unimplemented because it requires type matching.)
     */
    public VisageMember getMember(String name, VisageType type) {
        throw new UnsupportedOperationException("getMember not implemented yet.");
    }

    /** Get the attribute (field) of this class with a given name. */
    public VisageVarMember getVariable(String name) {
        VisageMemberFilter filter = new VisageMemberFilter();
        filter.setAttributesAccepted(true);
        filter.setRequiredName(name);
        List<VisageVarMember> attrs = getVariables(filter, true);
        return attrs.isEmpty() ? null : attrs.get(attrs.size() - 1);
    }

    /** Find the function that (best) matches the name and argument types. */
    public abstract VisageFunctionMember getFunction(String name, VisageType... argType);

    public VisageContext getReflectionContext() {
        return context;
    }

    /** Return raw uninitialized object. */
    public abstract VisageObjectValue allocate ();

    /** Create a new initialized object.
     * This is just {@code allocate}+{@code VisageObjectValue.initialize}.
     */
    public VisageObjectValue newInstance() {
        return allocate().initialize();
    }

    static class SortedMemberArray<T extends VisageMember> extends AbstractList<T> {
        VisageMember[] buffer = new VisageMember[4];
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
                VisageMember c = buffer[i];
                // First compare by name.
                int cmp = c.getName().compareToIgnoreCase(clname);
                if (cmp == 0)
                    cmp = c.getName().compareTo(clname);
                if (cmp > 0)
                    break;
                if (cmp < 0)
                    continue;
                // Next compare by owner. Inherited members go earlier.
                VisageClassType clowner = cl.getDeclaringClass();
                VisageClassType cowner = c.getDeclaringClass();
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
                if (cl instanceof VisageClassType)
                    break;
                if (c instanceof VisageClassType)
                    continue;
                // Sort var after member classes, but before other members.
                if (cl instanceof VisageVarMember)
                    break;
                if (c instanceof VisageVarMember)
                    continue;
                if (cl instanceof VisageFunctionMember && c instanceof VisageFunctionMember) {
                    String scl = ((VisageFunctionMember) cl).getType().toString();
                    String sc = ((VisageFunctionMember) c).getType().toString();
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
                VisageMember[] tmp = new VisageMember[2*sz];
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
