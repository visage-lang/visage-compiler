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

import java.net.MalformedURLException;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;
import static java.util.logging.Level.*;

/**
 * JavaFX version of javac's ToolProvider class.
 * Provides methods for locating tool providers, for example,
 * providers of compilers.  This class complements the
 * functionality of {@link java.util.ServiceLoader}.
 *
 * @author Peter von der Ah&eacute;
 * @since 1.6
 */
public class ToolProvider {

    private ToolProvider() {}

    private static final String propertyName = "sun.tools.ToolProvider";
    private static final String loggerName   = "javax.tools";

    /*
     * Define the system property "sun.tools.ToolProvider" to enable
     * debugging:
     *
     *     java ... -Dsun.tools.ToolProvider ...
     */
    static <T> T trace(Level level, Object reason) {
        // NOTE: do not make this method private as it affects stack traces
        try {
            if (System.getProperty(propertyName) != null) {
                StackTraceElement[] st = Thread.currentThread().getStackTrace();
                String method = "???";
                String cls = ToolProvider.class.getName();
                if (st.length > 2) {
                    StackTraceElement frame = st[2];
                    method = String.format((Locale)null, "%s(%s:%s)",
                                           frame.getMethodName(),
                                           frame.getFileName(),
                                           frame.getLineNumber());
                    cls = frame.getClassName();
                }
                Logger logger = Logger.getLogger(loggerName);
                if (reason instanceof Throwable) {
                    logger.logp(level, cls, method,
                                reason.getClass().getName(), (Throwable)reason);
                } else {
                    logger.logp(level, cls, method, String.valueOf(reason));
                }
            }
        } catch (SecurityException ex) {
            System.err.format((Locale)null, "%s: %s; %s%n",
                              ToolProvider.class.getName(),
                              reason,
                              ex.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Gets the Java&trade; programming language compiler provided
     * with this platform.
     * @return the compiler provided with this platform or
     * {@code null} if no compiler is provided
     */
    public static JavafxCompiler getJavafxCompiler() {
        if (Lazy.compilerClass == null)
            return trace(WARNING, "Lazy.compilerClass == null");
        try {
            return Lazy.compilerClass.newInstance();
        } catch (Throwable e) {
            return trace(WARNING, e);
        }
    }

    /**
     * This class will not be initialized until one of the above
     * methods are called.  This ensures that searching for the
     * compiler does not affect platform start up.
     */
    static class Lazy  {
        private static final String defaultJavafxCompilerName
            = "com.sun.tools.javafx.api.JavafxcTool";
        static final Class<? extends JavafxCompiler> compilerClass;
        static {
            Class<? extends JavafxCompiler> c = null;
            try {
                c = findClass().asSubclass(JavafxCompiler.class);
            } catch (Throwable t) {
                trace(WARNING, t);
            }
            compilerClass = c;
        }

        private static Class<?> findClass()
            throws MalformedURLException, ClassNotFoundException
        {
            try {
                return enableAsserts(Class.forName(defaultJavafxCompilerName, false, null));
            } catch (ClassNotFoundException e) {
                trace(FINE, e);
                throw e;
            }
        }

        private static Class<?> enableAsserts(Class<?> cls) {
            try {
                ClassLoader loader = cls.getClassLoader();
                if (loader != null)
                    loader.setPackageAssertionStatus("com.sun.tools.javafx", true);
                else
                    trace(FINE, "loader == null");
            } catch (SecurityException ex) {
                trace(FINE, ex);
            }
            return cls;
        }
    }
}
