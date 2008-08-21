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
import java.lang.reflect.*;
import java.lang.annotation.*;
import com.sun.javafx.runtime.annotation.SourceName;
import com.sun.tools.javafx.util.NotImplementedException;

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

    public TypeRef makeTypeRef(Type typ) {
        if (typ instanceof ParameterizedType) {
            ParameterizedType ptyp = (ParameterizedType) typ;
            Type raw = ptyp.getRawType();
            Type[] targs = ptyp.getActualTypeArguments();
            if (raw instanceof Class) {
                String rawName = ((Class) raw).getName();
                if (ClassRef.SEQUENCE_CLASSNAME.equals(rawName) &&
                    targs.length == 1) {
                    return new SequenceTypeRef(makeTypeRef(targs[0]));
                }
                if (ClassRef.OBJECT_VARIABLE_CLASSNAME.equals(rawName) &&
                    targs.length == 1) {
                    return makeTypeRef(targs[0]);
                }
                if (ClassRef.SEQUENCE_VARIABLE_CLASSNAME.equals(rawName) &&
                    targs.length == 1) {
                    return new SequenceTypeRef(makeTypeRef(targs[0]));
                }
                if (rawName.startsWith(ClassRef.FUNCTION_CLASSNAME_PREFIX)) {
                    FunctionTypeRef type = new FunctionTypeRef();
                    TypeRef[] prtypes = new TypeRef[targs.length-1];
                    for (int i = prtypes.length;  --i >= 0; )
                        prtypes[i] = makeTypeRef(targs[i+1]);
                    type.argTypes = prtypes;
                    type.minArgs = prtypes.length;
                    type.returnType = makeTypeRef(targs[0]);
                    return type;
                }
            }
                               
            typ = raw;
        }
        if (typ instanceof WildcardType) {
            WildcardType wtyp = (WildcardType) typ;
            Type[] upper = wtyp.getUpperBounds();
            Type[] lower = wtyp.getLowerBounds();
            typ = lower.length > 0 ? lower[0] : wtyp.getUpperBounds()[0];
            String rawName = ((Class) typ).getName();
            // Kludge
            if (rawName.equals("java.lang.Integer"))
                return getIntegerType();
            if (rawName.equals("java.lang.Double"))
                return getNumberType();
            return makeTypeRef(typ);
        }
        
        Class clas = (Class) typ;
        String rawName = ((Class) typ).getName();
        if (ClassRef.DOUBLE_VARIABLE_CLASSNAME.equals(rawName))
            return getNumberType();
        if (ClassRef.INT_VARIABLE_CLASSNAME.equals(rawName))
            return getIntegerType();

        return makeClassRef((Class) typ);
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
            public void append(ValueRef value) { throw new NotImplementedException(); }

            public ValueRef getSequence() { throw new NotImplementedException(); }
        };
    }

    public TypeRef getIntegerType() {
        return new PrimitiveTypeRef(Integer.TYPE, "Integer");
    }

    public TypeRef getNumberType() {
        return new PrimitiveTypeRef(Double.TYPE, "Number");
    }
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

    public MemberRef getMember(String name, TypeRef type) { throw new NotImplementedException(); }
    public AttributeRef getAttribute(String name) { throw new NotImplementedException(); }
    public MethodRef getMethod(String name, TypeRef... argType) { throw new NotImplementedException(); }
    
    protected void getMethods(MemberFilter filter, SortedMemberArray<? super MethodRef> result) {
        boolean isCompound = isCompoundClass();
        Class cls = /*isCompound ? refInterface :*/ refClass;
        LocalReflectionContext context = getReflectionContect();
        Method[] methods = cls.getDeclaredMethods();
        for (int i = 0;  i < methods.length;  i++) {
            Method m = methods[i];
            if (m.isSynthetic())
                continue;
            if (m.getAnnotation(com.sun.javafx.runtime.annotation.Inherited.class) != null)
                continue;
            String mname = m.getName();
            if ("userInit$".equals(mname) || "postInit$".equals(mname) ||
                    "addTriggers$".equals(mname) || "initialize$".equals(mname))
                continue;
            if (mname.endsWith("$impl"))
                continue;

            if (mname.startsWith("get$") ||
                    mname.startsWith("applyDefaults$")) {
                continue;
            }
            FunctionTypeRef type = new FunctionTypeRef();
            Type[] ptypes = m.getGenericParameterTypes();
            if (m.isVarArgs()) {
                // ????
            }
            TypeRef[] prtypes = new TypeRef[ptypes.length];
            for (int j = 0; j < ptypes.length;  j++)
                prtypes[j] = context.makeTypeRef(ptypes[j]);
            type.argTypes = prtypes;
            Type gret = m.getGenericReturnType();
            type.returnType = context.makeTypeRef(gret);
            MethodRef mref = new LocalMethodRef(m, this, type);
            if (filter != null && filter.accept(mref))
                result.insert(mref);
        }
    }
    
   protected void getAttributes(MemberFilter filter, SortedMemberArray<? super AttributeRef> result) {
        LocalReflectionContext context = getReflectionContect();
        Class cls = refClass;
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0;  i < fields.length;  i++) {
            Field fld = fields[i];
            if (fld.isSynthetic())
                continue;
            if (fld.getAnnotation(com.sun.javafx.runtime.annotation.Inherited.class) != null)
                continue;
            String name = fld.getName();
            SourceName sourceName = fld.getAnnotation(SourceName.class);
            if (sourceName != null)
                name = sourceName.value();
            Type gtype = fld.getGenericType();
            TypeRef tr = context.makeTypeRef(gtype);
            LocalAttributeRef ref = new LocalAttributeRef(name, this, tr, null);
            if (filter != null && filter.accept(ref))
                result.insert(ref);
        }
    }

    public ObjectRef allocate () { throw new NotImplementedException(); }

    public TypeRef getDeclaringType() {
        return null;
    }

    public boolean isStatic() {
        return true;
    }
  //public void setAttribute(AttributeRef field, ValueRef value) { throw new NotImplementedException(); }
  //public void initAttribute(AttributeRef field, ValueRef value) { throw new NotImplementedException(); }
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


class LocalAttributeRef extends AttributeRef {
    Method accessMethod;
    TypeRef type;
    String name;
    ClassRef owner;
    
    public LocalAttributeRef(String name, LocalClassRef owner, TypeRef type, Method accessMethod) {
        this.name = name;
        this.type = type;
        this.accessMethod = accessMethod;
        this.owner = owner;
    }

    @Override
    public TypeRef getType() {
        return type;
    }

    @Override
    public ValueRef getValue(ObjectRef obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(ObjectRef obj, ValueRef newValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initValue(ObjectRef obj, ValueRef ref) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        return name;
    }

    public TypeRef getDeclaringType() {
        return owner;
    }

    public boolean isStatic() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
class LocalMethodRef extends MethodRef {
    Method method;
    ClassRef owner;
    String name;
    FunctionTypeRef type;
    
    public LocalMethodRef(Method method, LocalClassRef owner, FunctionTypeRef type) {
        this.method = method;
        this.owner = owner;
        this.name = method.getName();
        this.type = type;
    }

    public String getName() { return name; }

    public ClassRef getDeclaringType() { return owner; }
    
    public boolean isStatic() {
        return (method.getModifiers() & Modifier.STATIC) != 0;
    }
    public FunctionTypeRef getType() {
        return type;
    }

    /** Invoke this method on the given receiver and arguments. */
    public ValueRef invoke(ObjectRef owner, ValueRef... arg) {
        throw new UnsupportedOperationException("not implemented: invoke");
    }
}
