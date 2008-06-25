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

package javafx.scene.image;

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGImage;
import java.awt.image.BufferedImage;
import javafx.scene.Node;

/**
 * @profile common
 */          
public class ImageView extends Node {

    /**
     * @treatasprivate implementation detail
     */
    public function impl_createSGNode():SGNode { new SGImage() }

    function getSGImage():SGImage { impl_getSGNode() as SGImage }

    // PENDING_DOC_REVIEW
    /**
     * Defines the {@link Image} to be painted by this {@code ImageView}. 
     *
     * @profile common
     */          
    public attribute image:Image;

    /* Note that that using image.getBufferedImage() instead
       of referring to the awtImage attribute fails.
       Note also that if you try and println this.awtImage
       in the bind expression, you get a stack overflow.
    */
    private attribute bufferedImage:BufferedImage = 
        bind if (image <> null) { image.bufferedImage } else { null }
        on replace {
            getSGImage().setImage(bufferedImage);
        }

    private function ul():Void {
        getSGImage().setLocation(new java.awt.geom.Point2D.Double(x, y));
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the current x coordinate of the {@code ImageView} origin.
     *
     * @profile common
     */              
    public attribute x: Number on replace { ul(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the current y coordinate of the {@code ImageView} origin.
     *
     * @profile common
     */              
    public attribute y: Number on replace { ul(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the current height of this {@code ImageView}.
     *
     * @profile common
     */              
    public attribute height:Number;
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the current width of this {@code ImageView}.
     *
     * @profile common
     */              
    public attribute width:Number;
    
    // PENDING_DOC_REVIEW
    /**
     * @profile common
     */              
    public attribute size:Number;

}
