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


public class CenterPanel extends Widget {
    private function replaceComponent(w:Widget):Void {
        var constraint = new java.awt.GridBagConstraints();
        constraint.anchor = Anchor.CENTER.id.intValue();
        constraint.fill = Fill.NONE.id.intValue();
        constraint.gridx = java.awt.GridBagConstraints.RELATIVE;
        constraint.gridy = java.awt.GridBagConstraints.RELATIVE;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.weightx = 0;
        constraint.weighty = 0;
        constraint.ipadx = 0;
        constraint.ipady = 0;
        if (jpanel.getComponentCount() > 0) {
            jpanel.remove(0);
        }
        jpanel.add(w.getComponent(), constraint);
        jpanel.validate();
    }
    private attribute jpanel: javax.swing.JPanel;
    public attribute content: Widget on replace {
        this.replaceComponent(content);
    };

    public function createComponent():javax.swing.JComponent {
        jpanel = javax.swing.JPanel{};
        jpanel.setOpaque(false);
        jpanel.setLayout(new java.awt.GridBagLayout());
        if (content <> null) {
            this.replaceComponent(content);
        }
        return jpanel;
    }
    public attribute focusable:Boolean = false;
}


