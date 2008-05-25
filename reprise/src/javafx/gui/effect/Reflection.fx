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
 * An effect that renders a reflected version of the input below the
 * actual input content.
 */
public class Reflection extends Effect {
    private attribute reflect = new com.sun.scenario.effect.Reflection();

    public function getImpl():com.sun.scenario.effect.Effect {
        reflect
    };

    /**
     * The input for this {@code Effect}.
     * If left unspecified, the source content will be used as the input.
     */
    public attribute input: Effect = Source { }
        on replace { reflect.setInput(input.getImpl()); }

    /**
     * The top offset adjustment, which is the distance between the
     * bottom of the input and the top of the reflection.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: 0.0
     *  Identity: 0.0
     * </pre>
     */
    public attribute topOffset: Number = 0
        on replace { reflect.setTopOffset(topOffset.floatValue()); }

    /**
     * The top opacity value, which is the opacity of the reflection
     * at its top extreme.
     * <pre>
     *       Min: 0.0
     *       Max: 1.0
     *   Default: 0.5
     *  Identity: 1.0
     * </pre>
     */
    public attribute topOpacity: Number = 0.5
        on replace { reflect.setTopOpacity(topOpacity.floatValue()); }

    /**
     * The bottom opacity value, which is the opacity of the reflection
     * at its bottom extreme.
     * <pre>
     *       Min: 0.0
     *       Max: 1.0
     *   Default: 0.0
     *  Identity: 1.0
     * </pre>
     */
    public attribute bottomOpacity: Number = 0
        on replace { reflect.setBottomOpacity(bottomOpacity.floatValue()); }

    /**
     * The fraction of the input that is visible in the reflection.
     * For example, a value of 0.5 means that only the bottom half of the
     * input will be visible in the reflection.
     * <pre>
     *       Min: 0.0
     *       Max: 1.0
     *   Default: 0.75
     *  Identity: 1.0
     * </pre>
     */
    public attribute fraction: Number = 0.75
        on replace { reflect.setFraction(fraction.floatValue()); }
}
