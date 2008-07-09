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


import javax.swing.UIManager;
import javax.swing.RootPaneContainer;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JDialog;
import java.awt.Graphics;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

// PENDING(shannonh) - this is public only to work around a compiler bug.
// Remove public modifier when resolved.
// http://openjfx.java.sun.com/jira/browse/JFXC-1050
/**
 * TransparentWindowImpl - This is a internal implementation class for Transparent and shaped window support in
 * Window.fx and Frame.fx. It should not be public or be ever used other than in those classes.
 *
 * @author Created by Jasper Potts (Jun 18, 2008)
 */
public class WindowImpl {
    private static final boolean isMac = "Mac OS X".equals(System.getProperty("os.name"));
    private static final WeakHashMap<java.awt.Window, ColorSet> storedWindowColors =
            new WeakHashMap<java.awt.Window, ColorSet>();
    private static final WeakHashMap<java.awt.Window, Boolean> transparentWindows =
            new WeakHashMap<java.awt.Window, Boolean>();

    static JDialog createJDialog(java.awt.Window owner) {
        if (isMac) {
            return new JDialog((java.awt.Window) owner) {
                @Override protected JRootPane createRootPane() {
                    RootPaneImpl rp = new RootPaneImpl() {
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
            return new JDialog((java.awt.Window) owner) {
                @Override protected JRootPane createRootPane() {
                    RootPaneImpl rp = new RootPaneImpl();
                    rp.setOpaque(true);
                    return rp;
                }
            };
        }
    }

    static JFrame createJFrame() {
        if (isMac) {
            return new JFrame() {
                @Override protected JRootPane createRootPane() {
                    RootPaneImpl rp = new RootPaneImpl() {
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
            return new JFrame() {
                @Override protected JRootPane createRootPane() {
                    RootPaneImpl rp = new RootPaneImpl();
                    rp.setOpaque(true);
                    return rp;
                }
            };
        }
    }

    static javax.swing.JFrame createTransparencySupportingJFrame() {
        if (isMac) {
            return new JFrame() {
                @Override protected JRootPane createRootPane() {
                    JRootPane rp = new JRootPane() {
                        @Override public void paint(Graphics g) {
                            System.out.println("clearingContentPane.paint");
                            g.clearRect(0, 0, getWidth(), getHeight());
                            super.paint(g);
                        }
                    };
                    rp.setOpaque(true);
                    return rp;
                }
            };
        } else {
            return new JFrame();
        }
    }

    static void setWindowTransparency(java.awt.Window window, boolean transparent) {
        // check if were are already in the required state
        Boolean isWindowTransparent = transparentWindows.get(window);
        if (isWindowTransparent == null) isWindowTransparent = false;
        if (isWindowTransparent != transparent) {
            if (isMac && window instanceof RootPaneContainer) {
                RootPaneContainer rootPaneContainer = (RootPaneContainer) window;
                ColorSet colorSet = storedWindowColors.get(window);
                if (transparent) {
                    // backup default colors
                    if (colorSet == null) {
                        colorSet = new ColorSet();
                        storedWindowColors.put(window, colorSet);
                    }
                    colorSet.defaultWindowBackground = window.getBackground();
                    window.setBackground(new java.awt.Color(0, 0, 0, 0));
                    if (!UIManager.getLookAndFeel().getName().startsWith("Mac")) {
                        colorSet.defaultContentPaneBackground = rootPaneContainer.getContentPane().getBackground();
                        rootPaneContainer.getContentPane().setBackground(new java.awt.Color(0, 0, 0, 0));
                    }
                    // hide decorations if frame
                    if (window instanceof JFrame) {
                        ((JFrame) window).setUndecorated(true);
                    }
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
                    // restore background colors
                    if (colorSet != null) {
                        window.setBackground(colorSet.defaultWindowBackground);
                        if (!UIManager.getLookAndFeel().getName().startsWith("Mac")) {
                            rootPaneContainer.getContentPane().setBackground(colorSet.defaultContentPaneBackground);
                        }
                    }
                    // show decorations if frame
                    if (window instanceof JFrame) {
                        ((JFrame) window).setUndecorated(false);
                    }
                    // resture window shadow
                    rootPaneContainer.getRootPane().putClientProperty("Window.hasShadow", Boolean.TRUE);
                    rootPaneContainer.getRootPane()
                            .putClientProperty("apple.awt.windowShadow.revalidateNow", new Object());
                }
                // keep track of if we made the window transparent
                transparentWindows.put(window, transparent);
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
                    // keep track of if we made the window transparent
                    transparentWindows.put(window, transparent);
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

    private static final class ColorSet {
        public java.awt.Color defaultWindowBackground, defaultContentPaneBackground;
    }

    static class RootPaneImpl extends JRootPane {}
}
