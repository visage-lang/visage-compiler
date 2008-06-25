/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
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

package javafx.ext.swing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.InputVerifier;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javafx.scene.HorizontalAlignment;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import com.sun.javafx.scene.BackgroundSupport;
import com.sun.javafx.scene.Util;

// PENDING_DOC_REVIEW
/**
 * {@code TextField} is a component that allows the editing 
 * of a single line of text.
 */
public class TextField extends Component {

    // PENDING_DOC_REVIEW
    /**
     * Represents the text contained in this {@code TextField}.
     */
    public attribute text: String on replace {
        if (not getJTextField().getText().equals(text)) {
            doAndIgnoreJComponentChange(function() {
                getJTextField().setText(text);
            });
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Represents the number of columns in this {@code TextField}.
     */
    public attribute columns: Integer = getJTextField().getColumns() on replace {
        doAndIgnoreJComponentChange(function() {
            getJTextField().setColumns(columns);
        });
    }

    // PENDING_DOC_REVIEW
    /**
     * A boolean attribute indicating whether this {@code TextField}
     * is editable or not.
     */
    public attribute editable: Boolean = getJTextField().isEditable() on replace {
        doAndIgnoreJComponentChange(function() {
            getJTextField().setEditable(editable);
        });
    }

   // PENDING_DOC_REVIEW
   /**
    * Represents the horizontal alignment of the {@code TextField}.
    */
    public attribute horizontalAlignment: HorizontalAlignment =
        Util.SwingConstant_To_HA(getJTextField().getHorizontalAlignment())
        on replace {
            doAndIgnoreJComponentChange(function() {
                getJTextField().setHorizontalAlignment(Util.HA_To_SwingConstant(horizontalAlignment));
            });
        }

   // PENDING_DOC_REVIEW
   /**
    * Represents the background of the {@code TextField}.
    */
    public attribute background: Paint = Color.fromAWTColor(getJTextField().getBackground()) on replace {
        getJTextFieldImpl().setBackgroundPaint(background.getAWTPaint());
    }

   // PENDING_DOC_REVIEW
   /**
    * A boolean attribute indicating whether this {@code TextField}
    * has a border or not.
    */
    public attribute borderless: Boolean = false on replace {
        getJTextFieldImpl().setBorderless(borderless);
    }

   // PENDING_DOC_REVIEW
   /**
    * A boolean attribute indicating whether this {@code TextField}
    * becomes selected when getting the focus or not.
    */
    public attribute selectOnFocus: Boolean = true;

   // PENDING_DOC_REVIEW
   /**
    * Represents the text field's action.
    */
    public attribute action: function(): Void;

   // PENDING_DOC_REVIEW
   /**
    * Represents the verify function for the {@code TextField}.
    */
    public attribute verify: function(newValue: String): Boolean;

    private attribute selectionStart: Integer = getJTextField().getSelectionStart();
    private attribute selectionEnd: Integer = getJTextField().getSelectionEnd();

   // PENDING_DOC_REVIEW
   /**
    * Returns the selected text's start position.
    */
    public bound function getSelectionStart(): Integer {
        selectionStart;
    }

   // PENDING_DOC_REVIEW
   /**
    * Returns the selected text's end position.
    */
    public bound function getSelectionEnd(): Integer {
        selectionEnd;
    }

   // PENDING_DOC_REVIEW
   /**
    * Selects the text between two indices.
    */
    public function select(start: Integer, end: Integer): Void {
        getJTextField().select(start, end);
    }

   // PENDING_DOC_REVIEW
   /**
    * Selects all the text in the {@code TextField}.
    */
    public function selectAll(): Void {
        getJTextField().selectAll();
    }

    postinit {
        var jTextField = getJTextField();

        jTextField.addPropertyChangeListener(PropertyChangeListener {
            public function propertyChange(e: PropertyChangeEvent): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                var propName = e.getPropertyName();
                if ("horizontalAlignment".equals(propName)) {
                    var value = jTextField.getHorizontalAlignment();
                    horizontalAlignment = Util.SwingConstant_To_HA(value);
                } else if ("editable".equals(propName)) {
                    editable = jTextField.isEditable();
                } else if ("columns".equals(propName)) {
                    columns = jTextField.getColumns();
                } else if (BackgroundSupport.BACKGROUND_PAINT_PROPERTY.equals(propName)) {
                    var bg = getJTextFieldImpl().getBackgroundPaint();
                    if (bg == null or bg instanceof java.awt.Color) {
                        background = Color.fromAWTColor(bg as java.awt.Color);
                    }
                }
            }
        });

        jTextField.getDocument().addDocumentListener(DocumentListener {
            private function ut(): Void {
                if (ignoreJComponentChange) {
                    return;
                }

                text = jTextField.getText();
            }

            public function insertUpdate(e: DocumentEvent): Void { ut(); }
            public function removeUpdate(e: DocumentEvent): Void { ut(); }
            public function changedUpdate(e: DocumentEvent): Void { ut(); }
        });

        jTextField.addCaretListener(CaretListener {
            public function caretUpdate(e: CaretEvent): Void {
                selectionStart = jTextField.getSelectionStart();
                selectionEnd = jTextField.getSelectionEnd();
            }
        });

        jTextField.addFocusListener(FocusListener {
            public function focusLost(e: FocusEvent): Void {}
            public function focusGained(e: FocusEvent): Void {
                 if (selectOnFocus) {
                     selectAll();
                 }
            }
        });

        jTextField.addActionListener(ActionListener {
            public function actionPerformed(e: ActionEvent): Void {
                if (action <> null) {
                    action();
                }
            }
        });

        // need a copy with a different name since "verify" clashes with the
        // InputVerifier.verify method name
        var v = bind verify;
        jTextField.setInputVerifier(InputVerifier {
            public function verify(c: JComponent): Boolean {
                if (v <> null) v(text) else true;
            }
        });
    }

    private /* final */ function getJTextFieldImpl(): JTextFieldImpl {
        getJTextField() as JTextFieldImpl;
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@code JTextField} delegate for this component.
     */
    public /* final */ function getJTextField(): JTextField {
        getJComponent() as JTextField;
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the {@code JComponent} delegate for this component.
     */
    protected /* final */ function createJComponent(): JComponent {
        new JTextFieldImpl();
    }

}
