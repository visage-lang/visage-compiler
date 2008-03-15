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
import java.io.File;
import java.awt.Dimension;

/**
 * @author jclarke
 */

public class ClassPathDialog {
    attribute classPath: File[];
    attribute selectedClassPath:Integer = -1;
    attribute action: function(classPath: File[]):Void;
    function show(parent:UIElement):Void {
        var self = this;
        var oldClassPath = classPath;
        var dlg:Dialog;
        dlg = Dialog {
            owner: parent
            visible: true
            
            title: "Class Path"
            height: 300
            width : 500
            modal: true
            content: BorderPanel {
                top: Label {text: "Class Path"}
                center: ListBox {
                    action: function():Void {self.edit(dlg.content);}
                    selection: bind self.selectedClassPath with inverse
                    cells: bind
                    for (f in self.classPath) ListCell {text: f.getCanonicalPath()}
                }
                bottom: FlowPanel {
                    border: EmptyBorder {
                        top: 5
                        left: 2
                        right: 5
                        bottom: 2
                    }
                    alignment: Alignment.LEADING
                    content:
                    [Button {
                        preferredSize: new Dimension(80,0)
                        text: "Add"
                        mnemonic: KeyStroke.A
                        action: function() {self.add(dlg.content);}
                    },
                    Button {
                        preferredSize: new Dimension(80,0)
                        text: "Edit"
                        action: function() {self.edit(dlg.content);}
                        enabled: bind self.selectedClassPath >= 0
                    },
                    Button {
                        preferredSize: new Dimension(80,0)
                        text: "Remove"
                        action: function() {self.remove();}
                        enabled: bind self.selectedClassPath >= 0
                    }]
                }
            }
            buttons:
            [Button {
                text: "OK"
                action: function():Void {
                    if(self.action <> null) 
                        self.action(self.classPath);
                    dlg.hide();
                }
            },
            Button {
                text: "Cancel"
                defaultCancelButton: true
                action: function():Void {
                    self.classPath = oldClassPath;
                    dlg.hide();
                }
            }]
        };
    }
                
  
    function add(w:Widget):Void {
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
        fc.showOpenDialog(w as UIElement);
  
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
    function edit(w:Widget):Void {
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
        fc.showOpenDialog(w as UIElement);
    }
    

}
