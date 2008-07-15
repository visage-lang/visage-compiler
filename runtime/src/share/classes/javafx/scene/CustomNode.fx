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
import com.sun.scenario.scenegraph.fx.FXNode;


/**
 * Define your own node classes by extending this class and 
 * implementing its create method. Example:
 
 <pre><code>public class Bars extends CustomNode {
    public function create():Node {
        return Group {
            content: for(x in [0..4]) {
                Rectangle {
                    y: indexof x * 20
                    width: 100 
                    height: 10 
                    fill:Color.RED
                }
            }
        }
    }
}

Bars { }</code></pre>
 *
 *<p><img src="doc-files/CustomNode01.png"/></p>
 * 
 * @profile common
 * @needsreview josh
 */
public abstract class CustomNode extends Node {

    /**
     * @treatasprivate implementation detail
     */
     public attribute impl_content:Node;

    /**
     * Returns the root of the hierarchy that defines this CustomNode.  
     * The <code>create</code> function is called once, when the 
     * CustomNode is constructed.
     * 
     * @profile common
     */          
    protected abstract function create():Node;

    /**
     * @treatasprivate implementation detail
     */
    public function impl_createSGNode(): SGNode { 
        var group:SGGroup = new SGGroup();
        return group;
    }

    postinit {
        impl_content = create();
        impl_content.parent = this;
        getSGGroup().add(impl_content.impl_getFXNode());
        impl_content.impl_requestLayout();
    }

    function getSGGroup(): SGGroup { impl_getSGNode() as SGGroup }
}
