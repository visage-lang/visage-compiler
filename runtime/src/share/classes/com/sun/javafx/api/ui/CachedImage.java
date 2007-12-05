/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.javafx.api.ui;
import java.awt.image.*;
import java.awt.*;
import java.util.*;
import sun.awt.image.*;
import java.net.URL;
import java.io.*;

public class CachedImage extends ByteArrayImageSource implements ImageDownloadObserver, ImageDownloadNotifier {

    Set mDownloadObservers = new HashSet();
    UIContextImpl mUIContext;
    URL mURL;

    @SuppressWarnings("unchecked")
    public void addImageDownloadObserver(ImageDownloadObserver observer) {
        synchronized (mDownloadObservers) {
            mDownloadObservers.add(observer);
        }
    }

    public void removeImageDownloadObserver(ImageDownloadObserver observer) {
        synchronized (mDownloadObservers) {
            mDownloadObservers.remove(observer);
        }
    }

    public void contentEncoding(String contentEncoding) {
        ImageDownloadObserver[] arr;
        synchronized (mDownloadObservers) {
            arr = new ImageDownloadObserver[mDownloadObservers.size()];
            mDownloadObservers.toArray(arr);
        }
        for (int i = 0; i  < arr.length; i++) {
            arr[i].contentEncoding(contentEncoding);
        }
    }

    int mTotalRead;
    int mTotalSize;
    public int getTotalSize() {return mTotalRead;}
    public int getTotalRead() {return mTotalSize;}
    

    public void progress(int read, int ofTotal) {
        ImageDownloadObserver[] arr;
        synchronized (mDownloadObservers) {
            arr = new ImageDownloadObserver[mDownloadObservers.size()];
            mDownloadObservers.toArray(arr);
            if (read == ofTotal) {
                mDownloadObservers.clear();
            }
        }
        mTotalRead = read;
        mTotalSize = ofTotal;
        for (int i = 0; i  < arr.length; i++) {
            arr[i].progress(read, ofTotal);
        }
    }


    public CachedImage(UIContextImpl ui, URL u) {
        super(new byte[0]);
        mURL = u;
        mUIContext = ui;
    }
    
    @Override
    protected ImageDecoder getDecoder() {
        try {
            InputStream is = mUIContext.getInputStream(mURL, this);
            return getDecoder(is);
        } catch (Exception e) {
            return null;
        }
    }

}
