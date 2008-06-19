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

package javafx.gui.effect;

/**
 * A buffer that contains floating point data, intended for use as a parameter
 * to effects such as {@code DisplacementMap}.
 */
public class FloatMap {
    private attribute map: com.sun.scenario.effect.FloatMap;

    function getImpl() : com.sun.scenario.effect.FloatMap {
        map;
    }

    private function update() {
        if (width > 0 and height > 0) {
            map = new com.sun.scenario.effect.FloatMap(width, height);
        }
    }

    /**
     * The width of the map, in pixels.
     * <pre>
     *       Min:    1
     *       Max: 4096
     *   Default:    1
     *  Identity:  n/a
     * </pre>
     */
    public attribute width: Integer = 1
        on replace { update(); }

    /**
     * The height of the map, in pixels.
     * <pre>
     *       Min:    1
     *       Max: 4096
     *   Default:    1
     *  Identity:  n/a
     * </pre>
     */
    public attribute height: Integer = 1
        on replace { update(); }

    /**
     * Sets the sample for a specific band at the given (x,y) location.
     * 
     * @param x the x location
     * @param y the y location
     * @param band the band to set (must be 1, 2, 3, or 4)
     * @param sample the sample value to set
     */
    public function setSample(x: Integer, y: Integer, band: Integer, s: Number) {
        map.setSample(x, y, band, s.floatValue());
    }

    /**
     * Sets the sample for the first band at the given (x,y) location.
     * 
     * @param x the x location
     * @param y the y location
     * @param s0 the sample value to set for the first band
     */
    public function setSamples(x: Integer, y: Integer,
                               s0: Number)
    {
        map.setSamples(x, y, s0.floatValue());
    }

    /**
     * Sets the sample for the first two bands at the given (x,y) location.
     * 
     * @param x the x location
     * @param y the y location
     * @param s0 the sample value to set for the first band
     * @param s1 the sample value to set for the second band
     */
    public function setSamples(x: Integer, y: Integer,
                               s0: Number, s1: Number)
    {
        map.setSamples(x, y, s0.floatValue(), s1.floatValue());
    }

    /**
     * Sets the sample for the first three bands at the given (x,y) location.
     * 
     * @param x the x location
     * @param y the y location
     * @param s0 the sample value to set for the first band
     * @param s1 the sample value to set for the second band
     * @param s2 the sample value to set for the third band
     */
    public function setSamples(x: Integer, y: Integer,
                               s0: Number, s1: Number, s2: Number)
    {
        map.setSamples(x, y, s0.floatValue(), s1.floatValue(), s2.floatValue());
    }

    /**
     * Sets the sample for each of the four bands at the given (x,y) location.
     * 
     * @param x the x location
     * @param y the y location
     * @param s0 the sample value to set for the first band
     * @param s1 the sample value to set for the second band
     * @param s2 the sample value to set for the third band
     * @param s3 the sample value to set for the fourth band
     */
    public function setSamples(x: Integer, y: Integer,
                               s0: Number, s1: Number, s2: Number, s3: Number)
    {
        map.setSamples(x, y, s0.floatValue(), s1.floatValue(), s2.floatValue(), s3.floatValue());
    }
}
