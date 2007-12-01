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

package com.sun.javafx.api.ui;
import java.awt.image.*;
import java.awt.*;
import java.awt.geom.*;

public class OpacityMaskFilter implements BufferedImageOp {

    Paint paint;

    public void setOpacityMask(Paint paint) {
        this.paint = paint;
    }

    public Paint getOpacityMask() {
        return paint;
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        int type = src.getType();
        Rectangle2D rect = new Rectangle2D.Double(0.0, 0.0, width, height);
        Graphics2D g2 = null;
        if (dst == null || dst.getWidth() != width || dst.getHeight() != height) {
            dst = createCompatibleDestImage( src, null );
            g2 = (Graphics2D)dst.getGraphics();
        } else {
            g2 = (Graphics2D)dst.getGraphics();
            g2.setComposite(AlphaComposite.Clear);
            g2.fill(rect);
        }
        g2.setPaint(paint);
        g2.fill(rect);
        g2.dispose();
        WritableRaster srcRaster = src.getRaster();
        WritableRaster dstRaster = dst.getRaster();
        int[] inPixels = new int[width];
        int[] outPixels = new int[width];
        for ( int y = 0; y < height; y++ ) {
            if ( type == BufferedImage.TYPE_INT_ARGB ) {
                srcRaster.getDataElements( 0, y, width, 1, inPixels );
                dstRaster.getDataElements( 0, y, width, 1, outPixels );
                for ( int x = 0; x < width; x++ )
                    inPixels[x] = filterRGB( x, y, inPixels[x], outPixels[x] );
                dstRaster.setDataElements( 0, y, width, 1, inPixels );
            } else {
                src.getRGB( 0, y, width, 1, inPixels, 0, width );
                dst.getRGB( 0, y, width, 1, outPixels, 0, width );
                for ( int x = 0; x < width; x++ )
                    inPixels[x] = filterRGB( x, y, inPixels[x], outPixels[x] );
                dst.setRGB( 0, y, width, 1, inPixels, 0, width );
            }
        }
        return dst;
    }

    final int filterRGB(int x, int y, int src, int dst) {
        return (src & 0x00ffffff) | (dst & 0xff000000);
    }

    public RenderingHints getRenderingHints() {
        return null;
    }

    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel dstCM) {
        if ( dstCM == null )
            dstCM = src.getColorModel();
        return new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), dstCM.isAlphaPremultiplied(), null);
    }

    public Rectangle2D getBounds2D( BufferedImage src ) {
        return new Rectangle(0, 0, src.getWidth(), src.getHeight());
    }
    
    public Point2D getPoint2D( Point2D srcPt, Point2D dstPt ) {
        if ( dstPt == null )
            dstPt = new Point2D.Double();
        dstPt.setLocation( srcPt.getX(), srcPt.getY() );
        return dstPt;
    }
}
