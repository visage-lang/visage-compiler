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
package javafx.gui.effect.light;

/**
 * Represents a spot light source at a given position in 3D space, with
 * configurable direction and focus.
 */
public class SpotLight extends PointLight {

    function createImpl():com.sun.scenario.effect.light.PointLight {
        new com.sun.scenario.effect.light.SpotLight();
    }

    private function getSpot():com.sun.scenario.effect.light.SpotLight {
        getImpl() as com.sun.scenario.effect.light.SpotLight;
    }

    /**
     * The x coordinate of the direction vector for this light.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 0.0
     *  Identity: n/a
     * </pre>
     */
    public attribute pointsAtX : Number = 0.0
        on replace { getSpot().setPointsAtX(pointsAtX.floatValue()); }

    /**
     * The y coordinate of the direction vector for this light.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 0.0
     *  Identity: n/a
     * </pre>
     */
    public attribute pointsAtY : Number = 0.0
        on replace { getSpot().setPointsAtY(pointsAtY.floatValue()); }

    /**
     * The z coordinate of the direction vector for this light.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 0.0
     *  Identity: n/a
     * </pre>
     */
    public attribute pointsAtZ : Number = 0.0
        on replace { getSpot().setPointsAtZ(pointsAtZ.floatValue()); }

    /**
     * The specular exponent, which controls the focus of this
     * light source.
     * <pre>
     *       Min: 0.0
     *       Max: 4.0
     *   Default: 1.0
     *  Identity: 1.0
     * </pre>
     */
    public attribute specularExponent : Number = 1.0
        on replace { getSpot().setSpecularExponent(specularExponent.floatValue()); }
}
