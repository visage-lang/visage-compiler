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

package javafx.scene.effect;

import javafx.scene.paint.Color;

/**
 * A high-level effect that renders a shadow inside the edges of the
 * given content with the specified color, radius, and offset.
 */
public class InnerShadow extends Effect {
    private attribute shadow = new com.sun.scenario.effect.InnerShadow();

    /**
     * @treatasprivate implementation detail.
     */
    override function impl_getImpl(): com.sun.scenario.effect.Effect {
        shadow
    };

    /**
     * The radius of the shadow blur kernel.
     * <pre>
     *       Min:  1.0
     *       Max: 63.0
     *   Default: 10.0
     *  Identity:  n/a
     * </pre>
     */
    public attribute radius: Number = 10
        on replace { shadow.setRadius(radius.floatValue()); }

    /**
     * The shadow {@code Color}.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: Color.BLACK
     *  Identity: n/a
     * </pre>
     */
    public attribute color: Color = Color.BLACK
        on replace { shadow.setColor(color.getAWTColor()); }

    /**
     * The shadow offset in the x direction, in pixels.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 0.0
     *  Identity: 0.0
     * </pre>
     */
    public attribute offsetX: Number = 0
        on replace { shadow.setOffsetX(offsetX.intValue()); }

    /**
     * The shadow offset in the y direction, in pixels.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 0.0
     *  Identity: 0.0
     * </pre>
     */
    public attribute offsetY: Number = 0
        on replace { shadow.setOffsetY(offsetY.intValue()); }
}
