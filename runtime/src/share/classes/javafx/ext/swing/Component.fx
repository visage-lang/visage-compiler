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

package javafx.ext.swing;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.RepaintManager;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.lang.StringBuffer;
import javafx.scene.Font;
import javafx.scene.paint.Color;

/**
 * A common component ancestor. 
 */
public abstract class Component extends ClusterElement {

    private static /* constant */ attribute FX_COMPONENT_KEY = new StringBuffer("FX_COMPONENT_KEY");

    attribute ignoreJComponentChange: Boolean = false;

    /* constant */ attribute jComponent: JComponent = createJComponent();

    /**
     * The component's name, which may be {@code null}.
     */
    public attribute name: String on replace {
        doAndIgnoreJComponentChange(function() {
            // PENDING(shannonh) - do we want the name on both?
            jComponent.setName(name);
            getRootJComponent().setName(name);
        });
    }

    /**
     * Returns this {@code Component's} name,
     * or {@code null} if it doesn't have a name.
     * 
     * @return this {@code Component's} name or {@code null}
     * @see #name
     */
    public /* final */ bound function getName(): String {
        name;
    }

    /**
     */
    attribute parent: Container = null;

    /**
     * Returns this {@code Component's} parent {@code Container},
     * or {@code null} if it doesn't have a parent.
     *
     * @return this {@code Component's} parent or {@code null}
     */
    public /* final */ bound function getParent(): Container {
        parent;
    }

   // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the component.
     * <p/>
     * The default value is {@code null}.
     */
    public attribute x: Integer on replace {
        doAndIgnoreJComponentChange(function() {
            var root = getRootJComponent();
            var pt = root.getLocation();
            pt.x = x;
            root.setLocation(pt);
        });
    }

   // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the component.
     * <p/>
     * The default value is {@code null}.
     */    
    public attribute y: Integer on replace {
        doAndIgnoreJComponentChange(function() {
            var root = getRootJComponent();
            var pt = root.getLocation();
            pt.y = y;
            root.setLocation(pt);
        });
    }

   // PENDING_DOC_REVIEW
    /**
     * Sets the width of the component. 
     * <p/>
     * The default value is {@code null}.
     */    
    public attribute width: Integer on replace {
        doAndIgnoreJComponentChange(function() {
            var root = getRootJComponent();
            var dim = root.getSize();
            dim.width = width;
            root.setSize(dim);
            if (not root.isValid()) {
                RepaintManager.currentManager(root).addInvalidComponent(root);
            }
        });
    }

   // PENDING_DOC_REVIEW
    /**
     * Sets the height of the component. 
     * <p/>
     * The default value is {@code null}.
     */    
    public attribute height: Integer on replace {
        doAndIgnoreJComponentChange(function() {
            var root = getRootJComponent();
            var dim = root.getSize();
            dim.height = height;
            root.setSize(dim);
            if (not root.isValid()) {
                RepaintManager.currentManager(root).addInvalidComponent(root);
            }
        });
    }

    /**
     * The {@code Component's} preferred size. A sequence of two values:
     * {@code [width, height]}. {@code []} or {@code null} (the default) means
     * that the {@code Component} calculates its own preferred size. A single
     * item sequence {@code [n]} is treated the same as {@code [n, 0]}. With
     * sequences larger than two items, only the first two items are used.
     */
    public attribute preferredSize: Integer[] = null on replace {
        var s = sizeof preferredSize;
        var d: Dimension = null;

        if (s == 1) {
            d = new Dimension(preferredSize[0], 0);
        } else if (s > 1) {
            d = new Dimension(preferredSize[0], preferredSize[1]);
        } else {
            d = null;
        }

        var root = getRootJComponent();
        root.setPreferredSize(d);
        root.revalidate();
    }

   // PENDING_DOC_REVIEW
    /**
     * Sets the foreground color of this component.
     */    
    public attribute foreground: Color = Color.fromAWTColor(jComponent.getForeground()) on replace {
        doAndIgnoreJComponentChange(function() {
            jComponent.setForeground(foreground.getAWTColor());
        });
    }

   // PENDING_DOC_REVIEW
    /**
     * Sets the font for this component.
     */    
    public attribute font: Font = Font.fromAWTFont(jComponent.getFont()) on replace {
        doAndIgnoreJComponentChange(function() {
            jComponent.setFont(font.getAWTFont());
        });
    }

   // PENDING_DOC_REVIEW
    /**
     * Sets whether or not this component is enabled. A component that is
     * enabled may respond to user input, while a component that is not enabled 
     * cannot respond to user input. Some components may alter their visual 
     * representation when they are disabled in order to provide feedback to 
     * the user that they cannot take input.
     * <p/>
     * Note: Disabling a component does not disable it's children.
     * <p/>
     * Note: Disabling a lightweight component does not prevent it from 
     * receiving MouseEvents
     */ 
    public attribute enabled: Boolean = jComponent.isEnabled() on replace {
        doAndIgnoreJComponentChange(function() {
            jComponent.setEnabled(enabled);
        });
    }

   // PENDING_DOC_REVIEW
    /**
     * Makes the component visible or invisible.
     */ 
    public attribute visible: Boolean = jComponent.isVisible() on replace {
        doAndIgnoreJComponentChange(function() {
            getRootJComponent().setVisible(visible);
        });
    }

    public attribute hmin: Integer = Layout.DEFAULT_SIZE;

    public attribute hpref: Integer = Layout.DEFAULT_SIZE;

    public attribute hmax: Integer = Layout.DEFAULT_SIZE;

    public attribute vmin: Integer = Layout.DEFAULT_SIZE;

    public attribute vpref: Integer = Layout.DEFAULT_SIZE;

    public attribute vmax: Integer = Layout.DEFAULT_SIZE;

    public attribute halign: Layout.Alignment = null;

    public attribute valign: Layout.Alignment = null;

    public attribute hisbaseline: Boolean = false;

    public attribute visbaseline: Boolean = false;

    function doAndIgnoreJComponentChange(func: function(): Void) {
        try {
            ignoreJComponentChange = true;
            func();
        } finally {
            ignoreJComponentChange = false;
        }
    }

    postinit {
        // PENDING(shannonh) - do we want the same property on both?
        jComponent.putClientProperty(FX_COMPONENT_KEY, this);
        getRootJComponent().putClientProperty(FX_COMPONENT_KEY, this);

        getRootJComponent().addComponentListener(ComponentListener {
            public function componentHidden(e: ComponentEvent): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                visible = false;
            }

            public function componentMoved(e: ComponentEvent): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                var p = getRootJComponent().getLocation();

                if (x <> p.x) {
                    x = p.x;
                }

                if (y <> p.y) {
                    y = p.y;
                }
            }

            public function componentResized(e: ComponentEvent): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                var d = getRootJComponent().getSize();

                if (width <> d.width) {
                    width = d.width;
                }

                if (height <> d.height) {
                    height = d.height;
                }
            }

            public function componentShown(e: ComponentEvent): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                visible = true;
            }
        });

        jComponent.addPropertyChangeListener(PropertyChangeListener {
            public function propertyChange(e: PropertyChangeEvent): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                var propName = e.getPropertyName();
                if ("foreground".equals(propName)) {
                    foreground = Color.fromAWTColor(jComponent.getForeground());
                } else if ("font".equals(propName)) {
                    font = Font.fromAWTFont(jComponent.getFont());
                } else if ("name".equals(propName)) {
                    name = jComponent.getName();
                } else if ("enabled".equals(propName)) {
                    enabled = jComponent.isEnabled();
                }
            }
        });
    }

    /**
     * Returns the Swing {@code JComponent} encapsulated by this {@code Component}.
     * Never returns {@code null}.
     * 
     * @return the Swing {@code JComponent} encapsulated by this {@code Component}
     * @see #getRootJComponent()
     */
    public /* final */ function getJComponent(): JComponent {
        jComponent;
    }

    /**
     * Some {@code Component} implementations are backed by a hierarchy of Swing
     * {@code JComponents} rather than a single {@code JComponent}. This method
     * returns the top {@code JComponent} in the hierarchy, the one which is to
     * be used to embed this {@code Component} in an underlying Swing scene
     * graph or hierarchy. This is also the {@code JComponent} on which the
     * {@code x, y, width, height} attributes operate.
     * <p>
     * This method must never return {@code null}, even during initialization.
     * <p>
     * The default implementation delegates to {@link #getJComponent()}.
     *
     * @return the root {@code JComponent}
     */
    public function getRootJComponent(): JComponent {
        getJComponent();
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the {@link JComponent} delegate for this component.
     */ 
    protected abstract function createJComponent(): JComponent;

    // PENDING_DOC_REVIEW
    /**
     * Determines if this component or one of its immediate subcomponents is an
     * object of the property with the specified key, and if so, returns the
     * containing component.
     */ 
    public static /* final */ function getComponentFor(jComponent: JComponent): Component {
        if (jComponent == null) null else jComponent.getClientProperty(FX_COMPONENT_KEY) as Component;
    }

    // PENDING_DOC_REVIEW
    /**
     * Get the size and location values from the component.
     */ 
    public static /* final */ function fromJComponent(jComponent: JComponent): Component {
        if (jComponent == null) {
            return null;
        }

        var comp = getComponentFor(jComponent);
        if (comp <> null) {
            return comp;
        }

        return JComponentWrapper {
            jComponent: jComponent;
        }
    }

}

// PENDING(shannonh) - this can be removed when we're given the ability to
// anonymousely subclass an FX class. Then the code above that uses this
// class can create an anonumous class inline. See issue:
// http://openjfx.java.sun.com/jira/browse/JFXC-641
private class JComponentWrapper extends Component {
    protected function createJComponent(): JComponent {
        null;
    }
}
