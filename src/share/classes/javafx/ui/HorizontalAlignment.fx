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
public class HorizontalAlignment {
    public attribute id: Number;
    public attribute name: String;
    
    public static attribute LEADING = HorizontalAlignment {
        id: javax.swing.SwingConstants.LEADING
        name: "LEADING"
    };

    public static attribute CENTER = HorizontalAlignment {
        id: javax.swing.SwingConstants.CENTER
        name: "CENTER"
    };

    public static attribute TRAILING = HorizontalAlignment {
        id: javax.swing.SwingConstants.TRAILING
        name: "TRAILING"
    };

}
