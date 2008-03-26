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
import javafx.ui.Insets;
import javafx.ui.Orientation;

public class ToolBar extends Widget {
    
    override attribute opaque = true;
    override attribute focusable = false;

    // TODO MARK AS FINAL
    private attribute panel: javax.swing.JPanel = new javax.swing.JPanel() on replace {
        panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 0, 0));
        panel.setBorder(null);
        panel.setOpaque(true);
        if (orientation == Orientation.HORIZONTAL) {	
            toolbar = new javax.swing.JToolBar(javax.swing.JToolBar.HORIZONTAL);
        } else {
            toolbar = new javax.swing.JToolBar(javax.swing.JToolBar.VERTICAL);
        }
        toolbar.setOpaque(false);
        toolbar.setFloatable(false);
        panel.add(toolbar);
    }
    protected attribute toolbar: javax.swing.JToolBar;

    public attribute floatable: Boolean on replace {
        toolbar.setFloatable(floatable);
    };
    public attribute rollover: Boolean = true on replace {
        toolbar.setRollover(rollover);
    };
    public attribute borderPainted: Boolean on replace {
        toolbar.setBorderPainted(borderPainted);
    };
    public attribute margin: Insets on replace {
        toolbar.setMargin(margin.awtinsets);
    };
    public attribute orientation: Orientation = Orientation.HORIZONTAL on replace {
        if ( orientation == Orientation.HORIZONTAL) {
            toolbar.setOrientation(javax.swing.JToolBar.HORIZONTAL);	
        } else {
            toolbar.setOrientation(javax.swing.JToolBar.VERTICAL);
    }
    };
    public attribute buttons: Widget[] on replace oldValue[lo..hi]=newVals {
        for(k in [lo..hi]) { 
            toolbar.remove(lo);
        }
        var ndx = lo;
        for(button in newVals) {
            toolbar.add(button.getComponent(), ndx);
            ndx++
        }
    }

    public function createComponent():javax.swing.JComponent{
        return panel;
    }
}







