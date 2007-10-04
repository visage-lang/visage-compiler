/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class BevelType {
    public attribute id: Number;
    public attribute name: String;
    
    public static attribute LOWERED = 
        BevelType{id: javax.swing.border.BevelBorder.LOWERED
                  name: "LOWERED"
               };
    public static attribute RAISED  =
        BevelType{id: javax.swing.border.BevelBorder.RAISED
                  name: "RAISED"
               };
}
