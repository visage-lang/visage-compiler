/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

// PENDING_DOC_REVIEW
/**
 * This class is used to create a multiple-exclusion scope for
 * a set of buttons. Creating a set of buttons with the
 * same <code>ToggleGroup</code> object means that
 * turning "on" one of those buttons
 * turns off all other buttons in the group.
 */
public class ToggleGroup {

    private attribute buttons: SelectableButton[];
    
    private /* constant */ attribute swingButtonGroup: javax.swing.ButtonGroup
                                                = new javax.swing.ButtonGroup();

    // PENDING_DOC_REVIEW
    /**
     * Clears the selection such that none of the buttons
     * in the <code>ToggleGroup</code> are selected.
     */
    public function clearSelection(): Void {
        swingButtonGroup.clearSelection();
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns all the buttons that are participating in this group.
     */
    public /* final */ bound function getButtons(): SelectableButton[] {
        buttons;
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns all the selected buttons within the group.
     */
    public /* final */ bound function getSelection(): SelectableButton {
        var selection = for (button in buttons where button.selected) button;
        if (sizeof selection == 0) null else selection[0];
    }

    // PENDING_DOC_REVIEW
    /**
     * Adds the button to the group.
     * @param SelectableButton the button to be added
     */
    function add(button: SelectableButton): Void {
        insert button into buttons;
        swingButtonGroup.add(button.getAbstractButton());
    }
   
    // PENDING_DOC_REVIEW
    /**
     * Removes the button from the group.
     * @param SelectableButton the button to be added
     */
    function remove(button: SelectableButton): Void {
        delete button from buttons;
        swingButtonGroup.remove(button.getAbstractButton());
    }

}
