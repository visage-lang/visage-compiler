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

package javafx.scene.layout;

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGGroup;
import javafx.scene.Group;

/**
 * @profile common
 */
public class HBox extends Group {

    public attribute spacing:Number on replace {
        impl_requestLayout();
    }

    init {
        impl_layout = doHBoxLayout;
    }

    // PENDING(shannonh) - should be private. Fix after http://openjfx.java.sun.com/jira/browse/JFXC-1421
    public function doHBoxLayout(g:Group):Void {
        var x:Number = 0;
        var y:Number = 0;
        for (node in content) {
            if (node.visible) {
                node.impl_layoutX = x;
                node.impl_layoutY = y;
                x += node.getBoundsWidth() + spacing;
            }
        }
    }
}
