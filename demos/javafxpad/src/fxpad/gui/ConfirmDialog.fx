/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */ 

package fxpad.gui;

import javax.swing.JOptionPane;
import javafx.ext.swing.*;

/**
 * @author jclarke
 */

public class ConfirmDialog  {
    public attribute owner: Window;
    public attribute confirmType: ConfirmType = ConfirmType.DEFAULT;
    public attribute messageType: MessageType = MessageType.PLAIN;
    public attribute icon: Icon;
    public attribute title: String;
    public attribute message: String;
    public attribute visible: Boolean = false on replace {
        if (visible) {
            //TODO DO LATER
            // do later {
                var result = JOptionPane.showConfirmDialog(
                    //TODO window is not public in class Window
                      owner.window,
                      message,
                      title,
                      confirmType.id.intValue(),
                      messageType.id.intValue(),
                      icon.getToolkitIcon());
                if (result == JOptionPane.YES_OPTION) { (this.onYes)(); } 
                else if (result == JOptionPane.NO_OPTION) { (this.onNo)(); }
                else if (result == JOptionPane.CLOSED_OPTION) { (this.onCancel)(); }
                else if (result == JOptionPane.OK_OPTION) { (this.onYes)(); }
                else if (result == JOptionPane.CANCEL_OPTION) { (this.onCancel)(); }
                visible = false;
            //TODO DO LATER
            //}
        } 
    }
    public attribute onYes: function():Void;
    public attribute onNo: function():Void;
    public attribute onCancel: function():Void;
}