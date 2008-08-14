/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafxpad;
import javafx.ui._all_classes;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import net.java.javafx.ui.UIContext;
import net.java.javafx.type.expr.ValidationError;
import net.java.javafx.type.expr.VariableDeclarator;
import net.java.javafx.typeImpl.Compilation;
import net.java.javafx.typeImpl.completion.CompletionProcessor;
import net.java.javafx.typeImpl.completion.CompletionParserTokenManager;
import net.java.javafx.typeImpl.completion.CompletionParserConstants;
import net.java.javafx.typeImpl.completion.SimpleCharStream;
import net.java.javafx.typeImpl.completion.Token;
import net.java.javafx.type.expr.Locatable;
import net.java.javafx.typeImpl.SourceCodeWriter;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.lang.StringBuffer;
import java.lang.Throwable;
import java.lang.System;
import java.lang.Character;
import java.lang.reflect.Method;
import javax.swing.KeyStroke as JKeyStroke;
import javax.swing.UIManager;
import javafx.ui.KeyEvent;
import java.awt.event.KeyEvent as AWTKeyEvent;
import javafx.ui.KeyStroke;
import javax.swing.Action;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map$Entry as MapEntry;
import java.lang.Math;
import java.awt.Dimension;
import java.lang.Thread;
import java.lang.Thread$UncaughtExceptionHandler as UncaughtExceptionHandler;
import java.lang.Throwable;
import java.net.URL;
import java.lang.Object;


class ErrorMessage {
    attribute line: Integer;
    attribute column: Integer;
    attribute length: Integer;
    attribute message: String;
}

class CompletionProposal {
    attribute replacementText: String;
    attribute displayValue: String;
    attribute cursorOffset: Number;
    attribute documentation: String?;
}

public class JavaFXPad extends CompositeWidget {
    attribute runAutomatically: Boolean;
    attribute validateAutomatically: Boolean;
    operation runNow();
    operation validateNow();
    function isValid(): Boolean;
    attribute hasCRs: Boolean;
    attribute fontSize: Number;
    attribute mouseX: Number;
    attribute mouseY: Number;
    attribute zoomValue: Number;
    attribute zoomSelection: Number;
    attribute zoomOptions: Number*;
    attribute lineNumbers: LineNumberPanel;
    attribute frame: Frame;
    attribute imageCache: <<java.awt.Image>>*;
    attribute publicClassImage: String;
    attribute publicMethodImage: String;
    attribute publicFieldImage: String;
    attribute url:String?;
    attribute userCode: String?;
    attribute resultFrame: Frame?;
    attribute compiledContent: Node*;
    attribute compileError: String?;
    attribute canvas:Canvas;
    attribute editor: SourceEditor;
    attribute rowHeader: Widget;
    attribute completionList:ListBox;
    attribute compileRequests: Number;
    attribute completionRequests: Number;
    attribute compilation: Compilation;
    attribute completionStartPos: Number;
    attribute inCompletion: Boolean;
    attribute updatingCompletions: Boolean;
    attribute completionX: Number;
    attribute completionY: Number;
    attribute pageSize: Number;
    attribute selectedDoc: String?;
    attribute selectedProposal: Number;
    attribute errMessages: ErrorMessage*;
    operation evaluate(sourceCode:String, run:Boolean);
    attribute completionProposals: CompletionProposal*;
    operation maybeCompile();
    operation showSelectedDoc();
    attribute compilerTimeline: Timeline;
    attribute showDocTimeline: Timeline;
    operation compile();
    operation completionRequest(k:KeyStroke);
    operation go();
    operation up();
    operation down();
    operation pageUp();
    operation pageDown();
    operation home();
    operation end();
    operation formatMethod(method:Method): String;
    operation formatType(type:Class): String;
    operation makeField(field:String): String;
    operation makeAttr(a:Attribute): String;
    operation makeVar(decl:VariableDeclarator): String;
    operation makeType(type:String): String;
    operation makeOp(op:Operation): String;
    operation makeFunction(funName:String, def:Operation): String;
    attribute editorLineCount: Number;
    operation formatCode();
    operation doCompletion(keyboard:Boolean);
    attribute sourcePath: URL*;
    operation setSourcePath(urls:URL*);
    attribute matchCase: Boolean;
    operation doSearch();
    attribute searchActive: Boolean;
    attribute searchValue: String;
    operation highlightAll();
    operation searchNext();
    operation searchPrev();
}

attribute JavaFXPad.compilerTimeline = Timeline {
        keyFrames:
        [at (0s) {},
        at (2s) {
              trigger {
                      println("COMPILING...");
                      compile();
              }
        }]
};

attribute JavaFXPad.showDocTimeline = Timeline {
          keyFrames:
          [at (0s) {},
          at (.5s) {
                trigger {
                    selectedDoc = completionProposals[selectedProposal].documentation;
                }
          }]
};

operation JavaFXPad.maybeCompile() {
    compilerTimeline.stop();
    compilerTimeline.start();
}

operation JavaFXPad.showSelectedDoc() {
    showDocTimeline.start();
}

operation JavaFXPad.doSearch() {
    if (searchActive) {
        searchNext();
    } else {
        searchActive = true;
    }
}

operation JavaFXPad.searchNext() {
    var text = editor.text;
    var value = searchValue;
    if (not matchCase) {
        text = text.toUpperCase();
        value = value.toUpperCase();
    }
    var dot = editor.caretDot + value.length() + 1;
    var i = text.indexOf(value, dot);
    if (i >= 0) {
        editor.highlight = [i, i + value.length()];
        editor.setSelection(i, i);
    }
}

operation JavaFXPad.searchPrev() {
    var text = editor.text;
    var dot = editor.caretDot-1;
    var value = searchValue;
    if (not matchCase) {
        text = text.toUpperCase();
        value = value.toUpperCase();
    }
    var i = text.lastIndexOf(value, dot);
    if (i >= 0) {
        editor.highlight = [i, i + value.length()];
        editor.setSelection(i, i);
    }
}

operation JavaFXPad.highlightAll() {
    if (searchValue == "") {
        editor.highlight = null;
        return;
    }
    var value = searchValue;
    var dot = 0; //editor.caretDot;
    var text = editor.text;
    if (not matchCase) {
        text = text.toUpperCase();
        value = value.toUpperCase();
    }
    var len = value.length();
    var i = text.indexOf(value, dot);
    delete editor.highlight;
    if (i >= 0) {
        var i0 = i;
        while (i >= 0) {
            insert [i, i+len] into editor.highlight;
            i = text.indexOf(value, i + 1);
        }
        editor.setSelection(i0, i0);
    }
}

trigger on JavaFXPad.searchActive = newValue {
    if (not newValue) {
        searchValue = "";
        editor.getComponent().requestFocus();
    }
}

trigger on JavaFXPad.searchValue = newValue {
    if (searchValue == "") {
        editor.highlight = null;
        return;
    }
    var dot = 0; //editor.caretDot;
    var text = editor.text;
    if (not matchCase) {
        text = text.toUpperCase();
        newValue = newValue.toUpperCase();
    }
    var i = text.indexOf(newValue, dot);
    if (i >= 0) {
        editor.highlight = [i, i + newValue.length()];
        editor.setSelection(i, i);
    } else {
        editor.highlight = null;
    }
}

operation JavaFXPad.setSourcePath(urls: URL*) {
    println("setting source path to : {urls}");
    sourcePath = urls;
    compilation.setSourcePath(urls);
}

attribute JavaFXPad.fontSize = 16;

trigger on new JavaFXPad {
    var self = this;
    /*
   Thread.currentThread().getThreadGroup().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
           operation uncaughtException(thr, t:Throwable) {
               if (t.message != null) {
                   insert ErrorMessage {message: "{t.message}"} into self.errMessages;
               }
           }
       });
*/
}

attribute JavaFXPad.validateAutomatically = true;
attribute JavaFXPad.runAutomatically = true;

trigger on (JavaFXPad.compileError = e) {
    println(e);
}


attribute JavaFXPad.imageCache = (select i.getImage() from i in select Image {url: url} from url in [publicMethodImage, publicFieldImage, publicClassImage]);

attribute JavaFXPad.publicMethodImage = "{__DOCBASE__}/images/methpub_obj.gif";
attribute JavaFXPad.publicFieldImage = "{__DOCBASE__}/images/field_public_obj.gif";
attribute JavaFXPad.publicClassImage = "{__DOCBASE__}/images/class_obj.gif";
attribute JavaFXPad.pageSize = 8;

attribute JavaFXPad.selectedProposal = bind completionList.selection;
attribute JavaFXPad.zoomOptions = [8.33, 12.5, 25, 50, 100, 125, 150, 200, 400, 800, 1600];
attribute JavaFXPad.zoomSelection = 4;
attribute JavaFXPad.zoomValue = bind zoomOptions[zoomSelection];
attribute JavaFXPad.editorLineCount = bind editor.lineCount;

trigger on JavaFXPad.zoomValue = value {    
//println("zoom = {value}");
}

trigger on JavaFXPad.editorLineCount = value {
//println("lineCount = {value}");
}

trigger on JavaFXPad.selectedProposal = value {
    selectedDoc = null;
    showSelectedDoc();
}

operation JavaFXPad.makeField(field:String): String {
    return "<html><table cellpadding='0 0 0 4' cellspacing='0'><tr><td><img src='{publicFieldImage}'></td><td>{field}</td></tr></html>";
}

operation JavaFXPad.makeAttr(a:Attribute): String {
    var t = formatType(a.Type);
    var s = formatType(a.Scope);
    return "<html><table cellpadding='0 0 0 4' cellspacing='0'><tr><td><img src='{publicFieldImage}'></td><td>{a.Name}&nbsp;&nbsp;&nbsp;&nbsp;{t}&nbsp;-&nbsp;{s}</td></tr></html>";
}

operation JavaFXPad.makeVar(decl:VariableDeclarator): String {
    var t = formatType(decl.getType());
    return "<html><table cellpadding='0 0 0 4' cellspacing='0'><tr><td><img src='{publicFieldImage}'></td><td>{decl.getVarName()}&nbsp;&nbsp;&nbsp;&nbsp;{t}&nbsp;-&nbsp;</td></tr></html>";
}

operation JavaFXPad.makeType(type:String): String {
    return "<html><table cellpadding='0 0 0 4' cellspacing='0'><tr><td><img src='{publicClassImage}'></td><td>{type}</td></tr></html>";
}

operation JavaFXPad.makeOp(op:Operation): String {
    var content = "{op.Name}(";
    var sep = "";
    var ret = null;
    for (i in op.Attributes) {
        if (i.Name == "this") {
            continue;
        } else if (i.Name == "return") {
            ret = i;
        } else {
            content = "{content}{sep}{i.Name}:{formatType(i.Type)}";
        }
        sep = ", ";
    }
    if (ret != null) {
        content = "{content}): {formatType(ret.Type)}";
    } else {
        content = "{content})";
    }
    return "<html><table cellpadding='0 0 0 4' cellspacing='0'><tr><td><img src='{publicMethodImage}'></td><td>{content}</td></tr></html>";
    
}

operation JavaFXPad.makeFunction(funName: String, op:Operation): String {
    var contentx = "{funName}(";
    var sep = "";
    var ret = null;
    for (i in op.Attributes[a|a.Name != 'this' and a.Name != 'return']) {
        contentx = "{contentx}{sep}{i.Name}";
        sep = ", ";
    }
    contentx = "{contentx})";
    return contentx;
}

operation JavaFXPad.go() {
    println("go...");
    userCode = getResourceAsString(url);
}

operation JavaFXPad.up() {
    var i = completionList.selection;
    if (i < 0) {
        i = 0;
    } else {
        i--;
        if (i < 0) {
            i = sizeof completionProposals-1;
        }
        completionList.selection = i;
    }
}

operation JavaFXPad.down() {
    var i = completionList.selection;
    if (i < 0) {
        i = 0;
    }  else {
        i++;
        if (i >= sizeof completionProposals) {
            i = 0;
        }
        completionList.selection = i;
    }
}

operation JavaFXPad.pageUp() {
    var i = completionList.selection;
    if (i < pageSize) {
        return;
    }
    i -= pageSize;
    if (i < 0) {
        i = 0;
    }
    completionList.selection = i;
}

operation JavaFXPad.pageDown() {
    var i = completionList.selection;
    if (i > sizeof completionProposals - pageSize) {
        return;
    }
    i += pageSize;
    if (i > sizeof completionProposals -1) {
        i = sizeof completionProposals - 1;
    }
    completionList.selection = i;
}

operation JavaFXPad.home() {
    completionList.selection = 0;
}

operation JavaFXPad.end() {
    completionList.selection = sizeof completionProposals -1;
}

trigger on JavaFXPad.editor = value {
    var comp = editor.getNonScrollPaneComponent();
    var inputMap = comp.getInputMap();
    var tabKey = inputMap.get(JKeyStroke.getKeyStroke(AWTKeyEvent.VK_TAB, 0));
    var leftKey = inputMap.get(JKeyStroke.getKeyStroke(AWTKeyEvent.VK_LEFT, 0));
    var rightKey = inputMap.get(JKeyStroke.getKeyStroke(AWTKeyEvent.VK_RIGHT, 0));
    var upKey = inputMap.get(JKeyStroke.getKeyStroke(AWTKeyEvent.VK_UP, 0));
    var downKey = inputMap.get(JKeyStroke.getKeyStroke(AWTKeyEvent.VK_DOWN, 0));
    var pageUpKey = inputMap.get(JKeyStroke.getKeyStroke(AWTKeyEvent.VK_PAGE_UP, 0));
    var pageDownKey = inputMap.get(JKeyStroke.getKeyStroke(AWTKeyEvent.VK_PAGE_DOWN, 0));
    var homeKey = inputMap.get(JKeyStroke.getKeyStroke(AWTKeyEvent.VK_HOME, 0));
    var endKey = inputMap.get(JKeyStroke.getKeyStroke(AWTKeyEvent.VK_END, 0));
    var enterKey = inputMap.get(JKeyStroke.getKeyStroke(AWTKeyEvent.VK_ENTER, 0));
    var enterAction = (Action)comp.getActionMap().get(enterKey);
    var upAction = (Action)comp.getActionMap().get(upKey);
    var downAction = (Action)comp.getActionMap().get(downKey);
    var pageUpAction = (Action)comp.getActionMap().get(pageUpKey);
    var pageDownAction = (Action)comp.getActionMap().get(pageDownKey);
    var homeAction = (Action)comp.getActionMap().get(homeKey);
    var endAction = (Action)comp.getActionMap().get(endKey);
    var leftAction = (Action)comp.getActionMap().get(leftKey);
    var rightAction = (Action)comp.getActionMap().get(rightKey);
    var self = this;
    comp.getActionMap().put(upKey, new Action() {
        operation isEnabled() {
            return upAction.isEnabled();
        }
        operation actionPerformed(e) {
            if (self.inCompletion) {
                self.up();
            } else {
                upAction.actionPerformed(e);
            }
        }
    });
    comp.getActionMap().put(downKey, new Action() {
        operation isEnabled() {
            return downAction.isEnabled();
        }
        operation actionPerformed(e) {
            if (self.inCompletion) {
                self.down();
            } else {
                downAction.actionPerformed(e);
            }
        }
    });
    comp.getActionMap().put(pageDownKey, new Action() {
        operation isEnabled() {
            return pageDownAction.isEnabled();
        }
        operation actionPerformed(e) {
            if (self.inCompletion) {
                self.pageDown();
            } else {
                //println("page down...{pageDownAction}");
                pageDownAction.actionPerformed(e);
            }
        }
    });
    comp.getActionMap().put(pageUpKey, new Action() {
        operation isEnabled() {
            return pageUpAction.isEnabled();
        }
        operation actionPerformed(e) {
            if (self.inCompletion) {
                self.pageUp();
            } else {
                //println("page up...{pageUpAction}");
                pageUpAction.actionPerformed(e);
            }
        }
    });
    comp.getActionMap().put(homeKey, new Action() {
        operation isEnabled() {
            return homeAction.isEnabled();
        }
        operation actionPerformed(e) {
            if (self.inCompletion) {
                self.home();
            } else {
                homeAction.actionPerformed(e);
            }
        }
    });
    comp.getActionMap().put(endKey, new Action() {
        operation isEnabled() {
            return endAction.isEnabled();
        }
        operation actionPerformed(e) {
            if (self.inCompletion) {
                self.end();
            } else {
                endAction.actionPerformed(e);
            }
        }
    });
    comp.getActionMap().put(leftKey, new Action() {
        operation isEnabled() {
            return leftAction.isEnabled();
        }
        operation actionPerformed(e) {
            self.inCompletion = false;
            leftAction.actionPerformed(e);
        }
    });
    comp.getActionMap().put(rightKey, new Action() {
        operation isEnabled() {
            return rightAction.isEnabled();
        }
        operation actionPerformed(e) {
            self.inCompletion = false;
            rightAction.actionPerformed(e);
        }
    });
    comp.getActionMap().put(tabKey, new Action() {
        operation isEnabled() {
            return true;
        }
        operation actionPerformed(e) {
        //println("tab pressed");
        }
    });
    comp.getActionMap().put(enterKey, new Action() {
        operation isEnabled() {
            return enterAction.isEnabled();
        }
        operation actionPerformed(e) {
            if (not self.inCompletion) {
                enterAction.actionPerformed(e);
            }
        }
    });
    var shift_ctrl_f = JKeyStroke.getKeyStroke(AWTKeyEvent.VK_F, AWTKeyEvent.CTRL_MASK+AWTKeyEvent.SHIFT_MASK);
    
    comp.getInputMap().put(shift_ctrl_f, "format");
    comp.getActionMap().put("format", new Action() {
        operation isEnabled() {
            return true;
        }
        operation actionPerformed(e) {
            self.formatCode();
        }
    });
    
    
    
}

operation JavaFXPad.formatCode() {
    if (true) {
        var line = editor.getLineOfOffset(editor.caretDot) + 1;
        var lineOffset = editor.getLineStartOffset(line-1);
        var colOffset = editor.caretDot - lineOffset + 1;
        var fmt = new JFXCodeFormatter();
        var result = fmt.formatCode(editor.text, line, lineOffset, colOffset, editor.caretDot);
        var newLineOff = fmt.getLineOffset();
        editor.text = result;
        editor.setCaretPosition(newLineOff);
        return;
    }
    //println("format code...");
    var textNoCRs = editor.text.replaceAll("\r", "");
    var reader = new StringReader(textNoCRs);
    var line = editor.getLineOfOffset(editor.caretDot) + 1;
    var lineOffset = editor.getLineStartOffset(line-1);
    var colOffset = editor.caretDot - lineOffset + 1;
    var sw = new StringWriter();
    var p = new PrintWriter(sw);
    var charStream = new SimpleCharStream(reader, 1, 0, 4096);
    var tokenizer = new CompletionParserTokenManager(charStream);
    var tokens = new ArrayList();
    var caretToken = null;
    var caretOff = 0;
    var newCaretPos = editor.caretDot;
    var lastTok = (Token)null;
    while (true) {
        var tok = tokenizer.getNextToken();
        tokens.add(tok);
        if (caretToken == null and tok.beginLine == line) {
            if (tok.beginColumn >= colOffset) {
                if (tok.beginColumn == colOffset or lastTok.beginLine < line) {
                    caretToken = tok;
                    caretOff = tok.beginColumn - colOffset;
                } else {
                    caretToken = lastTok;
                    caretOff = tok.beginColumn - colOffset;
                }
            }
        }
        if (tok.kind == 0) {
            break;
        }
        lastTok = tok;
    }
    var indent = "    ";
    var level = 0;
    var skip = false;
    var stack = new Stack();
    var lineNumVal = 1;
    for (i in [0..tokens.size() - 1]) {
        var tok = (Token)tokens.get(i);
        var special = tok.specialToken;
        var extra = "";
        stack.clear();
        var skipDec = false;
        var newLineCount = 0;
        while (special != null) {
            if (special.image != "\r") {
                if (special.image == "\n") {
                    newLineCount++;
                }
                stack.push(special);
            }
            special = special.specialToken;
        }
        while (stack.size() > 0) {
            special = (Token)stack.pop();
            var specialImage = special.image;
            //println("line {lineNumVal} special='{specialImage}'");
            if (special.kind == CompletionParserConstants.FORMAL_COMMENT or 
            special.kind == CompletionParserConstants.MULTI_LINE_COMMENT or
            special.kind == CompletionParserConstants.SINGLE_LINE_COMMENT) {
                // ok
                p.print(specialImage);
                if (special.kind == CompletionParserConstants.SINGLE_LINE_COMMENT) {
                    for (i in [0..<level]) {
                        p.print(indent);
                    }
                    skip = true;
                }
            } else if (specialImage == "\n") {
                lineNumVal++;
                p.print("\n");
                if (tok.image == '}') {
                    var next = special.specialToken;
                    if (next.image == "\r") {
                        next = next.specialToken;
                    }
                    if (--newLineCount == 0) {
                        //println("early dec level to {level} at {lineNumVal} next='{next.image}'");
                        skipDec = true;
                        level--;
                    } else {
                    //println("next after newline = '{next.image}'");
                    }
                }        
                for (i in [0..<level]) {
                    p.print(indent);
                }
                skip = true;
            } else if (specialImage == "\t") {
                if (not skip) {
                    p.print(indent);
                }
            } else if (special.image == '\r') {
            // nothing
            } else {
                if (not skip) {
                    p.print(specialImage);
                }
            }
            special = special.specialToken;
        }
        p.print(tok.image);
        if (tok.image == '{') {
            level++;
        //println("inc level to {level} at {lineNumVal}");
        } else if (tok.image == '}' and not skipDec) {
            level--;
        //println("dec level to {level} at {lineNumVal} skipDec={skipDec}");
        } else if (tok.image == '[') {
        } else if (tok.image == ']') {
        }
        if (false and caretToken == tok) {
            newCaretPos = sw.toString().length() -tok.image.length() + caretOff;
        }
        skip = false;
    }
    //p.print("\n");
    var newText = sw.toString();
    var lineCount = 0;
    var lineOff = 0;
    var newline = "\n".charAt(0);
    for (i in [0..newText.length()-1]) {
        if (lineCount == line-1) {
            lineOff = i;
            break;
        }
        if (newText.charAt(i) == newline) {
            lineCount++;
            
        }
    }
    //var lineOff = editor.getLineStartOffset(line-1);
    var space = ' '.charAt(0);
    var c = newText.charAt(lineOff);
    var newColOff = 0;
    while (c == space) {
        newColOff++;
        c = newText.charAt(lineOff+newColOff);
        if (c != space) {
            break;
        }
    }
    //println("colOffset={colOffset} newColOff={newColOff}");
    lineOff += if newColOff > colOffset-1 then newColOff else colOffset-1;
    if (userCode != newText) {
        editor.text = newText;
    }
    editor.setCaretPosition(lineOff);
}

operation JavaFXPad.doCompletion(keyboard:Boolean) {
    var selectedIndex = this.completionList.selection;
    var completion = completionProposals[selectedIndex];
    if (completion == null) {
        return;
    }
    var replacementValue = completion.replacementText;
    var cursorOffset = completion.cursorOffset;
    inCompletion = false;
    editor.replaceRange(replacementValue, completionStartPos, editor.caretDot);
    editor.setCaretPosition(editor.caretDot + cursorOffset);
}

operation JavaFXPad.formatType(t:Class) {
    var typeName = t.Name;
    var dot = typeName.lastIndexOf('.');
    if (dot > 0) {
        typeName = typeName.substring(dot + 1);
    }
    return typeName;
}

operation JavaFXPad.formatMethod(method:Method) {
    var name = method.getName();
    var buf = new StringBuffer();
    buf.append(name);
    buf.append('(');
    var parms = method.getParameterTypes();
    var sep = "";
    for (parm in parms) {
        buf.append(sep);
        buf.append("arg{indexof parm}");
        buf.append(":");
        var typeName = formatType(parm);
        buf.append(typeName);
        sep = ", ";
    }
    buf.append(") ");
    buf.append(" ");
    buf.append(formatType(method.getReturnType()));
    buf.append(" - ");
    buf.append(formatType(method.getDeclaringClass()));
    return "<html><table cellpadding='0 0 0 4' cellspacing='0'><tr><td><img src='{publicMethodImage}'></td><td>{buf.toString()}</td></tr></table></html>";
}

operation JavaFXPad.completionRequest(k:KeyStroke) {
    operation getNodeAndDerived() {
        var set = new HashSet();
        operation getDerivedTypes(c:Class, result:Set) {
            if (result.contains(c)) {
                return;
            }
            result.add(c);
            for (i in c.Subclasses) {
                getDerivedTypes(i, result);
            }
        }
        getDerivedTypes(:Node, set);
        return set.toArray();
    }
    var fileName = if url == null then "JavaFXPad" else url;
    var sourceCode = editor.text.replaceAll("\r\n", "\n"); //hack \r\n only counts as one character as far as the caret is concerned
    var processor = new CompletionProcessor();
    var members;
    var offset = editor.caretDot-1;
    delete errMessages;
    try {
        members = processor.getMembers(compilation, fileName,
        sourceCode, offset);
    } catch (err:ValidationError) {
	err.printStackTrace();
        compileError = err.getMessage();
        compileError = "";
        while (err != null) {
            compileError = "{compileError}{err.getMessage()}\n";
            var loc = err;
            var line = loc.getBeginLine();
            insert ErrorMessage {line: line, column: loc.getBeginColumn(), 
                    length: loc.getEndColumn() - loc.getBeginColumn()+1, message: err.getMessage().replaceAll("<", "&lt;")} into errMessages;
            err = err.getNextError();
        }
    }
    var lastToken = processor.getLastTokenBeforeOffset();
    var nextToken = processor.getNextTokenAfterOffset();
    var prefix = processor.getPrefix();
    if (prefix == null) {
        prefix = "";
    }
    completionStartPos = editor.caretDot - prefix.length();
    //delete completionProposals;
    completionList.selection = -1;
    var proposals:CompletionProposal = [];
    operation getIndentAtOffset() {
        var nl = sourceCode.lastIndexOf("\n", offset) + 1;
        var end = nl;
        if (nl < sourceCode.length()) {
            while (end < sourceCode.length() and Character.isWhitespace(sourceCode.charAt(end))) {
                end++;
            }
            return sourceCode.substring(nl, end);
        }
        return "";
    }
    var importPrefix = processor.getImportPrefix();
    for (i in members) {
        if (i instanceof String) {
            insert CompletionProposal {
                replacementText: (String)i
                displayValue: (String)i
                cursorOffset: 0
            } into proposals;
        } else if (i instanceof Operation) {
            var op = (Operation)i;
            if (not op.Name.startsWith(prefix)) {
                continue;
            }
            if (not op.Public) {
                continue;
            }
            var p = op.Attributes[a|not (a.Name in ["this", "return"])];
            insert CompletionProposal {
                replacementText: "{op.Name}()"
                displayValue: makeOp(op)
                cursorOffset: if p == [] then 0 else -1
                documentation: op.Documentation
            } into proposals;
        } else if (i instanceof Class) {
            var c = (Class)i;
            if (not c.Public or c.Abstract) {
                continue;
            }
            var name = this.formatType(c);
            if (not name.startsWith(prefix)) {
                continue;
            }
            var replacement = name;
            var cursorOffset = 0;
            if (nextToken.image != '{') {
                var indent = getIndentAtOffset();
                replacement = "{name} \{\n{indent}\t\n{indent}}";
	        cursorOffset = -(indent.length() + 2);
             }
            insert CompletionProposal {
                replacementText: replacement
                displayValue: makeType(name)
                cursorOffset: cursorOffset
                documentation: c.Documentation
            } into proposals;
        } else if (i instanceof Attribute) {
            var a = (Attribute)i;
            if (not a.Public) {
                continue;
            }
            if (not a.Name.startsWith(prefix)) {
                continue;
            }
            var replacement = a.Name;
            var cursorOffset = 0;
            if (processor.isObjLiteral()) {
                if (lastToken.next.next.image == "attribute") {
                    if (nextToken.image != "=") {
                        replacement = "{a.Name} = ";
                    }
                } else if (nextToken.image != ":") {
                    replacement = "{a.Name}: ";
                }
                if (a.OneToMany or a.ManyToMany) {
                    var indent = getIndentAtOffset();
                    replacement = "{replacement}\n{indent}[]";
                    cursorOffset = -1;
                }
            }
            insert CompletionProposal {
                replacementText: replacement
                displayValue: makeAttr(a)
                cursorOffset: cursorOffset
                documentation: a.Documentation
            } 
    	into proposals;
        } else if (i instanceof VariableDeclarator) {
            var decl = (VariableDeclarator)i;
            insert CompletionProposal {
                replacementText: decl.getVarName()
                displayValue: makeVar(decl)
            } into proposals;
        } else if (i instanceof Method) {
            var method = (Method)i;
            var name = method.getName();
            if (not name.startsWith(prefix)) {
                continue;
            }
            var displayName = this.formatMethod(method);
            insert CompletionProposal {
                replacementText: "{name}()"
                displayValue: displayName
                cursorOffset: if method.getParameterTypes() == [] then 0 else -1
            } into proposals;
        } else if (i instanceof MapEntry) {
            var m = (MapEntry)i;
            var key = (String)m.getKey();
            var value = m.getValue();
            var displayValue = makeField(key);
            var off = 0;
            if (value instanceof Color) {
                var color = (Color)value;
                var r = (color.red*255).intValue();
                var g = (color.green*255).intValue();
                var b = (color.blue*255).intValue();
                var bgcolor = "#{r format as <<%02X>>}{g format as <<%02X>>}{b format as <<%02X>>}";
                displayValue = "<html><table><tr><td width='16' bgcolor='{bgcolor}'>&nbsp;</td><td>{key}</td></tr></table></html>";          
            }
            if (not key.startsWith(prefix)) {
                continue;
            }
            if (value.class instanceof Operation) {
                displayValue = makeFunction(key, (Operation) value.class);
                var args = value.class.Attributes[a|a.Name != 'this' and a.Name != 'return'];
                off = key.length()+2;
                key = "{key}({foreach (a in args) "{a.Name}{if indexof a < sizeof args-1 then ", " else ""}"})";
                off = off - key.length();
            }
            insert CompletionProposal {
                replacementText: key
                displayValue: displayValue
                cursorOffset: off
            } into proposals;
        }
    }       
    delete proposals[p|not p.replacementText.startsWith(prefix)];
    if (sizeof proposals > 0) {
        var list = Arrays.asList(proposals);
        Collections.sort(list, new Comparator() {
            operation compare(o1, o2) {
                var p1 = (CompletionProposal)o1;
                var p2 = (CompletionProposal)o2;
                return p1.displayValue.compareTo(p2.displayValue);
            }
        });
        proposals = (CompletionProposal)list.toArray();
    }
    updatingCompletions = true;
    completionProposals = proposals;
    completionList.selection = 0;
    updatingCompletions = false;
    // fix me: this is just temporary hacking for now...
    var viewLocation = editor.modelToView(editor.caretDot);
    var editorLoc = editor.getNonScrollPaneComponent().getLocationOnScreen();
    var topViewLoc = component.getLocationOnScreen();
    var dx = editorLoc.x - topViewLoc.x;
    var dy = editorLoc.y - topViewLoc.y; 
    var x = dx + viewLocation.x + viewLocation.width;
    var y = dy + viewLocation.y + viewLocation.height;
    var w = component.getWidth();
    var h = component.getHeight();
    if (x + 300 > w) {
        x = w - 300;
    }
    if (y + 150 > h) {
        y = h - 150;
    }
    completionX = x;
    completionY = y;
    inCompletion = sizeof proposals > 0;
}

trigger on JavaFXPad.inCompletion = value {
    if (not value) {
        updatingCompletions = true;
        delete completionProposals;
        updatingCompletions = false;
    }
}

function JavaFXPad.isValid() {
    return sizeof errMessages == 0;
}


operation JavaFXPad.evaluate(sourceCode:String, run:Boolean) {
    //println("evaluating");
    if (compilation == null) {
        compilation = new Compilation(context:UIContext.getModule());
        compilation.setSourcePath(sourcePath);
    }
    var fileName = if url == null then "JavaFXPad" else url;
    var unit;
    try {
        unit = compilation.readCompilationUnit(fileName, new StringReader(sourceCode));
        //println("read unit");
    } catch(err:ValidationError) {
        err.printStackTrace();
        compileError = "";
        delete errMessages;
        while (err != null) {
            compileError = "{compileError}{err.getMessage()}\n";
            var loc = err;
            var line = loc.getBeginLine();
            insert ErrorMessage {line: line, column: loc.getBeginColumn(), length: loc.getEndColumn() - loc.getBeginColumn()+1, message: err.getMessage()} into errMessages;
            err = err.getNextError();
        }
        return;
    }
    var stmt = unit.getStatements();
    var processor = new CompletionProcessor();
    var line = editor.getLineOfOffset(editor.caretDot);
    var lineOffset = editor.getLineStartOffset(line);
    var colOffset = editor.caretDot - lineOffset;
    var t = processor.getType(stmt, lineOffset, colOffset);
    var contextType = processor.getContextType();
    var viewLocation = editor.modelToView(editor.caretDot);
    var err = compilation.getLastError();
    delete errMessages;
    compileError = null;
    if (err != null) {
        compileError = "";
        while (err != null) {
            compileError = "{compileError}{err.getMessage()}\n";
            var line = err.getBeginLine();
            insert ErrorMessage {line: line, column: err.getBeginColumn(), length: err.getEndColumn() - err.getBeginColumn()+1, message: err.getMessage().replaceAll("<", "&lt;")} into errMessages;
            err = err.getNextError();
        }
    } else if (run) {
        var result:Object;
        try {
	    //println("executing unit");
            result = unit.execute();
            //println("result={result}");
        } catch (err:ValidationError) {
	    err.printStackTrace();
            while (err != null) {
                compileError = "{compileError}{err.getMessage()}\n";
                var line = err.getBeginLine();
                var loc = err;
                insert ErrorMessage {line: line, column: loc.getBeginColumn(), length: loc.getEndColumn() - loc.getBeginColumn()+1, message: err.getMessage()} into errMessages;
                err = err.getNextError();
            }
            compileError = err.getMessage();
            return;
        } catch (e) {
            var st = (Locatable).;
	    var stackTrace = st[s|s.getURI() != __FILE__];
            //println("stackTrace={stackTrace}");
            result = e;
            var msg = "{e}";
            for (s in stackTrace) {
                msg = "{msg}\n\t at {s} ({s.getURI()} Line {s.getBeginLine()})";
            }
            if (st[0] != null) {
                insert ErrorMessage {line: st[0].getBeginLine(), column: st[0].getBeginColumn(), length: st[0].getEndColumn() - st[0].getBeginColumn()+1, message: "{st[0].getURI()} Line {st[0].getBeginLine()}: uncaught exception: {e}"} into errMessages;
            }
	    if (e instanceof Throwable) {
                ((Throwable)e).printStackTrace();
            }
            result = msg;
        }
        if (result instanceof Frame) {
            var f = (Frame)result;
            var w = f.width;
            var h = f.height;
            var widget = ((Frame)result).content;
            if (w == null or w == 0) {
                w = widget.getComponent().getPreferredSize().width;
            }
            if (h == null or h == 0) {
                h = widget.getComponent().getPreferredSize().height;
            }
	    if (f.menubar != null) {
		widget = RootPane {
		    menubar: f.menubar
		    content: BorderPanel {
                        opaque: true
                      center: widget
   		    }
		};
	    }

            f.content = null;
	    f.visible = false;
            f.close();
	    if (resultFrame != null) {resultFrame.showing = false;}
            resultFrame = f;
            result = Group {
                var: group
                transform: translate(30, 30)
                content: ViewOutline {
                    selected: true
                    view: widget
                    rectHeight: h
                    rectWidth: w
                }
            };
        }
        var notNode = select n from n in result where not (n instanceof Node) and not (n instanceof Widget);
        if (notNode != null) {
            compileError = "Incompatible type: expected Node, found {notNode[0].class.Name}";     compiledContent = View {
                content: TextArea {
                    text: "{if sizeof result > 1 then "[{foreach (i in result) "{if indexof i > 0 then " ," else ""}{i}"}]" else result}"
                    editable: false
                }
            };
        } else if (run) {
            compiledContent = select if x instanceof Widget then View {content: (Widget)x} else if x instanceof Node then (Node)x else null from x in result;
            compileError = null;
        }
        resultFrame.showing = true;
    }
    if (t == null and contextType == null) {
        return null;
    }
}


trigger on JavaFXPad.userCode[old] = value {
    if (not validateAutomatically) {
        return;
    }
    maybeCompile();
}

operation JavaFXPad.validateNow() {
    try {
        this.compile();
    } catch (e:Throwable) {
        var w = new StringWriter();
        e.printStackTrace(new PrintWriter(w));
        compileError = w.toString();
    }
}

operation JavaFXPad.runNow() {
    try {
        this.evaluate(userCode, true);
    } catch (e:Throwable) {
        var w = new StringWriter();
        e.printStackTrace(new PrintWriter(w));
        compileError = w.toString();
    }
}

operation JavaFXPad.compile() {
    if (this.inCompletion) {
        return;
    }
    var program = userCode;
    if (program.length() == 0) {
        compiledContent = null;
        compileError = null;
        return;
    }
    try {
        this.evaluate(program, runAutomatically);
    } catch (e:Throwable) {
        e.printStackTrace();
        var w = new StringWriter();
        e.printStackTrace(new PrintWriter(w));
        compileError = w.toString();
    }
}

operation JavaFXPad.composeWidget() {
    do later {if (url != null) {go();}}
    return Canvas {
        trigger on not assert a {
             insert ErrorMessage {line: a.lineNumber, message: "{a.sourceURL}:{a.lineNumber}: Assertion failed: {a.assertion} {a.description}"} into errMessages;
        } 


        border: EmptyBorder
        var: canvas


        cursor: DEFAULT
        content:
        [View {
            antialias: true
            antialiasText: true
	    sizeToFitCanvas: true
            content: SplitPane {
                orientation: VERTICAL
                content: bind
                [SplitView {
                    weight: 0.45
                    content: BorderPanel {
                        preferredSize: {height: 500, width: 100}
                        center:
                        ScrollPane {
                            cursor: DEFAULT
                            var font = new Font("Tahoma", "PLAIN", 8)
                            columnHeader: bind if canvas.height == 0 then null else Canvas {
                                border: EmptyBorder
                                content: Group {
                                    transform: bind scale(zoomValue/100, zoomValue/100)
                                    content: Group {
                                        
                                        content: 
                                        [Group {
                                            content: bind foreach (x in [0,5..(Math.max(this.canvas.width, this.canvas.viewport.currentWidth) *100/zoomValue/ 5).intValue()*5+100])
                                            [Line {
                                                stroke: black
                                                x1: x
                                                x2: x
                                                y1: if (x % 100 == 0) 
                                                then 0
                                                else if x % 10 == 0
                                                then 9
                                                else 12
                                                y2: 15
                                            },
                                            if x % 100 == 0 then Text {content: "{x.intValue()}" x: x+2, font: font} else null]
                                        },
                                        Polygon {
                                            var w = 7
                                            var h = 5
                                            transform: bind translate(Math.max(mouseX - w/2, -w/2), 10-h)
                                            fill: black
                                            points: [0, 0, w, 0, w/2, h]
                                        }]
                                    }
                                }
                            }
                            rowHeader: bind if canvas.width == 0 then null else Canvas {
                                border: EmptyBorder
                                var contentGroup = Group {
                                    transform: bind scale(zoomValue/100, zoomValue/100)
                                    content: Group {
                                        content: 
                                        [Group {
                                            
                                            content: bind foreach (y in [0,5..(Math.max(this.canvas.height, this.canvas.viewport.currentHeight)*100/zoomValue /5).intValue()*5+10])
                                            [Line {
                                                stroke: black
                                                y1: y
                                                y2: y
                                                x1: if (y % 100 == 0) 
                                                then 0
                                                else if y % 10 == 0
                                                then 9
                                                else 12
                                                x2: 15
                                            },
                                            if y % 100 == 0 then Text {content: "{y.intValue()}" transform: translate(6, y-10), halign:TRAILING, font: font} else null]
                                        },
                                        Polygon {
                                            var w = 5
                                            var h = 7
                                            transform: bind translate(10-w, Math.max(mouseY - h/2, -h/2))
                                            fill: black
                                            points: [0, 0, 0, h, w, h/2]
                                        }]
                                    }
                                }
                                content: Group {
                                    transform: bind translate(-contentGroup.currentX, 0)
                                    content: contentGroup
                                }
                            }
                            view: Canvas {
                                border: EmptyBorder

                                attribute: canvas
                                background: white
                                onMouseMoved: operation(e:MouseEvent) {
                                    mouseX = 1/(zoomValue/100)*e.x;
                                    mouseY = 1/(zoomValue/100)*e.y;
                                }
                                onMouseDragged: operation(e:MouseEvent) {
                                    mouseX = 1/(zoomValue/100)*e.x;
                                    mouseY = 1/(zoomValue/100)*e.y;
                                }
                                content: Group {
                                    transform: bind scale(zoomValue/100, zoomValue/100)
                                    content: Group {
                                        content: bind compiledContent
                                    }
                                }
                            }
                        }
                    }
                },
                SplitView {
                    weight: 0.45
                    content: BorderPanel {
                        preferredSize: {height: 500, width: 100}
                        cursor: DEFAULT
                        center: Canvas {
                            border: EmptyBorder
                            var: editorCanvas
                            content: 
                            [View {
                                antialias: true, antialiasText: true
                                size: bind select {height: h, width: w} from w in editorCanvas.width, h in editorCanvas.height
                                content: BorderPanel {
                                    bottom: BorderPanel {
                                        visible: bind searchActive
                                        border: EmptyBorder {top: 2, left: 4, bottom: 2, right: 4}
                                        center: Canvas {
                                            
                                            border: EmptyBorder
                                            focusable: false
                                            content: SearchPanel {
                                                // 
                                                closeAction: operation() {searchActive = false;}
                                                searchValue: bind searchValue
                                                matchCase: bind matchCase
                                                open: bind searchActive
                                                highlightAllAction: operation() {
                                                    highlightAll();
                                                }
                                                searchNextAction: operation() {searchNext();}
                                                searchPrevAction: operation() {searchPrev();}
                                            }
                                        }
                                    }
                                    center: SourceEditor {
                                        //onMouseWheelMoved: operation(e) {println(e);}
                                        //preferredSize: {height: 500, width: 800}
                                        editorKit: new <<net.java.javafx.ui.f3kit.F3EditorKit>>()
                                        opaque: true
                                        selectedTextColor: white
                                        foreground: black
                                        annotations: bind foreach (err in errMessages) 
                                        LineAnnotation {
                                            line: err.line
                                            column: err.column
                                            length: err.length
                                            var: self
                                            toolTipText: "<html><div 'width=300'>{err.message}</div></html>"
                                            content: Canvas {
			                        border: EmptyBorder

                                                content: Polyline {
                                                    stroke: red
                                                    strokeLineJoin: BEVEL
                                                    strokeWidth: 0.5
                                                    transform: bind translate(0, self.currentHeight-1)
                                                    points: bind foreach (i in [0,2..self.currentWidth + self.currentWidth % 2]) [i, if indexof i % 2 == 0 then 1.5 else -1.5]
                                                }
                                            }
                                        }
                                        attribute: editor
                                        tabSize: 4
                                        //rows: 1
                                        lineWrap: false
                                        border: EmptyBorder {left: 4, right: 4}
                                        var: self
                                        font: bind new Font("Monospaced", "PLAIN", fontSize)
                                        text: bind userCode
                                        rowHeader: Canvas {
			                    border: EmptyBorder
                                            cursor: DEFAULT
                                            background: new Color(220/255, 220/255, 220/255, 1.0)
                                            content: 
                                            [View {
                                                content: LineNumberPanel {
                                                    attribute: lineNumbers
                                                    lineCount: bind editor.lineCount
                                                    font: bind self.font
                                                    border: EmptyBorder {right: 4}
                                                }
                                            },
                                            Group {
                                                var r = bind lineNumbers.getCellBounds(0)
                                                var errImage = Image {url: "{__DOCBASE__}/images/error_obj.gif"}
                                                content: 
                                                bind foreach (err in errMessages) 
                                                View {
                                                    toolTipText: "<html><div 'width=300'>{err.message}</div></html>"
                                                    transform: bind translate(2, (err.line -1)*r.height)
                                                    content: SimpleLabel {icon: errImage}
                                                    
                                                }
                                                
                                            }]
                                        }
                                        foreground: bind if compileError == null then black else red
                                        onKeyTyped: operation(e:KeyEvent) {
                                            var k:KeyStroke = e.keyStroke;
                                            var modifiers:KeyStroke* = e.modifiers;
                                            if (this.inCompletion) {
                                                var req = ++completionRequests;
                                                do later {
                                                    if (this.inCompletion and req == completionRequests) {
                                                        this.completionRequest(k);
                                                    }
                                                }
                                            } else {
                                                if (e.keyChar == "\t") {
                                                    e.source.consume();
                                                    do later {
                                                        this.formatCode();
                                                    }
                                                }
                                            }
                                        }
                                        onKeyDown: operation(e:KeyEvent) {
                                            var k:KeyStroke = e.keyStroke;
                                            var modifiers:KeyStroke* = e.modifiers;
                                            
                                            if (k == PERIOD:KeyStroke or
                                            k == OPEN_BRACKET:KeyStroke or
                                            (k == SPACE:KeyStroke and CONTROL:KeyStroke in modifiers)) {
                                                do later {
                                                    this.completionRequest(k);
                                                }
                                            } else if (k == ESCAPE:KeyStroke) {
                                                this.inCompletion = false;
                                                maybeCompile();
                                            } else if (k == ENTER:KeyStroke) {
                                                if (this.inCompletion) {
                                                    do later {
                                                        this.doCompletion(true);
                                                    }
                                                }
                                            } else if (k == UP:KeyStroke and CONTROL:KeyStroke in modifiers) {
                                                fontSize++;
                                            } else if (k == DOWN:KeyStroke and CONTROL:KeyStroke in modifiers) {
                                                fontSize--;
                                            }       
                                        }
                                    } 
                                }
                            },
                            
                            View {     
                                visible: false
                                size: bind select {width: w} from w in editorCanvas.width
                                transform: bind translate(0, editorCanvas.height)
                                valign: BOTTOM
                                content:
                                GroupPanel {
                                    background: new Color(1, 1, 1, 0.9)
                                    var row = Row {}
                                    var c1 = Column
                                    var c2 = Column
                                    var c3 = Column {resizable: true}
                                    var c4 = Column
                                    var c5 = Column
                                    var c6 = Column
                                    var c7 = Column
                                    var c8 = Column
                                    rows: row
                                    columns: [c1, c2, c3, c4, c5, c6, c7, c8]
                                    content: 
                                    [Button {
                                        text: "Clear"
                                        row: row
                                        column: c1
                                    },
                                    SimpleLabel {
                                        text: "Find:"
                                        row: row
                                        column: c2
                                    },
                                    TextField {
                                        row: row
                                        column: c3
                                        columns: 10
                                    },
                                    Button {
                                        row: row
                                        column: c4
                                        text: "Next"
                                    },
                                    Button {
                                        row: row
                                        column: c5
                                        text: "Previous"
                                    },
                                    CheckBox {
                                        row: row
                                        column: c6
                                        text: "Highlight all"
                                    },
                                    CheckBox {
                                        row: row
                                        column: c7
                                        text: "Match case"
                                    },
                                    SimpleLabel {
                                        row: row
                                        column: c8
                                        text: "status"
                                    }]
                                }
                            }]
                        }
                    }
                },
                SplitView {
                    weight: 0.10
                    content: BorderPanel {
                        center: ListBox {
                            var: self
                            action: operation() { 
                                var err = errMessages[self.selection];
                                if (err != null) {
                                    do later {
                                        try {
                                            editor.selectLocation(err.line, err.column, err.line, err.column + err.length);
                                         } catch (badLocation) {
                                         }
                                    }
                                }
                            }
                            cells: bind foreach (err in errMessages) 
                            ListCell {
                                var eol = err.message.indexOf("\n")
                                text: "<html><table cellspacing='0' cellpadding='0'><tr><td><img src='{__DOCBASE__}/images/error_obj.gif'></img></td><td>&nbsp;{if eol < 0 then err.message else "{err.message.substring(0, eol).trim()}..."}</td></tr><table>"
                                toolTipText: "<html><div {if eol > 0 then "width='300'" else ""}>{err.message}</div></html>"
                            }
                        }
                    }
                }]
            }
        },
        Rect {
            selectable: true
            sizeToFitCanvas: true
            visible: bind inCompletion
            fill: new Color(0, 0, 0, 0)
            var req = 0
            onMousePressed: operation(e) {
                if (inCompletion) {
                    inCompletion = false;
                    maybeCompile();
                }
            }
            cursor: DEFAULT
        },
        View {
            antialias: false
            antialiasText: false
            visible: bind this.inCompletion
            transform: bind translate(this.completionX, this.completionY)
            cursor: DEFAULT
            content: BorderPanel {
                preferredSize: {width: 300, height: 150}
                center: ListBox {
                    attribute: completionList
                    locked: bind updatingCompletions
                    cursor: DEFAULT
                    focusable: false
                    cells: bind foreach (proposal in this.completionProposals) 
                    ListCell {
                        horizontalAlignment: LEADING
                        text: bind proposal.displayValue
                    }
                    action: operation() {this.doCompletion(false);}
                }
            //border: ShadowedBorder
            }
        },
        View {
            antialias: false
            antialiasText: false
            transform: bind translate(this.completionX+301, this.completionY)
            operation empty(str:String) {
                return str == null or str.trim().length() == 0;
            }
            visible: bind this.inCompletion and not empty(selectedDoc)
            content: BorderPanel {
                preferredSize: {width: 330, height: 150}
                center: ScrollPane {
                    horizontalScrollBarPolicy: NEVER
                    view: Label {
                        opaque: true
                        text: bind "<html><table width='300'><tr><td>{selectedDoc}</td></tr></table></html>"
                    }
                }
            }
        }]      
    };
}

operation getResourceAsString(urlStr:String) {
    try {
        var is = new URL(urlStr).openStream();
        var reader = new BufferedReader(new InputStreamReader(is));
        var line;
        var buf = new StringBuffer();
        while (true) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            buf.append(line);
            buf.append("\n");
        }
        reader.close();
        return buf.toString();
    } catch (e) {
    // ignore
    }
    return "";
}
