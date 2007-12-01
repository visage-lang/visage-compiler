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

