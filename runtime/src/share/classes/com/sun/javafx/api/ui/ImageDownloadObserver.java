/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.javafx.api.ui;

public interface ImageDownloadObserver {

    public void contentEncoding(String contentEncoding);
    public void progress(int read, int ofTotal);
    
}