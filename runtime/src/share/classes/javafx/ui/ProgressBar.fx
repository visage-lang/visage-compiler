/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
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
 
package javafx.ui; 

import javafx.ui.Widget;
import javafx.ui.Orientation;

public class ProgressBar extends Widget {

    override attribute focusable = false;

    // TODO MARK AS FINAL
    private attribute jprogressbar: javax.swing.JProgressBar;
    public attribute min: Number = 0 on replace {
        if(jprogressbar <> null) {
            jprogressbar.setMinimum(min.intValue());
        }
    };
    public attribute max: Number = 100 on replace {
        if(jprogressbar <> null) {
            jprogressbar.setMaximum(max.intValue());
        }
    };
    public attribute value: Number = 0 on replace {
        if(jprogressbar <> null) {
            jprogressbar.setValue(value.intValue());
        }
    };
    public attribute stringPainted: Boolean = false on replace {
        if(jprogressbar <> null) {
            jprogressbar.setStringPainted(stringPainted);
        }
    };
    public attribute borderPainted: Boolean = false on replace {
        if(jprogressbar <> null) {
            jprogressbar.setBorderPainted(borderPainted);
        }
    };
    public attribute string: String on replace {
        if(jprogressbar <> null) {
            jprogressbar.setString(string);
        }
    };
    public attribute orientation: Orientation = Orientation.HORIZONTAL on replace {
        if(jprogressbar <> null) {
            jprogressbar.setOrientation(if (orientation == Orientation.VERTICAL) 
                                then jprogressbar.VERTICAL
                                else jprogressbar.HORIZONTAL);
        }
    };
    public attribute indeterminate: Boolean = false on replace {
        if(jprogressbar <> null) {
            jprogressbar.setIndeterminate(indeterminate);
        }
    };
    public function createComponent():javax.swing.JComponent{
        jprogressbar = new javax.swing.JProgressBar();
        jprogressbar.setIndeterminate(indeterminate);
        jprogressbar.setMinimum(min.intValue());
        jprogressbar.setMaximum(max.intValue());
        jprogressbar.setValue(value.intValue());
        jprogressbar.setString(string);
        jprogressbar.setStringPainted(stringPainted);
        jprogressbar.setBorderPainted(borderPainted);
        jprogressbar.setOrientation(if (orientation == Orientation.VERTICAL) 
                                    then jprogressbar.VERTICAL
                                    else jprogressbar.HORIZONTAL);
        return jprogressbar;
    }
}

