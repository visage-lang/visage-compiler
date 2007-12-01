/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
 * particular file as subject to the "Classpath" exception as provided 
 * by Sun in the LICENSE file that accompanied this code. 
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

public class HBox extends CompositeNode {
    private attribute holders: Group[] = 
          bind foreach (ndx in [0..sizeof content exclusive])
             Group {
                  content: [ content[ndx] ]
                  //var prev = if (ndx > 0 and hasParent() )
                 //                then holders[ndx-1] else null
                 // var x = if (prev == null)
                 //           then 0 else prev.currentX + prev.currentWidth
                  transform: bind [ Translate.translate(
                            (if (ndx > 0 and hasParent() )
                                then holders[ndx-1].currentX + holders[ndx-1].currentWidth
                                else 0)                            
                            +(if (ndx > 0) then spacing else 0), 0.0) as Transform ]
             }
        ;
    public attribute content: Node[];
    public attribute spacing: Number;
    public function composeNode(): Node {
        Group {
            content: bind holders
        };
    }
}


