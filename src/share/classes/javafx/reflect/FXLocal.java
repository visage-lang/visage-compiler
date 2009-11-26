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

import java.lang.reflect.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.sun.javafx.functions.*;
import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.annotation.SourceName;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;
import com.sun.javafx.runtime.DependentsManager;

/**
 * Implement JavaFX rfeflection on top of {@java.lang.reflect}.
 * Hence, this implementation can only reflect/mirror values and classes
 * in the same VM that is doing the reflection.
 *
 * @author Per Bothner
 * @profile desktop
 */
public class FXLocal {
    public static Context getContext() { return Context.instance; }

    /** Implementation of {@link FXContext} using Java reflection.
     * Can only access objects and types in the current JVM.
     * Normally, this is a singleton, though in the future there might
     * be variants with different class search paths (similar to
     * {@code com.sun.jdi.PathSearchingVirtualMachine}).
     *
     * @profile desktop
     */

    public static class Context extends FXContext {
        static Context instance = new Context();

        private Context () {
        }

        /** Get the default instance. */
        public static Context getInstance() { return instance; }

        /** Create a reference to a given Object. */
        public ObjectValue mirrorOf(Object obj) {
            return new ObjectValue(obj, this);
        }

        public Value mirrorOf(final Object val, final FXType type) {
            // FIXME Perhaps if val==null we should use MiscValue?
            if (type instanceof ClassType)
                return new FXLocal.ObjectValue(val, (ClassType) type);
            else if (type instanceof FXPrimitiveType) {
                return ((FXPrimitiveType) type).mirrorOf(val);
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

        /** Get the {@code FXClassType} for the class with the given name. */
        public ClassType findClass(String cname) {
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
        public ClassType findClass(String cname, ClassLoader loader) {
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

        static final String LOCATION_PREFIX = "com.sun.javafx.runtime.location.";
        static final int LOCATION_PREFIX_LENGTH = LOCATION_PREFIX.length();
        static final String VARIABLE_STRING = "Variable";
        static final int VARIABLE_STRING_LENGTH = VARIABLE_STRING.length();

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
                        FXType rettype;
                        if (targs[0] == java.lang.Void.class)
                            rettype = FXPrimitiveType.voidType;
                        else
                            rettype = makeTypeRef(targs[0]);
                        return new FXFunctionType(prtypes, rettype);
                    }
                }
                           
                typ = raw;
            }
            if (typ instanceof WildcardType) {
                WildcardType wtyp = (WildcardType) typ;
                Type[] upper = wtyp.getUpperBounds();
                Type[] lower = wtyp.getLowerBounds();
                typ = lower.length > 0 ? lower[0] : wtyp.getUpperBounds()[0];
                if (typ instanceof Class) {
                    String rawName = ((Class) typ).getName();
                    // Kludge, because generics don't handle primitive types.
                    FXType ptype = getPrimitiveType(rawName);
                    if (ptype != null)
                        return ptype;
                }
                return makeTypeRef(typ);
            }
            if (typ instanceof GenericArrayType) {
                FXType elType = makeTypeRef(((GenericArrayType) typ).getGenericComponentType());
                return new FXJavaArrayType(elType);
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
            int rawLength = rawName.length();
            if (rawLength > LOCATION_PREFIX_LENGTH + VARIABLE_STRING_LENGTH &&
                    rawName.startsWith(LOCATION_PREFIX) &&
                    rawName.endsWith(VARIABLE_STRING)) {
                rawName = rawName.substring(LOCATION_PREFIX_LENGTH,
                        rawLength-VARIABLE_STRING_LENGTH);
                
                if (rawName.endsWith("Sequence")) { 
                    int newLength = rawName.length() - "Sequence".length(); 
                    if (newLength != 0) rawName = rawName.substring(0, newLength - 1); 
                    FXType ptype = getPrimitiveType(rawName); 
                    if (ptype != null) 
                        return new FXSequenceType(ptype); 
                } 

                FXType ptype = getPrimitiveType(rawName);
                if (ptype != null)
                    return ptype;
            }
            if (typ == Byte.TYPE)
                return FXPrimitiveType.byteType;
            if (typ == Short.TYPE)
                return FXPrimitiveType.shortType;
            if (typ == Integer.TYPE)
                return FXPrimitiveType.integerType;
            if (typ == Long.TYPE)
                return FXPrimitiveType.longType;
            if (typ == Float.TYPE)
                return FXPrimitiveType.floatType;
            if (typ == Double.TYPE)
                return FXPrimitiveType.doubleType;
            if (typ == Character.TYPE)
                return FXPrimitiveType.charType;
            if (typ == Boolean.TYPE)
                return FXPrimitiveType.booleanType;
            if (typ == Void.TYPE)
                return FXPrimitiveType.voidType;

            return makeClassRef((Class) typ);
        }

        /** Create a reference to a given Class. */
        public ClassType makeClassRef(Class cls) {
            int modifiers = 0;
            try {

                Class[] interfaces = cls.getInterfaces();
                for (int i = 0;  i < interfaces.length;  i++ ) {
                    String iname = interfaces[i].getName();
                    if (iname.equals(FXOBJECT_NAME)) {
                        modifiers |= FXClassType.FX_CLASS;
                    } else if (iname.equals(FXMIXIN_NAME)) {
                        modifiers |= FXClassType.FX_MIXIN;
                    } 
                }
                
                Class clsInterface = null;
                if ((modifiers & FXClassType.FX_MIXIN) != 0) {
                    String cname = cls.getName();
                    
                    if (cname.endsWith(MIXIN_SUFFIX)) {
                        cname = cname.substring(0, cname.length() - MIXIN_SUFFIX.length());
                        clsInterface = cls;
                        cls = Class.forName(cname, false, cls.getClassLoader());
                        if (cls == null) throw new RuntimeException("Missing mixin class " + cname);
                       
                    } else {
                        String intfName = cname + MIXIN_SUFFIX;
                        clsInterface = Class.forName(intfName, false, cls.getClassLoader());
                        if (clsInterface == null) throw new RuntimeException("Missing mixin interface " + intfName);
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
                return ctyp.isMixin() ? ctyp.refInterface : ctyp.refClass;
            }
        }

        public Value makeSequenceValue(FXValue[] values, int nvalues, FXType elementType) {
            return new SequenceValue(values, nvalues, elementType, this);
        }
    }

    static class JavaArrayType extends FXJavaArrayType {
        Class cls;
        JavaArrayType(FXType componentType, Class cls) {
            super(componentType);
            this.cls = cls;
        }

        public Class getJavaClass() { return cls; }
    }

    /** A mirror of a {@code Class} in the current JVM.
     * @profile desktop
     */
    public static class ClassType extends FXClassType {
        Class refClass;
        Class refInterface;
	protected static int VOFF_INITIALIZED = 1 << 16; // high to avoid collisions with masks added in parent class.

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
            return (name != null ? name : refClass.getName()).hashCode();
        }
    
        public boolean equals (Object obj) {
            return obj instanceof ClassType
                && refClass == ((ClassType) obj).refClass;
        }

        void getSuperClasses(boolean all, SortedClassArray result) {
            boolean isMixin = this.isMixin();
            Class cls = isMixin ? refInterface : refClass;
            Class[] interfaces = cls.getInterfaces();
            Context context = getReflectionContext();
            if (! isMixin) {
                Class s = cls.getSuperclass();
                if (s != null) {
                    ClassType cl = (ClassType) context.makeClassRef(s);
                    if (result.insert(cl) && all)
                        cl.getSuperClasses(all, result);
                }
            }
            for (int i = 0;  i < interfaces.length;  i++) {
                Class iface = interfaces[i];
                String iname = iface.getName();
                if (iname.equals(Context.FXOBJECT_NAME) || iname.equals(Context.FXMIXIN_NAME))
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
                Method meth;
                try {
                    meth = refClass.getMethod(name, ctypes);
                } catch (NoSuchMethodException ex) {
                    if (isMixin())
                        meth = null;
                    else
                        throw ex;
                }
                if (isMixin())
                    if (meth == null ||
                            (meth.getModifiers() &  Modifier.STATIC) == 0) {
                    meth = refInterface.getMethod(name, ctypes);
                }
                return asFunctionMember(meth, getReflectionContext());
            }
            catch (RuntimeException ex) {
                throw ex;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    
        private Method[] filter(Method[] methods, Class declaringClass) {
            List<Method> result = new ArrayList<Method>();
            for (Method m : methods) {
                if (m.getDeclaringClass() == declaringClass) {
                    result.add(m);
                }
            }
            return result.toArray(new Method[0]);
        }
        
        static final String[] SYSTEM_METHOD_EXCLUDES = {
            // keep in alphabetical order 
            "addDependent$",
            "allocateVarBits$",
            "complete$",
            "count$",
            "getAsBoolean$",
            "getAsByte$",
            "getAsChar$",
            "getAsDouble$",
            "getAsFloat$",
            "getAsInt$",
            "getAsLong$",
            "getAsShort$",
            "getDependentsManager$internal$",
            "getListenerCount$",
            "getType$",
            "getVFLGS$large$internal$",
            "getVFLGS$small$internal$",
            "initFXBase$",
            "initialize$",
            "isInitialized$",
            "javafx$run$",
            "makeInitMap$",
            "notifyDependents$",
            "postInit$",
            "printBits$",
            "printBitsAction$",
            "removeDependent$",
            "restrictSet$",
            "setDependentsManager$internal$",
            "setVFLGS$large$internal$",
            "setVFLGS$small$internal$",
            "switchDependence$",
            "switchBiDiDependence$",
            "userInit$",
            "varChangeBits$",
            "varTestBits$",
            "VCNT$"
        };
        static final String[] SYSTEM_METHOD_PREFIXES = {
            "access$scriptLevel$",
            "applyDefaults$",
            "be$",
            "get$",
            "elem$",
            "initVarBits$",
            "invalidate$",
            "onReplace$",
            "set$",
            "size$",
            "update$",
            "GETMAP$",
            "VOFF$"
        };
        static final String[] SYSTEM_METHOD_SUFFIXES = {
            "$impl"
        };

        protected void getFunctions(FXMemberFilter filter, SortedMemberArray<? super FXFunctionMember> result) {
            Class cls = refClass;
            Context context = getReflectionContext();
            Method[] methods;
            try {
                methods = cls.getDeclaredMethods();
            } catch (SecurityException e) {
                methods = filter(cls.getMethods(), cls);
            }
            skip: for (int i = 0;  i < methods.length;  i++) {
                Method m = methods[i];
                if (m.isSynthetic())
                    continue;
                if (m.getAnnotation(com.sun.javafx.runtime.annotation.Inherited.class) != null)
                    continue;
                String mname = m.getName();
                    
                for (String exclude : SYSTEM_METHOD_EXCLUDES) {
                    if (mname.equals(exclude))
                        continue skip;
                }
                for (String prefix : SYSTEM_METHOD_PREFIXES) {
                    if (mname.startsWith(prefix))
                        continue skip;
                }
                for (String suffix : SYSTEM_METHOD_SUFFIXES) {
                    if (mname.endsWith(suffix))
                        continue skip;
                }

                if (isMixin()) {
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
    
        private Field[] filter(Field[] fields, Class declaringClass) {
            List<Field> result = new ArrayList<Field>();
            for (Field f : fields) {
                if (f.getDeclaringClass() == declaringClass) {
                    result.add(f);
                }
            }
            return result.toArray(new Field[0]);
        }

        static protected java.lang.reflect.Method getMethodOrNull(Class cls, String name, Class... types) {
            java.lang.reflect.Method method = null;
            try {
                method = cls.getMethod(name, types);
            } catch (Throwable ex) {
            }
            return method;
        }

        static protected java.lang.reflect.Field getFieldOrNull(Class cls, String name) {
            java.lang.reflect.Field field = null;
            try {
                field = cls.getField(name);
            } catch (Throwable ex) {
            }
            return field;
        }

        static protected int getFieldIntOrDefault(Class cls, String name, int deflt) {
            try {
                java.lang.reflect.Field field = cls.getField(name);
                deflt = field.getInt(null);
            } catch (Throwable ex) {
            }
            return deflt;
        }
        
        static protected int callMethodIntOrDefault(java.lang.reflect.Method method, int deflt) {
            if (method != null) {
                try {
                    deflt = ((Integer)method.invoke(null)).intValue();
                } catch (Throwable ex) {
                }
            }
            return deflt;
        }

        static final String[] SYSTEM_VAR_PREFIXES = {
            "VFLGS$",
            "VCNT$",
            "VOFF$",
            "MAP$",
            "$scriptLevel$"
        };

	private void ensureVOffInitialized() {
	    // if no instances of this class have been created, there's no guarantee that VOFF$xxx are initialized,
	    // force initialization.
	    if ((this.modifiers & VOFF_INITIALIZED) == 0) {
		try {
		    refClass.getMethod("VCNT$").invoke(null);
		} catch (Throwable e) {
		} 
		this.modifiers |= VOFF_INITIALIZED;
	    }
	}


        protected void getVariables(FXMemberFilter filter, SortedMemberArray<? super FXVarMember> result) {
            Context ctxt = getReflectionContext();
            Class cls = refClass;
            String requiredName = filter.getRequiredName();
            // FIXME possible optimization if requiredName != null
            // In that case we could use Class.getDeclaredField(String).
            // However, it's tricky because we need to try all possible renamings.
            java.lang.reflect.Field[] fields;
            try {
                fields = cls.getDeclaredFields();
            } catch (SecurityException e) {
                fields = filter(cls.getFields(), cls);
            }
            fieldLoop: for (java.lang.reflect.Field fld : fields) {
                if (fld.isSynthetic()) {
                    continue;
                }
                String fname = fld.getName();
                
                SourceName sourceName = fld.getAnnotation(SourceName.class);
                String sname;
                if (sourceName == null) {
                    int dollar = fname.lastIndexOf('$');
                    if (dollar == -1) {
                        sname = fname;
                    } else {
                        for (String prefix : SYSTEM_VAR_PREFIXES) {
                            if (fname.startsWith(prefix)) {
                                continue fieldLoop;
                            }
                        }
                        if (fname.endsWith("$internal$"))
                            continue fieldLoop;
                        sname = fname.substring(dollar + 1);
                    }
                } else {
                    sname = sourceName.value();
                }
               
                if (requiredName != null && !requiredName.equals(sname)) {
                    continue;
                }
                    
                java.lang.reflect.Type gtype = fld.getGenericType();
                FXType tr = ctxt.makeTypeRef(gtype);
		
		ensureVOffInitialized();
                int offset  = getFieldIntOrDefault(cls, "VOFF" + fname, -1);
                VarMember ref = new VarMember(sname, this, tr, offset);
                ref.fld = fld;
                if (!isMixin()) {
                    ref.getter = getMethodOrNull(cls, "get" + fname);
 
                    if (ref.getter != null) {
                        ref.fld = null;
                        Class type = ref.getter.getReturnType();
                        ref.setter = getMethodOrNull(cls, "set" + fname, type);
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
                String cname = c.getName();
                int cmp = cname == clname ? 0
                        : cname == null ? -1 : cname == null ? 1
                        : cname.compareTo(clname);
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
        Method getter;
        Method setter;
        FXType type;
        String name;
        FXClassType owner;
        int offset;
    
        public VarMember(String name, ClassType owner, FXType type, int offset) {
            this.name = name;
            this.type = type;
            this.owner = owner;
            this.offset = offset;
        }

        @Override
        public FXType getType() {
            return type;
        }

        @Override
        public int getOffset() {
            return offset;
        }

        @Override
        public FXValue getValue(FXObjectValue obj) {
            Object robj = obj == null ? null : ((ObjectValue) obj).obj;
            try {
                if (fld != null || getter != null) {
                    Context context =
                        (Context) owner.getReflectionContext();
                    Object val;
                    if (getter != null)
                        val = getter.invoke(robj, new Object[0]);
                    else {
                        val = fld.get(robj);
                    }
                    // FIXME: yet to be implemented for compiled binds
                    return context.mirrorOf(val, type);
                }
            }
            catch (RuntimeException ex) {
                throw ex;
            }
            catch (Exception ex) {
                if (fld != null)
                    throw new RuntimeException("Illegal access of field " + fld);
                else
                    throw new RuntimeException("Illegal access of field getter " + getter);
            }
            throw new UnsupportedOperationException("Not supported yet - "+type+"["+type.getClass().getName()+"]");
        }
        
        public FXLocation getLocation(FXObjectValue obj) {
            return new VarMemberLocation(obj, this);
        }
        
        static final Object[] noObjects = {};

        protected void initVar(FXObjectValue instance, FXValue value) {
            instance.initVar(this, value);
        }

        @Override
        public void initValue(FXObjectValue instance, FXValue value) {
            instance.initVar(this, value);
        }

        @Override
        public void setValue(FXObjectValue obj, FXValue value) {
            Object robj = obj == null ? null : ((ObjectValue) obj).obj;
            try {
                if(type instanceof FXSequenceType && robj instanceof FXObject) {
                    Sequences.set((FXObject)robj, offset,(Sequence)((Value) value).asObject());
                    return;
                }
                if (fld != null || setter != null) {
                   
                    if (setter != null) {
                        setter.invoke(robj, ((Value) value).asObject());
                        return;
                    } else {
                        // FIXME: yet to be implemented for compiled binds
                        if (fld != null) {
                            fld.set(robj, value);
                            return;
                        }
                    }
                }
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getName() {
            return name;
        }

        public FXClassType getDeclaringClass() {
            return owner;
        }

        public boolean isStatic() {
            int mods = getter != null ? getter.getModifiers()
                    : fld.getModifiers();
            return (mods & Modifier.STATIC) != 0;
        }
         
	static class ListenerAdapter extends com.sun.javafx.runtime.FXBase implements FXChangeListenerID {
	    final FXChangeListener listener;
	    ListenerAdapter(FXChangeListener listener) {
		this.listener = listener;
	    }
	    
	    @Override public void update$(FXObject src, final int varNum, int phase) {
		// varNum does not matter, there is one change listener per <src, varNum> tuple.
		if (phase == FXObject.VFLGS$NEEDS_TRIGGER) {
		    this.listener.onChange();
		}
	    }
	}

        public FXChangeListenerID addChangeListener(FXObjectValue instance, FXChangeListener listener) {
	    if (!this.owner.isAssignableFrom(instance.getType()))
		throw new IllegalArgumentException("not an instance of " + this.owner);
	    // check if instance acually has a variable represented by this
	    FXObject src = (FXObject)((Value)instance).asObject();
	    DependentsManager deps = DependentsManager.get(src);
	    ListenerAdapter adapter = new ListenerAdapter(listener);
	    deps.addDependent(src, this.offset, adapter);
            return adapter;
        }
        
        public void removeChangeListener(FXObjectValue instance, FXChangeListenerID id) {
	    if (!this.owner.isAssignableFrom(instance.getType()))
		throw new IllegalArgumentException("not an instance of " + this.owner);
	    FXObject src = (FXObject)((Value)instance).asObject();
	    DependentsManager deps = DependentsManager.get(src);
	    deps.removeDependent(src, this.offset, (ListenerAdapter)id);
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
            return (method.getModifiers() &  Modifier.STATIC) != 0;
        }

        public FXFunctionType getType() {
            return type;
        }

        Object unwrap(FXValue value) {
            if (value == null)
                return null;
            return ((Value) value).asObject();
        }

        /** Invoke this method on the given receiver and arguments. */
        public FXValue invoke(FXObjectValue obj, FXValue... arg) {
            int alen = arg.length;
            Object[] rargs = new Object[alen];
            for (int i = 0;  i < alen;  i++) {
                rargs[i] = unwrap(arg[i]);
            }
            try {
                Object result = method.invoke(unwrap(obj), rargs);
                Context context =
                        (Context) owner.getReflectionContext();
                if (result == null && getType().getReturnType() == FXPrimitiveType.voidType)
                    return null;
                return context.mirrorOf(result, getType().getReturnType());
            }
             catch (RuntimeException ex) {
                 System.err.println("caught "+ex+" method:"+method+" rargs:"+rargs+" alen:"+alen);
                throw ex;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /** A value in the current JVM.
     *
     * @profile desktop
     */
    public static interface Value extends FXValue {
        public abstract Object asObject();
    }

    static class MiscValue implements FXLocal.Value {
        Object val;
        FXType type;
        public MiscValue(Object value, FXType type) {
            this.val = value;
            this.type = type;
        }

        public String getValueString() { return val == null ? "(null)" : val.toString(); }
        public FXType getType() { return type; }
        public boolean isNull() { return val == null; }
        public Object asObject() { return val; }
        public FXValue getItem(int index) { return this; }
        public int getItemCount() { return isNull() ? 0 : 1; }
    };

    /** A mirror of an {@code Object} in the current JVM.
     *
     * @profile desktop
     */
    public static class ObjectValue extends FXObjectValue implements FXLocal.Value {
        // FIXME It might be cleaner to require obj!=null,
        // and instead use MiscValue for null.
        Object obj;
        ClassType type;
        ClassType classType;
        int count;
        FXVarMember[] initMembers;
        FXValue[] initValues;
        

        public ObjectValue(Object obj, Context context) {
            type = context.makeClassRef(obj.getClass());
            this.obj = obj;
            if (obj instanceof FXObject) 
                count = ((FXObject) obj).count$();
        }

        public ObjectValue(Object obj, ClassType type) {
            this.type = type;
            this.obj = obj;
            if (obj instanceof FXObject) 
                count = ((FXObject) obj).count$();
        }

        public FXClassType getType() {
            return type;
        }
        
        public FXClassType getClassType() {
            if (classType == null) {
                if (obj == null)
                    classType = type;
                else {
                    Class cls = obj.getClass();
                    classType = type.getJavaImplementationClass() == cls ? type
                            : type.getReflectionContext().makeClassRef(obj.getClass());
                }
            }
            return classType;
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
            if (obj instanceof FXObject) {
                FXObject instance = (FXObject)obj;
            
                if (initMembers == null) {
                    instance.initialize$();
                } else {
                    int count = count();
                    
                    for (int offset = 0; offset < count; offset++ ) {
                        if (initMembers[offset] != null) {
                            initMembers[offset].setValue(this, initValues[offset]);
                        } else if (instance.varTestBits$(offset, FXObject.VFLGS$IS_INVALID, FXObject.VFLGS$IS_INVALID)) {
                            instance.applyDefaults$(offset);
                        }
                    }
                    
                    instance.complete$();
                }
            }
            return this;
        }

        public Object asObject() { return obj; }
        
        private int count() {
            return obj instanceof FXObject ? ((FXObject) obj).count$() : 0;
        }
        
        public void initVar(FXVarMember attr, FXValue value) {
            int offset = attr.getOffset();
            
            if (offset == -1) {
                attr.setValue(this, value);
            } else {
                if (initMembers == null) {
                    int count = count();
                
                    initMembers = new FXVarMember[count];
                    initValues = new FXValue[count];
                }
                
                initMembers[offset] = attr;
                initValues[offset] = value;
            }
        }
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
            this.context = context;
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
                return Sequences.make(TypeInfo.getTypeInfo(context.asClass(elementType)), objs);
            }
            return seq;
        }
    }

    /** Mirror a {@code Function} value in the current JVM.
     *
     * @profile desktop
     */
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

    /**
     *
     * @profile desktop
     */
    public static class VarMemberLocation extends FXVarMemberLocation {
        VarMember var;

        public VarMemberLocation(FXObjectValue object, VarMember var) {
            super(object, var);
            this.var = var;
        }

        // FIXME: yet to be implemented for compiled binds
        //    public AbstractVariable getAbstractVariable(FXObjectValue obj) {...}
    }
}
