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
import com.sun.javafx.runtime.annotation.SourceName;
import com.sun.tools.javafx.util.NotImplementedException;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.functions.*;

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
    public ObjectRef mirrorOf(Object obj) {
        return new LocalObjectRef(obj, this);
    }

    public ValueRef mirrorOf(final Object val, final TypeRef type) {
        if (type instanceof ClassRef)
            return new LocalObjectRef(val, (ClassRef) type);
        else if (type instanceof PrimitiveTypeRef) {
            if (type == PrimitiveTypeRef.integerType)
                return mirrorOf(((Integer) val).intValue());
            else // if (type == PrimitiveTypeRef.numberType)
                return mirrorOf(((Double) val).doubleValue());
        }
        else if (type instanceof SequenceTypeRef && val instanceof Sequence) {
            final Sequence seq = (Sequence) val;
            final TypeRef eltype = ((SequenceTypeRef) type).getComponentType();
            return new ValueRef() {
                public String getValueString() { return val == null ? null : val.toString(); }
                public TypeRef getType() { return type; }
                public boolean isNull() { return seq.isEmpty(); }
                public int getItemCount() { return seq.size(); }
                public ValueRef getItem(int index) { return mirrorOf(seq.get(index), eltype); }
            };
        }
        else if (type instanceof FunctionTypeRef && val instanceof Function) {
            final FunctionTypeRef ftype = (FunctionTypeRef) type;
            return new FunctionValueRef() {
                public ValueRef apply(ValueRef... arg) {
                    Object result;
                    int nargs = arg.length;
                    Object[] rargs = new Object[nargs];
                    for (int i = 0;  i < nargs;  i++)
                        rargs[i] = ((LocalValueRef) arg[i]).asObject();
                    switch (nargs) {
                        case 0:
                            result = ((Function0) val).invoke();
                            break;
                        case 1:
                            result = ((Function1) val).invoke(rargs[0]);
                            break;
                        case 2:
                            result = ((Function2) val).invoke(rargs[0], rargs[1]);
                            break;
                        case 3:
                            result = ((Function3) val).invoke(rargs[0],
                                    rargs[1], rargs[2]);
                            break;
                        case 4:
                            result = ((Function4) val).invoke(rargs[0],
                                    rargs[1], rargs[2], rargs[3]);
                            break;
                        case 5:
                            result = ((Function5) val).invoke(rargs[0],
                                    rargs[1], rargs[2], rargs[3], rargs[4]);
                            break;
                        case 6:
                            result = ((Function6) val).invoke(rargs[0],
                                    rargs[1], rargs[2], rargs[3], rargs[4],
                                    rargs[5]);
                            break;
                        case 7:
                            result = ((Function7) val).invoke(rargs[0],
                                    rargs[1], rargs[2], rargs[3], rargs[4],
                                    rargs[5], rargs[6]);
                            break;
                        case 8:
                            result = ((Function8) val).invoke(rargs[0],
                                    rargs[1], rargs[2], rargs[3], rargs[4],
                                    rargs[5], rargs[6], rargs[7]);
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    return mirrorOf(result, ftype.getReturnType());
                }
                public FunctionTypeRef getType() {
                    return ftype;
                }
                public boolean isNull() { return false; }

                public String getValueString() { return type.toString()+"{...}"; };
            };
        } else {
            return new ValueRef() {
                public String getValueString() { return val == null ? null : val.toString(); }
                public TypeRef getType() { return type; }
                public boolean isNull() { return val == null; }
            };
        }
    }

    public ObjectRef mirrorOf(String val) {
      return new LocalObjectRef(val, this);
    }

    public ValueRef mirrorOf (int value) {
        return new IntegerValue(value, getIntegerType());
    }

    public ValueRef mirrorOf (double value) {
        return new NumberValue(value, getNumberType());
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
        if (ClassRef.DOUBLE_VARIABLE_CLASSNAME.equals(rawName)
                || typ == Double.TYPE)
            return getNumberType();
        if (ClassRef.INT_VARIABLE_CLASSNAME.equals(rawName)
                || typ == Integer.TYPE)
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
        catch (RuntimeException ex) {
            throw ex;
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
        return PrimitiveTypeRef.integerType;
    }

    public TypeRef getNumberType() {
        return PrimitiveTypeRef.numberType;
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
    public LocalReflectionContext getReflectionContext() {
        return (LocalReflectionContext) super.getReflectionContext();
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
        LocalReflectionContext context = getReflectionContext();
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

    public MemberRef getMember(String name, TypeRef type) {
        throw new NotImplementedException();
    }

    public MethodRef getMethod(String name, TypeRef... argType) {
        int nargs = argType.length;
        Class[] ctypes = new Class[nargs];
        for (int i = 0;  i < nargs;  i++) {
            TypeRef typ = argType[i];
            Class cls;
            if (typ == PrimitiveTypeRef.integerType)
                cls = Integer.TYPE;
            else if (typ == PrimitiveTypeRef.numberType)
                cls = Double.TYPE;
            else { // FIXME - handle other cases
                LocalClassRef ctyp = (LocalClassRef) typ;
                cls = ctyp.isCompoundClass() ? ctyp.refInterface : ctyp.refClass;
            }
            ctypes[i] = cls;
        }
        try {
            Method meth = (isCompoundClass() ? refInterface : refClass).getMethod(name, ctypes);
            return asMethodRef(meth, getReflectionContext());
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    protected void getMethods(MemberFilter filter, SortedMemberArray<? super MethodRef> result) {
        boolean isCompound = isCompoundClass();
        Class cls = /*isCompound ? refInterface :*/ refClass;
        LocalReflectionContext context = getReflectionContext();
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

            if (mname.startsWith(ClassRef.LOCATION_GETTER_PREFIX) ||
                    mname.startsWith("applyDefaults$")) {
                continue;
            }
            if (isCompoundClass()) {
                try {
                    m = refInterface.getDeclaredMethod(m.getName(), m.getParameterTypes());
                }
                catch (Exception ex) {
                    // Just ignore ???
                }
            }
            MethodRef mref = asMethodRef(m, context);
            if (filter != null && filter.accept(mref))
                result.insert(mref);
        }
    }
    
    MethodRef asMethodRef(Method m, LocalReflectionContext context) {
        Type[] ptypes = m.getGenericParameterTypes();
        if (m.isVarArgs()) {
            // ????
        }
        FunctionTypeRef type = new FunctionTypeRef();
        TypeRef[] prtypes = new TypeRef[ptypes.length];
        for (int j = 0; j < ptypes.length;  j++)
            prtypes[j] = context.makeTypeRef(ptypes[j]);
        type.argTypes = prtypes;
        Type gret = m.getGenericReturnType();
        type.returnType = context.makeTypeRef(gret);
        return new LocalMethodRef(m, this, type);
    }
    
    protected void getAttributes(MemberFilter filter, SortedMemberArray<? super AttributeRef> result) {
        LocalReflectionContext context = getReflectionContext();
        Class cls = refClass;
        Class[] noClasses = {};
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
            LocalAttributeRef ref = new LocalAttributeRef(name, this, tr);
            ref.fld = fld;
            if (isCompoundClass()) {
                String getLocName = ClassRef.LOCATION_GETTER_PREFIX + name;
                try {
                  Method getter = refInterface.getMethod(getLocName, noClasses);
                  ref.fld = null;
                  ref.locationGetter = getter;
                } catch (NoSuchMethodException ex) {
                    // ??? for now leave 'fld'
                }
            }
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

class LocalObjectRef extends ObjectRef implements LocalValueRef {
    Object obj;
    ClassRef type;

    public LocalObjectRef(Object obj, LocalReflectionContext context) {
        type = context.makeClassRef(obj.getClass());
        this.obj = obj;
    }

    public LocalObjectRef(Object obj, ClassRef type) {
        this.type = type;
        this.obj = obj;
    }

    public ClassRef getType() {
        return type;
    }

    public boolean isNull() {
        return obj == null;
    }

    public String getValueString() {
        if (obj == null)
            return null;
        else
            return obj.toString();
    }

    public Object asObject() { return obj; }
}


class LocalAttributeRef extends AttributeRef {
    Method accessMethod;
    Field fld;
    Method locationGetter;
    TypeRef type;
    String name;
    ClassRef owner;
    
    public LocalAttributeRef(String name, LocalClassRef owner, TypeRef type) {
        this.name = name;
        this.type = type;
        this.owner = owner;
    }

    @Override
    public TypeRef getType() {
        return type;
    }

    @Override
    public ValueRef getValue(ObjectRef obj) {
        Object robj = obj == null ? null : ((LocalObjectRef) obj).obj;
        try {
            if (fld != null || locationGetter != null) {
                LocalReflectionContext context =
                    (LocalReflectionContext) owner.getReflectionContext();
                Object val;
                if (locationGetter != null)
                    val = locationGetter.invoke(robj, new Object[0]);
                else
                    val = fld.get(robj);
                if (val instanceof ObjectLocation)
                    val = ((ObjectLocation) val).get();
                return context.mirrorOf(val, type);
            }
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        throw new UnsupportedOperationException("Not supported yet - "+type+"["+type.getClass().getName()+"]");
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

    Object unwrap(ValueRef value) {
        return ((LocalValueRef) value).asObject();
    }

    /** Invoke this method on the given receiver and arguments. */
    public ValueRef invoke(ObjectRef owner, ValueRef... arg) {
        int alen = arg.length;
        Object[] rargs = new Object[alen];
        for (int i = 0;  i < alen;  i++) {
            rargs[i] = unwrap(arg[i]);
        }
        try {
            Object result = method.invoke(unwrap(owner), rargs);
            LocalReflectionContext context =
                    (LocalReflectionContext) owner.getReflectionContext();
            return context.mirrorOf(result, getType().getReturnType());
        }
         catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
