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

import java.lang.Object;
import java.lang.System;
import javafx.ui.Widget;
import javafx.ui.ComboBoxCell;
import javafx.ui.SingleSelection;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;

public class ComboBox extends Widget {
    attribute listeners:javax.swing.event.ListDataListener[];
    attribute jcombobox:JComboBox = JComboBox {
            public function toString():String {
                //TODO not sure this is right, the original would have been the JCombox "this"
                //TODO the above TODO, if I understand it, has been fixed by JFXC-767.
		// If this changes what should be coded below...
                return getClass().getName()+"@"+System.identityHashCode(this);
            }
        };
    public attribute selection: Integer = -1 on replace {
        if (selection >= 0 and selection < sizeof cells) {
            jcombobox.setSelectedIndex(selection);
            jcombobox.repaint();
        }
    };
    
    public attribute cells: ComboBoxCell[]= [ComboBoxCell{text: " "}]
    on replace oldValue[lo..hi]=newVals {
        
        if(lo <= hi) { 
            var e:javax.swing.event.ListDataEvent;
            e = new javax.swing.event.ListDataEvent(this, e.INTERVAL_REMOVED,
                                                        lo, hi);
            for (i in listeners) {
                i.intervalRemoved(e);
            }
        }

        var newHi = sizeof newVals - 1;
        if(newHi > 0) {
            var e:javax.swing.event.ListDataEvent;
            e = new javax.swing.event.ListDataEvent(this, e.INTERVAL_ADDED,
                                                        lo, lo + newHi);
            for (i in listeners) {
                i.intervalAdded(e);
            }
        }
        var ndx = lo;
        var sel = selection;        
        for(cell in newVals) {
            cell.combobox = this;
            if (ndx == sel) {
                var selNdx = ndx;
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                          public function run():Void {
                               jcombobox.setSelectedIndex(selNdx);
                          }
                });                
                
            }
            ndx++;            
        }        
        
    };
    

    public attribute editable: Boolean on replace {
        jcombobox.setEditable(editable);
    };
    public attribute lightweightPopupEnabled: Boolean on replace {
        jcombobox.setLightWeightPopupEnabled(lightweightPopupEnabled);
    };
    public attribute maximumRowCount: Number on replace {
        jcombobox.setMaximumRowCount(maximumRowCount.intValue());
    };
    public attribute action: function():Void;
    public attribute value: String; 
    private attribute textField:TextField = TextField{value: bind value with inverse};
    private attribute jtextField:JTextField = textField.getComponent() as JTextField;
    public function createComponent():javax.swing.JComponent {
        //jcombobox.setLightWeightPopupEnabled(false);
        jcombobox.setSelectedIndex(-1);
        jcombobox.addActionListener(ActionListener {
                 public function actionPerformed(e:ActionEvent) {
                    if (action <> null and jcombobox.getSelectedIndex() >= 0) {
                       action();
                    }
                 }
            });
        jcombobox.setEditor(ComboBoxEditor {
                public function setItem(anItem:Object):Void {
                    var c = anItem as ComboBoxCell;
                    var value = c.value;
                    jtextField.setText("{value}");
                }
                public function getItem():Object {
                    //var str = jtextField.getText();
                    return cells[selection] as Object;
                }
                public function selectAll():Void {
                    jtextField.selectAll();
                    jtextField.requestFocus();
                }
                public function getEditorComponent():java.awt.Component {
                    return textField.getComponent();
                }
                public function addActionListener(l:ActionListener):Void {
                    //jtextField.addActionListener(l);
                }
                public function removeActionListener(l:ActionListener):Void {
                    //jtextField.removeActionListener(l);
                }
            });
        jcombobox.setModel(javax.swing.ComboBoxModel {
                       public function getSize():Integer {
                           return sizeof cells;
                       }
                       public function getElementAt(i:Integer):Object {
                           return cells[i];
                       }
                       public function addListDataListener(l:javax.swing.event.ListDataListener):Void {
                           insert l into listeners;
                       }
                       public function removeListDataListener(l:javax.swing.event.ListDataListener):Void {
                           delete l from listeners;
                       }
                       public function setSelectedItem(anItem:Object):Void {
                           var cell = anItem as ComboBoxCell;
                           var i = for(x in cells where x == cell) indexof x; 
                           if (sizeof i > 0) {
                               selection = i[0];
                               jtextField.setText("{cells[selection].value}");
                           }
                       }
                       public function getSelectedItem():Object {
                           var i = selection;
                           return if (i >= 0) cells[i] else null;
                       }
                   });
        jcombobox.addItemListener(java.awt.event.ItemListener{
                                      public function itemStateChanged(e:java.awt.event.ItemEvent):Void {
                                          selection  = jcombobox.getSelectedIndex();
                                      }
                                  });
        jcombobox.setRenderer(javax.swing.ListCellRenderer {
                                  public function getListCellRendererComponent(
                                        jlist:javax.swing.JList, obj:Object,
                                        i:Integer, selected:Boolean, 
                                        focused:Boolean):java.awt.Component {
                                     var cell = obj as ComboBoxCell;
                                     return UIElement.context.getListCellRendererComponent(jlist,
                                                          cell.text,
                                                          i, selected, focused,
                                                          cell.toolTipText);
                                 }
                                 public function equals(o:Object) {
                                    //TODO NOT Sure about this
                                     return o == this;
                                 }
                              });        
        if (( selection == -1) and (cells <> [] ) ) { selection = 0; }
        return jcombobox;
    }
    init {
        if(cursor == null)
            cursor = Cursor.DEFAULT;

    }
}

