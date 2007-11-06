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

import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javafx.comp.JavafxModuleBuilder;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.script.*;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * This is script engine for the JavaFX Script language, based on
 * the https://scripting.dev.java.net Java language script engine by
 * A. Sundararajan.
 */
public class JavaFXScriptEngine extends AbstractScriptEngine 
        implements Compilable {
    private static final String SCRIPT_CONTEXT_NAME = "javafx$ctx";
    
    // Java compiler
    private JavaFXScriptCompiler compiler;

    public JavaFXScriptEngine() {
        compiler = new JavaFXScriptCompiler();
    }

    // my factory, may be null
    private ScriptEngineFactory factory;

    // my implementation for CompiledScript
    private class JavafxScriptCompiledScript extends CompiledScript {
        private Class clazz;

        JavafxScriptCompiledScript (Class clazz) {
            this.clazz = clazz;
        }

        public ScriptEngine getEngine() {
            return JavaFXScriptEngine.this;
        }

        public Object eval(ScriptContext ctx) throws ScriptException {
            return evalClass(clazz, ctx);
        }
        
        public String getName() {
            return clazz.getName();
        }
    }

    public CompiledScript compile(String script) throws ScriptException {
        Class clazz = parse(script, context); 
        return new JavafxScriptCompiledScript(clazz);
    }

    public CompiledScript compile(Reader reader) throws ScriptException {        
        return compile(readFully(reader));
    }

    public Object eval(String str, ScriptContext ctx) 
                       throws ScriptException {	
        Class clazz = parse(str, ctx);
        return evalClass(clazz, ctx);
    }

    public Object eval(Reader reader, ScriptContext ctx)
                       throws ScriptException {
        return eval(readFully(reader), ctx);
    }

    public ScriptEngineFactory getFactory() {
	synchronized (this) {
	    if (factory == null) {
	    	factory = new JavaFXScriptEngineFactory();
	    }
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

    private Class parse(String str, ScriptContext ctx) throws ScriptException {        
        String fileName = getFileName(ctx);
        String sourcePath = getSourcePath(ctx);
        String classPath = getClassPath(ctx);
        String binding = makeBindingStatement(ctx);
        String script = binding + str;
        DiagnosticCollector<JavaFileObject> diagnostics = 
            new DiagnosticCollector<JavaFileObject>();

        Map<String, byte[]> classBytes = compiler.compile(fileName, script,
                            ctx.getErrorWriter(), sourcePath, classPath, 
                            diagnostics, false);
        
        if (classBytes == null) {
            // check for unresolved variables, add to context, and retry
            boolean recompile = false;
            for (Diagnostic d : diagnostics.getDiagnostics()) {
                if (d.getCode().equals("compiler.err.cant.resolve.location")) {
                    Object[] args = ((JCDiagnostic)d).getArgs();
                    if (args.length >= 2 && args[1] instanceof String) {
                        ctx.setAttribute((String)args[1], null, 
                                         ScriptContext.ENGINE_SCOPE);
                        recompile = true;
                    }
                }
            }
            if (recompile) {
                binding = makeBindingStatement(ctx);
                script = binding + str;
                classBytes = compiler.compile(fileName, script,
                    ctx.getErrorWriter(), sourcePath, classPath);
            } else {
                JavaFXScriptCompiler.printDiagnostics(diagnostics, ctx.getErrorWriter());
            }
        }

        if (classBytes == null) {
            throw new ScriptException("compilation failed");
        }

        // create a ClassLoader to load classes from MemoryJavaFileManager
        MemoryClassLoader loader = new MemoryClassLoader(classBytes, classPath,
                                            getClass().getClassLoader());

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
        try {
            Method mainMethod = clazz.getMethod(JavafxModuleBuilder.runMethodString, new Class[0]);
            int modifiers = mainMethod.getModifiers();
            if (Modifier.isPublic(modifiers) && 
                Modifier.isStatic(modifiers)) {
                return mainMethod;
            }
        } catch (NoSuchMethodException nsme) {
        }
        return null;
    }

    private static String getFileName(ScriptContext ctx) {
        int scope = ctx.getAttributesScope(ScriptEngine.FILENAME);
        if (scope != -1) {
            return ctx.getAttribute(ScriptEngine.FILENAME, scope).toString();
        } else {
            return "$unnamed.fx";
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
                if (! isPublicClazz) {
                    // try to relax access
                    mainMethod.setAccessible(true);
                }        

                // call main method
                result = mainMethod.invoke(null, new Object[0]);
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

    private String makeBindingStatement(ScriptContext ctx) {
        
        // Merge attribute names from all scopes in context into single set.
        Set<String> attrs = new HashSet<String>();
        for (int scope : ctx.getScopes()) {
            attrs.addAll(ctx.getBindings(scope).keySet());
        }
        if (attrs.isEmpty())
            return "";

        // Define ScriptContext variable.
        StringBuilder sb = new StringBuilder();
        sb.append("var ");
        sb.append(SCRIPT_CONTEXT_NAME);
        sb.append(":javax.script.ScriptContext = \n");
        sb.append("com.sun.tools.javafx.script.ScriptContextManager.getContext(\"");
        sb.append(getScriptKey(ctx));
        sb.append("\");\n");
        
        // Define attributes as module variables.
        for (String attr : attrs) {
            sb.append("var ");
            sb.append(attr);
/* FIXME:  add type declaration when casts are supported
            Object value = ctx.getAttribute(attr);
            String type = value.getClass().getCanonicalName();
            if (type != null) {
                sb.append(':');
                sb.append(type);
            }
 */
            sb.append(" = ");
/* FIXME:  see above.
            sb.append('(');
            sb.append(type);
            sb.append(')');
 */
            sb.append(SCRIPT_CONTEXT_NAME);
            sb.append(".getAttribute(\"");
            sb.append(attr);
            sb.append("\");\n");
        }
        
        return sb.toString();
    }
}
