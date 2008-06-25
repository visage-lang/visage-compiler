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

// PENDING_DOC_REVIEW_2
/**
 * <p>
 * The {@code Affine} class represents an affine transform. An affine transform performs 
 * a linear mapping from 2D coordinates to other 2D coordinates 
 * while preserving the "straightness" and "parallelness" of lines. 
 * Affine transformations can be constructed 
 * using sequences translations, scales, and shears.</p>
 *
 * <p><b>
 * Note: application developers should not normally use this class directly, but instead use
 * the specific {@code Translate}, {@code Scale}, {@code Rotate}, or {@code Shear}
 * transforms instead.</b></p>
 
 * <p>
 * Such a coordinate transformation can be represented by a 3 row by
 * 3 column matrix with an implied last row of [ 0 0 1 ].  This matrix 
 * transforms source coordinates {@code (x,y)} into
 * destination coordinates {@code (x',y')} by considering
 * them to be a column vector and multiplying the coordinate vector
 * by the matrix according to the following process:</p>
 * 
 * <pre>
 *	[ x']   [  m00  m01  m02  ] [ x ]   [ m00x + m01y + m02 ]
 *	[ y'] = [  m10  m11  m12  ] [ y ] = [ m10x + m11y + m12 ]
 *	[ 1 ]   [   0    0    1   ] [ 1 ]   [         1         ]
 * </pre>
 *
 * @profile common
 * @needsreview
 */ 
public class Affine extends Transform {

    /**
     * @treatasprivate implementation detail
     */
    public function impl_getAffineTransform():AffineTransform {
        new AffineTransform(m00, m01, m02, m10, m11, m12)
    }

    private function u():Void {
        if (impl_node <> null) {
            impl_node.impl_updateFXNodeTransform();
        }
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate scaling element of the 3x3 matrix. 
     *
     * @profile common
     * @defaultvalue 1.0
     */ 
    public attribute m00:Number = 1.0 on replace { u(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate shearing element of the 3x3 matrix. 
     * 
     * @profile common
     * @defaultvalue 0.0
     */     
    public attribute m01:Number = 0.0 on replace { u(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate translation element of the 3x3 matrix. 
     *
     * @profile common
     * @defaultvalue 0.0
     */     
    public attribute m02:Number = 0.0 on replace { u(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate shearing element of the 3x3 matrix.
     *
     * @profile common
     * @defaultvalue 0.0
     */     
    public attribute m10:Number = 0.0 on replace { u(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate scaling element of the 3x3 matrix.
     *
     * @profile common
     * @defaultvalue 1.0
     */     
    public attribute m11:Number = 1.0 on replace { u(); }
    
     // PENDING_DOC_REVIEW
     /**
     * Defines the Y coordinate translation element of the 3x3 matrix.
     *
     * @profile common
     * @defaultvalue 0.0
     */     
    public attribute m12:Number = 0.0 on replace { u(); }

}
