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

import javafx.ui.Widget;

/** 
 * Container which provides absolute layout. You layout its content by 
 * specifing their (x, y, width, height) values.
 */

public class Panel extends Widget {
    private attribute jpanel:javax.swing.JPanel;
    /** A list of the components contained in this panel */
    public attribute content: Widget[]
        on insert [ndx] (c) {
            if (jpanel <> null) {
                jpanel.add(c.getComponent(), ndx);
            }
        }
        on delete [ndx] (w) {
            if (jpanel <> null) {
                jpanel.remove(ndx);
            }
        }
        on replace [ndx] (oldWidget)  {
            if (jpanel <> null) {
                jpanel.remove(ndx);
                jpanel.add(content[ndx].getComponent(), ndx);
            }
        };
    public attribute focusable: Boolean = false;
    public function createComponent():javax.swing.JComponent{
        jpanel = new javax.swing.JPanel();
        jpanel.setOpaque(false);
        jpanel.setLayout(null);
        for (i in [0..sizeof content exclusive]) {
            jpanel.add(content[i].getComponent(), i);
        }
        return jpanel;
    }
}


