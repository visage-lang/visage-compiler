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

import fxpad.gui.*;
import javafx.ext.swing.*;
import javafx.scene.*;
import javafx.scene.geometry.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.text.*;

import java.awt.Dimension;
import java.lang.Math;
import java.lang.System;
import javax.script.*;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import com.sun.javafx.api.*;
import javafx.animation.*;
import java.lang.StringBuffer;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import javax.script.ScriptContext;
import javax.swing.JComponent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.Locale;


/**
 * @author jclarke
 */

public class JavaFXPad extends Component {
    // this has to be created before ScriptEngineManager to catch stderr.
    attribute stdoutPane:StdoutPane;
    
    attribute manager:ScriptEngineManager = new ScriptEngineManager();
    attribute scrEng:ScriptEngine = manager.getEngineByExtension("javafx");
    attribute engine:JavaFXScriptEngine = scrEng as JavaFXScriptEngine;
    attribute scriptContext:ScriptContext = engine.getContext();
    
    private function loadBootScript():String {
        return getResourceAsString("{__DIR__}BootScript.script");
    }
    attribute bootLoad:Boolean = true;
     
    attribute url:String;
    attribute sourcePath: URL[];
    attribute classPath: URL[];
    //TODO work around until BorderPanel has visible attribute.
    attribute searchPanel:BorderPanel;
    
    attribute searchActive: Boolean on replace {
        if(not searchActive) {
            searchValue = "";
            editor.requestFocus();
        }
        //TODO work around until BorderPanel has visible attribute.
        searchPanel.getJComponent().setVisible(searchActive);
    }
    attribute searchValue: String on replace oldValue {
      
        if (searchValue == "") {
            delete editor.highlight;
        }else {
            var dot = 0; //editor.caretDot;
            var text = editor.text;
            var svalue = searchValue;
            if (not matchCase) {
                text = text.toUpperCase();
                svalue = svalue.toUpperCase();
            }
            var i = text.indexOf(svalue, dot);
            if (i >= 0) {
                editor.highlight = [i, i + svalue.length()];
                editor.setSelection(i, i);
            } else {
                delete editor.highlight;
            }
        }
        
    };
    attribute matchCase: Boolean;

    
    attribute compileTimeLine:Timeline = Timeline {
        keyFrames: [
            KeyFrame {
                time: 1s
                action: function() {
                    doRealCompile();
                }
            }
        ]
    };
    
    attribute editor: SourceEditor;
    attribute validateAutomatically: Boolean = true;
    attribute runAutomatically: Boolean = true;
    
    attribute userCode: String = loadBootScript()  on replace {
        if(validateAutomatically) {
            compile();
        }
    };
    attribute lineNumbers: LineNumberPanel;
    attribute fontSize: Integer = 16;
    attribute zoomOptions:Number[] = [8.33, 12.5, 25, 50, 100, 125, 150, 200, 400, 800, 1600];
    attribute zoomSelection:Integer = 4;
    attribute zoomValue: Number =  bind zoomOptions[zoomSelection];
    attribute mouseX: Number;
    attribute mouseY: Number;    
    attribute compiledContent: Node[];  
    attribute errMessages: Diagnostic[];
    attribute canvas:Canvas = Canvas {
                    //TODO - SEE BELOW - onMouseMoved: moveMouse
                    //TODO - SEE BELOW - onMouseDragged: moveMouse
                    content: [
                        //Rectangle { width: bind canvas.width 
                        //       height: bind canvas.height
                        //       fill: Color.WHITE }, 
                        Group {
                            transform: bind Transform.scale(zoomValue/100, zoomValue/100)
                            content: [
                                Group {
                                    content: bind compiledContent
                                }
                            ]
                        }
                    ]
                };
    attribute errImage:Image = Image {url: "{__DIR__}images/error_obj.gif" };
    
    postinit {
        var comp = canvas.getJComponent();
        comp.addMouseMotionListener(MouseMotionListener {
            public function mouseDragged(e: MouseEvent) : Void {
                mouseX = 1/(zoomValue/100)*e.getX();
                mouseY = 1/(zoomValue/100)*e.getY();   
            }
            public function mouseMoved(e: MouseEvent) : Void {
                mouseX = 1/(zoomValue/100)*e.getX();
                mouseY = 1/(zoomValue/100)*e.getY();
            }            
        });
    }
    function isValid():Boolean {
        return sizeof errMessages == 0;
    }
    
    function go() {
        userCode = getResourceAsString(url);
        compile();
    }
    

    function clearConsole() {
        stdoutPane.clear();
    }
    
    
    private function getResourceAsString(urlStr:String):String {
        try {
            var url  = new URL(urlStr);
            var is = url.openStream();
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
        }catch (e:MalformedURLException) {
            System.out.println(e.getMessage());
        } catch (ignore:java.lang.Exception) {
        // ignore
            //e.printStackTrace();
        }
        return "";
    }

    private function compile():Void {
        compileTimeLine.stop();
        if(userCode.length() == 0) {
            compiledContent = null;
            delete errMessages;
            return;
        }
        compileTimeLine.start();
    }
    
    private function doRealCompile():Void {
        var program = userCode;
        evaluate(program, runAutomatically );
    }
    
    private function evaluate(sourceCode:String, run:Boolean) {
        var diags = new DiagnosticCollector();
        try {
            delete errMessages;
            if(not run) {
                var script = engine.compile(sourceCode, diags);
            } else{
                var result = engine.eval(sourceCode, diags);
                if(result instanceof Frame) {
                    var f = result as Frame;
                    var w = f.width;
                    var h = f.height;
                    var component = f.content;
                    if (w == 0) {
                        w = component.getJComponent().getPreferredSize().width;
                    }
                    if (h == 0) {
                        h = component.getJComponent().getPreferredSize().height;
                    }
                    component = RootPane {
                        menus: f.menus
                        content: BorderPanel {
                            //TODO opaque: true
                            center: component
                        }
                    };
                    f.content = null;
                    f.visible = false;
                    result = Group {
                        transform: Transform.translate(30, 30)
                        content: [Rectangle {
                                width: w
                                height: h
                                stroke: Color.BLACK
                            },
                            ComponentView {
                                component: component
                            }
                            ]
                    };

                }else if(result instanceof Component) {
                    result = ComponentView { component: result as Component};
                }else if(not (result instanceof Node)) {
                    var obj = result;
                    var str:String = "{result}";

                    var d:Diagnostic = Diagnostic {
                        public function getKind():Diagnostic.Kind {
                            Diagnostic.Kind.ERROR;
                        }
                        public function getCode():String {"Error";}
                        public function getSource(): java.lang.Object {null;}
                        public function getPosition() : Integer {0;}
                        public function getStartPosition(): Integer {0;}
                        public function getEndPosition(): Integer {str.length()}
                        public function getColumnNumber(): Integer {1;}
                        public function getLineNumber(): Integer {1;}
                        public function getMessage(locale:Locale):String {
                            "Incompatible type: expected Node, found {result.getClass().getName()}";
                        }
                    };
                    result = ComponentView {
                        component: TextArea {
                            text: str
                        }
                    }; 
                    insert d into errMessages;
                }

                compiledContent = [result as Node];
            }
        }catch(e:ScriptException) {
            var errorList = diags.getDiagnostics();
            var iter = errorList.iterator();
            while(iter.hasNext()) {
                var d:Diagnostic = iter.next() as Diagnostic;
                insert d into errMessages;
            }
        } 
    }
    

    
    public function createJComponent(): JComponent {
        if(url != null) {
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                          public function run():Void {
                               go();
                          }
                });
        }
        var canvas1:Canvas;
        var splitPane: SplitPane;
        canvas1 = Canvas {
             //TODO border: LineBorder {lineColor: Color.BLACK }
             content: Group {
                 content: [
                Rectangle {
                    stroke: Color.BLACK 
                    cursor: Cursor.DEFAULT
                    //visible: bind inCompletion
                    fill: Color.TRANSPARENT
                    width: bind canvas1.width -1
                    height: bind canvas1.height -1
                    onMousePressed: function(e) {
                        // TODO - code completion
                    }

                },                 
                ComponentView {
                    component: splitPane = SplitPane {
                        //TODO border: LineBorder {lineColor: Color.BLACK }
                        orientation: Orientation.VERTICAL
                        content: [
                            SplitView { // display area
                                weight: 0.35
                                component: BorderPanel {
                                    //TODO border: LineBorder {lineColor: Color.BLACK }
                                    center: PadScrollPane {
                                        var font =  Font.font("Tahoma", FontStyle.PLAIN, 8);
                                        columnHeader: Canvas { // top ruler
                                            content: Group {
                                                transform: bind Transform.scale(zoomValue/100, zoomValue/100)
                                                content: Group {
                                                    content: [
                                                        Group {
                                                            // NO Viewport in Reprise
                                                            //var rulerWidth = bind(Math.max(canvas.width, canvas.viewport.currentWidth) *100/zoomValue/ 5).intValue();
                                                            var rulerWidth = bind(canvas.width *100/zoomValue/ 5).intValue();
                                                            var lastTic = bind rulerWidth*5+100;                                                        
                                                            content: bind for (x in [0..lastTic step 5]) {
                                                                Group { // TODO inserted this GROUP because of JXFC-876
                                                                    content: [
                                                                        Line {
                                                                            stroke: Color.BLACK
                                                                            startX: x
                                                                            startY: if(x mod 100 == 0) then 0 else if(x mod 10 == 0) then 9 else 12
                                                                            endX: x
                                                                            endY: 15
                                                                        },
                                                                        if(x mod 100 == 0) Text{content:"{x}", x: x+2, font:font} else null
                                                                    ]
                                                                }
                                                            }
                                                        },
                                                        Polygon {
                                                            transform: bind if(mouseX >= 0) {
                                                                    Transform.translate(mouseX-3.5,5);
                                                                } else {
                                                                    Transform.translate(-3.5, 5);
                                                                }
                                                            fill: Color.BLACK
                                                            points: [0.0, 0.0, 7.0, 0.0, 3.5, 5.0]  
                                                        }
                                                    ]
                                                }
                                            }
                                        }
                                        var contentGroup = Group {
                                            transform: bind Transform.scale(zoomValue/100, zoomValue/100)
                                            content: Group {
                                                content: [
                                                    Group {
                                                         // NO ViewPort in Reprise
                                                        //var rulerHeight = bind (Math.max(canvas.height, canvas.viewport.currentHeight) *100/zoomValue/ 5).intValue();
                                                        var rulerHeight = bind (canvas.height *100/zoomValue/ 5).intValue();
                                                        var lastTic = bind rulerHeight*5+100;
                                                    
                                                        content: bind for (y in [0..lastTic step 5]) {
                                                            Group { // TODO inserted this GROUP because of JXFC-876
                                                                content: [
                                                                    Line {
                                                                        stroke: Color.BLACK
                                                                        startX: if(y mod 100 == 0) then 0 else if(y mod 10 == 0) then 9 else 12
                                                                        startY: y
                                                                        endX: 15
                                                                        endY: y
                                                                    },
                                                                    if(y mod 100 == 0) Text {
                                                                            content:"{y}"
                                                                            font:font
                                                                            x: 6
                                                                            y: y-10
                                                                            horizontalAlignment:HorizontalAlignment.TRAILING
                                                                        } else null
                                                                ]
                                                            }
                                                        }
                                                    },
                                                    Polygon {
                                                        transform: bind if(mouseY >= 0) {
                                                                Transform.translate(5, mouseY-3.5);
                                                            } else {
                                                                Transform.translate( 5, 3.5);
                                                            }
                                                        fill: Color.BLACK
                                                        points: [0.0, 0.0, 0.0, 7.0, 5.0, 3.5 ]  
                                                    }
                                                ]
                                            }                                            
                                        };
                                        rowHeader: Canvas { // left margin ruler
                                            content: Group {
                                                transform: bind Transform.translate(-contentGroup.getX(), 0)
                                                content: contentGroup
                                            }
                                        }
                                        view: bind canvas // the main display for the script
                                    }
                                }
                            },
                           
                            SplitView { // Editor
                                var ebp:BorderPanel;
                                weight: 0.35
                                component: ebp = BorderPanel {
                                    //TODO border: LineBorder {lineColor: Color.BLACK }
                                    preferredSize: bind [canvas1.width,canvas1.height/3]

                                    center:  editor = SourceEditor {
                                        
                                        editorKit: new FXEditorKit()
                                        selectedTextColor: Color.WHITE
                                        foreground: Color.BLACK
                                        selectionColor: Color.rgb(184, 207, 229);
                                        tabSize: 4
                                        lineWrap: false
                                        //TODO border: EmptyBorder {left: 4, right: 4}
                                        font: bind Font.font("Monospaced", FontStyle.PLAIN, fontSize)
                                        text: bind userCode with inverse
                                        annotations: bind for (err in errMessages) {
                                            var lineNumber = err.getLineNumber();
                                            var columnNumber = err.getColumnNumber();
                                            var startPosition = err.getStartPosition();
                                            var endPosition = err.getEndPosition();
                                            var length = if (endPosition.intValue() > startPosition.intValue()) endPosition.intValue() - startPosition.intValue() else 1;
                                            var la:LineAnnotation;
                                            la = LineAnnotation {
                                                line: lineNumber.intValue()
                                                column: columnNumber.intValue();
                                                length: length.intValue();
                                                toolTipText: "<html><div 'width=300'>{err.getMessage(null)}</div></html>"
                                                content: Canvas {
                                                    content: Polyline {
                                                        stroke: Color.RED
                                                        strokeLineJoin: StrokeLineJoin.BEVEL
                                                        strokeWidth: 0.5
                                                        transform: bind Transform.translate(0, la.currentHeight-1)
                                                        points: bind for (i in [0.0..la.currentWidth step 2.0]) {
                                                            var x = i + la.currentWidth mod 2;
                                                            [x, if(indexof i mod 2 ==0) then 1.5 else -1.5]
                                                        }
                                                    }
                                                }
                                                
                                            }
                                        }
                                        rowHeader : Canvas {
                                            background: Color.rgb(220, 220, 220, 1.0)
                                            content: [
                                                ComponentView {
                                                    component: (lineNumbers = LineNumberPanel {
                                                        lineCount: bind editor.lineCount.intValue()
                                                        font: bind editor.font
                                                        //TODO border: EmptyBorder {right:4}
                                                    }) as Component
                                                },
                                                Group {

                                                    content: bind for (err in errMessages) {
                                                        var lineNumber:Number = err.getLineNumber();
                                                        var r = bind lineNumbers.getCellBounds(lineNumber.intValue() - 1);
                                                        ComponentView {
                                                            //TODO toolTipText: "<html><div 'width=300'>{err.getMessage(null)}</div></html>"
                                                            transform: bind Transform.translate(2, r.y + r.height/2 - errImage.height/2)
                                                            component: Label {icon: Icon{image:errImage}}

                                                        }
                                                    }
                                                }
                                            ]
                                        }
                                    }
                                    bottom: searchPanel = BorderPanel {
                                        //TODO visible: bind searchActive
                                        //TODO border: EmptyBorder {top: 2, left: 4, bottom: 2, right: 4}
                                        center: Canvas {
                                            //TODO border: null
                                            content: SearchPanel {
                                                closeAction: function() {searchActive = false;}
                                                pSearchValue: bind searchValue with inverse
                                                matchCase: bind matchCase with inverse
                                                open: bind searchActive
                                                highlightAllAction: function() {
                                                    highlightAll();
                                                }
                                                searchNextAction: function() {searchNext();}
                                                searchPrevAction: function() {searchPrev();}
                                            }
                                        }
                                        
                                    }                                    
                                }
                            },
                            SplitView { //Error messages
                                weight: 0.10
                                component: BorderPanel {
                                    //TODO border: LineBorder {lineColor: Color.BLACK }
                                    center: List {
                                        override attribute selectedIndex = -1 on replace {
                                            if(selectedIndex >= 0 and selectedIndex < sizeof errMessages) {
                                                var err = errMessages[selectedIndex];
                                                var lineNumber = err.getLineNumber();
                                                var columnNumber = err.getColumnNumber();
                                                var startPosition = err.getStartPosition();
                                                var endPosition = err.getEndPosition();
                                                var length = 1;
                                                if(endPosition.intValue() > startPosition.intValue()) {
                                                    length = endPosition.intValue() - startPosition.intValue();
                                                }
                                                editor.selectLocation(lineNumber.intValue(), columnNumber.intValue(), lineNumber.intValue(), 
                                                    columnNumber.intValue() + length);
                                            }
                                        }
                                        items: bind for(err in errMessages) {
                                            var image:String = if(err.getKind() == Diagnostic.Kind.ERROR) then
                                                     "{__DIR__}images/error_obj.gif"
                                                else "{__DIR__}images/warningS_obj.gif";
                                                
                                            ListItem {
                                                text: "<html><table cellspacing='0' cellpadding='0'><tr><td><img src='{image}'></img></td><td>&nbsp;{err.getMessage(null).trim()}</td></tr><table>"
                                                //TODO toolTipText: "<html><div>{err.getMessage(null)}</div></html>"
                                            }
                                        }
                                    }
                                }                                
                            },
                            SplitView { //Stdout
                                weight: 0.20
                                component: BorderPanel {
                                    //TODO border: TitledBorder {title: "Console"}
                                      center: stdoutPane =  StdoutPane{rows:4 columns:80}
                                }
                            }
                        ]
                    }
                },
/***********
                ComponentView {
                },
                ComponentView {
                }
*****************/
             ]
             }
         };
         
         canvas1.getJComponent();
    }

    function doSearch():Void  {
        if (searchActive) {
            searchNext();
        } else {
            searchActive = true;
        }
    } 
    function highlightAll() {
        if (searchValue == "") {
            delete editor.highlight;
        }else {
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

    }
    function searchNext() {
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
    
    function searchPrev() {
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
    
    
    function runNow():Void {
         this.evaluate(userCode, true);
    }
    
    function setSourcePath(urls: URL[]) {
        sourcePath = urls;
        scriptContext.setAttribute("sourcepath", URLsToString(urls), ScriptContext.ENGINE_SCOPE);
    }
    function setClassPath(urls: URL[]) {
        classPath = urls;
        scriptContext.setAttribute("classpath", URLsToString(urls), ScriptContext.ENGINE_SCOPE);
    }  
    
    private function URLsToString(urls: URL[]):String {
        var sb = new StringBuffer();
        sb.append(urls[0].toString());
        for(i in [1..< sizeof urls]) {
            sb.append(File.pathSeparator).append(urls[i].toString());
        }
        return sb.toString();
    }
   

}
