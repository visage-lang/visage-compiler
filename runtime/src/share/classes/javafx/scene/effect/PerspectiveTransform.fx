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
 * An effect that provides non-affine transformation of the input content.
 * Most typically {@code PerspectiveTransform} is used to provide a "faux"
 * three-dimensional effect for otherwise two-dimensional content.
 * <p>
 * A perspective transformation is capable of mapping an arbitrary
 * quadrilateral into another arbitrary quadrilateral, while preserving
 * the straightness of lines.  Unlike an affine transformation, the
 * parallelism of lines in the source is not necessarily preserved in the
 * output.
 */
public class PerspectiveTransform extends Effect {
    private attribute xform = new com.sun.scenario.effect.PerspectiveTransform();

    private function updateXform() {
        xform.setQuadMapping(ulx.floatValue(), uly.floatValue(),
                             urx.floatValue(), ury.floatValue(),
                             lrx.floatValue(), lry.floatValue(),
                             llx.floatValue(), lly.floatValue());
    }

    override function getImpl():com.sun.scenario.effect.Effect {
        xform
    };

    /**
     * The input for this {@code Effect}.
     * If left unspecified, the source content will be used as the input.
     */
    public attribute input: Effect = Source { }
        on replace { xform.setInput(input.getImpl()); }

    public attribute ulx:Number = 0.0 on replace { updateXform(); }
    public attribute uly:Number = 0.0 on replace { updateXform(); }
    public attribute urx:Number = 0.0 on replace { updateXform(); }
    public attribute ury:Number = 0.0 on replace { updateXform(); }
    public attribute lrx:Number = 0.0 on replace { updateXform(); }
    public attribute lry:Number = 0.0 on replace { updateXform(); }
    public attribute llx:Number = 0.0 on replace { updateXform(); }
    public attribute lly:Number = 0.0 on replace { updateXform(); }
}
