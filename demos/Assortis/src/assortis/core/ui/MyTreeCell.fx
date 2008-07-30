
package assortis.core.ui;



import java.lang.Object;

public class MyTreeCell {
    
    public attribute text: String;
    public attribute cells: MyTreeCell[];
    public attribute value: Object;
    
    override function toString ():String { return text;  } 
    
}
