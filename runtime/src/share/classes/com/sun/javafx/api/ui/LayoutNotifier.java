/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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