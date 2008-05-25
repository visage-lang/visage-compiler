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
package javafx.gui.effect;

/**
 * An effect that shifts each pixel according to an (x,y) distance from
 * the (red,green) channels of a map image, respectively.
 */
public class DisplacementMap extends Effect {
    private attribute displace = new com.sun.scenario.effect.DisplacementMap(
        new com.sun.scenario.effect.FloatMap(1, 1),
        new com.sun.scenario.effect.Source(true));
        
    public function getImpl():com.sun.scenario.effect.Effect {
        displace
    };

    /**
     * The input for this {@code Effect}.
     * If left unspecified, the source content will be used as the input.
     */
    public attribute input: Effect = Source { }
        on replace { displace.setContentInput(input.getImpl()); }

    /**
     * The map data for this {@code Effect}.
     */
    public attribute mapData: FloatMap = FloatMap { width: 1, height: 1 }
        on replace { displace.setMapData(mapData.getImpl()); }

    /**
     * The x scale factor.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 1.0
     *  Identity: 1.0
     * </pre>
     */
    public attribute scaleX: Number = 1.0
        on replace { displace.setScaleX(scaleX.floatValue()); }

    /**
     * The y scale factor.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 1.0
     *  Identity: 1.0
     * </pre>
     */
    public attribute scaleY: Number = 1.0
        on replace { displace.setScaleY(scaleY.floatValue()); }

    /**
     * The x offset factor.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 0.0
     *  Identity: 0.0
     * </pre>
     */
    public attribute offsetX: Number = 0.0
        on replace { displace.setOffsetX(offsetX.floatValue()); }

    /**
     * The y offset factor.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 0.0
     *  Identity: 0.0
     * </pre>
     */
    public attribute offsetY: Number = 0.0
        on replace { displace.setOffsetY(offsetY.floatValue()); }

    /**
     * Defines whether values taken from outside the edges of the map
     * "wrap around" or not.
     * <pre>
     *       Min:  n/a
     *       Max:  n/a
     *   Default: false
     *  Identity:  n/a
     * </pre>
     */
    public attribute wrap: Boolean = false
        on replace { displace.setWrap(wrap.booleanValue()); }
}
