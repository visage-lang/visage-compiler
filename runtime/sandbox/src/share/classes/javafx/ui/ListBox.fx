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
import javax.swing.*;
import javafx.ui.ScrollableWidget;
import javafx.ui.ListCell;
import javafx.ui.ListLayoutOrientation;
import javafx.ui.MultiSelection;
import javafx.ui.Border;
import javafx.ui.LineBorder;
import javafx.ui.Label;

import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.TransferHandler;
import java.util.List;


/**
 * A component that allows the user to select one or more objects from a
 * list. Encapsulates javax.swing.JList.
 */

public class ListBox extends ScrollableWidget {
    
    private attribute selectedCell:ListCell;
    attribute updateGeneration: Number;
    private attribute selectionGeneration: Number;
    
    /***************************************************************
     * Drag N Drop
     **************************************************************/  
    private attribute transferHandler: TransferHandler;
    /**
     * inidicates if a color drop changes the foreground color of the
     * selected text, if false, it changes the background color.
     * If not text is selected the entire text is changed.
     */
    public attribute colorDropChangesForeground:Boolean = true;
    
    /**
     * Indicates the drop mode which specifies which 
     * default drop action will occur. Only valid values for 
     * TextComponents are USE_SELECTION, ON, INSERT or ON_OR_INSERT
     */
    public attribute dropMode:DropMode = DropMode.USE_SELECTION on replace {
        if(dropMode != DropMode.USE_SELECTION and dropMode != DropMode.INSERT and
            dropMode != DropMode.ON and dropMode != DropMode.ON_OR_INSERT) {
            System.out.println("Illegal drop mode for text component,");
            System.out.println("only USE_SELECTION,INSERT, ON, or ON_OR_INSERT are allowed.");
            System.out.println("Reverting to USE_SELECTION");
            dropMode = DropMode.USE_SELECTION;
        }else {
            if(list != null)
                UIElement.context.setDropMode(dropMode.id, list);
        }
    };
    
    /**
     * Indicates if Drag and Drop is enabled.
     */    
    public attribute enableDND: Boolean on replace {
        if (enableDND) {
            if (transferHandler == null and list != null) {
                addTransferHandler();
            }
            if(list != null)
                list.setDragEnabled(true);
        } else if (list != null) {
            list.setDragEnabled(false);
        }
    };
    
    private function acceptDrop(value:Object):Boolean{
        if (this.canAcceptDrop != null) {
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
                transferData: value
                listIndex: i
            };
            return (this.canAcceptDrop)(e);
        }
        return onDrop != null;
    }
    private function getDragValue(): java.lang.Object  {
        var result:ListCell[] = [];
        //TODO MULTIPLE SELECTION
        //for (i in selection) {
        //    insert cells[i].value into result;
        //}
        insert cells[selection] into result;
        
        return result as java.lang.Object;
    };
    private bound function getDragText(): String  {
        return if(cells[selection].dragText != null) then
            cells[selection].dragText else
            cells[selection].text;

//TODO MULTIPLE SELECTION
/********************************
        var selectedCells:ListCell[] = [];
        
        for (i in selection) {
            insert cells[i] into selectedCells;
        }
         fix me...
        return if (selectedCells[0].dragText != null) then
            selectedCells[0].dragText else selectedCells[0].text;
*******************/
    }
    private function setDropValue(value:Object):Void {
        if (onDrop != null) {
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
                transferData: value
                listIndex: i
            };
            onDrop(e);
        }
    }

    
    private  function addTransferHandler():Void {
        UIElement.context.addTransferHandler(list,
            dropType,
            com.sun.javafx.api.ui.ValueGetter {
                public
                function get():Object {
                    return getDragValue();
                }
            },
            com.sun.javafx.api.ui.ValueSetter {
                public function set(value:Object):Void {
                    if(onDrop != null) {
                        setDropValue(value);
                    }
                }
            },
            com.sun.javafx.api.ui.ValueAcceptor {
                public
                function accept(value:Object):Boolean {
                    return if(onDrop != null and enableDND) {
                        acceptDrop(value);
                    } else {
                        enableDND;
                    };
                }
                public
                function dragEnter(value:Object):Void{/* empty */ }
                public
                function dragExit(value:Object):Void{/* empty */ }
            },
            com.sun.javafx.api.ui.VisualRepresentation {
                public
                function getComponent(value:Object):java.awt.Component {
                    var label = Label {
                        opaque: true
                        border: LineBorder {
                            lineColor: Color.BLACK
                        }
                        text: getDragText()
                    };
                    return label.getComponent();
                }
                public
                function getIcon(list:Object):javax.swing.Icon { return null; }
            });
        transferHandler = list.getTransferHandler();
    }
    
    /**
     * Optional handler called when the user drops an object
     */
    public attribute onDrop: function(e:ListDropEvent):Void = function (e:ListDropEvent):Void {
        var values:Object[] = e.transferData;
        var selectedNdx = selection;
        for(value in values) {
            if(value instanceof java.awt.Color) {
                if(colorDropChangesForeground) {
                    cellForeground = Color.fromAWTColor(value as java.awt.Color);
                }else {
                    cellBackground = Color.fromAWTColor(value as java.awt.Color);
                }
            }else {
                var newCells:ListCell[] = [];
                if (value instanceof List) {
                    var iter = (value as List).iterator();

                    while(iter.hasNext()) {
                        var c = iter.next();
                        if(c instanceof ListCell) {
                            insert (c as ListCell) into newCells;
                        }else {
                            insert ListCell {text:"{c}", value:c} into newCells;
                        }
                    }
                }else if (value instanceof javax.swing.tree.TreePath) {
                    var comp = (value as javax.swing.tree.TreePath).getLastPathComponent();
                    var newCell = ListCell {text:"{comp}", value:comp };
                    insert  newCell into newCells;
                }else if (value instanceof ListCell) {
                    insert (value as ListCell) into newCells;
                }else {
                    var newCell = ListCell {text:"{value}", value:value };
                    insert  newCell into newCells;
                }                            
                if(dropMode == DropMode.INSERT) {
                    var ndx = e.listIndex;
                    var dropPoint = new Point(e.x, e.y);
                    var r = list.getCellBounds(ndx,ndx);
                    if(r != null) {
                        if(r.y+r.height/2 > e.y) { //insert before
                            var head = cells[0..<ndx];
                            var tail =  cells[ndx..];
                            cells = [ head, newCells, tail];
                        }else { // insert after
                            var head = cells[0..ndx];
                            var next = ndx + 1;
                            var tail =  cells[next..];
                            cells = [ head, newCells, tail]; 
                        }
                    }else {
                        insert newCells into cells;
                    }                              
                }else if(dropMode == DropMode.ON) { //TODO
                    var ndx = e.listIndex;
                    if(ndx < 0) {
                        insert newCells into cells;
                    }else {
                        var head = cells[0..<ndx];
                        var next = ndx + 1;
                        var tail:ListCell[] = [];
                        if(next < sizeof cells)
                            tail = cells[next..];
                        cells = [ head, newCells, tail];
                    }                 
                }else if(dropMode == DropMode.ON_OR_INSERT) { //TODO
                    // When we go to JDK 1.6 it would be better to use the DropLocation
                    // but, for now, we fudge it from the drop point, +- 5.
                    var ndx = e.listIndex;
                    var dropPoint = new Point(e.x, e.y);
                    var r = list.getCellBounds(ndx,ndx);
                    if(r != null) {
                        if(r.y+5 > e.y) { //insert before
                            var head = cells[0..<ndx];
                            var tail =  cells[ndx..];
                            cells = [ head, newCells, tail];
                        }else if(e.y > r.y+r.height-5) { // insert after
                            var head = cells[0..ndx];
                            var next = ndx + 1;
                            var tail = cells[next..];
                            cells = [ head, newCells, tail]; 
                        } else  { // ON
                            var head = cells[0..<ndx];
                            var next = ndx + 1;
                            var tail:ListCell[] = [];
                            if(next < sizeof cells)
                                tail = cells[next..];
                            cells = [ head, newCells, tail];
                        }
                    }else {
                        insert newCells into cells;
                    }
                }else { // if(dropMode == DropMode.USE_SELECTION) {
                    if(selectedNdx < 0) {
                        insert newCells into cells;
                    }else {
                        var head = cells[0..<selectedNdx.intValue()];
                        var next = selectedNdx.intValue() + 1;
                        var tail:ListCell[] = [];
                        if(next < sizeof cells)
                            tail = cells[next..];
                        cells = [ head, newCells, tail];
                    }                             
                }                            
            }
        }
    };
    /**
     * Optional filter for the types of objects that may be dropped onto this textfield.
     * 
     */
    public attribute dropType: java.lang.Class;
    /**
     * <code>attribute acceptDrop: function(value): Boolean</code><br></br>
     * Optional handler called when the user drops an object onto this textfield.
     * If it returns false, the drop is rejected. 
     */
    public attribute canAcceptDrop: function(e:ListDropEvent):Boolean = function (e:ListDropEvent):Boolean {
        return enableDND;
    };  
    
    
    /***************************************************************
     * END Drag N Drop
     **************************************************************/        
    private attribute dirty: Boolean;
    // in Widget: private attribute keyListener: java.awt.event.KeyListener;
    private attribute listMouseListener: java.awt.event.MouseListener;
    
    // TODO MARK AS FINAL
    attribute list: javax.swing.JList = javax.swing.JList{};
    
    private attribute selectionListener:javax.swing.event.ListSelectionListener;
    attribute listeners:javax.swing.event.ListDataListener[];
    private attribute listmodel:javax.swing.ListModel;
    public attribute cellBackground: AbstractColor on replace {
        if(list != null) {
            list.setCellRenderer(makeCellRenderer());
        }
    };
    public attribute cellForeground: AbstractColor on replace {
        if(list != null) {
            list.setCellRenderer(makeCellRenderer());
        }
    };
    public attribute selectedCellBackground: AbstractColor on replace {
        if(list != null) {
            list.setCellRenderer(makeCellRenderer());
        }
    };
    public attribute selectedCellForeground: AbstractColor on replace {
        if(list != null) {
            list.setCellRenderer(makeCellRenderer());
        }
    };


    /** 
     * If true the list will not reflect changes to its cells attribute. 
     * When changed back to false synchronization will occur. Can be used 
     * as a performance optimizationwhen when doing large batch updates.
     */
    public attribute locked: Boolean = true on replace {
        if (not locked) {
            list.removeListSelectionListener(selectionListener);
            if (dirty) {
                dirty = false;
                delete listeners;
                list.setModel(new javax.swing.DefaultListModel()) ;
                list.setModel(listmodel);
                list.setSelectedIndex(selection);
                list.ensureIndexIsVisible(selection);
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
    public attribute layoutOrientation: ListLayoutOrientation = ListLayoutOrientation.VERTICAL on replace {
        if(layoutOrientation != null) {
            list.setLayoutOrientation(layoutOrientation.id.intValue());
        }
    };
//TODO MULTIPLE SELECTION
    /** Returns the index of the selected cell or -1 if no cell is selected. */
    public attribute selection:Integer = -1 on replace old {
        if (not locked) {
            list.removeListSelectionListener(selectionListener);
            list.setSelectedIndex(selection);
            if (selection >= 0) {
                if(old >= 0) {
                    cells[old.intValue()].selected = false;
                }
                cells[selection].selected = true;
                list.ensureIndexIsVisible(selection);
            }
            list.addListSelectionListener(selectionListener);
        }
    };
    /**
     * Returns the preferred number of visible rows.
     */
    public attribute visibleRowCount: Number = 8 on replace  {
        list.setVisibleRowCount(visibleRowCount.intValue());
        list.revalidate();
    };
    /**
     * If present, sets the height of every cell in the list. 
     */
    public attribute fixedCellHeight: Number = -1 on replace {
        list.setFixedCellHeight(fixedCellHeight.intValue());
    };
    /**
     * If present, sets the width of every cell in the list. 
     */
    public attribute fixedCellWidth: Number = -1 on replace {
        list.setFixedCellWidth(fixedCellWidth.intValue());
    };
    /** The cells of this list. */
    public attribute cells: ListCell[] on replace oldValue[lo..hi]=newVals {
        for(k in [lo..hi]) { 
            updateGeneration = updateGeneration + 1;
            if (not locked) {
                var s = selection;
                if (k == s) {
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
        }
        if(lo <= hi) {
            var e:javax.swing.event.ListDataEvent =
                e = new javax.swing.event.ListDataEvent(list, e.INTERVAL_REMOVED, lo, hi);
            var ls:javax.swing.event.ListDataListener[] = [];
            insert listeners into ls;
            for (j in ls) {
                j.intervalRemoved(e);
            }
        }
        var ndx = lo;
        for(cell in newVals) {
            cell.listbox = this;
            //TODO ++
            //cell.cacheGeneration = ++updateGeneration;
            updateGeneration = updateGeneration + 1;
            cell.cacheGeneration = updateGeneration;
            cell.myIndex = ndx;
            if (not locked) {
                var selected = ndx == selection or sizeof cells == 1 ;
                if (selected or cell.selected) {
                    list.removeListSelectionListener(selectionListener);
                    list.setSelectedIndex(ndx);
                    selection = ndx;
                    list.addListSelectionListener(selectionListener);
                }
            } else {
                dirty = true;
            }
            ndx++
        }
        if (sizeof newVals > 0 and not locked) {
            var endNdx = sizeof newVals-1;
            var e:javax.swing.event.ListDataEvent = 
                new javax.swing.event.ListDataEvent(list, e.INTERVAL_ADDED, lo, lo + endNdx);
            var ls:javax.swing.event.ListDataListener[] = [];
            insert listeners into ls;
            for (j in ls) {
                j.intervalAdded(e);
            }  
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
            if (keyListener != null) {
                list.removeKeyListener(keyListener);
            }
            if (listMouseListener != null) {
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


    public function locationToIndex(x:Number, y:Number):Integer {
        return list.locationToIndex(new java.awt.Point(x.intValue(), y.intValue()));
    }
    public function indexToLocation(i:Number):XY{
        var pt = list.indexToLocation(i.intValue());
        return XY {x: pt.getX(), y: pt.getY()};
    }
    
    private function makeCellRenderer():javax.swing.ListCellRenderer{
            javax.swing.ListCellRenderer {
                 public function getListCellRendererComponent(jlist:javax.swing.JList, obj:Object,
                                                        i:Integer, selected:Boolean, focused:Boolean):java.awt.Component {
                     var listCell = obj as ListCell;
                     var label = UIElement.context.getListCellRendererComponent(
                                      jlist,
                                      listCell.text,
                                      i, selected, focused,
                                      listCell.toolTipText) as javax.swing.JLabel;
                    if (listCell.border != null) {
                         label.setBorder(listCell.border.getBorder());
                    }
                    if (listCell.horizontalAlignment != null) {
                        label.setHorizontalAlignment(listCell.horizontalAlignment.id.intValue());
                    } 
                    if (listCell.verticalAlignment != null) {
                        label.setVerticalAlignment(listCell.verticalAlignment.id.intValue());
                    }
                    if (selected) {
                        if (selectedCellForeground != null) {
                            label.setForeground(selectedCellForeground.getColor());
                        }
                        if (selectedCellBackground != null) {
                            label.setBackground(selectedCellBackground.getColor());
                        }
                    } else {
                        if (cellForeground != null) {
                            label.setForeground(cellForeground.getColor());
                        }
                        if (cellBackground != null) {
                            label.setBackground(cellBackground.getColor());
                        }
                    }
                    //label.setOpaque(list.isOpaque());
                    return label;
                 }
             };
    }

    public function createView():javax.swing.JComponent {
        list.setDragEnabled(enableDND);
        UIElement.context.setDropMode(dropMode.id, list);
        //TODO MULTIPLE SELECTION
        // fix me...
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        //selection = -1;
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
        list.setCellRenderer(makeCellRenderer());
             //TODO ToolTip
        //javax.swing.ToolTipManager.sharedInstance().registerComponent(list);
        list.setModel(listmodel);
        selectionListener = javax.swing.event.ListSelectionListener {
              public function valueChanged(e:javax.swing.event.ListSelectionEvent):Void {
                  if (not e.getValueIsAdjusting()) {
                      var listSelection = list.getSelectedIndex();
                      if((listSelection < 0) and (0 < sizeof cells)) {
                          listSelection = 0;
                      }else if (listSelection >= sizeof cells) {
                          listSelection = sizeof cells - 1;
                      }
                      selection = listSelection;

                  }
              }
          };
          
        if (transferHandler == null) {
            addTransferHandler();
        }else {
              list.setTransferHandler(transferHandler);
        }          
        //do later {
            locked = false;
        //}
        return list;
    }

}



