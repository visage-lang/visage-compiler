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

package javafx.scene.effect.light;

/**
 * Represents a distant light source.
 */
public class DistantLight extends Light {

    private attribute distant = new com.sun.scenario.effect.light.DistantLight();

    function getImpl():com.sun.scenario.effect.light.Light {
        distant
    }

    /**
     * The azimuth of the light.  The azimuth is the direction angle
     * for the light source on the XY plane, in degrees.
     * <pre>
     *       Min:  n/a
     *       Max:  n/a
     *   Default: 45.0
     *  Identity:  n/a
     * </pre>
     */
    public attribute azimuth : Number = 45.0
        on replace { distant.setAzimuth(azimuth.floatValue()); }

    /**
     * The elevation of the light.  The elevation is the
     * direction angle for the light source on the YZ plane, in degrees.
     * <pre>
     *       Min:  n/a
     *       Max:  n/a
     *   Default: 45.0
     *  Identity:  n/a
     * </pre>
     */
    public attribute elevation : Number = 45.0
        on replace { distant.setElevation(elevation.floatValue()); }
}
