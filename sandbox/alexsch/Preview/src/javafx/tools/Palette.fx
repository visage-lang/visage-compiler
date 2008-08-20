/*
 * Palette.fx
 *
 * Created on 31.05.2008, 15:13:06
 */

package javafx.tools;

import javafx.gui.*;
import java.lang.System;


public class Palette extends ToolComponent{
    public var items: PaletteItem[];

    public var selectedItem: PaletteItem;


    public var selectedIndex: Integer on replace{
        selectedItem  = items[selectedIndex];
    };
    
    
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
