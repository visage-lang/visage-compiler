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

import javafx.ui.UIElement;
import javax.swing.JColorChooser;

public class ColorChooser extends Widget {
    private attribute jcolorchooser:javax.swing.JColorChooser;
    public attribute action: function(c:Color):Void;
    public attribute title: String;
    public attribute owner: UIElement;
    public attribute initialColor: Color = Color.WHITE;
    public attribute selectedColor: Color;
    /**
     * Indicates if Drag and Drop is enabled.
     */
    public attribute enableDND: Boolean = true on replace {
        if(jcolorchooser != null)
            jcolorchooser.setDragEnabled(enableDND);
    };    
    /**
     * Actually bring the dialog up.
     * Since setting this action true in an object literal will 
     * cause the dialog to pop-up immediately, this attribute 
     * must be placed after all other attributes -- so that they
     * will be initialized before it pops up.
     */
    public attribute showDialog: Boolean on replace {
        if (showDialog) {
            show();
        }
    };
    function show():Void {
        var result = javax.swing.JColorChooser.showDialog(owner.getWindow(), 
                                                              title,
                                                              initialColor.getColor());
        visible = false;
        if (result != null and action <> null) {
            action(Color.fromAWTColor(result));
        }
    }

    public function createComponent():javax.swing.JComponent {
        jcolorchooser = new JColorChooser(initialColor.getColor());
        jcolorchooser.setDragEnabled(enableDND);
        jcolorchooser.getSelectionModel().addChangeListener(javax.swing.event.ChangeListener {
                public function stateChanged(e:javax.swing.event.ChangeEvent):Void {
                    selectedColor = Color.fromAWTColor(jcolorchooser.getColor());
                }
            });
        return jcolorchooser;
    }

}

