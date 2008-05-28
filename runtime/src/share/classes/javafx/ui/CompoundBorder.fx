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

package javafx.ui;


public class CompoundBorder  extends Border {
    public attribute borders: Border[];
    
    public bound function getBorder():javax.swing.border.Border {
        constructCompoundBorder(borders);
    }
    
    private function constructCompoundBorder(theBorders: Border[]):javax.swing.border.Border {
        var result = new javax.swing.border.CompoundBorder(theBorders[0].getBorder(),
                                                        theBorders[1].getBorder());
        //for (i in [2..<sizeof borders) {
        //TODO fix sizeof
        //var numBorders = sizeof borders - 1;
        //WORK AROUND
        var numBorders = 0;
        for(b in theBorders) numBorders = numBorders + 1;  //TODO: revert to ++ when that works
        // END WORK AROUND
        if(numBorders > 2) {
            for (i in [2..<numBorders]) {
                result = new javax.swing.border.CompoundBorder(result,
                                                            theBorders[i].getBorder());
            };
        }
        return result;
    }

}
