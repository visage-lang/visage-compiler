/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;

import org.jdesktop.layout.GroupLayout;


public class Alignment {
    public attribute name: String;
    public attribute id:Number;
    
    public static attribute LEADING = 
        Alignment{name:"LEADING" id: GroupLayout.LEADING};
    public static attribute TRAILING = 
        Alignment{name:"TRAILING" id: GroupLayout.TRAILING};
    public static attribute CENTER = 
        Alignment{name:"CENTER" id: GroupLayout.CENTER};
    public static attribute BASELINE = 
        Alignment{name:"BASELINE" id: GroupLayout.BASELINE};
}
