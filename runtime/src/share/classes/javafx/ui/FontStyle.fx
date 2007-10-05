/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class FontStyle {
    public attribute id: Integer;
    public attribute name: String;
    
    public static attribute BOLD = FontStyle {id: java.awt.Font.BOLD, name: "BOLD"};
    public static attribute PLAIN = FontStyle {id: java.awt.Font.PLAIN, name: "PLAIN"};
    public static attribute ITALIC = FontStyle {id: java.awt.Font.ITALIC, name: "ITALIC"};

}
