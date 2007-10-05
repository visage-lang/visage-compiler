/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class Fill {
    public attribute id: Number;
    public attribute name: String;
    
    public static attribute HORIZONTAL = 
        Fill{id: java.awt.GridBagConstraints.HORIZONTAL
                  name: "HORIZONTAL"
               };

    public static attribute VERTICAL = 
        Fill{id: java.awt.GridBagConstraints.VERTICAL
                  name: "VERTICAL"
               };

    public static attribute BOTH = 
        Fill{id: java.awt.GridBagConstraints.BOTH
                  name: "BOTH"
               };

    public static attribute NONE = 
        Fill{id: java.awt.GridBagConstraints.NONE
                  name: "NONE"
               };               
}
