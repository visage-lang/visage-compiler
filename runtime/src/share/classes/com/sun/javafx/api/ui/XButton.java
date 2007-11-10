/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
