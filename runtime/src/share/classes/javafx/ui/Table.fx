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


import java.awt.Dimension;
import javafx.ui.ScrollableWidget;
import javafx.ui.TableAutoResizeMode;
import javafx.ui.TableCell;
import javafx.ui.TableColumn;
import java.lang.Object;
import java.awt.Component;
import java.util.List;
import java.lang.StringBuffer;
import java.awt.MouseInfo;
import java.lang.System;
import javax.swing.TransferHandler;
import java.awt.Point;

public class Table extends ScrollableWidget {
    private attribute UNSET: Integer = java.lang.Integer.MIN_VALUE;
    private attribute inSetSelection:Boolean;
    private attribute selectionListener:javax.swing.event.ListSelectionListener;
    private attribute selectionGeneration: Number;
    attribute tableModel: com.sun.javafx.api.ui.UIContextImpl.XTableCellModel;
    private attribute table:javax.swing.JTable = createTable();;
    private attribute rowcount: Number;
    private attribute dirty: Boolean;

    public attribute locked: Boolean;
    public attribute autoResizeMode: TableAutoResizeMode on replace {
        if (autoResizeMode <> null) {
            table.setAutoResizeMode(autoResizeMode.id.intValue());
        }        
    };

    public attribute visibleRowCount: Number;

    public attribute selection: Number = -1 on replace (old) {
        if (table <> null) {
            var model = table.getSelectionModel();
            if (old >= 0 and old < table.getRowCount()) {
                var c1 = old * sizeof columns;
                var c2 = c1 + sizeof columns;
                for (i in [c1..c2-1]) {
                    var c = cells[i.intValue()];
                    c.table = null;
                    c.selected = false;
                    c.table = this;
                }
            }
            if (selection < 0 or selection >= table.getRowCount()) {
                model.clearSelection();
                selection = -1;
            } else {
                if (table.getSelectedRow() <> selection) {
                    inSetSelection = true;
                    table.setRowSelectionInterval(selection.intValue(), selection.intValue());
                    inSetSelection = false;
                }
                if (true) {
                    var c1 = selection * sizeof columns;
                    var c2 = c1 + sizeof columns;
                    for (i in [c1..c2-1]) {
                        var c = cells[i.intValue()];
                        c.table = null;
                        c.selected = true;
                        c.table = this;
                    }
                }
            }
            //onSelectionChange(selection);
        }
        
    };

    public attribute rowHeight: Number = UNSET on replace {
        if(rowHeight > 0)
            table.setRowHeight(rowHeight.intValue());
    };
    public attribute rowMargin: Number = UNSET on replace {
        if(rowMargin >= 0)
            table.setRowMargin(rowMargin.intValue());
    };
    public attribute rowSelectionAllowed:Boolean on replace {
        table.setRowSelectionAllowed(rowSelectionAllowed);
    };

    public attribute showGrid: Boolean on replace {
        table.setShowGrid(showGrid);
    };
    public attribute showHorizontalLines: Boolean on replace {
        table.setShowHorizontalLines(showHorizontalLines);
    };
    public attribute showVerticalLines: Boolean on replace {
        table.setShowVerticalLines(showVerticalLines);
    };
    public attribute onSelectionChange: function():Void;

    public attribute gridColor: Color on replace {
        if(gridColor <> null and gridColor.getColor() <> null)
            table.setGridColor(gridColor.getColor());
    };
    public attribute columns: TableColumn[];
    public attribute cells: TableCell[] on replace oldValue[lo..hi]=newVals {
        var s = selection;
        if (tableModel <> null) {
            for(k in [lo..hi]) { 
                tableModel.removeCell(lo);
            }
        }
        var ndx = lo;
        for(cell in newVals) {
            cell.table = this;
            if (tableModel <> null) {
                tableModel.addCell(ndx, cell.text, cell.toolTipText,
                                   cell.font.getFont(), cell.background.getColor(), cell.foreground.getColor(),
                                   cell.border.getBorder());
                
            }
            ndx++
        }
        selection = s;
    };
    public attribute intercellSpacing: Dimension on replace {
        if(table <> null and intercellSpacing <> null)
            table.setIntercellSpacing(intercellSpacing);
    };
    protected function createTable(): javax.swing.JTable {
        return UIElement.context.createTable();
    }
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
     * default drop action will occur.
     */
    public attribute dropMode:DropMode = DropMode.USE_SELECTION on replace {
        if(table <> null) {
            UIElement.context.setDropMode(dropMode.id, table);
        }
    }
    
    /**
     * Indicates if Drag and Drop is enabled.
     */    
    public attribute enableDND: Boolean on replace {
        if (enableDND) {
            if (transferHandler == null and table <> null) {
                addTransferHandler();
            }
            if(table <> null)
                table.setDragEnabled(true);
        } else if (table <> null) {
            table.setDragEnabled(false);
        }
    };
    
    /**
     * Optional handler called when the user drops an object
     */
    public attribute onDrop: function(e:DropEvent):Void = function (e:DropEvent):Void {
        var values = e.transferData;
        for(value in values) {
            if(value instanceof java.awt.Color) {
                //TODO get cells
                if(colorDropChangesForeground) {
                    //cell.forground = Color.fromAWTColor(value as java.awt.Color);
                }else {
                    //cell.background  = Color.fromAWTColor(value as java.awt.Color);
                }
            }else {
                var newCells:TableCell[] = [];
                if (value instanceof List) {
                    var iter = (value as List).iterator();
                    while(iter.hasNext()) {
                        var c = iter.next();
                        if(c instanceof TableCell) {
                            insert(c as TableCell) into newCells;
                        }else {
                            insert TableCell {text:"{c}", value:c} into newCells;
                        }
                    }
                }else if (value instanceof TableCell) {
                    insert(value as TableCell) into newCells;
                }else if (value instanceof String) { // CSV translates to TableCells.
                    var st = new java.util.StringTokenizer((value as String), ",");
                    var tokens:String[] = [];
                    while(st.hasMoreTokens()) {
                        insert st.nextToken() into tokens;
                    }
                    for(t in tokens ) {
                        insert TableCell {text:"{t}"} into newCells;
                    }
                }else if (value instanceof javax.swing.tree.TreePath){
                    var path = (value as javax.swing.tree.TreePath);
                    var comp = path.getLastPathComponent();
                    insert TableCell {text:"{comp}", value:comp} into newCells;
                }else {
                    insert TableCell {text:"{value}", value:value} into newCells;
                }
                var dropPoint = new Point(e.x, e.y);
                var viewCol = table.columnAtPoint(dropPoint);
                var viewRow = table.rowAtPoint(dropPoint);
                var column = table.convertColumnIndexToModel(viewCol);
                var row = UIElement.context.convertRowIndexToModel(table,viewRow);
                System.out.println("Table: Drop View {viewRow},{viewCol}");
                System.out.println("Table: Drop Model {row},{column}");
                if(dropMode == DropMode.INSERT) {
                    
                }else if(dropMode == DropMode.ON) { 
                    var ndx = row * sizeof columns + column;
                    for(n in newCells) {
                        cells[ndx++] = n;
                    }                    
                }else if(dropMode == DropMode.ON_OR_INSERT) { //TODO
                    var ndx = row * sizeof columns + column;
                    var cellRect = table.getCellRect(viewRow, viewCol, false);
                    if(cellRect.contains(dropPoint)) { // ON
                        for(n in newCells) {
                            cells[ndx++] = n;
                        }
                    }else {
                        //TODO
                        System.out.println("TODO- ON_OR_INSERT, INSERT is not implemented yet.");
                    }
                }else if(dropMode == DropMode.INSERT_ROWS) {
                        //TODO
                        System.out.println("TODO- INSERT_ROWS is not implemented yet.");                    
                }else if(dropMode == DropMode.INSERT_COLS) {  
                        //TODO
                        System.out.println("TODO- INSERT_COLS is not implemented yet.");                    
                }else if(dropMode == DropMode.ON_OR_INSERT_ROWS) {
                    var ndx = row * sizeof columns + column;
                    var cellRect = table.getCellRect(viewRow, viewCol, false);
                    if(cellRect.contains(dropPoint)) { // ON
                        for(n in newCells) {
                            cells[ndx++] = n;
                        }
                    }else {
                        //TODO
                        System.out.println("TODO- ON_OR_INSERT_ROWS, INSERT_ROWS  is not implemented yet.");
                    }
                }else if(dropMode == DropMode.ON_OR_INSERT_COLS) {
                    var ndx = row * sizeof columns + column;
                    var cellRect = table.getCellRect(viewRow, viewCol, false);
                    if(cellRect.contains(dropPoint)) { // ON
                        for(n in newCells) {
                            cells[ndx++] = n;
                        }
                    }else {
                        //TODO
                        System.out.println("TODO- ON_OR_INSERT_COLS, INSERT_COLS is not implemented yet.");
                    }                    
                }else { // if(dropMode == DropMode.USE_SELECTION) {
                    var anchorRow = UIElement.context.convertRowIndexToModel(table, table.getSelectionModel().getAnchorSelectionIndex());
                    var leadRow = table.convertColumnIndexToModel(table.getSelectionModel().getLeadSelectionIndex());
                    var anchorColumn  = UIElement.context.convertRowIndexToModel(table, table.getColumnModel().getSelectionModel().getAnchorSelectionIndex());
                    var leadColumn  = table.convertColumnIndexToModel(table.getColumnModel().getSelectionModel().getLeadSelectionIndex());
                    System.out.println("Table: selection anchor {anchorRow},{anchorColumn}");
                    System.out.println("Table: selection lead {leadRow},{leadRow}");
                    
                    var ndx = anchorRow * sizeof columns + anchorColumn;
                    var lndx = leadRow * sizeof columns + leadColumn;
                    
                    for(n in newCells) {
                        cells[ndx++] = n;
                        if(ndx > lndx) {
                            break;
                        }
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
    public attribute canAcceptDrop: function(e:DropEvent):Boolean = function (e:DropEvent):Boolean {
        return enableDND;
    };     
    
    private function acceptDrop(value:Object):Boolean{
        if (this.canAcceptDrop <> null) {
            var info = MouseInfo.getPointerInfo();
            var location = table.getLocationOnScreen();
            var p = info.getLocation();
            var x = p.getX() - location.getX();
            var y = p.getY() - location.getY();
            p.setLocation(x, y);
            var e = DropEvent {
                x: p.getX()
                y: p.getY()
                transferData: value
                dropMode: dropMode
            };
            return (this.canAcceptDrop)(e);
        }
        return onDrop <> null;
    }
    private function getDragValue(): java.lang.Object  {
        var c1 = selection * sizeof columns;
        var c2 = c1 + sizeof columns;
        var result = for (i in [c1..c2-1]) {
            cells[i.intValue()];
        }
        return result as java.lang.Object;
    };
    private function getDragText(): String  {
        return null; //todo
        
    }
    private function setDropValue(value:Object):Void {
        if (onDrop <> null) {
            var info = MouseInfo.getPointerInfo();
            var location = table.getLocationOnScreen();
            var p = info.getLocation();
            var x = p.getX() - location.getX();
            var y = p.getY() - location.getY();
            p.setLocation(x, y);
            var e = DropEvent {
                x: p.getX()
                y: p.getY()
                transferData: value
                dropMode: dropMode
            };
            onDrop(e);
        }
    }
    
    
    
    
    public function createView():javax.swing.JComponent {
        tableModel = new com.sun.javafx.api.ui.UIContextImpl.XTableCellModel();
        var cnames:String[] = [];
        var alignIds:Integer[] = [];
        for(c in columns) {
            insert c.text into cnames;
            insert c.alignment.id.intValue() into alignIds;
        }
        
        tableModel.setColumnNames(cnames);        
        tableModel.setColumnAlignments(alignIds); 
        //for (i in cells) {
        for( ii in [0..<sizeof cells]) {
            var i = cells[ii];
            tableModel.addCell(ii, i.text, i.toolTipText,
                               i.font.getFont(), i.background.getColor(), i.foreground.getColor(),
                               i.border.getBorder());
        }
        table.setModel(tableModel);
        var columnModel = table.getColumnModel();
        for (i in [0..<table.getColumnCount()]) {
            var col = columns[i.intValue()];
            if (col.width <> UNSET) {
                columnModel.getColumn(i.intValue()).setPreferredWidth(col.width.intValue());
            }
        }
        if (autoResizeMode <> null) {
            //table.setAutoResizeMode(autoResizeMode.id);
        }
        if (intercellSpacing <> null) {
            table.setIntercellSpacing(intercellSpacing);
        }
        if (selection >= 0 and selection < table.getRowCount()) {
            table.clearSelection();
            table.setRowSelectionInterval(selection.intValue(), selection.intValue());
        }
        return table;
    }   
    
    private  function addTransferHandler():Void {
        UIElement.context.addTransferHandler(table,
            dropType,
            com.sun.javafx.api.ui.ValueGetter {
                public
                function get():Object {
                    return getDragValue();
                }
            },
            com.sun.javafx.api.ui.ValueSetter {
                public
                function set(value:Object):Void {
                    if(onDrop <> null) {
                        setDropValue(value);
                    }
                }
            },
            com.sun.javafx.api.ui.ValueAcceptor {
                public
                function accept(value:Object):Boolean {
                    return if(onDrop <> null and enableDND) {
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
        transferHandler = table.getTransferHandler();
    }
    
    init {
        var self = this;
        showHorizontalLines = true;
        showVerticalLines = true;
        showGrid = true;
        UIElement.context.installXTableCellRenderer(table);
        table.setOpaque(false);
        //table.getTableHeader().setOpaque(true);
        table.setSelectionMode(table.getSelectionModel().SINGLE_SELECTION);
        UIElement.context.installTableHeaderRenderer(table);

        selectionListener = javax.swing.event.ListSelectionListener {
                  public function valueChanged(e:javax.swing.event.ListSelectionEvent):Void {
                      if (inSetSelection) {
                          return;
                      }
                      if (not e.getValueIsAdjusting()) {
                          selectionGeneration = selectionGeneration + 1;
                          var n = selectionGeneration;
                          var row = table.getSelectedRow();
                            //TODO DO LATER - this is a work around until a more permanent solution is provided
                          javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                                      public function run():Void {
                                          if (selectionGeneration == n and table.getSelectedRow() == row) {
                                              selection = row;
                                          }
                                      }
                          });   
                      }
                  }
              };
        table.getSelectionModel().addListSelectionListener(selectionListener);
        table.setDragEnabled(enableDND);
        if(transferHandler == null) {
            addTransferHandler();
        }
    }    
}










