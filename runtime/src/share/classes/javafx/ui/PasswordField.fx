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


public class PasswordField extends Widget {
    protected attribute inListener: Boolean;
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
        }
        return textField;
    }
}

