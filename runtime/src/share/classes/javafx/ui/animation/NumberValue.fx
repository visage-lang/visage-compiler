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

public class NumberValue extends KeyValue {

    public attribute value: Number;       
    public attribute interpolate: NumberInterpolator = DISCRETE;

    public function getInterpolatedValue(oldValue:Object, t:Number): Object {
           var c1 = (oldValue as java.lang.Number).doubleValue();
           var c2 = value;
           return interpolate.interpolate(c1, c2, t);
    }

    public function getValue():Object {
        return value;
    }

    public static attribute LINEAR:NumberInterpolator = NumberInterpolator {
        public function interpolate(oldValue:Number, newValue:Number, t:Number):Number {
            return oldValue + (newValue-oldValue)*t;
        }
    }

    public static function SPLINE(x1:Number,
                                  y1:Number,
                                  x2:Number,
                                  y2:Number): NumberInterpolator {
        return SplineInterpolator {
            x1: x1;
            y1: y1;
            x2: x2;
            y2: y2;
        };
    }

    public static attribute EASEIN:NumberInterpolator = SPLINE(0, 0, .5, 1);

    public static attribute EASEOUT:NumberInterpolator = SPLINE(0.5, 0, 1, 1);

    public static attribute EASEBOTH:NumberInterpolator = SPLINE(0.5, 0, 0.5, 1);

    public static attribute DISCRETE:NumberInterpolator = NumberInterpolator {
        public function interpolate(oldValue:Number, newValue:Number,
                                    t:Number):Number {
            return if (t == 1.0) newValue else oldValue;
        }
    };
}
