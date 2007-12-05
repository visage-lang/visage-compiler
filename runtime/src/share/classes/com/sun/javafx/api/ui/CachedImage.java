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
