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
import java.awt.Dimension;
import java.lang.Math;
import java.lang.System;
import javax.script.*;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import com.sun.javafx.api.*;
import javafx.ui.animation.*;
import java.lang.StringBuffer;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * @author jclarke
 */

public class JavaFXPad extends CompositeWidget {
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
    attribute searchActive: Boolean on replace {
        if(not searchActive) {
            searchValue = "";
            editor.requestFocus();
        }
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
                keyTime: 1s
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
    attribute moveMouse: function(e:MouseEvent):Void = 
                function(e:MouseEvent):Void {
                    mouseX = 1/(zoomValue/100)*e.x;
                    mouseY = 1/(zoomValue/100)*e.y;
                };  
    attribute inCompletion:Boolean;
    attribute compiledContent: Node[];  
    attribute errMessages: Diagnostic[];
    attribute canvas:Canvas = Canvas {
                    onMouseMoved: moveMouse
                    onMouseDragged: moveMouse
                    content: [
                       //Rect { width: bind canvas.width 
                       //        height: bind canvas.height
                       //        fill: Color.WHITE }, 
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

    function isValid():Boolean {
        return sizeof errMessages == 0;
    }
    
    function go() {
        userCode = getResourceAsString(url);
        compile();
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
                var ret = engine.eval(sourceCode, diags);
                if (ret instanceof Widget) {
                    ret = View {
                        content: ret as Widget
                        //sizeToFitCanvas: true
                    }
                }
                compiledContent = [ret as Node];
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
    
    public function composeWidget(): Widget {
        if(url <> null) {
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                          public function run():Void {
                               go();
                          }
                });
        }
        return Canvas {
             cursor: Cursor.DEFAULT
             border: LineBorder {lineColor: Color.BLACK }
             content: [
                View {
                    antialias: true
                    antialiasText: true
                    sizeToFitCanvas: true
                    content: SplitPane {
                        border: LineBorder {lineColor: Color.BLACK }
                        orientation: Orientation.VERTICAL
                        content:  [  // was bound, but your don't want bind here
                            SplitView { // display area
                                weight: 0.45
                                content: BorderPanel {
                                    border: LineBorder {lineColor: Color.BLACK }
                                    preferredSize: new Dimension(100, 500)
                                    center: ScrollPane {
                                        cursor: Cursor.DEFAULT
                                        var font =  Font.Font("Tahoma", ["PLAIN"], 8);
                                        columnHeader: Canvas { // top ruler  // was bound, but your don't want bind here
                                            content: Group {
                                                transform: bind Transform.scale(zoomValue/100, zoomValue/100)
                                                content: Group {
                                                    content: [
                                                        Group {
                                                            var rulerWidth = bind (Math.max(canvas.width, canvas.viewport.currentWidth) *100/zoomValue/ 5).intValue();
                                                            var lastTic = bind rulerWidth*5+100;
                                                            content: bind for (x in [0..lastTic step 5]) {
                                                                    [
                                                                        Line {
                                                                            stroke: Color.BLACK
                                                                            x1: x
                                                                            y1: if(x %100 == 0) then 0 else if(x %10 == 0) then 9 else 12
                                                                            x2: x
                                                                            y2: 15
                                                                        } as Node,
                                                                        if(x %100 == 0) Text{content:"{x}", x: x+2, font:font} as Node else null
                                                                    ]
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
                                                        var rulerHeight = bind (Math.max(canvas.height, canvas.viewport.currentHeight) *100/zoomValue/ 5).intValue();
                                                        var lastTic = bind rulerHeight*5+100;
                                                        content: bind for (y in [0..lastTic step 5]) {
                                                                [
                                                                    Line {
                                                                        stroke: Color.BLACK
                                                                        x1: if(y %100 == 0) then 0 else if(y %10 == 0) then 9 else 12
                                                                        y1: y
                                                                        x2: 15
                                                                        y2: y
                                                                    } as Node,
                                                                    if(y %100 == 0) Text {
                                                                            content:"{y}"
                                                                            font:font
                                                                            x: 6
                                                                            y: y-10
                                                                            //transform: Transform.translate(6, y-10)
                                                                            halign:HorizontalAlignment.TRAILING
                                                                        } as Node else null
                                                                ]
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
                                                transform: bind Transform.translate(-contentGroup.currentX, 0)
                                                content: contentGroup
                                            }
                                        }
                                        view: canvas // the main display for the script
                                    }
                                }
                            },
                            SplitView { // Editor
                                weight: 0.45
                                content: BorderPanel {
                                    border: LineBorder {lineColor: Color.BLACK }
                                    cursor: Cursor.DEFAULT
                                    preferredSize: new Dimension(100, 500)
                                    var editorCanvas:Canvas;
                                    center: (editorCanvas = Canvas {
                                        border: LineBorder {lineColor: Color.BLACK }
                                        content: [
                                            View {
                                                size: bind new Dimension(editorCanvas.width, editorCanvas.height)
                                                //sizeToFitCanvas: true
                                                content: BorderPanel {
                                                    bottom: BorderPanel {
                                                        visible: bind searchActive
                                                        border: EmptyBorder {top: 2, left: 4, bottom: 2, right: 4}
                                                        center: Canvas {
                                                            border: null
                                                            focusable: false
                                                            content: SearchPanel {
                                                                closeAction: function() {searchActive = false;}
                                                                pSearchValue: bind searchValue with inverse
                                                                matchCase: bind matchCase
                                                                open: bind searchActive
                                                                highlightAllAction: function() {
                                                                    highlightAll();
                                                                }
                                                                searchNextAction: function() {searchNext();}
                                                                searchPrevAction: function() {searchPrev();}
                                                            }
                                                        }
                                                        
                                                    }
                                                    center:  (editor = SourceEditor {
                                                        
                                                        editorKit: new com.sun.javafx.api.ui.fxkit.FXEditorKit()
                                                        opaque: true
                                                        selectedTextColor: Color.WHITE
                                                        foreground: Color.BLACK
                                                        selectionColor: Color.rgb(184, 207, 229);
                                                        tabSize: 4
                                                        lineWrap: false
                                                        border: EmptyBorder {left: 4, right: 4}
                                                        font: bind Font.Font("Monospaced", ["PLAIN"], fontSize)
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
                                                                            var x = i + la.currentWidth % 2;
                                                                            [x, if(indexof i %2 ==0) then 1.5 else -1.5]
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                        }
                                                        rowHeader : Canvas {
                                                            cursor: Cursor.DEFAULT
                                                            background: Color.rgba(220, 220, 220, 255)
                                                            content: [
                                                                View {
                                                                    content: (lineNumbers = LineNumberPanel {
                                                                        lineCount: bind editor.lineCount.intValue()
                                                                        font: bind editor.font
                                                                        border: EmptyBorder {right:4}
                                                                    } )as Widget
                                                                },
                                                                Group {
                                                                    content: bind for (err in errMessages) {
									var lineNumber = err.getLineNumber();
                                                                        var r = bind lineNumbers.getCellBounds(0);
                                                                        var errImage = Image {url: "{__DIR__}images/error_obj.gif" };
                                                                        View {
                                                                            toolTipText: "<html><div 'width=300'>{err.getMessage(null)}</div></html>"
                                                                            transform: bind Transform.translate(2, (lineNumber.intValue() -1)*r.height)
                                                                            content: SimpleLabel {icon: errImage}
                                                                            
                                                                        }
                                                                    }
                                                                }
                                                            ]
                                                        }
                                                        
                                                    }) as Widget
                                                }
                                            },
                                            View {
                                            }
                                        ]
                                    }) as Widget
                                }                                
                            },    
                            SplitView { //Error messages
                                weight: 0.10
                                content: BorderPanel {
                                    var listBox:ListBox;
                                    border: LineBorder {lineColor: Color.BLACK }
                                    center: (listBox = ListBox {
                                        action: function() {
                                            if(listBox.selection >= 0 and listBox.selection < sizeof errMessages) {
                                                var err = errMessages[listBox.selection];
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
                                        cells: bind for(err in errMessages) {
                                            var image:String = if(err.getKind() == Diagnostic.Kind.ERROR) then
                                                     "{__DIR__}images/error_obj.gif"
                                                else "{__DIR__}images/warningS_obj.gif";
                                                
                                            ListCell {
                                                text: "<html><table cellspacing='0' cellpadding='0'><tr><td><img src='{image}'></img></td><td>&nbsp;{err.getMessage(null).trim()}</td></tr><table>"
                                                toolTipText: "<html><div>{err.getMessage(null)}</div></html>"
                                            }
                                        }
                                    }) as Widget
                                }                                
                            }                            
                        ]
                    }
                },
                Rect {
                    stroke: Color.BLACK 
                    selectable: true
                    sizeToFitCanvas: true
                    cursor: Cursor.DEFAULT
                    visible: bind inCompletion
                    fill: Color.color(0, 0, 0, 0) as Paint
                    onMousePressed: function(e) {
                        // TODO code completion
                    }

                },
                View {// TODO code completion
                },
                View {// TODO code completion
                }
             ]
         };
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
