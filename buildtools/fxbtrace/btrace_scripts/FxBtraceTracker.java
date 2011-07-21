/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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
import java.util.Map;
import java.lang.ref.*;
import com.sun.visage.runtime.FXObject;

/**
 * This BTrace script tries to measure aggregate stat like total
 * FXObject count, total dependent count, total notification count etc.
 * This script measures histogram of FXObjects as well.
 *
 * @author A. Sundararajan
 */
@BTrace public class FxBtraceTracker { 
   private static Class dependentClass;
   private static Class weakBinderRefClass;

   // @Property exposes this field as MBean attribute
   @Property
   private static Map<String, Integer> histo = newHashMap();
   
   /*
    * its unfortunate that the btrace/mbean does not refresh the
    * hashmap, therefore we get these values and present them via
    * mbean individually.
    */
    @Property
    private static long fxObjectCount = 0;
    @Property
    private static long fxWeakRefsCount = 0;
    @Property
    private static long fxDependentCount = 0;
    @Property
    private static long fxNotifyDependentsCount = 0;

    @OnMethod(
	clazz="com.sun.visage.runtime.FXBase",
        method="<init>"
    ) 
    public static void onNewFXObject(@Self Object obj, boolean dummy) {
        fxObjectCount++;
        String cn = name(classOf(obj));
        Integer i = get(histo, cn);
        if (i == null) {
            i = box(1);
        } else {
            i = box(unbox(i) + 1);
        } 
        put(histo, cn, i);
    }

    @OnMethod(
        clazz="com.sun.visage.runtime.WeakBinderRef",
        method="<init>"
    )
    public static void onNewWeakBinderRef(@Self Object obj, FXObject referred) {
        weakBinderRefClass = probeClass();
        fxWeakRefsCount++;
    }

    @OnMethod(
        clazz="com.sun.visage.runtime.Dependent",
        method="<init>"
    )
    public static void onNewDependent(@Self Object obj, FXObject referred) {
        dependentClass = probeClass();
        fxWeakRefsCount++;
    }

    @OnMethod(
        clazz="java.lang.ref.Reference",
        method="clear"
    )
    public static void onReferenceClear(@Self Object obj) {
        if ((dependentClass != null && isInstance(dependentClass, obj)) ||
            (weakBinderRefClass != null && isInstance(weakBinderRefClass, obj))) {
            fxWeakRefsCount--;
        }
    }

    @OnMethod(
	clazz="com.sun.visage.runtime.FXBase",
        method="addDependent$"
    ) 
    public static void onAddDependent(FXObject obj, int varNum, FXObject dep) {
        fxDependentCount++;
    }

    @OnMethod(
	clazz="com.sun.visage.runtime.FXBase",
        method="removeDependent$"
    ) 
    public static void onRemoveDependent(FXObject obj, int varNum, FXObject dep) {
        fxDependentCount--;
    }

    @OnMethod(
	clazz="com.sun.visage.runtime.FXBase",
        method="notifyDependents$"
    ) 
    public static void onNotifyDependents(FXObject obj, int varNum) {
        fxNotifyDependentsCount++;
    }

    @OnTimer(4000) 
    public static void print() {
        if (size(histo) != 0) {
            println("========================");
            printNumberMap("FXBase Histogram", histo);
            println("========================");
        }
    }
}
