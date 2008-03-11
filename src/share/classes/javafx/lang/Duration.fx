/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.lang;

import java.lang.Object;
import java.lang.Comparable;
import java.lang.Long;
import java.lang.Math;

/**
 * A class that defines a duration of time.  Duration instances are defined in
 * milliseconds, but can be easily created using time literals; for
 * example, a two-and-a-half minute Duration instance can be defined in several
 * ways:
 * <code><pre>    Duration t = 2m + 30s;
    Duration t = 2.5s;
    Duration t = 2500ms;</pre></code>
 */
public class Duration extends Comparable {

    /** The duration of time, as expressed in milliseconds. */
    public attribute millis: Number;

    public function equals(obj:Object):Boolean {
        if (obj instanceof Duration) {
            var t = obj as Duration;
            return t.millis == millis;
        }
        return false;
    }

    public function compareTo(obj:Object):Integer {
        var t = obj as Duration;
        var m1 = millis;
        var m2 = t.millis;
        var cmp = m1 - m2;
        return if (cmp < 0) -1 else if (cmp > 0) 1 else 0;
    }

    public function hashCode():Integer {
        return Long.valueOf(millis.longValue()).hashCode();
    }

    /** Returns the number of milliseconds in this period. */
    public function toMillis():Number {
        return millis;
    }
    
    /** Returns the number of whole seconds in this period. */
    public function toSeconds():Number {
        return Math.floor(millis / 1000);
    }
    
    /** Returns the number of whole minutes in this period. */
    public function toMinutes(): Number {
        return Math.floor(millis / 60 / 1000);
    }
    
    /** Returns the number of whole hours in this period. */
    public function toHours(): Number {
        return Math.floor(millis / 60 / 60 / 1000);
    }

    /** Add this instance and another Duration instance to return a new Duration instance.
     *  This function does not change the value of called Duration instance. */
    public function add(other:Duration):Duration {
        return Duration {
            millis: millis + other.millis;
        }
    }

    /** Subtract this instance from another Duration instance to return a new Duration instance.
     *  This function does not change the value of called Duration instance. */
    public function sub(other:Duration):Duration {
        return Duration {
            millis: millis - other.millis;
        }
    }

    /** Multiply this instance with a number to return a new Duration instance.
     *  This function does not change the value of called Duration instance. */
    public function mul(n:Number):Duration {
        return Duration {
            millis: millis * n;
        }
    }


    /** Divide this instance by a number to return a new Duration instance.
     *  This function does not change the value of called Duration instance. */
    public function div(n:Number):Duration {
        return Duration {
            millis: millis / n;
        }
    }

    public function negate():Duration {
        return Duration {
            millis: -millis;
        }
    }

    public function toString(): String {
        return "{millis}ms";
    }

    public function lt(other: Duration):Boolean {
        return compareTo(other) < 0;
    }

    public function le(other: Duration):Boolean {
        return compareTo(other) <= 0;
    }

    public function gt(other: Duration):Boolean {
        return compareTo(other) > 0;
    }

    public function ge(other: Duration):Boolean {
        return compareTo(other) >= 0;
    }

    public function toDate():java.util.Date {
        return new java.util.Date(millis.longValue());
    }
}
