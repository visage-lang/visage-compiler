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
 
package javafx.ui; 

import javafx.ui.Icon;
import java.awt.Dimension;
import com.sun.javafx.api.ui.ImageDownloadNotifier;
import com.sun.javafx.api.ui.ImageDownloadObserver;
import java.util.HashSet;

/**
 * An implementation of the Icon interface that paints Icons
 * from Images. Images are created from a URL
 */

public class Image extends Icon {
    public static attribute PERM_IMAGE_CACHE:HashSet = HashSet{};
    private attribute notifier: com.sun.javafx.api.ui.ImageLoadingNotifier;
    protected attribute image:java.awt.Image;
    attribute baseURL:String;
    public attribute cache: Boolean;

    private function getImage0():java.awt.Image {
        if (image == null and url <> null) {
            var imageUrl = url;
            if (baseURL <> null and baseURL.length() > 0) {
                var base = new java.net.URL(baseURL);
                var tmpUrl = new java.net.URL(base, url); 
                imageUrl = tmpUrl.toString();
            }
            image = if (preloadIfLocal and (url.startsWith("data:") or url.startsWith("file:") or url.startsWith("jar:"))) 
                then UIElement.context.getLoadedImage(imageUrl)
                else UIElement.context.getImage(imageUrl);

            if (image.getSource() instanceof ImageDownloadNotifier) {
                var notifierDL = image.getSource() as ImageDownloadNotifier;
                totalDownloadSize = notifierDL.getTotalSize();
                totalDownloaded = notifierDL.getTotalRead();
                notifierDL.addImageDownloadObserver(ImageDownloadObserver {
                        public function progress(totalRead:Integer, ofTotal:Integer):Void {
                            /********** JXFC-332 JXFC-333 **********
                            //TODO DO LATER - this is a work around until a more permanent solution is provided
                            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                                      public function run():Void {
                                            totalDownloadSize = ofTotal;
                                            totalDownloaded = totalRead;
                                            if(downloadProgress <> null) {
                                                downloadProgress(totalRead, ofTotal);
                                            }
                                      }
                            });
                             *******************/
                        }
                        public function contentEncoding(contentEncoding:String):Void {
                            //empty
                        }
                    });
                if (notifierDL.getTotalRead() > 0 and notifierDL.getTotalRead() == notifierDL.getTotalSize()) {
                    if(downloadProgress <> null) {
                        downloadProgress(notifierDL.getTotalRead(), notifierDL.getTotalSize());
                    }
                }
            }
            
            if (image <> null and size <> null) {
                new javax.swing.ImageIcon(image);
                var w = image.getWidth(null).doubleValue();
                var h = image.getHeight(null).doubleValue();
                if (false) {
                    var t = new java.awt.geom.AffineTransform();
                    t.scale(size.width/w, size.height/h);
                    var ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
                    var gs = ge.getDefaultScreenDevice();
                    var gc = gs.getDefaultConfiguration();
                    var im = gc.createCompatibleImage(size.width, size.height,
                                                      java.awt.Transparency.TRANSLUCENT);
                    var g = im.getGraphics() as java.awt.Graphics2D;
                    g.setTransform(t);
                    g.drawImage(image, 0, 0, null);
                    g.dispose();
                    image = im;
                } else {
                    var thumb = image;
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
                    image = thumb;
                }

            }
        }

        if (cache and image <>  null) {
            PERM_IMAGE_CACHE.add(image);
        }
        return image;
    }

    public attribute preloadIfLocal: Boolean = true;
    public attribute url: String on replace {
        image = null;
        this.getImage0();
    };
    public attribute size: Dimension;
    public attribute onLoad: function() on replace {
        this.installListener();
    };
    public attribute stretch: Stretch;
    public attribute totalDownloadSize: Number;
    public attribute totalDownloaded: Number;
    public attribute stretchDirection: StretchDirection;
    public attribute downloadProgress: function(totalRead:Number, ofTotal:Number):Void;
    private function installListener() {
        if (image <> null and onLoad <> null) {
            var loadingListener = com.sun.javafx.api.ui.ImageLoadingNotifier.ImageLoadingListener {
                                public function imageLoadingDone(img:java.awt.Image):Void {
                                    if(onLoad <> null) {
                                        onLoad();
                                    }
                                    notifier = null;
                                }
                                public function imageLoadingFailed(img:java.awt.Image):Void {
                                    if(onLoad <> null) {
                                        onLoad();
                                    }
                                    notifier = null;
                                }
                                public function imageWidthAvailable(img:java.awt.Image, w:Integer):Void {
                                    // Empty
                                }
                                public function imageHeightAvailable(img:java.awt.Image, h:Integer):Void {
                                    // Empty
                                }
                            };
            notifier = new com.sun.javafx.api.ui.ImageLoadingNotifier(image,loadingListener);
        }
    }
    public function getURL():java.net.URL {
        return UIElement.context.getImageURL(url);
    }
    public function getIcon(): javax.swing.Icon  {
        var im = this.getImage();
        return if (im == null) then null else  new javax.swing.ImageIcon(im);
    }
    public function getImage():java.awt.Image {
        return if (image == null) then getImage0() else image;
    }
    protected function createIcon(): javax.swing.Icon {
        return null;
    }
}



