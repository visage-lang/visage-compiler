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

public class SourcePathDialog {
    attribute sourcePath: File[];
    attribute selectedSourcePath:Integer = -1;
    attribute action: function(sourcePath: File[]):Void;
    function show(parent:SwingWindow):Void {
        var self = this;
        var oldSourcePath = sourcePath;
        var dlg:SwingDialog;
        dlg = SwingDialog {
            owner: parent
            //TODO see Work Arround below - visible: true
            
            title: "Source Path"
            height: 300
            width : 500
            //TODO modal: true
            content: BorderPanel {
                top: Label {text: "Source Path"}
                center: List {
                    override attribute selectedIndex = bind self.selectedSourcePath with inverse on replace {
                        self.edit(dlg.content);
                    }
                    items: bind
                    for (f in self.sourcePath) ListItem {text: f.getCanonicalPath()}
                }
                bottom: FlowPanel {
                    /*** TODO border: EmptyBorder {
                        top: 5
                        left: 2
                        right: 5
                        bottom: 2
                    } ***/
                    //TODO alignment: Alignment.LEADING
                    content:
                    [SwingButton {
                        preferredSize: [ 80, 0]
                        text: "Add"
                        //TODO mnemonic: KeyCode.VK_A
                        action: function() {self.add(dlg.content);}
                    },
                    SwingButton {
                        preferredSize: [ 80, 0]
                        text: "Edit"
                        action: function() {self.edit(dlg.content);}
                        enabled: bind self.selectedSourcePath >= 0
                    },
                    SwingButton {
                        preferredSize: [ 80, 0]
                        text: "Remove"
                        action: function() {self.remove();}
                        enabled: bind self.selectedSourcePath >= 0
                    }]
                }
            }
            /******* TODO 
            buttons:
            [SwingButton {
                text: "OK"
                action: function():Void {
                    if(self.action <> null) 
                        self.action(self.sourcePath);
                    dlg.visible = false;
                }
            },
            SwingButton {
                text: "Cancel"
                defaultCancelButton: true
                action: function():Void {
                    self.sourcePath = oldSourcePath;
                    dlg.visible = false;
                }
            }]
             * ***********/
        };
        
        
        // BEGIN SWING WORK AROUND
        dlg.getJDialog().setModal(true);
        
        
        var b = SwingButton {
                text: "OK"
                action: function():Void {
                    if(self.action != null) 
                        self.action(self.sourcePath);
                    dlg.visible=false;
                }  
        };
        dlg.getJDialog().add(b.getJComponent());
        b = SwingButton {
            text: "Cancel"
            // TODO defaultCancelButton: true
            action: function():Void {
                self.sourcePath = oldSourcePath;
                dlg.visible=false;
            }
        };
        dlg.getJDialog().add(b.getJComponent());
        dlg.visible = true;
        // END SWING WORK AROUND
        dlg;        
    }
                
  
    function add(w:Component):Void {
        var fc:FileChooser;
        fc = FileChooser {
            title: "Source Path"
            files: true
            directories: true
            fileFilters:
            [FileFilter {
                filter: function(f: File):Boolean {
                    return f.isDirectory();
                }
                description: "Source Directories"
            },
            FileFilter {
                filter: function(f:File):Boolean {
                    if (f.isDirectory()) {
                        return true;
                    }
                    var name = f.getName().toUpperCase();
                    return name.endsWith(".ZIP") or name.endsWith(".JAR");
                }
                description: "Archive Files (*.zip, *.jar)"
            }]
            action: function(selectedFile:File):Void {
                if (selectedSourcePath < 0) {
                    insert selectedFile into sourcePath;
                    selectedSourcePath = 0;
                } else {
                    var i = selectedSourcePath;
                    insert selectedFile before sourcePath[selectedSourcePath];
                    selectedSourcePath = i;
                }
            }
        };
        fc.showOpenDialog(w);
  
    }
    function remove():Void {
        if(selectedSourcePath >= 0 and selectedSourcePath < sizeof sourcePath) {
            var i = selectedSourcePath;
            delete sourcePath[selectedSourcePath];
            if (i == sizeof sourcePath) {
                i--;
            }
            selectedSourcePath = i;
        }
    }
    function edit(w:Component):Void {
        var fc:FileChooser;
        fc = FileChooser {
            title: "Source Path"
            files: true
            directories: true
            fileFilters:
            [FileFilter {
                filter: function(f: File):Boolean {return f.isDirectory();}
                description: "Source Directories"
            },
            FileFilter {
                filter: function(f: File):Boolean {
                    if (f.isDirectory()) {return true;}
                    var name = f.getName().toUpperCase();
                    return name.endsWith(".ZIP") or name.endsWith(".JAR");
                }
                description: "Archive Files (*.zip, *.jar)"
            }]
            action: function(selectedFile:File):Void {
                sourcePath[selectedSourcePath] = selectedFile;
            }
            cwd: sourcePath[selectedSourcePath]
        };
        fc.showOpenDialog(w);
    }
    

}
