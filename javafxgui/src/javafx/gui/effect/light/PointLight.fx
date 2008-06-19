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
 * Represents a light source at a given position in 3D space.
 */
public class PointLight extends Light {

    private attribute point = createImpl();

    function createImpl():com.sun.scenario.effect.light.PointLight {
        new com.sun.scenario.effect.light.PointLight();
    }

    function getImpl():com.sun.scenario.effect.light.Light {
        point
    }

    /**
     * The x coordinate of the light position.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 0.0
     *  Identity: n/a
     * </pre>
     */
    public attribute x : Number = 0.0
        on replace { point.setX(x.floatValue()); }

    /**
     * The y coordinate of the light position.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 0.0
     *  Identity: n/a
     * </pre>
     */
    public attribute y : Number = 0.0
        on replace { point.setY(y.floatValue()); }

    /**
     * The z coordinate of the light position.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 0.0
     *  Identity: n/a
     * </pre>
     */
    public attribute z : Number = 0.0
        on replace { point.setZ(z.floatValue()); }
}
