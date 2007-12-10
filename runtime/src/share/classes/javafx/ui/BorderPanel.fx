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

public class BorderPanel extends Widget {
    private attribute jpanel: JPanel;
    //TODO JFXC-267
    public attribute top:Widget /************** on replace (oldValue)  {
        if (jpanel <> null) {
            if (oldValue <> null) {
                jpanel.remove(oldValue.getComponent());
            }
            if (top <> null) {
                jpanel.add(top.getComponent(), java.awt.BorderLayout.NORTH);
            }
            jpanel.revalidate();
            jpanel.repaint();
        }
    }**********/;

    //TODO JFXC-267
    public attribute left:Widget /**************on replace (oldValue)  {
        if (jpanel <> null) {
            if (oldValue <> null) {
                jpanel.remove(oldValue.getComponent());
            }
            if (left <> null) {
                jpanel.add(left.getComponent(), java.awt.BorderLayout.WEST);
            }
            jpanel.revalidate();
            jpanel.repaint();
        }
    }**********/;
    //TODO JFXC-267
    public attribute bottom:Widget /**************on replace (oldValue) {
        if (jpanel <> null) {
            if (oldValue <> null) {
                jpanel.remove(oldValue.getComponent());
            }
            if (bottom <> null) {
                jpanel.add(bottom.getComponent(), java.awt.BorderLayout.SOUTH);
            }
            jpanel.revalidate();
            jpanel.repaint();
        }
    }**********/;
    //TODO JFXC-267
    public attribute right:Widget /**************on replace (oldValue)  {
        if (jpanel <> null) {
            if (oldValue <> null) {
                jpanel.remove(oldValue.getComponent());
            }
            if (right <> null) {
                jpanel.add(right.getComponent(), java.awt.BorderLayout.EAST);
            }
            jpanel.revalidate();
            jpanel.repaint();
        }
    }**********/;  
    //TODO JFXC-267
    public attribute center:Widget /**************on replace (oldValue)  {
        if (jpanel <> null) {
            if (oldValue <> null) {
                jpanel.remove(oldValue.getComponent());
            }
            if (center <> null) {
                jpanel.add(center.getComponent(), java.awt.BorderLayout.CENTER);
            }
            jpanel.revalidate();
            jpanel.repaint();
        }
    }**********/;
    
    public function createComponent():javax.swing.JComponent {
        jpanel = UIElement.context.createPanel();
        jpanel.setOpaque(false);
        var layout = new java.awt.BorderLayout();
        jpanel.setLayout(layout);
        if (top <> null) {
            jpanel.add(top.getComponent(), java.awt.BorderLayout.NORTH);
        }
        if (left <> null) {
            jpanel.add(left.getComponent(), java.awt.BorderLayout.WEST);
        }
        if (center <> null) {
            jpanel.add(center.getComponent(), java.awt.BorderLayout.CENTER);
        }
        if (bottom <> null) {
            jpanel.add(bottom.getComponent(), java.awt.BorderLayout.SOUTH);
        }
        if (right <> null) {
            jpanel.add(right.getComponent(), java.awt.BorderLayout.EAST);
        }
        jpanel;
    }    
    public attribute focusable:Boolean = false;    
}



