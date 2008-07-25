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
import javax.swing.JRadioButtonMenuItem;

// PENDING_DOC_REVIEW
/**
 * An implementation of a radio button menu item.
 * A {@code RadioButtonMenuItem} is
 * a menu item that is part of a group of menu items in which only one
 * item in the group can be selected. The selected item displays its
 * selected state. Selecting it causes any other selected item to
 * switch to the unselected state.
 * To control the selected state of a group of radio button menu items,
 * use a {@code SwingToggleGroup} object.
 */
public class RadioButtonMenuItem extends MenuItem, SelectableButton {

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@code JRadioButtonMenuItem} delegate for this component.
     */
    public /* final */ function getJRadioButtonMenuItem(): JRadioButtonMenuItem {
        getJMenuItem() as JRadioButtonMenuItem;
    }

    function createJMenuItem(): JMenuItem {
        new JRadioButtonMenuItem();
    }

}
