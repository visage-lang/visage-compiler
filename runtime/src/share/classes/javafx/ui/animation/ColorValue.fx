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

import java.lang.Object;
import javafx.ui.Color;


// these outer classes are workarounds for inner class bugs
class SplineColorInterpolator extends ColorInterpolator {
    attribute x1: Number;
    attribute y1: Number;
    attribute x2: Number;
    attribute y2: Number;
    attribute spline: NumberInterpolator;
    init {
        spline = NumberValue.SPLINE(x1, y1, x2, y2);
    }
    public function interpolate(oldValue:Color, newValue:Color,
                                t:Number):Color {
        var spline = NumberValue.SPLINE(x1, y1, x2, y2);
        t = spline.interpolate(0.0, 1.0, t);
        return oldValue.interpolate(newValue, t);
    }
}

class NumberInterpolatorAdapter extends ColorInterpolator {

    attribute numInterp:NumberInterpolator;

    public function interpolate(oldValue:Color,
                                newValue:Color,
                                t:Number):Color {
        t = numInterp.interpolate(0.0, 1.0, t);
        return oldValue.interpolate(newValue, t);
    }

}

public class ColorValue extends KeyValue {

    public attribute value: Color;
    public attribute interpolate: ColorInterpolator = DISCRETE;
    public function getInterpolatedValue(oldValue:Object, t:Number): Object {
        var c1 = oldValue as javafx.ui.Color;
        return interpolate.interpolate(c1, value, t);
    }

    public function getValue():Object {
        return value;
    }

    // construct color interpolator from number interpolator
    public static function colorInterpolator(numInterp:NumberInterpolator):ColorInterpolator {
        return NumberInterpolatorAdapter {
            numInterp: numInterp;
        };
    }

    public static attribute DISCRETE:ColorInterpolator = ColorInterpolator {
        public function interpolate(oldValue:Color, newValue:Color,
                                    t:Number):Color {
            return if (t == 1.0) newValue else oldValue;
        }
    };

    public static attribute LINEAR:ColorInterpolator = colorInterpolator(NumberValue.LINEAR);

    public static attribute EASEIN:ColorInterpolator = colorInterpolator(NumberValue.EASEIN);

    public static attribute EASEOUT:ColorInterpolator = colorInterpolator(NumberValue.EASEOUT);
    public static attribute EASEBOTH:ColorInterpolator = colorInterpolator(NumberValue.EASEBOTH);

    public static function SPLINE(x1:Number, y1:Number, x2:Number, y2:Number):ColorInterpolator {
        SplineColorInterpolator {
            x1: x1, y1: y1, x2: x2, y2: y2;
        }
    }
}
