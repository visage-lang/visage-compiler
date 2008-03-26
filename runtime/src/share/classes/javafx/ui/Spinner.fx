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


public class Spinner extends Widget {

    override attribute focusable = false;

    // TODO MARK AS FINAL
    protected attribute spinner:javax.swing.JSpinner = UIElement.context.createSpinner() on replace {
        spinner.addChangeListener(javax.swing.event.ChangeListener {
            public function stateChanged(e:javax.swing.event.ChangeEvent):Void {
                value = (spinner.getValue() as java.lang.Number).doubleValue();
            }
        }); 
    };
    public attribute min: Number on replace {
        (spinner.getModel() as com.sun.javafx.api.ui.BigDecimalSpinnerModel).setMinimum(min as java.lang.Number);
    };
    public attribute max: Number on replace {
        (spinner.getModel() as com.sun.javafx.api.ui.BigDecimalSpinnerModel).setMaximum(max as java.lang.Number);
    };
    public attribute value: Number on replace {
            (spinner.getModel() as com.sun.javafx.api.ui.BigDecimalSpinnerModel).setValue(value);
    };
    public attribute stepSize: Number on replace {
        (spinner.getModel() as com.sun.javafx.api.ui.BigDecimalSpinnerModel).setStepSize(value);
    };
    public attribute onChange: function(value:Number);
    public attribute editorForeground: Color on replace {
        if (editorForeground <> null) {
            var comp = spinner.getEditor();
            if (comp instanceof javax.swing.JSpinner.DefaultEditor) {
                var def = comp as javax.swing.JSpinner.DefaultEditor;
                def.getTextField().setForeground(editorForeground.getColor());
            }
        }
    };
    public attribute editorBackground: Color on replace {
        if (editorBackground <> null) {
            var comp = spinner.getEditor();
            if (comp instanceof javax.swing.JSpinner.DefaultEditor) {
                var def = comp as javax.swing.JSpinner.DefaultEditor;
                def.getTextField().setBackground(editorBackground.getColor());
            }
        }
    };
    public attribute editorFont: Font on replace {
        if (editorFont <> null) {
            var comp = spinner.getEditor();
            if (comp instanceof javax.swing.JSpinner.DefaultEditor) {
                var def = comp as javax.swing.JSpinner.DefaultEditor;
                def.getTextField().setFont(editorFont.getFont());
            }
        }
    }

    public function createComponent():javax.swing.JComponent {
       return spinner;
    }
}
