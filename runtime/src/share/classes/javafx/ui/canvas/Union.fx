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
import com.sun.scenario.scenegraph.SGShape;

public class Union extends Shape {
    public attribute content: Shape[] on replace {
        this.getArea();
    };

    protected function getArea() {
        if (sgshape <> null) {
            if (sizeof content == 1) {
               sgshape.setShape(content[0].getTransformedShape());
            } else if (sizeof content > 1) {
                var s0 = content[0].getTransformedShape();
                if (s0 == null) {
                    s0 = new java.awt.geom.Rectangle2D.Double();
                }
                var area = new java.awt.geom.Area(s0);
                var i = 0;
                for (c in content) {
                    if (i > 0) {
                        var s1 = c.getTransformedShape();
                        if (s1 <> null) {
                            area.add(new java.awt.geom.Area(s1));
                        }
                    }
                    i = i + 1;
                }
                sgshape.setShape(area);
            }
        }
    }

    public function createShape(): SGShape  {
        sgshape = new SGShape();
        this.getArea();
        return sgshape;
    }
}


