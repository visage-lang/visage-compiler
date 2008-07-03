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

package javafx.scene;

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGGroup;

// PENDING_DOC_REVIEW
/**
 * <p>The {@code Group} class represents a list of {@link Node}s objects.
 * Example:</p>
 *
@example
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.geometry.*;
Group {
    content: for(x in [0..4]) {
        Rectangle {
            y: indexof x * 20
            width: 100 
            height: 10 
            fill:Color.RED
        }
    }
}
 *
 * @profile common
 * @needsreview
 */      
public class Group extends Node {

    /**
     * @treatasprivate implementation detail
     */
    public function impl_createSGNode(): SGNode { new SGGroup() }

    function getSGGroup(): SGGroup { impl_getSGNode() as SGGroup }

    // PENDING_DOC_REVIEW
    /**
     * Replaces the current array of {@link Node}s with the specified array. 
     *
     * @profile common
     */          
    public attribute content: Node[] on replace oldNodes[a..b] = newNodes {
        for (node in oldNodes[a..b]) {
            getSGGroup().remove(node.impl_getFXNode());
            node.parent = null;
        }
        var index = a;
        for (node in newNodes) {
            getSGGroup().add(index, node.impl_getFXNode());
            index = index + 1;
            node.parent = this;
        }
        requestLayout();
    }
       
    public attribute layout: function(g:Group):Void = null;

}
