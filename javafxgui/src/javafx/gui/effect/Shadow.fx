/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import javafx.gui.Color;

public class Shadow extends Effect {
    private attribute shadow = new com.sun.scenario.effect.Shadow();

    public function getImpl():com.sun.scenario.effect.Effect {
        shadow
    };

    /**
     * The input for this {@code Effect}.
     * If left unspecified, the source content will be used as the input.
     */
    public attribute input: Effect = Source { }
        on replace { shadow.setInput(input.getImpl()); }

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
}