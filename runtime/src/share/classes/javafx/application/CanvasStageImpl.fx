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

package javafx.application;

import com.sun.scenario.scenegraph.JSGPanel;
import com.sun.scenario.scenegraph.SGGroup;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.LayoutManager;
import java.awt.Container;
import java.awt.Dimension;
import javafx.scene.*;
import javafx.scene.paint.*;
import com.sun.javafx.scene.JSGPanelImpl;

/**
 * CanvasStageImpl provides a JSGPanel based implementation of Stage.
 */
class CanvasStageImpl {

    public /* constant */ attribute jsgPanel : JSGPanelImpl = createJsgPanel();

    public attribute stage: Stage on replace{
        if (stage != null){
            stage.width = jsgPanel.getWidth();
            stage.height = jsgPanel.getHeight();
        }
    };

    // =================================================================================================================
    // private

    /** bound to stage.content so we can react to changes */
    private attribute stageContent: Node[] = bind stage.content on replace oldNodes[a..b] = newNodes {
        for (node in oldNodes[a..b]) {
            getRoot().remove(node.impl_getFXNode());
        }
        var index = a;
        for (node in newNodes) {
            getRoot().add(index, node.impl_getFXNode());
            index = index + 1;
        }
    }

    /** bound to stage.fill so we can react to changes */
    private attribute stageFill: Paint = bind stage.fill on replace {
        jsgPanel.setBackgroundPaint(stageFill.getAWTPaint());
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the windows content.
     */
    private function createJsgPanel(): JSGPanelImpl {
        var panel = new JSGPanelImpl();
        panel.setOpaque(true);
        panel.setLayout(createLayoutManager());
        panel.setScene(new SGGroup());
        return panel;
    }

    private function getRoot(): SGGroup {
        jsgPanel.getScene() as SGGroup;
    }

    /**
     * Create layout manager for the JSGPanel
     */
    private function createLayoutManager() {
        LayoutManager {
            public function layoutContainer(p:Container):Void {
                for (node in stage.content) {
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

    private function updateCachedBounds(node:Node):Void {
        node.impl_cachedBounds = node.impl_getFXNode().getBounds();
        node.impl_cachedXYWH = node.impl_getSGNode().getBounds();
    }

    postinit {
        jsgPanel.addComponentListener(ComponentAdapter {
            public function componentResized(e:ComponentEvent): Void {
                var d = jsgPanel.getSize();
                if (stage.width != d.width) {
                    stage.width = d.width;
                }
                if (stage.height != d.height) {
                    stage.height = d.height;
                }
            }
        });
    }

}