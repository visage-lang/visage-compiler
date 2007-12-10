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

public class FlowPanel extends Widget {
    private attribute panel: com.sun.javafx.api.ui.ScrollablePanel;
    private attribute layout: java.awt.FlowLayout;
    protected function createLayout():com.sun.javafx.api.ui.ScrollablePanel {
        foreach (i in [0..panel.getComponentCount() exclusive]) {
            panel.remove(0);
        }
        var align = 
            if (alignment == Alignment.LEADING)
            then java.awt.FlowLayout.LEADING
            else if (alignment == Alignment.TRAILING)
            then java.awt.FlowLayout.TRAILING
            else if (alignment == Alignment.CENTER)
            then java.awt.FlowLayout.CENTER
            else java.awt.FlowLayout.LEADING; // default
        layout = new com.sun.javafx.api.ui.FlowLayout(align, hgap.intValue(), vgap.intValue());
        panel.setLayout(layout);
        foreach (i in content) {
            panel.add(i.getComponent());
        }
        return panel;
    }

    public attribute alignment:Alignment = Alignment.CENTER on replace {
        if (panel <> null) {
            this.createLayout();
        }
    };
    public attribute vgap: Number = 5 on replace {
        if (layout <> null)
            layout.setVgap(vgap.intValue());
    };
    public attribute hgap: Number = 5 on replace {
        if (layout <> null)
            layout.setHgap(hgap.intValue());
    };
    public attribute content: Widget[]
        on insert [ndx] (newalue) {
            if (panel <> null) {
                panel.add(newalue.getComponent(), ndx);
                panel.revalidate();
                panel.repaint();
            }
        }

        on delete[ndx] (oldValue) {
            if (panel <> null) {
                panel.remove(ndx);
                panel.revalidate();
                panel.repaint();
            }
        }

        on replace [ndx] (oldValue) {
            if (panel <> null) {
                panel.remove(ndx);
                panel.add(content[ndx].getComponent(), ndx);
                panel.revalidate();
                panel.repaint();
            }
        };

    public function createComponent():javax.swing.JComponent {
        panel = com.sun.javafx.api.ui.ScrollablePanel{};
        panel.setScrollableTracksViewportWidth(true);
        panel.setScrollableTracksViewportHeight(false);
        panel.setOpaque(false);
        this.createLayout();
        return panel;
    }
    public attribute focusable:Boolean = false;

}

