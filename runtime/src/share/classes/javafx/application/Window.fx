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

import java.math.BigInteger;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JWindow;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javafx.lang.FX;
import javafx.ext.swing.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.image.Image;

// PENDING_DOC_REVIEW
/**
 * This is a abstract base class for all top-level windows. A window is a toplevel canvas for displaying scene nodes on
 * desktop platforms.
 */
public abstract class Window {

    private attribute initialized: Boolean = false;

    // PENDING(shannonh) - replace use of this attribute with isInitialized() or
    // similar when the following is resolved:
    // http://openjfx.java.sun.com/jira/browse/JFXC-1061
    private static /* final */ attribute UNINITIALIZED: Integer = java.lang.Integer.MIN_VALUE;

    /* constant */ protected attribute canvasStage:CanvasStageImpl = CanvasStageImpl{ stage: bind stage };
    /* constant */ protected attribute window : java.awt.Window = createWindow();

    public attribute name: String on replace {
        window.setName(name);
    }

    public attribute closeAction: function(): Void = close;
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the title of the {@code Window}.
     */
    public attribute title: String on replace { setWindowTitle(title); }

    abstract function setWindowTitle(title:String): Void;

    // PENDING_DOC_REVIEW
    /**
     * Defines whether this {@code Window} is resizable by the user.
     */
    public attribute resizable: Boolean = true on replace { setWindowResizable(resizable); }

    abstract function setWindowResizable(resizable:Boolean): Void;

    // PENDING_DOC_REVIEW
    /**
     * Defines the possible styles for a {@code Window} which are: {@code  WindowStyle.DECORATED},
     * {@code WindowStyle.UNDECORATED}, or {@code  WindowStyle.TRANSPARENT}.
     *
     * @setonce
     */
    public  /* set-once */ attribute windowStyle:WindowStyle = WindowStyle.DECORATED on replace {
        if (windowStyle == WindowStyle.DECORATED) {
            // do nothing as this is the default
        } else if (windowStyle == WindowStyle.UNDECORATED) {
            setUndecorated(true);
        } else if (windowStyle == WindowStyle.TRANSPARENT) {
            setUndecorated(true);
            WindowImpl.setWindowTransparency(window,true);
        }
    }

    abstract function setUndecorated(undecorated:Boolean): Void;

    // PENDING_DOC_REVIEW
    /**
     * Defines the icon images to be used in the window decorations and when minimized. The images should be different
     * sizes of the same thing and the best size will be chosen, eg. 16x16, 32,32.
     */
    public attribute icons: Image[] = null on replace {
        var iconList:java.util.ArrayList = new java.util.ArrayList();
        for (icon in icons){
            iconList.add(icon.getBufferedImage());
        }
        window.setIconImages(iconList);
    }

    // PENDING(shannonh) - binding support for x, y, width, height, visible

    public attribute x: Integer on replace {
        var pt = window.getLocation();
        pt.x = x;
        window.setLocation(pt);
    }

    public attribute y: Integer on replace {
        var pt = window.getLocation();
        pt.y = y;
        window.setLocation(pt);
    }

    public attribute width: Integer = UNINITIALIZED on replace {
        if (initialized) {
            var dim = window.getSize();
            dim.width = width;
            window.setSize(dim);
        }
    }

    public attribute height: Integer = UNINITIALIZED on replace {
        if (initialized) {
            var dim = window.getSize();
            dim.height = height;
            window.setSize(dim);
        }
    }

    public attribute visible: Boolean on replace {
        if (initialized) {
            setWindowVisibility(visible);
        }
    }

    /**
     * This is the stage that the window presents on screen for showing content.
     */
    public attribute stage: Stage;

    // PENDING_DOC_REVIEW
    /**
     * Defines the opacity of the whole of this {@code Window} (Both the window and its content). The opacity is at the
     * range [0..1]. Once this has been changed to a non {@code 1.0} value then there may be a performance penalty
     * depending on platform. A value of {@code 0.0} may or may not disable the mouse event handling on this
     * {@code Window}. If this window is maximized this property will have no effect as maximized windows can only be
     * 100% opaque. This is a platform-dependent behavior. This only has effect if the platform supports it. Currently
     * Sun Java 6u10 or newer and Apple Java support this.
     */
    public attribute opacity:Number = 1.0 on replace {
        WindowImpl.setWindowOpacity(window,opacity);
    }

    postinit {
        var rootPaneContainer:RootPaneContainer = window as RootPaneContainer;
        rootPaneContainer.setContentPane(canvasStage.jsgPanel);

        if (visible) {
            setWindowVisibility(true);
        }

        var w = window;

        w.addComponentListener(ComponentListener {
            public function componentHidden(e:ComponentEvent): Void {
                visible = false;
            }

            public function componentMoved(e:ComponentEvent): Void {
                var p = window.getLocation();

                if (x != p.x) {
                    x = p.x;
                }

                if (y != p.y) {
                    y = p.y;
                }
            }

            public function componentResized(e:ComponentEvent): Void {
                var d = window.getSize();

                if (width != d.width) {
                    width = d.width;
                }

                if (height != d.height) {
                    height = d.height;
                }
            }

            public function componentShown(e:ComponentEvent): Void {
                visible = true;
            }
        });

        w.addWindowListener(WindowAdapter {
            public function windowClosing(e: WindowEvent): Void {
                if (closeAction != null) {
                    closeAction();
                }
            }
        });

        initialized = true;
    }

    // PENDING_DOC_REVIEW
    /**
     * If this Window is visible, brings this Window to the front and may make
     * it the focused Window.
     */
    public function toFront(): Void {
        window.toFront();
    }

    // PENDING_DOC_REVIEW
    /**
     * If this Window is visible, sends this Window to the back and may cause
     * it to lose focus or activation if it is the focused or active Window.
     */
    public function toBack(): Void {
        window.toBack();
    }

    // PENDING_DOC_REVIEW
    /**
     * Close this window and indicate that there's no intention to use it again. If this is an application (vs. Applet),
     * VM will exit after all windows are closed provided all Timelines are stoped as well. Window *can* be shown again
     * with {@code visible = true}.
     */
    public function close(): Void {
        visible = false;
        window.dispose();
    }


    function setLocation(){
        if (not window.isLocationByPlatform() and (window.getX() == 0) and (window.getY() == 0)) {
            window.setLocationRelativeTo(null);
        }
    }

    private attribute isWindowInitialized: Boolean = false;

    private function getRootPaneContainer(): RootPaneContainer {
        window as RootPaneContainer;
    }

    private function setWindowVisibility(visible: Boolean): Void {
        if (visible) {
            if (width == UNINITIALIZED or height == UNINITIALIZED) {
                window.pack();
                if (width == UNINITIALIZED) {
                    width = window.getSize().width;
                }
                if (height == UNINITIALIZED) {
                    height = window.getSize().height;
                }
            }

            window.setSize(width, height);

            setLocation();
        }

        window.setVisible(visible);
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the {@link java.awt.Window} delegate for this component. Must implement RootPaneContainer, ie be a
     * JWindow,JDialog or JFrame.
     */
    abstract function createWindow(): java.awt.Window;

}
