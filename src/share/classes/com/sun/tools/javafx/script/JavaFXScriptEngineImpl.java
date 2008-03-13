/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.script;

import com.sun.javafx.api.JavaFXScriptEngine;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javafx.comp.JavafxDefs;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.script.*;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceConstant;

/**
 * This is script engine for the JavaFX Script language, based on
 * the https://scripting.dev.java.net Java language script engine by
 * A. Sundararajan.
 */
public class JavaFXScriptEngineImpl extends AbstractScriptEngine
        implements JavaFXScriptEngine {

    private static final String SCRIPT_CONTEXT_NAME = "javafx$ctx";
    
    // Java compiler
    private JavaFXScriptCompiler compiler;

    // Scripts parsed by this engine
    private LinkedList<Class> scripts = new LinkedList<Class>();

    public JavaFXScriptEngineImpl() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            parentClassLoader = cl;
        }
        compiler = new JavaFXScriptCompiler(parentClassLoader);
    }

    // my factory, may be null
    private ScriptEngineFactory factory;

    private DiagnosticCollector<JavaFileObject> diagnostics;
    
    private ClassLoader parentClassLoader = getClass().getClassLoader();

    public List<Diagnostic<? extends JavaFileObject>> getLastDiagnostics() {
        return diagnostics == null ? null : diagnostics.getDiagnostics();
    }

    // my implementation for CompiledScript
    private class JavafxScriptCompiledScript extends CompiledScript {
        private Class clazz;

        JavafxScriptCompiledScript(Class clazz) {
            this.clazz = clazz;
        }

        public ScriptEngine getEngine() {
            return JavaFXScriptEngineImpl.this;
        }

        public Object eval(ScriptContext ctx) throws ScriptException {
            return evalClass(clazz, ctx);
        }

        public String getName() {
            return clazz.getName();
        }
    }

    public CompiledScript compile(String script) throws ScriptException {
        return compile(script, null);
    }

    public CompiledScript compile(Reader reader) throws ScriptException {
        return compile(readFully(reader));
    }

    public CompiledScript compile(String script, DiagnosticListener<JavaFileObject> listener)
            throws ScriptException {
        Class clazz = parse(script, context, listener, true);
        scripts.addFirst(clazz);  // most recent scripts get referenced first
        return new JavafxScriptCompiledScript(clazz);
    }

    public CompiledScript compile(Reader script, DiagnosticListener<JavaFileObject> listener)
            throws ScriptException {
        return compile(readFully(script), listener);
    }
    
    public Object eval(String script, DiagnosticListener<JavaFileObject> listener) 
            throws ScriptException {
        return eval(script, getContext(), listener);
    }

    public Object eval(Reader script, DiagnosticListener<JavaFileObject> listener) throws ScriptException {
        return eval(script, getContext(), listener);
    }

    public Object eval(String str, ScriptContext ctx)
            throws ScriptException {
        return eval(str, ctx, null);
    }

    public Object eval(Reader reader, ScriptContext ctx)
            throws ScriptException {
        return eval(readFully(reader), ctx);
    }

    public Object eval(String script, ScriptContext context, DiagnosticListener<JavaFileObject> listener) throws ScriptException {
        Class clazz = parse(script, context, listener, false);
        scripts.addFirst(clazz);  // most recent scripts get referenced first
        return evalClass(clazz, context);
    }

    public Object eval(Reader reader, ScriptContext context, DiagnosticListener<JavaFileObject> listener) throws ScriptException {
        return eval(readFully(reader), context, listener);
    }

    public Object eval(String script, Bindings bindings, DiagnosticListener<JavaFileObject> listener) throws ScriptException {
        ScriptContext ctx = getContext();
        ctx.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        return eval(script, ctx, listener);
    }

    public Object eval(Reader reader, Bindings bindings, DiagnosticListener<JavaFileObject> listener) throws ScriptException {
        ScriptContext ctx = getContext();
        ctx.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        return eval(reader, ctx, listener);
    }

    public ScriptEngineFactory getFactory() {
        if (factory == null) {
            factory = new JavaFXScriptEngineFactory();
        }
        return factory;
    }

    public Bindings createBindings() {
        return new SimpleBindings();
    }

    void setFactory(ScriptEngineFactory factory) {
        this.factory = factory;
    }

    // Internals only below this point

    private Class parse(String str, ScriptContext ctx, 
            final DiagnosticListener<JavaFileObject> listener,
            boolean inferBindings) throws ScriptException {
        String fileName = getFileName(ctx);
        String sourcePath = getSourcePath(ctx);
        String classPath = getClassPath(ctx);
        String script = str;
        diagnostics = new DiagnosticCollector<JavaFileObject>();
        DiagnosticListener<JavaFileObject> 
            diagnosticListener = new DiagnosticListener<JavaFileObject>() {
            public void report(Diagnostic<? extends JavaFileObject> rep) {
                if (listener != null)
                    listener.report(rep);
                diagnostics.report(rep);
            }
        };
        DiagnosticCollector<JavaFileObject> firstCollector = 
                new DiagnosticCollector<JavaFileObject>();

        Map<String, byte[]> classBytes = compiler.compile(fileName, script,
                ctx.getErrorWriter(), sourcePath, classPath, firstCollector, false);

        if (firstCollector.getDiagnostics().size() > 0) {
            // check for unresolved variables, add to context, and retry
            boolean recompile = false;
            Set<String> attrs = new HashSet<String>();
            for (Diagnostic d : firstCollector.getDiagnostics()) {
                if (d.getCode().equals("compiler.err.cant.resolve.location")) {
                    Object[] args = ((JCDiagnostic) d).getArgs();
                    if (args.length >= 2 && args[1] instanceof String) {
                        attrs.add((String) args[1]);
                        recompile = true;
                    }
                }
            }
            if (recompile) {
                String binding = makeBindingStatement(ctx, attrs, inferBindings);
                script = binding + str;
                classBytes = compiler.compile(fileName, script,
                        ctx.getErrorWriter(), sourcePath, classPath,
                        diagnosticListener, true);
            } else {
                for (Diagnostic<? extends JavaFileObject> d : firstCollector.getDiagnostics())
                    diagnosticListener.report(d);
                JavaFXScriptCompiler.printDiagnostics(diagnostics, ctx.getErrorWriter());
            }
        }

        if (classBytes == null) {
            throw new ScriptException("compilation failed");
        }

        // create a ClassLoader to load classes from MemoryJavaFileManager
        MemoryClassLoader loader = new MemoryClassLoader(classBytes, classPath,
                parentClassLoader);

        Iterable<Class> classes;
        try {
            classes = loader.loadAll();
        } catch (ClassNotFoundException exp) {
            throw new ScriptException(exp);
        }

        // search for class with main method
        Class c = findMainClass(classes);
        if (c == null) {
            throw new ScriptException("no main class found");
        }
        return c;
    }

    private static Class findMainClass(Iterable<Class> classes) {
        // find a public class with public static main method
        for (Class clazz : classes) {
            int modifiers = clazz.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                Method mainMethod = findMainMethod(clazz);
                if (mainMethod != null) {
                    return clazz;
                }
            }
        }

        // okay, try to find package private class that
        // has public static main method
        for (Class clazz : classes) {
            Method mainMethod = findMainMethod(clazz);
            if (mainMethod != null) {
                return clazz;
            }
        }

        // no main class found!
        return null;
    }

    // find public static void main(String[]) method, if any
    private static Method findMainMethod(Class clazz) {
        // TODO: JFXC-916 Replace findMainMethod with simpler implemenation (below)
        Method mainMethod = null;
        try {
            mainMethod = clazz.getMethod(JavafxDefs.runMethodString, Sequence.class);
        } catch (NoSuchMethodException nsme) {
            try {
                mainMethod = clazz.getMethod(JavafxDefs.runMethodString, SequenceLocation.class);
            } catch (NoSuchMethodException nsme2) {
                return null;
            }
        }
        int modifiers = mainMethod.getModifiers();
        if (Modifier.isPublic(modifiers) &&
                Modifier.isStatic(modifiers)) {
            return mainMethod;
        }
        return null;

//        try {
//            Method mainMethod = clazz.getMethod(JavafxDefs.runMethodString, new Class[0]);
//            int modifiers = mainMethod.getModifiers();
//            if (Modifier.isPublic(modifiers) &&
//                    Modifier.isStatic(modifiers)) {
//                return mainMethod;
//            }
//        } catch (NoSuchMethodException nsme) {
//        }
//        return null;
    }
    
    // TODO: JFXC-916 Remove this function
    private static Object getMainArgs(Method mainMethod) {
        if (mainMethod == null || mainMethod.getParameterTypes().length != 1)
            return null;
        
        if (mainMethod.getParameterTypes()[0] == SequenceLocation.class)
            return SequenceConstant.make(Sequences.emptySequence(String.class));
        else
            return Sequences.emptySequence(String.class);
    }

    private static String getFileName(ScriptContext ctx) {
        int scope = ctx.getAttributesScope(ScriptEngine.FILENAME);
        if (scope != -1) {
            return ctx.getAttribute(ScriptEngine.FILENAME, scope).toString();
        } else {
            return "___FX_SCRIPT___.fx";
        }
    }

    private static String getScriptKey(ScriptContext ctx) {
        return "script-context";
    }

    // for certain variables, we look for System properties. This is
    // the prefix used for such System properties
    private static final String SYSPROP_PREFIX = "com.sun.tools.javafx.script.";

    private static final String SOURCEPATH = "sourcepath";
    private static String getSourcePath(ScriptContext ctx) {
        int scope = ctx.getAttributesScope(SOURCEPATH);
        if (scope != -1) {
            return ctx.getAttribute(SOURCEPATH).toString();
        } else {
            // look for "com.sun.tools.javafx.script.sourcepath"
            return System.getProperty(SYSPROP_PREFIX + SOURCEPATH);
        }
    }

    private static final String CLASSPATH = "classpath";
    private static String getClassPath(ScriptContext ctx) {
        int scope = ctx.getAttributesScope(CLASSPATH);
        if (scope != -1) {
            return ctx.getAttribute(CLASSPATH).toString();
        } else {
            // look for "com.sun.tools.javafx.script.classpath"
            String res = System.getProperty(SYSPROP_PREFIX + CLASSPATH);
            if (res == null) {
                res = System.getProperty("java.class.path");
            }
            return res;
        }
    }

    private static Object evalClass(Class clazz, ScriptContext ctx)
            throws ScriptException {
        // JSR-223 requirement
        ctx.setAttribute("context", ctx, ScriptContext.ENGINE_SCOPE);
        if (clazz == null) {
            return null;
        }
        String scriptKey = getScriptKey(ctx);
        try {
            ScriptContextManager.putContext(scriptKey, ctx);
            Object result = null;

            // find the main method
            Method mainMethod = findMainMethod(clazz);
            if (mainMethod != null) {
                boolean isPublicClazz = Modifier.isPublic(clazz.getModifiers());
                if (!isPublicClazz) {
                    // try to relax access
                    mainMethod.setAccessible(true);
                }

                // call main method
                // TODO: JFXC-916 Remove this call, create empty sequence in place
                Object args = getMainArgs(mainMethod);
                result = mainMethod.invoke(null, args);
            }

            return result;
        } catch (Exception exp) {
            exp.printStackTrace();
            Throwable cause = exp.getCause();
            if (cause != null) {
                System.err.println("Cause:");
                cause.printStackTrace();
            }
            throw new ScriptException(exp);
        } finally {
            ScriptContextManager.removeContext(scriptKey);
        }
    }

    // read a Reader fully and return the content as string
    private String readFully(Reader reader) throws ScriptException {
        char[] arr = new char[8*1024]; // 8K at a time
        StringBuilder buf = new StringBuilder();
        int numChars;
        try {
            while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
                buf.append(arr, 0, numChars);
            }
        } catch (IOException exp) {
            throw new ScriptException(exp);
        }
        return buf.toString();
    }

    private String makeBindingStatement(ScriptContext ctx, Set<String> attrs, boolean inferBindings) {
        // Merge attribute names from all scopes in context into single set.
        if (attrs.isEmpty()) {
            return "";
        }

        // Define ScriptContext variable.
        StringBuilder sb = new StringBuilder();
        sb.append("var ");
        sb.append(SCRIPT_CONTEXT_NAME);
        sb.append(":javax.script.ScriptContext = ");
        sb.append("com.sun.tools.javafx.script.ScriptContextManager.getContext(\"");
        sb.append(getScriptKey(ctx));
        sb.append("\"); ");

        // Define attributes as module variables.
        for (String attr : attrs) {
            Object value = ctx.getAttribute(attr);
            if (value != null || inferBindings) { // value==null when compiling scripts
                sb.append("var ");
                sb.append(attr);
                String type = null;
                if (value != null) {
                    type = value.getClass().getCanonicalName();
                    if (type != null) {
                        sb.append(':');
                        sb.append(type);
                    }
                }
                sb.append(" = ");
                sb.append(SCRIPT_CONTEXT_NAME);
                sb.append(".getAttribute(\"");
                sb.append(attr);
                sb.append("\")");
                if (value != null) {
                    sb.append(" as ");
                    sb.append(type);
                }
                sb.append("; ");
            }
        }

        return sb.toString();
    }

    public Object invokeMethod(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
        if (thiz == null)
            throw new ScriptException("target object not specified");
        if (name == null)
            throw new ScriptException("method name not specified");
        Method method = findMethod(thiz.getClass(), name, args);
        if (method == null)
            throw new ScriptException(new NoSuchMethodException());
        try {
            method.setAccessible(true);
            return method.invoke(thiz, args);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    public Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
        if (name == null)
            throw new ScriptException("method name not specified");
        for (Class script : scripts) {
            Method method = findMethod(script, name, args);
            if (method != null) {
                try {
                    Constructor cons = findDefaultConstructor(script);
                    cons.setAccessible(true);
                    Object instance = cons.newInstance();
                    method.setAccessible(true);
                    return method.invoke(instance, args);
                } catch (Exception e) {
                    throw new ScriptException(e);
                }

            }
        }
        throw new ScriptException(new NoSuchMethodException(name));
    }

    private static Method findMethod(Class clazz, String name, Object[] args) {
        Class[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++)
            argTypes[i] = args[i].getClass();

        try {
            return clazz.getMethod(name, argTypes);
        } catch (NoSuchMethodException e) {
        // fall-through
        }

        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name)) {
                if (m.isVarArgs())
                    return m;
                Class[] parameters = m.getParameterTypes();
                if (args.length != parameters.length)
                    continue;
                if (argsMatch(argTypes, parameters))
                    return m;
                }
            }
        return null;
    }

    private Constructor findDefaultConstructor(Class script) throws NoSuchMethodException {
        Constructor[] cs = script.getDeclaredConstructors();
        for (Constructor c : cs) {
            if (c.getParameterTypes().length == 0) {
                return c;
            }
        }
        throw new NoSuchMethodException("default constructor");
    }

    private static boolean argsMatch(Class[] argTypes, Class[] parameterTypes) {
        for (int i = 0; i < argTypes.length; i++) {
            Class arg = argTypes[i];
            Class param = parameterTypes[i];
            if (param.isPrimitive())
                param = wrappers.get(param);
            if (param == null || !(arg.isAssignableFrom(param)))
                return false;
            }
        return true;
    }

    private static Map<Class,Class> wrappers = new HashMap<Class,Class>();
    static {
        wrappers.put(boolean.class, Boolean.class);
        wrappers.put(byte.class, Byte.class);
        wrappers.put(char.class, Character.class);
        wrappers.put(double.class, Double.class);
        wrappers.put(float.class, Float.class);
        wrappers.put(int.class, Integer.class);
        wrappers.put(long.class, Long.class);
        wrappers.put(short.class, Short.class);
    }

    public <T> T getInterface(Class<T> clazz) {
        return makeInterface(null, clazz);
    }

    public <T> T getInterface(Object thiz, Class<T> clazz) {
        if (thiz == null) {
            throw new IllegalArgumentException("script object is null");
        }
        return makeInterface(thiz, clazz);
    }

    private <T> T makeInterface(Object obj, Class<T> clazz) {
        final Object thiz = obj;
        if (clazz == null || !clazz.isInterface()) {
            throw new IllegalArgumentException("interface Class expected");
        }
        return (T) Proxy.newProxyInstance(
            clazz.getClassLoader(),
            new Class[] { clazz },
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method m, Object[] args)
                            throws Throwable {
                    if (thiz == null)
                            return invokeFunction(m.getName(), args);
                        return invokeMethod(thiz, m.getName(), args);
                    }
                });
    }

    // Workarounds for backward compatibility with old JSR-223 api on mac os

    public Object invoke(String name, Object...args) throws ScriptException, NoSuchMethodException
    {
	return invokeFunction(name, args);
    }

    public Object invoke(Object thiz, String name, Object...args) throws ScriptException, NoSuchMethodException {
	return invokeMethod(thiz, name, args);
    }

}
