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

import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JComponent;
import javafx.lang.Sequences;
import javafx.lang.FX;

// PENDING_DOC_REVIEW
/**
 * An implementation of a menu -- a popup window containing {@link MenuItem}s. 
 */
public class Menu extends MenuItem, Container {

    // PENDING_DOC_REVIEW
    /**
     * Defines the array of {@link MenuItem}s objects
     * to be displayed by this {@code Menu}.  
     */
    public attribute items: MenuItem[] on replace oldMenuItems[a..b] = newSlice {
        var jMenu = getJMenu();

        for (menuItem in oldMenuItems[a..b]) {
            jMenu.remove(menuItem.getJMenuItem());
            unparentFromThisContainer(menuItem);
        }

        var index = if (a == 0) 0 else indexOfJMenuItemInJMenu(items[a - 1].getJMenuItem(), jMenu) + 1;
        for (menuItem in newSlice) {
            var oldidx = indexOfJMenuItemInJMenu(menuItem.getJMenuItem(), jMenu);
            if (oldidx <> -1 and oldidx < index) {
                jMenu.add(menuItem.getJMenuItem(), index - 1);
            } else {
                jMenu.add(menuItem.getJMenuItem(), index++);
            }
            parentToThisContainer(menuItem);
        }

        jMenu.revalidate();
        jMenu.repaint();
        resetMenusFromJMenu();
    }

    /**
     * {@inheritDoc}
     */
    protected function remove(component: Component): Void {
        // PENDING(shannonh) - what I really want here is a deleteByIdentity operator
        // http://openjfx.java.sun.com/jira/browse/JFXC-1005
        var itemIndices = for (item in items where FX.isSameObject(item, component)) indexof item;
        for (i in [sizeof itemIndices - 1..0 step -1]) {
            delete items[itemIndices[i]];
        }
    }

    private static function indexOfJMenuItemInJMenu(item: JMenuItem, menu: JMenu): Integer {
        var children = menu.getMenuComponents();
        return Sequences.indexByIdentity(children, item);
    }

    private function resetMenusFromJMenu(): Void {
        var jMenu = getJMenu();
        var fromJMenu = for (i in [0..<jMenu.getMenuComponentCount()],
                             j in [Component.getComponentFor(jMenu.getMenuComponent(i) as JComponent)]
                             where j instanceof MenuItem) j as MenuItem;

        if (not Sequences.isEqualByContentIdentity(items, fromJMenu)) {
            // PENDING(shannonh) - want to do this without firing the trigger
            // http://openjfx.java.sun.com/jira/browse/JFXC-1007
            items = fromJMenu;
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@link JMenu} delegate for this component.
     */
    public /* final */ function getJMenu(): JMenu {
        getJMenuItem() as JMenu;
    }

    function createJMenuItem(): JMenuItem {
        new JMenu();
    }

}
