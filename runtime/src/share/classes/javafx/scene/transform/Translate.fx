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

package javafx.scene.transform;

import java.awt.geom.AffineTransform;

// PENDING_DOC_REVIEW
/**
 * <p>This class represents an {@code Affine} object that translates coordinates 
 * by the specified factors. Normally application developers will use the
 * {@code Transform.translate} function instead of accessing this class
 * directly.</p>
 
 * <p>The matrix representing the translating transformation 
 * is as follows:</p>
 * <pre>
 *		[   1    0    tx  ]
 *		[   0    1    ty  ]
 *		[   0    0    1   ]
 * </pre>
 *
 * @profile common
 * @needsreview josh
 */
public class Translate extends Transform {

    /**
     * @treatasprivate implementation detail
     */
    override function impl_getAffineTransform():AffineTransform {
        AffineTransform.getTranslateInstance(x, y)
    }

    private function u():Void {
        if (impl_node != null) {
            impl_node.impl_updateFXNodeTransform();
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the distance by which coordinates are translated in the
     * X axis direction
     *
     * @profile common
     */
    public attribute x: Number = 0.0 on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the distance by which coordinates are translated in the
     * Y axis direction
     *
     * @profile common
     */
    public attribute y: Number = 0.0 on replace { u(); }

}
