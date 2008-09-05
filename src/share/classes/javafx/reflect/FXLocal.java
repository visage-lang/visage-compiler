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
import com.sun.javafx.functions.*;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;
import com.sun.javafx.runtime.annotation.SourceName;
import com.sun.tools.javafx.util.NotImplementedException;
import com.sun.javafx.runtime.FXObject;

/**
 * Implement JavaFX rfeflection on top of {@java.lang.reflect}.
 * Hence, this implementation can only reflect/mirror values and classes
 * in the same VM that is doing the reflection.
 *
 * @author Per Bothner
 */
public class FXLocal {
    public static Context getContext() { return Context.instance; }

    /** Implementation of {@link FXContext} using Java reflection.
     * Can only access objects and types in the current JVM.
     * Normally, this is a singleton, though in the future there might
     * be variants with different class search paths (similar to
     * {@code com.sun.jdi.PathSearchingVirtualMachine}).
     */

    public static class Context extends FXContext {
        static Context instance = new Context();

        private Context () {
        }

        /** Get the default instance. */
        public static Context getInstance() { return instance; }

        /** Create a reference to a given Object. */
        public FXObjectValue mirrorOf(Object obj) {
            return new ObjectValue(obj, this);
        }

        public FXValue mirrorOf(final Object val, final FXType type) {
            if (type instanceof FXClassType)
                return new FXLocal.ObjectValue(val, (FXClassType) type);
            else if (type instanceof FXPrimitiveType) {
                if (type == FXPrimitiveType.integerType)
                    return mirrorOf(((Integer) val).intValue());
                else // if (type == FXPrimitiveType.numberType)
                    return mirrorOf(((Double) val).doubleValue());
            }
            else if (type instanceof FXSequenceType && val instanceof Sequence) {
                Sequence seq = (Sequence) val;
                return new SequenceValue(seq, (FXSequenceType) type, this);
            }
            else if (type instanceof FXFunctionType && val instanceof Function) {
                final FXFunctionType ftype = (FXFunctionType) type;
                return new FunctionValue((Function) val, ftype, this);
            } else {
                return new MiscValue(val, type);
            }
        }

        public ObjectValue mirrorOf(String val) {
          return new ObjectValue(val, this);
        }

        public FXIntegerValue mirrorOf (int value) {
            return new FXIntegerValue(value, getIntegerType());
        }

        public FXNumberValue mirrorOf (double value) {
            return new FXNumberValue(value, getNumberType());
        }

        /** Get the {@code FXClassType} for the class with the given name. */
        public FXClassType findClass(String cname) {
            ClassLoader loader;
            try {
                loader = Thread.currentThread().getContextClassLoader();
            }
            catch (java.lang.SecurityException ex) {
               loader = getClass().getClassLoader();
            }
            return findClass(cname, loader);
        }

        /** Get the {@code FXClassType} for the class with the given name. */
        public FXClassType findClass(String cname, ClassLoader loader) {
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

        public FXType makeTypeRef(Type typ) {
            if (typ instanceof ParameterizedType) {
                ParameterizedType ptyp = (ParameterizedType) typ;
                Type raw = ptyp.getRawType();
                Type[] targs = ptyp.getActualTypeArguments();
                if (raw instanceof Class) {
                    String rawName = ((Class) raw).getName();
                    if (FXClassType.SEQUENCE_CLASSNAME.equals(rawName) &&
                        targs.length == 1) {
                        return new FXSequenceType(makeTypeRef(targs[0]));
                    }
                    if (FXClassType.OBJECT_VARIABLE_CLASSNAME.equals(rawName) &&
                            targs.length == 1) {
                        return makeTypeRef(targs[0]);
                    }
                    if (FXClassType.SEQUENCE_VARIABLE_CLASSNAME.equals(rawName) &&
                        targs.length == 1) {
                        return new FXSequenceType(makeTypeRef(targs[0]));
                    }
                    if (rawName.startsWith(FXClassType.FUNCTION_CLASSNAME_PREFIX)) {
                        FXType[] prtypes = new FXType[targs.length-1];
                        for (int i = prtypes.length;  --i >= 0; )
                            prtypes[i] = makeTypeRef(targs[i+1]);
                        return new FXFunctionType(prtypes, makeTypeRef(targs[0]));
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
            if (typ instanceof TypeVariable) {
                // KLUDGE
                typ = Object.class;
            }
        
            Class clas = (Class) typ;
            if (clas.isArray()) {
                FXType elType = makeTypeRef(clas.getComponentType());
                return new FXJavaArrayType(elType);
            }
            String rawName = ((Class) typ).getName();
            if (FXClassType.DOUBLE_VARIABLE_CLASSNAME.equals(rawName)
                    || typ == Double.TYPE)
                return getNumberType();
            if (FXClassType.INT_VARIABLE_CLASSNAME.equals(rawName)
                    || typ == Integer.TYPE)
                return getIntegerType();
            if (typ == Byte.TYPE)
                return FXPrimitiveType.byteType;
            if (typ == Short.TYPE)
                return FXPrimitiveType.shortType;
            if (typ == Long.TYPE)
                return FXPrimitiveType.longType;
            if (typ == Float.TYPE)
                return FXPrimitiveType.floatType;
            if (typ == Character.TYPE)
                return FXPrimitiveType.charType;
            if (typ == Boolean.TYPE)
                return FXPrimitiveType.booleanType;
            if (typ == Void.TYPE)
                return FXPrimitiveType.voidType;

            return makeClassRef((Class) typ);
        }

        /** Create a reference to a given Class. */
        public FXClassType makeClassRef(Class cls) {
            int modifiers = 0;
            try {
                String cname = cls.getName();
                Class clsInterface = null;
                if (cname.endsWith(INTERFACE_SUFFIX)) {
                    clsInterface = cls;
                    cname = cname.substring(0, cname.length()-INTERFACE_SUFFIX.length());
                    cls = Class.forName(cname, false, cls.getClassLoader());
                    modifiers = FXClassType.COMPOUND_CLASS|FXClassType.FX_CLASS;
                    return new ClassType(this, modifiers, cls, clsInterface);
                }
                Class[] interfaces = cls.getInterfaces();
                String intfName = cname + INTERFACE_SUFFIX;
                for (int i = 0;  i < interfaces.length;  i++ ) {
                    String iname = interfaces[i].getName();
                    if (iname.equals(FXOBJECT_NAME))
                        modifiers |= FXClassType.FX_CLASS;
                    else if (iname.equals(intfName)) {
                        clsInterface = interfaces[i];
                        modifiers |= FXClassType.COMPOUND_CLASS;
                    }
                }
                return new ClassType(this, modifiers, cls, clsInterface);
            }
            catch (RuntimeException ex) {
                throw ex;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    
        public static Class asClass (FXType type) {
            if (type instanceof FXPrimitiveType)
                return ((FXPrimitiveType) type).clas;
            else if (type instanceof JavaArrayType)
                return ((JavaArrayType) type).getJavaClass();
            else { // FIXME - handle other cases
                ClassType ctyp = (ClassType) type;
                return ctyp.isCompoundClass() ? ctyp.refInterface : ctyp.refClass;
            }
        }

        public FXValue makeSequenceValue(FXValue[] values, int nvalues, FXType elementType) {
            return new SequenceValue(values, nvalues, elementType, this);
        }

        public FXType getIntegerType() {
            return FXPrimitiveType.integerType;
        }

        public FXType getNumberType() {
            return FXPrimitiveType.numberType;
        }
    }

    public static class JavaArrayType extends FXJavaArrayType {
        Class cls;
        JavaArrayType(FXType componentType, Class cls) {
            super(componentType);
            this.cls = cls;
        }

        public Class getJavaClass() { return cls; }
    }

    public static class ClassType extends FXClassType {
        Class refClass;
        Class refInterface;

        public ClassType(Context context, int modifiers,
                Class refClass, Class refInterface) {
            super(context, modifiers);
            this.refClass = refClass;
            this.refInterface = refInterface;
            this.name = refClass.getCanonicalName();
        }

        public Class getJavaImplementationClass() { return refClass; }
        public Class getJavaInterfaceClass() { return refInterface; }

        @Override
        public Context getReflectionContext() {
            return (Context) super.getReflectionContext();
        }

        /** Returns a hash-code.
         * @return the hash-code of the name.
         */
        public int hashCode() {
            return name.hashCode();
        }
    
        public boolean equals (Object obj) {
            return obj instanceof ClassType
                && refClass == ((ClassType) obj).refClass;
        }

        void getSuperClasses(boolean all, SortedClassArray result) {
            boolean isCompound = isCompoundClass();
            Class cls = isCompound ? refInterface : refClass;
            Class[] interfaces = cls.getInterfaces();
            Context context = getReflectionContext();
            if (! isCompound) {
                Class s = cls.getSuperclass();
                if (s != null) {
                    ClassType cl = (ClassType) context.makeClassRef(s);
                    if (result.insert(cl) && all)
                        cl.getSuperClasses(all, result);
                }
            }
            for (int i = 0;  i < interfaces.length;  i++) {
                Class iface = interfaces[i];
                if (iface.getName().equals(Context.FXOBJECT_NAME))
                    continue;
                ClassType cl = (ClassType) context.makeClassRef(iface);
                if (result.insert(cl) && all)
                    cl.getSuperClasses(all, result);
            }
        }

        public List<FXClassType> getSuperClasses(boolean all) {
            SortedClassArray result = new SortedClassArray();
            if (all)
                result.insert(this);
            getSuperClasses(all, result);
            return result;
        }
    
        public FXFunctionMember getFunction(String name, FXType... argType) {
            int nargs = argType.length;
            Class[] ctypes = new Class[nargs];
            for (int i = 0;  i < nargs;  i++) {
                ctypes[i] = Context.asClass(argType[i]);
            }
            try {
                Method meth = (isCompoundClass() ? refInterface : refClass).getMethod(name, ctypes);
                return asFunctionMember(meth, getReflectionContext());
            }
            catch (RuntimeException ex) {
                throw ex;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    
        protected void getFunctions(FXMemberFilter filter, SortedMemberArray<? super FXFunctionMember> result) {
            boolean isCompound = isCompoundClass();
            Class cls = /*isCompound ? refInterface :*/ refClass;
            Context context = getReflectionContext();
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

                if (mname.startsWith(FXClassType.LOCATION_GETTER_PREFIX) ||
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
                FXFunctionMember mref = asFunctionMember(m, context);
                if (filter != null && filter.accept(mref))
                    result.insert(mref);
            }
        }
    
        FXFunctionMember asFunctionMember(Method m, Context context) {
            java.lang.reflect.Type[] ptypes = m.getGenericParameterTypes();
            if (m.isVarArgs()) {
                // ????
            }
            FXType[] prtypes = new FXType[ptypes.length];
            for (int j = 0; j < ptypes.length;  j++)
                prtypes[j] = context.makeTypeRef(ptypes[j]);
            java.lang.reflect.Type gret = m.getGenericReturnType();
            FXFunctionType type = new FXFunctionType(prtypes, context.makeTypeRef(gret));
            return new FXLocal.FunctionMember(m, this, type);
        }
    
        protected void getVariables(FXMemberFilter filter, SortedMemberArray<? super FXVarMember> result) {
            Context context = getReflectionContext();
            Class cls = refClass;
            Class[] noClasses = {};
            String requiredName = filter.getRequiredName();
            // FIXME possible optimization if requiredName != null
            // In that case we could use Class.getDeclaredField(String).
            // However, it's tricky because we need to try all possible renamings.
            java.lang.reflect.Field[] fields = cls.getDeclaredFields();
            for (int i = 0;  i < fields.length;  i++) {
                java.lang.reflect.Field fld = fields[i];
                if (fld.isSynthetic())
                    continue;
                if (fld.getAnnotation(com.sun.javafx.runtime.annotation.Inherited.class) != null)
                    continue;
                String name = fld.getName();
                SourceName sourceName = fld.getAnnotation(SourceName.class);
                if (sourceName != null)
                    name = sourceName.value();
                if (requiredName != null && ! requiredName.equals(name))
                    continue;
                java.lang.reflect.Type gtype = fld.getGenericType();
                FXType tr = context.makeTypeRef(gtype);
                VarMember ref = new VarMember(name, this, tr);
                ref.fld = fld;
                if (isCompoundClass()) {
                    String getLocName = FXClassType.LOCATION_GETTER_PREFIX + name;
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

        public FXObjectValue allocate () {
            Class cls = refClass;
            Context context = getReflectionContext();
            try {
                Object instance;
                if (isJfxType()) {
                    Constructor cons = cls.getDeclaredConstructor(Boolean.TYPE);
                    instance = cons.newInstance(Boolean.TRUE);
                }
                else {
                    instance = cls.newInstance();
                }
                return new ObjectValue(instance, this);
            }
            catch (RuntimeException ex) {
                throw ex;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public FXClassType getDeclaringClass() {
            return null;
        }

        public boolean isStatic() {
            return true;
        }
    }
    
    static class SortedClassArray extends AbstractList<FXClassType> {
        ClassType[] buffer = new ClassType[4];
        int sz;
        public FXClassType get(int index) {
            if (index >= sz)
                throw new IndexOutOfBoundsException();
            return buffer[index];
        }
        public int size() { return sz; }
        // This is basically 'add' under a different non-public name.
        boolean insert(ClassType cl) {
            String clname = cl.getName();
            // We could use binary search, but the lack of a total order
            // for ClassLoaders complicates that.  Linear search should be ok.
            int i = 0;
            for (; i < sz; i++) {
                ClassType c = buffer[i];
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
                ClassType[] tmp = new ClassType[2*sz];
                System.arraycopy(buffer, 0, tmp, 0, sz);
                buffer = tmp;
            }
            System.arraycopy(buffer, i, buffer, i+1, sz-i);
            buffer[i] = cl;
            sz++;
            return true;
        }
    }

    static class VarMember extends FXVarMember {
        Method accessMethod;
        Field fld;
        Method locationGetter;
        FXType type;
        String name;
        FXClassType owner;
    
        public VarMember(String name, ClassType owner, FXType type) {
            this.name = name;
            this.type = type;
            this.owner = owner;
        }

        @Override
        public FXType getType() {
            return type;
        }

        @Override
        public FXValue getValue(FXObjectValue obj) {
            Object robj = obj == null ? null : ((ObjectValue) obj).obj;
            try {
                if (fld != null || locationGetter != null) {
                    Context context =
                        (Context) owner.getReflectionContext();
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
        public void setValue(FXObjectValue obj, FXValue newValue) {
            Object robj = obj == null ? null : ((ObjectValue) obj).obj;
            try {
                if (fld != null || locationGetter != null) {
                    Context context =
                        (Context) owner.getReflectionContext();
                    Object val;
                    if (locationGetter != null)
                        val = locationGetter.invoke(robj, new Object[0]);
                    else
                        val = fld.get(robj);
                    Object newVal = ((Value) newValue).asObject();
                    if (val instanceof ObjectLocation)
                        ((ObjectLocation) val).set(newVal);
                    else {
                        fld.set(robj, newVal);
                    }
                    return;
                }
            }
            catch (RuntimeException ex) {
                throw ex;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void initValue(FXObjectValue obj, FXValue ref) {
            setValue(obj, ref);
        }

        public String getName() {
            return name;
        }

        public FXClassType getDeclaringClass() {
            return owner;
        }

        public boolean isStatic() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    static class FunctionMember extends FXFunctionMember {
        Method method;
        FXClassType owner;
        String name;
        FXFunctionType type;
    
        FunctionMember(Method method, ClassType owner, FXFunctionType type) {
            this.method = method;
            this.owner = owner;
            this.name = method.getName();
            this.type = type;
        }

        public String getName() { return name; }

        public FXClassType getDeclaringClass() { return owner; }
    
        public boolean isStatic() {
            return (method.getModifiers() &  java.lang.reflect.Modifier.STATIC) != 0;
        }

        public FXFunctionType getType() {
            return type;
        }

        Object unwrap(FXValue value) {
            return ((Value) value).asObject();
        }

        /** Invoke this method on the given receiver and arguments. */
        public FXValue invoke(FXObjectValue owner, FXValue... arg) {
            int alen = arg.length;
            Object[] rargs = new Object[alen];
            for (int i = 0;  i < alen;  i++) {
                rargs[i] = unwrap(arg[i]);
            }
            try {
                Object result = method.invoke(unwrap(owner), rargs);
                Context context =
                        (Context) owner.getReflectionContext();
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

    public static interface Value {
        public Object asObject();
    }

    static class MiscValue extends FXValue implements FXLocal.Value {
        Object val;
        FXType type;
        public MiscValue(Object value, FXType type) {
            this.val = value;
            this.type = type;
        }

        public String getValueString() { return val == null ? null : val.toString(); }
        public FXType getType() { return type; }
        public boolean isNull() { return val == null; }
        public Object asObject() { return val; }
    };

    public static class ObjectValue extends FXObjectValue implements FXLocal.Value {
        Object obj;
        FXClassType type;

        public ObjectValue(Object obj, Context context) {
            type = context.makeClassRef(obj.getClass());
            this.obj = obj;
        }

        public ObjectValue(Object obj, FXClassType type) {
           this.type = type;
            this.obj = obj;
        }

        public FXClassType getType() {
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

        public ObjectValue initialize() {
            if (obj instanceof FXObject)
                ((FXObject) obj).initialize$();
            return this;
        }

        public Object asObject() { return obj; }
    }

    static class SequenceValue extends FXSequenceValue implements FXLocal.Value {
        Sequence seq;
        Context context;

        public SequenceValue(FXValue[] values, int nvalues, FXType elementType,
                Context context) {
            super(values, nvalues, elementType);
            this.context = context;
        }

        public SequenceValue(Sequence seq, FXSequenceType sequenceType,
                Context context) {
            super(seq.size(), sequenceType);
            this.seq = seq;
        }

        public FXValue getItem(int index) {
            if (index < 0 || index >= nvalues )
                return null;
            if (values == null)
                values = new FXValue[nvalues];
            if (values[index] == null && seq != null)
                values[index] =
                        context.mirrorOf(seq.get(index), type.getComponentType()); 
            return values[index];
        }
    
        public Sequence asObject() {
            if (seq == null) {
                FXType elementType = type.getComponentType();
                Object[] objs = new Object[nvalues];
                for (int i = 0;  i < nvalues;  i++)
                    objs[i] = ((FXLocal.Value) values[i]).asObject();
                /* FIXME
                if (elementType == PrimitiveTypeRef.integerType)
                    ;
                else if (elementType == PrimitiveTypeRef.numberType)
                    ;
                */
                return Sequences.make(context.asClass(elementType), objs);
            }
            return seq;
        }
    }

    public static class FunctionValue extends FXFunctionValue implements FXLocal.Value {
        Function val;
        FXFunctionType ftype;
        Context context;
        public FunctionValue(Function val, FXFunctionType ftype, Context context) {
            this.val = val;
            this.ftype = ftype;
            this.context = context;
        }

        public FXValue apply(FXValue... arg) {
            Object result;
            int nargs = arg.length;
            Object[] rargs = new Object[nargs];
            for (int i = 0;  i < nargs;  i++)
                rargs[i] = ((FXLocal.Value) arg[i]).asObject();
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
                            rargs[1], rargs[2], rargs[3], rargs[4], rargs[5]);
                    break;
                case 7:
                    result = ((Function7) val).invoke(rargs[0], rargs[1],
                            rargs[2], rargs[3], rargs[4], rargs[5], rargs[6]);
                    break;
                case 8:
                    result = ((Function8) val).invoke(rargs[0], rargs[1],
                            rargs[2], rargs[3], rargs[4],
                            rargs[5], rargs[6], rargs[7]);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            return context.mirrorOf(result, ftype.getReturnType());
        }
        public FXFunctionType getType() {
            return ftype;
        }
        public boolean isNull() { return false; }
        public String getValueString() { return ftype.toString()+"{...}"; };
        public Function asObject() { return val; }
    }
}
