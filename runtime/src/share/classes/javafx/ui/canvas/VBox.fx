/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
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
 
package javafx.ui.canvas; 

import javafx.ui.*;
import java.lang.System;

public class VBox extends CompositeNode {
    private attribute holders: VBoxHolder[] = bind for (i in content) VBoxHolder {
            vbox: this, content: i
    }
    function doLayout():Void {
             var y = 0;
             var j = 0;
             for (i in content) {
                  holders[j].transform = [Transform.translate(0, y)];
                  y += i.currentY  + i.currentHeight + spacing;
                  j++;
             }
    }
    public attribute content: Node[];
    public attribute spacing: Number;
    
    public function composeNode(): Node {
        return  Group {
            content: bind holders
        };
    }
}

