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
import javax.swing.TransferHandler;
import java.lang.Object;
import java.awt.Component;
import java.util.List;
import java.lang.StringBuffer;
import java.awt.MouseInfo;
import java.lang.System;
import java.awt.Point;

public class TextArea extends ScrollableWidget {
    private attribute caretColor: Color = bind foreground on replace {
        jtextarea.setCaretColor(caretColor.getColor());
    };
    private attribute inUpdate:Boolean;
    private attribute documentListener:javax.swing.event.DocumentListener;
    
    // TODO MARK AS FINAL
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
    /***************************************************************
     * Drag N Drop
     **************************************************************/   
    /**
     * inidicates if a color drop changes the foreground color of the
     * selected text, if false, it changes the background color.
     * If not text is selected the entire text is changed.
     */
    public attribute colorDropChangesForeground:Boolean = true;
    
    /**
     * Indicates the drop mode which specifies which 
     * default drop action will occur. Only valid values for 
     * TextComponents are USE_SELECTION and INSERT
     */
    public attribute dropMode:DropMode = DropMode.USE_SELECTION on replace {
        if(dropMode <> DropMode.USE_SELECTION and dropMode <> DropMode.INSERT) {
            System.out.println("Illegal drop mode for text component,");
            System.out.println("only USE_SELECTION and INSERT are allowed.");
            System.out.println("Reverting to USE_SELECTION");
            dropMode = DropMode.USE_SELECTION;
        }else {
            if(jtextarea <> null)
                UIElement.context.setDropMode(dropMode.id, jtextarea);
        }
    };
    
    /**
     * Indicates if Drag and Drop is enabled.
     */
    public attribute enableDND: Boolean = true on replace {
        jtextarea.setDragEnabled(enableDND);
    };
    

    /**
     * Optional handler called when the user drops an object
     */
    public attribute onDrop: function(e:DropEvent):Void = function (e:DropEvent):Void {
        var values = e.transferData;
        for(val in values) {
            if(val instanceof java.awt.Color) {
                if(colorDropChangesForeground) {
                    jtextarea.setForeground(val as java.awt.Color);
                }else {
                    jtextarea.setBackground(val as java.awt.Color);
                }
            }else {
                var valStr:String = "";
                if(val instanceof List) {
                    var sb = new StringBuffer();
                    var iter = (val as List).iterator();
                    var firstTime = true;
                    while(iter.hasNext()) {
                        if(not firstTime) {
                            sb.append(", ");
                        }
                        sb.append("{iter.next()}");
                        firstTime = false;
                    }
                    valStr = "{sb}";
                }else if (val instanceof javax.swing.tree.TreePath) {
                    var comp = (val as javax.swing.tree.TreePath).getLastPathComponent();
                    valStr = "{comp}";                    
                }else {
                    valStr = "{val}";
                }
                if(dropMode == DropMode.INSERT) {
                    var dropPoint = new Point(e.x, e.y);
                    var insertPos = jtextarea.viewToModel(dropPoint);
                    if(insertPos < 0) 
                        insertPos = jtextarea.getCaretPosition();
                    jtextarea.<<insert>>(valStr, insertPos);
                    text = jtextarea.getText();
                }else { //if(dropMode == DropMode.USE_SELECTION) {
                    jtextarea.replaceSelection(valStr);
                    text = jtextarea.getText();
                }
            }
        }
    };
    /**
     * Optional filter for the types of objects that may be dropped onto this textfield.
     * 
     */
    public attribute dropType: java.lang.Class;
    /**
     * <code>attribute acceptDrop: function(value): Boolean</code><br></br>
     * Optional handler called when the user drops an object onto this textfield.
     * If it returns false, the drop is rejected. 
     */
    public attribute canAcceptDrop: function(e:DropEvent):Boolean = function (e:DropEvent):Boolean {
        return enableDND;
    };
    
    
    private bound function acceptDrop(value:Object):Boolean{
        if (this.canAcceptDrop <> null) {
            var info = MouseInfo.getPointerInfo();
            var location = jtextarea.getLocationOnScreen();
            var p = info.getLocation();
            var e = DropEvent {
                x: p.getX() - location.getX();
                y: p.getY() - location.getY();
                transferData: value
            };
            (this.canAcceptDrop)(e);
        } else {
            onDrop <> null
        }
    }
    
    private function setDropValue(value:Object):Void {
        if (onDrop <> null) {
            var info = MouseInfo.getPointerInfo();
            var location = jtextarea.getLocationOnScreen();
            var p = info.getLocation();
            var x = p.getX() - location.getX();
            var y = p.getY() - location.getY();
            p.setLocation(x, y);
            var e = DropEvent {
                x: p.getX()
                y: p.getY()
                transferData: value
            };
            onDrop(e);
        }
    }    
    /***************************************************************
     * END Drag N Drop
     **************************************************************/    
    
    
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
        UIElement.context.addTransferHandler(jtextarea,
            null,//value.getClass(),
            com.sun.javafx.api.ui.ValueGetter {
                public function get():Object {
                    return jtextarea.getSelectedText();
                }
            },
            com.sun.javafx.api.ui.ValueSetter {
                public function set(val:Object):Void {
                    if(onDrop <> null) {
                        setDropValue(val);
                    }  
                }
            },
            com.sun.javafx.api.ui.ValueAcceptor {
                public bound function accept(value:Object):Boolean {
                    return if(onDrop <> null and enableDND) {
                        acceptDrop(value);
                    } else {
                        enableDND;
                    };
                }
                public function dragEnter(value:Object):Void {
                }
                public function dragExit(value:Object):Void {
                }
            },
            com.sun.javafx.api.ui.VisualRepresentation {
                public bound function getComponent(value:Object):Component {
                    var label = TextField {
                        value: "{jtextarea.getSelectedText()}"
                        border: border
                        foreground: foreground
                        background: background
                        columns: columns
                    };
                    return label.getComponent();
                }
                public function getIcon(list:Object):javax.swing.Icon {
                    return null;
                }
            });
        
        return jtextarea;
    }


}



