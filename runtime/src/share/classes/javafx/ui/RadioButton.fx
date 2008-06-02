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

import javafx.ui.SelectableButton;
import javafx.ui.Icon;
import javafx.ui.KeyStroke;
import javafx.ui.Insets;
import javafx.ui.HorizontalAlignment;
import javafx.ui.VerticalAlignment;

public class RadioButton extends SelectableButton {
    // TODO MARK AS FINAL
    private attribute jradiobutton: javax.swing.JRadioButton = new javax.swing.JRadioButton() on replace {
        jradiobutton.setOpaque(false);
    };
    public attribute text:String on replace {
        jradiobutton.setText(text);
    };
    public attribute mnemonic: KeyStroke on replace {
        if (mnemonic <> null) {
            jradiobutton.setMnemonic(mnemonic.id.intValue());
        }
    };
    public attribute icon: Icon on replace {
        var sicon = null;
        if (icon <> null) {
            sicon = icon.getIcon();
        }
        jradiobutton.setIcon(sicon);
        if (selectedIcon == null) {
            jradiobutton.setSelectedIcon(sicon);
        }        
    };
    public attribute selectedIcon: Icon on replace {
        var jicon = null;
        if (selectedIcon <> null) {
            jicon = selectedIcon.getIcon();
        }
        jradiobutton.setSelectedIcon(jicon);        
    };
    public attribute pressedIcon: Icon on replace {
        var jicon = null;
        if (pressedIcon <> null) {
            jicon = pressedIcon.getIcon();
        }
        jradiobutton.setPressedIcon(jicon);        
    };
    public attribute rolloverIcon: Icon on replace {
        var jicon = null;
        if (rolloverIcon <> null) {
            jicon = rolloverIcon.getIcon();
        }
        jradiobutton.setRolloverIcon(jicon);        
    };
    public attribute rolloverSelectedIcon: Icon on replace {
        var jicon = null;
        if (rolloverSelectedIcon <> null) {
            jicon = rolloverSelectedIcon.getIcon();
        }
        jradiobutton.setRolloverSelectedIcon(jicon);        
    };
    public attribute disabledIcon: Icon on replace {
        var jicon = null;
        if (disabledIcon <> null) {
            jicon = disabledIcon.getIcon();
        }
        jradiobutton.setDisabledIcon(jicon);        
    };
    public attribute disabledSelectedIcon: Icon on replace {
        var jicon = null;
        if (disabledSelectedIcon <> null) {
            jicon = disabledSelectedIcon.getIcon();
        }
        jradiobutton.setDisabledSelectedIcon(jicon);        
    };
    public attribute iconTextGap: Number on replace {
        jradiobutton.setIconTextGap(iconTextGap.intValue());
    };
    public attribute contentAreaFilled: Boolean on replace {
        jradiobutton.setContentAreaFilled(contentAreaFilled);
        jradiobutton.revalidate();        
    };
    public attribute focusPainted:Boolean on replace {
        jradiobutton.setFocusPainted(focusPainted);
        jradiobutton.revalidate();
    };
    public attribute borderPainted:Boolean on replace {
        jradiobutton.setBorderPainted(borderPainted);
        jradiobutton.revalidate();        
    };
    public attribute action: function():Void;
    public attribute margin:Insets on replace {
        jradiobutton.setMargin(margin.awtinsets);
        jradiobutton.revalidate();        
    };


    public attribute horizontalTextPosition:HorizontalAlignment = HorizontalAlignment.TRAILING 
        on replace {
            jradiobutton.setHorizontalTextPosition(horizontalTextPosition.id.intValue());
            jradiobutton.revalidate();
        };
    public attribute verticalTextPosition:VerticalAlignment = VerticalAlignment.CENTER
        on replace {
            jradiobutton.setVerticalTextPosition(verticalTextPosition.id.intValue());
            jradiobutton.revalidate();
        };
    public attribute horizontalAlignment:HorizontalAlignment = HorizontalAlignment.LEADING 
        on replace {
            jradiobutton.setHorizontalAlignment(horizontalAlignment.id.intValue());
            jradiobutton.revalidate();
        };
    public attribute verticalAlignment:VerticalAlignment = VerticalAlignment.CENTER
        on replace {
            jradiobutton.setVerticalAlignment(verticalAlignment.id.intValue());
            jradiobutton.revalidate(); 
        };

    public function setSelected(value:Boolean):Void {
        jradiobutton.setSelected(value);
    }

    public function createComponent():javax.swing.JComponent{
        var self = this;
        jradiobutton.addItemListener(java.awt.event.ItemListener {
                public function itemStateChanged(e:java.awt.event.ItemEvent):Void {
                    var i = for(x in buttonGroup.buttons where x == self) indexof x;
                    selected = jradiobutton.isSelected();
                    if(onChange <> null){
                        (onChange)(selected);
                     }
                    if (selected) {
                        buttonGroup.selection = i[0];
                    }
                }
            });
        jradiobutton.addActionListener(java.awt.event.ActionListener {
                public function actionPerformed(e:java.awt.event.ActionEvent):Void {
                    if(action <> null) {
                        action();
                    }
                }
            });
        return jradiobutton;
    }

        
}

