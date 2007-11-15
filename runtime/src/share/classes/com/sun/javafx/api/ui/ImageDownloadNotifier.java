/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.javafx.api.ui;

public interface ImageDownloadNotifier {
    public void addImageDownloadObserver(ImageDownloadObserver observer);
    public void removeImageDownloadObserver(ImageDownloadObserver observer);
    public int getTotalSize();
    public int getTotalRead();
}
