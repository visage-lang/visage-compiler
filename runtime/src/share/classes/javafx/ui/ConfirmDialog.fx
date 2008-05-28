/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
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


package javafx.ui;
import javafx.lang.DeferredTask;
import javafx.ui.UIElement;
import javafx.ui.MessageType;
import javafx.ui.Icon;
import javax.swing.JOptionPane;

public class ConfirmDialog {
    public attribute owner: UIElement;
    public attribute confirmType: ConfirmType = ConfirmType.DEFAULT;
    public attribute messageType: MessageType = MessageType.PLAIN;
    public attribute icon: Icon;
    public attribute title: String;
    public attribute message: String;
    public attribute visible: Boolean = false on replace {
        if (visible) {
            DeferredTask {
                action: function() : Void {
                    var result = JOptionPane.showConfirmDialog(
                          owner.getWindow(),
                          message,
                          title,
                          confirmType.id.intValue(),
                          messageType.id.intValue(),
                          icon.getIcon());
                    if (result == JOptionPane.YES_OPTION) { (this.onYes)(); }
                    else if (result == JOptionPane.NO_OPTION) { (this.onNo)(); }
                    else if (result == JOptionPane.CLOSED_OPTION) { (this.onCancel)(); }
                    else if (result == JOptionPane.OK_OPTION) { (this.onYes)(); }
                    else if (result == JOptionPane.CANCEL_OPTION) { (this.onCancel)(); }
                    visible = false;
                }
            }
        } 
    }
    public attribute onYes: function():Void;
    public attribute onNo: function():Void;
    public attribute onCancel: function():Void;
}

