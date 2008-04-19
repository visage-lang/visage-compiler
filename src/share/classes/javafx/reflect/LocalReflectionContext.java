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

/** Implementation of {@link ReflectionContext} using Java reflection.
 * Can only access objects and types in the current JVM.
 * Normally, this is a singleton, though in the future there might
 * be variants with different class search paths (similar to
 * {@code com.sun.jdi.PathSearchingVirtualMachine}).
 */

public class LocalReflectionContext extends ReflectionContext {
    static LocalReflectionContext instance = new LocalReflectionContext();

    private LocalReflectionContext () { 
    }

    /** Get the default instance. */
    public static LocalReflectionContext getInstance() { return instance; }

    /** Create a reference to a given Object. */
    public ObjectRef makeObjectRef(Object obj) {
        return new LocalObjectRef(obj, this);
        //throw new Error();
        //return new LocalObjectRef(this, obj);
    }

    /** Get the {@code ClassRef} for the class with the given name. */
    public ClassRef findClass(String cname) {
        ClassLoader loader;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        }
        catch (java.lang.SecurityException ex) {
           loader = getClass().getClassLoader();
        }
        return findClass(cname, loader);
    }

    /** Get the {@code ClassRef} for the class with the given name. */
    public ClassRef findClass(String cname, ClassLoader loader) {
        String n = cname;
        Exception ex0 = null;
        for (;;) {
            try {
                Class cl = Class.forName(n, false, loader);
                // if (! cl.getCanonicalName().equals(cname)) break;
                return makeClassRef(cl);
            } catch (Exception ex) {
                if (ex0 == null)
                    ex0 = ex;
                int dot = n.lastIndexOf('.');
                if (dot < 0)
                    break;
                n = n.substring(0, dot)+'$'+n.substring(dot+1);
            }
        }
        throw new RuntimeException(ex0);
    }

    /** Create a reference to a given Class. */
    public ClassRef makeClassRef(Class cls) {
        int modifiers = 0;
        try {
            String cname = cls.getName();
            Class clsInterface = null;
            if (cname.endsWith(INTERFACE_SUFFIX)) {
                clsInterface = cls;
                cname = cname.substring(0, cname.length()-INTERFACE_SUFFIX.length());
                cls = Class.forName(cname, false, cls.getClassLoader());
                modifiers = ClassRef.COMPOUND_CLASS|ClassRef.FX_CLASS;
                return new LocalClassRef(this, modifiers, cls, clsInterface);
            }
            Class[] interfaces = cls.getInterfaces();
            String intfName = cname + INTERFACE_SUFFIX;
            for (int i = 0;  i < interfaces.length;  i++ ) {
                String iname = interfaces[i].getName();
                if (iname.equals(FXOBJECT_NAME))
                    modifiers |= ClassRef.FX_CLASS;
                else if (iname.equals(intfName)) {
                    clsInterface = interfaces[i];
                    modifiers |= ClassRef.COMPOUND_CLASS;
                }
            }
            return new LocalClassRef(this, modifiers, cls, clsInterface);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public SequenceBuilder makeSequenceBuilder (TypeRef elementType) {
        return new SequenceBuilder() {
            public void append(ValueRef value) { throw new Error(); }

            public ValueRef getSequence() { throw new Error(); }
        };
    }

    public TypeRef getIntegerType() { throw new Error(); }

    public TypeRef getNumberType() { throw new Error(); }
}

class LocalClassRef extends ClassRef {
    Class refClass;
    Class refInterface;

    public LocalClassRef(LocalReflectionContext context, int modifiers,
            Class refClass, Class refInterface) {
        super(context, modifiers);
        this.refClass = refClass;
        this.refInterface = refInterface;
        this.name = refClass.getCanonicalName();
    }

    @Override
    public LocalReflectionContext getReflectionContect() {
        return (LocalReflectionContext) super.getReflectionContect();
    }

    /** Returns a hash-code.
     * @return the hash-code of the name.
     */
    public int hashCode() {
        return name.hashCode();
    }
    
    public boolean equals (Object obj) {
        return obj instanceof LocalClassRef
            && refClass == ((LocalClassRef) obj).refClass;
    }
    
    static class SortedClassArray extends AbstractList<ClassRef> {
        LocalClassRef[] buffer = new LocalClassRef[4];
        int sz;
        public ClassRef get(int index) {
            if (index >= sz)
                throw new IndexOutOfBoundsException();
            return buffer[index];
        }
        public int size() { return sz; }
        // This is basically 'add' under a different non-public name.
        boolean insert(LocalClassRef cl) {
            String clname = cl.getName();
            // We could use binary search, but the lack of a total order
            // for ClassLoaders complicates that.  Linear search should be ok.
            int i = 0;
            for (; i < sz; i++) {
                LocalClassRef c = buffer[i];
                int cmp = c.getName().compareTo(clname);
                if (cmp > 0)
                    break;
                if (cmp == 0) {
                    if (c.refClass == cl.refClass)
                        return false;
                    // Arbitrary order if same name but different loaders.
                    break;
                }
            }
            if (sz == buffer.length) {
                LocalClassRef[] tmp = new LocalClassRef[2*sz];
                System.arraycopy(buffer, 0, tmp, 0, sz);
                buffer = tmp;
            }
            System.arraycopy(buffer, i, buffer, i+1, sz-i);
            buffer[i] = cl;
            sz++;
            return true;
        }
    }

    void getSuperClasses(boolean all, SortedClassArray result) {
        boolean isCompound = isCompoundClass();
        Class cls = isCompound ? refInterface : refClass;
        Class[] interfaces = cls.getInterfaces();
        LocalReflectionContext context = getReflectionContect();
        if (! isCompound) {
            Class s = cls.getSuperclass();
            if (s != null) {
                LocalClassRef cl = (LocalClassRef) context.makeClassRef(s);
                if (result.insert(cl) && all)
                    cl.getSuperClasses(all, result);
            }
        }
        for (int i = 0;  i < interfaces.length;  i++) {
            Class iface = interfaces[i];
            if (iface.getName().equals(LocalReflectionContext.FXOBJECT_NAME))
                continue;
            LocalClassRef cl = (LocalClassRef) context.makeClassRef(iface);
            if (result.insert(cl) && all)
                cl.getSuperClasses(all, result);
        }
    }

    public List<ClassRef> getSuperClasses(boolean all) {
        SortedClassArray result = new SortedClassArray();
        if (all)
            result.insert(this);
        getSuperClasses(all, result);
        return result;
    }

    public MemberRef getMember(String name, TypeRef type) { throw new Error(); }
    public AttributeRef getAttribute(String name) { throw new Error(); }
    public MethodRef getMethod(String name, TypeRef... argType) { throw new Error(); }
    public ObjectRef allocate () { throw new Error(); }

    public void getMembers(MemberHandler handler, boolean all) { throw new Error(); }
  //public void setAttribute(AttributeRef field, ValueRef value) { throw new Error(); }
  //public void initAttribute(AttributeRef field, ValueRef value) { throw new Error(); }
}

class LocalObjectRef extends ObjectRef {
    Object obj;
    ClassRef type;
    
    public LocalObjectRef(Object obj, LocalReflectionContext context) {
        type = context.makeClassRef(obj.getClass());
        this.obj = obj;
    }
    
    public ClassRef getType() {
        return type;
    }
    
    public boolean isNull() {
        return obj == null;
    }
}
