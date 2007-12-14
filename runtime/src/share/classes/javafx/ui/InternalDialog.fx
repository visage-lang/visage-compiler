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



import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;


class InternalDialog extends InternalWindow {
    function updateButtons() {
        while (buttonpanel.getComponentCount() > 0) {
            buttonpanel.remove(0);
        }
        if (sizeof buttons == 0) {
            southPanel.remove(0);
        } else {
            if (sizeof buttons == 1) {
                southPanel.add(buttonpanel);
            }
            //TODO what is this????
            /***********************************
            var builder = new com.jgoodies.forms.builder.ButtonBarBuilder(buttonpanel);
            var btns:javax.swing.JButton[];
            for (i in buttons) {
                insert (i.getComponent() as javax.swing.JButton) into btns;
            }
            builder.addGriddedButtons(btns);
            ************************************/
            southPanel.validate();
        }
    }
    public function onSetContent(w:Widget) {
        centerPanel.add(w.getComponent(),BorderLayout.CENTER);
        panel.validate();
        this.setContent(panel);
    }
    attribute panel: JPanel;
    attribute buttonpanel: JPanel;
    attribute southPanel: JPanel;
    attribute centerPanel: JPanel;

    public attribute buttons: Button[]
        on replace [ndx] (oldValue) {
            updateButtons();
        }
        on insert [ndx] (newValue) {
            updateButtons();
        }
        on delete [ndx] (oldValue) {
            updateButtons();
        };
    init {
            panel = new JPanel();
            panel.setName("internalDialog.content");
            panel.setLayout(new BorderLayout());
            panel.setOpaque(false);
            buttonpanel = new javax.swing.JPanel();
            buttonpanel.setOpaque(false);
            var p = new JPanel();
            p.setLayout(new FlowLayout(FlowLayout.TRAILING));
            p.setBorder(new javax.swing.border.EmptyBorder(6, 0, 0, 0));
            p.add(buttonpanel);
            p.setOpaque(false);
            panel.add(p, java.awt.BorderLayout.SOUTH);
            southPanel = p;
            p = new JPanel();
            p.setOpaque(false);
            //p.setBorder(new net.java.javafx.ui.ShadowedBorder());
            centerPanel = new JPanel();
            centerPanel.setOpaque(false);
            centerPanel.setLayout(new BorderLayout());
            p.setLayout(new BorderLayout());
            centerPanel.setBorder(new javax.swing.border.EmptyBorder(4,4,4,4));
            p.add(centerPanel, BorderLayout.CENTER);
            panel.add(p, BorderLayout.CENTER);
            panel.setBorder(new javax.swing.border.EmptyBorder(6,6,6,6));
            resizable = false;
            anchor = Anchor.CENTER;
    }
}




