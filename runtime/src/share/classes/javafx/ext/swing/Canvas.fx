/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
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

package javafx.ext.swing;

import javax.swing.JComponent;
import java.awt.LayoutManager;
import java.awt.Dimension;
import java.awt.Container;
import com.sun.scenario.scenegraph.JSGPanel;
import com.sun.scenario.scenegraph.SGGroup;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.CustomNode;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;


// PENDING_DOC_REVIEW
/**
 * A graphical container for the {@link Node} objects. 
 *  
 * @profile common
 */
public class Canvas extends Component {

    public attribute background: Paint = Color.fromAWTColor(getJSGPanelImpl().getBackground()) on replace {
        doAndIgnoreJComponentChange(function() {
            getJSGPanelImpl().setBackgroundPaint(background.getAWTPaint());
        });
    }
        
    private function updateCachedBounds(node:Node):Void {
        node.impl_cachedBounds = node.impl_getFXNode().getBounds();
        node.impl_cachedXYWH = node.impl_getSGNode().getBounds();
    }

    private function createLayoutManager() {
        LayoutManager {
            public function layoutContainer(p:Container):Void { 
                for (node in content) {
                    doLayout(node); 
                    updateCachedBounds(node);
                }
            }
            public function minimumLayoutSize(p:Container):Dimension { 
                return preferredLayoutSize(p);
            }
            public function preferredLayoutSize(p:Container):Dimension { 
                // JSGPanel overrides getPreferredSize(), doesn't delegate to 
                // its LayoutManager
                return new Dimension();
            }
            public function addLayoutComponent(s:String, c:java.awt.Component):Void { }
            public function removeLayoutComponent(c:java.awt.Component):Void { }
        }
    }

    protected /* final */ function createJComponent(): JComponent {
        var panel = new JSGPanelImpl();
        panel.setLayout(createLayoutManager());
        panel.setScene(new SGGroup());
        return panel;
    }

    private function doLayout(root:Node):Void {
        if (root.impl_needsLayout) {
            if (root instanceof Group) {
                for (child in (root as Group).content) {
                    doLayout(child);
                }
            }
            else if (root instanceof CustomNode) {
                doLayout((root as CustomNode).impl_content);
            }
        }
        if (root instanceof Group) {
            var group:Group = root as Group;
            if (group.layout != null) {
                group.layout(group); 
            }
            for (child in group.content) {
                updateCachedBounds(child);
            }
        }
        root.impl_needsLayout = false;
    }

    private /* final */ function getJSGPanelImpl(): JSGPanelImpl {
        getJComponent() as JSGPanelImpl;
    }

    private function getRoot(): SGGroup {
        getJSGPanelImpl().getScene() as SGGroup;
    }

    // PENDING_DOC_REVIEW
    /**
     * The array of {@link Node}s to be rendered on this {@code Canvas}.
     * 
     * @profile common
     */    
    public attribute content: Node[] on replace oldNodes[a..b] = newNodes {
        for (node in oldNodes[a..b]) {
            getRoot().remove(node.impl_getFXNode());
        }
        var index = a;
        for (node in newNodes) {
            getRoot().add(index, node.impl_getFXNode());
            index = index + 1;
        }
    }

    postinit {
        var jSGPanel = getJSGPanelImpl();

        jSGPanel.addPropertyChangeListener(BackgroundSupport.BACKGROUND_PAINT_PROPERTY,
                                           PropertyChangeListener {
            public function propertyChange(e: PropertyChangeEvent): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                var bg = getJSGPanelImpl().getBackgroundPaint();
                if (bg == null or bg instanceof java.awt.Color) {
                    background = Color.fromAWTColor(bg as java.awt.Color);
                }
            }
        });
    }

}
