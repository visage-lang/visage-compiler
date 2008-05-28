
package assortis.core.ui;


import javafx.gui.*;

import java.lang.Object;

public class MyTreeCell {
    
    public attribute text: String;
    public attribute cells: MyTreeCell[];
    public attribute value: Object;
    
    public function toString ():String { return text;  } 
    
}