/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package fxpad.gui;

import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.lang.System;
import javafx.lang.FX;
import javafx.ext.swing.*;
import javafx.scene.*;

/**
 * @author jclarke
 */

public class SplitPane extends Component, Container {
    public /* final */ function getJPanel(): JPanel {
        getJComponent() as JPanel;
    }
    public /* final */ function getJXMultiSplitPane(): JXMultiSplitPane {
        getJComponent() as JXMultiSplitPane;
    }
    
    
    public var floatingDividers:Boolean = 
        getJXMultiSplitPane().getMultiSplitLayout().getFloatingDividers()
    on replace {
        getJXMultiSplitPane().getMultiSplitLayout().setFloatingDividers(floatingDividers);
    };
    
    // TODO MARK AS FINAL
    protected var root: MultiSplitLayout.Split = MultiSplitLayout.Split{} 
        on replace {
            root.setWeight(1.0);
        }

    public var vertical: Boolean = false on replace {
        root.setRowLayout(not vertical);
    }

    function getModel(): java.util.List {
        var n = for (p in content) p.getSplitNode();
        var div = null;
        var result = new ArrayList();
        for (i in n) {
            if (div != null) {
                result.add(div);
            }
            result.add(i);
            div = new MultiSplitLayout.Divider();
        }
        return result;
    }
    
    public var content: SplitView[] on replace oldValues[lo..hi]=newVals {
        var pane = getJXMultiSplitPane();
        for(k in [lo..hi]) {
            pane.remove(lo);
        }
        for(view in newVals) {
            view.splitpane = this;
            pane.add(view.id, view.component.getJComponent());
        }
        root.setChildren(this.getModel());
        pane.getMultiSplitLayout().setModel(root);

        //pane.revalidate();
        //pane.repaint();
    };
    
    
    /* final */override function createJComponent(): JComponent {
    
        var pane = new JXMultiSplitPane();
        pane.setOpaque(false);
        pane;
    }    
    
    /**
     * {@inheritDoc}
     */
    /* final */ override function remove(component: Component): Void {
        // PENDING(shannonh) - what I really want here is a deleteByIdentity operator
        // http://openjfx.java.sun.com/jira/browse/JFXC-1005
        var indices = for (c in content where FX.isSameObject(c.component, component)) indexof c;
        for (i in [sizeof indices - 1..0 step -1]) {
            delete content[indices[i]];
        }
    }     
}