X/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafxcpad;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.System;
import java.io.*;
import javafx.ui.FileFilter;
import java.net.URL;
import java.util.StringTokenizer;

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
    }
    operation setSourcePath() {
	if (ARGUMENTS:String <> null) {
	    var path = ARGUMENTS:String;
	    var izer = new StringTokenizer(path, ";");
	    var urls = [];
	    while (izer.hasMoreTokens()) {
		var tok = izer.nextToken();
		insert new URL(tok) into urls;
	    }
	    javafxPad.setSourcePath(urls);
	}
    }
    var dummy = setSourcePath()
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
                     var code = javafxPad.userCode;
		     if (not code.endsWith("\n")) {
                         fw.write(code);
                         fw.write("\n");
		     } else {
                         fw.write(code);
		     }
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
			    var code = javafxPad.userCode;
			    if (not code.endsWith("\n")) {
				fw.write(code);
				fw.write("\n");
			    } else {
				fw.write(code);
			    }
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
                text: "Class Path..."
                action: operation() {
                    var d = SourcePathDialog {
		        sourcePath: select new File(u.getPath()) from u in javafxPad.sourcePath where u.protocol == "file"
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
                cells: bind for (i in javafxPad.zoomOptions)
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
"import javafx.ui.Button;

Button \{ 
    text: \"Hello\"
}
";
