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


package javafx.ui;
import javafx.ui.SelectableButton;
import javafx.ui.SingleSelection;

public class ButtonGroup {
    private attribute buttongroup:javax.swing.ButtonGroup = 
        javax.swing.ButtonGroup{};

    public attribute buttons: SelectableButton[] 
        on insert [indx] (newValue) {
            newValue.buttonGroup = this;
            var c = newValue.getComponent();
            buttongroup.add(c as javax.swing.AbstractButton);
            if (newValue.selected) {
                selection = indx;
            } else if (indx == selection) {
                newValue.selected = true;
            }  

        }
        on replace [indx] (oldValue) {
            buttongroup.remove(oldValue.getComponent() as javax.swing.AbstractButton);
            var newValue = buttons[indx];
            newValue.buttonGroup = this;
            var c = newValue.getComponent();
            buttongroup.add(c as javax.swing.AbstractButton);
            if (newValue.selected) {
                selection = indx;
            } else if (indx == selection) {
                newValue.selected = true;
            }
        }
        on delete [indx] (oldValue) {
            buttongroup.remove(oldValue.getComponent() as javax.swing.AbstractButton);
        };
    public attribute selection: Number on replace (oldValue) {
        buttons[oldValue.intValue()].selected = false;
        buttons[selection.intValue()].selected = true;
        if (this.onSelectionChange <> null) {
            (this.onSelectionChange)(SingleSelection {
                                         anchorIndex: oldValue
                                             },
                                     SingleSelection {
                                         anchorIndex: selection
                                             });
        }
    };
    public attribute onSelectionChange: function(oldSelection:SingleSelection,
                                          newSelection:SingleSelection):Void;
}


