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

import javax.swing.event.InternalFrameEvent;

public class InternalFrame extends Widget {
    // private
    private attribute jinternalframe: javax.swing.JInternalFrame = new javax.swing.JInternalFrame();;
    // public
    public attribute isPalette: Boolean on replace {
        jinternalframe.putClientProperty("JInternalFrame.isPalette", true);
    };
    public attribute menubar: MenuBar on replace {
        jinternalframe.setJMenuBar(menubar.getComponent() as javax.swing.JMenuBar);
    };
    public attribute layer: Number;
    public attribute desk: DesktopPane;
    public attribute title: String on replace  {
        jinternalframe.setTitle(title);
    };
    public attribute content: Widget on replace {
        if (content <> null) {
            jinternalframe.setContentPane(content.getComponent());
            content.getComponent().setOpaque(true);
        }
    };
    public attribute maximizable:Boolean on replace {
        jinternalframe.setMaximizable(maximizable);
    };
    public attribute onClose: function();
    //TODO ?? public function toFront();
    public attribute selected: Boolean on replace {
        if (selected) {
            jinternalframe.setVisible(true);
            jinternalframe.setSelected(selected);
        } 
    };
    public attribute deselected: Boolean on replace {
        if (deselected) {
            jinternalframe.setVisible(deselected);
        }
    };
    public attribute closable: Boolean = true on replace  {
        jinternalframe.setClosable(closable);
    };
    public attribute resizable: Boolean = true on replace {
        jinternalframe.setResizable(resizable);
    };
    public attribute iconifiable: Boolean = true on replace {
        jinternalframe.setIconifiable(iconifiable);
    };

    public function createComponent():javax.swing.JComponent {
        var self = this;
        jinternalframe.addInternalFrameListener(javax.swing.event.InternalFrameAdapter {
            public function internalFrameActivated(e:javax.swing.event.InternalFrameEvent):Void {
                for (i in desk.frames) {

                    if (i <> self) {
                        i.selected = false;
                    }
                }
                selected = true;
            }
        } as javax.swing.event.InternalFrameListener );
        return jinternalframe;
    }

    public function setBounds(b:java.awt.Rectangle):Void {
        jinternalframe.getDesktopPane().getDesktopManager().setBoundsForFrame(jinternalframe, b.x, b.y, b.width, b.height);
    }
    
    init {
        // override defaults in superclass
	focusable = false; //TODO: should be protected by not isInitialized

        jinternalframe.addInternalFrameListener( javax.swing.event.InternalFrameListener{
            public function internalFrameOpened(e: InternalFrameEvent):Void {
                //empty
            }

            public function internalFrameClosing(e: InternalFrameEvent):Void {
                if(onClose <> null) {
                    onClose();
                }
            }

            public function internalFrameClosed(e: InternalFrameEvent):Void {
                //empty
            }

            public function internalFrameIconified(e: InternalFrameEvent):Void {
                //empty
            }

            public function internalFrameDeiconified(e: InternalFrameEvent):Void {
                //empty
            }

            public function internalFrameActivated(e: InternalFrameEvent):Void {
                //empty
            }

            public function internalFrameDeactivated(e: InternalFrameEvent):Void {
                //empty
            }
        });


    }
}





