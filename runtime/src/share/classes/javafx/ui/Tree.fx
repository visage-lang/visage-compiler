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

import com.sun.javafx.api.ui.UIContextImpl.FXTreeCellRenderer;
import com.sun.javafx.api.ui.UIContextImpl.FXTreeModel;
import java.lang.Object;

public class Tree extends ScrollableWidget {
    private attribute cellRenderer: FXTreeCellRenderer;
    attribute inSelectionChange: Boolean on replace {
         if (not inSelectionChange) {
            tree.repaint();
         }        
    };
    private attribute inModel: Number;
    private function selectionUpdate(newSelection:TreePath):Void {
        if (inSelectionChange) {return;}
        inSelectionChange = true;
        var newPath:Object[] = newSelection.nodes;
        var node = [root];
        var path:TreeCell[] = [];
        //tree.getSelectionModel().removeTreeSelectionListener(treeSelectionListener);
        for (x in newPath) {
            //node = node[n|n.value == x];
            var nnode:TreeCell[] = [];
            for(n in node) {
                if(n.value == x) {
                    insert n into nnode;
                }
            };
            node = nnode;
            if (node <> null) {
                insert node[0] into path;
                node = node[0].cells;
            } else {
                break;
            }
        }
        if (node <> null and sizeof path > 0) {
            var tp = new javax.swing.tree.TreePath(path[0]);
            for (i in [1..sizeof path exclusive]) {
                tp = tp.pathByAddingChild(path[i]);
            }
            tree.clearSelection();
            tree.setSelectionPath(tp);
            tree.makeVisible(tp);
        }
        //tree.getSelectionModel().addTreeSelectionListener(treeSelectionListener);
        inSelectionChange = false;
    }
    function fireRowInserted(row:TreeCell, i:Number):Void{
        if (true or sizeof listeners > 0) {
            var path:TreeCell[] = [];
            var r = row.parent;
            while (r <> null) {
                insert r into path;
                r = r.parent;
            }
            var rpath:Object[] = [];
            for( j in [sizeof path -1 ..0]){
                insert path[j] into rpath;
            }
            var children:Object[] = [ row ];
            var childIndices:Integer[] = [ i.intValue()];
            var event = new javax.swing.event.TreeModelEvent(treemodel as Object,
                                                                 rpath,
                                                                 childIndices,
                                                                 children);
            treemodel.fireTreeNodesInserted(event);
        }
        if (row.selected) {
            var p:TreeCell[] = [];
            var r = row;
            while (r <> null) {
                insert r into p;
                r = r.parent;
            }
            var tp = new javax.swing.tree.TreePath(p);
            tree.addSelectionPath(tp);
        }
    }

    function fireRowDeleted(row:TreeCell, i:Number):Void {
        if (true or sizeof listeners > 0) {
            var path:TreeCell[] = [];
            var r = row.parent;
            while (r <> null) {
                insert r into path;
                r = r.parent;
            }
            var rpath:Object[] = [];
            // reverse rpath
            for( j in [sizeof path -1 ..0]){
                insert path[j] into rpath;
            }           
            var children:Object[] = [ row ];
            var childIndices:Integer[] = [ i.intValue()];
            var event = new javax.swing.event.TreeModelEvent(treemodel as Object,
                                                                 rpath,
                                                                 childIndices,
                                                                 children);
            treemodel.fireTreeNodesRemoved(event);
        }
    }

    function fireTreeStructureChanged(row:TreeCell):Void {
        if (true or sizeof listeners > 0) {
            var path:TreeCell[] = [];
            var r = row;
            while (r <> null) {
                insert r into path;
                r = r.parent;
            }
            var rpath:TreeCell[] = [];
            for( j in [sizeof path -1 ..0]){
                insert path[j] into rpath;
            }            
            var event = new javax.swing.event.TreeModelEvent(treemodel as Object,
                                                                 rpath,
                                                                 null,
                                                                 null);
            treemodel.fireTreeStructureChanged(event);
        }
    }
    function fireNodeValueChanged(row:TreeCell):Void{
        if (true or sizeof listeners > 0) {
            var path:TreeCell[] = [];
            var r = row.parent;
            while (r <> null) {
                insert r into path;
                r = r.parent;
            }
            if (path == null) { 
               return;
            }
            //var i = select indexof x from x in row.parent.cells where x == row;
            var i = -1;
            for(ii in [0..sizeof row.parent.cells exclusive]) {
                if(row.parent.cells[ii] == row) {
                    i = ii;
                    break;
                }
            }
            var rpath:TreeCell[] = [];
            for( j in [sizeof path -1 ..0]){
                insert path[j] into rpath;
            }  
            var children:Object[] = [ row ];
            var childIndices:Integer[] = [ i.intValue()];            
            var event = new javax.swing.event.TreeModelEvent(treemodel as Object,
                                                                 rpath,
                                                                 childIndices,
                                                                 children);
            treemodel.fireTreeNodesChanged(event);
        }
    }

    private attribute treeSelectionListener: javax.swing.event.TreeSelectionListener;
    private attribute listeners: javax.swing.event.TreeModelListener[];
    attribute tree: javax.swing.JTree;
    private attribute treemodel: FXTreeModel;
    public attribute selection: TreePath on replace {
        if(sizeof selection.nodes > 0) {
            selectedValue = selection.nodes[sizeof selection.nodes-1];
            if (treemodel <> null) {
                this.selectionUpdate(selection);
            }    
        }
    };
    public attribute selectedValue:Object;
    public attribute leadSelectionPath: Object[]
        on insert [ndx] (e) {
           if (not inSelectionChange) {
               if (sizeof leadSelectionPath > 0 and root.value == leadSelectionPath[0]) {
                   var tp = new javax.swing.tree.TreePath(root);
                   var p = root;
                   for (i in [1..sizeof leadSelectionPath exclusive]) {
                       var cell:TreeCell = null;
                       for (c in p.cells) {
                           if(c.value == leadSelectionPath[i]) {
                               cell = c;
                               break;
                           }
                       }
                       if (cell <> null) {
                           tp = tp.pathByAddingChild(cell);
                       } else {
                           break;
                       }
                       p = cell;
                   }
                   tree.setLeadSelectionPath(tp);
               }
           }
        }
        on replace [ndx] (oldValue) {
           if (not inSelectionChange) {
               var newValue = leadSelectionPath[ndx];
               if (sizeof leadSelectionPath > 0 and root.value == leadSelectionPath[0]) {
                   var tp = new javax.swing.tree.TreePath(root);
                   var p = root;
                   for (i in [1..sizeof leadSelectionPath exclusive]) {
                       var cell:TreeCell = null;
                       for (c in p.cells) {
                           if(c.value == leadSelectionPath[i]) {
                               cell = c;
                               break;
                           }
                       }
                       if (cell <> null) {
                           tp = tp.pathByAddingChild(cell);
                       } else {
                           break;
                       }
                       p = cell;
                   }
                   tree.setLeadSelectionPath(tp);
               }
           }
        }
        on delete [ndx] (e) {
           if (not inSelectionChange) {
               if (sizeof leadSelectionPath > 0 and root.value == leadSelectionPath[0]) {
                   var tp = new javax.swing.tree.TreePath(root);
                   var p = root;
                   for (i in [1..sizeof leadSelectionPath exclusive]) {
                       var cell:TreeCell = null;
                       for (c in p.cells) {
                           if(c.value == leadSelectionPath[i]) {
                               cell = c;
                               break;
                           }
                       }
                       if (cell <> null) {
                           tp = tp.pathByAddingChild(cell);
                       } else {
                           break;
                       }
                       p = cell;
                   }
                   tree.setLeadSelectionPath(tp);
               }
           }
        };
    public attribute onSelectionChange:function():Void;
    public attribute showRootHandles:Boolean on replace {
        tree.setShowsRootHandles(showRootHandles);
    };
    public attribute rootVisible:Boolean on replace {
        tree.setRootVisible(rootVisible);
    };
    public attribute root: TreeCell on replace {
        var rootSelected = root.selected;
        root.tree = this;
        if (treemodel == null) {
            treemodel = new FXTreeModel(this);
            tree.setModel(treemodel);
            treeSelectionListener = treemodel;
            tree.getSelectionModel().addTreeSelectionListener(treeSelectionListener);
        } else {
            this.fireTreeStructureChanged(root);
        }
        this.selectionUpdate(selection);
        if (rootSelected) {
            var p:TreeCell[] = [];
            var r = root;
            while (r <> null) {
                insert r into p;
                r = r.parent;
            }
            var tp = new javax.swing.tree.TreePath(p);
            tree.addSelectionPath(tp);
        }        
    };
    public attribute rowHeight: Number on replace {
        tree.setRowHeight(rowHeight.intValue());
    };
    public attribute cellBackground: AbstractColor on replace {
        cellRenderer.setCellBackground(cellBackground.getColor());
    };
    public attribute cellForeground: AbstractColor on replace {
        cellRenderer.setCellForeground(cellForeground.getColor());
    };
    public attribute selectedCellBackground: AbstractColor on replace {
        cellRenderer.setSelectedCellBackground(selectedCellBackground.getColor());
    };
    public attribute selectedCellForeground: AbstractColor on replace {
        cellRenderer.setSelectedCellForeground(selectedCellForeground.getColor());
    };
    
    public function createView():javax.swing.JComponent {
        return tree;
    }

    init {
        tree = UIElement.context.createTree();
        cellRenderer = tree.getCellRenderer() as FXTreeCellRenderer;    
        tree.setModel(null);
        //var selectionCount = 0;
    }    
}

