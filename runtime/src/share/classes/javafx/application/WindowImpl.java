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
package javafx.application;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import java.awt.Graphics;
import java.lang.reflect.Method;

/**
 * TransparentWindowImpl - This is an internal implementation class for
 * Transparent and shaped window support in Window.fx and Frame.fx. It should
 * not be public or be ever used other than in those classes.
 *
 * @author Created by Jasper Potts (Jun 18, 2008)
 */
class WindowImpl {
    private static final boolean isMac = "Mac OS X".equals(System.getProperty("os.name"));

    static JDialog createJDialog(java.awt.Window owner) {
        JDialog dialog;
        if (isMac) {
            dialog = new JDialog((java.awt.Window) owner) {
                @Override protected JRootPane createRootPane() {
                    JRootPane rp = new JRootPane() {
                        @Override public void paint(Graphics g) {
                            g.clearRect(0, 0, getWidth(), getHeight());
                            super.paint(g);
                        }
                    };
                    rp.setOpaque(true);
                    return rp;
                }
            };
        } else {
            dialog = new JDialog((java.awt.Window) owner);
        }
        // set the default background color of white
        dialog.setBackground(java.awt.Color.WHITE);
        return dialog;
    }

    static JFrame createJFrame() {
        JFrame frame;
        if (isMac) {
            frame = new JFrame() {
                @Override protected JRootPane createRootPane() {
                    JRootPane rp = new JRootPane() {
                        @Override public void paint(Graphics g) {
                            g.clearRect(0, 0, getWidth(), getHeight());
                            super.paint(g);
                        }
                    };
                    rp.setOpaque(true);
                    return rp;
                }
            };
        } else {
            frame = new JFrame();
        }
        // set the default background color of white
        frame.setBackground(java.awt.Color.WHITE);
        return frame;
    }

    static void setWindowTransparency(java.awt.Window window, boolean transparent) {
        // check if were are already in the required state
        if (isMac && window instanceof RootPaneContainer) {
        System.out.println("setWindowTransparency isMac="+isMac+ " transparent="+transparent);
            RootPaneContainer rootPaneContainer = (RootPaneContainer) window;
            window.setBackground(new java.awt.Color(0, 0, 0, 0));
            // remove window shadows as they need to be updated
            // every time the contents outline changes and there
            // is no way we can detect this. It is to expensive
            // to just update on every repaint.
            rootPaneContainer.getRootPane()
                    .putClientProperty("apple.awt.windowShadow.revalidateNow", new Object());
            rootPaneContainer.getRootPane().putClientProperty("Window.hasShadow", Boolean.FALSE);
            // disable window dragging
            rootPaneContainer.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", false);
        } else {
            // try 6u10 AWTUtilities transparency
            Class c = null;
            try {
                c = Class.forName("com.sun.awt.AWTUtilities");
            } catch (ClassNotFoundException e) {}// ignore as handled
            if (c != null) {
                try {
                    Method m = c.getMethod("setWindowOpaque", java.awt.Window.class, Boolean.TYPE);
                    try {
                        m.invoke(null, window, !transparent);
                    } catch (UnsupportedOperationException e) {
                        System.err.println("Warning: Transparent windows are not " +
                                "supported by the current platform.");
                    }
                } catch (Exception e) {
                    System.err.println("Error setting window transparency using AWTUtilities");
                    e.printStackTrace();
                }
            }
        }
    }

    static void setWindowOpacity(java.awt.Window window, double opacity) {
        if (isMac && window instanceof RootPaneContainer) {
            RootPaneContainer rootPaneContainer = (RootPaneContainer) window;
            rootPaneContainer.getRootPane().putClientProperty("Window.alpha", new Float(opacity));
        } else {
            // try 6u10 AWTUtilities transparency
            Class c = null;
            try {
                c = Class.forName("com.sun.awt.AWTUtilities");
            } catch (ClassNotFoundException e) {}// ignore as handled
            if (c != null) {
                try {
                    Method m = c.getMethod("setWindowOpacity", java.awt.Window.class, Float.TYPE);
                    try {
                        m.invoke(null, window, (float)opacity);
                    } catch (UnsupportedOperationException e) {
                        System.err.println("Warning: Setting the opacity of a window is not " +
                                "supported by the current platform.");
                    }
                } catch (Exception e) {
                    System.err.println("Error setting window opacity using AWTUtilities");
                    e.printStackTrace();
                }
            }
        }
    }

}
