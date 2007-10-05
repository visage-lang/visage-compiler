/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;

import javax.swing.JOptionPane;


public class ConfirmType {
    public attribute id: Number;
    
    public static attribute DEFAULT = 
        ConfirmType{id: JOptionPane.DEFAULT_OPTION};
    public static attribute YES_NO = 
        ConfirmType{id: JOptionPane.YES_NO_OPTION};
    public static attribute YES_NO_CANCEL = 
        ConfirmType{id: JOptionPane.YES_NO_CANCEL_OPTION};
    public static attribute OK_CANCEL = 
        ConfirmType{id: JOptionPane.OK_CANCEL_OPTION};
}
