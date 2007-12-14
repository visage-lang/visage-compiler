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

public abstract class Gradient extends Transformable, Paint  {
    protected attribute gradient: com.sun.javafx.runtime.awt.MultipleGradientPaint;
    /** The ramp of colors to use on this gradient */
    public attribute stops: Stop[] 
        on replace [ndx] (oldVaue) {
            createGradient();
        }
        on insert [ndx] (newValue) {
            createGradient();
        }
        on delete [ndx] (oldVaue) {
            createGradient();
        };
    public attribute spreadMethod: SpreadMethod = SpreadMethod.PAD on replace {
        createGradient();
    };
    public attribute colorSpace: ColorSpaceType = ColorSpaceType.SRGB on replace {
        createGradient();
    };
    public attribute transparency: Integer on replace {
        createGradient();
    };

    protected abstract function createGradient():Void;

    public function getGradient():com.sun.javafx.runtime.awt.MultipleGradientPaint {
        if(gradient == null) {
            createGradient();
        }
        return gradient;
    }

     public function getPaint():java.awt.Paint {
        return getGradient() as java.awt.Paint;
     }

    function getColors(): java.awt.Color[] { 
        var colors:java.awt.Color[] = [];
        for (s in stops) {
            insert s.color.getColor() into colors;
        }
        return colors;
    }
    function getFractions(): Number[] {
        var fractions:Number[] = [];
        for (s in stops) {
            insert s.offset into fractions;
        }
        return fractions;
    }
}


