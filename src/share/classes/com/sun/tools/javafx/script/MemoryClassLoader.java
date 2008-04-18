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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.StringTokenizer;

/**
 * ClassLoader that loads .class bytes from memory.
 *
 * @author A. Sundararajan
 */
public final class MemoryClassLoader extends URLClassLoader {
    private Map<String, byte[]> classBytes;
    private URL source;

    public MemoryClassLoader(URL source, Map<String, 
               byte[]> classBytes, String classPath, ClassLoader parent) {
        super(toURLs(classPath), parent);
        this.source = source;
        this.classBytes = classBytes;
    }

    public Class load(String className) throws ClassNotFoundException {
        return loadClass(className);
    }

    public Iterable<Class> loadAll() throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>(classBytes.size());
        for (String name : classBytes.keySet()) {
            classes.add(loadClass(name));
        }
        return classes;
    }

    @Override
    protected Class findClass(String className) throws ClassNotFoundException {
        byte[] buf = classBytes.get(className);
        if (buf != null) {
            // clear the bytes in map -- we don't need it anymore
            classBytes.put(className, null);
            return defineClass(className, buf, 0, buf.length);
        } else {
            return super.findClass(className);
        }
    }

    @Override
    public URL findResource(String name) {
        if (name.endsWith(".class")) {
            name = name.substring(0, name.length() - 6);
            if (classBytes.containsKey(name))
                return source;
        }
        return super.findResource(name);
    }

    private static URL[] toURLs(String classPath) {
        if (classPath == null) {
            return new URL[0];
        }

        List<URL> list = new ArrayList<URL>();
        StringTokenizer st = new StringTokenizer(classPath, File.pathSeparator);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            File file = new File(token);
            if (file.exists()) {
                try {
                    list.add(file.toURI().toURL());
                } catch (MalformedURLException mue) {}
            } else {
                try {
                    list.add(new URL(token));
                } catch (MalformedURLException mue) {}
            }
        }
        URL[] res = new URL[list.size()];
        list.toArray(res);
        return res;
    }
}
