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

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.fx.FXNode;


/**
 * Define your own node classes by extending this class and 
 * implementing its create method.
 * 
 * @profile common
 */
public abstract class CustomNode extends Node {

     attribute content:Node;

    /**
     * Returns the root of the hierarchy that defines this CustomNode.  
     * The <code>create</code> function is called once, when the 
     * CustomNode is constructed.
     * 
     * @profile common
     */          
    protected abstract function create():Node;

    function createSGNode(): SGNode { 
        var group:SGGroup = new SGGroup();
        return group;
    }

    postinit {
        content = create();
        content.parent = this;
        getSGGroup().add(content.getFXNode());
        content.requestLayout();
    }

    function getSGGroup(): SGGroup { getSGNode() as SGGroup }
}
