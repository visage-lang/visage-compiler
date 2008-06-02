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

import javafx.ui.Widget;
import javafx.ui.KeyStroke;
import javafx.ui.Icon;
import javafx.ui.Insets;
import javafx.ui.HorizontalAlignment;
import javafx.ui.VerticalAlignment;

public class CheckBox extends Widget {
    private attribute jcheckbox: javax.swing.JCheckBox = javax.swing.JCheckBox{} on replace {
        jcheckbox.setOpaque(false);
        jcheckbox.addActionListener(java.awt.event.ActionListener {
                                        public function actionPerformed(e:java.awt.event.ActionEvent):Void {
                                            selected = jcheckbox.isSelected();
                                            if(onChange <> null) {
                                                onChange(selected);
                                            }
                                            if(action <> null) {
                                                action();
                                            }
                                        }
                                    });
    };
    public attribute text: String on replace  {
        jcheckbox.setText(text);
    };
    public attribute mnemonic: KeyStroke on replace {
        jcheckbox.setMnemonic(mnemonic.id);
    };
    public attribute icon: Icon on replace {
        var sicon:javax.swing.Icon = null;
        if (icon <> null) {
            sicon = icon.getIcon();
        }
        jcheckbox.setIcon(sicon);
    };
    public attribute selectedIcon: Icon on replace {
        var sicon:javax.swing.Icon = null;
        if (icon <> null) {
            sicon = selectedIcon.getIcon();
        }
        jcheckbox.setSelectedIcon(sicon);
    };
    public attribute pressedIcon: Icon on replace {
        var sicon:javax.swing.Icon = null;
        if (icon <> null) {
            sicon = pressedIcon.getIcon();
        }
        jcheckbox.setPressedIcon(sicon);
    };
    public attribute rolloverIcon: Icon on replace {
        var sicon:javax.swing.Icon = null;
        if (icon <> null) {
            sicon = rolloverIcon.getIcon();
        }
        jcheckbox.setRolloverIcon(sicon);
    };
    public attribute rolloverSelectedIcon: Icon on replace {
        var sicon:javax.swing.Icon = null;
        if (icon <> null) {
            sicon = rolloverSelectedIcon.getIcon();
        }
        jcheckbox.setRolloverSelectedIcon(sicon);
    };
    public attribute disabledIcon: Icon on replace {
        var sicon:javax.swing.Icon = null;
        if (icon <> null) {
            sicon = disabledIcon.getIcon();
        }
        jcheckbox.setDisabledIcon(sicon);
    };
    public attribute disabledSelectedIcon: Icon on replace {
        var sicon:javax.swing.Icon = null;
        if (icon <> null) {
            sicon = disabledSelectedIcon.getIcon();
        }
        jcheckbox.setDisabledSelectedIcon(sicon);
    };
    public attribute iconTextGap: Number on replace {
        jcheckbox.setIconTextGap(iconTextGap.intValue());
    };
    public attribute contentAreaFilled: Boolean on replace {
        jcheckbox.setContentAreaFilled(contentAreaFilled);
    };
    public attribute focusPainted:Boolean on replace {
        jcheckbox.setFocusPainted(focusPainted);
    };
    public attribute borderPainted:Boolean on replace  {
        jcheckbox.setBorderPainted(borderPainted);
    };
    public attribute borderPaintedFlat: Boolean on replace  {
        jcheckbox.setBorderPaintedFlat(borderPaintedFlat);
    }
    public attribute action: function():Void;
    public attribute selected: Boolean on replace {
        jcheckbox.setSelected(selected);
    };
    public attribute margin:Insets on replace  {
        jcheckbox.setMargin(margin.awtinsets);
    };
    public attribute horizontalTextPosition:HorizontalAlignment = HorizontalAlignment.TRAILING
        on replace {
            jcheckbox.setHorizontalTextPosition(horizontalTextPosition.id.intValue());
            jcheckbox.revalidate(); 
        };
    public attribute verticalTextPosition:VerticalAlignment = VerticalAlignment.CENTER
            on replace {
                jcheckbox.setVerticalTextPosition(verticalTextPosition.id.intValue());
                jcheckbox.revalidate(); 
            };
    public attribute horizontalAlignment:HorizontalAlignment = HorizontalAlignment.LEADING 
        on replace {
            jcheckbox.setHorizontalAlignment(horizontalAlignment.id.intValue());
            jcheckbox.revalidate();
        };
    public attribute verticalAlignment:VerticalAlignment = VerticalAlignment.CENTER
        on replace {
            jcheckbox.setVerticalAlignment(verticalAlignment.id.intValue());
            jcheckbox.revalidate(); 
        };            
    public attribute onChange: function(newValue:Boolean):Void;
    public function createComponent():javax.swing.JComponent{
        return jcheckbox;
    }

}

