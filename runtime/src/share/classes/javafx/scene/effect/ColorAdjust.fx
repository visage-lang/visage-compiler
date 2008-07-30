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

/**
 * An effect that allows for per-pixel adjustments of hue, saturation,
 * brightness, and contrast.
 */
public class ColorAdjust extends Effect {
    private attribute adjust = new com.sun.scenario.effect.ColorAdjust();

    override function getImpl():com.sun.scenario.effect.Effect {
        adjust
    };

    /**
     * The input for this {@code Effect}.
     * If left unspecified, the source content will be used as the input.
     */
    public attribute input: Effect = Source { }
        on replace { adjust.setInput(input.getImpl()); }

    /**
     * The hue adjustment value.
     * <pre>
     *       Min: -1.0
     *       Max: +1.0
     *   Default:  0.0
     *  Identity:  0.0
     * </pre>
     */
    public attribute hue: Number = 0
        on replace { adjust.setHue(hue.floatValue()); }

    /**
     * The saturation adjustment value.
     * <pre>
     *       Min: -1.0
     *       Max: +1.0
     *   Default:  0.0
     *  Identity:  0.0
     * </pre>
     */
    public attribute saturation: Number = 0
        on replace { adjust.setSaturation(saturation.floatValue()); }

    /**
     * The brightness adjustment value.
     * <pre>
     *       Min: -1.0
     *       Max: +1.0
     *   Default:  0.0
     *  Identity:  0.0
     * </pre>
     */
    public attribute brightness: Number = 0
        on replace { adjust.setBrightness(brightness.floatValue()); }

    /**
     * The contrast adjustment value.
     * <pre>
     *       Min: 0.25
     *       Max: 4.00
     *   Default: 1.00
     *  Identity: 1.00
     * </pre>
     */
    public attribute contrast: Number = 1
        on replace { adjust.setContrast(contrast.floatValue()); }
}
