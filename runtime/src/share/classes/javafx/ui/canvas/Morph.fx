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


import javafx.ui.Color;
import javafx.ui.Paint;
import com.sun.javafx.api.ui.Morphing2D;
import com.sun.scenario.scenegraph.SGShape;

public class Morph extends Shape {
    public attribute start: Shape;
    public attribute end: Shape;
    public attribute morph: Number on replace {
        morphing2D.setMorphing(morph);
        sgshape.setShape(morphing2D);
        updateFill();
    };
    private attribute morphing2D: Morphing2D;
    private attribute awtStartShape: java.awt.Shape = bind start.getTransformedShape()
        on replace {
            updateMorphing2D();
        };
    private attribute awtEndShape: java.awt.Shape = bind end.getTransformedShape()
        on replace {
            updateMorphing2D();
        };
    private attribute fill1: Paint on replace { // JXFC-XXX = bind start.fill on replace {
        updateFill();
    };
    private attribute fill2: Paint on replace { // JXFC-XXX = bind start.fill on replace {
        updateFill();
    };
    private function updateMorphing2D() {
        if (awtStartShape <> null and awtEndShape <> null) {
            morphing2D = new Morphing2D(awtStartShape, awtEndShape);
            morphing2D.setMorphing(morph);
            if (sgshape <> null) {
                sgshape.setShape(morphing2D);
            }
        }
    }
    private function updateFill() {
        //TODO JXFC-XXX cannot locate attribute in super class with MI
        /*****************
        var fill1 = start.fill;
        var fill2 = end.fill;
        if ((fill1 instanceof Color) and (fill2 instanceof Color)) {
            var c1 = fill1 as Color;
            var c2 = fill2 as Color;
            fill = c1.interpolate(c2, morph);
        }
        ******************/
    }

    public function createShape(): SGShape  {
        var sgshape = new SGShape();
        updateMorphing2D();
        sgshape.setShape(morphing2D);
        return sgshape;
    }

}


