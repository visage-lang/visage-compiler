/* 
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved. 
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

package javafx.ui.filter;

import javafx.ui.Color;
import com.sun.scenario.effect.PhongLighting;
import com.sun.scenario.effect.light.DistantLight;

public class Lighting extends Filter {
    private attribute light : DistantLight;
    private attribute phong : PhongLighting;

    init {
        light = new DistantLight(0, 0, java.awt.Color.WHITE);
        phong = new PhongLighting(light);
        phong.setSurfaceScale(new java.lang.Float(1.5));
        phong.setDiffuseConstant(2);
        phong.setSpecularConstant(new java.lang.Float(0.3));
        phong.setSpecularExponent(20);
    }

    public bound function getImpl():com.sun.scenario.effect.Effect {
        phong
    };

    public attribute lightAzimuth: Number = 0.0
        on replace { light.setAzimuth(lightAzimuth.floatValue()); }
    public attribute lightElevation: Number = 0.0
        on replace { light.setElevation(lightElevation.floatValue()); }
    public attribute lightColor: Color = Color { red: 1; green: 1; blue: 1 }
        on replace { light.setColor(lightColor.getColor()); }
}
