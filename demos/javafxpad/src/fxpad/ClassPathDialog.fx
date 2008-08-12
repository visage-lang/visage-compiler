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
import java.io.File;
import java.awt.Dimension;

/**
 * @author jclarke
 */
public class ClassPathDialog {
    var classPath: File[];
    var selectedClassPath:Integer = -1;
    var action: function(classPath: File[]):Void;
    function show(parent:SwingWindow):Void {
        var self = this;
        var oldClassPath = classPath;
        var dlg:SwingDialog;
        dlg = SwingDialog {
            owner: parent
            //TODO - see below visible: true
            
            title: "Class Path"
            height: 300
            width : 500
            //TODO - see below modal: true
            content: BorderPanel {
                top: Label {text: "Class Path"}
                center: List {
                    override var selectedIndex = bind self.selectedClassPath with inverse on replace {
                        self.edit(dlg.content);
                    }
                    items: bind
                    for (f in self.classPath) ListItem {text: f.getCanonicalPath()}
                }
                bottom: FlowPanel {
                    /* TODO border: EmptyBorder {
                        top: 5
                        left: 2
                        right: 5
                        bottom: 2
                    }*/
                    //TODO alignment: Alignment.LEADING
                    content:
                    [SwingButton {
                        preferredSize:  [80, 0]
                        text: "Add"
                        //TODO mnemonic: KeyCode.VK_A
                        action: function() {self.add(dlg.content);}
                    },
                    SwingButton {
                        preferredSize:  [80, 0]
                        text: "Edit"
                        action: function() {self.edit(dlg.content);}
                        enabled: bind self.selectedClassPath >= 0
                    },
                    SwingButton {
                        preferredSize:  [80, 0]
                        text: "Remove"
                        action: function() {self.remove();}
                        enabled: bind self.selectedClassPath >= 0
                    }]
                }
            }
            /** TODO  - see below
            buttons:
            [SwingButton {
                text: "OK"
                action: function():Void {
                    if(self.action <> null) 
                        self.action(self.classPath);
                    dlg.visible=false;
                }
            },
            SwingButton {
                text: "Cancel"
                defaultCancelButton: true
                action: function():Void {
                    self.classPath = oldClassPath;
                    dlg.visible=false;
                }
            }]
            ******************************/
        };
        
        // BEGIN SWING WORK AROUND
        dlg.getJDialog().setModal(true);
        
        
        var b = SwingButton {
                text: "OK"
                action: function():Void {
                    if(self.action != null) 
                        self.action(self.classPath);
                    dlg.visible=false;
                }  
        };
        dlg.getJDialog().add(b.getJComponent());
        b = SwingButton {
            text: "Cancel"
            // TODO defaultCancelButton: true
            action: function():Void {
                self.classPath = oldClassPath;
                dlg.visible=false;
            }
        };
        dlg.getJDialog().add(b.getJComponent());
        dlg.visible = true;
        // END SWING WORK AROUND
        dlg;
    }
                
  
    function add(c:Component):Void {
        var fc:FileChooser;
        fc = FileChooser {
            title: "Class Path"
            files: true
            directories: true
            fileFilters:
            [FileFilter {
                filter: function(f: File):Boolean {
                    return f.isDirectory();
                }
                description: "Class Directories"
            },
            FileFilter {
                filter: function(f:File):Boolean {
                    if (f.isDirectory()) {
                        return true;
                    }
                    var name = f.getName().toUpperCase();
                    return name.endsWith(".JAR");
                }
                description: "Archive Files (*.jar)"
            }]
            action: function(selectedFile:File):Void {
                if (selectedClassPath < 0) {
                    insert selectedFile into classPath;
                    selectedClassPath = 0;
                } else {
                    var i = selectedClassPath;
                    insert selectedFile before classPath[selectedClassPath];
                    selectedClassPath = i;
                }
            }
        };
        fc.showOpenDialog(c);
  
    }
    function remove():Void {
        if(selectedClassPath >= 0 and selectedClassPath < sizeof classPath) {
            var i = selectedClassPath;
            delete classPath[selectedClassPath];
            if (i == sizeof classPath) {
                i--;
            }
            selectedClassPath = i;
        }
    }
    function edit(c:Component):Void {
        var fc:FileChooser;
        fc = FileChooser {
            title: "Class Path"
            files: true
            directories: true
            fileFilters:
            [FileFilter {
                filter: function(f: File):Boolean {return f.isDirectory();}
                description: "Class Directories"
            },
            FileFilter {
                filter: function(f: File):Boolean {
                    if (f.isDirectory()) {return true;}
                    var name = f.getName().toUpperCase();
                    return name.endsWith(".JAR");
                }
                description: "Archive Files (*.jar)"
            }]
            action: function(selectedFile:File):Void {
                classPath[selectedClassPath] = selectedFile;
            }
            cwd: classPath[selectedClassPath]
        };
        fc.showOpenDialog(c);
    }
}
