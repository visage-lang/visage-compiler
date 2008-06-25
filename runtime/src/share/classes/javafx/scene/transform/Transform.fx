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
import javafx.scene.Node;

// PENDING_DOC_REVIEW
/**
 * This class provides static functions to perform rotating, scaling, shearing, 
 * and translation transformations for {@code Affine} objects.
 *
 * <p>Example:</p>
 *
 * <pre><code>Rectangle {
 *  width: 50
 *  height: 50
 *  fill:Color.RED
 *  transform: Transform.rotate(45,0,0); //rotate by 45 degress
 * }
 * </code></pre>
 *
 * @profile common
 * @needsreview josh
 */
public abstract class Transform {

    /**
     * @treatasprivate implementation detail
     */
    public attribute impl_node:Node;
    
    // PENDING_DOC_REVIEW
    /**
     * Constructs an {@code java.awt.geom.AffineTransform} object which performes
     * the actual transformation
     *
     * @return an {@code java.awt.geom.AffineTransform} object which performes
     * the actual transformation 
     * 
     * @treatasprivate implementation detail
     */    
    public abstract function impl_getAffineTransform():AffineTransform;

    // PENDING_DOC_REVIEW
    /**
     * Returns a new {@code Affine} object from 6 number
     * values representing the 6 specifiable entries of the 3x3
     * transformation matrix.
     *
     * @param m00 the X coordinate scaling element of the 3x3 matrix
     * @param m10 the Y coordinate shearing element of the 3x3 matrix
     * @param m01 the X coordinate shearing element of the 3x3 matrix
     * @param m11 the Y coordinate scaling element of the 3x3 matrix
     * @param m02 the X coordinate translation element of the 3x3 matrix
     * @param m12 the Y coordinate translation element of the 3x3 matrix
     * @return a new {@code Affine} object derived from specified parameters 
     *
     * @profile common
     */
    public static function affine(m00:Number, m01:Number, m02:Number, m10:Number, m11:Number, m12:Number):Affine {
        Affine{ m00:m00 m01:m01 m02:m02 m10:m10 m11:m11 m12:m12 }
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns a {@code Translate} object representing a translation transformation.
     * <p>
     * The matrix representing the returned transform is:
     * <pre>
     *		[   1    0    tx  ]
     *		[   0    1    ty  ]
     *		[   0    0    1   ]
     * </pre>
     *
     * @param tx the distance by which coordinates are translated in the
     * X axis direction
     * @param ty the distance by which coordinates are translated in the
     * Y axis direction
     * @return a {@code Translate} object that represents a
     * translation transformation, created with the specified vector.
     *
     * @profile common
     */
    public static function translate(x:Number, y:Number):Translate {
        Translate {x:x y:y}
    }    

    // PENDING_DOC_REVIEW
    /**
     * Returns a {@code Rotate} object that rotates coordinates around an anchor point.
     * This operation is equivalent to translating the coordinates so
     * that the anchor point is at the origin (S1), then rotating them
     * about the new origin (S2), and finally translating so that the
     * intermediate origin is restored to the coordinates of the original
     * anchor point (S3).
     * <p>
     * The matrix representing the returned transform is:
     * <pre>
     *		[   cos(theta)    -sin(theta)    x-x*cos+y*sin  ]
     *		[   sin(theta)     cos(theta)    y-x*sin-y*cos  ]
     *		[       0              0               1        ]
     * </pre>
     * Rotating by a positive angle theta rotates points on the positive
     * X axis toward the positive Y axis.
     *
     * @param theta the angle of rotation measured in radians
     * @param anchorx the X coordinate of the rotation anchor point
     * @param anchory the Y coordinate of the rotation anchor point
     * @return a {@code Rotate} object that rotates coordinates 
     * around the specified point by the specified angle of rotation.
     *     
     * @profile common
     */
    public static function rotate(angle:Number, x:Number, y:Number):Rotate {
        Rotate{ angle:angle x:x y:y }
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns a {@code Scale} object representing a scaling transformation.
     * <p>
     * The matrix representing the returned transform is:
     * <pre>
     *		[   sx   0    0   ]
     *		[   0    sy   0   ]
     *		[   0    0    1   ]
     * </pre>
     *
     * @param sx the factor by which coordinates are scaled along the
     * X axis direction
     * @param sy the factor by which coordinates are scaled along the
     * Y axis direction
     * @return an <code>AffineTransform</code> object that scales 
     * coordinates by the specified factors.
     *
     * @profile common
     */
    public static function scale(x:Number, y:Number):Scale {
        Scale{ x:x y:y }
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns a {@code Shear} object representing a shearing transformation.
     * <p>
     * The matrix representing the returned transform is:
     * <pre>
     *		[   1   shx   0   ]
     *		[  shy   1    0   ]
     *		[   0    0    1   ]
     * </pre>
     *
     * @param shx the multiplier by which coordinates are shifted in the
     * direction of the positive X axis as a factor of their Y coordinate
     * @param shy the multiplier by which coordinates are shifted in the
     * direction of the positive Y axis as a factor of their X coordinate
     * @return an <code>AffineTransform</code> object that shears 
     * coordinates by the specified multipliers.
     *
     * @profile common
     */
    public static function shear(x:Number, y:Number):Shear {
        Shear{ x:x y:y }
    }

}
