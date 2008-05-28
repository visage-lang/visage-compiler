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

import javafx.ui.MenuItem;
import javafx.ui.SelectableButton;

public class RadioButtonMenuItem extends MenuItem, SelectableButton {
    // TODO MARK AS FINAL
    private attribute jradiobuttonmenuitem:javax.swing.JRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
    
    public function createMenuItem():javax.swing.JMenuItem  {
        var self = this;
        jradiobuttonmenuitem.setSelected(selected);
        jradiobuttonmenuitem.addChangeListener(javax.swing.event.ChangeListener {
                    public function stateChanged(e:javax.swing.event.ChangeEvent):Void {
                        var i = -1;
                        for (x in buttonGroup.buttons) {
                            i = i + 1;
                            if(x == self ) {
                                break;
                            }
                        }
                        //TODO DO LATER - this is a work around until a more permanent solution is provided
                        //javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                        //          public function run():Void {
                                        if (selected == jradiobuttonmenuitem.isSelected()) {
                                            //return;
                                        }
                                        selected = jradiobuttonmenuitem.isSelected();
                                        if (self.onChange <> null) {
                                             self.onChange(selected);
                                        }
                                        if (selected) {
                                            buttonGroup.selection = i;
                                        }
                                  //}
                        //});   
                    }
                });
        return jradiobuttonmenuitem;
    }
    public function setSelected(value:Boolean):Void {
        jradiobuttonmenuitem.setSelected(value);
    }    

}



