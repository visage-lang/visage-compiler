package javafx.dev;

/**
 * @author Alexandr Scherbatiy, alexsch@dev.java.net
 */

import javafx.gui.*;
import java.lang.System;


public class Palette extends DevComponent{
    public attribute items: PaletteItem[];

    public attribute selectedItem: PaletteItem;


    public attribute selectedIndex: Integer on replace{
        selectedItem  = items[selectedIndex];
    };
    
    override attribute dragEnable = true;
    
    override attribute drag = function(): java.lang.Object {
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