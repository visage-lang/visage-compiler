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
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.LayoutManager;
import java.awt.Container;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import com.sun.scenario.scenegraph.JSGPanel;
import com.sun.scenario.scenegraph.SGGroup;
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
 *
 * @profile desktop
 */
public abstract class Window {

    private attribute initialized: Boolean = false;

    // PENDING(shannonh) - replace use of this attribute with isInitialized() or
    // similar when the following is resolved:
    // http://openjfx.java.sun.com/jira/browse/JFXC-1061
    private static /* final */ attribute UNINITIALIZED: Integer = java.lang.Integer.MIN_VALUE;

    /* constant */ protected attribute window : java.awt.Window = createWindow();
    /* constant */ attribute canvas : JSGPanelImpl = createCanvas();

    attribute ignoreWindowChange: Boolean = false;

    public attribute name: String on replace {
        doAndIgnoreWindowChange(function() {
            window.setName(name);
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

    public attribute closeAction: function(): Void = close;
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the title of the {@code Frame}.
     */
    public attribute title: String on replace { setWindowTitle(title); }

    abstract function setWindowTitle(title:String): Void;

    // PENDING_DOC_REVIEW
    /**
     * Defines whether this frame is resizable by the user.
     */
    public attribute resizable: Boolean = isWindowResizable() on replace { setWindowResizable(resizable); }

    abstract function isWindowResizable(): Boolean;

    abstract function setWindowResizable(resizable:Boolean): Void;

    // PENDING(shannonh) - synching FROM Swing
    public attribute icons: Image[] = null on replace {
        var iconList:java.util.ArrayList = new java.util.ArrayList();
        for (icon in icons){
            iconList.add(icon.getBufferedImage());
        }
        window.setIconImages(iconList);
    }

    private function getRoot(): SGGroup {
        canvas.getScene() as SGGroup;
    }

    // PENDING_DOC_REVIEW
    /**
     * The array of {@link Node}s to be rendered on this {@code Window}.
     *
     * @profile common
     */
    public attribute content: Node[] on replace oldNodes[a..b] = newNodes {
        for (node in oldNodes[a..b]) {
            getRoot().remove(node.impl_getFXNode());
        }
        var index = a;
        for (node in newNodes) {
            getRoot().add(index, node.impl_getFXNode());
            index = index + 1;
        }
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

    function setLocation(){
        if (not window.isLocationByPlatform() and (window.getX() == 0) and (window.getY() == 0)) {
            window.setLocationRelativeTo(null);
        }
    }

    // PENDING_DOC_REVIEW
    /*
     * Defines the background fill of this {@code Window}. If {@code null} the window will paint no background or
     * decorations(like title bar) and the opacity of the contents will depend on the alpha at which they a drawn. Often
     * this is referred to as "per-pixel transparency". Pixels that are 100% transparent (ie. no content has been drawn
     * there) do not accept mouse events. The mouse events pass directly to the window behind as if this window did not
     * exist for that pixel. This differs from {@code opacity} as it only makes the window background and decorations
     * 100% transparent not effecting the transparency of any content drawn in the window. This only has effect if the
     * platform supports it. Currently Sun Java 6u10 or newer and Apple Java support this.
     */
    public attribute fill: Paint = Color.fromAWTColor(window.getBackground()) on replace {
        // detect if Paint contains transparency, if it does then make window per-pixel transparent
        // Note: Any new Paint subclasses need to be added here
        var transparent:Boolean = fill == null;
        if (fill instanceof Color) {
            var color:Color = fill as Color;
            transparent = color.opacity <> 1;
        } else if (fill instanceof LinearGradient) {
            var g:LinearGradient = fill as LinearGradient;
            for (stop in g.stops){
                transparent = transparent or stop.color.opacity <> 1;
            }
        } else if (fill instanceof RadialGradient) {
            var g:RadialGradient = fill as RadialGradient;
            for (stop in g.stops){
                transparent = transparent or stop.color.opacity <> 1;
            }
        }
        WindowImpl.setWindowTransparency(window,transparent);
        var rootPaneContainer:RootPaneContainer = window as RootPaneContainer;
        var rootPane:WindowImpl.RootPaneImpl = rootPaneContainer.getRootPane() as WindowImpl.RootPaneImpl;
        //rootPane.setBackgroundPaint(fill.getAWTPaint());
        canvas.setBackgroundPaint(fill.getAWTPaint());
    }

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


    private attribute isWindowInitialized: Boolean = false;

    protected function doAndIgnoreWindowChange(func: function(): Void) {
        try {
            ignoreWindowChange = true;
            func();
        } finally {
            ignoreWindowChange = false;
        }
    }

    postinit {
        var rootPaneContainer:RootPaneContainer = window as RootPaneContainer;
        rootPaneContainer.setContentPane(canvas);

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

                if (x <> p.x) {
                    x = p.x;
                }

                if (y <> p.y) {
                    y = p.y;
                }
            }

            public function componentResized(e:ComponentEvent): Void {
                var d = window.getSize();

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
                if (ignoreWindowChange) {
                    return;
                }

                var propName = e.getPropertyName();
                if ("name".equals(propName)) {
                    name = window.getName();
                }
            }
        });

        initialized = true;
    }

    private function getRootPaneContainer(): RootPaneContainer {
        window as RootPaneContainer;
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


    // PENDING(shannonh) - JavaDoc. Something like:
    // Close this window and indicate that there's no intention to use it again.
    // If this is an application (vs. Applet), VM will exit after all windows
    // are closed. Window *can* be shown again with setVisible(true)
    public function close(): Void {
        window.dispose();
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the windows content.
     */
    function createCanvas(): JSGPanelImpl {
        var panel = new JSGPanelImpl();
        panel.setOpaque(true); // panel is non-opaque as background is painted for us by root pane
        panel.setLayout(createLayoutManager());
        panel.setScene(new SGGroup());
//        panel.setBackground(new java.awt.Color(255,128,128,128));
        return panel;
    }

    /**
     * Create layout manager for the JSGPanel
     */
    private function createLayoutManager() {
        LayoutManager {
            public function layoutContainer(p:Container):Void {
                for (node in content) {
                    doLayout(node);
                    updateCachedBounds(node);
                }
            }
            public function minimumLayoutSize(p:Container):Dimension {
                return preferredLayoutSize(p);
            }
            public function preferredLayoutSize(p:Container):Dimension {
                // JSGPanel overrides getPreferredSize(), doesn't delegate to
                // its LayoutManager
                return new Dimension();
            }
            public function addLayoutComponent(s:String, c:java.awt.Component):Void { }
            public function removeLayoutComponent(c:java.awt.Component):Void { }
        }
    }

    private function doLayout(root:Node):Void {
        if (root.impl_needsLayout) {
            if (root instanceof Group) {
                for (child in (root as Group).content) {
                    doLayout(child);
                }
            }
            else if (root instanceof CustomNode) {
                doLayout((root as CustomNode).impl_content);
            }
        }
        if (root instanceof Group) {
            var group:Group = root as Group;
            if (group.layout <> null) {
                group.layout(group);
            }
            for (child in group.content) {
                updateCachedBounds(child);
            }
        }
        root.impl_needsLayout = false;
    }

    private function updateCachedBounds(node:Node):Void {
        node.impl_cachedBounds = node.impl_getFXNode().getBounds();
        node.impl_cachedXYWH = node.impl_getSGNode().getBounds();
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the {@link java.awt.Window} delegate for this component. Must implement RootPaneContainer, ie be a
     * JWindow,JDialog or JFrame.
     */
    abstract function createWindow(): java.awt.Window;
}
