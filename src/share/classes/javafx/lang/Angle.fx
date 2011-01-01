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
import java.lang.IllegalStateException;
import java.lang.Math;
import java.lang.ThreadLocal;
import java.text.DecimalFormat;
import java.util.Formattable;
import javafx.util.Bits;
import java.util.Formatter;
import java.util.FormattableFlags;
import java.lang.StringBuilder;

/**
 * Used to specify a angle of 0ms.
 *
 * @profile common
 */
public def ZERO:Angle = Angle {value: 0, unit: AngleUnit.RADIAN}

def DF = ThreadLocal {
    override function initialValue():Object {
        return new DecimalFormat("#.######");
    }
}

// script-level "static" functions below

/**
 * Factory method that returns a Angle instance for a specified
 * value and unit.
 *
 * @param value the magnitude of the angle
 * @param unit the unit of the angle
 * @return a Angle instance of the specified value and unit type
 * @profile common
 */
public function valueOf(value:Double, unit:AngleUnit):Angle {
    if (value == 0)
        ZERO
    else Angle {
        value: value
        unit: unit
    }
}

public function valueOf(value:Float, unit:AngleUnit):Angle {
    valueOf(value as Double, unit)
}

public def TYPE_INFO = com.sun.javafx.runtime.TypeInfo.makeAndRegisterTypeInfo(ZERO);

/**
 * A class that defines an angle measurement.  Angle instances can be specified
 * as degrees, radians, or turns.  For precise calculations, they are stored
 * in the given unit type as a double value.
 * <p>
 * Angles can be easily created using angle literals; for
 * example, a 90 degree Angle instance can be defined in several
 * ways:
 * <code><pre>    Angle a = 90deg;
    Angle a = .25turn;
    Angle a = Angle.valueOf(Math.PI / 2, AngleUnit.RADIAN);</pre></code>
 * Angle instances are immutable, and are therefore replaced rather than modified.
 * To create a new Angle instance, either use a time literal, or use the
 * <code>Angle.valueOf(value, unit)</code> factory method.
 *
 * @profile common
 */
public class Angle extends Comparable, Formattable {

    var value:Double;
    var unit:AngleUnit;

    /**
     * Returns the number of degrees in this angle.
     *
     * @profile common
     */
    public function toDegrees():Double {
        if (unit == AngleUnit.DEGREE) {
            return value;
        } else if (unit == AngleUnit.RADIAN) {
            return value * 180 / Math.PI;
        } else if (unit == AngleUnit.TURN) {
            return value * 360;
        } else {
            throw new IllegalStateException("Unknown unit: {unit}");
        }
    }

    /**
     * Returns a new Length object with an angle in degrees.
     *
     * @profile common
     */
    public function toDegreeAngle():Angle {
        if (this == ZERO) this;
        Angle {
            value: toDegrees();
            unit: AngleUnit.DEGREE;
        }
    }
    
    /**
     * Returns the number of radians in this angle.
     *
     * @profile common
     */
    public function toRadians():Number {
        if (unit == AngleUnit.RADIAN) {
            return value;
        } else if (unit == AngleUnit.DEGREE) {
            return value * Math.PI / 180;
        } else if (unit == AngleUnit.TURN) {
            return value * 2 * Math.PI;
        } else {
            throw new IllegalStateException("Unknown unit: {unit}");
        }
    }
    
    /**
     * Returns a new Length object with an angle in radians.
     *
     * @profile common
     */
    public function toRadianAngle():Angle {
        if (this == ZERO) this;
        Angle {
            value: toRadians();
            unit: AngleUnit.RADIAN;
        }
    }

    /**
     * Returns the number of turns in this angle.
     *
     * @profile common
     */
    public function toTurns():Number {
        if (unit == AngleUnit.TURN) {
            return value;
        } else if (unit == AngleUnit.DEGREE) {
            return value / 360;
        } else if (unit == AngleUnit.RADIAN) {
            return value / 2 / Math.PI;
        } else {
            throw new IllegalStateException("Unknown unit: {unit}");
        }
    }

    /**
     * Returns a new Length object with an angle in turns.
     *
     * @profile common
     */
    public function toTurnAngle():Angle {
        if (this == ZERO) this;
        Angle {
            value: toTurns();
            unit: AngleUnit.TURN;
        }
    }

    /**
     * Add this instance and another Angle instance to return a new Angle instance.
     * This function does not change the value of the called Angle instance.
     *
     * @profile common
     */
    public function add(other:Angle):Angle {
        if (unit == other.unit) {
            return valueOf(value + other.value, unit);
        } else {
            return valueOf(toRadians() + other.toRadians(), AngleUnit.RADIAN);
        }
    }

    /**
     * Subtract other Angle instance from this instance to return a new Angle instance.
     * This function does not change the value of the called Angle instance.
     *
     * @profile common
     */
    public function sub(other:Angle):Angle {
        if (unit == other.unit) {
            return valueOf(value - other.value, unit);
        } else {
            return valueOf(toRadians() - other.toRadians(), AngleUnit.RADIAN);
        }
    }

    /**
     * Multiply this instance with a number to return a new Angle instance.
     * This function does not change the value of the called Angle instance.
     *
     * @profile common
     */ 
    public function mul(n:Number):Angle {
        return valueOf(value * n, unit);
    }


    /**
     * Divide this instance by a number to return a new Angle instance.
     * This function does not change the value of the called Angle instance.
     *
     * @profile common
     */     
    public function div(n:Number):Angle {
        if (n == 0) {
            throw new java.lang.ArithmeticException("/ by zero");
        }
        
        return valueOf(value / n, unit);
    }

    /**
     * Divide this instance by another Angle to return the ratio.
     * This function does not change the value of the called Angle instance.
     *
     * @profile common
     */     
    public function div(other:Angle):Double {
        if (other.value == 0) {
            throw new java.lang.ArithmeticException("/ by zero");
        }
        if (unit == other.unit) {
            return value / other.value;
        } else {
            return toRadians() / other.toRadians();
        }
    }

    /** 
     * Return a new Angle instance which has a negative number of milliseconds
     * from this instance.  For example, <code>(50deg).negate()</code> returns
     * a Angle of -50 degrees.
     * This function does not change the value of the called Angle instance.
     *
     * @profile common
     */
    public function negate():Angle {
        return valueOf(-value, unit);
    }

    /**
     * Prints out a string representation of the length with a precision of
     * up to 6 digits (internally angles are represented as doubles with much
     * higher precision).
     *
     * @profile common
     */        
    override function toString():String {
        def df = DF.get() as DecimalFormat;
        df.setMinimumFractionDigits(0);
        return "{df.format(value)}{unit.getSuffix()}";
    }

    /**
     * Formatting function that allows angles to be properly formatted by
     * printf, String.format, or the JavaFX format primitive.
     * <p>
     * Width sets the minimum width of this field, following the justification
     * rules set in flags.
     * <p>
     * Precision can be used to specify exactly how many digits should appear
     * to the right of the decimal, including zero padding.
     * <p>
     * The alternate form is not supported for angles.
     * <p>
     * Finally, the uppercase flag is also supported, but only applies to the
     * length suffix.
     */
    override function formatTo(formatter:Formatter, flags:Integer, width:Integer, precision:Integer):Void {
        def uppercase = Bits.bitAnd(flags, FormattableFlags.UPPERCASE) != 0;
        def leftJustify = Bits.bitAnd(flags, FormattableFlags.LEFT_JUSTIFY) != 0;
        def sb = StringBuilder {};
        def df = DF.get() as DecimalFormat;
        df.setMinimumFractionDigits(precision);
        sb.append(df.format(value));
        sb.append(if (uppercase) unit.getSuffix().toUpperCase() else unit.getSuffix());
        def len = sb.length();
        if (len < width) {
            if (leftJustify) {
                for (i in [1..width-len]) sb.append(' ');
            } else {
                for (i in [1..width-len]) sb.<<insert>>(0, ' ');
            }
        }
        formatter.out().append(sb);
    }

    /**
     * Returns true if the specified angle is less than (<) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @profile common
     */
    public function lt(other:Angle):Boolean {
        return compareTo(other) < 0;
    }

    /** 
     * Returns true if the specified angle is less than or equal to (<=) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @profile common
     */
    public function le(other:Angle):Boolean {
        return compareTo(other) <= 0;
    }

    /** 
     * Returns true if the specified angle is greater than (>) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @profile common
     */
    public function gt(other:Angle):Boolean {
        return compareTo(other) > 0;
    }

    /**
     * Returns true if the specified angle is greater than or equal to (>=) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @profile common
     */
    public function ge(other:Angle):Boolean {
        return compareTo(other) >= 0;
    }

    override function compareTo(obj:Object):Integer {
        def a = obj as Angle;
        if (a.value == value and a.unit == unit) {
            return 0;
        }
        def cmp = toRadians() - a.toRadians();
        return if (cmp < 0)
             -1
        else if(cmp > 0)
             1
        else
             0
    }

    override function equals(obj:Object):Boolean {
        if (obj instanceof Angle) {
            def a = obj as Angle;
            if (isSameObject(a, this)) {
                return true;
            }
            return a.toRadians() == toRadians();
        }
        return false;
    }

    override function hashCode():Integer {
        var v = value as Long;
        v = Bits.bitXor(v, unit.ordinal());
        return Bits.bitXor(v, Bits.unsignedShiftRight(v, 32)) as Integer;
    }
}
