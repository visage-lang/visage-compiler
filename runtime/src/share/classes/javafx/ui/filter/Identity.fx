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

package javafx.ui.filter;
import java.awt.image.BufferedImageOp;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;


public class Identity extends Filter {
    public function getFilter(): BufferedImageOp {
        BufferedImageOp { 
            public function filter( src:BufferedImage, dest:BufferedImage):BufferedImage {
                return src; 
            } 
            public function  getBounds2D(src:BufferedImage):Rectangle2D {   
                 return null; // do nothing
            }
            public function  createCompatibleDestImage(src:BufferedImage,
                                            destCM:ColorModel):BufferedImage {
                 return null; // do nothing
            }
            public function  getPoint2D(srcPt:Point2D, dstPt:Point2D):Point2D {
                 return null; // do nothing
            }
            public function getRenderingHints():RenderingHints {
                return null; // do nothing
            }
        };
     }
    public function createFilter(): BufferedImageOp { getFilter(); }
}

