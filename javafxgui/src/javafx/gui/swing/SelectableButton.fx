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

package javafx.gui.swing;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javafx.gui.*;

// PENDING_DOC_REVIEW
/**
 * An implementation of a selectable button.
 * The {@link ToggleButton} is a subclass of {@code SelectableButton}.
 */
public abstract class SelectableButton extends AbstractButton {

    public attribute selected: Boolean = getAbstractButton().isSelected() on replace {
        doAndIgnoreJComponentChange(function() {
            getAbstractButton().setSelected(selected);
        });

        // ButtonGroup might prevent setting selected to false, in which case,
        // we need to stay in synch.
        if (not selected and getAbstractButton().isSelected()) {
            selected = true;
        }
    }

    postinit {
        var ab = getAbstractButton();

        ab.addChangeListener(ChangeListener {
            public function stateChanged(e: ChangeEvent): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                // Only update if this is a selected state change.
                // Remember that selected could be bound, and we don't
                // want to try to update it (and risk an exception) if
                // it's not actually the selected state changing
                if (selected <> ab.isSelected()) {
                    selected = ab.isSelected();
                }
            }
        });
    }

}
