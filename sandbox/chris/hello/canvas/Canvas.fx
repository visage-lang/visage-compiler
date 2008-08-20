/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package hello.canvas;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.event.KeyListener;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.lang.Math;
import java.lang.System;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JViewport;
import com.sun.scenario.scenegraph.SGAlignment;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.JSGPanel;
import hello.AbstractWidget;
import hello.Widget;

public class Canvas extends Widget, CanvasElement, Container {

    // private:
    var jsgpanel: JSGPanel;
    var root: SGGroup;
    // public:
    public var content: Node[] on insert[i](c) {
	c.parentCanvasElement = (this as java.lang.Object) as CanvasElement;
	if (root != null) {
	    root.add(c.getNode());
	}
    } on delete[i](c) {
	if (root != null) {
	    root.remove(c.getNode());
	}
    } on replace[i](oldValue) {
	var newValue = content[i];
	newValue.parentCanvasElement = (this as java.lang.Object) as CanvasElement;
	if (root != null) {
	    root.remove(oldValue.getNode());
	    root.add(newValue.getNode());
	}
    }

    //var border = { var n = 5; EmptyBorder {top: n, left: n, right: n, bottom: n}};

    public function getF3(obj:SGNode): Node {
	if (obj == null) { return null; }
	var n = obj.getAttribute("f3") as Node;
	if (n != null) {
	    return n;
	}
	obj = obj.getParent();
	return getF3(obj);
    }

    public function pick(x: Number, y: Number, w: Number, h: Number):Node[] {
	var result:Node[] = [];
	// TODO: SGNode.pick() needs to take (x,y,w,h)
	var path = jsgpanel.getScene().pick(new java.awt.Point(x.intValue(), y.intValue()));
	if (path.isEmpty()) {
	    return result;
	}
	var lastNode:Node = null;
	for (i in [0..path.size()-1]) {
	    var n = getF3(path.get(i) as SGNode);
	    if (n != null and n != lastNode) {
		insert n into result;
	    }
	    lastNode = n;
	}
	return result;
    }


    public function getCanvas() {
	return this;
    }

    public function createComponent():javax.swing.JComponent {
	root = new SGGroup();
	jsgpanel = new JSGPanel();
	jsgpanel.setOpaque(false);
	jsgpanel.setScene(root);
	for (i in content) {
	    i.parentCanvasElement = (this as java.lang.Object) as CanvasElement;
	    root.add(i.getNode());
	}
	return jsgpanel;
    }

    public function raiseNode(n:Node): Void {
    }

    public function lowerNode(n:Node): Void {
    }

    public function moveNodeToFront(n:Node) {
    }

    public function moveNodeToBack(n:Node) {
    }
}

