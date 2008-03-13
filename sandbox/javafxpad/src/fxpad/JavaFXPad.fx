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

/**
 * @author jclarke
 */

public class JavaFXPad extends CompositeWidget {
    attribute manager:ScriptEngineManager = new ScriptEngineManager();
    attribute scrEng:ScriptEngine = manager.getEngineByExtension("javafx");
    attribute engine:JavaFXScriptEngine = scrEng as JavaFXScriptEngine;
    
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
    attribute userCode: String = "import javafx.ui.*;
import javafx.ui.canvas.*;
Text \{ content: 'foobar jim', fill:Color.RED, font:Font.Font('Tahoma', ['BOLD'], 36) }"

    on replace {
        if(validateAutomatically) {
            System.out.println("Compile");
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
                    //System.out.println("{System.currentTimeMillis()}, x= {mouseX} y = {mouseY}");
                };    
    attribute compiledContent: Node[];  
    attribute errMessages: Diagnostic[];
    attribute canvas:Canvas = Canvas {
                    background: Color.WHITE
                    onMouseMoved: moveMouse
                    onMouseDragged: moveMouse
                    content: Group {
                        transform: bind Transform.scale(zoomValue/100, zoomValue/100)
                        content: Group {
                            content: bind compiledContent
                        }
                    }
                };

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
        System.out.println("compile propgram = {program}");
        evaluate(program, runAutomatically);
    }
    
    private function evaluate(sourceCode:String, run:Boolean) {
        System.out.println("evaluate run = {run}, sourceCode = '{sourceCode}'");
        var diags = new DiagnosticCollector();
        try {
            delete errMessages;
            if(not run) {
                System.out.println("compile = '{sourceCode}'");
                var script = engine.compile(sourceCode, diags);
                System.out.println("Return from compile = {script}");
            } else{
                System.out.println("eval = '{sourceCode}'");
                var ret = engine.eval(sourceCode, diags);
                compiledContent = [ret as Node];
                System.out.println("Return from eval = {ret}");
            }
        }catch(e:ScriptException) {
            var errorList = diags.getDiagnostics();
            var iter = errorList.iterator();
            while(iter.hasNext()) {
                var d:Diagnostic = iter.next() as Diagnostic;
                System.out.println("Diag: {d}");
                insert d into errMessages;
            }
        } 
        System.out.println ("Done compile");
    }
    

    
    public function composeWidget(): Widget {
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
                        content: /*bind*/ [
                            SplitView {
                                weight: 0.45
                                content: BorderPanel {
                                    border: LineBorder {lineColor: Color.BLACK }
                                    preferredSize: new Dimension(100, 500)
                                    center: ScrollPane {
                                        cursor: Cursor.DEFAULT
                                        var font =  Font.Font("Tahoma", ["PLAIN"], 8);
                                        columnHeader: Canvas { // top ruler
                                            content: Group {
                                                transform: bind Transform.scale(zoomValue/100, zoomValue/100)
                                                content: Group {
                                                    content: [
                                                        Group {
                                                            content: /* TODO JFXC-878 bind */ for (x in [0..(Math.max(canvas.width, canvas.viewport.currentWidth) *100/zoomValue/ 5).intValue()*5+100 step 5]) {
                                                                Group { // TODO inserted this GROUP because of JXFC-876
                                                                    content: [
                                                                        Line {
                                                                            stroke: Color.BLACK
                                                                            x1: x
                                                                            y1: if(x %100 == 0) then 0 else if(x %10 == 0) then 9 else 12
                                                                            x2: x
                                                                            y2: 15
                                                                        },
                                                                        if(x %100 == 0) Text{content:"{x}", x: x+2, font:font} else null
                                                                    ]
                                                                }
                                                            }
                                                        },
                                                        Polygon {
                                                            transform: bind if(mouseX >= 0) {
                                                                    //System.out.println("BX: {System.currentTimeMillis()}: x = {mouseX}");
                                                                    Transform.translate(mouseX-3.5,5);
                                                                } else {
                                                                    Transform.translate(-3.5, 5);
                                                                }
                                                            fill: Color.BLACK
                                                            points: [0, 0, 7, 0, 3.5, 5]  
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
                                                        content: /* TODO JFXC-878 bind */ for (y in [0..(Math.max(canvas.height, canvas.viewport.currentHeight) *100/zoomValue/ 5).intValue()*5+100 step 5]) {
                                                            Group { // TODO inserted this GROUP because of JXFC-876
                                                                content: [
                                                                    Line {
                                                                        stroke: Color.BLACK
                                                                        x1: if(y %100 == 0) then 0 else if(y %10 == 0) then 9 else 12
                                                                        y1: y
                                                                        x2: 15
                                                                        y2: y
                                                                    },
                                                                    if(y %100 == 0) Text {
                                                                            content:"{y}"
                                                                            font:font
                                                                            x: 6
                                                                            y: y-10
                                                                            //transform: Transform.translate(6, y-10)
                                                                            halign:HorizontalAlignment.TRAILING
                                                                        } else null
                                                                ]
                                                            }
                                                        }
                                                    },
                                                    Polygon {
                                                        transform: bind if(mouseY >= 0) {
                                                                //System.out.println("BY: {System.currentTimeMillis()}: y = {mouseY}");    
                                                                Transform.translate(5, mouseY-3.5);
                                                            } else {
                                                                Transform.translate( 5, 3.5);
                                                            }
                                                        fill: Color.BLACK
                                                        points: [0, 0, 0, 7, 5, 3.5 ]  
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
                            SplitView {
                                weight: 0.45
                                content: BorderPanel {
                                    border: LineBorder {lineColor: Color.BLACK }
                                    cursor: Cursor.DEFAULT
                                    preferredSize: new Dimension(100, 500)
                                    var editorCanvas:Canvas;
                                    center: editorCanvas = Canvas {
                                        border: LineBorder {lineColor: Color.BLACK }
                                        content: [
                                            View {
                                                //size: bind new Dimension(editorCanvas.width, editorCanvas.height)
                                                sizeToFitCanvas: true
                                                content: BorderPanel {
                                                    bottom: BorderPanel {
                                                        visible: false
                                                    }
                                                    center:  editor = SourceEditor {
                                                        editorKit: new com.sun.javafx.api.ui.fxkit.FXEditorKit()
                                                        opaque: true
                                                        selectedTextColor: Color.WHITE
                                                        foreground: Color.BLACK
                                                        selectionColor: Color.MEDIUMAQUAMARINE
                                                        tabSize: 4
                                                        lineWrap: false
                                                        border: EmptyBorder {left: 4, right: 4}
                                                        font: bind Font.Font("Monospaced", ["PLAIN"], fontSize)
                                                        text: bind userCode with inverse
                                                        //annotation: bind for (err in errMessages)
                                                        rowHeader : Canvas {
                                                            cursor: Cursor.DEFAULT
                                                            background: Color.rgba(220, 220, 220, 255)
                                                            content: [
                                                                View {
                                                                    content: lineNumbers = LineNumberPanel {
                                                                        lineCount: bind editor.lineCount.intValue()
                                                                        font: bind editor.font
                                                                        border: EmptyBorder {right:4}
                                                                    }
                                                                },
                                                                Group {
                                                                    var r = bind lineNumbers.getCellBounds(0);
                                                                    var errImage = Image {url: "{__DIR__}images/error_obj.gif" };
                                                                    content: bind for (err in errMessages) {
                                                                        View {
                                                                            toolTipText: "<html><div 'width=300'>{err.getMessage(null)}</div></html>"
                                                                            transform: bind Transform.translate(2, (err.getLineNumber() -1)*r.height)
                                                                            content: SimpleLabel {icon: errImage}
                                                                            
                                                                        }
                                                                    }
                                                                }
                                                            ]
                                                        }
                                                        
                                                    }
                                                }
                                            },
                                            View {
                                            }
                                        ]
                                    }
                                }                                
                            },    
                            SplitView {
                                weight: 0.10
                                content: BorderPanel {
                                    var listBox:ListBox;
                                    border: LineBorder {lineColor: Color.BLACK }
                                    center: listBox = ListBox {
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
                                                System.out.println("source = '{err.getSource()}'");
                                                System.out.println("select {err.getClass()} ({lineNumber}, {columnNumber}, {startPosition}, {endPosition})");
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
                                    }
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
                    //visible: bind inCompletion
                    fill: Color.color(0, 0, 0, 0) as Paint
                    onMousePressed: function(e) {
                        // TODO
                    }

                },
                View {
                },
                View {
                }
             ]
         };
    }


}