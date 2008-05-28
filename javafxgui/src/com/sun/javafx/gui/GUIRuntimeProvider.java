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
package com.sun.javafx.gui;

import java.lang.reflect.Method;
import java.awt.EventQueue;
import com.sun.javafx.runtime.RuntimeProvider;
import com.sun.javafx.runtime.sequence.Sequences;
import java.lang.reflect.InvocationTargetException;

public class GUIRuntimeProvider implements RuntimeProvider {

    public boolean usesRuntimeLibrary(Class application) {
        return true;
    }

    public Object run(final Method entryPoint, final String... args) throws Throwable  {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    InternalHelper.initDefaultLAF();
                    entryPoint.invoke(null, Sequences.make(String.class, args));
                } catch (IllegalAccessException ignore) {
                    throw new AssertionError();
                } catch (InvocationTargetException ite) {
                    Throwable cause = ite.getCause();
                    StackTraceElement[] stack = cause.getStackTrace();
                    int n = 0;
                    while (n < stack.length) {
                        if (stack[n++].getMethodName().equals(entryPoint.getName())) {
                            break;

                        }
                    }
                    StackTraceElement[] shortStack = new StackTraceElement[n];
                    System.arraycopy(stack, 0, shortStack, 0, n);
                    cause.setStackTrace(shortStack);
                    cause.printStackTrace();
                }
            }
        });

        return null;
    }

    public void deferTask(Runnable task) {
        EventQueue.invokeLater(task);
    }

}
