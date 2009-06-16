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

package com.sun.javafx.runtime;

/**
 * Primitive class to support javafx.lang.Duration
 *
 * @author Tom Ball
 */
public class Duration implements Comparable {
    protected double millis;
    // this is a singleton Duration that is of indefinite length.
    // It is treated as positive infinity for comparison purposes.
    protected static Duration indefinite;
    private static Duration ZERO;
    private static Duration ONE;
    private static Class<?> fxClass;

    static {
        try {
            fxClass = Class.forName("javafx.lang.Duration");
            ZERO = (Duration) realMake(0);
            ONE = (Duration) realMake(1);
            indefinite = (Duration)realMake(Double.POSITIVE_INFINITY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setMillis(double millis) {
        this.millis = millis;
    }

    protected boolean isIndefinite() {
        return this == indefinite;
    }

    /** hack to workaround issue with static functions in FX that invoke an instance of their class. */
    protected static Object make(double ms) throws Exception {
        if (ms == 0) {
            return ZERO;
        }
        if (ms == 1) {
            return ONE;
        }
        if (ms == Double.POSITIVE_INFINITY) {
            return indefinite;
        }
        return realMake(ms);
    }

    private static Object realMake(double ms) throws Exception {
        Duration dur = (Duration)fxClass.newInstance();
        dur.millis = ms;
        return dur;
    }

    @Override
    public int compareTo(Object obj) {
        Duration d = (Duration)obj;
        if (this == d) {
            return 0;
        }
        if (d.isIndefinite()) {
            return -1;
        }
        if (isIndefinite()) {
            return 1;
        }
        double cmp = millis - d.millis;
        return cmp < 0 ? -1 : cmp > 0 ? 1 : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Duration) {
            Duration d = (Duration)obj;
            if (this == d) {
                return true;
            }
            if (isIndefinite() || d.isIndefinite()) {
                return false;
            }
            return d.millis == millis;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this == indefinite) {
            // some unlikely value
            return Integer.MIN_VALUE + 89;
        }
        long value = (long)millis;            // from java.lang.Double.longValue()
        return (int)(value ^ (value >>> 32)); // from java.lang.Long.hashCode()
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
