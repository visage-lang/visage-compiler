/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.scene;

import java.beans.*;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Support class for implementing {@code Paint}, translucent and
 * transparent (null) backgrounds on components.
 */
public class BackgroundSupport implements PropertyChangeListener {

    public static final String BACKGROUND_PAINT_PROPERTY = "backgroundPaint";

    private java.awt.Paint backgroundPaint;

    private BackgroundSupportable comp;

    private static final java.awt.Color NOCOLOR = new java.awt.Color(0, 0, 0, 0) {
        public String toString() {
            return "NOCOLOR";
        }

        public boolean equals(Object other) {
            return other == this;
        }
    };

    public interface BackgroundSupportable {
        public java.awt.Color getBackground();
        public void setBackground(java.awt.Color color);
        public boolean isOpaque();
        public void setOpaque(boolean opaque);
        public void repaint();
        public int getWidth();
        public int getHeight();
        public void addPropertyChangeListener(String propertyName, PropertyChangeListener pcl);
        public void fireBackgroundPaintChange(java.awt.Paint oldValue, java.awt.Paint newValue);
    }

    public BackgroundSupport(BackgroundSupportable comp) {
        this.comp = comp;
        backgroundPaint = comp.getBackground();
        comp.addPropertyChangeListener("background", this);
    }

    public void setBackgroundPaint(java.awt.Paint backgroundPaint) {
        java.awt.Paint old = this.backgroundPaint;
        this.backgroundPaint = backgroundPaint;

        if (backgroundPaint == null) {
            comp.setBackground(NOCOLOR);
            comp.setOpaque(false);
        } else if (backgroundPaint instanceof java.awt.Color && ((java.awt.Color)backgroundPaint).getAlpha() == 255) {
            comp.setBackground((java.awt.Color)backgroundPaint);
            comp.setOpaque(true);
        } else {
            comp.setBackground(NOCOLOR);
            comp.setOpaque(false);
            comp.repaint();
        }

        comp.fireBackgroundPaintChange(old, backgroundPaint);
    }

    public java.awt.Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public void propertyChange(PropertyChangeEvent pce) {
        java.awt.Color nv = (java.awt.Color)pce.getNewValue();
        if (nv != backgroundPaint && nv != NOCOLOR) {
            setBackgroundPaint(nv);
        }
    }

    public void paintComponent(Graphics g) {
        if (!comp.isOpaque() && backgroundPaint != null) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setPaint(backgroundPaint);
            g2d.fillRect(0, 0, comp.getWidth(), comp.getHeight());
        }
    }

}
