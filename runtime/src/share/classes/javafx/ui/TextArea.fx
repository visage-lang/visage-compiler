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

import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JTextArea;

public class TextArea extends ScrollableWidget {
    private attribute caretColor: Color = bind foreground on replace {
        jtextarea.setCaretColor(caretColor.getColor());
    };
    private attribute inUpdate:Boolean;
    private attribute documentListener:javax.swing.event.DocumentListener;
    private attribute jtextarea: JTextArea = new JTextArea();
    public attribute editable: Boolean = true on replace {
        jtextarea.setEditable(editable);
    };
    public attribute rows: Number on replace {
        jtextarea.setRows(rows.intValue());
    };
    public attribute columns: Number on replace {
        jtextarea.setColumns(columns.intValue());
    };
    public attribute lineWrap: Boolean on replace {
        jtextarea.setLineWrap(lineWrap);
    };
    public attribute tabSize: Number on replace {
        if(jtextarea <> null) {
            jtextarea.setTabSize(tabSize.intValue());
        }
    };
    public attribute wrapStyleWord: Boolean on replace {
        jtextarea.setWrapStyleWord(wrapStyleWord);
    };
    public attribute text:String on replace {
        if (not inUpdate) {
            jtextarea.setText(text);
            jtextarea.select(0, 0);
            jtextarea.revalidate();
        }
    };
    public attribute caretDot: Integer;
    public attribute caretMark: Integer;
    public attribute lineCount: Number;
    public function modelToView(offset:Integer): Rectangle{
        return jtextarea.modelToView(offset);
    }
    public function viewToModel(p:Point): Number {
        return jtextarea.viewToModel(p);
    }
    public function getLineOfOffset(offset:Integer): Integer{
        return jtextarea.getLineOfOffset(offset);
    }
    public function getLineStartOffset(line:Integer): Integer{
        return jtextarea.getLineStartOffset(line);
    }
    public function replaceRange(replacement:String, start:Number, end:Number){
        jtextarea.replaceRange(replacement, start.intValue(), end.intValue());
        jtextarea.revalidate();
    }
    public function setCaretPosition(pos:Number) {
        if(jtextarea <> null) {
            jtextarea.setCaretPosition(pos.intValue());
        }
    }
    
    public attribute selectionStart:Integer = 0 on replace {
        if (inSelectionUpdate == false and jtextarea <> null) {
            jtextarea.setSelectionStart(selectionStart);
        }
    };
    public attribute selectionEnd:Integer = 0 on replace {
        if (inSelectionUpdate == false and jtextarea <> null) {
            jtextarea.setSelectionEnd(selectionEnd);
        }
    };
    public function selectAll() {
        inSelectionUpdate = true;
        jtextarea.selectAll();
        selectionStart = jtextarea.getSelectionStart();
        selectionEnd = jtextarea.getSelectionEnd();
        inSelectionUpdate = false;
    }
    private attribute inSelectionUpdate:Boolean = false;

    public function createView(): javax.swing.JComponent {
        if (editable) {
            jtextarea.select(0,0);
        }
        documentListener = javax.swing.event.DocumentListener {
            public function insertUpdate(e:javax.swing.event.DocumentEvent):Void {
                inUpdate = true;
                text = jtextarea.getText();
                //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                          public function run():Void {
                               lineCount = jtextarea.getLineCount();
                          }
                });
                inUpdate = false;
            }
            public function removeUpdate(e:javax.swing.event.DocumentEvent):Void {
                inUpdate = true;
                text = jtextarea.getText();
                //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                          public function run():Void {
                               lineCount = jtextarea.getLineCount();
                          }
                });
                inUpdate = false;
            }
            public function changedUpdate(e:javax.swing.event.DocumentEvent):Void {
                inUpdate = true;
                text = jtextarea.getText();
                //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                          public function run():Void {
                              lineCount = jtextarea.getLineCount();
                          }
                });
                inUpdate = false;
            }
        };
        jtextarea.addCaretListener(CaretListener {
                    public function caretUpdate(e:CaretEvent):Void {
                        inSelectionUpdate = true;
                        selectionStart = jtextarea.getSelectionStart();
                        selectionEnd = jtextarea.getSelectionEnd();
                        inSelectionUpdate = false;
                        caretDot = e.getDot();
                        caretMark = e.getMark();
                    }
            });
        jtextarea.getDocument().addDocumentListener(documentListener);
        return jtextarea;
    }


}



