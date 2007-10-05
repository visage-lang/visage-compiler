/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class ListLayoutOrientation {
    public attribute id: Number;
    public attribute name: String;
    
    public static attribute VERTICAL = ListLayoutOrientation {
        id: javax.swing.JList.VERTICAL
        name: "VERTICAL"
    };

    public static attribute VERTICAL_WRAP = ListLayoutOrientation {
        id: javax.swing.JList.VERTICAL_WRAP
        name: "VERTICAL_WRAP"
    };

    public static attribute HORIZONTAL_WRAP = ListLayoutOrientation {
        id: javax.swing.JList.HORIZONTAL_WRAP
        name: "HORIZONTAL_WRAP"
    };

}
