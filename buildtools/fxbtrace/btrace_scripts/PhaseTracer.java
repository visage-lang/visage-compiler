/*
 * Copyright 2010 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;
import com.sun.btrace.AnyType;

/**
 * Prints an indented trace message showing phase information.
 * Entry and exit of invalidate and be$.
 * Shows parameters to sequence invalidation.
 *
 * Also, increments a MBean property on every invalidation.
 *
 * @author A. Sundararajan
 * with modifications by:
 * @author Robert Field
 */
@BTrace public class PhaseTracer { 
    // expose number of invalidations (monotonically increasing)
    // as a MBean property
    @Property
    public static long invalidations;

    public static String indent = "";

    @OnMethod(
        clazz="+com.sun.javafx.runtime.FXObject",
        method="/invalidate\\$.+/"
    )
    public static void onInvalidateEnter(
        @ProbeClassName String className, 
        @ProbeMethodName String methodName,
        AnyType[] args) { 
        invalidations++;
	print(indent);
        print(args[args.length-1].toString().equals("1")? "+ " : "@ ");
        print(className);
        print(".");
        print(substr(methodName, strlen("invalidate$")));
        if (args.length == 4) {
          print(" [");
          print(args[0]);
          print("..");
          print(args[1]);
          print("] = ");
          print(args[2]);
        }
        println();
	indent += "   ";
    }

    @OnMethod(
        clazz="+com.sun.javafx.runtime.FXObject",
        method="/invalidate\\$.+/",
        location=@Location(Kind.RETURN)
    )
    public static void onInvalidateReturn(
        @ProbeClassName String className, 
        @ProbeMethodName String methodName) { 
        indent = indent.substring(3);
	print(indent);
        print("- ");
        print(className);
        print(".");
        println(substr(methodName, strlen("invalidate$")));
    }

    // "be$foo" are called to do recomputation set (internal set)
    @OnMethod(
        clazz="+com.sun.javafx.runtime.FXObject",
        method="/be\\$.+/"
    )
    public static void onBeEnter(
        @ProbeClassName String className, @ProbeMethodName String methodName) {
	print(indent);
        print("= ");
        print(className);
        print(".");
        println(substr(methodName, strlen("be$")));
	indent += "   ";
    }

    // "be$foo" are called to do recomputation set (internal set)
    @OnMethod(
        clazz="+com.sun.javafx.runtime.FXObject",
        method="/be\\$.+/",
        location=@Location(Kind.RETURN)
    )
    public static void onBeReturn(
        @ProbeClassName String className, @ProbeMethodName String methodName) {
        indent = indent.substring(3);
	print(indent);
        print("- ");
        print(className);
        print(".");
        println(substr(methodName, strlen("be$")));
    }
}
