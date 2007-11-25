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
import javax.swing.*;
import javafx.ui.ScrollableWidget;
import javafx.ui.ListCell;
import javafx.ui.ListLayoutOrientation;
import javafx.ui.MultiSelection;
import javafx.ui.Border;
import javafx.ui.LineBorder;
import javafx.ui.Label;
import java.awt.MouseInfo;
import javax.swing.TransferHandler;

class ListDropEvent extends DropEvent {
    public attribute listIndex: Integer;
}
/**
 * A component that allows the user to select one or more objects from a
 * list. Encapsulates javax.swing.JList.
 */

public class ListBox extends ScrollableWidget {
    private attribute transferHandler: TransferHandler;
    private attribute selectedCell:ListCell;
    attribute updateGeneration: Number;
    private attribute selectionGeneration: Number;
    private function acceptDrop(value:Object):Boolean{
        if (this.canAcceptDrop <> null) {
            var info = MouseInfo.getPointerInfo();
            var location = list.getLocationOnScreen();
            var p = info.getLocation();
            var x = p.getX() - location.getX();
            var y = p.getY() - location.getY();
            p.setLocation(x, y);
            var i = locationToIndex(p.getX(), p.getY());
            var e = ListDropEvent {
                x: p.getX()
                y: p.getY()
                //TODO JFXC-302
                //transferData: value
                listIndex: i
            };
            return (this.canAcceptDrop)(e);
        }
        return onDrop <> null;
    }
    private function getDragValue(): java.lang.Object  {
        var result:ListCell[] = [];
        //TODO MULTIPLE SELECTION
        //foreach (i in selection) {
        //    insert cells[i].value into result;
        //}
        insert cells[selection.intValue()] into result;
        
        return result as java.lang.Object;
    };
    private function getDragText(): String  {
        return if(cells[selection.intValue()].dragText <> null) then
            cells[selection.intValue()].dragText else
            cells[selection.intValue()].text;

//TODO MULTIPLE SELECTION
/********************************
        var selectedCells:ListCell[] = [];
        
        foreach (i in selection) {
            insert cells[i] into selectedCells;
        }
         fix me...
        return if (selectedCells[0].dragText <> null) then
            selectedCells[0].dragText else selectedCells[0].text;
*******************/
    }
    private function setDropValue(value:Object):Void {
        if (onDrop <> null) {
            var info = MouseInfo.getPointerInfo();
            var location = list.getLocationOnScreen();
            var p = info.getLocation();
            var x = p.getX() - location.getX();
            var y = p.getY() - location.getY();
            p.setLocation(x, y);
            var i = locationToIndex(p.getX(), p.getY());
            var e = ListDropEvent {
                x: p.getX()
                y: p.getY()
                //TODO JFXC-302
                //transferData: value
                listIndex: i
            };
            onDrop(e);
        }
    }
    private attribute dirty: Boolean;
    private attribute keyListener: java.awt.event.KeyListener;
    private attribute listMouseListener: java.awt.event.MouseListener;
    attribute list: javax.swing.JList;
    private attribute selectionListener:javax.swing.event.ListSelectionListener;
    attribute listeners:javax.swing.event.ListDataListener[];
    private attribute listmodel:javax.swing.ListModel;
    public attribute cellBackground: AbstractColor;
    public attribute cellForeground: AbstractColor;
    public attribute selectedCellBackground: AbstractColor;
    public attribute selectedCellForeground: AbstractColor;

    public attribute enableDND: Boolean on replace {
        if (enableDND) {
            if (transferHandler == null) {
                UIElement.context.addTransferHandler(list,
                    dropType,
                    com.sun.javafx.api.ui.ValueGetter {
                        public function get():Object {
                            return getDragValue();
                        }
                    },
                    com.sun.javafx.api.ui.ValueSetter {
                        public function set(value:Object):Void {
                            setDropValue(value);
                        }
                    },
                    com.sun.javafx.api.ui.ValueAcceptor {
                        public function accept(value:Object):Boolean {
                            return acceptDrop(value);
                        }
                        public function dragEnter(value:Object):Void{/* empty */ }
                        public function dragExit(value:Object):Void{/* empty */ }
                    },
                    com.sun.javafx.api.ui.VisualRepresentation {
                        public function getComponent(value:Object):java.awt.Component {
                            var label = Label {
                                opaque: true
                                border: LineBorder {
                                    lineColor: Color.BLACK
                                }
                                text: getDragText()
                            };
                            return label.getComponent();
                        }
                        public function getIcon(list:Object):javax.swing.Icon { return null; }
                    });
                transferHandler = list.getTransferHandler();
            }
            list.setDragEnabled(true);
        } else {
            list.setDragEnabled(false);
        }
    };
    /** 
     * If true the list will not reflect changes to its cells attribute. 
     * When changed back to false synchronization will occur. Can be used 
     * as a performance optimizationwhen when doing large batch updates.
     */
    public attribute locked: Boolean on replace {
        if (not locked) {
            list.removeListSelectionListener(selectionListener);
            if (dirty) {
                dirty = false;
                delete listeners;
                list.setModel(new javax.swing.DefaultListModel()) ;
                list.setModel(listmodel);
                list.setSelectedIndex(selection.intValue());
                list.ensureIndexIsVisible(selection.intValue());
            }
            list.addListSelectionListener(selectionListener);
        } else {
            list.removeListSelectionListener(selectionListener);
        }
    };

    /**
     * Defines the way list cells are layed out. Consider a <code>ListBox</code>
     * with four cells, this can be layed out in one of the following ways:
     * <pre>
     *   0
     *   1
     *   2
     *   3
     * </pre>
     * <pre>
     *   0  1
     *   2  3
     * </pre>
     * <pre>
     *   0  2
     *   1  3
     * </pre>
     * <p>
     * These correspond to the following values:
     *
     * <table border="1" 
     *  summary="Describes layouts VERTICAL, HORIZONTAL_WRAP, and VERTICAL_WRAP">
     *   <tr><th><p align="left">Value</p></th><th><p align="left">Description</p></th></tr>
     *   <tr><td><code>VERTICAL</code>
     *       <td>The cells should be layed out vertically in one column.
     *   <tr><td><code>HORIZONTAL_WRAP</code>
     *       <td>The cells should be layed out horizontally, wrapping to
     *           a new row as necessary.  The number
     *           of rows to use will either be defined by
     *           <code>visibleRowCount</code> if > 0, otherwise the
     *           number of rows will be determined by the width of the 
     *           <code>ListBox</code>.
     *   <tr><td><code>VERTICAL_WRAP</code>
     *       <td>The cells should be layed out vertically, wrapping to a
     *           new column as necessary.  The number
     *           of rows to use will either be defined by
     *           <code>visibleRowCount</code> if > 0, otherwise the
     *           number of rows will be determined by the height of the 
     *           <code>ListBox</code>.
     *  </table>
     * The default value of this property is <code>VERTICAL</code>.
     * <p>
     */
    public attribute layoutOrientation: ListLayoutOrientation on replace {
        if(layoutOrientation <> null) {
            list.setLayoutOrientation(layoutOrientation.id.intValue());
        }
    };
//TODO MULTIPLE SELECTION
    /** Returns the index of the selected cell or -1 if no cell is selected. */
    public attribute selection: Number = -1 on replace (old) {
        if (not locked) {
            list.removeListSelectionListener(selectionListener);
            list.setSelectedIndex(selection.intValue());
            if (selection >= 0) {
                cells[old.intValue()].selected = false;
                cells[selection.intValue()].selected = true;
                list.ensureIndexIsVisible(selection.intValue());
            }
            list.addListSelectionListener(selectionListener);
        }
    };
    /**
     * Returns the preferred number of visible rows.
     */
    public attribute visibleRowCount: Number on replace  {
        list.setVisibleRowCount(visibleRowCount.intValue());
        list.revalidate();
    };
    /**
     * If present, sets the height of every cell in the list. 
     */
    public attribute fixedCellHeight: Number on replace {
        list.setFixedCellHeight(fixedCellHeight.intValue());
    };
    /**
     * If present, sets the width of every cell in the list. 
     */
    public attribute fixedCellWidth: Number on replace {
        list.setFixedCellWidth(fixedCellWidth.intValue());
    };
    /** The cells of this list. */
    public attribute cells: ListCell[]
        on insert [ndx] (cell) {
            cell.listbox = this;
            //TODO ++
            //cell.cacheGeneration = ++updateGeneration;
            updateGeneration = updateGeneration + 1;
            cell.cacheGeneration =updateGeneration;
            cell.myIndex = ndx;
            if (not locked) {
                var e:javax.swing.event.ListDataEvent;
                var selected = ndx == selection or sizeof cells == 1 ;
                e = new javax.swing.event.ListDataEvent(list, e.INTERVAL_ADDED, ndx, ndx);
                var ls:javax.swing.event.ListDataListener[] = [];
                //TODO JXFC-300
                //insert listeners into ls;
                foreach (l in listeners) {
                    insert l into ls;
                }
                foreach (j in ls) {
                    j.intervalAdded(e);
                }
                if (selected or cell.selected) {
                    list.removeListSelectionListener(selectionListener);
                    list.setSelectedIndex(ndx);
                    selection = ndx;
                    list.addListSelectionListener(selectionListener);
                }
            } else {
                dirty = true;
            }
        }

        on delete [ndx] (cell) {
            //TODO ++
            //++updateGeneration;
            updateGeneration = updateGeneration + 1;
            if (not locked) {
                var e:javax.swing.event.ListDataEvent;
                var s = selection;
                e = new javax.swing.event.ListDataEvent(list, e.INTERVAL_REMOVED, ndx, ndx);
                var ls:javax.swing.event.ListDataListener[] = [];
                //TODO JXFC-300
                //insert listeners into ls;
                foreach (l in listeners) {
                    insert l into ls;
                }
                foreach (j in ls) {
                    j.intervalRemoved(e);
                }
                if (ndx == s) {
                    if (s >= sizeof cells) {
                        s = sizeof cells -1;
                    }
                    if (s >= 0) {
                       list.setSelectedIndex(s.intValue());
                    }
                }
            } else {
                dirty = true;
            }
        };
    private attribute onSelectionChange: function(oldSelection:MultiSelection, newSelection:MultiSelection):Void;
    /** 
     * <code>attribute action: function()</code><br></br>
     * Optional handler called when the user double-clicks on a list cell or
     * presses the ENTER key.
     */
    public attribute action: function():Void on replace  {
        if (action == null) {
            if (keyListener <> null) {
                list.removeKeyListener(keyListener);
            }
            if (listMouseListener <> null) {
                list.removeMouseListener(listMouseListener);
            }
        } else {
            if (keyListener == null) {
                keyListener = java.awt.event.KeyAdapter {
                    public function keyPressed(e:java.awt.event.KeyEvent):Void {
                        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                            action();
                        }
                    }
                } as java.awt.event.KeyListener;
                listMouseListener = java.awt.event.MouseAdapter {
                    public function mouseClicked(e:java.awt.event.MouseEvent):Void {
                        if (e.getClickCount() == 2 and
                            javax.swing.SwingUtilities.isLeftMouseButton(e)) {
                                action();
                        }
                    }
                } as java.awt.event.MouseListener;
            }
            list.addKeyListener(keyListener);
            list.addMouseListener(listMouseListener);
        }
    };
    /**
     * Optional handler called when the user drops an object into this list.
     */
    public attribute onDrop: function(e:ListDropEvent);
    /**
     * Optional filter for the types of objects that may be dropped into this
     * list.
     */
    public attribute dropType: java.lang.Class;
    /**
     * <code>attribute acceptDrop: function(value): Boolean</code><br></br>
     * Optional handler called when the user drops an object into this list.
     * If it returns false, the drop is rejected. 
     */
    public attribute canAcceptDrop: function(e:ListDropEvent):Boolean;

    public function locationToIndex(x:Number, y:Number):Integer {
        return list.locationToIndex(new java.awt.Point(x.intValue(), y.intValue()));
    }
    public function indexToLocation(i:Number):XY{
        var pt = list.indexToLocation(i.intValue());
        return XY {x: pt.getX(), y: pt.getY()};
    }

    public function createView():javax.swing.JComponent {
        layoutOrientation = ListLayoutOrientation.VERTICAL;
        list = javax.swing.JList{};
        //TODO MULTIPLE SELECTION
        // fix me...
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        selection = -1;
        listmodel = javax.swing.ListModel {
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
        };
        list.setCellRenderer(javax.swing.ListCellRenderer {
                 public function getListCellRendererComponent(jlist:javax.swing.JList, obj:Object,
                                                        i:Integer, selected:Boolean, focused:Boolean):java.awt.Component {
                     var listCell = obj as ListCell;
                     var label = UIElement.context.getListCellRendererComponent(
                                      jlist,
                                      listCell.text,
                                      i, selected, focused,
                                      listCell.toolTipText) as javax.swing.JLabel;
                    if (listCell.border <> null) {
                         label.setBorder(listCell.border.getBorder());
                    }
                    if (listCell.horizontalAlignment <> null) {
                        //TODO JXFC-303
                        //label.setHorizontalAlignment(listCell.horizontalAlignment.id.intValue());
                    } 
                    if (listCell.verticalAlignment <> null) {
                        //TODO JXFC-303
                        //label.setVerticalAlignment(listCell.verticalAlignment.id.intValue());
                    }
                    if (selected) {
                        if (selectedCellForeground <> null) {
                            label.setForeground(selectedCellForeground.getColor());
                        }
                        if (selectedCellBackground <> null) {
                            label.setBackground(selectedCellBackground.getColor());
                        }
                    } else {
                        if (cellForeground <> null) {
                            label.setForeground(cellForeground.getColor());
                        }
                        if (cellBackground <> null) {
                            label.setBackground(cellBackground.getColor());
                        }
                    }
                    //label.setOpaque(list.isOpaque());
                    return label;
                 }
                 public function equals(o:Object):Boolean {
                     return o == this;
                 }
             });
        javax.swing.ToolTipManager.sharedInstance().registerComponent(list);
        list.setModel(listmodel);
        selectionListener = javax.swing.event.ListSelectionListener {
              public function valueChanged(e:javax.swing.event.ListSelectionEvent):Void {
                  if (not e.getValueIsAdjusting()) {
                          selection = list.getSelectedIndex();

                  }
              }
          };
        //do later {
            locked = false;
        //}
        return list;
    }

    init {
        locked = true;
    }

}



