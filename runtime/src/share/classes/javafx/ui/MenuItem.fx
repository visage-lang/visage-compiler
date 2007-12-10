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
import javafx.ui.Accelerator;
import javafx.ui.KeyStroke;
import javafx.ui.Icon;

import java.awt.event.ActionEvent;

import javafx.ui.AbstractMenuItem;

public class MenuItem extends AbstractMenuItem {
    protected attribute jmenuitem:javax.swing.JMenuItem;
    protected function createMenuItem():javax.swing.JMenuItem{
        var menuItem = new javax.swing.JMenuItem();
        //TODO Widget  UIContext
        //menuItem.setIcon(new javax.swing.ImageIcon(context.getTransparentImage(13, 13)));
        return menuItem;
    }

    public attribute mnemonic: KeyStroke on replace {
        if (jmenuitem <> null) {
            jmenuitem.setMnemonic(mnemonic.id);
        }
    }
    public attribute accelerator: Accelerator on replace  {
        if (jmenuitem <> null) {
            var mask = 0;
            foreach (i in accelerator.modifier) {
                var id = i.id;
                mask = mask + id;
            }
            jmenuitem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(accelerator.keyStroke.id, mask)); 
        }
    }

    public attribute text: String on replace {
        if (jmenuitem <> null) {
            jmenuitem.setText(text);
        }
    }
    public attribute icon: Icon on replace {
        if (jmenuitem <> null) {
            jmenuitem.setIcon(icon.getIcon());
        }
    }
    public attribute action: function():Void;
    
    public function createComponent():javax.swing.JComponent {
        jmenuitem = this.createMenuItem();
        jmenuitem.setOpaque(false);
        jmenuitem.addActionListener(java.awt.event.ActionListener {
                                        public function actionPerformed(e:ActionEvent):Void {
                                            if (action <> null) {
                                                action();
                                            }
                                        }
                                    });
        if (mnemonic <> null) {
            jmenuitem.setMnemonic(mnemonic.id);
        }
        if (icon <> null) {
            jmenuitem.setIcon(icon.getIcon());
        }
        if (accelerator <> null) {
            var mask = 0;
            foreach (i in accelerator.modifier) {
                var id = i.id;
                mask = mask + id;
            }
            jmenuitem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(accelerator.keyStroke.id, mask)); 
        }
        if (text <> null) {
            jmenuitem.setText(text);
        }
        return jmenuitem;
    }    
}
