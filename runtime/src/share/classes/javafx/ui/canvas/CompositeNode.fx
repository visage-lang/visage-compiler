/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafx.ui.canvas;

import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import java.lang.Exception;
import javafx.ui.canvas.CanvasElement;

/**
 * Abstract base class for user-defined Node's. You can create your
 * own node classes by extending this class and implementing its
 * <code>composeNode()</code> method.
 */
public abstract class CompositeNode extends Node {

    private attribute __composite__: Node;

    /** Abstract method which returns the content of this user-defined node */
    protected abstract function composeNode(): Node;

    public function createNode(): SGNode {
        __composite__ = this.composeNode();
        __composite__.parentCanvasElement = this;
        var n = __composite__.getNode();
        if (n == null) {
            throw new Exception("CompositeNode class {this.getClass().getName()} returned null from composeNode()");
        }
        var sggroup = new SGGroup();
        sggroup.add(n);
        //sggroup.putAttribute("f3", this);
        return sggroup; // TODO: couldn't we simply return "n" itself?
    }
}
