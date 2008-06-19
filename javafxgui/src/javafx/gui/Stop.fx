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

package javafx.gui;

/**
 * <p>Defines one element of the ramp of colors to use on a gradient. 
 * For more information see {@code javafx.gui.LinearGradient} and 
 * {@code javafx.gui.RadialGradient}.</p>
 *
 * <p>Example:</p>
 * <pre><code>
 * // object bounding box relative (proportional:true, default)
 * Rectangle {
 *     x: 0 y: 0 width: 100 height: 100
 *     fill: LinearGradient {
 *         startX: 0.0
 *         startY: 0.0
 *         endX: 1.0
 *         endY: 0.0
 *         stops: [
 *            <b>Stop { offset: 0.0 color: Color.BLACK },</b>
 *            <b>Stop { offset: 1.0 color: Color.RED }</b>
 *         ]
 *     }
 * }
 *</code></pre>
 *
 * @needsreview josh
 * @profile common
 */
public class Stop {

    /** 
     * The {@code offset} attribute is a number ranging from {@code 0} to {@code 1}
     * which indicates where this gradient stop is placed. For linear gradients,
     * the {@code offset} attribute represents a location along the gradient vector.
     * For radial gradients, it represents a percentage distance from
     * {@code (focusX,focusY)} to the edge of the outermost/largest circle.
     * @profile common
     */
    public attribute offset: Number;

    /** 
     * The color of the gradient at this offset.
     * @profile common
     */
    public attribute color: Color;

}
