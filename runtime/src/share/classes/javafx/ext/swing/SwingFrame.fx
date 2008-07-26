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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JComponent;
import java.math.BigInteger;
import javafx.lang.Sequences;
import javafx.lang.FX;

private function indexOfJMenuInJMenuBar(item: JMenu, menubar: JMenuBar): Integer {
    var children = menubar.getComponents();
    return Sequences.indexByIdentity(children, item);
}

// PENDING_DOC_REVIEW
/**
 * A {@code Frame} is a top-level window with a title and a border
 * which can contain a list of {@link Menu}s to provide 
 * better interaction with the user.  
 * <p>
 * This class exists temporarily to allow for building applications based on
 * hierarchies of Swing based {@code Components}. In the future, the intention
 * is to turn {@code Component} into a {@code Node} and move layout into the
 * {@code Node} world. At that time, this class will dissapear, and only
 * {@link javafx.application.Frame} will remain.
 */
public class SwingFrame extends SwingWindow {

    // PENDING_DOC_REVIEW
    /**
     * Defines the title of the {@code Frame}.
     */
    public attribute title: String on replace {
        doAndIgnoreJWindowChange(function() {
            getJFrame().setTitle(title);
        });
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines whether this frame is resizable by the user.
     */
    public attribute resizable: Boolean = getJFrame().isResizable() on replace {
        doAndIgnoreJWindowChange(function() {
            getJFrame().setResizable(resizable);
        });
    }

    // PENDING(shannonh) - make bindable
    public attribute iconified: Boolean = false on replace {
        var oldState = BigInteger.valueOf(getJFrame().getExtendedState());
        var newState = if (iconified) oldState.setBit(0) else oldState.clearBit(0);
        getJFrame().setExtendedState(newState.intValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the array of {@link Menu}s to be used by this {@Frame}.
     */
    public attribute menus: Menu[] on replace oldMenus[a..b] = newSlice {
        var frame = getJFrame();
        var menubar = frame.getJMenuBar();

        if (menus == null) {
            for (menu in oldMenus[a..b]) {
                unparentFromThisContainer(menu);
            }

            getJFrame().setJMenuBar(null);
            frame.getRootPane().revalidate();
            frame.getRootPane().repaint();
        } else if (menubar == null) {
            menubar = JMenuBar{};
            getJFrame().setJMenuBar(menubar);
            for (menu in menus) {
                menubar.add(menu.getJMenu());
                parentToThisContainer(menu);
            }
            
            menubar.revalidate();
            menubar.repaint();
            resetMenusFromJMenuBar();
        } else {
            for (menu in oldMenus[a..b]) {
                menubar.remove(menu.getJMenu());
                unparentFromThisContainer(menu);
            }

            var index = if (a == 0) 0 else indexOfJMenuInJMenuBar(menus[a - 1].getJMenu(), menubar) + 1;
            for (menu in newSlice) {
                var oldidx = indexOfJMenuInJMenuBar(menu.getJMenu(), menubar);
                if (oldidx != -1 and oldidx < index) {
                    menubar.add(menu.getJMenu(), index - 1);
                } else {
                    menubar.add(menu.getJMenu(), index++);
                }
                parentToThisContainer(menu);
            }

            menubar.revalidate();
            menubar.repaint();
            resetMenusFromJMenuBar();
        }
    }

    postinit {
        var jFrame = getJFrame();

        jFrame.addPropertyChangeListener(PropertyChangeListener {
            public function propertyChange(e: PropertyChangeEvent): Void {
                if (ignoreJWindowChange) {
                    return;
                }

                var propName = e.getPropertyName();
                if ("title".equals(propName)) {
                    title = jFrame.getTitle();
                } else if ("resizable".equals(propName)) {
                    resizable = jFrame.isResizable();
                } 
            }
        });

        jFrame.addWindowStateListener(WindowStateListener {
            public function windowStateChanged(e: WindowEvent): Void {
                var state = BigInteger.valueOf(e.getNewState());
                iconified = state.testBit(0); // Frame.ICONIFIED = 1
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    protected function remove(component: Component): Void {
        SwingWindow.remove(component);
        // PENDING(shannonh) - what I really want here is a deleteByIdentity operator
        // http://openjfx.java.sun.com/jira/browse/JFXC-1005
        var menuIndices = for (menu in menus where FX.isSameObject(menu, component)) indexof menu;
        for (i in [sizeof menuIndices - 1..0 step -1]) {
            delete menus[menuIndices[i]];
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@link JFrame} delegate for this frame.
     */
    public /* final */ function getJFrame(): JFrame {
        window as JFrame;
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the specific {@link java.awt.Window} delegate for this frame.
     */
    /* final */ function createWindow(): java.awt.Window {
        var f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        return f;
    }

    private function resetMenusFromJMenuBar(): Void {
        var mb = getJFrame().getJMenuBar();
        var fromJMenuBar = for (i in [0..<mb.getComponentCount()],
                                j in [Component.getComponentFor(mb.getComponent(i) as JComponent)]
                                where j instanceof Menu) j as Menu;

        if (not Sequences.isEqualByContentIdentity(menus, fromJMenuBar)) {
            // PENDING(shannonh) - want to do this without firing the trigger
            // http://openjfx.java.sun.com/jira/browse/JFXC-1007
            menus = fromJMenuBar;
        }
    }

}
