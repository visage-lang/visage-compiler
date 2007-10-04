/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class MessageType {
    public attribute id: Number;
    
    public static attribute INFORMATION = MessageType {
        id: javax.swing.JOptionPane.INFORMATION_MESSAGE
    };

    public static attribute ERROR = MessageType {
        id: javax.swing.JOptionPane.ERROR_MESSAGE
    };

    public static attribute WARNING = MessageType {
        id: javax.swing.JOptionPane.WARNING_MESSAGE
    };

    public static attribute QUESTION = MessageType {
        id: javax.swing.JOptionPane.QUESTION_MESSAGE
    };

    public static attribute PLAIN = MessageType {
        id: javax.swing.JOptionPane.PLAIN_MESSAGE
    };
               
}
