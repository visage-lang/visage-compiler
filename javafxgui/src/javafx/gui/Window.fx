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

package javafx.gui;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JWindow;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javafx.lang.FX;
import javafx.gui.component.*;

// PENDING_DOC_REVIEW
/**
 * A {@code Window} object is a top-level window.
 * <p/>
 * A window may have either a frame, dialog, or another window defined as its
 * owner when it's constructed. 
 */
public class Window extends Container {

    private attribute initialized: Boolean = false;

    // PENDING(shannonh) - replace use of this attribute with isInitialized() or
    // similar when the following is resolved:
    // http://openjfx.java.sun.com/jira/browse/JFXC-1061
    private static /* final */ attribute UNINITIALIZED: Integer = java.lang.Integer.MIN_VALUE;

    /*
     * This attribute must be defined before createWindow() is called, so
     * that the window can be created with the owner.
     */
    public /* set-once */ attribute owner : Window = null;

    private /* constant */ attribute window : java.awt.Window = createWindow();

    attribute ignoreJWindowChange: Boolean = false;

    public attribute name: String on replace {
        doAndIgnoreJWindowChange(function() {
            getWindow().setName(name);
        });
    }

    /**
     * Returns this {@code Window's} name,
     * or {@code null} if it doesn't have a name.
     * 
     * @return this {@code Window's} name or {@code null}
     */
    public /* final */ bound function getName(): String {
        name;
    }

    /**
     * This {@code Window's} content.
     */
    public attribute content: Component = null on replace oldContent {
        if (oldContent <> null) {
            unparentFromThisContainer(oldContent);
        }

        parentToThisContainer(content);

        if (content <> null) {
            getRootPaneContainer().setContentPane(content.getRootJComponent());
        } else {
            getRootPaneContainer().setContentPane(new JPanel());
        }
    }

    /**
     * {@inheritDoc}
     */
    protected function remove(component: Component): Void {
        if (FX.isSameObject(content, component)) {
            content = null;
        }
    }

    /**
     * Returns {@code null}. {@code Windows} don't have parents.
     * 
     * @return {@code null}
     */
    public /* final */ bound function getParent(): Container {
        null;
    }

    // PENDING(shannonh) - binding support for x, y, width, height, visible

    public attribute x: Integer on replace {
        var pt = getWindow().getLocation();
        pt.x = x;
        getWindow().setLocation(pt);
    }

    public attribute y: Integer on replace {
        var pt = getWindow().getLocation();
        pt.y = y;
        getWindow().setLocation(pt);
    }

    public attribute width: Integer = UNINITIALIZED on replace {
        if (initialized) {
            var dim = getWindow().getSize();
            dim.width = width;
            getWindow().setSize(dim);
        }
    }

    public attribute height: Integer = UNINITIALIZED on replace {
        if (initialized) {
            var dim = getWindow().getSize();
            dim.height = height;
            getWindow().setSize(dim);
        }
    }

    public attribute visible: Boolean on replace {
        if (initialized) {
            setWindowVisibility(visible);
        }
    }

    private function setWindowVisibility(visible: Boolean): Void {
        var w = getWindow();

        if (visible) {
            if (width == UNINITIALIZED or height == UNINITIALIZED) {
                w.pack();

                if (width == UNINITIALIZED) {
                    width = w.getSize().width;
                }

                if (height == UNINITIALIZED) {
                    height = w.getSize().height;
                }
            }

            w.setSize(width, height);

            if (not w.isLocationByPlatform() and (w.getX() == 0) and (w.getY() == 0)) {
                w.setLocationRelativeTo(if (owner <> null) owner.getWindow() else null);
            }
        }

        w.setVisible(visible);
    }

    public attribute background: Color = Color.fromAWTColor(getWindow().getBackground()) on replace {
        doAndIgnoreJWindowChange(function() {
            getWindow().setBackground(background.getAWTColor());
        });
    }

    private attribute isWindowInitialized: Boolean = false;

    function doAndIgnoreJWindowChange(func: function(): Void) {
        try {
            ignoreJWindowChange = true;
            func();
        } finally {
            ignoreJWindowChange = false;
        }
    }

    postinit {
        if (visible) {
            setWindowVisibility(true);
        }

        var w = getWindow();

        w.addComponentListener(ComponentListener {
            public function componentHidden(e:ComponentEvent): Void {
                visible = false;
            }

            public function componentMoved(e:ComponentEvent): Void {
                var p = getWindow().getLocation();

                if (x <> p.x) {
                    x = p.x;
                }

                if (y <> p.y) {
                    y = p.y;
                }
            }

            public function componentResized(e:ComponentEvent): Void {
                var d = getWindow().getSize();

                if (width <> d.width) {
                    width = d.width;
                }

                if (height <> d.height) {
                    height = d.height;
                }
            }

            public function componentShown(e:ComponentEvent): Void {
                visible = true;
            }
        });

        w.addPropertyChangeListener(PropertyChangeListener {
            public function propertyChange(e: PropertyChangeEvent): Void {
                if (ignoreJWindowChange) {
                    return;
                }

                var propName = e.getPropertyName();
                if ("background".equals(propName)) {
                    background = Color.fromAWTColor(window.getBackground());
                } else if ("name".equals(propName)) {
                    name = window.getName();
                }
            }
        });

        initialized = true;
    }

    private function getRootPaneContainer(): RootPaneContainer {
        getWindow() as RootPaneContainer;
    }

    // PENDING_DOC_REVIEW
    /**
     * If this Window is visible, brings this Window to the front and may make
     * it the focused Window.
     */
    public function toFront(): Void {
        getWindow().toFront();
    }

    // PENDING_DOC_REVIEW
    /**
     * If this Window is visible, sends this Window to the back and may cause
     * it to lose focus or activation if it is the focused or active Window.
     */
    public function toBack(): Void {
        getWindow().toBack();
    }


    // PENDING(shannonh) - JavaDoc. Something like:
    // Close this window and indicate that there's no intention to use it again.
    // If this is an application (vs. Applet), VM will exit after all windows
    // are closed. Window *can* be shown again with setVisible(true)
    public function close(): Void {
        getWindow().dispose();
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@link java.awt.Window} delegate for this window.
     */
    public /* final */ function getWindow(): java.awt.Window {
        window;
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the {@link java.awt.Window} delegate for this component.
     */
    protected function createWindow(): java.awt.Window {
        if (owner <> null) {
            new JWindow(owner.getWindow())
        } else {
            new JWindow();
        }
    }

}
