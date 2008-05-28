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

import javafx.ui.Widget;

// GridLayout

public class GridPanel extends Widget {

    override attribute focusable = false;

    private attribute UNSET: Integer = java.lang.Integer.MIN_VALUE;
    private attribute jpanel: javax.swing.JPanel;
    private attribute layout: java.awt.GridLayout;

    public attribute rows: Number on replace  {
        layout.setRows(rows.intValue());
    };
    public attribute columns: Number on replace {
        layout.setColumns(columns.intValue());
    };
    public attribute hgap: Number = UNSET on replace {
        layout.setHgap(hgap.intValue());
    };
    public attribute vgap: Number = UNSET on replace {
        layout.setVgap(vgap.intValue());
    };
    public attribute cells: Widget[] on replace oldValue[lo..hi]=newVals {
        if(jpanel <> null) {
            for(k in [lo..hi]) { 
                jpanel.remove(lo);
            }
            var ndx = lo;
            for(cell in newVals) {
                jpanel.add(cell.getComponent(), ndx);
                ndx++
            }
            jpanel.validate();
        }
    };
    
    public function createComponent():javax.swing.JComponent {
        jpanel = javax.swing.JPanel{};
        jpanel.setOpaque(false);
        layout = new java.awt.GridLayout(rows.intValue(), columns.intValue());
        if (vgap <> UNSET) {
            layout.setVgap(vgap.intValue());
        }
        if (hgap <> UNSET) {
            layout.setHgap(hgap.intValue());
        }
        jpanel.setLayout(layout);
        for (i in cells) {
            jpanel.add(i.getComponent());
        }
        return jpanel;
    }
}




