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

import javafx.ui.ScrollableWidget;
import javafx.ui.ContentType;
import java.lang.Object;

public class TextPane extends ScrollableWidget {
    private attribute inUpdate:Boolean;
    private attribute documentListener:javax.swing.event.DocumentListener;
    
    // TODO MARK AS FINAL
    private attribute jtextpane: javax.swing.JTextPane = javax.swing.JTextPane{} on replace {
        jtextpane.getDocument().putProperty("imageCache",
                                            UIElement.context.getImageCache());
        editable = true;
        documentListener = javax.swing.event.DocumentListener {
            public function insertUpdate(e:javax.swing.event.DocumentEvent):Void {
                inUpdate = true;

                inUpdate = false;
            }
            public function removeUpdate(e:javax.swing.event.DocumentEvent):Void {
                inUpdate = true;

                inUpdate = false;
            }
            public function changedUpdate(e:javax.swing.event.DocumentEvent):Void {
                inUpdate = true;

                inUpdate = false;
            }
        };
        jtextpane.getDocument().addDocumentListener(documentListener);          
    };
    public attribute editable: Boolean on replace {
        jtextpane.setEditable(editable);
    };
    public attribute content: Object[] on replace oldValue[lo..hi]=newVals {
        var off = 0;
        for (i in [0..<lo]) {
            var e = content[i];
            if (e instanceof String) {
                off += (e as String).length();
            } else {
                off += 1;
            }
        }  
        for(element in oldValue[lo..hi]) { 
            var endOff = off;
            if (element instanceof String) {
                endOff += (element as String).length();
            }
            jtextpane.select(off, endOff);
            jtextpane.replaceSelection("");
        }
        
        var ndx = lo;
        for(element in newVals) {
            jtextpane.select(off, off);
            if (element instanceof Widget) {
                jtextpane.insertComponent((element as Widget).getComponent());
                off++;
            } else if (element instanceof Icon) {
                jtextpane.insertIcon((element as Icon).getIcon());
                off++;
            } else {
                jtextpane.replaceSelection("{element}");
                off += "{element}".length();
            }
            ndx++
        }
    };
    
    public attribute contentType: ContentType on replace {
        jtextpane.setContentType(contentType.mimeType);
    };
    
    public function createView(): javax.swing.JComponent {
        return jtextpane;
    }

    
    init {

    }
}


