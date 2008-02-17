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

/**
 * Romain Guy's glass pane progress panel
 */
public class InfiniteProgressPanel extends Widget {
    attribute root:javax.swing.JRootPane;
    public attribute backgroundOpacity: Number = 160 on replace {
        pane.setOpacity(backgroundOpacity.intValue());
    };
    attribute pane:com.sun.javafx.api.ui.HiPerfInfiniteProgressPanel;
    public attribute progress: Boolean on replace {
        if (content <> null) {
            pane.setVisible(progress);
        }
    };
    public attribute text: String on replace {
        pane.setText(text);
    };
    public attribute content: Widget on replace  {
        root.setContentPane(content.getComponent());
    };
    public function createComponent():javax.swing.JComponent {
        root = new javax.swing.JRootPane();
        root.setContentPane(content.getComponent());
        pane = new com.sun.javafx.api.ui.HiPerfInfiniteProgressPanel(UIElement.context.createSimpleLabel(), false);
        pane.setRootPane(root);
        pane.setOpacity(backgroundOpacity.intValue());
        pane.setText(text);
        return root;
    }
    
    init {
        // override defaults in superclass
	focusable = false; //TODO: should be protected by not isInitialized
    }
}

