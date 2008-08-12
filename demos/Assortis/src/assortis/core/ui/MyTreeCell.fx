
package assortis.core.ui;



import java.lang.Object;

public class MyTreeCell {
    
    public var text: String;
    public var cells: MyTreeCell[];
    public var value: Object;
    
    override function toString ():String { return text;  } 
    
}
