/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
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
    attribute jcombobox:JComboBox;

    public attribute cells: ComboBoxCell[]= [ComboBoxCell{text: " "}]
    on replace oldValue[lo..hi]=newVals {
        if(lo < hi) { 
            var e:javax.swing.event.ListDataEvent;
            e = new javax.swing.event.ListDataEvent(this, e.INTERVAL_REMOVED,
                                                        lo, hi);
            for (i in listeners) {
                i.intervalRemoved(e);
            }
        }
        var ndx = lo;
        for(cell in newVals) {
            cell.combobox = this;
            var sel = selection;

            if (ndx == sel) {
                jcombobox.setSelectedIndex(ndx);
            }
            ndx++
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
    };
    
    public attribute selection: Number on replace {
        if (selection >= 0 and selection < sizeof cells) {
            jcombobox.setSelectedIndex(selection.intValue());
            jcombobox.repaint();
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
    private attribute textField:TextField = TextField{value: bind value};
    private attribute jtextField:JTextField = textField.getComponent() as JTextField;
    public function createComponent():javax.swing.JComponent {
        return jcombobox;
    }
    init {
        cursor = Cursor.DEFAULT;
        selection = -1;
        jcombobox = JComboBox {
            public function toString():String {
                //TODO not sure this is right, the original would have been the JCombox "this"
                return getClass().getName()+"@"+System.identityHashCode(this);
            }
        };
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
                    return cells[selection.intValue()] as Object;
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
                               jtextField.setText("{cells[selection.intValue()].value}");
                           }
                       }
                       public function getSelectedItem():Object {
                           var i = selection.intValue();
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
    }
}

