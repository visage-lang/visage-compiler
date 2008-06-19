package assortis.sources.language.javafx.api.gui;

import javafx.gui.*;
import javafx.gui.swing.*;
import java.lang.System;

function menuAction(text: String): function() { function() {System.out.println(text);} }

Frame{
    menus: [
        Menu{
            text: "File"
            items: [
                MenuItem{ text: "New File..."  action: menuAction("Create new file...")},
                MenuItem{ text: "Open File..." action: menuAction("Open file...")},
                MenuItem{ text: "Exit"         action: menuAction("Exit")}
            ]
        }, Menu{
            text: "Edit"
            items: [
                MenuItem{ text: "Undo"          action: menuAction("Undo...")},
                MenuItem{ text: "Redo"          action: menuAction("Redo...")},
                MenuItem{ text: "Cut"           action: menuAction("Cut...")},
                MenuItem{ text: "Copy"          action: menuAction("Copy...")},
                MenuItem{ text: "Paste"         action: menuAction("Paste...")}
            ]
        }
    ]
}
