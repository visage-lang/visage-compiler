/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.gui;
import java.awt.geom.Point2D;
import java.lang.System;
import javafx.fxunit.*;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGRectangle;
import com.sun.scenario.scenegraph.event.SGNodeListener;
import com.sun.scenario.scenegraph.event.SGNodeEvent;
import com.sun.scenario.scenegraph.event.SGKeyListener;
import com.sun.scenario.scenegraph.event.SGMouseListener;
import com.sun.scenario.scenegraph.event.SGFocusListener;
import com.sun.scenario.scenegraph.event.SGMouseAdapter;
import com.sun.scenario.scenegraph.fx.FXNode;

/**
 * Much of Node.fx is a thin wrapper around comparable scene graph functionality.
 * We're not interested in testing that aspect of Node. Rather, this test case
 * focuses on ensuring bound functions are really bound, and so forth.
 *
 * @author Richard
 */
public class NodeTest extends FXTestCase {
    function testGetXIsBound() {
        var node = MockNode{};
        var x = bind node.getX();
        node.getSGRectangle().setX(100);
        assertTrue(x == 100);
    }

    function testGetYIsBound() {
        var node = MockNode{};
        var y = bind node.getY();
        node.getSGRectangle().setY(100);
        assertTrue(y == 100);
    }

    function testGetWidthIsBound() {
        var node = MockNode{};
        var width = bind node.getWidth();
        node.getSGRectangle().setWidth(100);
        assertTrue(width == 100);
    }

    function testGetHeightIsBound() {
        var node = MockNode{};
        var height = bind node.getHeight();
        node.getSGRectangle().setHeight(100);
        assertTrue(height == 100);
    }

    function testGetBoundsXIsBound() {
        var node = MockNode{};
        var x = bind node.getBoundsX();
        node.getSGRectangle().setX(100);
        node.translateX = 100;
        assertTrue(x == 200);
    }

    function testGetBoundsYIsBound() {
        var node = MockNode{};
        var y = bind node.getBoundsY();
        node.getSGRectangle().setY(100);
        node.translateY = 100;
        assertTrue(y == 200);
    }

    function testGetBoundsWidthIsBound() {
        var node = MockNode{};
        var width = bind node.getBoundsWidth();
        node.getSGRectangle().setWidth(100);
        node.scaleX = 2.0;
        assertTrue(width == 200);
    }

    function testGetBoundsHeightIsBound() {
        var node = MockNode{};
        var height = bind node.getBoundsHeight();
        node.getSGRectangle().setHeight(100);
        node.scaleY = 2.0;
        assertTrue(height == 200);
    }

    function testContains() {
        var node = MockNode{};
        node.getSGRectangle().setHeight(50);
        node.getSGRectangle().setWidth(50);

        assertTrue(node.contains(0,0));
        assertTrue(node.contains(25,25));
        assertTrue(node.contains(49,49));
        assertFalse(node.contains(50,50));

        node.scaleX = 2.0;
        node.scaleY = 2.0;

        assertTrue(node.contains(0,0));
        assertTrue(node.contains(50,50));
        assertTrue(node.contains(99,99));
        assertFalse(node.contains(100,100));

        node.translateX = 10;
        node.translateY = 10;

        assertFalse(node.contains(0,0));
        assertFalse(node.contains(9,9));
        assertTrue(node.contains(10,10));
        assertTrue(node.contains(109,109));
        assertFalse(node.contains(110,110));

        var rnode = MockNode {
            rotate:45
            anchorX:50
            anchorY:50
            translateX:50
            translateY:50
        };
        node.getSGRectangle().setWidth(100);
        node.getSGRectangle().setHeight(100);

        assertFalse(rnode.contains(0,0));
        // Currently fails due to [likely] a Scenario bug where bounds is 0x0:
        assertTrue(rnode.contains(100,30));

    }

    function testToFront() {
        var a = MockNode{};
        var b = MockNode{};
        var c = MockNode{};
        var group = Group {
            content: [a, b, c]
        };

        c.toFront();
        assertEquals(c, group.content[0]);
        assertEquals(a, group.content[1]);
        assertEquals(b, group.content[2]);

        a.toFront();
        assertEquals(a, group.content[0]);
        assertEquals(c, group.content[1]);
        assertEquals(b, group.content[2]);
    }

    function testToFrontParentIsNull() {
        // succeeds if no exception is thrown
        var a = MockNode{};
        a.toFront();
    }

    function testToBack() {
        var a = MockNode{};
        var b = MockNode{};
        var c = MockNode{};
        var group = Group {
            content: [a, b, c]
        };

        a.toBack();
        assertEquals(b, group.content[0]);
        assertEquals(c, group.content[1]);
        assertEquals(a, group.content[2]);

        c.toBack();
        assertEquals(b, group.content[0]);
        assertEquals(a, group.content[1]);
        assertEquals(c, group.content[2]);
    }

    function testToBackParentIsNull() {
        // succeeds if no exception is thrown
        var a = MockNode{};
        a.toBack();
    }

    function testRequestLayout() {
        var node = MockNode{id:"node"};
        var custom = MockCustomNode{id:"custom"};
        var group = MockGroup {
            id: "group"
            content: [node, custom]
        };
        var other = MockGroup{id:"other"};
        var root = MockGroup {
            id: "root"
            content: [group, other]
        };

        // I now have a tree that looks like this:
        //              root
        //              /  \
        //           group other
        //           /   \
        //        node  custom

        // reset all the flags
        node.needsLayout = false;
        custom.needsLayout = false;
        group.needsLayout = false;
        other.needsLayout = false;
        root.needsLayout = false;

        // only group and custom nodes need layout (since there is no point in
        // doing layout for leaf nodes). Test that calling requestLayout on
        // "node" causes group and root to both need layout.
        node.requestLayout();
        assertTrue(group.needsLayout);
        assertTrue(root.needsLayout);
        assertFalse(custom.needsLayout);
        assertFalse(other.needsLayout);

        // reset all the flags
        node.needsLayout = false;
        custom.needsLayout = false;
        group.needsLayout = false;
        other.needsLayout = false;
        root.needsLayout = false;

        // test that calling requestLayout on "group" causes both group and root
        // to need layout
        group.requestLayout();
        assertTrue(group.needsLayout);
        assertTrue(root.needsLayout);
        assertFalse(custom.needsLayout);
        assertFalse(other.needsLayout);

        // reset all the flags
        node.needsLayout = false;
        custom.needsLayout = false;
        group.needsLayout = false;
        other.needsLayout = false;
        root.needsLayout = false;

        // test that calling requestLayout on "group" when root is already
        // requiring layout still needs root and group to have layout
        root.needsLayout = true;
        group.requestLayout();
        assertTrue(group.needsLayout);
        assertTrue(root.needsLayout);
        assertFalse(custom.needsLayout);
        assertFalse(other.needsLayout);

        // reset all the flags
        node.needsLayout = false;
        custom.needsLayout = false;
        group.needsLayout = false;
        other.needsLayout = false;
        root.needsLayout = false;

        // test that calling requestLayout on "custom" causes custom, group,
        // and root to all need layout
        custom.requestLayout();
        assertTrue(group.needsLayout);
        assertTrue(root.needsLayout);
        assertTrue(custom.needsLayout);
        assertFalse(other.needsLayout);
    }

    function testLookup() {
        var node = MockNode{id:"node"};
        var custom = MockCustomNode{id:"custom"};
        var group = MockGroup {
            id: "group"
            content: [node, custom]
        };
        var other = MockGroup{id:"other"};
        var root = MockGroup {
            id: "root"
            content: [group, other]
        };

        // I now have a tree that looks like this:
        //              root
        //              /  \
        //           group other
        //           /   \
        //        node  custom

        // look for things starting from root
        assertEquals(root, root.lookup("root"));
        assertEquals(group, root.lookup("group"));
        assertEquals(other, root.lookup("other"));
        assertEquals(node, root.lookup("node"));
        assertEquals(custom, root.lookup("custom"));

        // starting from group
        assertEquals(null, group.lookup("root"));
        assertEquals(group, group.lookup("group"));
        assertEquals(null, group.lookup("other"));
        assertEquals(node, group.lookup("node"));
        assertEquals(custom, group.lookup("custom"));

        // starting from other
        assertEquals(null, other.lookup("root"));
        assertEquals(null, other.lookup("group"));
        assertEquals(other, other.lookup("other"));
        assertEquals(null, other.lookup("node"));
        assertEquals(null, other.lookup("custom"));

        // starting from node
        assertEquals(null, node.lookup("root"));
        assertEquals(null, node.lookup("group"));
        assertEquals(null, node.lookup("other"));
        assertEquals(node, node.lookup("node"));
        assertEquals(null, node.lookup("custom"));

        // starting from custom
        assertEquals(null, custom.lookup("root"));
        assertEquals(null, custom.lookup("group"));
        assertEquals(null, custom.lookup("other"));
        assertEquals(null, custom.lookup("node"));
        assertEquals(custom, custom.lookup("custom"));
    }

    function testTranslate() {
        var node = MockNode {
            translateX:10
            translateY:10
        };

        var fxnode = node.getFXNode();
        var tx = fxnode.getCompositeTransform();
        var point = tx.transform(new Point2D.Double(node.getX(), node.getY()), null);
        assertTrue(point.getX() == 10);
        assertTrue(point.getY() == 10);        
    }

    function testScale() {
        var node = MockNode {
            scaleX:10
            scaleY:10
        };
        node.getSGRectangle().setX(10);
        node.getSGRectangle().setY(10);
        node.getSGRectangle().setWidth(100);
        node.getSGRectangle().setHeight(100);

        var fxnode = node.getFXNode();
        var tx = fxnode.getCompositeTransform();
        var point = tx.transform(new Point2D.Double(node.getX(), node.getY()), null);
        assertTrue(point.getX() == 100);
        assertTrue(point.getY() == 100);        
        point = tx.transform(new Point2D.Double(node.getX() + node.getWidth(), node.getY() + node.getHeight()), null);
        assertTrue(point.getX() == 1100);
        assertTrue(point.getY() == 1100);        
    }

    function testScaleAboutCenter() {
        var node = MockNode {
            scaleX:10
            scaleY:10
            anchorX:60
            anchorY:60
        };
        node.getSGRectangle().setX(10);
        node.getSGRectangle().setY(10);
        node.getSGRectangle().setWidth(100);
        node.getSGRectangle().setHeight(100);

        var fxnode = node.getFXNode();
        var tx = fxnode.getCompositeTransform();
        var point = tx.transform(new Point2D.Double(node.getX(), node.getY()), null);
        assertTrue(point.getX() == -440);
        assertTrue(point.getY() == -440);
        point = tx.transform(new Point2D.Double(node.getX() + node.getWidth(), node.getY() + node.getHeight()), null);
        assertTrue(point.getX() == 560);
        assertTrue(point.getY() == 560);
    }

    function testRotation() {
        var node = MockNode {
            rotate:180
        };
        node.getSGRectangle().setWidth(100);
        node.getSGRectangle().setHeight(100);

        var fxnode = node.getFXNode();
        var tx = fxnode.getCompositeTransform();
        var point = tx.transform(new Point2D.Double(node.getX(), node.getY()), null);
        assertTrue(point.getX() == 0);
        assertTrue(point.getY() == 0);        
        point = tx.transform(new Point2D.Double(node.getX() + node.getWidth(), node.getY() + node.getHeight()), null);
        assertTrue(point.getX() == -100);
        assertTrue(point.getY() == -100);        
    }

    function testRotationAboutCenter() {
        var node = MockNode {
            rotate:180
            anchorX:50
            anchorY:50
        };
        node.getSGRectangle().setWidth(100);
        node.getSGRectangle().setHeight(100);

        var fxnode = node.getFXNode();
        var tx = fxnode.getCompositeTransform();
        var point = tx.transform(new Point2D.Double(node.getX(), node.getY()), null);
        assertTrue(point.getX() == 100);
        assertTrue(point.getY() == 100);        
        point = tx.transform(new Point2D.Double(node.getX() + node.getWidth(), node.getY() + node.getHeight()), null);
        assertTrue(point.getX() == 0);
        assertTrue(point.getY() == 0);        
    }

    // TODO need to test that mouse blocking works correctly
}

private class MockNode extends Node {
    function createSGNode():SGNode {
        new SGRectangle();
    }

    function  getSGRectangle():SGRectangle {
        getSGNode() as SGRectangle;
    }
}

private class MockGroup extends Group {}

private class MockCustomNode extends CustomNode {
    protected function create():Node { MockNode{} }
}