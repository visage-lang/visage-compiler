/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafxpad;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.System;
import java.io.*;
import javafx.ui.FileFilter;
import java.net.URL;

Frame {
    operation fileExists(urlStr:String) {
        if (urlStr == null) {return false;}
	try {
           var url = new URL(urlStr);
           if (url.getProtocol() == "file") {
              var file = new File(url.getPath());
              return file.exists() and file.isFile() and file.canWrite();
           }
        } catch (e) {
            return false;
        }
        return false;
    }
    var: frame
    var javafxPad = JavaFXPad {
        userCode: userCode()
        frame: frame
        url: if ARGUMENTS:String != null then ARGUMENTS:String else null
    }        
    onClose: operation() {System.exit(0);}
    visible: true
    title: "JavaFXPad"
    height: 800
    width: 1000
    var fileChooser = FileChooser {
           fileFilters: {
                filter: operation(f:File) {
                    return f.isDirectory() or f.name.endsWith(".fx");
                }
                description: "JavaFX Files (*.fx)"
           }
    }
    menubar: MenuBar {
        menus: 
        [Menu {
            text: "File"
            mnemonic: F
            items:
            [MenuItem {
                text: "Open"
                mnemonic: O
                action: operation() {
                    fileChooser.action = operation(f:File) {
                         javafxPad.url = f.toURL().toString();
                         javafxPad.go();
                    };
                    fileChooser.showOpenDialog(null);                        
                }
            },
            MenuItem {
                text: "Save"
                mnemonic: S
                accelerator: {modifier: COMMAND, keyStroke: S}
                enabled: bind fileExists(javafxPad.url)
                action: operation() {
                     var url = javafxPad.url;
                     var fname = url.substring("file:".length());
                     var fw = new FileWriter(fname);
                     fw.write(javafxPad.userCode);
                     fw.close();
                }
            },
            MenuItem {
                text: "Save As"
                mnemonic: A
                action: operation() {
                    fileChooser.action = operation(f:File) {
                        operation writeFile() {
                            var fw = new FileWriter(f);
                            fw.write(javafxPad.userCode);
                            fw.close();
                            javafxPad.url = f.toURL().toString();
                        }
                        if (f.exists()) {
                            ConfirmDialog {
                              confirmType: YES_NO
                              title: "Save As"
                              message: "File exists, overwrite?"
                              onYes: writeFile
                              visible: true
                            }
                        } else {
                            writeFile();
                        }
                    };
                    fileChooser.showSaveDialog(null);                        
                }
            },
            MenuSeparator,
            MenuItem {
                text: "Exit"
                mnemonic: X
                action: operation() {
                    System.exit(0);
                }
            }]
        },
        Menu {
	    mnemonic: E
            text: "Edit"
            items:
            [MenuItem {
                text: "Find"
                mnemonic: F
                accelerator: {keyStroke: F, modifier: COMMAND}
                action: operation() {
                    javafxPad.doSearch();
                }
            }]
	},
        Menu {
            mnemonic: R
            text: "Run"
            items:
            [RadioButtonMenuItem {
                visible: false
                mnemonic: A
                text: "Validate Automatically"
                selected: bind javafxPad.validateAutomatically 
		enabled: bind not javafxPad.runAutomatically
            },
            MenuItem {
                visible: false
                mnemonic: R
                text: "Validate"
                enabled: bind not javafxPad.validateAutomatically
                action: operation() {
                    javafxPad.validateNow();
                }
            },
            RadioButtonMenuItem {
                mnemonic: U
                text: "Run Automatically"
                selected: bind javafxPad.runAutomatically
            },
            MenuItem {
                mnemonic: R
                text: "Run"
                enabled: bind not javafxPad.runAutomatically and javafxPad.isValid()
                action: operation() {
                    javafxPad.runNow();
                }
            },
            MenuItem {
                mnemonic: S
                text: "Source Path..."
                action: operation() {
                    var d = SourcePathDialog {
		        sourcePath: select new File(u.getPath()) from u in javafxPad.sourcePath
                        action: operation(path:File*) {
			    javafxPad.setSourcePath(select f.toURL() from f in path);
			}
	            };
                    d.show(frame);
                }
            }]
           
        }]
    }
    content: GroupPanel {

        var row1 = Row {alignment: LEADING}
        var row2 = Row {resizable: true}
        var col = Column {resizable: true}
        rows: [row1, row2]
        columns: col
        content: 
        [GroupLayout {
            row: row1
            column: col
            var row = Row {alignment: BASELINE}
            var labelCol = new Column
            var fieldCol = new Column {resizable: true}
            var butCol = new Column
            var zoomCol = new Column
            rows: row
            columns: [labelCol, fieldCol, butCol, zoomCol]
            content: 
            [SimpleLabel {
                row: row
                column: labelCol
                text: "Location:"
            },
            TextField {
                row: row
                column: fieldCol
                columns: 60
                value: bind javafxPad.url
                action: operation() {
                    javafxPad.go();
                }
            },
            Button {
                row: row
                column: butCol
                text: "Go"
                action: operation() {
                    javafxPad.go();
                }
            },
            ComboBox {
                row: row
                column: zoomCol
                cells: bind foreach (i in javafxPad.zoomOptions)
                   ComboBoxCell { text: "{i format as <<0.##>>}%"}
                selection: bind javafxPad.zoomSelection
            }]
        },
        BorderPanel {
            row: row2
            column: col
            center: javafxPad
        }]
    }
}

function userCode() =
"import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;

Group \{

    var x = 0
    var y = 0
    var alpha = 1

    transform: bind translate(x, y)
    opacity: bind alpha

    onMouseDragged: operation(e) \{
        // updating x and y will move the group
        // since a translation transform is bound to them
        x += e.localDragTranslation.x;
        y += e.localDragTranslation.y;
    }

    onMouseClicked: operation(e) \{
        // updating alpha will fade the group
        // since the opacity of the group is bound to it
        if (alpha == 1) \{
            alpha = [0, 1] animation \{dur: 1s interpolate: EASEBOTH};
        }
    }

    toolTipText: \"Click to fade, or drag to move rectangle\"

    content:
    [Rect \{
        cursor: HAND
        selectable: true
        x: 10
        y: 10
        width: 385
        height: 220
        fill: LinearGradient \{
            x1: 0
            y1: 0
            x2: 1
            y2: 0
            stops:
            [Stop \{offset: 0, color: gray},
            Stop \{offset: .5, color: new Color(.8, .8, .8, 1)},
            Stop \{offset: 1, color: gray}]
        }
        stroke: black
        strokeWidth: 1
    },
    Group \{
        
        function filt(color:Color, bump:Number, intensity:Number, burstFactor:Number) = 
           [ShapeBurst \{factor: burstFactor},
            LightFilter \{
                highlight: 3 
                viewDistance: 300 
                diffuseColor: color
                bumpSoftness: bump
                light: AmbientLight \{intensity: intensity}
            }];
        
        content: [
        Group \{
            filter: ShadowFilter \{x: 10, y: -9, distance: 10 shadowColor: black, opacity: 0.7 }
            
            content: [
            Rect \{height: 150, width: 210, transform: scale(2, 2)},
            Path \{
                d: [
                MoveTo \{
                    x: 50.0
                    y: 90.0
                    absolute: true
                },
                CurveTo \{
                    x1: 0.0
                    y1: 90.0
                    x2: 0.0
                    y2: 30.0
                    x3: 50.0
                    y3: 30.0
                    smooth: false
                    absolute: true
                },
                LineTo \{
                    x: 150.0
                    y: 30.0
                    absolute: true
                },
                CurveTo \{
                    x1: 200.0
                    y1: 30.0
                    x2: 200.0
                    y2: 90.0
                    x3: 150.0
                    y3: 90.0
                    smooth: false
                    absolute: true
                },
                ClosePath \{},
                ]
                stroke: rgba(0xD9, 0x00, 0x00, 0xff)
                strokeWidth: 10.0
                transform: scale(2, 2)
                filter: valueof filt(red:Color, 6, .5, 4)
            },
            Path \{
                d: [
                MoveTo \{
                    x: 60.0
                    y: 80.0
                    absolute: true
                },
                CurveTo \{
                    x1: 30.0
                    y1: 80.0
                    x2: 30.0
                    y2: 40.0
                    x3: 60.0
                    y3: 40.0
                    smooth: false
                    absolute: true
                },
                LineTo \{
                    x: 140.0
                    y: 40.0
                    absolute: true
                },
                CurveTo \{
                    x1: 170.0
                    y1: 40.0
                    x2: 170.0
                    y2: 80.0
                    x3: 140.0
                    y3: 80.0
                    smooth: false
                    absolute: true
                },
                ClosePath \{},
                ]
                fill: rgba(0xD9, 0x00, 0x00, 0xff)
                transform: scale(2, 2)
                filter: valueof filt(red:Color, 5, .5, 7)
            },
            Group \{
                content: [
                Text \{
                    verticalAlignment: BASELINE
                    content: 'JavaFX'
                    font: Font \{faceName: 'Verdana', style: BOLD, size: 25.0}
                    fill: white
                    x: 50.0
                    y: 69.0
                    transform: scale(2, 2)
                    filter: [ShadowFilter \{x: 5, y: -5, distance: 3, shadowColor:black opacity: 0.8}]
                    
                }]
            }]
        }]
    }]
}";
