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

package com.sun.javafx.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;
import java.util.Properties;

import com.sun.javafx.functions.Function0;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;
import com.sun.javafx.animation.AnimationProvider;

/**
 * First code that is run to start a JavaFX Script application.
 * 
 * @author Tom Ball
 */
public class Entry {
    public static volatile Thread uiThread;
    private static RuntimeProvider provider;
    private static AnimationProvider animationProvider;

    // FIXME: we really should avoid having this state be static, but
    // right now there is no effective way of virtualizing it.
    // Problems will occur with applets that are loaded by the same
    // class loader, i.e., by the classic Java Plug-In.
    private static String[] commandLineArgs;
    private static NamedArgumentProvider namedArgProvider;

    public static void start(final Class<?> app, String[] commandLineArgs) throws Throwable {
        if (commandLineArgs != null) {
            setNamedArgumentProvider(NamedArgumentProviderDefault.getInstance(commandLineArgs));
            if (namedArgProvider == null)
                Entry.commandLineArgs = (String[]) commandLineArgs.clone();
        }
        final Method main = app.getMethod(entryMethodName(), Sequence.class);
        Object args = Sequences.make(TypeInfo.String, commandLineArgs);
        try {
            AccessController.doPrivileged(
                new PrivilegedAction<Void>() {
                    public Void run() {
			try { 
			    // TODO: make this not get set for webstart case, 
			    // perhaps move to usesRuntimeLibrary
            		    String codebase = app.getProtectionDomain().getCodeSource().getLocation().toString();
            		    SystemProperties.setFXProperty(SystemProperties.codebase, codebase);
			} catch (NullPointerException ignored) {
	    		    // just in case the codesource is null
			}
                        main.setAccessible(true);
                        provider = runtimeProviderLocator();
                        return null;
                    }
                }
            ); 
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

    public static void deferAction(final Function0<Void> function) {
        deferAction(new Runnable() {
            public void run() {
                function.invoke$(null, null, null);
            }
        });
    }

    public static void deferAction(Runnable function) {
        if (provider == null)
            provider = runtimeProviderLocator();
        assert provider != null;
        provider.deferAction(function);
    }

    public static void exit() {
        if (provider == null) 
            provider = runtimeProviderLocator();
        assert provider != null;
        provider.exit();
    }

    public static void setNamedArgumentProvider(NamedArgumentProvider provider) {
        namedArgProvider = provider;
    }

    public static Sequence<? extends String> getArguments() {
        return Sequences.make(TypeInfo.String, commandLineArgs);
    }

    private static Object getArgument(int argument) {
        String[] args = commandLineArgs;
        if (args == null)
            return null;
        if (argument < 0 || argument >= args.length)
            return null;
        return args[argument];
    }

    public static Object getArgument(String key) {
        NamedArgumentProvider naProvider = namedArgProvider;
        Object val = null;
        if (naProvider != null)
            val = naProvider.get(key);
        if (val == null) {
            // Try the command line arguments
            try {
                return getArgument(Integer.parseInt(key));
            } catch (NumberFormatException e) {
            }
        }
        return val;
    }

    private static boolean loadAnimationProviderFailed = false;
    public static AnimationProvider getAnimationProvider() {
        if (!loadAnimationProviderFailed && (animationProvider == null)) {
            AccessController.doPrivileged(
                    new PrivilegedAction<Void>() {
                        public Void run() {
                            animationProvider = animationProviderLocator();
                            return null;
                        }
                    });
            if (animationProvider == null) {
                loadAnimationProviderFailed = true;
            }
        }

        return animationProvider;
    }

    private static AnimationProvider animationProviderLocator() {

        Iterator<?> iterator;
        Class<?> loaderClass;
        String loadMethodName;
        boolean usingServiceLoader;

        try {
            // Lookup Java 6 public API first
            loaderClass = Class.forName("java.util.ServiceLoader", true, null);
            loadMethodName = "load";
            usingServiceLoader = true;
        } catch (ClassNotFoundException cnfe) {
            try {
                // Lookup Java 5 Sun-private API
                loaderClass = Class.forName("sun.misc.Service", true, null);
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
            Object result = loadMethod.invoke(null, AnimationProvider.class, cl);

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
        if (iterator.hasNext()) {
            AnimationProvider result = (AnimationProvider) iterator.next();
            return result;
        }

        // If no provider, return null.
        return null;

    }

    private static RuntimeProvider runtimeProviderLocator() {
        Iterator<?> iterator;
        Class<?> loaderClass;
        String loadMethodName;
        boolean usingServiceLoader;

        try {
            // Lookup Java 6 public API first
            loaderClass = Class.forName("java.util.ServiceLoader", true, null);
            loadMethodName = "load";
            usingServiceLoader = true;
        } catch (ClassNotFoundException cnfe) {
                // Lookup Java 5 Sun-private API
                final Class<?>[] cls = new Class<?>[1];
                AccessController.doPrivileged(
                    new PrivilegedAction<Void>() {
                        public Void run() {
                            try {
                                cls[0] = Class.forName(
                                        "sun.misc.Service", true, null);
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
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
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
            return iterator.hasNext() ? (RuntimeProvider) iterator.next() : new NoRuntimeDefault();
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

    private static class NoRuntimeDefault extends Thread implements RuntimeProvider {

        private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();

        public boolean usesRuntimeLibrary(Class application) {
            return true;
        }

        public Object run(final Method entryPoint, final String... args) throws Throwable {
            /*
             * Add the Script invokation to the Queue
             */
            taskQueue.add(new Runnable() {
                public void run() {
                    try {
                        main(entryPoint, args);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            });
            this.start();
            return null;
        }

        private boolean hasActiveAnimation() {

            if (animationProvider == null) {
                return false;
            }

            return animationProvider.hasActiveAnimation();
        }

        @Override
        public void run() {
            Runnable task;
            try {
                while ((task = taskQueue.poll()) != null ||
                        hasActiveAnimation()) {
                        if (task != null) {
                            task.run();
                        } else {
                            try {
                                java.lang.Thread.sleep(1000);
                            } catch (java.lang.InterruptedException ie) {
                                break;
                            }
                        }
                }
                /*
                 * Implicit Exit after Timelines complete
                 */
                javafx.lang.FX.exit();
            } catch (FXExit fxExit) {
                return;
            }
        }

        private Object main(Method entryPoint, String... args) throws Throwable {
            try {
                return entryPoint.invoke(null, Sequences.make(TypeInfo.String, args));
            } catch (InvocationTargetException ite) {
                Throwable cause = ite.getCause();
                /*
                 * Explicit Exit via FX.exit()
                 */
                if (cause instanceof FXExit) {
                    return null;
                }
                throw cause;
            }
        }

        public void deferAction(Runnable r) {
            taskQueue.add(r);
        }        

        /*
         * Do nothing in this case as there should be no 
         * graphics displayed
         */
        public void exit() {
            try {
                System.exit(0);
            } catch (Throwable ignored) {
            }
        }
    }

    private static class NamedArgumentProviderDefault implements NamedArgumentProvider {

        private static final char DELIMITER = '=';

        private Properties namedArguments;	

        private NamedArgumentProviderDefault(Properties namedArguments) {
            this.namedArguments= namedArguments;
	}

        /**
         * Returns the given named argument, or null if the given argument
         * is not present. This usually returns a String, but some
         * environments may return some other kinds of values.
         */
        public Object get(String name) {
            return namedArguments.getProperty(name);
        }

        static NamedArgumentProvider getInstance(String [] commandline) {
            Properties namedArguments= new Properties();
            for(String arg : commandline) {
                int index = arg.indexOf(DELIMITER);
                if (index<=0)
                    return null;
                namedArguments.setProperty(arg.substring(0, index), arg.substring(index+1));
            }	
            return new NamedArgumentProviderDefault(namedArguments);
        }
    }

}
