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

import java.lang.Math;
import java.lang.InterruptedException;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;


/* TBD: Need a way to ask for an image that's no bigger than widthXheight, not just
   sizeXsize.
 */

// PENDING_DOC_REVIEW
/**
 * The {@code Image} class represents graphical images. 
 * 
 * @profile common
 */
public class Image {
    
    // PENDING_DOC_REVIEW
    /**
     * The URL to use in fetching the pixel data.
     *
     * @profile common
     */              
    public attribute url:String;
    
    // PENDING_DOC_REVIEW
    /**
     * The approximate percentage of image's loading that
     * has been completed. The default value is {@code 0}.
     *
     * @profile common
     */              
    public attribute progress:Number = 0.0;
    
    // PENDING_DOC_REVIEW
    /**
     * Determines the size of the image.
     * <p/>
     * If image's {@code size} is specified then the image is scaled 
     * so that the maximum dimension is equal to size 
     * and the original aspect ratio is preserved.
     *  
     * @profile common
     */              
    public attribute size:Number = 0.0;
    
    // PENDING_DOC_REVIEW
    /**
     * Determines the width of the image.
     * <p/>
     * If image's {@code size} is specified then the image is scaled 
     * so that the maximum dimension is equal to size 
     * and the original aspect ratio is preserved.
     * <p/> 
     * If image's {@code width} and {@code height} are specified
     * then the image is scaled to match.
     * <p/>
     * If only {@code width} is specified the image is scaled to match 
     * and the original aspect ratio is preserved.
     *  
     * @profile common
     */              
    public attribute width:Number = 0.0;   

    // PENDING_DOC_REVIEW
    /**
     * Determines the height of the image.
     * <p/>
     * If image's {@code size} is specified then the image is scaled 
     * so that the maximum dimension is equal to size 
     * and the original aspect ratio is preserved.
     * <p/> 
     * If image's {@code width} and {@code height} are specified
     * then the image is scaled to match.
     * <p/>
     * If only {@code height} is specified the image is scaled to match 
     * and the original aspect ratio is preserved.  
     *
     * @profile common
     */              
    public attribute height:Number = 0.0;  
    
    // PENDING_DOC_REVIEW
    /**
     * Determines the placeholder image which is used
     * when {@link #backgroundLoading} is set to true 
     * and the URL is loaded in the background.
     * The default value is {@code null}.
     * 
     * @profile common
     */              
    public attribute placeholder:Image = null;
    
    // PENDING_DOC_REVIEW
    /**
     * Determines if the {@link placeholder} image is used
     * while the URL is loaded in the background.
     * The default value is {@code null}
     *  
     * @profile common
     */              
    public attribute backgroundLoading:Boolean = false;
    
    attribute bufferedImage:BufferedImage = null;
    private attribute loadImageTask:Task = null;
    private attribute initialized:Boolean = false;

    /* Note the bound keyword doesn't work (yet) and so this function
       doesn't get revaluated if it appears in a bind expression.
       ImageView.fx now uses the package private bufferedImage attribute
       instead.  
    */
    public bound function getBufferedImage():BufferedImage { bufferedImage; }
    
    /* Can't do this lazily in getBufferedImage() because then the width/height
       attributes can be queried before they're initialized.
    */
    postinit {
        initialize();
    }

    private function initialize() {
        ImageIO.setUseCache(false);  // HACK
        if (bufferedImage != null) {
            bufferedImage = maybeScaleImage(asCompatibleImage(bufferedImage), width, height, size);
            syncSizeAttributes();
            progress = 100.0;
        }
        else if (backgroundLoading) {
             if  (placeholder != null) {
                 bufferedImage = placeholder.getBufferedImage();
             } 
            createLoadImageTask().execute();
            syncSizeAttributes();
        }
        else {
            var reader:ImageReader = findImageReader(new URL(url));
            if (reader != null) {
                bufferedImage = maybeScaleImage(readImage(reader), width, height, size);
                syncSizeAttributes();
                progress = 100.0;
            }
        }
    }
    
    private function createLoadImageTask():Task {
        loadImageTask =  Task {
            attribute reader:ImageReader = null;
            attribute w:Number = width;
            attribute h:Number = height;
            attribute s:Number = size;
            public function doInBackground():java.lang.Object {
                reader = findImageReader(new URL(url));
                if (reader != null) {
                    // reader.addIIOReadProgressListener(createProgressListener());
                    var i:BufferedImage = readImage(reader);
                    return maybeScaleImage(i, w, h, s);
                }
                else {
                    return null;
                }
            }
            protected function succeeded(image:java.lang.Object):Void {
                bufferedImage = image as BufferedImage;
                syncSizeAttributes();
                progress = 100;
            }
            private function abort() {
                if (reader != null) {
                    reader.abort();
                }
            }
            protected function interrupted(e:InterruptedException):Void { abort(); }
            protected function cancelled():Void { abort(); }
            protected function finished():Void { 
                reader = null;
                loadImageTask = null;
            }
            private function updateProgress(percentageDone:Number) {
                setProgress(percentageDone, 0.0, 100.0);
            }
            private function createProgressListener():IIOReadProgressListener {
                IIOReadProgressListener {
                    // see http://openjfx.java.sun.com/jira/browse/JFXC-644
                    public function imageProgress(r:ImageReader, percentageDone):Void {
                        updateProgress(percentageDone);
                    }
                    public function imageStarted(r:ImageReader, imageIndex:Integer):Void { }
                    public function imageComplete(r:ImageReader):Void { }
                    public function readAborted(r:ImageReader):Void { }
                    public function sequenceStarted(r:ImageReader, minIndex:Integer):Void { }
                    public function sequenceComplete(r:ImageReader):Void { }
                    public function thumbnailStarted(r:ImageReader, imageIndex:Integer, thumbIndex:Integer):Void { }
                    public function thumbnailProgress(r:ImageReader, percentageDone):Void { }
                    public function thumbnailComplete(r:ImageReader):Void { }
                }
            }
        }
    }

    private function syncSizeAttributes():Void {
        if (bufferedImage != null) {
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
            size = Math.max(width, height);
        }
    }

    /* If any of the width/height/size attributes were set, scale
     * the image to match.
     */
    private static function maybeScaleImage(image:BufferedImage, width:Number, height:Number, size:Number):BufferedImage {
        var w:Number = image.getWidth();
        var h:Number = image.getHeight();
        if ((width == 0.0) and (height == 0.0) and (size == 0.0)) {
            width = w;
            height = h;
            size = Math.max(w, h);
            return image;
        }
        else {
            if (size != 0.0) {
                var scale = size / Math.max(w, h);
                width = w * scale;
                height = h * scale;
            }
            else if (height == 0.0) {
                height = width/w * h;
            }
            else if (width == 0.0) {
                width = height/h * w;
            }
            return if (w != width or h != height) scale(image, width, height) else image;
        }
    }

    private static function createCompatibleImage(w:Integer, h:Integer, transparency:Integer):BufferedImage {
        // we don't know in advance to which screen this image will be
        // rendered, so just assume the default screen (on most modern
        // hardware with multiple screens, all screens will use the same
        // pixel layout anyway, so this isn't a bad guess)
        var gc = GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDefaultConfiguration();
        return gc.createCompatibleImage(w, h, transparency);
    }

    private static function asCompatibleImage(image:BufferedImage):BufferedImage {
        var gc = GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDefaultConfiguration();
        if (not gc.getColorModel(image.getTransparency()).equals(image.getColorModel())) {
            var newimg =
                createCompatibleImage(image.getWidth(), image.getHeight(),
                                      image.getTransparency());
            var g2d:Graphics2D = newimg.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
            image = newimg;
        }
        return image;
    }

    /* Scale the image to match the width/height attributes
     * 
     * This function is essentially the same as the getScaledInstance() 
     * example from Chris Campbell's "The Perils of Image.getScaledInstance()" 
     * article:
     * http://today.java.net/pub/a/today/2008/04/03/perils-of-image-getscaledinstance.html
     */
    private static function scale(image:BufferedImage, width:Number, height:Number):BufferedImage {
        var w:Number = image.getWidth();
        var h:Number = image.getHeight(); 
        var transparency = image.getTransparency();
        var interpolationHint = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        while (true) {
            w = if (w > width) Math.max(w/2.0, width) else width;
            h = if (h > height) Math.max(h/2.0, height) else height;
            var tmp = createCompatibleImage(w, h, transparency);
            var g2d:Graphics2D = tmp.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolationHint);
            g2d.drawImage(image, 0, 0, w, h, null);
            g2d.dispose();
            image = tmp;
            if (w == width and h == height) {
                break;
            }
        }
        return image;
    }

    private static function findImageReader(url:URL):ImageReader {
	var input:ImageInputStream = null; 
	try {
	    input = ImageIO.createImageInputStream(url.openStream());
	}
	catch(ignored:IOException) { 
            return null;
        }
	var reader:ImageReader = null;
        var readers:Iterator = ImageIO.getImageReaders(input);
        while((reader == null) and (readers != null) and readers.hasNext()) {
            reader = readers.next() as ImageReader;
        }
        if (reader != null) {
            reader.setInput(input);
        }
        else {
	    if (input != null) {
		try { input.close(); } catch (e:IOException) { }
	    }
        }
        return reader;
    }

    private static function readImage(reader:ImageReader):BufferedImage {
	var image:BufferedImage = null;
	try {
            image = asCompatibleImage(reader.read(reader.getMinIndex()));
	}
	catch (ignored:IOException) { }
	finally {
	    var input:ImageInputStream = reader.getInput() as ImageInputStream;
	    if (input != null) {
		try { input.close(); } catch (e:IOException) { }
	    }
	    if (reader != null) {
		reader.removeAllIIOReadProgressListeners();
		reader.dispose();
	    }
	}
        return image;
    }

    public function cancel():Void {
        if (loadImageTask != null) {
            loadImageTask.cancel(true);
            loadImageTask = null;
            if (bufferedImage != null) {
                bufferedImage.flush();
                if (placeholder != null) {
                    bufferedImage = placeholder.getBufferedImage();
                    width = placeholder.width;
                    height = placeholder.height;
                }
                else {
                    bufferedImage = null;
                    width = height = 0;
                }
            }
        }
    }

    public static function fromBufferedImage(image:BufferedImage) {
        Image { bufferedImage:image }
    }

}
