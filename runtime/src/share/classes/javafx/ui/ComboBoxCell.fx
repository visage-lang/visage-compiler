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

import javafx.ui.ComboBox;

public class ComboBoxCell {
    protected attribute combobox:ComboBox;
    public attribute value:java.lang.Object;
    public attribute text: String on replace {
        if (combobox <> null) {
            var e:javax.swing.event.ListDataEvent;
            //var i = select indexof x from x in combobox.cells where x == this;
           var i = -1;
           var ii = 0;
           for (c in combobox.cells) {
               if(this == c) {
                   i = ii;
                   break;
               }
               ii = ii + 1;
           }
            e = new javax.swing.event.ListDataEvent(this, e.CONTENTS_CHANGED,
                                                        i, i);
            for (j in combobox.listeners) {
                j.contentsChanged(e);
            }
        }
    };
    public attribute toolTipText: String;
}

