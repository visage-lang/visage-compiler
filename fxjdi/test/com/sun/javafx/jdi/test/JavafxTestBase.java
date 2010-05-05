/*
 * Copyright 2001-2005 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.jdi.test;

import com.sun.jdi.event.BreakpointEvent;
import java.io.File;

/**
 * Base class for tests in which the target is a JavaFX application. This takes
 * care of setting javafxrt.jar in classpath in addition to application class path.
 * Also has utility methods for javafx methods.
 *
 * @author sundar
 */
public abstract class JavafxTestBase extends TestScaffold {
    protected static String classPath() {
        return testBuildDirectory() +
                File.pathSeparator +
                javafxrtJarPath();
    }

    protected static String javafxrtJarPath() {
        return System.getProperty("javafxrt.jar");
    }

    protected static String fxMainClassName() {
        return "com.sun.javafx.runtime.Main";
    }

    protected static String fxRunMethodName() {
        return "javafx$run$";
    }

    protected static String fxRunMethodSignature() {
        return "(Lcom/sun/javafx/runtime/sequence/Sequence;)Ljava/lang/Object;";
    }

    protected static final String[] ARGS = {
        "-J-classpath",
        classPath(),
        fxMainClassName()
    };

    protected static String[] arguments(String targetClassName) {
        String[] args = new String[ARGS.length + 1];
        System.arraycopy(ARGS, 0, args, 0, ARGS.length);
        args[args.length - 1] = targetClassName;
        return args;
    }

    protected JavafxTestBase(String targetClassName) {
        super(arguments(targetClassName));
    }

    protected JavafxTestBase(String[] args) {
        super(args);
    }

    protected BreakpointEvent startToMain() {
        return startToMain(fxMainClassName());
    }
}