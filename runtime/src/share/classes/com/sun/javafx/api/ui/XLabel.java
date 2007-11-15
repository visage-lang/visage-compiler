/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.javafx.api.ui;
import javax.swing.*;
public abstract class XLabel extends JEditorPane {
    abstract public void setPreloadImages(boolean value);
    abstract public boolean getPreloadImages();
}