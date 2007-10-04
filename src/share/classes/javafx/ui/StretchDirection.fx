/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class StretchDirection {
    public attribute id: String;
    
    public static attribute BOTH = StretchDirection {
        id: "BOTH"
    };

    public static attribute DOWN_ONLY = StretchDirection {
        id: "DOWN_ONLY"
    };

    public static attribute UP_ONLY = StretchDirection {
        id: "UP_ONLY"
    };

}
