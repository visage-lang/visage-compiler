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

package javafx.ui.filter;

import javafx.ui.Image;

public class Identity extends Filter {
    private attribute identity = new com.sun.scenario.effect.Identity(null);

    public bound function getImpl():com.sun.scenario.effect.Effect {
        identity
    };
    
    public attribute source: Image
        on replace { identity.setSource(source.getImage() as java.awt.image.BufferedImage); }

    private function ul():Void {
        identity.setLocation(new java.awt.geom.Point2D.Double(x, y));
    }
    public attribute x: Number on replace { ul(); }
    public attribute y: Number on replace { ul(); }
}