/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;

/**
 * Provides enumerated values for component alignment and text position
 */
public class VerticalAlignment {
    public attribute id: Number;
    public attribute name: String;
    
    public static attribute TOP = VerticalAlignment {
        id: javax.swing.SwingConstants.TOP
        name: "TOP"
    };


    public static attribute MIDDLE = VerticalAlignment {
        id: javax.swing.SwingConstants.CENTER
        name: "MIDDLE"
    };

    public static attribute CENTER = MIDDLE;

    public static attribute BOTTOM = VerticalAlignment {
        id: javax.swing.SwingConstants.BOTTOM
        name: "BOTTOM"
    };

}
