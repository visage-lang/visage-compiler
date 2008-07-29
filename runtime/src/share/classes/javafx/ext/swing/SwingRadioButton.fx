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

import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

// PENDING_DOC_REVIEW
/**
 * An implementation of a radio button -- an item that can be selected or
 * deselected, and which displays its state to the user.
 * Create a {@code SwingToggleGroup} and use the button's
 * <code>toggleGroup</code> attribute to include the SwingRadioButton objects
 * in the group.
 */
public class SwingRadioButton extends SwingToggleButton {

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@code JRadioButton} delegate for this component.
     */
    public /* final */ function getJRadioButton(): JRadioButton {
        getJToggleButton() as JRadioButton;
    }

    override function createJToggleButton(): JToggleButton {
        new JRadioButton();
    }

}
