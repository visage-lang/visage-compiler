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
package com.sun.javafx.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * First code that is run to start a JavaFX Script application.
 * 
 * @author Tom Ball
 */
public class Entry {

    public static void start(Class<?> app, String... args) throws Throwable {
        Method main = null;
        main = app.getDeclaredMethod("javafx$run$", new Class[0]);

        RuntimeProvider provider = runtimeProviderLocator(app);
        if (provider != null && provider.usesRuntimeLibrary(app)) {
            provider.run(main);
        } else {
            try {
                main.invoke(null);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }

    private static RuntimeProvider runtimeProviderLocator(Class app) {
        Iterator<?> iterator;
        Class<?> loaderClass;
        String loadMethodName;
        boolean usingServiceLoader;

        try {
            // Lookup Java 6 public API first
            loaderClass = Class.forName("java.util.ServiceLoader");
            loadMethodName = "load";
            usingServiceLoader = true;
        } catch (ClassNotFoundException cnfe) {
            try {
                // Lookup Java 5 Sun-private API
                loaderClass = Class.forName("sun.misc.Service");
                loadMethodName = "providers";
                usingServiceLoader = false;
            } catch (ClassNotFoundException cnfe2) {
                throw new AssertionError("Failed discovering ServiceLoader");
            }
        }

        try {
            // java.util.ServiceLoader.load or sun.misc.Service.providers
            Method loadMethod = loaderClass.getMethod(loadMethodName,
                    Class.class,
                    ClassLoader.class);
            ClassLoader cl = Entry.class.getClassLoader();
            Object result = loadMethod.invoke(null, RuntimeProvider.class, cl);

            // For java.util.ServiceLoader, we have to call another
            // method to get the iterator.
            if (usingServiceLoader) {
                Method m = loaderClass.getMethod("iterator");
                result = m.invoke(result); // serviceLoader.iterator();
            }

            iterator = (Iterator<?>) result;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return iterator.hasNext() ? (RuntimeProvider) iterator.next() : null;
    }
}
