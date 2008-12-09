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

package javafx.lang;

import java.lang.Object;
import java.lang.Comparable;
import java.lang.Math;

/**
 * A class that defines a duration of time.  Duration instances are defined in
 * milliseconds, but can be easily created using time literals; for
 * example, a two-and-a-half minute Duration instance can be defined in several
 * ways:
 * <code><pre>    Duration t = 2m + 30s;
    Duration t = 2.5m;
    Duration t = 2500ms;</pre></code>
 * Duration instances are immutable, and are therefore replaced rather than modified.
 * To create a new Duration instance, either use a time literal, or use the
 * <code>Duration.valueOf(milliseconds)</code> factory method.
 *
 * @profile common
 */
public class Duration extends com.sun.javafx.runtime.Duration {

    /** Returns the number of milliseconds in this period. 
     *
     * @profile common
     */
    public function toMillis():Number {
        return millis;
    }
    
    /** Returns the number of whole seconds in this period. 
     *
     * @profile common
     */
    public function toSeconds():Number {
        return Math.floor(millis / 1000);
    }
    
    /** Returns the number of whole minutes in this period. 
     *
     * @profile common
     */
    public function toMinutes(): Number {
        return Math.floor(millis / 60 / 1000);
    }
    
    /** Returns the number of whole hours in this period. 
     *
     * @profile common
     */
    public function toHours(): Number {
        return Math.floor(millis / 60 / 60 / 1000);
    }

    /** Add this instance and another Duration instance to return a new Duration instance.
     *  This function does not change the value of called Duration instance. 
     *
     * @profile common
     */
    public function add(other:Duration):Duration {
        return valueOf(millis + other.millis);
    }

    /** Subtract other Duration instance from this instance to return a new Duration instance.
     *  This function does not change the value of called Duration instance. 
     *
     * @profile common
     */
    public function sub(other:Duration):Duration {
        return valueOf(millis - other.millis);
    }

    /** Multiply this instance with a number to return a new Duration instance.
     *  This function does not change the value of called Duration instance. 
     *
     * @profile common
     */ 
    public function mul(n:Number):Duration {
        return valueOf(millis * n);
    }


    /** Divide this instance by a number to return a new Duration instance.
     *  This function does not change the value of called Duration instance. 
     *
     * @profile common
     */     
    public function div(n:Number):Duration {
        if (n == 0) {
            throw new java.lang.ArithmeticException("/ by zero");
        }
        
        return valueOf(millis / n);
    }

    /** Divide this instance by another Duration to return the ratio.
     *  This function does not change the value of called Duration instance. 
     *
     * @profile common
     */     
    public function div(other:Duration):Number {
        if (other.millis == 0) {
            throw new java.lang.ArithmeticException("/ by zero");
        }
        
        return millis / other.millis;
    }

    /** 
     * Return a new Duration instance which has a negative number of milliseconds
     * from this instance.  For example, <code>(50ms).negate()</code> returns
     * a Duration of -50 milliseconds.  This function does not change the value 
     * of called Duration instance. 
     *
     * @profile common
     */
    public function negate():Duration {
        return valueOf(-millis);
    }

    /**
     * @profile common
     */        
    override function toString(): String {
        return "{millis}ms";
    }

    /** 
     * Returns true if the specified duration is less than (<) this instance. 
     *
     * @profile common
     */
    public function lt(other: Duration):Boolean {
        return compareTo(other) < 0;
    }

    /** 
     * Returns true if the specified duration is less than or equal to (<=) this instance. 
     *
     * @profile common
     */
    public function le(other: Duration):Boolean {
        return compareTo(other) <= 0;
    }

    /** 
     * Returns true if the specified duration is greater than (>) this instance. 
     *
     * @profile common
     */
    public function gt(other: Duration):Boolean {
        return compareTo(other) > 0;
    }

    /**
     * Returns true if the specified duration is greater than or equal to (>=) this instance. 
     *
     * @profile common
     */
    public function ge(other: Duration):Boolean {
        return compareTo(other) >= 0;
    }

    /** 
     * Returns a Date instance initialized to the length of this instance.  
     * It is equivalent to <code>new java.util.Data(this.toMillis())</code>.
     */
    public function toDate():java.util.Date {
        return new java.util.Date(millis.longValue());
    }
}

// script-level "static" functions below

    /**
     * Factory method that returns a Duration instance for a specified
     * number of milliseconds.
     *
     * @param ms the number of milliseconds
     * @return a Duration instance of the specified number of milliseconds
     * @profile common
     */
    public function valueOf(ms: Number): Duration {
        return com.sun.javafx.runtime.Duration.make(ms) as Duration;
    }

    def TYPE_INFO = com.sun.javafx.runtime.TypeInfo.makeAndRegisterTypeInfo(Duration.valueOf(0.0));

