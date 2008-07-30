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

package javafx.animation;

import java.lang.Float;
import java.lang.Object;
import com.sun.scenario.animation.Interpolators;

/**
 * @profile common
 */
public def DISCRETE:Interpolator =
        CoreInterpolator { i: Interpolators.getDiscreteInstance(); };

/**
 * @profile common
 */
public def LINEAR:Interpolator =
        CoreInterpolator { i: Interpolators.getLinearInstance(); };

/**
 * @profile common
 */
public def EASEBOTH:Interpolator =
        CoreInterpolator { i: Interpolators.getEasingInstance(); };

/**
 * @profile common
 */
public def EASEIN:Interpolator =
        CoreInterpolator { i: Interpolators.getEasingInstance(new Float(0.2), new Float(0.0)); };

/**
 * @profile common
 */
public def EASEOUT:Interpolator =
        CoreInterpolator { i: Interpolators.getEasingInstance(new Float(0.0), new Float(0.2)); };

/**
 * @profile common
 */
public function SPLINE(x1: Number, y1: Number, x2: Number, y2: Number):Interpolator {
        CoreInterpolator { i: Interpolators.getSplineInstance(x1.floatValue(), y1.floatValue(),
                                                              x2.floatValue(), y2.floatValue()); }
    }

/**
 * @profile common
 */
public abstract class Interpolator {
    
    /**
     * @profile common
     */    
    public abstract function interpolate(startValue:Object, endValue:Object, fraction:Number):Object;
}

class CoreInterpolator extends SimpleInterpolator {
    var i:com.sun.scenario.animation.Interpolator;

    override function curve(t: Number) : Number {
        i.interpolate(t.floatValue())
    }
}
