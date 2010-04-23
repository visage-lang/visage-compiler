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
import java.lang.annotation.Annotation;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.functions.*;
import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;

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
            Object t = PlatformUtils.resolveGeneric(this, typ);
            if (t instanceof FXType)
                return (FXType) t;
        
            Class clas = (Class) t;
            if (clas.isArray()) {
                FXType elType = makeTypeRef(clas.getComponentType());
                return new FXJavaArrayType(elType);
            }
            String rawName = clas.getName();
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

            return makeClassRef(clas);
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
            else if (type instanceof FXSequenceType)
                return Sequence.class;
            else { // FIXME - handle other cases
                ClassType ctyp = (ClassType) type;
                return ctyp.isMixin() ? ctyp.refInterface : ctyp.refClass;
            }
        }

        @Override
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
            this.name = PlatformUtils.getCanonicalName(refClass);
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
        @Override
        public int hashCode() {
            return (name != null ? name : refClass.getName()).hashCode();
        }
    
        @Override
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
            "getDepChain$internal$",
            "getThisRef$internal$",
            "getListenerCount$",
            "getType$",
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
            "setDepChain$internal$",
            "setThisRef$internal$",
            "switchDependence$",
            "userInit$",
            "getFlags$",
            "setFlags$",
            "varChangeBits$",
            "varTestBits$",
            "VCNT$"
        };
        static final String[] SYSTEM_METHOD_PREFIXES = {
            "applyDefaults$",
            "get$",
            "elem$",
            "initVars$",
            "invalidate$",
            "invoke$",
            "onReplace$",
            "seq$",
            "set$",
            "size$",
            "update$",
            "DCNT$",
            "DEP$",
            "FCNT$",
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
                if (PlatformUtils.isSynthetic(m))
                    continue;
                if (PlatformUtils.checkInherited(m) > 0)
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
            java.lang.reflect.Type[] ptypes = PlatformUtils.getGenericParameterTypes(m);
            /*
            if (m.isVarArgs()) {
                // ????
            }
            */
            FXType[] prtypes = new FXType[ptypes.length];
            for (int j = 0; j < ptypes.length;  j++)
                prtypes[j] = context.makeTypeRef(ptypes[j]);
            java.lang.reflect.Type gret = PlatformUtils.getGenericReturnType(m);
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

        static Object[] NO_ARGS = { };
        
        static protected int callMethodIntOrDefault(java.lang.reflect.Method method, int deflt) {
            if (method != null) {
                try {
                    deflt = ((Integer)method.invoke(null, NO_ARGS)).intValue();
                } catch (Throwable ex) {
                }
            }
            return deflt;
        }

        static final String[] SYSTEM_VAR_PREFIXES = {
            "DCNT$",
            "DEP$",
            "FCNT$",
            "VFLG$",
            "VCNT$",
            "VOFF$",
            "MAP$",
            "$script$"
        };

	private void ensureVOffInitialized() {
	    // if no instances of this class have been created, there's no guarantee that VOFF$xxx are initialized,
	    // force initialization.
	    if ((this.modifiers & VOFF_INITIALIZED) == 0) {
		try {
		    refClass.getMethod("VCNT$").invoke(null, NO_ARGS);
		} catch (Throwable e) {
		} 
		this.modifiers |= VOFF_INITIALIZED;
	    }
	}


        protected void getVariables(FXMemberFilter filter, SortedMemberArray<? super FXVarMember> result) {
            VarMember[] varTable = this.variables;
            if (varTable != null) {
                String requiredName = filter.getRequiredName();
                if (requiredName != null) {
                    VarMember v = varTable[searchVariable(varTable, requiredName)];
                    if (v != null)
                        result.insert(v);
                } else {
                    for (int i = varTable.length; --i >= 0; ) {
                        VarMember v = varTable[i];
                        if (v != null && filter.accept(v))
                            result.insert(v);
                    }
                }
                return;
            }
            Context ctxt = getReflectionContext();
            Class cls = refClass;
            java.lang.reflect.Field[] fields;
            ArrayList<VarMember> varList = new ArrayList<VarMember>();
            try {
                fields = cls.getDeclaredFields();
            } catch (SecurityException e) {
                fields = filter(cls.getFields(), cls);
            }
            fieldLoop: for (java.lang.reflect.Field fld : fields) {
                if (PlatformUtils.isSynthetic(fld)) {
                    continue;
                }
                String fname = fld.getName();
                
                String sname = PlatformUtils.getSourceNameFromAnnotation(fld);
                if (sname == null) {
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
                }
               
                FXType tr = ctxt.makeTypeRef(fld.getGenericType());
		
		ensureVOffInitialized();
                int offset;
                try {
                    java.lang.reflect.Field field = cls.getField("VOFF" + fname);
                    offset = field.getInt(null);
                } catch (Throwable ex) {
                    offset = -1;
                }

                VarMember ref = new VarMember(sname, this, tr, offset);
                ref.fld = fld;
                varList.add(ref);

                if (filter != null && filter.accept(ref))
                    result.insert(ref);
            }

            // Enter varList into the varTable hash-table.
            int nvars = varList.size();
            // We want the varTable hash-table to be at most 75% full.
            int varTableSize = 8;
            while (4 * nvars > 3 * varTableSize)
                varTableSize += varTableSize;
            varTable = new VarMember[varTableSize];
            while (--nvars >= 0) {
                VarMember v = varList.get(nvars);
                varTable[searchVariable(varTable, v.name)] = v;
            }
            this.variables = varTable;
        }

        // A simple hash-table keyed vt variable name.
        VarMember[] variables;

        /** A simple hash-table search algorithm.
         * @param variables The hash-table.  Its length must be a power of two,
         *    and there must be at least one unused slot.
         * @param name The name of the VarMember to search for.
         * @return The index of a slot in {@code variables} that matches the name,
         *     or null if there is no such slot.
         */
        static int searchVariable(VarMember[] variables, String name) {
            int hash = name.hashCode();
            int size = variables.length;
            int h = 12347 * hash + hash;
            int mask = size - 1;
            int i = h & mask;
            for (;;) {
                VarMember v = variables[i];
                if (v == null || v.name.equals(name))
                    return i;
                i = (i + 53) & mask;
            }
        }

        public ObjectValue allocate () {
            Class cls = refClass;
            Context context = getReflectionContext();
            try {
                Object instance;
                if (isJfxType()) {
                    // Note that Java5-style varargs are not supported on CDC.
                    Class[] types = { Boolean.TYPE };
                    Constructor cons = cls.getDeclaredConstructor(types);
                    Object[] args = { Boolean.TRUE };
                    instance = cons.newInstance(args);
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

        Annotation getAnnotation (Class clas) {
            Class cls = refClass;
            return cls.getAnnotation(clas);
        }

        public boolean isPublic() {
            int p = PlatformUtils.checkPublic(this);
            if (p >= 0)
                return p > 0;
            Class cls = refClass;
            return (cls.getModifiers() & Modifier.PUBLIC) != 0;
        }

        public boolean isProtected() {
            return PlatformUtils.isProtected(this);
        }

        public boolean isPackage() {
            int p = PlatformUtils.checkPackage(this);
            if (p >= 0)
                return p > 0;
            Class cls = refClass;
            return (cls.getModifiers() & Modifier.PUBLIC) == 0;
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
        Field fld;
        Method getter;
        Method setter;
        FXType type;
        String name;
        ClassType owner;
        int offset;
        static final int GETTER_SETTER_SET = 1;
        static final int ACCESS_FLAGS_SET = 2;
        static final int IS_PUBLIC = 4;
        static final int IS_PROTECTED = IS_PUBLIC << 1;
        static final int IS_PACKAGE = IS_PUBLIC << 2;
        static final int IS_PUBLIC_INIT = IS_PUBLIC << 3;
        static final int IS_PUBLIC_READ = IS_PUBLIC << 4;
        int flags;
    
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

        private void checkGetterSetter() {
            if ((flags & GETTER_SETTER_SET) != 0)
                return;
            Class cls = owner.refInterface;
            if (cls == null)
                cls = owner.refClass;
            String get, set;
            boolean isJfx = owner.isJfxType();
            if (isJfx) {
                get = "get$";
                set = "set$";
            } else {
                get = "get";
                set = "set";
            }
            Method g = ClassType.getMethodOrNull(cls, get + name);
            String xname = name;
            if (g == null && isJfx) {
                xname = cls.getSimpleName() + "$" + name;
                g = ClassType.getMethodOrNull(cls, get + xname);
            }
            getter = g;
            flags |= GETTER_SETTER_SET;
            if (g != null) {
                Class rtype = g.getReturnType();
                setter = ClassType.getMethodOrNull(cls, set + xname, rtype);
            }
        }

        @Override
        public FXValue getValue(FXObjectValue obj) {
            Object robj = obj == null ? null : ((ObjectValue) obj).obj;
            try {
                checkGetterSetter();
                if (fld != null || getter != null) {
                    Context context =
                        (Context) owner.getReflectionContext();
                    Object val;
                    if (getter != null) {
                        val = getter.invoke(robj, new Object[0]);
                    } else {
                        fld.setAccessible(true);
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
                if (type instanceof FXSequenceType && robj instanceof FXObject) {
                    Sequences.set((FXObject)robj, offset,(Sequence)((Value) value).asObject());
                    return;
                }
                checkGetterSetter();
                if (fld != null || setter != null) {
                   
                    if (setter != null) {
                        Object[] args = { ((Value) value).asObject() };
                        setter.invoke(robj, args);
                        return;
                    } else {
                        // FIXME: yet to be implemented for compiled binds
                        if (fld != null) {
                            fld.setAccessible(true);
                            fld.set(robj, ((Value) value).asObject());
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

        private void checkAccessFlags() {
            if ((flags & ACCESS_FLAGS_SET) != 0)
                return;
            flags |= ACCESS_FLAGS_SET;
            checkGetterSetter();
            if (! getDeclaringClass().isJfxType()
                    || ! PlatformUtils.checkAccessAnnotations(this)) {
                int mods = getter != null ? getter.getModifiers()
                        : fld.getModifiers();
                if ((mods & Modifier.PUBLIC) != 0)
                    flags |= IS_PUBLIC;
                if ((mods & Modifier.PROTECTED) != 0)
                    flags |= IS_PROTECTED;
                int mask = Modifier.PUBLIC|Modifier.PROTECTED|Modifier.PRIVATE;
                if ((mods & mask) == 0)
                    flags |= IS_PACKAGE;
             }
        }

        public boolean isStatic() {
            checkGetterSetter();
            int mods = getter != null ? getter.getModifiers()
                    : fld.getModifiers();
            return (mods & Modifier.STATIC) != 0;
        }

        public boolean isPublic() {
            checkAccessFlags();
            return (flags & IS_PUBLIC) != 0;
        }

        public boolean isProtected() {
             checkAccessFlags();
            return (flags & IS_PROTECTED) != 0;
        }

        public boolean isPackage() {
            checkAccessFlags();
            return (flags & IS_PACKAGE) != 0;
        }

        public boolean isPublicInit() {
            checkAccessFlags();
            return (flags & IS_PUBLIC_INIT) != 0;
        }

        public boolean isPublicRead() {
            checkAccessFlags();
            return (flags & IS_PUBLIC_READ) != 0;
        }

        public boolean isDef() {
            int d = PlatformUtils.checkDef(this);
            if (d >= 0)
                return d > 0;
            return fld != null && (fld.getModifiers() & Modifier.FINAL) != 0;
        }

        static class ListenerAdapter extends com.sun.javafx.runtime.FXBase implements FXChangeListenerID {
            final FXChangeListener listener;
            
            ListenerAdapter(FXChangeListener listener) {
                this.listener = listener;
            }
            
            @Override
            public boolean update$(FXObject src, final int depNum, int startPos, int endPos, int newLength, int phase) {
                // varNum does not matter, there is one change listener per <src, varNum> tuple.
                if ((phase & PHASE_TRANS$PHASE) == PHASE$TRIGGER) {
                    this.listener.onChange();
                }
                return true;
            }
        }

        public FXChangeListenerID addChangeListener(FXObjectValue instance, FXChangeListener listener) {
	    if (!this.owner.isAssignableFrom(instance.getType()))
		throw new IllegalArgumentException("not an instance of " + this.owner);
	    // check if instance acually has a variable represented by this
	    FXObject src = (FXObject)((Value)instance).asObject();
	    ListenerAdapter adapter = new ListenerAdapter(listener);
	    src.addDependent$(this.offset, adapter, 0);
            return adapter;
        }
        
        public void removeChangeListener(FXObjectValue instance, FXChangeListenerID id) {
	    if (!this.owner.isAssignableFrom(instance.getType()))
		throw new IllegalArgumentException("not an instance of " + this.owner);
	    FXObject src = (FXObject)((Value)instance).asObject();
	    src.removeDependent$(this.offset, (ListenerAdapter)id);
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
                throw ex;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public boolean isPublic() {
            int p = PlatformUtils.checkPublic(this);
            if (p >= 0)
                return p > 0;
            else
                return (method.getModifiers() & Modifier.PUBLIC) != 0;
        }

        public boolean isProtected() {
            int p = PlatformUtils.checkProtected(this);
            if (p >= 0)
                return p > 0;
            else
                return (method.getModifiers() & Modifier.PROTECTED) != 0;
        }

        public boolean isPackage() {
            int p = PlatformUtils.checkPackage(this);
            if (p >= 0)
                return p > 0;
            int mods = method.getModifiers();
            int mask = Modifier.PUBLIC|Modifier.PROTECTED|Modifier.PRIVATE;
            return (mods & mask) == 0;
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
            type = obj == null ? (ClassType) context.anyType
                    : context.makeClassRef(obj.getClass());
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
                            : type.getReflectionContext().makeClassRef(cls);
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
                    instance.initialize$(true);
                } else {
                    int count = count();
                    
                    instance.initVars$();
                    
                    for (int offset = 0; offset < count; offset++ ) {
                        instance.varChangeBits$(offset, 0, FXObject.VFLGS$INIT$READY);
                        
                        if (initMembers[offset] != null) {
                            initMembers[offset].setValue(this, initValues[offset]);
                        } else {
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
                int flag = attr.getType() instanceof FXSequenceType ? FXObject.VFLGS$INIT_OBJ_LIT_SEQUENCE
                    : FXObject.VFLGS$INIT_OBJ_LIT;
                ((FXObject)obj).setFlags$(offset, flag);
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
            if (nargs > 8) throw new IllegalArgumentException();
            Object[] rargs = nargs > 2 ? new Object[nargs] : null;
            Object arg1 = null, arg2 = null;
            for (int i = 0;  i < nargs;  i++) {
                Object targ = ((FXLocal.Value) arg[i]).asObject();
                if (i == 0)
                    arg1 = targ;
                else if (i == 1)
                    arg2 = targ;
                else
                    rargs[i-2] = targ;
            }
            result = ((Function) val).invoke$(arg1, arg2, rargs);
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
