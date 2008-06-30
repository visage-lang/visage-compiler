/*
 * FXMenu.fx
 *
 * Created on Mar 13, 2008, 12:01:42 PM
 */

package jfx.assortis.lang.api.widgets.menu;


import javafx.ui.*;

Frame{
    menubar: MenuBar{
        menus: [
        Menu{
            text: "File"
            items: [
            MenuItem{
                text: "New File..."
            },
            MenuItem{
                text: "Open File..."
            },
            MenuSeparator{},
            MenuItem{
                text: "Exit"
            },
            ]
        },
        Menu{
            text: "Edit"
            items: [
            MenuItem{
                text: "Undo"
            },
            MenuItem{
                text: "Redo"
            },
            MenuSeparator{},
            MenuItem{
                text: "Cut"
            },
            MenuItem{
                text: "Copy"
            },
            MenuItem{
                text: "Paste"
            },
            ]
        }
        
        ]
    }
}
