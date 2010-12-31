/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.lang;

import java.lang.Comparable;
import java.lang.Math;
import javafx.util.Bits;

/**
 * Used to specify a angle of 0ms.
 *
 * @profile common
 */
public def ZERO: Angle = Angle { millis: 0};

/**
 * Used to specify a angle of 1ms.
 *
 * @profile common
 */
public def ONE: Angle = Angle { millis: 1};

/**
 * Used to specify a angle of indefinite length.
 *
 * @profile common
 */
public def INDEFINITE: Angle = Angle { millis: Double.POSITIVE_INFINITY};


// script-level "static" functions below

    /**
     * Factory method that returns a Angle instance for a specified
     * number of milliseconds.
     *
     * @param ms the number of milliseconds
     * @return a Angle instance of the specified number of milliseconds
     * @profile common
     */
    public function valueOf(ms: Double): Angle {
        if (ms == 0)
            ZERO
        else if (ms == 1)
            ONE
        else if (ms == Double.POSITIVE_INFINITY) //TODO: handling of negative infinity
            INDEFINITE
        else
             Angle {
                 millis: ms
             }

    }
    public function valueOf(ms: Float): Angle {
        valueOf(ms as Double)
    }

    public def TYPE_INFO = com.sun.javafx.runtime.TypeInfo.makeAndRegisterTypeInfo(ZERO);

/**
 * A class that defines a angle of time.  Angle instances are defined in
 * milliseconds, but can be easily created using time literals; for
 * example, a two-and-a-half minute Angle instance can be defined in several
 * ways:
 * <code><pre>    Angle t = 2m + 30s;
    Angle t = 2.5m;
    Angle t = 2500ms;</pre></code>
 * Angle instances are immutable, and are therefore replaced rather than modified.
 * To create a new Angle instance, either use a time literal, or use the
 * <code>Angle.valueOf(milliseconds)</code> factory method.
 *
 * @profile common
 */
public class Angle extends Comparable {

    var millis : Double;

    /** Returns the number of milliseconds in this period or Double.POSITIVE_INFINITY if the period is INDEFINITE.
     *
     * @profile common
     */
    public function toMillis():Double {
        return millis;
    }
    
    /** Returns the number of whole seconds in this period or Number.POSITIVE_INFINITY if the period is INDEFINITE. 
     *
     * @profile common
     */
    public function toSeconds():Number {
        return Math.floor(millis / 1000) as Number;
    }
    
    /** Returns the number of whole minutes in this period or Number.POSITIVE_INFINITY if the period is INDEFINITE. 
     *
     * @profile common
     */
    public function toMinutes(): Number {
        return Math.floor(millis / 60 / 1000) as Number;
    }
    
    /** Returns the number of whole hours in this period or Number.POSITIVE_INFINITY if the period is INDEFINITE. 
     *
     * @profile common
     */
    public function toHours(): Number {
        return Math.floor(millis / 60 / 60 / 1000) as Number;
    }

    /** Add this instance and another Angle instance to return a new Angle instance.
     *  If either instance is INDEFINITE, return INDEFINITE.
     *  This function does not change the value of the called Angle instance.
     *
     * @profile common
     */
    public function add(other:Angle):Angle {
        // Note that several of these functions assume that the value of millis in INDEFINITE
        // is Double.POSITIVE_INFINITY.
        return valueOf(millis + other.millis);
    }

    /** Subtract other Angle instance from this instance to return a new Angle instance.
     *  If either instance is INDEFINITE, return INDEFINITE.
     *  This function does not change the value of the called Angle instance.
     *
     * @profile common
     */
    public function sub(other:Angle):Angle {
        if (isIndefinite() or other.isIndefinite()) {
            return INDEFINITE;
        }
        return valueOf(millis - other.millis);
    }

    /** Multiply this instance with a number to return a new Angle instance.
     *  If the called Angle instance is INDEFINITE, return INDEFINITE.
     *  This function does not change the value of the called Angle instance.
     *
     * @profile common
     */ 
    public function mul(n:Number):Angle {
        return valueOf(millis * n);
    }


    /** Divide this instance by a number to return a new Angle instance.
     *  If the called Angle instance is INDEFINITE, return INDEFINITE.
     *  This function does not change the value of the called Angle instance.
     *
     * @profile common
     */     
    public function div(n:Number):Angle {
        if (n == 0) {
            throw new java.lang.ArithmeticException("/ by zero");
        }
        
        return valueOf(millis / n);
    }

    /** Divide this instance by another Angle to return the ratio.
     *  If both instances are INDEFINITE, return NaN.
     *  If this instance is INDEFINITE, return POSITIVE_INFINITY
     *  If the other instance is INDEFINITE, return 0.0.
     *  This function does not change the value of the called Angle instance.
     *
     * @profile common
     */     
    public function div(other:Angle):Number {
        if (isIndefinite() and other.isIndefinite()) {
            return java.lang.Float.NaN;
        } 
        if (isIndefinite()) {
            return java.lang.Float.POSITIVE_INFINITY;
        }
        if (other.isIndefinite()) {
            return 0.0;
        }
        if (other.millis == 0) {
            throw new java.lang.ArithmeticException("/ by zero");
        }
        
        return (millis / other.millis) as Number;
    }

    /** 
     * Return a new Angle instance which has a negative number of milliseconds
     * from this instance.  For example, <code>(50ms).negate()</code> returns
     * a Angle of -50 milliseconds.
     * If the called Angle instance is INDEFINITE, return INDEFINITE.
     * This function does not change the value of the called Angle instance.
     *
     * @profile common
     */
    public function negate():Angle {
        return if (isIndefinite()) INDEFINITE else valueOf(-millis);
    }

    function isIndefinite() : Boolean{
        return millis == java.lang.Float.POSITIVE_INFINITY;
    }

    /**
     * @profile common
     */        
    override function toString(): String {
        return if (isIndefinite()) "INDEFINITE" else "{millis}ms";
    }

    /** 
     * Returns true if the specified angle is less than (<) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @profile common
     */
    public function lt(other: Angle):Boolean {
        return compareTo(other) < 0;
    }

    /** 
     * Returns true if the specified angle is less than or equal to (<=) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @profile common
     */
    public function le(other: Angle):Boolean {
        return compareTo(other) <= 0;
    }

    /** 
     * Returns true if the specified angle is greater than (>) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @profile common
     */
    public function gt(other: Angle):Boolean {
        return compareTo(other) > 0;
    }

    /**
     * Returns true if the specified angle is greater than or equal to (>=) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @profile common
     */
    public function ge(other: Angle):Boolean {
        return compareTo(other) >= 0;
    }

    override function compareTo(obj : Object) : Integer {
        def d = obj as Angle;
        if (this.millis == d.millis) {
            return 0
        }
        if (d.isIndefinite()) {
            return -1
        }
        if (isIndefinite()) {
            return 1
        }
        def cmp = millis - d.millis;
        return if (cmp < 0)
             -1
        else if(cmp > 0)
             1
        else
             0
    }

    override function equals(obj : Object) : Boolean {
        if (obj instanceof Angle) {
            def d = obj as Angle;
            if (isSameObject(d, this)) {
                return true;
            }

            if (d.isIndefinite() or isIndefinite()) {
                return false;
            }
            return d.millis == millis;
        }
        return false;
    }

    override function hashCode() : Integer {
        if (isIndefinite()) {
            // some unlikely value
            return Integer.MIN_VALUE + 89;
        }
        def value = millis as Long;
        return Bits.bitXor(value, Bits.unsignedShiftRight(value, 32)) as Integer;
    }
}

