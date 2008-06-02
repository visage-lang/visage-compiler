/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
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

package com.sun.javafx.api.ui;
import java.awt.*;
import javax.swing.*;

public class LayoutNotifier extends JPanel implements LayoutManager2 {

    LayoutManager2 mLayout;
    PaintListener mPaintListener;

    public LayoutNotifier() {
    }

    public void setPaintListener(PaintListener l) {
        mPaintListener = l;
    }

    public PaintListener getPaintListener() {
        return mPaintListener;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (mPaintListener != null) {
            mPaintListener.paint(g);
        } else {
            super.paintComponent(g);
        }
    }

    public void setLayoutManager(LayoutManager2 layout) {
        mLayout = layout;
        if (layout != null) {
            setLayout(this);
        }
    }
    
    public LayoutManager2 getLayoutManager() {
        return mLayout;
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        mLayout.addLayoutComponent(comp, constraints);
    }

    public Dimension maximumLayoutSize(Container target) {
        return mLayout.maximumLayoutSize(target);
    }

    public float getLayoutAlignmentX(Container target) {
        return mLayout.getLayoutAlignmentX(target);
    }

    public float getLayoutAlignmentY(Container target) {
        return mLayout.getLayoutAlignmentY(target);
    }

    public void invalidateLayout(Container target) {
        mLayout.invalidateLayout(target);
    }

    public void addLayoutComponent(String name, Component comp) {
        mLayout.addLayoutComponent(name, comp);
    }

    public void removeLayoutComponent(Component comp) {
        mLayout.removeLayoutComponent(comp);
    }

    public Dimension preferredLayoutSize(Container parent) {
        return mLayout.preferredLayoutSize(parent);
    }
   
    public Dimension minimumLayoutSize(Container parent) {
        return mLayout.minimumLayoutSize(parent);
    }

    public void layoutContainer(Container parent) {
        mLayout.layoutContainer(parent);
    }


}
