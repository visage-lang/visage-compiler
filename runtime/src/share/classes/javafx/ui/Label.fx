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

import javax.swing.event.*;

public class Label extends Widget {

    override attribute focusable = false;

    private attribute updateCount: Number;
    private attribute jlabel: com.sun.javafx.api.ui.XLabel;
    public attribute text: String on replace {
        if (jlabel <> null) {
            //TODO ++
            updateCount = updateCount + 1;
            var c = updateCount;
            //TODO DO LATER - this is a work around until a more permanent solution is provided
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                      public function run():Void {
                        if (c == updateCount) {
                            jlabel.setText(text);
                            if (jlabel.isFocusable()) {
                                jlabel.select(0, 0);
                            }
                         } 
                      }
            });
        }
    };
    public attribute honorDisplayProperties: Boolean = true on replace  {
        if (jlabel <> null) {
            jlabel.putClientProperty(jlabel.HONOR_DISPLAY_PROPERTIES, honorDisplayProperties);
        }
    };
    public attribute preloadImages: Boolean;
    public attribute onHyperlinkActivated: function(url:String):java.lang.Object;

    public function createComponent():javax.swing.JComponent {
        jlabel = UIElement.context.createLabel();	
        jlabel.addHyperlinkListener(HyperlinkListener {
                public function hyperlinkUpdate(e:HyperlinkEvent):Void {
                    var type = e.getEventType();
                    if (type == HyperlinkEvent.EventType.ACTIVATED) {
                        var url = e.getDescription();
                        if (not url.startsWith("object;")) {
                            if(onHyperlinkActivated <> null) {
                                onHyperlinkActivated(url);
                            }
                        }
                    }
                }
            });
        jlabel.setFocusable(true);
        jlabel.setOpaque(false);
        if (text <> null) {
            jlabel.setText(text);
            if (jlabel.isFocusable()) {
                jlabel.select(0, 0);
            }
        }
        jlabel.putClientProperty(jlabel.HONOR_DISPLAY_PROPERTIES,
                                 honorDisplayProperties);
        jlabel.setPreloadImages(preloadImages);
        return jlabel;
    }
}



