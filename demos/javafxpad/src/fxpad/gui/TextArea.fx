/*
 * TextArea.fx
 *
 * Created on Apr 18, 2008, 12:12:50 PM
 */

package fxpad.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.InputVerifier;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javafx.ext.swing.*;
import javafx.scene.paint.*;

// PENDING_DOC_REVIEW
/**
 * {@code TextArea} is a component that allows the editing 
 * of a single line of text.
 */
public class TextArea extends Component {

    attribute ignoreJComponentChangeXX: Boolean = false;

    // PENDING_DOC_REVIEW
    /**
     * Represents the text contained in this {@code TextArea}.
     */
    public attribute text: String on replace {
        if (not getJTextArea().getText().equals(text)) {
            doAndIgnoreJComponentChangeXX(function() {
                getJTextArea().setText(text);
            });
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Represents the number of columns in this {@code TextArea}.
     */
    public attribute columns: Integer = getJTextArea().getColumns() on replace {
        doAndIgnoreJComponentChangeXX(function() {
            getJTextArea().setColumns(columns);
        });
    }
    // PENDING_DOC_REVIEW
    /**
     * Represents the number of rows in this {@code TextArea}.
     */
    public attribute rows: Integer = getJTextArea().getRows() on replace {
        doAndIgnoreJComponentChangeXX(function() {
            getJTextArea().setRows(rows);
        });
    }    
    
    public attribute lineWrap: Boolean = getJTextArea().getLineWrap() on replace {
        doAndIgnoreJComponentChangeXX(function() {
            getJTextArea().setLineWrap(lineWrap);
        });
    };
    public attribute tabSize: Number = getJTextArea().getTabSize()  on replace {
        doAndIgnoreJComponentChangeXX(function() {
            getJTextArea().setTabSize(tabSize.intValue());
        });
    };
    public attribute wrapStyleWord: Boolean = getJTextArea().getWrapStyleWord()  on replace {
        doAndIgnoreJComponentChangeXX(function() {
            getJTextArea().setWrapStyleWord(wrapStyleWord);
        });
    }    

    // PENDING_DOC_REVIEW
    /**
     * A boolean attribute indicating whether this {@code TextArea}
     * is editable or not.
     */
    public attribute editable: Boolean = getJTextArea().isEditable() on replace {
        doAndIgnoreJComponentChangeXX(function() {
            getJTextArea().setEditable(editable);
        });
    }


   // PENDING_DOC_REVIEW
   /**
    * Represents the background of the {@code TextArea}.
    */
    public attribute background: Paint = Color.fromAWTColor(getJTextArea().getBackground()) on replace {
        getJTextAreaImpl().setBackgroundPaint(background.getAWTPaint());
    }

   // PENDING_DOC_REVIEW
   /**
    * A boolean attribute indicating whether this {@code TextArea}
    * has a border or not.
    */
    public attribute borderless: Boolean = false on replace {
        getJTextAreaImpl().setBorderless(borderless);
    }

   // PENDING_DOC_REVIEW
   /**
    * A boolean attribute indicating whether this {@code TextArea}
    * becomes selected when getting the focus or not.
    */
    public attribute selectOnFocus: Boolean = true;


   // PENDING_DOC_REVIEW
   /**
    * Represents the verify function for the {@code TextArea}.
    */
    public attribute verify: function(newValue: String): Boolean;
    
    public function modelToView(offset:Integer): java.awt.Rectangle{
        return getJTextArea().modelToView(offset);
    }
    public function viewToModel(p:java.awt.Point): Number {
        return getJTextArea().viewToModel(p);
    }
    public function getLineOfOffset(offset:Integer): Integer{
        return getJTextArea().getLineOfOffset(offset);
    }
    public function getLineStartOffset(line:Integer): Integer{
        return getJTextArea().getLineStartOffset(line);
    }
    public function replaceRange(replacement:String, start:Number, end:Number){
        getJTextArea().replaceRange(replacement, start.intValue(), end.intValue());
        getJTextArea().revalidate();
    }
    public function setCaretPosition(pos:Number) {
        getJTextArea().setCaretPosition(pos.intValue());
    }
    
    attribute selectionStart: Integer = getJTextArea().getSelectionStart();
    attribute selectionEnd: Integer = getJTextArea().getSelectionEnd();

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
        getJTextArea().select(start, end);
    }

   // PENDING_DOC_REVIEW
   /**
    * Selects all the text in the {@code TextArea}.
    */
    public function selectAll(): Void {
        getJTextArea().selectAll();
    }

    postinit {
        var jTextArea = getJTextArea();

        jTextArea.addPropertyChangeListener(PropertyChangeListener {
            override function propertyChange(e: PropertyChangeEvent): Void {
                if (ignoreJComponentChangeXX) {
                    return;
                }

                var propName = e.getPropertyName();
                if ("editable".equals(propName)) {
                    editable = jTextArea.isEditable();
                } else if ("columns".equals(propName)) {
                    columns = jTextArea.getColumns();
                } else if ("rows".equals(propName)) {
                    rows = jTextArea.getRows();  
                } else if ("lineWrap".equals(propName)) {
                    lineWrap = jTextArea.getLineWrap();
                } else if ("tabSize".equals(propName)) {
                    tabSize = jTextArea.getTabSize();
                } else if ("wrapStyleWord".equals(propName)) {
                    wrapStyleWord   = jTextArea.getWrapStyleWord();                 
                } else if ("backgroundPaint".equals(propName)) {
                    var bg = getJTextAreaImpl().getBackgroundPaint();
                    if (bg == null or bg instanceof java.awt.Color) {
                        var col = Color.fromAWTColor(bg as java.awt.Color);
                        if (background != col) {
                            background = col;
                        }
                    }
                }
            }
        });

        jTextArea.getDocument().addDocumentListener(DocumentListener {
            function ut(): Void {
                if (ignoreJComponentChangeXX) {
                    return;
                }

                text = jTextArea.getText();
            }

            override function insertUpdate(e: DocumentEvent): Void { ut(); }
            override function removeUpdate(e: DocumentEvent): Void { ut(); }
            override function changedUpdate(e: DocumentEvent): Void { ut(); }
        });

        jTextArea.addCaretListener(CaretListener {
            override function caretUpdate(e: CaretEvent): Void {
                selectionStart = jTextArea.getSelectionStart();
                selectionEnd = jTextArea.getSelectionEnd();
            }
        });

        jTextArea.addFocusListener(FocusListener {
            override function focusLost(e: FocusEvent): Void {}
            override function focusGained(e: FocusEvent): Void {
                 if (selectOnFocus) {
                     selectAll();
                 }
            }
        });

        // need a copy with a different name since "verify" clashes with the
        // InputVerifier.verify method name
        var v = bind verify;
        jTextArea.setInputVerifier(InputVerifier {
            override function verify(c: JComponent): Boolean {
                if (v != null) v(text) else true;
            }
        });
    }

    /* final */ function getJTextAreaImpl(): JTextAreaImpl {
        getJTextArea() as JTextAreaImpl;
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@code JTextArea} delegate for this component.
     */
    public /* final */ function getJTextArea(): JTextArea {
        getJComponent() as JTextArea;
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates the {@code JComponent} delegate for this component.
     */
    /* final */ override function createJComponent(): JComponent {
        new JTextAreaImpl();
    }

    function doAndIgnoreJComponentChangeXX(func: function(): Void) {
        try {
            ignoreJComponentChangeXX = true;
            func();
        } finally {
            ignoreJComponentChangeXX = false;
        }
    }

}
