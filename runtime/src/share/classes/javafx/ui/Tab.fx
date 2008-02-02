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

import javafx.ui.TabbedPane;
import javafx.ui.Widget;
import javafx.ui.Icon;
import java.awt.Dimension;

public class Tab {
    protected attribute tabbedPane: TabbedPane;
    attribute panel: javax.swing.JPanel;
    public function selectTab() {
        if (content <> null and panel.getComponentCount() == 0) {
            panel.add(content.getComponent(),
                      java.awt.BorderLayout.CENTER);
            panel.getParent().validate();
        }
    }

    public attribute toolTipText: String;
    public attribute icon: Icon on replace {
         if (tabbedPane <> null) {
            var i = 0;
            for (ii in [0..<sizeof tabbedPane.tabs] ) {
                    if(tabbedPane.tabs[i] == this){
                        i = ii;
                        break;
                    }
            }
            var sicon:javax.swing.Icon = null;
            if (icon <> null) {
                sicon = icon.getIcon();
            }
            tabbedPane.tabbedpane.setIconAt(i, sicon);
        }
    };
    public attribute title: String on replace {
        var i = 0;
        for (ii in [0..<sizeof tabbedPane.tabs] ) {
                if(tabbedPane.tabs[i] == this){
                    i = ii;
                    break;
                }
        }
        tabbedPane.tabbedpane.setTitleAt(i, title);
    };
    public attribute content: Widget on replace {
        if (tabbedPane <> null) {
            if (panel.getComponentCount() > 0) {
                panel.remove(0);
            }
            panel.add(content.getComponent(), java.awt.BorderLayout.CENTER);
        }
    };
    public attribute preferredSize: Dimension on replace {
        if (preferredSize <> null) {
            panel.setPreferredSize(preferredSize);
        }
    };

     init {
        panel = new javax.swing.JPanel();
        panel.setOpaque(false);
        panel.setLayout(new java.awt.BorderLayout());
    }
}





