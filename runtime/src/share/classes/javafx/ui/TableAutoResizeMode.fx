/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class TableAutoResizeMode {
    public attribute id: Number;
    
    public static attribute NEXT_COLUMN = TableAutoResizeMode {
        id: javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN
    };

    public static attribute SUBSEQUENT_COLUMNS = TableAutoResizeMode {
        id: javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
    };

    public static attribute LAST_COLUMN = TableAutoResizeMode {
        id: javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN
    };


    public static attribute ALL_COLUMNS = TableAutoResizeMode {
        id: javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS
    };
    
}
