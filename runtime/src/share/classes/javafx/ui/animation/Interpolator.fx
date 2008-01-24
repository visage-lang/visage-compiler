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

package javafx.ui.animation;

import com.sun.scenario.animation.Interpolators;
import java.lang.Object;
import java.lang.Double;

class SplineInterpolator extends Interpolator {
    attribute x1: Number;
    attribute y1: Number;
    attribute x2: Number;
    attribute y2: Number;
    attribute spline: com.sun.scenario.animation.Interpolator;
    init {
        spline = Interpolators.getSplineInstance(x1.floatValue(),
                                        y1.floatValue(), 
                                        x2.floatValue(), 
                                        y2.floatValue());
    }
    public function interpolate(oldValue:Object,
                                newValue:Object,
                                t:Number):Object 
    {
    
        return NUMBER.LINEAR.interpolate(oldValue, newValue,
                                         spline.interpolate(t.floatValue()));
    }
}

public class NUMBER {
    public static attribute LINEAR:Interpolator = Interpolator {
        public function interpolate(oldValue:Object,
                                    newValue:Object,
                                    t:Number):Object 
        {
            var x1 = (oldValue as java.lang.Number).doubleValue(); // hack
            var x2 = (newValue as java.lang.Number).doubleValue(); // hack
            return x1 + (x2 - x1)*t;
        }
    };

    public static function SPLINE(x1:Number,
                                  y1:Number,
                                  x2:Number,
                                  y2:Number): Interpolator {
        return SplineInterpolator {
            x1: x1;
            y1: y1;
            x2: x2;
            y2: y2;
        };
    }
}

public abstract class Interpolator {

    public abstract function interpolate(oldValue:Object,
                                         newValue:Object,
                                         t:Number):Object;

}
