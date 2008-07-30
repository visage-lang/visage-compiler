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
import javafx.scene.*;
import javafx.scene.paint.*;
import java.awt.Rectangle;
import java.awt.Point;
import javafx.input.*;
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
import javax.swing.event.ChangeListener;
import javax.swing.JComponent;

/**
 * @author jclarke
 */



public class SourceEditor extends ExtendedScrollableComponent {
    
     public attribute editable: Boolean = true on replace {
         getJTextArea().setEditable(editable);
         getJTextArea().getCaret().setVisible( true );
     };    
    private attribute inUpdate:Boolean;
    private attribute edit:UndoableEdit;
    private attribute undoManager: UndoManager = new UndoManager();
    public attribute annotations: LineAnnotation[] on replace oldValues [lo..hi] = newValues {
        for( i in [lo..hi] ) {
            getJTextArea().removeAnnotation(lo);
        }
        var ndx = lo;
        for(ann in newValues) {
            getJTextArea().addAnnotation(ndx, ann.getAn());
            ndx++;
        }
    }
    
    // TODO should be in Component
    public function requestFocus():Void {
        getJComponent().requestFocusInWindow();
    }
    
    public  attribute highlight: Number[] on replace oldValues [lo..hi] = newValues {
        doHighlight();
    };
    private attribute documentListener:DocumentListener = DocumentListener {
        override function insertUpdate(e:DocumentEvent):Void {
            if(not inUpdate) {
                inUpdate = true;
                text = getJTextArea().getText();
                inUpdate = false;
                lineCount = getJTextArea().getLineCount();
            }
        }
        override function removeUpdate(e:DocumentEvent):Void {
            if(not inUpdate) {
                inUpdate = true;
                text = getJTextArea().getText();
                inUpdate = false;
                lineCount = getJTextArea().getLineCount();
            }
        }
        override function changedUpdate(e:DocumentEvent):Void {
            if(not inUpdate) {
                inUpdate = true;
                text = getJTextArea().getText();
                inUpdate = false;
                lineCount = getJTextArea().getLineCount();
            }
        }
    };
    public attribute lineCount: Number;
    public attribute caretDot: Integer;
    public attribute caretMark: Integer;
    
    public attribute rows: Number = getJTextArea().getRows() on replace {
        getJTextArea().setRows(rows);
    };
    public attribute columns: Number = getJTextArea().getColumns() on replace {
        getJTextArea().setColumns(columns);
    };

    

    
    public attribute editorKit: javax.swing.text.EditorKit = getJTextArea().getEditorKit() on replace {
        getJTextArea().setEditorKit(editorKit);
    };
    public attribute disabledTextColor: Color = Color.fromAWTColor(getJTextArea().getDisabledTextColor()) on replace {
        getJTextArea().setDisabledTextColor(disabledTextColor.getAWTColor());
    };
    public attribute selectedTextColor: Color=  Color.fromAWTColor(getJTextArea().getSelectedTextColor()) on replace {
        getJTextArea().setSelectedTextColor(selectedTextColor.getAWTColor());
    };
    public attribute selectionColor: Color =  Color.fromAWTColor(getJTextArea().getSelectionColor()) on replace {
        if(selectionColor != null) {
            getJTextArea().setSelectionColor(selectionColor.getAWTColor());
        }
    };
    public attribute caretColor: Color  =  Color.fromAWTColor(getJTextArea().getCaretColor()) on replace {
        getJTextArea().setCaretColor(caretColor.getAWTColor());
    };
    public attribute tabSize: Number = getJTextArea().getTabSize() on replace {
        getJTextArea().setTabSize(tabSize);
    };
    public attribute lineWrap: Boolean = getJTextArea().getLineWrap() on replace {
        getJTextArea().setLineWrap(lineWrap);
    };
    public attribute wrapStyleWord: Boolean = getJTextArea().getWrapStyleWord() on replace {
        getJTextArea().setWrapStyleWord(wrapStyleWord);
    };
    public attribute text:String on replace old {
        if (not inUpdate) {
            if(text != getJTextArea().getText()) {
                inUpdate = true;
                getJTextArea().setText(text);
                lineCount = getJTextArea().getLineCount();
                undoManager.discardAllEdits();
                getJTextArea().select(0, 0);
                getJTextArea().revalidate();
                getJTextArea().repaint();
                inUpdate = false;
            }
        }

    }
    
    
    private function doHighlight() {
        var highlighter = getJTextArea().getHighlighter();
        highlighter.removeAllHighlights();
        var painter = new DefaultHighlightPainter(getJTextArea().getSelectionColor());
        if (sizeof highlight mod 2 == 0) {
          for (i in [0..<sizeof highlight step 2]) {
              var p0 = highlight[i];
              var p1 = highlight[i+1];
              highlighter.addHighlight(p0, p1, painter);
          }
        }
    }
    public function modelToView(offset:Integer): Rectangle {
        return getJTextArea().modelToView(offset);
    }
    public function viewToModel(p:Point): Number {
         return getJTextArea().viewToModel(p);
    }
    public function getLineOfOffset(offset:Integer): Integer {
         return getJTextArea().getLineOfOffset(offset);
    }
    public function getLineStartOffset(line:Integer): Integer {
        return getJTextArea().getLineStartOffset(line);
    }
    public function replaceRange(replacement:String, start:Number, end:Number) {
        getJTextArea().replaceRange(replacement, start, end);
        getJTextArea().revalidate();
    }
    public function setCaretPosition(pos:Number) {
        getJTextArea().setCaretPosition(pos);
    }
    public function setSelection(startPos:Integer, endPos:Integer) {
        getJTextArea().select(startPos, endPos);
    }
    public function selectLocation(startLine:Integer, startColumn:Integer, endLine:Integer, endColumn:Integer) {
        var off1 = getJTextArea().getLineStartOffset(startLine-1);
        var off2 = getJTextArea().getLineStartOffset(endLine-1);
        var startPos = off1 + startColumn-1;
        var endPos = off2 + endColumn-1;
        //System.out.println("Select {startPos}, {endPos}");
        getJTextArea().select(startPos, endPos);
        getJTextArea().getCaret().setSelectionVisible(true);
    }
    
    protected function getJTextArea() : FXTextArea {
        getJComponent() as FXTextArea;
    }
    
    postinit {
        getJTextArea().getDocument().addDocumentListener(documentListener);
    }
    
    override function createJComponent(): JComponent {
        var jtextarea = new FXTextArea();
        jtextarea.setOpaque(false);
        jtextarea.setEditable(true);
        jtextarea.getCaret().setVisible( true );
        jtextarea.select(0,0);
        jtextarea.getDocument().addUndoableEditListener(UndoableEditListener {
            override function undoableEditHappened(e:UndoableEditEvent):Void {
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
                if (edit != null) {
                    undoManager.addEdit(edit);
                    edit = null;
                }
            }
        });
        // TODO KEYMODIFIER
        var COMMAND = if (java.lang.System.getProperty("os.name").toLowerCase().contains("mac")) 
            then java.awt.event.InputEvent.META_MASK 
            else java.awt.event.InputEvent.CTRL_MASK;
            
        var z = javax.swing.KeyStroke.getKeyStroke(KeyCode.VK_Z, COMMAND);
        var y = javax.swing.KeyStroke.getKeyStroke(KeyCode.VK_Y, COMMAND);
        jtextarea.getInputMap().put(z, "undo");
        jtextarea.getInputMap().put(y, "redo");
        jtextarea.getActionMap().put("undo", javax.swing.AbstractAction {
                override function isEnabled():Boolean {
                    return undoManager.canUndo();
                }
                function actionPerformed(e):Void {
                    undoManager.undo();
                }
            });
        jtextarea.getActionMap().put("redo", javax.swing.AbstractAction {
                override function isEnabled():Boolean {
                    return undoManager.canRedo();
                }
                function actionPerformed(e):Void {
                    undoManager.redo();
                }
            });
            
        caretDot = jtextarea.getCaret().getDot();
        caretMark = jtextarea.getCaret().getMark();
        jtextarea.addCaretListener(CaretListener {
                override function caretUpdate(e:CaretEvent):Void {
                    caretDot = e.getDot();
                    caretMark = e.getMark();
                }
        });            
        
        jtextarea;
    }
    

}
