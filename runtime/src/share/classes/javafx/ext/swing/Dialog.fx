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
import javax.swing.JDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// PENDING_DOC_REVIEW
/**
 * A {@code Dialog} is a top-level window with a title and a border
 * that is typically used to take some form of input from the user.
 */
public class Dialog extends Window {

    // PENDING_DOC_REVIEW
    /**
     * Defines the title of the {@code Dialog}.
     */
    public attribute title: String on replace {
        doAndIgnoreJWindowChange(function() {
            getJDialog().setTitle(title);
        });
    }

    public attribute closeAction: function(): Void = close;

    // PENDING_DOC_REVIEW
    /**
     * Defines whether this dialog is resizable by the user.
     */
    public attribute resizable: Boolean = true on replace {
        doAndIgnoreJWindowChange(function() {
            getJDialog().setResizable(resizable);
        });
    }

    postinit {
        var jDialog = getJDialog();

        jDialog.addPropertyChangeListener(PropertyChangeListener {
            public function propertyChange(e: PropertyChangeEvent): Void {
                if (ignoreJWindowChange) {
                    return;
                }

                var propName = e.getPropertyName();
                if ("title".equals(propName)) {
                    title = e.getNewValue() as String;
                } else if ("resizable".equals(propName)) {
                    resizable = e.getNewValue() as Boolean;
                } 
            }
        });

        jDialog.addWindowListener(WindowAdapter {
            public function windowClosing(e: WindowEvent): Void {
                if (closeAction <> null) {
                    closeAction();
                }
            }
        });
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@link JDialog} delegate for this dialog.
     */
    /* final */ function getJDialog(): JDialog {
        window as JDialog;
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the specific {@link java.awt.Window} delegate for this dialog.
     */
    /* final */ function createWindow(): java.awt.Window {
        var d = if (owner <> null) new JDialog(owner.window) else new JDialog();
        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        return d;
    }

}
