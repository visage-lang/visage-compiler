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
import java.awt.*;
import javax.swing.*;

public class ScrollablePanel extends JPanel implements Scrollable {

    boolean mTracksViewportWidth;
    boolean mTracksViewportHeight;
    Dimension mPreferredScrollableViewportSize;

    public Dimension getPreferredScrollableViewportSize() {
        Dimension result;
        if (mPreferredScrollableViewportSize != null) {
            result = mPreferredScrollableViewportSize;
        } else {
            result = super.getPreferredSize();
        }
        //System.out.println("svpsize="+result);
        return result;
    }

    @Override
    public Dimension getPreferredSize() {
        return getPreferredScrollableViewportSize();
    }

    public void setPreferredScrollableViewportSize(Dimension dim) {
        mPreferredScrollableViewportSize = dim;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, 
                                          int orientation, 
                                          int direction) {
        return 1;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, 
                                           int orientation, 
                                           int direction) {
        return 1;
    }
    
    public boolean getScrollableTracksViewportWidth() {
        return mTracksViewportWidth;
    }

    public void setScrollableTracksViewportWidth(boolean value) {
        mTracksViewportWidth = value;
    }

    public boolean getScrollableTracksViewportHeight() {
        boolean result = mTracksViewportHeight || 
            (getParent() instanceof JViewport &&
             ((JViewport)getParent()).getHeight() > getPreferredSize().height);
        //System.out.println("tracks viewport height = " + result);
        return result;

    }

    public void setScrollableTracksViewportHeight(boolean value) {
        mTracksViewportHeight = value;
    }

}
