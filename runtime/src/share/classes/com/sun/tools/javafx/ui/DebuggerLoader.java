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

package com.sun.tools.javafx.ui;

import java.lang.reflect.Method;

/**
 * Simple main-method invoker, so that arbitrary compiled FX classes can be
 * be used when debugging runtime code.  To use, specify this class in the
 * debugger as the main class, and the target class as its parameter.
 * 
 * @author Tom Ball
 */
public class DebuggerLoader {
    public static void main(String[] args) {
        try {
            if (args.length == 0)
                throw new IllegalArgumentException("class to debug not specified");
            Class<?> arg = Class.forName(args[0]);
            Class<?> c = Class.forName("com.sun.javafx.runtime.Entry");
            Method m = c.getMethod("start", Class.class);
            m.invoke(null, arg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
