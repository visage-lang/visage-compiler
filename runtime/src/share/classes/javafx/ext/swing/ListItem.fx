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
 * A class that represents a cell in a {@link List} component. 
 */
public class ListItem {

    // PENDING_DOC_REVIEW
    /**
     * Defines the single line of text this {@code ListItem} will display.  
     * If the value of text is null or empty string, nothing is displayed.
     */
    public var text: String on replace {
        if (list != null) {
            list.fireContentsChanged(this);
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines whether this {@code ListItem} is selected.
     */
    public var selected: Boolean on replace {
        if (list != null) {
            if (selected) {
                list.selectedItem = this;
            } else if (FX.isSameObject(list.selectedItem, this)) {
                list.selectedItem = null;
            }
        }
    }

    public var value: Object;

    var list: List;

    var listIndex: Integer;

    override function toString(): String {
        text;
    }
}
