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

import javafx.ui.Widget;
import javafx.ui.HorizontalAlignment;
import java.lang.Object;
import java.awt.Component;
import java.util.List;
import java.lang.StringBuffer;
import java.awt.MouseInfo;
import java.lang.System;
import java.awt.Point;


public class PasswordField extends Widget {
    protected attribute inListener: Boolean;
    // TODO MARK AS FINAL
    private attribute textField: javax.swing.JPasswordField;

    public attribute value: String on replace {
        if (not inListener) {
            textField.setText(value);
        }
    };
    public attribute onChange: function(newValue:String);
    public attribute action: function();
    public attribute columns: Number on replace {
            textField.setColumns(columns.intValue());
    };
    public attribute horizontalAlignment:HorizontalAlignment on replace {
        textField.setHorizontalAlignment(horizontalAlignment.id.intValue());
    };
    
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
            if(textField <> null)
                UIElement.context.setDropMode(dropMode.id, textField);
        }
    };
    /**
     * Indicates if Drag and Drop is enabled.
     */
    public attribute enableDND: Boolean = true on replace {
        textField.setDragEnabled(enableDND);
    };
    

    /**
     * Optional handler called when the user drops an object
     */
    public attribute onDrop: function(e:DropEvent):Void = function (e:DropEvent):Void {
        var values = e.transferData;
        for(val in values) {
           if(val instanceof java.awt.Color) {
                if(colorDropChangesForeground) {
                    textField.setForeground(val as java.awt.Color);
                }else {
                    textField.setBackground(val as java.awt.Color);
                }
            }else {
                var valStr:String;
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
                    var insertPos = textField.viewToModel(dropPoint);
                    if(insertPos < 0) 
                        insertPos = textField.getCaretPosition();                    
                    if(insertPos == 0) {
                        value = "{valStr}{value}";
                    }else if (insertPos == value.length()) {
                        value = "{value}{valStr}";
                    }else {
                        var sb = new StringBuffer(value);
                        sb.<<insert>>(insertPos, valStr);
                        value = "{sb}";
                    }
                }else { //if(dropMode == DropMode.USE_SELECTION) {
                    textField.replaceSelection(valStr);
                    value = textField.getText();
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
    public attribute canAcceptDrop: function(e:DropEvent):Boolean = function(e:DropEvent):Boolean {
        return enableDND;
    };  
    
    
    private bound function acceptDrop(value:Object):Boolean{
        if (this.canAcceptDrop <> null) {
            var info = MouseInfo.getPointerInfo();
            var location = textField.getLocationOnScreen();
            var p = info.getLocation();
            var x = p.getX() - location.getX();
            var y = p.getY() - location.getY();
            var e = DropEvent {
                x: x
                y: y
                transferData: value
                dropMode: dropMode
            };
            return (this.canAcceptDrop)(e);
        } else {
            return onDrop <> null;
        }
    }
    
    private function setDropValue(value:Object):Void {
        if (onDrop <> null) {
            var info = MouseInfo.getPointerInfo();
            var location = textField.getLocationOnScreen();
            var p = info.getLocation();
            var x = p.getX() - location.getX();
            var y = p.getY() - location.getY();
            p.setLocation(x, y);
            var e = DropEvent {
                x: p.getX()
                y: p.getY()
                transferData: value
                dropMode: dropMode
            };
            onDrop(e);
        }
    }    
    /***************************************************************
     * END Drag N Drop
     **************************************************************/      
    
    /** Sets the echo character for this PasswordField, expressed as an integer value representing a Unicode character. The default value is U+2022 (\u2022), representing a bullet character. */
    public attribute echoChar: Integer = 0 on replace {
        var string = "{%c echoChar}";
        var symbol = string.charAt(0);
        textField.setEchoChar(symbol);
    };
    
    public attribute selectOnFocus:Boolean;
    public attribute selectionStart:Integer = 0 on replace {
        if (inSelectionUpdate == false) {
            textField.setSelectionStart(selectionStart);
        }
    };
    public attribute selectionEnd:Integer = 0 on replace {
        if (inSelectionUpdate == false) {
            textField.setSelectionEnd(selectionEnd);
        }
    };
    public function selectAll() {
        inSelectionUpdate = true;
        textField.selectAll();
        selectionStart = textField.getSelectionStart();
        selectionEnd = textField.getSelectionEnd();
        inSelectionUpdate = false;
    }
    private attribute inSelectionUpdate:Boolean = false;

    public function createComponent():javax.swing.JComponent{
        if(textField == null ){
            selectOnFocus = true;
            textField = new javax.swing.JPasswordField();    
            echoChar = 0x2022; // set echoChar after we created the textfield

            textField.setFont(javax.swing.UIManager.getFont("TextField.font"));

            textField.getDocument().addDocumentListener(javax.swing.event.DocumentListener {
                                    public function insertUpdate(e:javax.swing.event.DocumentEvent):Void {
                                        inListener = true;
                                        value = textField.getText();
                                        inListener = false;
                                        if(onChange <> null) {
                                            onChange(value);
                                        }
                                    }
                                    public function removeUpdate(e:javax.swing.event.DocumentEvent):Void {
                                        inListener = true;
                                        value = textField.getText();
                                        if(onChange <> null) {
                                            onChange(value);
                                        }
                                        inListener = false;
                                    }
                                    public function changedUpdate(e:javax.swing.event.DocumentEvent):Void {
                                        inListener = true;
                                        value = textField.getText();
                                        inListener = false;
                                        if(onChange <> null) {
                                            onChange(value);
                                        }
                                    }
            });

            textField.addActionListener(java.awt.event.ActionListener{
                                    public function actionPerformed(e:java.awt.event.ActionEvent):Void {
                                        value = textField.getText();
                                        if (action <> null) {
                                              (action)();
                                        } else {
                                              var root = textField.getRootPane();      
                                              var but = root.getDefaultButton();
                                              if (but <> null) {
                                                  if(onChange <> null) {
                                                    onChange(textField.getText());
                                                  }
                                                  but.doClick(0);
                                              }
                                        }
                                    }       
            });

            textField.addCaretListener(javax.swing.event.CaretListener {
                                    public function caretUpdate(e:javax.swing.event.CaretEvent):Void {
                                        inSelectionUpdate = true;
                                        selectionStart = textField.getSelectionStart();
                                        selectionEnd = textField.getSelectionEnd();
                                        inSelectionUpdate = false;
                                    }
            });

            textField.addFocusListener(java.awt.event.FocusAdapter {
                                    public function focusGained(e:java.awt.event.FocusEvent):Void {
                                        if (selectOnFocus == true) {
                                            selectAll();
                                        }
                                    }
            } as java.awt.event.FocusListener);
        UIElement.context.addTransferHandler(textField,
            null,//value.getClass(),
            com.sun.javafx.api.ui.ValueGetter {
                public function get():Object {
                    // Can't export a Password field.
                    return null;
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
                public function accept(value:Object):Boolean {
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
                public function getComponent(value:Object):Component {
                    var label = TextField {
                        value: "****"
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
            
        }
        return textField;
    }
}

