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
import javafx.ui.Orientation;
import javafx.ui.SplitView;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class SplitPane extends Widget {
    protected attribute split:com.sun.javafx.api.ui.MultiSplitPane
                               = new com.sun.javafx.api.ui.MultiSplitPane();
    protected attribute root: com.sun.javafx.api.ui.MultiSplitLayout.Split
                               = com.sun.javafx.api.ui.MultiSplitLayout.Split{};

    public attribute orientation: Orientation = Orientation.HORIZONTAL on replace {
        if (root <> null) {
            root.setRowLayout(orientation == Orientation.HORIZONTAL);
        }
    };
    public attribute content: SplitView[] on replace oldValue[lo..hi]=newVals {
        if (split <> null) {
            for(k in [lo..hi]) { 
                split.remove(lo);
            }
            for(pane in newVals) {
                pane.splitpane = this;
                split.add(pane.id, pane.content.getComponent());
            }
            root.setChildren(this.getModel());
            split.getMultiSplitLayout().setModel(root);            
        }
    };
        
    private function getModel(): List {
        var n = for (p in content) p.getSplitNode();
        var div = null;
        var result = new ArrayList();
        for (i in n) {
            if (div <> null) {
                result.add(div);
            }
            result.add(i);
            div = new com.sun.javafx.api.ui.MultiSplitLayout.Divider();
        }
        return result;
    }
    
    public attribute focusable: Boolean = false;
    
    public function createComponent():javax.swing.JComponent {
        split.setOpaque(false);
        root.setRowLayout(orientation == Orientation.HORIZONTAL);
        root.setWeight(1.0);
        root.setChildren(this.getModel());
        split.getMultiSplitLayout().setModel(root);
        for (p in content) {
            split.add(p.id, p.content.getComponent());
        }
        return split;

    }    
}






