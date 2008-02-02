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

public class TableCell {
    protected attribute table:Table;
    public attribute text:String on replace {
        if (table.tableModel <> null) {
            //var cr = select indexof x from x in table.cells where x == this;
            var cr = -1;
            for(i in [0..<sizeof table.cells]){
                if(table.cells[i] == this) {
                    cr = i;
                    break;
                }
            }
            table.tableModel.updateCell(cr, text, toolTipText, font.getFont(),
                                        background.getColor(), foreground.getColor(), border.getBorder());
        }        
    };
    public attribute toolTipText: String on replace {
        if (table.tableModel <> null) {
            //var cr = select indexof x from x in table.cells where x == this;
            var cr = -1;
            for(i in [0..<sizeof table.cells]){
                if(table.cells[i] == this) {
                    cr = i;
                    break;
                }
            }
            table.tableModel.updateCell(cr, text, toolTipText, font.getFont(),
                                        background.getColor(), foreground.getColor(), border.getBorder());
        }        
    };
    public attribute background: Color on replace {
        if (table.tableModel <> null) {
            //var cr = select indexof x from x in table.cells where x == this;
            var cr = -1;
            for(i in [0..<sizeof table.cells]){
                if(table.cells[i] == this) {
                    cr = i;
                    break;
                }
            }
            table.tableModel.updateCell(cr, text, toolTipText, font.getFont(),
                                        background.getColor(), foreground.getColor(), border.getBorder());
        }        
    };
    public attribute foreground: Color on replace {
        if (table.tableModel <> null) {
            //var cr = select indexof x from x in table.cells where x == this;
            var cr = -1;
            for(i in [0..<sizeof table.cells]){
                if(table.cells[i] == this) {
                    cr = i;
                    break;
                }
            }
            table.tableModel.updateCell(cr, text, toolTipText, font.getFont(),
                                        background.getColor(), foreground.getColor(), border.getBorder());
        }        
    };
    public attribute font: Font on replace {
        if (table.tableModel <> null) {
            //var cr = select indexof x from x in table.cells where x == this;
            var cr = -1;
            for(i in [0..<sizeof table.cells]){
                if(table.cells[i] == this) {
                    cr = i;
                    break;
                }
            }
            table.tableModel.updateCell(cr, text, toolTipText, font.getFont(),
                                        background.getColor(), foreground.getColor(), border.getBorder());
        }        
    };
    public attribute border: Border;
    public attribute value:Object;
    public attribute selected: Boolean on replace {
        if (table <> null) {
            if (selected) {
                //var cr = select indexof x from x in table.cells where x == this;
                var cr = -1;
                for (i in [0..<sizeof table.cells]){
                    if(table.cells[i] == this) {
                        cr = i;
                        break;
                    }
                }
                var row = (cr / sizeof table.columns).intValue();
                table.selection = row;
            } else {
                table.selection = -1;
            }
        } 
    };
    
    public function toString():String {
        return text;
    }
}



