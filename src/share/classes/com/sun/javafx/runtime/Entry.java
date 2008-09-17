/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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
package com.sun.javafx.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;

import com.sun.javafx.functions.Function0;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * First code that is run to start a JavaFX Script application.
 * 
 * @author Tom Ball
 */
public class Entry {
    private static RuntimeProvider provider;

    public static void start(Class<?> app, String[] commandLineArgs) throws Throwable {
        Method main = app.getMethod(entryMethodName(), Sequence.class);
        Object args = Sequences.make(TypeInfos.String, commandLineArgs);
        
        try {
            main.setAccessible(true);
            provider = runtimeProviderLocator();
            if (provider != null && provider.usesRuntimeLibrary(app)) {
                provider.run(main, commandLineArgs);
            } else {
                try {
                    main.invoke(null, args);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            }
        } catch (AccessControlException ex) {
            // applet or jnlp app security problem:  try just running main
             try {
                main.invoke(null, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }

    public static void deferTask(final Function0<Void> function) {
        deferTask(new Runnable() {
            public void run() {
                function.invoke();
            }
        });
    }

    public static void deferTask(Runnable function) {
        if (provider == null)
            provider = runtimeProviderLocator();
        assert provider != null;
        provider.deferTask(function);
    }

    private static RuntimeProvider runtimeProviderLocator() {
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
                // Lookup Java 5 Sun-private API
                final Class<?>[] cls = new Class<?>[1];
                AccessController.doPrivileged(
                    new PrivilegedAction<Void>() {
                        public Void run() {
                            try {
                                cls[0] = Class.forName("sun.misc.Service");
                            } catch (ClassNotFoundException cnfe2) {
                                cls[0] = null;
                            }
                            return null;
                        }
                    });
                loaderClass = cls[0];
                loadMethodName = "providers";
                usingServiceLoader = false;
        }
        if (loaderClass == null)
            throw new AssertionError("Failed discovering ServiceLoader");

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
        try {
            return iterator.hasNext() ? (RuntimeProvider) iterator.next() : null;
        } catch (Error e) {
            // ServiceConfigurationError is in java.util in Java 6, sun.misc in Java 5,
            // so ignore its package
            if (e.getClass().getSimpleName().equals("ServiceConfigurationError"))
                return new NoRuntimeDefault(); // no service found
            else
                throw e;
        }
    }
    
    public static String entryMethodName() {
        return "javafx$run$";
    }
    
    private static class NoRuntimeDefault implements RuntimeProvider {

        public boolean usesRuntimeLibrary(Class application) {
            return true;
        }

        public Object run(Method entryPoint, String... args) throws Throwable {
            try {
                return entryPoint.invoke(null, (Object)args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

        public void deferTask(Runnable task) {
            java.awt.EventQueue.invokeLater(task);
        }        
    }
}
