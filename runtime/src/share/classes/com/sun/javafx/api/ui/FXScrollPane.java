/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.javafx.api.ui;
import javax.swing.*;
import java.awt.*;

public class FXScrollPane extends JScrollPane {

    public FXScrollPane() {
    }

    public FXScrollPane(Component comp) {
        super(comp);
    }

    @Override
    protected JViewport createViewport() {
        return new FXViewport();
    }

    @Override
    public boolean isValidateRoot() {
        return false;
    }

    /*
    public void repaint(long tm, int x, int y, int w, int h) {
        Rectangle repaintBounds = new Rectangle(x, y, w, h);
        Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
        System.out.println("sp repaintBounds = "+repaintBounds);
        bounds = bounds.intersection(repaintBounds);
        System.out.println("sp bounds="+bounds);
        super.repaint(tm, bounds.x, bounds.y, bounds.width, bounds.height);
    }
    */
    
}
