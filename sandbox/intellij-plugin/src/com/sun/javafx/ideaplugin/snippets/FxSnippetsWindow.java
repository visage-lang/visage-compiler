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

package com.sun.javafx.ideaplugin.snippets;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.util.IconLoader;
import com.sun.javafx.ideaplugin.FxPlugin;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;

/**
 * @author David Kaspar
 */
public class FxSnippetsWindow implements ProjectComponent {

    private final Icon ICON_PLUS = IconLoader.getIcon("/icons/inspector/plus.png");
    private final Icon ICON_MINUS = IconLoader.getIcon ("/icons/inspector/minus.png");

    private final Project project;
    private final JTree tree;

    public FxSnippetsWindow (Project project) {
        this.project = project;

        DefaultMutableTreeNode root = new DefaultMutableTreeNode ();
        
        DefaultMutableTreeNode apps = new CategoryNode ("Applications");
        apps.add (new SnippetsNode ("Frame", "Frame {\n" + "    title: \"MyApplication\"\n" + "    width: 200\n" + "    height: 200\n" + "    closeAction: function() { java.lang.System.exit( 0 ); }\n" + "    visible: true\n" + "    \n" + "    content: Canvas {\n" + "        content: []\n" + "    }\n" + "}\n"));
        apps.add (new SnippetsNode ("Application", "Application {\n}\n"));
        apps.add (new SnippetsNode ("CustomNode", "public class MyCustomNode extends CustomNode {\n" + "    \n" + "    public function create(): Node {\n" + "        return Group {\n" + "            content: []\n" + "        };\n" + "    }\n" + "}\n"));
        root.add (apps);

        DefaultMutableTreeNode actions = new CategoryNode ("Actions");
        actions.add (new SnippetsNode ("onMouseMoved", "onMouseMoved: function (e:MouseEvent) {\n}\n"));
        actions.add (new SnippetsNode ("onMouseEntered", "onMouseEntered: function (e:MouseEvent) {\n}\n"));
        actions.add (new SnippetsNode ("onMouseExited", "onMouseExited: function (e:MouseEvent) {\n}\n"));
        actions.add (new SnippetsNode ("onMouseClicked", "onMouseClicked: function (e:MouseEvent) {\n}\n"));
        actions.add (new SnippetsNode ("onMousePressed", "onMousePressed: function (e:MouseEvent) {\n}\n"));
        actions.add (new SnippetsNode ("onMouseReleased", "onMouseReleased: function (e:MouseEvent) {\n}\n"));
        actions.add (new SnippetsNode ("onMouseDragged", "onMouseDragged: function (e:MouseEvent) {\n}\n"));
        actions.add (new SnippetsNode ("onMouseWheelMoved", "onMouseWheelMoved: function (e:MouseEvent) {\n}\n"));
        actions.add (new SnippetsNode ("onKeyPressed", "onKeyPressed: function (e:KeyEvent) {\n}\n"));
        actions.add (new SnippetsNode ("onKeyReleased", "onKeyReleased: function (e:KeyEvent) {\n}\n"));
        actions.add (new SnippetsNode ("onKeyTyped", "onKeyTyped: function (e:KeyEvent) {\n}\n"));
        root.add (actions);

        DefaultMutableTreeNode colors = new CategoryNode ("Colors");
        colors.add (new SnippetsNode ("Black", "Color.BLACK"));
        colors.add (new SnippetsNode ("Blue", "Color.BLUE"));
        colors.add (new SnippetsNode ("Cyan", "Color.CYAN"));
        colors.add (new SnippetsNode ("Dark Gray", "Color.DARKGRAY"));
        colors.add (new SnippetsNode ("Green", "Color.GREEN"));
        colors.add (new SnippetsNode ("Light Gray", "Color.LIGHTGRAY"));
        colors.add (new SnippetsNode ("Magenta", "Color.MAGENTA"));
        colors.add (new SnippetsNode ("Orange", "Color.ORANGE"));
        colors.add (new SnippetsNode ("Pink", "Color.PINK"));
        colors.add (new SnippetsNode ("Red", "Color.RED"));
        colors.add (new SnippetsNode ("White", "Color.WHITE"));
        colors.add (new SnippetsNode ("Yellow", "Color.YELLOW"));
        root.add (colors);

        DefaultMutableTreeNode shapes = new CategoryNode ("Basic Shapes");
        shapes.add (new SnippetsNode ("Arc", "Arc {\n" + "    centerX: 100, centerY: 100\n" + "    radiusX: 40, radiusY: 15\n" + "    startAngle: 18, length: 120\n" + "    type: ArcType.OPEN\n" + "    fill: Color.GREEN\n" + "}\n"));
        shapes.add (new SnippetsNode ("Circle", "Circle {\n" + "    centerX: 10, centerY: 10\n" + "    radius: 5\n" + "}\n"));
        shapes.add (new SnippetsNode ("Ellipse", "Ellipse {\n" + "    centerX: 100, centerY: 100\n" + "    radiusX: 40, radiusY: 15\n" + "    fill: Color.GREEN\n" + "}\n"));
        shapes.add (new SnippetsNode ("Image", "ImageView {\n" + "    image: Image {\n" + "        url: \"{__DIR__}/myPicture.png\"\n" + "    }\n" + "}\n"));
        shapes.add (new SnippetsNode ("Line", "Line {\n" + "    x1: 10, y1: 10\n" + "    x2: 10, y2: 10\n" + "    strokeWidth: 1\n" + "}\n"));
        shapes.add (new SnippetsNode ("Polygon", "Polygon {\n" + "    points : [ 0,0, 100,0, 100,100 ]\n" + "    fill: Color.YELLOW\n" + "}\n"));
        shapes.add (new SnippetsNode ("Polyline", "Polyline {\n" + "    points : [ 0,0, 100,0, 100,100 ]\n" + "    strokeWidth: 2.0\n" + "    stroke: Color.RED\n" + "}\n"));
        shapes.add (new SnippetsNode ("Rectangle", "Rectangle {\n" + "    x: 10, y: 10\n" + "    width: 10, height: 10\n" + "}\n"));
        shapes.add (new SnippetsNode ("Text", "Text {\n" + "    font: Font { \n" + "        size: 24 \n" + "        style: FontStyle.PLAIN\n" + "    }\n" + "    x: 10, y: 30\n" + "    content: \"HelloWorld\"\n" + "}\n"));
        root.add (shapes);

        DefaultMutableTreeNode timeline = new CategoryNode ("Timeline");
        timeline.add (new SnippetsNode ("Timeline", "Timeline {\n" + "    repeatCount: Timeline.INDEFINITE\n" + "    keyFrames : [\n" + "        KeyFrame {\n" + "            time : 1s\n" + "            \n" + "        }\n" + "    ]\n" + "}\n"));
        timeline.add (new SnippetsNode ("KeyFrame", "KeyFrame {\n" + "    time: 1s\n" + "    \n" + "}\n"));
        timeline.add (new SnippetsNode ("Values", "values : {\n" + "    variable => 0.0\n" + "}\n"));
        timeline.add (new SnippetsNode ("Action", "action: function() {\n}\n"));
        root.add (timeline);

        DefaultTreeModel model = new DefaultTreeModel (root, true);
        tree = new JTree (model);
        tree.setRootVisible (false);

        tree.expandPath (new TreePath (new Object[] {root, apps }));
        tree.expandPath (new TreePath (new Object[] {root, actions }));
        tree.expandPath (new TreePath (new Object[] {root, colors }));
        tree.expandPath (new TreePath (new Object[] {root, shapes }));
        tree.expandPath (new TreePath (new Object[] {root, timeline }));

        tree.setCellRenderer (new DefaultTreeCellRenderer() {
            public Component getTreeCellRendererComponent (JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Object pass = value;
                if (value instanceof CategoryNode) {
                    pass = "<html><b>" + ((CategoryNode) value).getName ();
                } else if (value instanceof SnippetsNode) {
                    pass = ((SnippetsNode) value).getName ();
                }
                Component component = super.getTreeCellRendererComponent (tree, pass, selected, expanded, leaf, row, hasFocus);

                if (component instanceof JLabel) {
                    if (value instanceof CategoryNode) {
                        ((JLabel) component).setIcon (expanded ? ICON_MINUS : ICON_PLUS);
                    } else if (value instanceof SnippetsNode) {
                        ((JLabel) component).setIcon (null);
                    }
                }
                return component;
            }
        });

        tree.setDragEnabled (false);
        DragSource.getDefaultDragSource ().createDefaultDragGestureRecognizer (tree, DnDConstants.ACTION_COPY, new DragGestureListener() {
            public void dragGestureRecognized (DragGestureEvent dge) {
                Point origin = dge.getDragOrigin ();
                TreePath path = tree.getClosestPathForLocation (origin.x, origin.y);
                Object object = path != null ? path.getLastPathComponent () : null;
                if (object instanceof SnippetsNode) {
                    String code = ((SnippetsNode) object).getCode ();
                    if (code != null)
                        dge.startDrag (null, new StringSelection (code));
                }
            }
        });
    }

    @NonNls @NotNull public String getComponentName () {
        return "FxSnippets";
    }

    public void initComponent () {
    }

    public void disposeComponent () {
    }

    public void projectOpened () {
        tree.setPreferredSize (new Dimension (100, 100));
        ToolWindow window = ToolWindowManager.getInstance (project).registerToolWindow ("FxSnippets", tree, ToolWindowAnchor.RIGHT);
        window.setIcon (FxPlugin.FX_ICON);
        window.setTitle ("JavaFX Snippets");
//        window.show (null);
//        window.setAvailable (true, null);
    }

    public void projectClosed () {
//        window.setAvailable (false, null);
        ToolWindowManager.getInstance (project).unregisterToolWindow ("FxSnippets");
    }

    private static class CategoryNode extends DefaultMutableTreeNode {

        private final String name;

        private CategoryNode (String name) {
            super (null, true);
            this.name = name;
        }

        public String getName () {
            return name;
        }

    }

    private static class SnippetsNode extends DefaultMutableTreeNode {

        private final String name;
        private final String code;

        private SnippetsNode (String name, String code) {
            super (null, false);
            this.name = name;
            this.code = code;
        }

        public String getName () {
            return name;
        }

        public String getCode () {
            return code;
        }

    }

}
