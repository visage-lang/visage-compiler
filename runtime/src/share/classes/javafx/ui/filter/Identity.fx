/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
    protected function createFilter(): BufferedImageOp { getFilter(); }
}

