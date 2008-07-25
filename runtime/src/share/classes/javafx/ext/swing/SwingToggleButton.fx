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

import javax.swing.JToggleButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

// PENDING_DOC_REVIEW
/**
 * An implementation of a two-state button.
 * The {@link SwingRadioButton} and {@link SwingCheckBox} classes
 * are subclasses of this class.
 */
public class SwingToggleButton extends SelectableButton {

    // PENDING_DOC_REVIEW
    /**
     * Represents the toggle group of the {@code SwingToggleButton}.
     * A button can belong only to a single {@code SwingToggleGroup}.
     */
    public attribute toggleGroup: SwingToggleGroup on replace oldValue {
        if (oldValue != null) {
            oldValue.remove(this);
        }
        
        if (toggleGroup != null) {
            toggleGroup.add(this);
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@code JToggleButton} delegate for this component.
     */
    public /* final */ function getJToggleButton(): JToggleButton {
        getAbstractButton() as JToggleButton;
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the {@code AbstractButton} delegate for this component.
     */
    protected /* final */ function createAbstractButton(): javax.swing.AbstractButton {
        createJToggleButton();
    }

    function createJToggleButton(): JToggleButton {
        new JToggleButton();
    }

}
