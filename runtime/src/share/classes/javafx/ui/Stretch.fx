/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class Stretch {
    public attribute id: String;
    
    public static attribute FILL = Stretch {
        id: "FILL"
    };

    public static attribute UNIFORM = Stretch {
        id: "UNIFORM"
    };

    public static attribute UNIFORM_TO_FILL = Stretch {
        id: "UNIFORM_TO_FILL"
    };
               
}
