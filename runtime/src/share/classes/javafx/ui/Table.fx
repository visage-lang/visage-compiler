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
import com.sun.tools.javafx.ui.SequenceUtil;


public class Table extends ScrollableWidget {
    private attribute UNSET: Integer = java.lang.Integer.MIN_VALUE;
    private attribute inSetSelection:Boolean;
    private attribute selectionListener:javax.swing.event.ListSelectionListener;
    private attribute selectionGeneration: Number;
    attribute tableModel: com.sun.javafx.api.ui.UIContextImpl.XTableCellModel;
    private attribute table:javax.swing.JTable;
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

    public attribute rowHeight: Number on replace {
        table.setRowHeight(rowHeight.intValue());
    };
    public attribute rowMargin: Number on replace {
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
        table.setGridColor(gridColor.getColor());
    };
    public attribute columns: TableColumn[];
    public attribute cells: TableCell[]
        on insert [ndx] (cell) {
            cell.table = this;
            if (tableModel <> null) {
                var s = selection;
                tableModel.addCell(ndx, cell.text, cell.toolTipText,
                                   cell.font.getFont(), cell.background.getColor(), cell.foreground.getColor(),
                                   cell.border.getBorder());
                selection = s;
            }
        }
        on delete [ndx] (cell) {
            if (tableModel <> null) {
                var s = selection;
                tableModel.removeCell(ndx);
                selection = s;
            }
        };
    public attribute intercellSpacing: Dimension on replace {
        table.setIntercellSpacing(intercellSpacing);
    };
    protected function createTable(): javax.swing.JTable {
        return UIElement.context.createTable();
    }
    
    public function createView():javax.swing.JComponent {
        tableModel = new com.sun.javafx.api.ui.UIContextImpl.XTableCellModel();
        var cnames:String[] = [];
        var alignIds:Integer[] = [];
        for(c in columns) {
            insert c.text into cnames;
            insert c.alignment.id.intValue() into alignIds;
        }
        
        tableModel.setColumnNames(SequenceUtil.sequenceOfString2StringArray(cnames));        
        tableModel.setColumnAlignments(SequenceUtil.sequenceOfInteger2intArray(alignIds)); 
        //for (i in cells) {
        for( ii in [0..sizeof cells exclusive]) {
            var i = cells[ii];
            tableModel.addCell(ii, i.text, i.toolTipText,
                               i.font.getFont(), i.background.getColor(), i.foreground.getColor(),
                               i.border.getBorder());
        }
        table.setModel(tableModel);
        var columnModel = table.getColumnModel();
        for (i in [0..table.getColumnCount() exclusive]) {
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
    
    init {
        var self = this;
        showHorizontalLines = true;
        showVerticalLines = true;
        showGrid = true;
        table = this.createTable();
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
                          //TODO JXFC-333
                          /*******
                            //TODO DO LATER - this is a work around until a more permanent solution is provided
                          javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                                      public function run():Void {
                                          if (selectionGeneration == n and table.getSelectedRow() == row) {
                                              selection = row;
                                          }
                                      }
                          });   
                           * ******/                       
                      }
                  }
              };
        table.getSelectionModel().addListSelectionListener(selectionListener);
    }    
}










