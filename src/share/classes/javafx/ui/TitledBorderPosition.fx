/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class TitledBorderPosition {
    public attribute id: Number;
    public attribute name: String;
    
    public static attribute DEFAULT = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.DEFAULT_POSITION
        name: "DEFAULT"
    };

    public static attribute ABOVE_TOP = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.ABOVE_TOP
        name: "ABOVE_TOP"
    };

    public static attribute TOP = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.TOP
        name: "TOP"
    };

    public static attribute BELOW_TOP = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.BELOW_TOP
        name: "BELOW_TOP"
    };

    public static attribute ABOVE_BOTTOM = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.ABOVE_BOTTOM
        name: "ABOVE_BOTTOM"
    };

    public static attribute BOTTOM = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.BOTTOM
        name: "BOTTOM"
    };

    public static attribute BELOW_BOTTOM = TitledBorderPosition {
        id: javax.swing.border.TitledBorder.BELOW_BOTTOM
        name: "BELOW_BOTTOM"
    };    
}
