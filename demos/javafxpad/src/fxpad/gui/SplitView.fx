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

import java.lang.System;
import org.jdesktop.swingx.MultiSplitLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javafx.ext.swing.*;

/**
 * @author jclarke
 */

public class SplitView  {
    // TODO MARK AS FINAL
    protected attribute id: String = "{System.identityHashCode(this)}";

    // TODO MARK AS FINAL
    protected attribute splitpane: SplitPane;

    // TODO MARK AS FINAL
    protected attribute splitnode: MultiSplitLayout.Leaf
        = new MultiSplitLayout.Leaf(id);

    public attribute weight: Number on replace {
        if (splitnode != null) {
            splitnode.setWeight(weight);
        }
    };
    
    public attribute component: Component on replace old {
        component.getJComponent().setMinimumSize(new Dimension(0, 0));
        if (splitpane != null) {
            if (old != null) {
                splitpane.getJPanel().remove(old.getJComponent());
            }
            if (component != null) {
                splitpane.getJPanel().add(component.getJComponent(), id);
            }
        }
    };
    
    protected function getSplitNode(): MultiSplitLayout.Node{
       return splitnode;
    }
}