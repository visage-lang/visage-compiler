/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
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

public class ImageLoadingNotifier implements ImageObserver {

    ImageLoadingListener mListener;
    Image mImage;
    
    public ImageLoadingNotifier(Image image, ImageLoadingListener l) {
        mImage = image;
        mListener = l;
        int w = mImage.getWidth(this);
        int h = mImage.getHeight(this);
        if (w >= 0 && h >= 0) {
            mListener.imageLoadingDone(image);
        }
    }

    public interface ImageLoadingListener {
        public void imageLoadingDone(Image image);
        public void imageWidthAvailable(Image image, int w);
        public void imageHeightAvailable(Image image, int h);
        public void imageLoadingFailed(Image image);
    }

    public boolean imageUpdate(Image img, int infoflags,
                               int x, int y, int w, int h) {
        if ((infoflags & WIDTH) != 0) {
            mListener.imageWidthAvailable(img, w);
        }
        if ((infoflags & HEIGHT) != 0) {
            mListener.imageHeightAvailable(img, h);
        }
        if ((infoflags & (ALLBITS)) != 0) {
            mListener.imageLoadingDone(img);
            return false;
        }
        if ((infoflags & ERROR) != 0) {
            mListener.imageLoadingFailed(img);
            return false;
        }
        if ((infoflags & ABORT) != 0) {
            mListener.imageLoadingFailed(img);
            return false;
        }
        return true;
    }
    
}
