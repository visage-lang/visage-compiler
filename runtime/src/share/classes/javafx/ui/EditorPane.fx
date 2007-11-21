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


public class EditorPane extends ScrollableWidget {
    private attribute inUpdate:Boolean;
    private attribute documentListener:javax.swing.event.DocumentListener;
    private attribute jeditorpane: javax.swing.JEditorPane;
    public attribute editable: Boolean = true on replace {
        jeditorpane.setEditable(editable);
    };
    public attribute text: String on replace {
        if (not inUpdate) {
           jeditorpane.setText(text);
        }
    };
    public attribute honorDisplayProperties: Boolean on replace {
        jeditorpane.putClientProperty(jeditorpane.HONOR_DISPLAY_PROPERTIES,
                                      honorDisplayProperties);
    };
    public attribute margin:Insets on replace {
        jeditorpane.setMargin(margin.awtinsets);
    };
    public attribute contentType: ContentType on replace {
        jeditorpane.setContentType(contentType.mimeType);
    };
    public attribute editorKit: javax.swing.text.EditorKit on replace  {
        jeditorpane.setEditorKit(editorKit);
    };

    public function createView():javax.swing.JComponent {
        if(jeditorpane == null) {
            jeditorpane = javax.swing.JEditorPane{};
            jeditorpane.setOpaque(false);
            jeditorpane.getDocument().putProperty("imageCache",
                                                  UIElement.context.getImageCache());
            documentListener = javax.swing.event.DocumentListener{
                public function insertUpdate(e:javax.swing.event.DocumentEvent):Void {
                    inUpdate = true;
                    text = jeditorpane.getText();
                    inUpdate = false;
                }
                public function removeUpdate(e:javax.swing.event.DocumentEvent):Void {
                    inUpdate = true;
                    text = jeditorpane.getText();
                    inUpdate = false;
                }
                public function changedUpdate(e:javax.swing.event.DocumentEvent):Void {
                    inUpdate = true;
                    text = jeditorpane.getText();
                    inUpdate = false;
                }
            };
            jeditorpane.getDocument().addDocumentListener(documentListener);
        }
        jeditorpane.select(0,0);
        return jeditorpane;
    }


}




