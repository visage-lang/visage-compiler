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
package javafx.ui;

import java.lang.Math;

public class TimerImpl {


    attribute keyTimes:Number[];
    attribute keySplines:Number[];

    attribute beginPole:Number;
    attribute endPole:Number;
    attribute beginPoleDelta:Number = 0.25;
    attribute endPoleDelta:Number = 0.25;
    attribute primary_K:Number = 1.0;
    public attribute to:Number on replace {
        calcControlValues();
    };
    attribute indirect:Boolean = false;
    public attribute startTime:Integer;
    public attribute duration:Integer on replace {
        calcControlValues();
    };
    attribute type:Integer = EASEBOTH;

    public static attribute LINEAR:Integer = 0;
    public static attribute EASEIN:Integer = 1;
    public static attribute EASEOUT:Integer = 2;
    public static attribute EASEBOTH:Integer = 3;
    public static attribute SPLINE:Integer = 4;
    public static attribute DISCRETE:Integer = 5;

    public function setLinear() {
        type = LINEAR;
    }

    public function setEaseIn() {
        type = EASEIN;
        beginPole = 0.25;
        endPole = 100;
        calcControlValues();
    }

    public function setEaseOut() {
        type = EASEOUT;
        beginPole = 15;
        endPole = 0.25;
        calcControlValues();
    }

    public function setEaseBoth() {
        type = EASEBOTH;
        beginPole = endPole = 0.25;
        calcControlValues();
    }

    public function setSpline() {
        type = SPLINE;
    }

    function calcControlValues():Void {
        var currentValue = 0;
        // create direction multiplier
        var dir = if(this.indirect) then -1.0 else 1.0;
        // set beginPole and endPole values
        if ( currentValue < this.to ) {
            this.beginPole = currentValue - dir*this.beginPoleDelta;
            this.endPole = this.to + dir*this.endPoleDelta;
        } else {
            this.beginPole = currentValue + dir*this.beginPoleDelta;
            this.endPole = this.to - dir*this.endPoleDelta;
        }
        // calculate value for primary_K
        // a default value of 1.0 means the attribute will be static, i.e.
        // the animation will still be calculated but the result will
        // always be the same.
        this.primary_K = 1.0;
        var kN = 1.0*(this.beginPole - this.to )*
            (currentValue - this.endPole);
        var kD = 1.0*(this.beginPole - currentValue)*
            (this.to - this.endPole);
        // NOTE: in future this should probaly check for really small amounts not
        // just zero
        if (kD > 0.0001) this.primary_K = Math.abs(kN/kD);
    }

    public function calcNextValue(timeDifference:Integer, currentValue:Integer):Integer {
        if (type == LINEAR) {
            var elapsed =  timeDifference.doubleValue()/this.duration.doubleValue();
            return (elapsed*this.to.doubleValue()).intValue();
        } else if (type == SPLINE) {
            return calcNextSplineValue(timeDifference, currentValue);
        }
        var nextValue  = currentValue.doubleValue();
        var aEndPole   = this.endPole;
        var aBeginPole = this.beginPole;
        var K = Math.exp((timeDifference*1.0/this.duration.doubleValue())*
                            Math.log(this.primary_K));
        // calculate nextValue using the pole and new K value
        //System.out.println("K="+K);
        if( K <> 1.0 ) {
            var aNumerator   = aBeginPole*aEndPole*(1 - K);
            var aDenominator = aEndPole - K*aBeginPole;
            if( aDenominator <> 0.0 ) nextValue = aNumerator/aDenominator;
        }
        return nextValue.intValue();
    }

    public function calcNextSplineValue(timeDifference:Integer, currentValue:Integer):Integer {
        var elapsed =  timeDifference.doubleValue()/this.duration.doubleValue();
        var keyTime = 0;
        foreach (i in [0..sizeof keyTimes exclusive]) {
            if (elapsed < keyTimes[i]) {
                keyTime = i; // keep going we want the last one
            }
        }
        var j = keyTime*4;
        var x1 = keySplines[j];
        var y1 = keySplines[j+1];
        var x2 = keySplines[j+2];
        var y2 = keySplines[j+3];
        var t1 = keyTimes[keyTime]*this.duration.doubleValue();
        var t2 = elapsed;
        var t = t2 - t1;
        var invT = 1-t;
        var b1 = 3 * t * (invT * invT);
        var b2 = 3 * (t * t) * invT;
        var b3 = t * t * t;
        var result = (b1 * y1) + (b2 * y2) + b3;
        return (result*this.to).intValue();
    }


}
