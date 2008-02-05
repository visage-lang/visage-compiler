/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package basicdnd;

import javafx.ui.*;
import java.awt.Dimension;
import java.lang.System;

/**
 * @author jclarke
 */

var textArea:TextArea;
var textField:TextField;
var list:ListBox;
var table:Table;
var tree:Tree;
var colorChooser:ColorChooser;

Frame {
    title: "JavaFX BasicDND"
    onClose: function() {System.exit(0);}
    visible: true  
    menubar: MenuBar {
        menus: [ Menu {
                text: 'File'
                mnemonic: KeyStroke.F
                items: [
                    MenuSeparator{},
                    MenuItem {
                        text: 'Exit'
                        mnemonic: KeyStroke.X
                        action: function() {
                             java.lang.System.exit(0);
                         }

                    }
                ]
            },
            Menu {
                text: 'Edit'
                mnemonic: KeyStroke.E
                items: [
                    MenuItem.cutMenuItem(),
                    MenuItem.copyMenuItem(),
                    MenuItem.pasteMenuItem()
                ]
            }
            ]
    }
    content: BorderPanel {
        border: EmptyBorder {top: 5 , left: 5, bottom: 5, right: 5}
        center: SplitPane {
            content: [
                SplitView {
                    content: Box {
                        orientation: Orientation.PAGE
                        border: EmptyBorder {top: 5 , left: 5, bottom: 5, right: 5}
                        content:[
                            BorderPanel {
                                border: TitledBorder { title: "JTable"}
                                center: table = Table {
                                    //dropMode: DropMode.INSERT
                                    dropMode: DropMode.ON
                                    columns: [
                                    TableColumn{text:"Column 0"},
                                    TableColumn{text:"Column 1"},
                                    TableColumn{text:"Column 2"},
                                    TableColumn{text:"Column 3"}
                                    ]
                                    cells:[
                                    TableCell{text:"Table 00"},
                                    TableCell{text:"Table 01"},
                                    TableCell{text:"Table 02"},
                                    TableCell{text:"Table 03"},

                                    TableCell{text:"Table 10"},
                                    TableCell{text:"Table 11"},
                                    TableCell{text:"Table 12"},
                                    TableCell{text:"Table 13"},

                                    TableCell{text:"Table 20"},
                                    TableCell{text:"Table 21"},
                                    TableCell{text:"Table 22"},
                                    TableCell{text:"Table 23"},

                                    TableCell{text:"Table 30"},
                                    TableCell{text:"Table 31"},
                                    TableCell{text:"Table 32"},
                                    TableCell{text:"Table 33"}

                                    ]
                                } 
                            },
                            BorderPanel {
                                border: TitledBorder { title: "JColorChooser"}
                                center: colorChooser = ColorChooser{ }
                            }                
                        ]
                    }
               },
               SplitView {
                    content: Box {
                        orientation: Orientation.PAGE
                        border: EmptyBorder {top: 5 , left: 5, bottom: 5, right: 5}
                        content:[
                            BorderPanel {
                                border: TitledBorder { title: "JTextField"}
                                center: textField = TextField {
                                    columns: 30
                                    enableDND: false // on by default
                                    dropMode: DropMode.INSERT
                                    text: "Favorite foods:\nPizza, Moussaka, Pot roast"
                                }
                            },   
                            BorderPanel {
                                border: TitledBorder { title: "JTextArea"}
                                center: textArea = TextArea {
                                    rows: 5
                                    columns: 30
                                    enableDND: false // on by default
                                    dropMode: DropMode.INSERT
                                    text: "Favorite shows:\nBuffy, Alias, Angel"
                                }
                            }, 
                            BorderPanel {
                                border: TitledBorder { title: "JList"}
                                center: list = ListBox {
                                    preferredSize: new Dimension(300, 100)
                                    dropMode: DropMode.ON_OR_INSERT
                                    cells: [
                                    ListCell{text: "Martha Washington"},
                                    ListCell{text: "Abigail Adams"},
                                    ListCell{text: "Martha Randolph"},
                                    ListCell{text: "Dolley Madison"},
                                    ListCell{text: "Elizabeth Monroe"},
                                    ListCell{text: "Louisa Adams"},
                                    ListCell{text: "Emily Donelson"},
                                    ]
                                }
                            },    
                            BorderPanel {
                                border: TitledBorder { title: "JTree"}
                                center: tree = Tree {
                                    preferredSize: new Dimension(300, 100)
                                    showRootHandles: true
                                    rootVisible: true
                                    dropMode: DropMode.ON_OR_INSERT
                                    root: TreeCell {
                                        text: "Mia Familia"
                                        cells: [
                                        TreeCell {
                                            text: "Sharon"
                                            cells: [
                                            TreeCell {
                                                text: "Maya"
                                                cells: [
                                                TreeCell {
                                                    text: "Muffin"
                                                },
                                                TreeCell {
                                                    text: "Winky"
                                                }
                                                ]
                                            },
                                            TreeCell {
                                                text: "Anya"
                                            },
                                            TreeCell {
                                                text: "Bongo"
                                            },
                                            ]
                                        }
                                        ]
                                    }
                                }

                            }                 
                        ]            
                    }// Box
                }// SplitView
               ]
            } // SplitPane
        pageEnd: CheckBox {
                 text: "Turn on Drag and Drop"
                 onChange: function(toggle:Boolean):Void {
                    textArea.enableDND = toggle;
                    textField.enableDND = toggle;
                    list.enableDND = toggle;
                    table.enableDND = toggle;
                    tree.enableDND = toggle;
                    colorChooser.enableDND = toggle;
                }
            }
     }
}