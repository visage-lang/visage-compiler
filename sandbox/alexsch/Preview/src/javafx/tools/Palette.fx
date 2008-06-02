/*
 * Palette.fx
 *
 * Created on 31.05.2008, 15:13:06
 */

package javafx.tools;

import javafx.gui.*;
import java.lang.System;


public class Palette extends ToolComponent{
    public attribute items: PaletteItem[];

    public attribute selectedItem: PaletteItem;


    public attribute selectedIndex: Integer on replace{
        //System.out.println("[palette] selected item: {items[selectedIndex]}");
        //drag(items[selectedIndex]);
        selectedItem  = items[selectedIndex];
        
    };
    
    
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