/*
 * Copyright 1999-2005 Sun Microsystems, Inc.  All Rights Reserved.
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

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

public class XButton extends JButton {

    boolean mDefault;
    boolean mDefaultClose;
    boolean mFirstPaint = true;
    String mText;
    int mRotation = 0;
    boolean mInPaint;
    int mRotatedHeight;
    int mRotatedWidth;

    public XButton() {
        super();
    }

    public void setRotation(int r) {
        mRotation = r;
        invalidate();
        validate();
        repaint();
    }

    public int getRotation() {
        return mRotation;
    }

    public void setDefault(boolean value) {
        mDefault = value;
    }

    public void setDefaultClose(boolean value) {
        mDefaultClose = value;
    }

    @Override
    public int getWidth() {
        if (mInPaint) {
            return mRotatedWidth;
        }
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        if (mInPaint) {
            return mRotatedHeight;
        }
        return super.getHeight();
    }

    @Override
    public Dimension getPreferredSize() {
        boolean old = mInPaint;
        mInPaint = true;
        Dimension dim = super.getPreferredSize();
        mInPaint = old;
        if (mRotation == 90 || mRotation == 270) {
            dim = new Dimension(dim.height, dim.width);
        }
        return dim;
    }

    @Override
    public Insets getInsets() {
        Insets insets = super.getInsets();
        if (mInPaint) {
            if (mRotation != 0) {
                insets = new Insets(insets.left, insets.top, insets.right, insets.bottom);
            }
        }
        return insets;
    }

    @Override
    public Dimension getSize() {
        if (mInPaint) {
            return new Dimension(getWidth(), getHeight());
        }
        return super.getSize();
    }

    @Override
    public void paintComponent(Graphics g) {
        //System.out.println("button clip="+g.getClipBounds());
        if (mFirstPaint) {
            mFirstPaint = false;
            if (mDefault) {
                JRootPane root = getRootPane();
                if (root != null) {
                    root.setDefaultButton(this);
                }
            }
            if (mDefaultClose) {
                JRootPane root = getRootPane();
                if (root != null) {
                    KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
                    root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "ESCAPE");
                    root.getActionMap().put("ESCAPE",
                                            new AbstractAction() {
                                                public void actionPerformed(ActionEvent e) {
                                                    doClick(0);
                                                }
                                            });
                    root.putClientProperty("net.java.javafx.ui.defaultClose",
                                           this);
                }
            }
        }
        Graphics2D g2 = (Graphics2D) g;
        Composite oldComp = g2.getComposite();
        if (!isEnabled()) {
            if (getClientProperty("html") != null) {
                if (isOpaque()) {
                    g.setColor(getBackground());
                    Rectangle rect = g.getClipBounds();
                    g.fillRect(rect.x, rect.y, rect.width, rect.height);
                }
                AlphaComposite composite = 
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                g2.setComposite(composite);
            } 
        }
        AffineTransform  oldTransform = g2.getTransform();
        if (mRotation != 0) {
            mRotatedHeight = getWidth();
            mRotatedWidth = getHeight();
            g2.rotate(Math.toRadians(-mRotation), getWidth()/2, getHeight()/2); 
            g.translate(-getHeight()/2 + getWidth()/2, getHeight()/2 - getWidth()/2);
            mInPaint = true;
            super.paintComponent(g2);
            mInPaint = false;
        } else {
            super.paintComponent(g);
        }
        g2.setComposite(oldComp);
        g2.setTransform(oldTransform);
    }
}
