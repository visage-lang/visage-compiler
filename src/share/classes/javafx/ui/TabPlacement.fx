/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class TabPlacement {
    public attribute id: Number;
    public attribute name: String;
    
    public static attribute TOP = TabPlacement {
            name: "TOP"
            id: javax.swing.JTabbedPane.TOP
    };

    public static attribute LEFT = TabPlacement {
            name: "LEFT"
            id: javax.swing.JTabbedPane.LEFT
    };

    public static attribute BOTTOM = TabPlacement {
            name: "BOTTOM"
            id: javax.swing.JTabbedPane.BOTTOM
    };

    public static attribute RIGHT = TabPlacement {
            name: "RIGHT"
            id: javax.swing.JTabbedPane.RIGHT
    };

}
