/*
 * Copyright 2005-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.api;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

/**
 * JavaFX version of javac's ToolProvider class.
 * 
 * @author Tom Ball
 */
public class ToolProvider {
    private static Logger logger = Logger.getLogger("com.sun.javafx");

    private ToolProvider() {}

    /**
     * Gets a JavaFX Script compiler instance.
     * @return the compiler instance or {@code null} if no compiler 
     *         is included as part of the application classpath
     */
    public static JavafxCompiler getJavafxCompiler() {
        try {
            URL[] urls = new URL[] {
                getPath("com.sun.tools.javafx.api.JavafxcTool"),
                getPath("com.sun.tools.javac.util.Context")
            };
            ClassLoader parent = JavafxCompiler.class.getClassLoader();
            ClassLoader cl = new CompilerClassLoader(urls, parent);
            Class<?> cls = Class.forName("com.sun.tools.javafx.api.JavafxcTool", false, cl);
            return (JavafxCompiler)cls.newInstance();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Gets a JavaFX Script script engine instance.  This is an alternative
     * to the general use where the script engine is looked up as a service.
     * @return the script engine instance or {@code null} if no script engine 
     *         is included as part of the application classpath
     */
    public static JavaFXScriptEngine getJavaFXScriptEngine() {
        try {
            URL[] urls = new URL[] {
                getPath("com.sun.tools.javafx.script.JavaFXScriptEngineImpl"),
                getPath("com.sun.tools.javac.util.Context")
            };
            ClassLoader parent = JavafxCompiler.class.getClassLoader();
            ClassLoader cl = new CompilerClassLoader(urls, parent);
            Class<?> cls = Class.forName("com.sun.tools.javafx.script.JavaFXScriptEngineImpl", false, cl);
            return (JavaFXScriptEngine)cls.newInstance();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * ClassLoader which loads internal javafxc and javac classes from the
     * specified path instead of the classpath default.
     */
    private static class CompilerClassLoader extends URLClassLoader {
        public CompilerClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        @Override
        public URL findResource(String name) {
            URL url = super.findResource(name);
            return url;
        }
        
        @Override
        protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (name.indexOf("sun.tools") >= 0) {
                Class c = findLoadedClass(name);
                if (c != null) {
                    logger.info("found loaded class: " + name);
                    return c;
                }
                c = findClass(name);
                if (c == null) {
                    logger.info("didn't find class:  " + name);
                    return super.loadClass(name, resolve);
                }
                if (resolve)
                    resolveClass(c);
                return c;
            }
            return super.loadClass(name, resolve);
        }
    }

    /**
     * Return the classpath element that contains the specified class.
     * @return the classpath element, usually the class's jar file
     * @throws java.net.MalformedURLException
     */
    private static URL getPath(String className) throws MalformedURLException {
        String classFile = className.replace('.', '/') + ".class";
        ClassLoader cl = ToolProvider.class.getClassLoader();
        if (cl == null)
            cl = ClassLoader.getSystemClassLoader();
        URL classURL = cl.getResource(classFile);
        String path = classURL.getPath();
        assert path.endsWith(classFile);
        path = path.substring(0, path.indexOf(classFile) - 1);
        if (path.endsWith("!")) {
            path = path.substring(0, path.length() - 1);
        }
        File f = new File(path);
        if (f.exists()) {
            return f.toURI().toURL();
        }
        return new URL(path);
    }
}
