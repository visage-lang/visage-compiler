package javafx.dev;

/**
 * @author Alexandr Scherbatiy, alexsch@dev.java.net
 */

import javafx.ext.swing.*;
import java.lang.System;


public class Palette extends DevComponent{
    public var items: PaletteItem[];

    public var selectedItem: PaletteItem;


    public var selectedIndex: Integer on replace{
        selectedItem  = items[selectedIndex];
    };
    
    override var dragEnable = true;
    
    override var drag = function(): java.lang.Object {
        System.out.println("[palette] Drag");
        return selectedItem.value;
    }
    
    protected function composeComponent(): Component {
        return List{
            items: bind for (item in items)
                ListItem{
                    text: item.name
                }
             selectedIndex: bind selectedIndex with inverse
        }
    } 

}
