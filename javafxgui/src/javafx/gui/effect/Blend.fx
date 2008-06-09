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

/**
 * An effect that blends the two inputs together using one of the
 * pre-defined {@code BlendMode}s.
 */
public class Blend extends Effect {
    private attribute blend = new com.sun.scenario.effect.Blend(
        BlendMode.SRC_OVER.toolkitValue,
        new com.sun.scenario.effect.Source(true),
        new com.sun.scenario.effect.Source(true));
        
    function getImpl():com.sun.scenario.effect.Effect {
        blend
    };

    /**
     * The {@code BlendMode} used to blend the two inputs together.
     * <pre>
     *       Min: n/a
     *       Max: n/a
     *   Default: BlendMode.SRC_OVER
     *  Identity: n/a
     * </pre>
     */
    public attribute mode: BlendMode = BlendMode.SRC_OVER
        on replace { blend.setMode(mode.toolkitValue); }

    /**
     * The opacity value, which is modulated with the top input prior
     * to blending.
     * <pre>
     *       Min: 0.0
     *       Max: 1.0
     *   Default: 1.0
     *  Identity: 1.0
     * </pre>
     */
    public attribute opacity: Number = 1
        on replace { blend.setOpacity(opacity.floatValue()); }

    /**
     * The bottom input for this {@code Blend} operation.
     * If left unspecified, the source content will be used as the input.
     */
    public attribute bottomInput: Effect = Source { }
        on replace { blend.setBottomInput(bottomInput.getImpl()); }

    /**
     * The top input for this {@code Blend} operation.
     * If left unspecified, the source content will be used as the input.
     */
    public attribute topInput: Effect = Source { }
        on replace { blend.setTopInput(topInput.getImpl()); }
}
