/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
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

import javafx.ui.SelectableButton;
import javafx.ui.Icon;
import javafx.ui.KeyStroke;

public class ToggleButton extends SelectableButton {
    // TODO MARK AS FINAL
    protected attribute button: com.sun.javafx.api.ui.XToggleButton;
    
    public attribute defaultButton: Boolean;
    public attribute text: String on replace {
        button.setText(text);
    };
    public attribute mnemonic: KeyStroke;
    public attribute icon: Icon on replace {
        var jicon = null;
        if (icon <> null) {
            jicon = icon.getIcon();
        }
        button.setIcon(jicon);
        if (selectedIcon == null) {
            button.setSelectedIcon(jicon);
        }
    };
    public attribute selectedIcon: Icon on replace {
        var jicon = null;
        if (icon <> null) {
            jicon = icon.getIcon();
        }
        button.setSelectedIcon(jicon);
    };
    
    public attribute pressedIcon: Icon on replace {
        var jicon = null;
        if (icon <> null) {
            jicon = icon.getIcon();
        }
        button.setPressedIcon(jicon);
    };
    public attribute rolloverIcon: Icon on replace {
        var jicon = null;
        if (icon <> null) {
            jicon = icon.getIcon();
        }
        button.setRolloverIcon(jicon);
    };
    public attribute rolloverSelectedIcon: Icon on replace {
        var jicon = null;
        if (icon <> null) {
            jicon = icon.getIcon();
        }
        button.setRolloverSelectedIcon(jicon);
    };
    public attribute disabledIcon: Icon on replace {
        var jicon = null;
        if (icon <> null) {
            jicon = icon.getIcon();
        }
        button.setDisabledIcon(jicon);
    };
    public attribute disabledSelectedIcon: Icon on replace {
        var jicon = null;
        if (icon <> null) {
            jicon = icon.getIcon();
        }
        button.setDisabledSelectedIcon(jicon);
    };
    public attribute iconTextGap: Number on replace {
        button.setIconTextGap(iconTextGap.intValue());
    };
    
    public attribute contentAreaFilled: Boolean on replace {
        button.setContentAreaFilled(contentAreaFilled);
    };
    public attribute focusPainted:Boolean on replace {
        button.setFocusPainted(focusPainted);
    };
    public attribute borderPainted: Boolean on replace {
        button.setBorderPainted(borderPainted);
    };
    public attribute action: function():Void;
    
    public function createComponent():javax.swing.JComponent {
        return button;
    }
    
    public function setSelected(value:Boolean):Void {
        button.setSelected(value);
    }
    init {
        //TODO override
        button = UIElement.context.createToggleButton();
        enabled = true;
        button.setOpaque(false);
        button.addActionListener(java.awt.event.ActionListener {
                                     public function actionPerformed(e:java.awt.event.ActionEvent):Void {
                                         if(action <> null) 
                                            action();
                                     }
                                 });
        var self = this;
        button.addItemListener(java.awt.event.ItemListener {
                public function itemStateChanged(e:java.awt.event.ItemEvent):Void {
                    var seq = for(x in buttonGroup.buttons where x == this) sizeof x;
                    var ndx = 0;
                    if(sizeof seq > 0) {
                        ndx = seq[0];
                    }
                    selected = button.isSelected();
                    //onChange(selected);
                    if (selected) {
                         buttonGroup.selection = ndx;
                    }
                }
            });
    }
}




