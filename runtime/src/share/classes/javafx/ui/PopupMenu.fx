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

import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;



public class PopupMenu {
    private attribute jpopupMenu: JPopupMenu;
    public attribute owner: Widget;
    public attribute items: MenuItem[]
        on insert [ndx] (item) {
            if (jpopupMenu <> null) {
                jpopupMenu.add( item.getComponent(), ndx);
            }
        }
        on delete [ndx] (item) {
            if (jpopupMenu <> null) {
                jpopupMenu.remove(ndx);
            }
        }
        on replace [ndx] (item) {
            if (jpopupMenu <> null) {
                jpopupMenu.remove(ndx);
                jpopupMenu.add(items[ndx].getComponent(), ndx);
            }
        };
    public attribute x: Number;
    public attribute y: Number;
    public attribute visible: Boolean on replace {
        if (visible) {
            if (jpopupMenu == null) {
                jpopupMenu = new JPopupMenu();
                for (i in items) {
                    jpopupMenu.add(i.getComponent());
                }
                jpopupMenu.addPopupMenuListener(PopupMenuListener {
                        public function popupMenuWillBecomeVisible(e:PopupMenuEvent):Void {
                        }
                        public function popupMenuWillBecomeInvisible(e:PopupMenuEvent):Void {
                            //TODO DO LATER - this is a work around until a more permanent solution is provided
                            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                                      public function run():Void {
                                           visible = false;
                                      }
                            });
                        }
                        public function popupMenuCanceled(e:PopupMenuEvent):Void {
                            //TODO DO LATER - this is a work around until a more permanent solution is provided
                            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                                      public function run():Void {
                                           visible = false;
                                      }
                            });
                        }
                    });
            }
            //TODO DO LATER - this is a work around until a more permanent solution is provided
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                      public function run():Void {
                           jpopupMenu.show(owner.getComponent(), x.intValue(), y.intValue());
                      }
            });
        } else {
            jpopupMenu.setVisible(false);
        }
    };
}



