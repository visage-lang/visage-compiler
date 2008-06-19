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
 * An effect that returns a mask that is the inverse of the input (i.e.,
 * opaque areas of the input become transparent and vice versa).
 */
public class InvertMask extends Effect {
    private attribute invert = new com.sun.scenario.effect.InvertMask();

    function getImpl():com.sun.scenario.effect.Effect {
        invert
    };

    /**
     * The input for this {@code Effect}.
     * If left unspecified, the source content will be used as the input.
     */
    public attribute input: Effect = Source { }
        on replace { invert.setInput(input.getImpl()); }

    /**
     * The amount of padding added to each side of the resulting
     * image, in pixels.
     * <pre>
     *       Min:  0.0
     *       Max:  n/a
     *   Default: 10.0
     *  Identity:  0.0
     * </pre>
     */
    public attribute pad: Number = 10
        on replace { invert.setPad(pad.intValue()); }
}
