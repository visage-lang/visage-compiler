/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.animation;

import java.lang.Float;
import java.lang.Object;
import com.sun.scenario.animation.Interpolators;

public abstract class Interpolator {
    public abstract function interpolate(t: Number) : Number;

    public static /* readonly */ attribute DISCRETE:Interpolator =
        CoreInterpolator { i: Interpolators.getDiscreteInstance(); };
    public static /* readonly */ attribute LINEAR:Interpolator =
        CoreInterpolator { i: Interpolators.getLinearInstance(); };
    public static /* readonly */ attribute EASEBOTH:Interpolator =
        CoreInterpolator { i: Interpolators.getEasingInstance(); };
    public static /* readonly */ attribute EASEIN:Interpolator =
        CoreInterpolator { i: Interpolators.getEasingInstance(new Float(0.2), new Float(0.0)); };
    public static /* readonly */ attribute EASEOUT:Interpolator =
        CoreInterpolator { i: Interpolators.getEasingInstance(new Float(0.0), new Float(0.2)); };
    public static function SPLINE(x1: Number, y1: Number, x2: Number, y2: Number):Interpolator {
        CoreInterpolator { i: Interpolators.getSplineInstance(x1.floatValue(), y1.floatValue(),
                                                              x2.floatValue(), y2.floatValue()); }
    }
}

class CoreInterpolator extends Interpolator {
    attribute i:com.sun.scenario.animation.Interpolator;

    public function interpolate(t: Number) : Number {
        i.interpolate(t.floatValue())
    }
}
