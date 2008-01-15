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
 
package javafx.ui.canvas; 


import java.awt.Dimension;
import java.awt.RenderingHints;
import javafx.ui.Image;
import javafx.ui.Stretch;
import javafx.ui.StretchDirection;
import javafx.ui.canvas.Node;
import com.sun.scenario.scenegraph.SGImage;
import com.sun.scenario.scenegraph.SGNode;

/**
 * A node that contains an Image. 
 */
public class ImageView extends Node {
    private attribute sgimage: SGImage = new SGImage();
    // Seems to be something wrong with this bind as
    // when image changes, awtImage does not get updated
    // after the original creation.
    private attribute awtImage: java.awt.Image //= bind image.getImage() 
    on replace {
        //java.lang.System.out.println("ImageView.awtImage on replace: sgimage = {sgimage}");
        if (sgimage <> null) {
            this.getImage();
        }
    };
    
    private attribute needsScaling: Boolean;
    public attribute image: Image on replace {
        //java.lang.System.out.println("ImageView.image on replace image = {image}, sgimage = {sgimage}");
        awtImage = image.getImage();
        if (sgimage <> null) {
            this.getImage();
        }
    };
    public attribute animated: Boolean;
    public attribute imageOpacity: Number = 1.0;
    public attribute loaded: Boolean;
    public attribute size: Dimension on replace {
        if (sgimage <> null) {
            this.getImage();
        }
    };
    public attribute stretch: Stretch;
    public attribute stretchDirection: StretchDirection;
    public function downloadPercentage(): Number{
        var result = if (image.totalDownloadSize == 0) then 0 else image.totalDownloaded/image.totalDownloadSize;
        return result*100;
    }
    public function getImage(){
        //java.lang.System.out.println("getImage() {image}={awtImage}-{size} ");
        if (image <> null and size <> null) {
            loaded = false;
            var curImage = image;
            var im = curImage.getImage();
            if (preload) {
                im = resizeImage(im);
                //loaded = sgimage.setImage(im); // TODO: return value
                sgimage.setImage(im);
                loaded = true;
            } else {
                //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                          public function run():Void {
                                im = resizeImage(im);
                                if (curImage == image) {
                                    //println("setting resized image");
                                    //loaded = sgimage.setImage(im);
                                    sgimage.setImage(im);
                                    loaded = true;
                                }
                          }
                });                
            }
        } else {
            loaded = false;
            sgimage.setImage(awtImage);
        }
    }

    public function resizeImage(img:java.awt.Image): java.awt.Image {
        
        if (img == null) {
            return null;
        }
        //TODO DO - this is a work around until a more permanent solution is provided
        javax.swing.SwingUtilities.invokeAndWait(java.lang.Runnable {
                  public function run():Void {
                      new javax.swing.ImageIcon(img);
                  }
        });
        var w:Number = img.getWidth(null);
        var h:Number = img.getHeight(null);
        var thumb = img;
        if (size.width < size.height) {
            var ratio = w/h;
            while (true) {
                w /= 2;
                if (w < size.width) {
                    w = size.width;
                }
                var temp = new java.awt.image.BufferedImage(w.intValue(),  (w / ratio).intValue(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
                var g2 = temp.createGraphics();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                                    java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(thumb, 0, 0, temp.getWidth(), temp.getHeight(), null);
                g2.dispose();
                thumb = temp;
                if (w == size.width) {
                    break;
                }
            } 
        } else {
            var ratio = h/w;
            while (true) {
                h /= 2;
                if (h < size.height) {
                    h = size.height;
                }
                var temp = new java.awt.image.BufferedImage((h / ratio).intValue(), h.intValue(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
                var g2 = temp.createGraphics();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                                    java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(thumb, 0, 0, temp.getWidth(), temp.getHeight(), null);
                g2.dispose();
                thumb = temp;
                if (h == size.height) {
                    break;
                }
            } 
        }
        return thumb;
    }

    public attribute preload: Boolean;
    public attribute antialias: Boolean = true on replace {
        updateInterpolationHint();
    }
    public attribute accelerate: Boolean;
    public attribute drawImmediately: Boolean = true;
    
    public function updateInterpolationHint() : Void {
        if (antialias) {
            sgimage.setInterpolationHint(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        } else {
            sgimage.setInterpolationHint(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        }
    }
    init {
        //sgimage = new SGImage();
        // TODO: do we need SGImage.setImageListener()
        /*
        sgimage.setImageListener(ZImageListener {
                public function imageUpdate(done:Boolean):Void {
                    loaded = done;
                    ag.repaint();
                }
            });
        */
        loaded = true;
    }
    public function createNode(): SGNode {
        getImage();
        updateInterpolationHint();
        return sgimage;
    }
}


// TODO: implement some of the following
/*
trigger on ImageView.antialias = value {
     zimage.setAntialias(value);
}

trigger on ImageView.drawImmediately = value {
     zimage.setIncrementalDrawing(drawImmediately);
}

trigger on ImageView.accelerate = value {
     zimage.setAccelerated(value);
}

trigger on ImageView.animated = value {
    zimage.setAnimated(value);
}

trigger on ImageView.imageOpacity = value {
    zimage.setImageOpacity(value);
}
*/

