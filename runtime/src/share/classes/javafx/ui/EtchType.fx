/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class EtchType {
    public attribute id: Number;
    public attribute name: String;

    public static attribute LOWERED = 
        EtchType{id: javax.swing.border.EtchedBorder.LOWERED
                  name: "LOWERED"
               };
    public static attribute RAISED  =
        EtchType{id: javax.swing.border.EtchedBorder.RAISED
                  name: "RAISED"
               };
}
