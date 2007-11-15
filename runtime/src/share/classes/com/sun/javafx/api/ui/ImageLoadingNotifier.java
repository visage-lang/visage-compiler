/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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