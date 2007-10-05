/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class TitledBorderJustification {
    public attribute id: Number;
    public attribute name: String;
    
    public static attribute DEFAULT = TitledBorderJustification {
        id: javax.swing.border.TitledBorder.DEFAULT_POSITION
        name: "DEFAULT"
    };

    public static attribute LEFT = TitledBorderJustification {
        id: javax.swing.border.TitledBorder.LEFT
        name: "LEFT"
    };

    public static attribute CENTER = TitledBorderJustification {
        id: javax.swing.border.TitledBorder.CENTER
        name: "CENTER"
    };

    public static attribute RIGHT = TitledBorderJustification {
        id: javax.swing.border.TitledBorder.RIGHT
        name: "RIGHT"
    };

    public static attribute LEADING = TitledBorderJustification {
        id: javax.swing.border.TitledBorder.LEADING
        name: "LEADING"
    };

    public static attribute TRAILING = TitledBorderJustification {
        id: javax.swing.border.TitledBorder.TRAILING
        name: "TRAILING"
    };

}
