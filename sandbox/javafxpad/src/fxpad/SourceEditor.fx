/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package fxpad;

import javafx.ui.*;
import javafx.ui.canvas.*;

import com.sun.javafx.api.ui.fxkit.FXTextArea;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;

/**
 * @author jclarke
 */

public class SourceEditor extends ScrollableWidget {
    private attribute jtextarea: FXTextArea = new FXTextArea();
    private attribute inUpdate:Boolean;
    private attribute edit:UndoableEdit;
    private attribute undoManager: UndoManager;
    private attribute documentListener:DocumentListener = DocumentListener {
        public function insertUpdate(e:DocumentEvent):Void {
            if(not inUpdate) {
                inUpdate = true;
                text = jtextarea.getText();
                inUpdate = false;
                lineCount = jtextarea.getLineCount();
            }
        }
        public function removeUpdate(e:DocumentEvent):Void {
            if(not inUpdate) {
                inUpdate = true;
                text = jtextarea.getText();
                inUpdate = false;
                lineCount = jtextarea.getLineCount();
            }
        }
        public function changedUpdate(e:DocumentEvent):Void {
            if(not inUpdate) {
                inUpdate = true;
                text = jtextarea.getText();
                inUpdate = false;
                lineCount = jtextarea.getLineCount();
            }
        }
    };
    public attribute lineCount: Number;
    
     public attribute editable: Boolean = jtextarea.isEditable() on replace {
         jtextarea.setEditable(editable);
     }
    
    public attribute editorKit: javax.swing.text.EditorKit = jtextarea.getEditorKit() on replace {
        jtextarea.setEditorKit(editorKit);
    }
    public attribute selectedTextColor: Color on replace {
        jtextarea.setSelectedTextColor(selectedTextColor.getColor());
    }
    public attribute selectionColor: Color on replace {
        jtextarea.setSelectionColor(selectionColor.getColor());
    };
    public attribute caretColor: Color on replace {
        jtextarea.setCaretColor(caretColor.getColor());
    }
    public attribute tabSize: Number = jtextarea.getTabSize() on replace {
        jtextarea.setTabSize(tabSize);
    }
    public attribute lineWrap: Boolean = jtextarea.getLineWrap() on replace {
        jtextarea.setLineWrap(lineWrap);
    }
    public attribute text:String on replace {
        if (not inUpdate) {
            inUpdate = true;
            jtextarea.setText(text);
            lineCount = jtextarea.getLineCount();
            undoManager.discardAllEdits();
            jtextarea.select(0, 0);
            jtextarea.revalidate();
            jtextarea.repaint();
            inUpdate = false;
        }

    }
    
    public function createView(): javax.swing.JComponent {
        if(editable) {
            jtextarea.select(0,0);
        }
        jtextarea.getDocument().addUndoableEditListener(UndoableEditListener {
            function undoableEditHappened(e:UndoableEditEvent):Void {
                if (edit == null) {
                    edit = e.getEdit();
                } else if (not edit.addEdit(e.getEdit())) {
                    var compoundEdit = new CompoundEdit();
                    compoundEdit.addEdit(edit);
                    compoundEdit.addEdit(e.getEdit());
                    edit = compoundEdit;
                }
                if (edit instanceof CompoundEdit) {
                    var compoundEdit = edit as CompoundEdit;
                    if (compoundEdit.isInProgress()) {
                        compoundEdit.end();
                    }
                }
                if (edit <> null) {
                    undoManager.addEdit(edit);
                    edit = null;
                }
            }
        });
        var z = javax.swing.KeyStroke.getKeyStroke(KeyStroke.Z.id, KeyModifier.COMMAND.id);
        var y = javax.swing.KeyStroke.getKeyStroke(KeyStroke.Y.id, KeyModifier.COMMAND.id);
        jtextarea.getInputMap().put(z, "undo");
        jtextarea.getInputMap().put(y, "redo");
        jtextarea.getActionMap().put("undo", javax.swing.AbstractAction {
                public function isEnabled():Boolean {
                    return undoManager.canUndo();
                }
                function actionPerformed(e):Void {
                    undoManager.undo();
                }
            });
        jtextarea.getActionMap().put("redo", javax.swing.AbstractAction {
                public function isEnabled():Boolean {
                    return undoManager.canRedo();
                }
                function actionPerformed(e):Void {
                    undoManager.redo();
                }
            });
        jtextarea.getDocument().addDocumentListener(documentListener);
        jtextarea;
    }

}