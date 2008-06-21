/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafxcpad;
//import javafx.ui._all_classes;
import javafxpad.JFXCodeFormatter;
import java.text.DecimalFormat;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import net.java.javafx.ui.UIContext;
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
import java.util.List;
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
import java.lang.ClassLoader;
import java.net.URLClassLoader;
import java.lang.Object;
import javax.script.*;
import javax.tools.Diagnostic;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.io.File;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

class WidgetHolder extends Widget {
    attribute content: JComponent;
}

operation WidgetHolder.createComponent() {
    return content;
}

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
    attribute preferences: Preferences;
    attribute module: <<net.java.javafx.type.Module>>;
    attribute classLoader: ClassLoader?;
    attribute engine: ScriptEngine?;
    attribute runAutomatically: Boolean;
    attribute validateAutomatically: Boolean;
    operation runNow();
    operation validateNow();
    function isValid(): Boolean;
    private attribute hasCRs: Boolean;
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
    preferences.put("CLASSPATH", "{foreach (u in urls) "{u};"}");
    //    compilation.setSourcePath(urls); 
    if (sizeof urls > 0) {
	classLoader = new URLClassLoader(urls);
    } else {
	classLoader = null;
    }
}

attribute JavaFXPad.fontSize = 16;

trigger on new JavaFXPad {
    preferences = Preferences.userRoot().node("JavaFXCPad");
    var path = preferences.get("CLASSPATH", "");
    if (path != "") {
	var izer = new StringTokenizer(path, ";");
	var urls = [];
	while (izer.hasMoreTokens()) {
	    var tok = izer.nextToken();
	    insert new URL(tok) into urls;
	}
	sourcePath = urls;
	classLoader = new URLClassLoader(sourcePath);
    } else {
        var u1 = new URL(new URL(__FILE__), "../../../dist/javafxc.jar");
        var u2 = new URL(new URL(__FILE__), "../../chris/build/hellofx.jar");
        sourcePath = [u1, u2];
	classLoader = new URLClassLoader(sourcePath);
    }
}

attribute JavaFXPad.validateAutomatically = true;
attribute JavaFXPad.runAutomatically = true;

trigger on (JavaFXPad.compileError = e) {
    println("compileError={e}");
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
    trigger on (i = [false, true] animation {dur: 500ms, condition: bind selectedProposal == value}) {
        if (i and selectedProposal == value) {
            selectedDoc = completionProposals[value].documentation;
        }                        
    }
}

operation JavaFXPad.makeField(field:String): String {
    return "<html><table cellpadding='0 0 0 4' cellspacing='0'><tr><td><img src='{publicFieldImage}'></td><td>{field}</td></tr></html>";
}

operation JavaFXPad.makeAttr(a:Attribute): String {
    var t = formatType(a.Type);
    var s = formatType(a.Scope);
    return "<html><table cellpadding='0 0 0 4' cellspacing='0'><tr><td><img src='{publicFieldImage}'></td><td>{a.Name}&nbsp;&nbsp;&nbsp;&nbsp;{t}&nbsp;-&nbsp;{s}</td></tr></html>";
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

trigger on JavaFXPad.classLoader = newValue {
    engine = null;
}


operation JavaFXPad.evaluate(sourceCode:String, run:Boolean) {
    //println("evaluating");
    compileError = null;
    if (engine == null) {
	try {
	    var cl = classLoader;
	    if (cl == null) { 
		println("CLASSLOADER is null");
		throw "null class loader";
	    }
	    engine = (ScriptEngine)cl.loadClass("com.sun.tools.javafx.script.JavaFXScriptEngineImpl").instantiate();
	    var cp = sourcePath;
	    if (sizeof cp == 0) {
		if (cl instanceof URLClassLoader) {
		    cp = ((URLClassLoader)cl).getURLs();
		}
	    }
	    if (sizeof cp > 0) {
		var classPath = "";
		for (i in cp) {
		    classPath = "{classPath}{File.pathSeparator}{if "file" == i.getProtocol() then i.getPath() else i.toString()}";
		}
		println("CLASSPATH={classPath}");
		engine.getContext().setAttribute("classpath", classPath, ScriptContext.ENGINE_SCOPE);
	    } else {
	    }
	} catch (e) {
            var exc:<<java.lang.Exception>> = e;
            exc.printStackTrace();
	    MessageDialog {
                title: "Error"
                owner: this
		messageType: ERROR
		visible: true
        	message: "Compiler JARS not found (javac.jar, javafxc.jar): set up your class path in the Run menu"
	    };
	}
    }
    var fileName = if url == null then "JavaFXPad" else url;
    var slash = fileName.lastIndexOf("/");
    var dot = fileName.lastIndexOf(".fx");
    var className = if slash > 0 then fileName.substring(slash+1, dot) else fileName;
    var result:Object;
    var errWriter = new StringWriter();
    delete errMessages;
    try {
	/*
        engine.getContext().setAttribute(ScriptEngine.FILENAME, fileName, 
					 ScriptContext.ENGINE_SCOPE);
	*/
	engine.getContext().setErrorWriter(errWriter);
        result = engine.eval(new StringReader(sourceCode));
        println("result = {result}");
    } catch(err:ScriptException) {
        //err.printStackTrace();
        result = "";
        println("err={err.toString()}");
	var a = engine.class.Attributes[a|a.Name=='lastDiagnostics'];
        var diag = (List)engine[a];
	if (diag.size() > 0) {
	    var iter = diag.iterator();
	    delete errMessages;
	    while (iter.hasNext()) {
		var d = (Diagnostic)iter.next();
                println(d.class.Name);
		var errMsg = ErrorMessage {
		    line: d.getLineNumber()
		    column: d.getColumnNumber()
		    message: "{fileName}:{d.getLineNumber()}:{d.getMessage(null).replaceAll("___FX_SCRIPT___", className)}"
		    length: Math.max(d.getEndPosition() + 1 - d.getStartPosition(), 1)
		}; 
		println("errMsg ={errMsg}");
		insert errMsg into errMessages;
		result = "{result}{fileName}:{d.getLineNumber()}:{d.getMessage(null).replaceAll("___FX_SCRIPT___", className)}\n";
	    }
	} else {
	    var w = new StringWriter();
	    err.getCause().printStackTrace(new PrintWriter(w));
	    result = w.toString();
	}
    } catch (e:Throwable) {
	var w = new StringWriter();
	e.printStackTrace(new PrintWriter(w));
	result = w.toString();
    }
    if (compileError != null) {
        //result = compileError;
    } else if (run) {
    }
    var comp:JComponent = null;
    if (result instanceof JComponent) {
          comp = (JComponent)result;
    } 
    if (comp == null) {
	var a = result.class.Attributes[a|a.Name == 'JComponent'][0];
	if (a != null and a.Type == :JComponent) {
	   comp = (JComponent)result[a];
	} 
    }
    if (comp != null) {
	compiledContent = View {
	    content: WidgetHolder {
		content: comp
	    }
	};
    } else {
	var a = result.class.Attributes[a | a.Name == 'frame'][0];
	if (a != null and a.Type == :JFrame) {
	    var frame = (JFrame)result[a];
	    var widget = (JComponent)frame.getContentPane();
	    var w = frame.getRootPane().getWidth();
	    var h = frame.getRootPane().getHeight();
	    if (w == 0) {
		w = widget.getPreferredSize().width;
	    }
	    if (h == 0) {
		h = widget.getPreferredSize().height;
	    }
	    compiledContent =  Group {
                transform: translate(30, 30)
                content: ViewOutline {
                    selected: true
                    rectHeight: h
                    rectWidth: w
                    view: WidgetHolder {component: widget, height: h, width: w} 
                }
            };
	    frame.setContentPane(new <<javax.swing.JPanel>>());
	    frame.hide();
            frame.addWindowListener(new <<java.awt.event.WindowListener>>() {
				operation windowOpened(e) {
				    frame.hide();
				    frame.dispose();
                                }
	    });

	} else {
	    compiledContent = View {
		antialias: true
		antialiasText: true
		content: TextArea {
		    text: "{if sizeof result > 1 then "[{foreach (i in result) "{if indexof i > 0 then " ," else ""}{i}"}]" else result}"
		    editable: false
		}
	    };
	}
    }
}


trigger on JavaFXPad.userCode[old] = value {
    var req = ++compileRequests;
    if (not validateAutomatically) {
        return;
    }
    trigger on (compile = [false, true] animation {dur: 1s}) {
       //println("compile {req}  {compileRequests} {compile}");
       if (compile and compileRequests == req) {
            this.compile();
        }
    }
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
        delete errMessages;
        compiledContent = null;
        compileError = null;
        return;
    }
    try {
	var oldLoader = Thread.currentThread().getContextClassLoader();
	try {
	    Thread.currentThread().setContextClassLoader(classLoader);
	    this.evaluate(program, runAutomatically);
	} finally {
	    Thread.currentThread().setContextClassLoader(oldLoader);
	}
    } catch (e:Throwable) {
        e.printStackTrace();
        //var w = new StringWriter();
        //e.printStackTrace(new PrintWriter(w));
        //compileError = w.toString();
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
                                                var req = ++compileRequests;
                                                trigger on (compile = [false, true] animation {dur: 2s}) {
                                                    if (compile and compileRequests == req) {
                                                        this.compile();
                                                    }
                                                }
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
				    text: "<html><table cellspacing='0' cellpadding='0'><tr><td><img src='{__DOCBASE__}/images/error_obj.gif'></img></td><td>&nbsp;{if eol < 0 then err.message else "{err.message.replace("\n", ",\t")}"}</td></tr><table>"
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
            onMousePressed: operation(e) {
                if (inCompletion) {
                    inCompletion = false;
                    var req = ++compileRequests;
                    trigger on (compile = [false, true] animation {dur: 2s}) {
                        if (compile and compileRequests == req) {
                            this.compile();
                        }
                    }
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
