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

import java.lang.Object;
import javafx.lang.FX;

// PENDING_DOC_REVIEW  
/**
 * A class that represents a cell in a {@link ComboBox} component.
 */   
public class ComboBoxItem {

    // PENDING_DOC_REVIEW  
    /**
     * Defines a string of a {@code ComboBoxItem}.
     */    
    public attribute text: String on replace {
        if (combo != null) {
            combo.fireContentsChanged(this);
        }
    }

    // PENDING_DOC_REVIEW  
    /**
     * Defines whether a combobox item is selected or not.
     */ 
    public attribute selected: Boolean on replace {
        if (combo != null) {
            if (selected) {
                combo.selectedItem = this;
            } else if (FX.isSameObject(combo.selectedItem, this)) {
                combo.selectedItem = null;
            }
        }
    }

   // PENDING_DOC_REVIEW  
   /**
    * Defines a value of a {@code ComboBoxItem}.
    */ 
    public attribute value: Object;

    attribute combo: ComboBox;

    attribute comboIndex: Integer;

   // PENDING_DOC_REVIEW  
   /**
    * Returns a string representing this {@code ComboBoxItem}.
    */ 
    public override function toString(): String {
        text;
    }

}
