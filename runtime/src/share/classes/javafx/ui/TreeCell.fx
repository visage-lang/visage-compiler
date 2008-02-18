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

public class TreeCell {
    protected function getTree():Tree {
        if (tree <> null) {
            return tree;
        }
        var p = parent;
        while (p <> null) {
            if (p.tree <> null) {
                tree = p.tree;
                break;
            }
            p = p.parent;
        }
        return tree;
    }
    protected attribute tree: Tree;
    protected attribute parent: TreeCell;


    public attribute text: String on replace {
        if(dragText == null or dragText == "")
            dragText = text;
        this.getTree().fireNodeValueChanged(this);
    };
    public attribute dragText: String;
    public attribute toolTipText: String;
    public attribute cells: TreeCell[] on replace oldValue[lo..hi]=newVals {
        for(n in oldValue[lo..hi]) { 
            this.getTree().fireRowDeleted(n, lo);
        }
        var ndx = lo;
        for(row in newVals) {
            row.parent = this;
            row.tree = this.getTree();
            this.getTree().fireRowInserted(row, ndx);
            ndx++
        }
    };
    public attribute value: Object;
    public attribute selected: Boolean on replace {
        if (tree <> null and not tree.inSelectionChange) {
            var p:TreeCell[] = [];
            var r = this;
            while (r <> null) {
                insert r into p;
                r = r.parent;
            }
            var tp = new javax.swing.tree.TreePath(p);
            if (selected) {
                tree.tree.addSelectionPath(tp);
            } else {
                tree.tree.removeSelectionPath(tp);
            }
        }        
    };
    
    public function toString():String {
        return text;
    }

}


