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
import java.lang.Object;

/** 
 * A SimpleIterator is defined in terms of a "curve".
 * It can be used for any value type that either implements Interpolatable
 * or that extends java.lang.Number.
 *
 * @profile common
 */
public abstract class SimpleInterpolator extends Interpolator {

    /** Mapping from [0.0..1.0] to itself.
      * @param t time, but normalized to the range [0.0..1.0],
      * where 0.0 is the start of the current interval (KeyFrame),
      * while 1.0 is the end of the current interval (KeyFrame).
      * Usually a function that increases monotonically.
      * 
      * @profile common
      */
    public abstract function curve(t: Number) : Number;

    /**
     * @profile common
     */      
    public function interpolate(startValue:Object, endValue:Object, fraction:Number):Object {
        if (startValue instanceof java.lang.Number and
	    endValue instanceof java.lang.Number) {
	    var start : Number = (startValue as java.lang.Number).doubleValue();
	    var end : Number = (endValue as java.lang.Number).doubleValue();
	    var val = start + (end-start)*curve(fraction);
	    if (startValue instanceof java.lang.Integer and
	        endValue instanceof java.lang.Integer)
		java.lang.Integer.valueOf((val+0.5).intValue())
            else
		java.lang.Double.valueOf(val)
        }
        else if (startValue instanceof Interpolatable) {
            (startValue as Interpolatable).ofTheWay((endValue as Interpolatable), curve(fraction));  
        } else {
            // discrete
            if (fraction == 1.0) endValue else startValue;
        }
    }

    /**
     * @profile common
     */      
    public function interpolate(startValue:Number, endValue:Number, fraction:Number):Number {
        return startValue + (endValue-startValue)*curve(fraction);
    }

    /**
     * @profile common
     */      
    public function interpolate(startValue:Integer, endValue:Integer, fraction:Number):Integer {
        return (startValue + (endValue-startValue)*curve(fraction) + 0.5).intValue();
    }
}
