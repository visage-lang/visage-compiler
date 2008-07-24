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
import javafx.scene.paint.*;
import java.lang.System;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;


var javafxPad = JavaFXPad{ url: if(sizeof __ARGS__ > 0) __ARGS__[0] else null };

function fileExists(urlStr:String):Boolean {
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



var fileChooser = FileChooser {
       fileFilters: FileFilter{
            filter: function(f:File):Boolean {
                return f.isDirectory() or f.getName().endsWith(".fx");
            }
            description: "JavaFX Files (*.fx)"
       }
};

var frame:SwingFrame;

frame = SwingFrame {
    visible: true
    closeAction: function() { System.exit(0); }
    title: "JavaFXPad"
    height: 800
    width: 1000
    menus: [
        Menu {
            text: "File"
            //TODO mnemonic: KeyCode.VK_F
            items: [
                MenuItem {
                    text: "Open"
                    //TODO mnemonic: KeyCode.VK_O
                    action: function() {
                        // do open
                        fileChooser.action = function(f:File):Void {
                             javafxPad.url = f.toURL().toString();
                             javafxPad.go();
                        };
                        fileChooser.showOpenDialog(null);

                    }
                },
                MenuItem {
                    text: "Save"
                    //TODO mnemonic: KeyCode.VK_S
                    //TODO accelerator: Accelerator{modifier: KeyModifier.COMMAND, keyStroke: KeyStroke.S}
                    enabled: bind fileExists(javafxPad.url)
                    action: function() {
                        // do save
                         var url = javafxPad.url;
                         var fname = url.substring("file:".length());
                         var fw = new FileWriter(fname);
                         fw.write(javafxPad.userCode);
                         fw.close();
                    }
                },
                MenuItem {
                    text: "Save As"
                    //TODO mnemonic: KeyCode.VK_A
                    action: function() {
                        // do save as
                        fileChooser.action = function(f:File):Void {
                            // TODO this causes an NPE in compiler
                            //function writeFile():Void {
                            //    var fw = new FileWriter(f);
                            //     fw.write(javafxPad.userCode);
                            //     fw.close();
                            //     javafxPad.url = f.toURL().toString();
                            // }
                            if (f.exists()) {
                                ConfirmDialog {
                                  confirmType: ConfirmType.YES_NO
                                  title: "Save As"
                                  message: "File exists, overwrite?"
                                  //onYes: writeFile();
                                  onYes: function ():Void {
                                        var fw = new FileWriter(f);
                                        fw.write(javafxPad.userCode);
                                        fw.close();
                                        javafxPad.url = f.toURL().toString();
                                    }
                                  visible: true
                                }
                            } else {
                                //TODO NPE - writeFile();  
                                var fw = new FileWriter(f);
                                fw.write(javafxPad.userCode);
                                fw.close();
                                javafxPad.url = f.toURL().toString();                                    
                            }
                        };
                        fileChooser.showSaveDialog(null);

                    }
                },
                //TODO MenuSeparator {},
                MenuItem {
                    text: "Exit"
                    //mnemonic: KeyCode.VK_X
                    action: function() {
                        System.exit(0);
                    }
                }
            ]
        },
        Menu {
            text: "Edit"
            //TODO mnemonic: KeyCode.VK_E
            items: [
                MenuItem {
                    text: "Find"
                    //TODO mnemonic: KeyCode.VK_F
                    //TODO accelerator: Accelerator{modifier: KeyModifier.COMMAND, keyStroke: KeyStroke.F}
                    action: function() {
                        javafxPad.doSearch();
                    }
                },
                // TODO MenuSeparator{},
                MenuItem {
                    text: "Clear console"
                    //TODO mnemonic: KeyCode.VK_C
                    //TODO accelerator: Accelerator{modifier: KeyModifier.COMMAND, keyStroke: KeyStroke.C}
                    action: function() {
                        javafxPad.clearConsole();
                    }
                }
            ] 
        },
        Menu {
            text: "Run"
            //TODO mnemonic: KeyCode.VK_R
            items: [

                RadioButtonMenuItem {
                    //TODO mnemonic: KeyCode.VK_U
                    text: "Run Automatically"
                    selected: bind javafxPad.runAutomatically with inverse
                },
                MenuItem {
                    //TODO mnemonic: KeyCode.VK_R
                    text: "Run"
                    enabled: bind not javafxPad.runAutomatically and javafxPad.isValid()
                    action: function() {
                        javafxPad.runNow();
                    }
                },
                MenuItem {
                    //TODO mnemonic: KeyCode.VK_S
                    text: "Source Path..."
                    action: function() {
                        var d = SourcePathDialog {
                            sourcePath: for (u in javafxPad.sourcePath) new File(u.getPath())
                            action: function(path:File[]):Void {
                                javafxPad.setSourcePath(for(f in path)f.toURL());
                            }
                        };
                        d.show(frame);

                    }
                },
                MenuItem {
                    //TODO mnemonic: KeyCode.VK_C
                    text: "Class Path..."
                    action: function() {
                        var d = ClassPathDialog {
                            classPath: for (u in javafxPad.classPath) new File(u.getPath())
                            action: function(path:File[]):Void {
                                javafxPad.setClassPath(for(f in path)f.toURL());
                            }
                        };
                        d.show(frame);

                    }
                }                    
            ]
        }

    ]
    content: ClusterPanel {
        var locLabel = Label { text: "Location:" }
        var urlTextField = TextField {
            columns: 60
            background: Color.WHITE
            hmax: Layout.UNLIMITED_SIZE
            vmax: Layout.PREFERRED_SIZE
            text: bind javafxPad.url with inverse
            action: function() {
                javafxPad.go();
            }
        };
        var goButton = SwingButton {
            text: "Go"
            vmax: Layout.PREFERRED_SIZE
            action: function() {
                javafxPad.go();
            }
        };
        var zoomComboBox = ComboBox {
            vmax: Layout.PREFERRED_SIZE
            items:  for (i in javafxPad.zoomOptions)
                ComboBoxItem{ text: "{i}%" } 
                selectedIndex: bind  javafxPad.zoomSelection with inverse
        }
        var bp = BorderPanel {
            hmax: Layout.UNLIMITED_SIZE
            vmax: Layout.UNLIMITED_SIZE
            center: javafxPad
        };
        hcluster: SequentialCluster {
            content: [
            ParallelCluster {
                content: [
                    SequentialCluster {
                        content: [locLabel, urlTextField, goButton, zoomComboBox]
                    },/* LEADING, RESIZABLE */
                    bp
                ] 
            }
            ]
        }
        vcluster: SequentialCluster {
            content: [
                ParallelCluster {
                    content: [locLabel, urlTextField, goButton, zoomComboBox]
                },
                bp  
            ]
        }
    }

}
