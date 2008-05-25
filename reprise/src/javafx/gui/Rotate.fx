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
package javafx.gui;

import java.lang.Math;
import java.awt.geom.AffineTransform;

// PENDING_DOC_REVIEW
/**
 * This class represents an {@code Affine} object that rotates coordinates 
 * around an anchor point. This operation is equivalent to translating the  
 * coordinates so that the anchor point is at the origin (S1), then rotating them  
 * about the new origin (S2), and finally translating so that the
 * intermediate origin is restored to the coordinates of the original
 * anchor point (S3).
 * <p/>
 * The matrix representing the returned transform is:
 * <pre>
 *		[   cos(theta)    -sin(theta)    x-x*cos+y*sin  ]
 *		[   sin(theta)     cos(theta)    y-x*sin-y*cos  ]
 *		[       0              0               1        ]
 * </pre>
 * 
 * @profile common
 */
public class Rotate extends Transform {

    function getAffineTransform():AffineTransform {
        AffineTransform.getRotateInstance(Math.toRadians(angle), x, y);
    }

    private function u():Void {
        if (node <> null) {
            node.updateFXNodeTransform();
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the angle of rotation measured in degrees.
     *
     * @profile common
     */
    public attribute angle: Number = 0.0 on replace { u(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the rotation anchor point.
     * 
     * @profile common
     */
    public attribute x: Number = 0.0 on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the rotation anchor point.
     * 
     * @profile common
     */
    public attribute y: Number = 0.0 on replace { u(); }

}
