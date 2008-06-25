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
 * This class represents an {@code Affine} object that scales coordinates 
 * by the specified factors. The matrix representing the scaling transformation 
 * is as follows:
 * <pre>
 *		[   sx   0    0   ]
 *		[   0    sy   0   ]
 *		[   0    0    1   ]
 * </pre>
 *
 * @profile common
 */
public class Scale extends Transform {

    /**
     * @treatasprivate implementation detail
     */
    public function impl_getAffineTransform():AffineTransform {
        AffineTransform.getScaleInstance(x, y)
    }

    private function u():Void {
        if (impl_node <> null) {
            impl_node.impl_updateFXNodeTransform();
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the factor by which coordinates are scaled 
     * along the X axis direction. The default value is {@code 1.0}.
     *
     * @profile common
     */
    public attribute x: Number = 1.0 on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the factor by which coordinates are scaled 
     * along the Y axis direction. The default value is {@code 1.0}.
     *
     * @profile common
     */
    public attribute y: Number = 1.0 on replace { u(); }

}
