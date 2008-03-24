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

import java.lang.System;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.awt.Rectangle;
import java.awt.Point;
import com.sun.javafx.api.ui.fxkit.FXTextArea;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

/**
 * @author jclarke
 */

public class SourceEditor extends ScrollableWidget {
    private attribute jtextarea: FXTextArea = new FXTextArea() on replace {
        jtextarea.setOpaque(false);
        jtextarea.setEditable(true);
        jtextarea.getCaret().setVisible( true );
    }
     public attribute editable: Boolean = true on replace {
         jtextarea.setEditable(editable);
         jtextarea.getCaret().setVisible( true );
     };    
    private attribute inUpdate:Boolean;
    private attribute edit:UndoableEdit;
    private attribute undoManager: UndoManager = new UndoManager();
    //TODO public attribute annotations: LineAnnotation[] on replace oldValues [lo..hi] = newValues {

    //}
    public  attribute highlight: Number[] on replace oldValues [lo..hi] = newValues {
        doHighlight();
    };
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
    public attribute caretDot: Integer;
    public attribute caretMark: Integer;
    
    public attribute rows: Number = jtextarea.getRows() on replace {
        jtextarea.setRows(rows);
    };
    public attribute columns: Number = jtextarea.getColumns() on replace {
        jtextarea.setColumns(columns);
    };

    

    
    public attribute editorKit: javax.swing.text.EditorKit = jtextarea.getEditorKit() on replace {
        jtextarea.setEditorKit(editorKit);
    };
    public attribute disabledTextColor: Color = Color.fromAWTColor(jtextarea.getDisabledTextColor()) on replace {
        jtextarea.setDisabledTextColor(disabledTextColor.getColor());
    };
    public attribute selectedTextColor: Color=  Color.fromAWTColor(jtextarea.getSelectedTextColor()) on replace {
        jtextarea.setSelectedTextColor(selectedTextColor.getColor());
    };
    public attribute selectionColor: Color =  Color.fromAWTColor(jtextarea.getSelectionColor()) on replace {
        jtextarea.setSelectionColor(selectionColor.getColor());
    };
    public attribute caretColor: Color  =  Color.fromAWTColor(jtextarea.getCaretColor()) on replace {
        jtextarea.setCaretColor(caretColor.getColor());
    };
    public attribute tabSize: Number = jtextarea.getTabSize() on replace {
        jtextarea.setTabSize(tabSize);
    };
    public attribute lineWrap: Boolean = jtextarea.getLineWrap() on replace {
        jtextarea.setLineWrap(lineWrap);
    };
    public attribute wrapStyleWord: Boolean = jtextarea.getWrapStyleWord() on replace {
        jtextarea.setWrapStyleWord(wrapStyleWord);
    };
    public attribute text:String on replace old {
        if (not inUpdate) {
            if(text <> jtextarea.getText()) {
                System.out.println("Changing text {text} <> {jtextarea.getText()}");
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

    }
    
    public function requestFocus():Void {
        getComponent().requestFocusInWindow();
    }
    
    public function createView(): javax.swing.JComponent {
        System.out.println("CreateView: {this}");
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
            
        caretDot = jtextarea.getCaret().getDot();
        caretMark = jtextarea.getCaret().getMark();
        jtextarea.addCaretListener(CaretListener {
                function caretUpdate(e:CaretEvent):Void {
                    caretDot = e.getDot();
                    caretMark = e.getMark();
                    System.out.println("caret position is {jtextarea.getCaretPosition()} Dot = {caretDot}, Mark = {caretMark}");
                }
        });            
        jtextarea.getDocument().addDocumentListener(documentListener);
        jtextarea;
    }
    
    private function doHighlight() {
        var highlighter = jtextarea.getHighlighter();
        highlighter.removeAllHighlights();
        var painter = new DefaultHighlightPainter(jtextarea.getSelectionColor());
        if (sizeof highlight % 2 == 0) {
          for (i in [0..<sizeof highlight step 2]) {
              var p0 = highlight[i];
              var p1 = highlight[i+1];
              highlighter.addHighlight(p0, p1, painter);
          }
        }
    }
    public function modelToView(offset:Integer): Rectangle {
        return jtextarea.modelToView(offset);
    }
    public function viewToModel(p:Point): Number {
         return jtextarea.viewToModel(p);
    }
    public function getLineOfOffset(offset:Integer): Integer {
         return jtextarea.getLineOfOffset(offset);
    }
    public function getLineStartOffset(line:Integer): Integer {
        return jtextarea.getLineStartOffset(line);
    }
    public function replaceRange(replacement:String, start:Number, end:Number) {
        jtextarea.replaceRange(replacement, start, end);
        jtextarea.revalidate();
    }
    public function setCaretPosition(pos:Number) {
        jtextarea.setCaretPosition(pos);
    }
    public function setSelection(startPos:Integer, endPos:Integer) {
        jtextarea.select(startPos, endPos);
    }
    public function selectLocation(startLine:Integer, startColumn:Integer, endLine:Integer, endColumn:Integer) {
        var off1 = jtextarea.getLineStartOffset(startLine-1);
        var off2 = jtextarea.getLineStartOffset(endLine-1);
        var startPos = off1 + startColumn-1;
        var endPos = off2 + endColumn-1;
        //System.out.println("Select {startPos}, {endPos}");
        jtextarea.select(startPos, endPos);
        jtextarea.getCaret().setSelectionVisible(true);
    }

}