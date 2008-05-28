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

import javafx.ui.UIElement;
import javafx.ui.AbstractFrame;
import javafx.ui.MenuBar;
import javafx.ui.Dialog;
import javafx.ui.Image;

class Window extends AbstractFrame {
    public attribute disposeOnClose: Boolean = true;
    public attribute screenx: Number on replace  {
        frame.setLocation(new java.awt.Point(screenx.intValue(), screeny.intValue()));
    };
    public attribute screeny: Number on replace {
        frame.setLocation(new java.awt.Point(screenx.intValue(), screeny.intValue()));
    };
    public attribute owner: UIElement;
    public attribute frame: javax.swing.JWindow = javax.swing.JWindow{} on replace {
        frame.setBackground(new java.awt.Color(0,0,0,0));
        win = frame;
        win.addWindowListener(java.awt.event.WindowAdapter  {
              public function windowClosing(e:java.awt.event.WindowEvent) {
                  if(onClose <> null)
                        onClose();
                  if (disposeOnClose) {
                      frame.dispose();
                  }
              }
          } as java.awt.event.WindowListener);        
    }
    public attribute menubar: MenuBar on replace {
        //TODO
    };;
    public attribute content: Widget on replace {
        this.setContentPane(content);
    };
    public attribute title: String on replace {
        //TODO
    };;
    public attribute height: Number  = -1 on replace  {
        var dim = frame.getSize();
        dim.height = height.intValue();
        frame.setSize(dim);
    };
    public attribute width: Number = - 1 on replace  {
        var dim = frame.getSize();
        dim.width = width.intValue();
        frame.setSize(dim);
    };
    public function show(){
        this.pack();
        var dim = frame.getSize();
        if (height <> -1) {
            dim.height = height.intValue();
        }
        if (width <> -1) {
            dim.width = width.intValue();
        }
        frame.setSize(dim);
        frame.setLocation(-2000, -2000);
        frame.setVisible(true);
        frame.setVisible(false);
        if (owner <> null) {
            frame.setLocationRelativeTo(owner.getWindow());
        } else {
            frame.setLocation(screenx.intValue(), screeny.intValue());
        }
        frame.setVisible(true);
    }
    public function hide(){
        frame.setVisible(false);
    }
    public function pack()  {
        frame.pack();
    };
    public function showDialog(d:Dialog) {
        //Empty
    };
    public attribute onClose: function():Void;
    public attribute visible: Boolean = true on replace  {
         if (visible) {
            this.pack();
            //TODO DO LATER - this is a work around until a more permanent solution is provided
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                          public function run():Void {
                               show();
                          }
                });

         } else {
            frame.setVisible(false);
         }
    };
    public attribute resizable: Boolean on replace {
        //TODO
    };
    public attribute iconImage: Image on replace {
        //TODO
    };
    public attribute undecorated: Boolean on replace {
        //TODO
    };
    public function setContentPane(w:Widget) {
        frame.setContentPane(w.getComponent());
        frame.validate();
    }

    public function close() {
        frame.dispose();
        frame = null;
    }


}




