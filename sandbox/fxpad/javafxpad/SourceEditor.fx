/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafxpad;
import javafx.ui.*;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.undo.*;
import javax.swing.event.*;
import javax.swing.text.DefaultHighlighter$DefaultHighlightPainter as DefaultHighlightPainter;

public class LineAnnotation {
    private attribute changeListeners: <<javax.swing.event.ChangeListener>>*;
    private attribute an: <<net.java.javafx.ui.f3kit.F3TextArea$LineAnnotation>>;
    private operation getAn():<<net.java.javafx.ui.f3kit.F3TextArea$LineAnnotation>>;
    private operation fireChange();

    public attribute line: Integer;
    public attribute column: Integer;
    public attribute length: Integer;
    public attribute currentX: Number;
    public attribute currentY: Number;
    public attribute currentWidth: Number;
    public attribute currentHeight: Number;
    public attribute content: Widget;
    public attribute toolTipText: String;
}


trigger on LineAnnotation.line = value {
    fireChange();
}

trigger on LineAnnotation.column = value {
    fireChange();
}

trigger on LineAnnotation.length = value {
    fireChange();
}

trigger on LineAnnotation.content = value {
    fireChange();
}

operation LineAnnotation.fireChange() {
    for (i in changeListeners) {
        i.stateChanged(null);
    }
}

operation LineAnnotation.getAn() {
    if (an == null) {
        var self = this;
        an = new <<net.java.javafx.ui.f3kit.F3TextArea$LineAnnotation>>() {
            operation getLine() {
                return self.line;
            }
            operation getColumn() {
                return self.column;
            }
            operation getLength() {
                return self.length;
            }
            operation getToolTipText() {
                return self.toolTipText;
            }
            operation setBounds(x, y, w, h) {
                self.currentX = x;
                self.currentY = y;
                self.currentWidth = w;
                self.currentHeight = h;
            }
            operation getComponent() {
                return self.content.getComponent();
            }
            operation addChangeListener(l) {
                insert l into self.changeListeners;
            }
            operation removeChangeListener(l) {
                delete self.changeListeners[x|x == l];
            }
        };
    }
    return an;
}

trigger on LineAnnotation.currentWidth = x {
    //println("width = {x}");
}


public class SourceEditor extends ScrollableWidget {
    private operation doHighlight();
    private attribute edit:UndoableEdit?;
    private attribute undoManager: UndoManager;
    private attribute inUpdate:Boolean;
    private attribute documentListener:<<javax.swing.event.DocumentListener>>;
    private attribute jtextarea: <<net.java.javafx.ui.f3kit.F3TextArea>>;
    public attribute highlight: Number*;
    public attribute editable: Boolean?;
    public attribute rows: Number?;
    public attribute columns: Number?;
    public attribute lineWrap: Boolean?;
    public attribute tabSize: Number?;
    public attribute wrapStyleWord: Boolean?;
    public attribute text:String;
    public attribute caretDot: Integer;
    public attribute caretMark: Integer;
    public attribute lineCount: Number;
    public attribute selectedTextColor: Color?;
    public attribute selectionColor: Color?;
    public attribute caretColor: Color?;
    public attribute disabledTextColor: Color?;
    public operation modelToView(offset:Integer): Rectangle;
    public operation viewToModel(p:Point): Number;
    public operation getLineOfOffset(offset:Integer): Integer;
    public operation getLineStartOffset(line:Integer): Integer;
    public operation replaceRange(replacement:String, start:Number, end:Number);
    public operation setCaretPosition(pos:Number);
    public operation setSelection(startPos:Integer, endPos:Integer);
    public operation selectLocation(startLine:Integer, startColumn:Integer, endLine:Integer, endColumn:Integer);
    public attribute editorKit: <<javax.swing.text.EditorKit>>?;
    public attribute annotations: LineAnnotation*;

}

trigger on insert n into SourceEditor.highlight {
    doHighlight();
}

trigger on delete n from SourceEditor.highlight {
    doHighlight();
}

trigger on SourceEditor.highlight[oldValue] = newValue {
    doHighlight();
}

operation SourceEditor.doHighlight() { 
    var highlighter = jtextarea.getHighlighter();
    highlighter.removeAllHighlights();
    var painter = new DefaultHighlightPainter(jtextarea.getSelectionColor());
    if (sizeof highlight % 2 == 0) {
      for (i in [0,2..<sizeof highlight]) {
	  var p0 = highlight[i];
	  var p1 = highlight[i+1];
	  highlighter.addHighlight(p0, p1, painter);
      }
    }
}

trigger on insert a into SourceEditor.annotations {
    jtextarea.addAnnotation(indexof a, a.getAn());
}

trigger on delete a from SourceEditor.annotations {
    jtextarea.removeAnnotation(indexof a);
}

trigger on SourceEditor.annotations[oldValue] = newValue {
    jtextarea.setAnnotation(indexof newValue, newValue.getAn());
}

operation SourceEditor.setCaretPosition(pos:Number) {
    jtextarea.setCaretPosition(pos);
}

operation SourceEditor.setSelection(startPos:Integer, endPos:Integer) {
    jtextarea.<<select>>(startPos, endPos);
}

operation SourceEditor.selectLocation(startLine:Integer, startColumn:Integer, endLine:Integer, endColumn:Integer) {
    var off1 = jtextarea.getLineStartOffset(startLine-1);
    var off2 = jtextarea.getLineStartOffset(endLine-1);
    var startPos = off1 + startColumn-1;
    var endPos = off2 + endColumn-1;
    jtextarea.<<select>>(startPos, endPos);
    jtextarea.getCaret().setSelectionVisible(true);
}

operation SourceEditor.modelToView(offset:Integer) {
    return jtextarea.modelToView(offset);
}

operation SourceEditor.viewToModel(p:Point) {
    return jtextarea.viewToModel(p);
}

trigger on SourceEditor.tabSize = value {
    jtextarea.setTabSize(value);
}

operation SourceEditor.getLineOfOffset(offset:Integer) {
    return jtextarea.getLineOfOffset(offset);
}

operation SourceEditor.getLineStartOffset(line: Number) {
    return jtextarea.getLineStartOffset(line);
}

operation SourceEditor.createView():<<javax.swing.JComponent>> {
    if (editable) {
        jtextarea.<<select>>(0,0);
    }
    var self = this;
    documentListener = new <<javax.swing.event.DocumentListener>>() {
        operation insertUpdate(e:<<javax.swing.event.DocumentEvent>>) {
            if (not self.inUpdate) {
                self.inUpdate = true;
                self.text = self.jtextarea.getText();
                self.inUpdate = false;
                self.lineCount = self.jtextarea.getLineCount();
            }
        }
        operation removeUpdate(e:<<javax.swing.event.DocumentEvent>>) {
            if (not self.inUpdate) {
                self.inUpdate = true;
                self.text = self.jtextarea.getText();
                self.inUpdate = false;
                self.lineCount = self.jtextarea.getLineCount();
            }
        }
        operation changedUpdate(e:<<javax.swing.event.DocumentEvent>>) {
            if (not self.inUpdate) {
                self.inUpdate = true;
                self.text = self.jtextarea.getText();
                self.lineCount = self.jtextarea.getLineCount();
                self.inUpdate = false;
            }
        }
    };
    var count = 0;
    jtextarea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            operation undoableEditHappened(e:UndoableEditEvent) {
                if (self.edit == null) {
                    self.edit = e.getEdit();
                } else if (not self.edit.addEdit(e.getEdit())) {
                    var compoundEdit = new CompoundEdit();
                    compoundEdit.addEdit(self.edit);
                    compoundEdit.addEdit(e.getEdit());
                    self.edit = compoundEdit;
                }
                var c = ++count;
                trigger on (i = [false, true] animation {dur: {millis: 1000}, condition: bind count == c}) { 
                    if (i and c == count) {
                        if (self.edit instanceof CompoundEdit) {
                            var compoundEdit = (CompoundEdit)self.edit;
                            if (compoundEdit.isInProgress()) {
                                compoundEdit.end();
                            }
                        }
                        if (self.edit <> null) {
                            self.undoManager.addEdit(self.edit);
                            self.edit = null;
                        }
                    }
                }
            }
        });
    var z = <<javax.swing.KeyStroke>>.getKeyStroke(Z:KeyStroke.id, COMMAND:KeyModifier.id);
    var y = <<javax.swing.KeyStroke>>.getKeyStroke(Y:KeyStroke.id, COMMAND:KeyModifier.id);
    jtextarea.getInputMap().put(z, "undo");
    jtextarea.getInputMap().put(y, "redo");
    jtextarea.getActionMap().put("undo", new <<javax.swing.Action>>() {
            operation isEnabled() {
                return self.undoManager.canUndo();
            }
            operation actionPerformed(e) {
                self.undoManager.undo();
            }
        });
    jtextarea.getActionMap().put("redo", new <<javax.swing.Action>>() {
            operation isEnabled() {
                return self.undoManager.canRedo();
            }
            operation actionPerformed(e) {
                self.undoManager.redo();
            }
        });
    caretDot = jtextarea.getCaret().getDot();
    caretMark = jtextarea.getCaret().getMark();
    jtextarea.addCaretListener(new CaretListener() {
                operation caretUpdate(e:CaretEvent) {
                    self.caretDot = e.getDot();
                    self.caretMark = e.getMark();
                }
        });
    
   <<javax.swing.ToolTipManager>>.sharedInstance().registerComponent(jtextarea);
    jtextarea.getDocument().addDocumentListener(documentListener);
    return jtextarea;
}

operation SourceEditor.replaceRange(value:String, start:Number, end:Number) {
    jtextarea.replaceRange(value, start, end);
    jtextarea.revalidate();
}


trigger on (SourceEditor.rows = value) {
    jtextarea.setRows(value);
}

trigger on (SourceEditor.columns = value) {
    jtextarea.setColumns(value);
}

trigger on (SourceEditor.lineWrap = value) {
    jtextarea.setLineWrap(value);
}

trigger on (SourceEditor.wrapStyleWord = value) {
    jtextarea.setWrapStyleWord(value);
}

trigger on (SourceEditor.editable = value) {
    jtextarea.setEditable(value);
}

trigger on SourceEditor.editorKit = value {
    jtextarea.setEditorKit(value);
}

trigger on (new SourceEditor) {
    jtextarea = new <<net.java.javafx.ui.f3kit.F3TextArea>>();
    jtextarea.setOpaque(false);
    undoManager = new UndoManager();
    var self = this;
    editable = true;
}

trigger on (SourceEditor.text = value) {
    if (not inUpdate) {
        inUpdate = true;
        jtextarea.setText(value);
        lineCount = jtextarea.getLineCount();
        this.undoManager.discardAllEdits();
        jtextarea.<<select>>(0, 0);
        jtextarea.revalidate();
        jtextarea.repaint();
        inUpdate = false;
    }
}

trigger on SourceEditor.selectedTextColor = color {
    jtextarea.setSelectedTextColor(color.getColor());
}

trigger on SourceEditor.selectionColor = color {
    jtextarea.setSelectionColor(color.getColor());
}

trigger on SourceEditor.caretColor = color {
    jtextarea.setCaretColor(color.getColor());
}
