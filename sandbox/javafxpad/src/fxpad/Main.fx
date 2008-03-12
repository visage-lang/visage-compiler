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
import java.lang.System;

var row1     = Row {alignment: Alignment.LEADING};
var row2     = Row {resizable: true};
var row      =  Row {alignment: Alignment.BASELINE};
var col      = Column {resizable: true};
var labelCol = Column{};
var fieldCol = Column {resizable: true};
var butCol   = Column{};
var zoomCol  = Column{};

var javafxPad = JavaFXPad{};

Frame {
    visible: true
    onClose: function() { System.exit(0); }
    title: "JavaFXPad"
    height: 800
    width: 1000
    menubar: MenuBar {
        menus: [
            Menu {
                text: "File"
                mnemonic: KeyStroke.F
                items: [
                    MenuItem {
                        text: "Open"
                        mnemonic: KeyStroke.O
                        action: function() {
                            // do open
                            System.out.println("Open()");
                        }
                    },
                    MenuItem {
                        text: "Save"
                        mnemonic: KeyStroke.S
                        accelerator: Accelerator{modifier: KeyModifier.COMMAND, keyStroke: KeyStroke.S}
                        //enabled: bind fileExists(javafxPad.url)
                        action: function() {
                            // do save
                            System.out.println("Save()");
                        }
                    },
                    MenuItem {
                        text: "Save As"
                        mnemonic: KeyStroke.A
                        action: function() {
                            // do save as
                            System.out.println("SaveAs()");
                        }
                    },
                    MenuSeparator {},
                    MenuItem {
                        text: "Exit"
                        mnemonic: KeyStroke.X
                        action: function() {
                            System.exit(0);
                        }
                    }
                ]
            },
            Menu {
                text: "Edit"
                mnemonic: KeyStroke.E
                items: [
                    MenuItem {
                        text: "Find"
                        mnemonic: KeyStroke.F
                        accelerator: Accelerator{modifier: KeyModifier.COMMAND, keyStroke: KeyStroke.F}
                        action: function() {
                            System.out.println("doSearch()");
                        }
                    }
                ] 
            },
            Menu {
                text: "Run"
                mnemonic: KeyStroke.R
                items: [
 
                    RadioButtonMenuItem {
                        mnemonic: KeyStroke.U
                        text: "Run Automatically"
                        //selected: bind javafxPad.runAutomatically
                    },
                    MenuItem {
                        mnemonic: KeyStroke.R
                        text: "Run"
                        //enabled: bind not javafxPad.runAutomatically and javafxPad.isValid()
                        action: function() {
                            System.out.println("runNow()");
                        }
                    },
                    MenuItem {
                        mnemonic: KeyStroke.S
                        text: "Source Path..."
                        action: function() {
                            System.out.println("sourcePath()");
                        }
                    }                    
                ]
            }
            
        ]
    }
    content: GroupPanel {
        rows: [row1, row2]
        columns: [col]
        content: [
            GroupLayout {
                row:row1
                column: col 
                rows: [row]
                columns: [labelCol, fieldCol, butCol, zoomCol]
                content: [
                    SimpleLabel {
                        row: row
                        column: labelCol
                        text: "Location:"
                    } as GroupElement,
                    TextField {
                        row: row
                        column: fieldCol
                        columns: 60
                        //value: bind javafxPad.url
                        action: function() {
                            //javafxPad.go();
                        }
                    } as GroupElement, 
                    Button {
                        row: row
                        column: butCol
                        text: "Go"
                        action: function() {
                            //javafxPad.go();
                        }
                    } as GroupElement,
                    ComboBox {
                        row: row
                        column: zoomCol
                        //selection: bind javafxPad.zoomSelection
                        //cells: bind for (i in javafxPad.zoomOptions)
                        //   ComboBoxCell { text: "{i}%" }
                    } as GroupElement                   
                ]
            },
            BorderPanel {
                row:row2
                column: col
                center: javafxPad
            } as GroupElement
        ]
    }
    
    
}