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
import java.awt.Component;
import java.util.List;
import java.lang.StringBuffer;
import java.awt.MouseInfo;
import java.lang.System;
import javax.swing.TransferHandler;

public class Tree extends ScrollableWidget {
    // TODO MARK AS FINAL
    attribute tree: javax.swing.JTree = UIElement.context.createTree();
    
    // TODO MARK AS FINAL
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
            for (i in [1..<sizeof path]) {
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
            var rpath:TreeCell[] = reverse path;
            var children:Object[] = [ row ];
            var childIndices:Integer[] = [ i.intValue()];
            var event = new javax.swing.event.TreeModelEvent(treemodel,
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
            var rpath:TreeCell[] = reverse path;
            var children:Object[] = [ row ];
            var childIndices:Integer[] = [ i.intValue()];
            var event = new javax.swing.event.TreeModelEvent(treemodel,
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
            var rpath:TreeCell[] = reverse path;
            var event = new javax.swing.event.TreeModelEvent(treemodel,
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
            var seq = for(x in row.parent.cells where x == row) indexof x;
            var i = -1;
            if(sizeof seq > 0) {
                i = seq[0];
            }
            var rpath:TreeCell[] = reverse path;
            var children:Object[] = [ row ];
            var childIndices:Integer[] = [i];            
            var event = new javax.swing.event.TreeModelEvent(treemodel,
                                                                 rpath,
                                                                 childIndices,
                                                                 children);
            treemodel.fireTreeNodesChanged(event);
        }
    }

    private attribute treeSelectionListener: javax.swing.event.TreeSelectionListener;
    private attribute listeners: javax.swing.event.TreeModelListener[];
    
    
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
    public attribute leadSelectionPath: Object[] on replace oldValue[lo..hi]=newVals  {
       if (not inSelectionChange) {
           if (sizeof leadSelectionPath > 0 and root.value == leadSelectionPath[0]) {
               var tp = new javax.swing.tree.TreePath(root);
               var p = root;
               for (i in [1..<sizeof leadSelectionPath]) {
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
        tree.repaint();
    };
    public attribute cellForeground: AbstractColor on replace {
        cellRenderer.setCellForeground(cellForeground.getColor());
        tree.repaint();
    };
    public attribute selectedCellBackground: AbstractColor on replace {
        cellRenderer.setSelectedCellBackground(selectedCellBackground.getColor());
        tree.repaint();
    };
    public attribute selectedCellForeground: AbstractColor on replace {
        cellRenderer.setSelectedCellForeground(selectedCellForeground.getColor());
        tree.repaint();
    };
    
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
        if(dropMode <> DropMode.USE_SELECTION and dropMode <> DropMode.INSERT and
            dropMode <> DropMode.ON and dropMode <> DropMode.ON_OR_INSERT) {
            System.out.println("Illegal drop mode for text component,");
            System.out.println("only USE_SELECTION,INSERT, ON, or ON_OR_INSERT are allowed.");
            System.out.println("Reverting to USE_SELECTION");
            dropMode = DropMode.USE_SELECTION;
        }else {
            if(tree <> null)
                UIElement.context.setDropMode(dropMode.id, tree);
        }
    };
    
    /**
     * Indicates if Drag and Drop is enabled.
     */    
    public attribute enableDND: Boolean on replace {
        if (enableDND) {
            if (transferHandler == null and tree <> null) {
                addTransferHandler();
            }
            tree.setDragEnabled(true);
        } else if (tree <> null) {
            tree.setDragEnabled(false);
        }
    };
    
    /**
     * Optional handler called when the user drops an object
     */
    public attribute onDrop: function(e:TreeDropEvent):Void = function (e:DropEvent):Void {
        var values = e.transferData;
        var selectedParent:TreeCell = null;
        var selectedCell:TreeCell = null;
        if(not tree.isSelectionEmpty()) {
            selectedCell = tree.getSelectionPath().getLastPathComponent() as TreeCell;
            selectedParent = selectedCell.parent;
        } 
        var dropPath = tree.getClosestPathForLocation(e.x, e.y);
        var dropCell = dropPath.getLastPathComponent() as TreeCell;
        var parent = if(dropCell.parent <> null)dropCell.parent else root;
        for(value in values) {
            if(value instanceof java.awt.Color) {
                if(colorDropChangesForeground) {
                    cellForeground = Color.fromAWTColor(value as java.awt.Color);
                }else {
                    cellBackground = Color.fromAWTColor(value as java.awt.Color);
                }
            }else {
                var newCells:TreeCell[] = [];
                if (value instanceof List) {
                    var iter = (value as List).iterator();
                    while(iter.hasNext()) {
                        var c = iter.next();
                        if(c instanceof TreeCell) {
                            insert(c as TreeCell) into newCells;
                        }else {
                            insert TreeCell {text:"{c}", value:c} into newCells;
                        }
                    }

                }else if (value instanceof TreeCell) {
                    insert(value as TreeCell) into newCells;
                }else {
                    insert TreeCell {text:"{value}", value:value} into newCells;
                }
                
                System.out.flush();
                if(dropMode == DropMode.INSERT) {
                    var ndx = -1;
                    for(i in [0..<sizeof parent.cells]) {
                        if(parent.cells[i] == dropCell) {
                            ndx = i;
                            break;
                        }
                    }
                    if(ndx >= 0) {
                        var head = parent.cells[0..<ndx];
                        var tail = parent.cells[ndx..];
                        parent.cells = [ head, newCells, tail];
                    }else {
                        insert newCells into parent.cells;
                    }
                }else if(dropMode == DropMode.ON) { 
                    insert newCells into dropCell.cells;
                }else if(dropMode == DropMode.ON_OR_INSERT) { //TODO
                    var dropPoint = new java.awt.Point(e.x, e.y);
                    if(dropPath == null) {
                        insert newCells into root.cells;
                    }else {
                        var r = tree.getPathBounds(dropPath);
                        if(r <> null) {
                            if(r.contains(dropPoint)) {
                                insert newCells into dropCell.cells;
                            }else if(e.x < r.x or e.y < r.y) { // insert before
                                var ndx = -1;
                                for(i in [0..<sizeof parent.cells]) {
                                    if(parent.cells[i] == dropCell) {
                                        ndx = i;
                                        break;
                                    }
                                }
                                if(ndx >= 0) {
                                    var head = parent.cells[0..<ndx];
                                    var tail = parent.cells[ndx..];
                                    parent.cells = [ head, newCells, tail];
                                }else {
                                    insert newCells into parent.cells;
                                }   
                            }else { //insert after
                                var ndx = -1;
                                for(i in [0..<sizeof parent.cells]) {
                                    if(parent.cells[i] == selectedCell) {
                                        ndx = i;
                                        break;
                                    }
                                }
                                if(ndx >= 0) {
                                    var head = parent.cells[0..ndx];
                                    var next = ndx + 1;
                                    var tail = parent.cells[next..];
                                    parent.cells = [ head, newCells, tail];
                                }else {
                                    insert newCells into parent.cells;
                                } 
                            }
                        }else {
                            insert newCells into dropCell.cells;
                        }
                    }
                }else {// if(dropMode == DropMode.USE_SELECTION) {
                    if(selectedCell <> null) {
                        insert newCells into selectedCell.cells;
                    }else{
                        insert newCells into root.cells;
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
    public attribute canAcceptDrop: function(e:TreeDropEvent):Boolean = function (e:DropEvent):Boolean {
        return enableDND;
    };  
    
    
    private bound function acceptDrop(value:Object):Boolean{
        if (this.canAcceptDrop <> null) {
            var info = MouseInfo.getPointerInfo();
            var location = tree.getLocationOnScreen();
            var p = tree.getLocation();
            var e = TreeDropEvent {
                x: p.getX() - location.getX();
                y: p.getY() - location.getY();
                transferData: value
                selection: tree.getSelectionPath()
            };
           (this.canAcceptDrop)(e)
        } else {
           onDrop <> null
        }
    }
    private function getDragValue(): java.lang.Object  {
        //TODO MULTIPLE SELECTION
        return tree.getSelectionPath();
    }
    private function getDragText(): String  {
        var path = tree.getSelectionPath();
        return "{path.getLastPathComponent()}";
        //TODO MULTIPLE SELECTION
    }
    private function setDropValue(value:Object):Void {
        if (onDrop <> null) {
            var info = MouseInfo.getPointerInfo();
            var location = tree.getLocationOnScreen();
            var p = info.getLocation();
            var x = p.getX() - location.getX();
            var y = p.getY() - location.getY();
            p.setLocation(x, y);
            var e = TreeDropEvent {
                x: p.getX()
                y: p.getY()
                transferData: value
                selection: tree.getSelectionPath()
            };
            onDrop(e);
        }
    }

    
    private  function addTransferHandler():Void {
        UIElement.context.addTransferHandler(tree,
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
        transferHandler = tree.getTransferHandler();
    }
    
    /***************************************************************
     * END Drag N Drop
     **************************************************************/        
    
    
    public function createView():javax.swing.JComponent {
        return tree;
    }

    init {
        cellRenderer = tree.getCellRenderer() as FXTreeCellRenderer;    
    }    
}

