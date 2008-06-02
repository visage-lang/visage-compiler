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
 
package javafx.ui.canvas; 


import com.sun.scenario.scenegraph.SGShape;

public class Arc extends Shape {
    // TODO MARK AS FINAL
    private attribute awtArc: java.awt.geom.Arc2D.Double;

    public attribute x: Number on replace {
        awtArc.setArc(x, y, width, height, startAngle, length, closure.id.intValue());
        sgshape.setShape(awtArc);
    };
    public attribute y: Number on replace {
        awtArc.setArc(x, y, width, height, startAngle, length, closure.id.intValue());
        sgshape.setShape(awtArc);
    };
    public attribute width: Number on replace {
        awtArc.setArc(x, y, width, height, startAngle, length, closure.id.intValue());
        sgshape.setShape(awtArc);
    };
    public attribute height: Number on replace {
        awtArc.setArc(x, y, width, height, startAngle, length, closure.id.intValue());
        sgshape.setShape(awtArc);
    };
    public attribute startAngle: Number on replace {
        awtArc.setArc(x, y, width, height, startAngle, length, closure.id.intValue());
        sgshape.setShape(awtArc);
    };
    public attribute length: Number on replace {
        awtArc.setArc(x, y, width, height, startAngle, length, closure.id.intValue());
        sgshape.setShape(awtArc);
    };
    public attribute closure: ArcClosure = ArcClosure.OPEN on replace {
        awtArc.setArc(x, y, width, height, startAngle, length, closure.id.intValue());
        sgshape.setShape(awtArc);
    };
    public function createShape(): SGShape {
        awtArc = new java.awt.geom.Arc2D.Double(x, y, width, height, startAngle, length, closure.id.intValue());
        var sgshape = new SGShape();
        sgshape.setShape(awtArc);
        return sgshape;
    }
}




