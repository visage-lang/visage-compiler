/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.ui;

/**
 * Maps to JDK 1.6 javafx.swing.DropMode, also indicates which default
 * drop operation to perform. For JDK 1.5, this has no visual effect but still
 * effects the default drop operation.
 * 
 * @see javax.swing.DropMode
 * @author jclarke
 */

public class DropMode {
    /**
     * an identifier number, this number must track the order of enums
     * defined in javax.swing.DropMode
     */
    public attribute id: Integer;
    
    /**
     * A component's own internal selection mechanism (or caret for text
     * components) should be used to track the drop location.
     */
    public static attribute USE_SELECTION = DropMode{id:0};

    /**
     * The drop location should be tracked in terms of the index of
     * existing items. Useful for dropping on items in tables, lists,
     * and trees.
     */
    public static attribute ON = DropMode{id:1};

    /**
     * The drop location should be tracked in terms of the position
     * where new data should be inserted. For components that manage
     * a list of items (list and tree for example), the drop location
     * should indicate the index where new data should be inserted.
     * For text components the location should represent a position
     * between characters. For components that manage tabular data
     * (table for example), the drop location should indicate
     * where to insert new rows, columns, or both, to accommodate
     * the dropped data.
     */
    public static attribute INSERT = DropMode{id:2};

    /**
     * The drop location should be tracked in terms of the row index
     * where new rows should be inserted to accommodate the dropped
     * data. This is useful for components that manage tabular data.
     */
    public static attribute INSERT_ROWS = DropMode{id:3};

    /**
     * The drop location should be tracked in terms of the column index
     * where new columns should be inserted to accommodate the dropped
     * data. This is useful for components that manage tabular data.
     */
    public static attribute INSERT_COLS = DropMode{id:4};

    /**
     * This mode is a combination of <code>ON</code>
     * and <code>INSERT</code>, specifying that data can be
     * dropped on existing items, or in insert locations
     * as specified by <code>INSERT</code>.
     */
    public static attribute ON_OR_INSERT = DropMode{id:5};
    
    /**
     * This mode is a combination of <code>ON</code>
     * and <code>INSERT_ROWS</code>, specifying that data can be
     * dropped on existing items, or as insert rows
     * as specified by <code>INSERT_ROWS</code>.
     */
    public static attribute ON_OR_INSERT_ROWS = DropMode{id:6};
    
    /**
     * This mode is a combination of <code>ON</code>
     * and <code>INSERT_COLS</code>, specifying that data can be
     * dropped on existing items, or as insert columns
     * as specified by <code>INSERT_COLS</code>.
     */
    public static attribute ON_OR_INSERT_COLS = DropMode{id:7};
 
}
