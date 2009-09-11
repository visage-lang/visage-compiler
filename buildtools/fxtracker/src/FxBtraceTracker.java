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
import java.util.concurrent.atomic.AtomicInteger;
import com.sun.javafx.runtime.location.*;

/**
 * This is plagiarized from the btrace samples, and essentially does the following:
 *    a> injects the tracking code using the OnMethod annotation
 *    b> acts as a bridge between mbean and FxTracker to view the
 *       Fx tracking counters from jvisualvm or jconsole.
 * 
 * @author ksrini
 */
@BTrace public class FxBtraceTracker { 
   // @Property exposes this field as MBean attribute
   @Property
   private static Map<String, AtomicInteger> histo = newHashMap();
   
   /*
    * its unfortunate that the btrace/mbean does not refresh the
    * hashmap, therefore we get these values and present them via
    * mbean individually.
    */
    @Property
    private static long bindingExpressionCount = 0;
    @Property
    private static long changeListenerCount = 0;
    @Property
    private static long triggerCount = 0;
    @Property
    private static long viewLocationCount = 0;
    @Property
    private static long weakLocationCount = 0;
    @Property
    private static long weakMeHolderCount = 0;
    @Property
    private static long totalChildrenCount = 0;
    @Property
    private static long locationMapSize = 0;
    @Property
    private static long iteratorCount = 0;
    @Property
    private static long listenerCount = 0;
    @Property
    private static long fxObjectCount = 0;
    @Property
    private static long sdlNullReferentCount = 0;


    // insert new ctors and update tracking data for AbstractLocation
    @OnMethod(
	clazz="com.sun.javafx.runtime.location.AbstractLocation",
        method="<init>"
    ) 
    public static void onnewObject(@Self Object obj) {
        FxTracker.track(obj);
        bindingExpressionCount = FxTracker.getBindingExpressionCount();
        changeListenerCount =  FxTracker.getChangeListenerCount();
        triggerCount = FxTracker.getTriggerCount();
        viewLocationCount = FxTracker.getViewLocationCount();
        weakLocationCount = FxTracker.getWeakLocationCount();
        totalChildrenCount = FxTracker.getTotalChildrenCount();
        locationMapSize = FxTracker.getLocationMapSize();
        iteratorCount = FxTracker.getIteratorCount();
        listenerCount = FxTracker.getListenerCount();
        fxObjectCount = FxTracker.getFxObjectCount();
        sdlNullReferentCount = FxTracker.getSdlNullReferentCount();
        String cn = name(classOf(obj));
        AtomicInteger ai = get(histo, cn);
        if (ai == null) {
            ai = newAtomicInteger(1);
            put(histo, cn, ai);
        } else {
            incrementAndGet(ai);
        }     
    }

    // insert new ctors and update tracking data for FXBase
    @OnMethod(
	clazz="com.sun.javafx.runtime.FXBase",
        method="<init>"
    ) 
    public static void onnewObject(@Self Object obj, boolean dummy) {
        FxTracker.track(obj);
        bindingExpressionCount = FxTracker.getBindingExpressionCount();
        changeListenerCount =  FxTracker.getChangeListenerCount();
        triggerCount = FxTracker.getTriggerCount();
        viewLocationCount = FxTracker.getViewLocationCount();
        weakLocationCount = FxTracker.getWeakLocationCount();
        totalChildrenCount = FxTracker.getTotalChildrenCount();
        locationMapSize = FxTracker.getLocationMapSize();
        iteratorCount = FxTracker.getIteratorCount();
        listenerCount = FxTracker.getListenerCount();
        fxObjectCount = FxTracker.getFxObjectCount();
        sdlNullReferentCount = FxTracker.getSdlNullReferentCount();
        String cn = name(classOf(obj));
        AtomicInteger ai = get(histo, cn);
        if (ai == null) {
            ai = newAtomicInteger(1);
            put(histo, cn, ai);
        } else {
            incrementAndGet(ai);
        }     
    }

    // insert new ctors and update tracking data for StaticDependentLocation
    @OnMethod(
	clazz="com.sun.javafx.runtime.location.StaticDependentLocation",
        method="<init>"
    )
    public static void onnewObject(@Self Object obj, com.sun.javafx.runtime.location.Location r) {
        FxTracker.track(obj);
        bindingExpressionCount = FxTracker.getBindingExpressionCount();
        changeListenerCount =  FxTracker.getChangeListenerCount();
        triggerCount = FxTracker.getTriggerCount();
        viewLocationCount = FxTracker.getViewLocationCount();
        weakLocationCount = FxTracker.getWeakLocationCount();
        totalChildrenCount = FxTracker.getTotalChildrenCount();
        locationMapSize = FxTracker.getLocationMapSize();
        iteratorCount = FxTracker.getIteratorCount();
        listenerCount = FxTracker.getListenerCount();
        fxObjectCount = FxTracker.getFxObjectCount();
        sdlNullReferentCount = FxTracker.getSdlNullReferentCount();
        String cn = name(classOf(obj));
        AtomicInteger ai = get(histo, cn);
        if (ai == null) {
            ai = newAtomicInteger(1);
            put(histo, cn, ai);
        } else {
            incrementAndGet(ai);
        }
    }

    @OnTimer(4000) 
    public static void print() {
        if (size(histo) != 0) {
            printNumberMap("Component Histogram", histo);
        }
    }    
}

