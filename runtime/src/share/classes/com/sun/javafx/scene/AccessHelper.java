/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.scene;

import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.light.Light;

/**
 * Uses the "accessor" pattern to give e.g. javafx.gui.Node access to
 * implementation details in the javafx.gui.effect package.
 * Public only to allow use from other packages.
 *
 * Not part of the public API.
 */
public class AccessHelper {
    
    /**
     * Provides access to implementation details in
     * javafx.gui.effect package.
     */
    public interface EffectAccessor {
        public Effect getImpl(Object fxEffect);
    }
    
    private static EffectAccessor theEffectAccessor;
    
    public static void setEffectAccessor(EffectAccessor accessor) {
        if (theEffectAccessor != null) {
            throw new InternalError("EffectAccessor already initialized");
        }
        theEffectAccessor = accessor;
    }
    
    public static Effect getEffectImpl(Object fxEffect) {
        if (fxEffect == null) {
            return null;
        }
        return theEffectAccessor.getImpl(fxEffect);
    }
    
    
    /**
     * Provides access to implementation details in
     * javafx.gui.effect.light package.
     */
    public interface LightAccessor {
        public Light getImpl(Object fxLight);
    }
    
    private static LightAccessor theLightAccessor;
    
    public static void setLightAccessor(LightAccessor accessor) {
        if (theLightAccessor != null) {
            throw new InternalError("LightAccessor already initialized");
        }
        theLightAccessor = accessor;
    }
    
    public static Light getLightImpl(Object fxLight) {
        if (fxLight == null) {
            return null;
        }
        return theLightAccessor.getImpl(fxLight);
    }
}
