/*
 * Copyright 1999-2005 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.ui;

public class TimerImpl {

    float[] keyTimes;
    float[] keySplines;

    double beginPole;
    double endPole;
    double beginPoleDelta = 0.25;
    double endPoleDelta = 0.25;
    double primary_K = 1.0;
    public double to;
    boolean indirect = false;
    public long startTime;
    public int duration;
    int type = EASEBOTH;

    final static int LINEAR = 0;
    final static int EASEIN = 1;
    final static int EASEOUT = 2;
    final static int EASEBOTH = 3;
    final static int SPLINE = 4;
    final static int DISCRETE = 5;

    public void setLinear() {
        type = LINEAR;
    }

    public void setEaseIn() {
        type = EASEIN;
        beginPole = 0.25;
        endPole = 100;
        calcControlValues();
    }

    public void setEaseOut() {
        type = EASEOUT;
        beginPole = 15;
        endPole = 0.25;
        calcControlValues();
    }

    public void setEaseBoth() {
        type = EASEBOTH;
        beginPole = endPole = 0.25;
        calcControlValues();
    }

    public void setSpline() {
        type = SPLINE;
    }


    public TimerImpl(long startTimeMillis, int durationMillis, int count) {
        this.startTime = startTimeMillis;
        this.duration = durationMillis;
        this.to = count;
        calcControlValues();
    }

    public void setTo(int count) {
        this.to = count;
        calcControlValues();
    }

    public void setStartTime(long t) {
        startTime = t;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setDuration(int dur) {
        this.duration = dur;
        calcControlValues();
    }
    
    public int getDuration() {
        return duration;
    }
    
    void calcControlValues() {
        int currentValue = 0;
        // create direction multiplier
        double dir = this.indirect ? -1.0 : 1.0;
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
        double kN = 1.0*(this.beginPole - this.to )*
            (currentValue - this.endPole);
        double kD = 1.0*(this.beginPole - currentValue)*
            (this.to - this.endPole);
        // NOTE: in future this should probaly check for really small amounts not
        // just zero
        if (kD > 0.0001) this.primary_K = Math.abs(kN/kD);
    }
    
    public int calcNextValue(int timeDifference, int currentValue) {
        if (type == LINEAR) {
            double elapsed = (double)timeDifference/(double)this.duration;
            int result = (int)(elapsed*(double)this.to);
            return result;
        } else if (type == SPLINE) {
            return calcNextSplineValue(timeDifference, currentValue);
        }
        double nextValue  = currentValue;
        double aEndPole   = this.endPole;
        double aBeginPole = this.beginPole;
        double K = Math.exp((timeDifference*1.0/(double)this.duration)*
                            Math.log(this.primary_K));
        // calculate nextValue using the pole and new K value
        //System.out.println("K="+K);
        if( K != 1.0 ) {
            double aNumerator   = aBeginPole*aEndPole*(1 - K);
            double aDenominator = aEndPole - K*aBeginPole;
            if( aDenominator != 0.0 ) nextValue = aNumerator/aDenominator;
        }
        return (int)nextValue;
    }

    public int calcNextSplineValue(int timeDifference, int currentValue) {
        float elapsed = (float)timeDifference/(float)this.duration;
        int keyTime = 0;
        for (int i = keyTimes.length-1; i > 0; i--) {
            if (elapsed < keyTimes[i]) {
                keyTime = i;
                break;
            }
        }
        int j = keyTime*4;
        float x1 = keySplines[j];
        float y1 = keySplines[j+1];
        float x2 = keySplines[j+2];
        float y2 = keySplines[j+3];
        float t1 = keyTimes[keyTime]*(float)this.duration;
        float t2 = elapsed;
        float t = t2 - t1;
        float invT = 1-t;
        float b1 = 3 * t * (invT * invT);
        float b2 = 3 * (t * t) * invT;
        float b3 = t * t * t;
        float result = (b1 * y1) + (b2 * y2) + b3;
        return (int)(result*this.to);
    }

    public void setKeyTimes(float[] f) {
        keyTimes = f;
    }

    public void setKeySplines(float[] f) {
        keySplines = f;
    }
}

    