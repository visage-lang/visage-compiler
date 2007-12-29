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

import javafx.ui.AbstractFrame;
import javafx.ui.UIElement;
import javafx.ui.Button;
import javafx.ui.Widget;
import javafx.ui.Border;
import javafx.ui.EmptyBorder;

public class Dialog extends AbstractFrame {
    private attribute jdialog:javax.swing.JDialog;
    private attribute p:javax.swing.JPanel;
    private attribute buttonpanel: javax.swing.JPanel;

    public attribute modal:Boolean;
    public attribute owner: UIElement;
    public attribute title: String;
    public attribute content:Widget on replace {
        if (content.getComponent() <> null) {
            p.add(content.getComponent(), java.awt.BorderLayout.CENTER);
        }
    };
    public attribute buttons: Button[]
        on insert [indx] (button) {
            if (buttonpanel == null) {
                buttonpanel = new javax.swing.JPanel();
                buttonpanel.setLayout(new javax.swing.BoxLayout(buttonpanel,
                                javax.swing.BoxLayout.X_AXIS));
                buttonpanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 0, 0));
                buttonpanel.add(javax.swing.Box.createHorizontalGlue());
                p.add(buttonpanel, java.awt.BorderLayout.SOUTH);
            }
            buttonpanel.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(5,0)));
            buttonpanel.add(button.getComponent());
            var dim = new java.awt.Dimension(80,0);
            for (i in buttons) {
                var d = i.getComponent().getPreferredSize();
                if (dim.height < d.height) {
                    dim.height = d.height;
                }
                if (dim.width < d.width) {
                    dim.width = d.width;
                }
            }
            for (i in buttons) {
                i.getComponent().setPreferredSize(dim);
                i.getComponent().setMinimumSize(dim);
                i.getComponent().setMaximumSize(dim);
            }
        };
    public attribute height:Number;
    public attribute width: Number;
    public attribute border: Border on replace {
        p.setBorder(border.getBorder());
    };
    public attribute visible: Boolean on replace {
         if (visible) {
            //TODO DO LATER
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                      public function run():Void {
                           show();
                      }
            });
         } else {
            hide();
         }
    };
    public function show() {
        var win = owner.getWindow();
        if (win instanceof java.awt.Dialog) {
            jdialog = new javax.swing.JDialog(win as java.awt.Dialog);
        } else {
            jdialog = new javax.swing.JDialog(win as java.awt.Frame);
        }
        jdialog.setContentPane(p);
        win.addWindowListener(java.awt.event.WindowAdapter   {
                  public function windowClosing(e:java.awt.event.WindowEvent):Void {
                      var root = jdialog.getRootPane();
                      var but = root.getClientProperty("net.java.javafx.ui.defaultClose") as javax.swing.JButton;
                      if (but <> null) {
                            but.doClick(0);
                      }
                      if(onClose <> null)
                        onClose();
                      jdialog.dispose();
                      jdialog = null;
                  }
              } as java.awt.event.WindowListener);
        jdialog.setModal(modal);
        jdialog.setTitle(title);
        jdialog.pack();
        var dim = jdialog.getSize();
        if (height <> -1) {
            dim.height = height.intValue();
        }
        if (width <> -1) {
            dim.width = width.intValue();
        }
        jdialog.setSize(dim);
        if (win <> null) {
            jdialog.setLocationRelativeTo(win);
        }
        jdialog.requestFocus();
        jdialog.show();
    }

    public function showDialog(w:UIElement){
        owner = w;  
        this.show();
    }
    public function hide():Void {
        jdialog.dispose();
        jdialog = null;
    }
    public attribute onClose: function():Void;

    init {
        height = -1;
        width = -1;
        p = new javax.swing.JPanel();
        p.setLayout(new java.awt.BorderLayout());
        border = EmptyBorder {top: 10 left: 10 right: 10 bottom: 10};
        
        //init buttons
        if (buttonpanel == null) {
            buttonpanel = new javax.swing.JPanel();
            buttonpanel.setLayout(new javax.swing.BoxLayout(buttonpanel,
                            javax.swing.BoxLayout.X_AXIS));
            buttonpanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 0, 0));
            buttonpanel.add(javax.swing.Box.createHorizontalGlue());
            p.add(buttonpanel, java.awt.BorderLayout.SOUTH);
        }
        for(i in buttons) {
            buttonpanel.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(5,0)));
            buttonpanel.add(i.getComponent());      
        }

        var dim = new java.awt.Dimension(80,0);
        for (i in buttons) {
            var d = i.getComponent().getPreferredSize();
            if (dim.height < d.height) {
                dim.height = d.height;
            }
            if (dim.width < d.width) {
                dim.width = d.width;
            }
        }
        for (i in buttons) {
            i.getComponent().setPreferredSize(dim);
            i.getComponent().setMinimumSize(dim);
            i.getComponent().setMaximumSize(dim);
        }        
    }
}

