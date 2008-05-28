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

public class Reflection extends Filter {
    private attribute reflect = new com.sun.scenario.effect.Reflection();

    public bound function getImpl():com.sun.scenario.effect.Effect {
        reflect
    };

    public attribute topOffset: Number = 0
        on replace { reflect.setTopOffset(topOffset.floatValue()); }
    public attribute topOpacity: Number = 0.5
        on replace { reflect.setTopOpacity(topOpacity.floatValue()); }
    public attribute bottomOpacity: Number = 0
        on replace { reflect.setBottomOpacity(bottomOpacity.floatValue()); }
    public attribute fraction: Number = 0.75
        on replace { reflect.setFraction(fraction.floatValue()); }
}
