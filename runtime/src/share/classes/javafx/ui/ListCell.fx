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

import javafx.ui.ListBox;
import javafx.ui.HorizontalAlignment;
import javafx.ui.VerticalAlignment;
import javafx.ui.Border;
import javafx.ui.LineBorder;
import javafx.ui.Label;


public class ListCell {
    protected attribute cacheGeneration: Number;
    protected attribute myIndex: Integer;
    protected attribute listbox: ListBox;
    public attribute dragText: String;
    public attribute value: java.lang.Object;
    public attribute border: Border;
    public attribute horizontalAlignment: HorizontalAlignment;
    public attribute verticalAlignment: VerticalAlignment;
    public attribute text: String on replace {
        if (listbox <> null) {
            if (not listbox.locked) {
                var e:javax.swing.event.ListDataEvent;
                if (cacheGeneration < listbox.updateGeneration) {
                    for (i in [0..<sizeof listbox.cells ] ) {
                        var c = listbox.cells[i];
                        c.myIndex = i;
                        c.cacheGeneration = listbox.updateGeneration;
                    }
                }
                e = new javax.swing.event.ListDataEvent(listbox.list, 
                        e.CONTENTS_CHANGED, myIndex, 
                        sizeof listbox.cells - 1);
                var ls:javax.swing.event.ListDataListener[] = [];
                insert listbox.listeners into ls;
                for (j in ls) {
                    j.contentsChanged(e);
                }
            }
        }
    };
    public attribute toolTipText: String;
    public attribute selected: Boolean on replace {
        if (listbox <> null) {
            if (not listbox.locked) {
                if (cacheGeneration < listbox.updateGeneration) {
                    for ( i in [0..<sizeof listbox.cells] ) {
                        var c = listbox.cells[i];
                        c.myIndex = i;
                        c.cacheGeneration = listbox.updateGeneration;
                    }
                }
                
                if (selected and myIndex <> listbox.selection) {
                    //TODO MULTIPLE SELECTION
                    //insert myIndex into listbox.selection;
                    listbox.selection = myIndex;
                } 
            }
        }
    };
    
    public function toString():String {
        return text;
    }
}


