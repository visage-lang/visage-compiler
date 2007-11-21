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
 
package javafx.ui; 

import javafx.ui.canvas.Transformable;

public class Gradient extends AbstractColor, Transformable {
    /** The ramp of colors to use on this gradient */
    public attribute stops: Stop[];
    public attribute spreadMethod: SpreadMethod;
    public attribute gradientUnits: GradientUnits;
    //TODO JFXC-285
    protected attribute holders: StopHolder[] /*****= 
            bind foreach (s in stops ) { 
                StopHolder{
                    gradient: this, 
                    jstop: new com.sun.javafx.api.ui.Stop(), 
                    offset: bind s.offset, 
                    color: bind s.color.getColor()
                }
             }***/;
    protected attribute stopCount: Number;

    //TODO how should this class handle these methods???
    public function getColor(): java.awt.Color { return null;}
    public function getPaint():java.awt.Paint{ return null;}
}

class StopHolder {
    attribute gradient: Gradient;
    attribute jstop: com.sun.javafx.api.ui.Stop;
    attribute offset: Number on replace {
        jstop.setOffset(offset.floatValue());
        gradient.stopCount = gradient.stopCount + 1;
    };
    attribute color: java.awt.Color on replace {
        jstop.setColor(color);
        gradient.stopCount = gradient.stopCount + 1;
    };
}

