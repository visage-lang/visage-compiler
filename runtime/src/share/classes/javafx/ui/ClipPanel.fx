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
import java.lang.Math;
import java.awt.Dimension;

public class ClipPanel extends Widget {
    // private
    private attribute p: javax.swing.JPanel;

    // public
    public attribute content: Widget on replace  {
        if (p <> null) {
            if (p.getComponentCount() > 0) {
                p.remove(0);
            }
            p.add(content.getComponent());
            p.invalidate();
        }
    };
    public attribute clipWidth: Number = java.lang.Integer.MAX_VALUE on replace {
        p.revalidate();
    };
    public attribute clipHeight: Number = java.lang.Integer.MAX_VALUE on replace {
        p.revalidate();
    };
    public attribute fullHeight: Number;
    public attribute fullWidth: Number;
    public function createComponent():javax.swing.JComponent{
        p = new javax.swing.JPanel(false);
        p.setLayout(java.awt.LayoutManager {

                public function removeLayoutComponent(comp:java.awt.Component):Void {
                }
                public function addLayoutComponent(name:String, comp:java.awt.Component):Void {
                }

                public function preferredLayoutSize(parent:java.awt.Container):java.awt.Dimension {
                    var comp = content.getComponent();
                    var dim = comp.getPreferredSize();
                    fullHeight = dim.height;
                    fullWidth = dim.width;
                    var res = new java.awt.Dimension(Math.min(dim.width, clipWidth).intValue(),
                                         Math.min(dim.height, clipHeight).intValue());
                    return res;

                }

                public function minimumLayoutSize(parent:java.awt.Container):java.awt.Dimension {
                    return new java.awt.Dimension(0, 0);
                }

                public function layoutContainer(parent:java.awt.Container):Void {
                    var comp = content.getComponent();
                    var dim = comp.getPreferredSize();
                    if (dim.width < parent.getWidth()) {
                        dim.width = parent.getWidth();
                    }
                    if (dim.height < parent.getHeight()) {
                        dim.height = parent.getHeight();
                    }
                    fullHeight = dim.height;
                    fullWidth = dim.width;
                    var res = new Dimension(Math.min(dim.width, clipWidth).intValue(),
                                            Math.min(dim.height, clipHeight).intValue());
                    comp.setBounds(0, 0, 
                                   res.width,
                                   res.height);
                }
            });
        p.add(content.getComponent());
        p.setOpaque(false);
        return p;
    }
    
    init {
        // override defaults in superclass
	focusable = false; //TODO: should be protected by not isInitialized
    }
}

