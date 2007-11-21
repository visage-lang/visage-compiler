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

import java.awt.event.ActionEvent;

public class TextField extends Widget {
    private attribute propertyChangeListener:java.beans.PropertyChangeListener;
    private attribute propertyChangeRunnable:java.lang.Runnable;
    private attribute textField: javax.swing.JTextField;
    private attribute verifier: com.sun.javafx.api.ui.XInputVerifierImpl;


    public attribute value: String on replace  {
        if (value <> textField.getText()) {
            textField.setText(value);
        }
    };
    public attribute text: String;
    public attribute onChange: function(newValue:String) on replace (old){
        if (old == null and onChange <> null) {
            textField.addPropertyChangeListener(propertyChangeListener);
        } else if (old <> null) {
            textField.removePropertyChangeListener(propertyChangeListener);
        }
    };
    public attribute action: function();
    public attribute columns: Number on replace {
        textField.setColumns(columns.intValue());
    };
    public attribute editable: Boolean on replace {
        textField.setEditable(editable);
    };
    public attribute enableDND: Boolean on replace {
        textField.setDragEnabled(enableDND);
    };
    public attribute verify: function(newValue:String):Boolean on replace  {
        if (verifier == null) {
            verifier = new com.sun.javafx.api.ui.XInputVerifierImpl();
            verifier.addInputVerifier(com.sun.javafx.api.ui.XInputVerifier {
                                          public function verify(comp:javax.swing.JComponent):Boolean {
                                            //TODO OUTER THIS
                                            //  return (this.verify)(textField.getText());
                                            return true;
                                          }
                                      });
            textField.setInputVerifier(verifier);
        }
    };
    public attribute horizontalAlignment:HorizontalAlignment on replace {
        textField.setHorizontalAlignment(horizontalAlignment.id.intValue());
    };
    public attribute margin: Insets on replace {
        textField.setMargin(margin.awtinsets);
    };
    
    public attribute selectOnFocus:Boolean;
    public attribute selectionStart:Integer = 0 on replace {
        if (inSelectionUpdate == false) {
            textField.setSelectionStart(selectionStart);
        }
    };
    public attribute selectionEnd:Integer = 0 on replace  {
        if (inSelectionUpdate == false) {
            textField.setSelectionEnd(selectionEnd);
        }
    };
    public function selectAll(): Void {
        inSelectionUpdate = true;
        textField.selectAll();
        selectionStart = textField.getSelectionStart();
        selectionEnd = textField.getSelectionEnd();
        inSelectionUpdate = false;
    }
    private attribute inSelectionUpdate:Boolean = false;

    public function createComponent():javax.swing.JComponent {
        return textField;
    }
    init {
        editable = true;
        enableDND = true;
        selectOnFocus = true;
        text = value;
        textField = new javax.swing.JFormattedTextField();

        textField.setFont(javax.swing.UIManager.getFont("TextField.font"));
        var selectionColor = javax.swing.UIManager.getColor("TextField.selectionColor");
        if (selectionColor == null) {
            var temp = new javax.swing.JTextField();
            selectionColor = temp.getSelectionColor();
            javax.swing.UIManager.put("TextField.selectionColor", selectionColor);
        }
        textField.setSelectionColor(selectionColor);
        var selectedTextColor = javax.swing.UIManager.getColor("TextField.selectedTextColor");
        if (selectedTextColor == null) {
            var temp = new javax.swing.JTextField();
            selectedTextColor = temp.getSelectedTextColor();
            javax.swing.UIManager.put("TextField.selectedTextColor", selectedTextColor);
        }
        textField.setSelectedTextColor(selectedTextColor);

        
        propertyChangeRunnable = java.lang.Runnable {
                                      public function run():Void {
                                          value = textField.getText();
                                          if(onChange <> null)
                                            onChange(textField.getText());
                                      }
                            };
        propertyChangeListener = java.beans.PropertyChangeListener {
                public function propertyChange(e:java.beans.PropertyChangeEvent):Void {
                    if (e.getPropertyName().equals("value")) {
                            //TODO DO LATER - this is a work around until a more permanent solution is provided
                            javax.swing.SwingUtilities.invokeLater(propertyChangeRunnable);                          
                    }
                }
        };
        textField.addPropertyChangeListener(propertyChangeListener);

        textField.addActionListener(java.awt.event.ActionListener {
                                        public function actionPerformed(e:ActionEvent):Void {
                                            value = textField.getText();
                                            if(onChange <> null) {
                                                onChange(textField.getText());
                                            }
                                            if (action <> null) {
                                                (action)();
                                            } else {
                                                  var root = textField.getRootPane();      
                                                  var but = root.getDefaultButton();
                                                  if (but <> null) {
                                                      but.doClick(0);
                                                  }
                                            }
                                        }       
                                    });

        if (true) {      // ????
            //TODO Transfer Handler
            /*********************************************
                    UIElement.context.addTransferHandler(textField,
                                                            :String,
                                                            new com.sun.javafx.api.ui.ValueGetter() {
                                                                function get() {
                                                                    return value;
                                                                }
                                                            },
                                                            new com.sun.javafx.api.ui.ValueSetter() {
                                                                function set(value) {
                                                                    value = "{value}";

                                                                }
                                                            },
                                                            new com.sun.javafx.api.ui.ValueAcceptor() {
                                                                function accept(value) {
                                                                    return enableDND;
                                                                }
                                                            },
                                                            new com.sun.javafx.api.ui.VisualRepresentation() {
                                                                function getComponent(value) {
                                                                    var label = TextField {
                                                                        value: value
                                                                        border: border
                                                                        foreground: foreground
                                                                        background: background
                                                                        columns: columns
                                                                        value: value
                                                                    };
                                                                    return label.getComponent();
                                                                }
                                                            });
         ******************/
        }

        var documentListener = javax.swing.event.DocumentListener {
                                        public function insertUpdate(e:javax.swing.event.DocumentEvent):Void {
                                            text = textField.getText();
                                        }
                                        public function removeUpdate(e:javax.swing.event.DocumentEvent):Void {
                                            text = textField.getText();
                                        }
                                        public function changedUpdate(e:javax.swing.event.DocumentEvent):Void {
                                            text = textField.getText();
                                        }
        };
        textField.getDocument().addDocumentListener(documentListener);

        textField.addCaretListener(javax.swing.event.CaretListener {
                                    public function caretUpdate(e:javax.swing.event.CaretEvent):Void {
                                        inSelectionUpdate = true;
                                        selectionStart = textField.getSelectionStart();
                                        selectionEnd = textField.getSelectionEnd();
                                        inSelectionUpdate = false;
                                    }
        });

        textField.addFocusListener(java.awt.event.FocusListener {
                                    public function focusGained(e:java.awt.event.FocusEvent):Void {
                                        if (selectOnFocus == true) {
                                            selectAll();
                                        }
                                    }
                                    public function focusLost(e:java.awt.event.FocusEvent):Void {
                                        //empty
                                    }
        });
    }

}



