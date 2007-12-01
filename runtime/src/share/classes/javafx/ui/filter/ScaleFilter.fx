/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafx.ui.filter;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.RenderingHints;

public class ScaleFilter extends Filter {
    public attribute x: Number;
    public attribute y: Number;
    public function createFilter(): BufferedImageOp{
        return BufferedImageOp {
                public function filter(src:BufferedImage, dst:BufferedImage):BufferedImage {
                    var w = src.getWidth()*x;
                    var h = src.getHeight()*y;
                    var scaleImage = src.getScaledInstance(w.intValue(),  h.intValue(), Image.SCALE_AREA_AVERAGING );
                    if (dst == null) {
                        dst = new BufferedImage(src.getColorModel(),
                            src.getColorModel().createCompatibleWritableRaster(w.intValue(), h.intValue()),
                            src.getColorModel().isAlphaPremultiplied(), null);     
                    }
                    var g = dst.createGraphics();
                    g.drawImage( src, 0, 0, w.intValue(), h.intValue(), null );
                    g.dispose();
                    return dst;
                }
                // The rest are empty methods to fullfill the BufferedImageOp interface contract
                public function getBounds2D(src:BufferedImage):Rectangle2D {
                    return new Rectangle2D.Double(0,0, src.getWidth(), src.getHeight());
                    
                }
                public function createCompatibleDestImage(src:BufferedImage,
                                         destCM:ColorModel):BufferedImage {
                    return src;
                }
                public function getPoint2D(srcPt:Point2D, dstPt:Point2D):Point2D {
                        return srcPt;
                    
                }
                public function getRenderingHints():RenderingHints {
                    return null;
                    
                }
           };
    }
}

