/*
 * Regression test for bug JFXC-155
 * @test
 * @run
 */
import java.awt.*; 
import java.lang.System; 

public abstract class Node { 
    public attribute opacity: Number = 1; 
} 

public class AbstractVisualNode extends Node { 
    public attribute fill: Paint = null; 
    public attribute stroke: Paint = null; 
} 

public class VisualNode extends Node { 
} 


public class Shape extends VisualNode, AbstractVisualNode { 
} 

public class Rect extends Shape { 
    public attribute x: Number; 
    public attribute y: Number; 
    public attribute width: Number; 
    public attribute height: Number; 
} 

var rect = MyRect { 
}; 

var t = Text { 
}; 


public class Text extends Shape { 
    attribute fill:Paint = Color.black; 
} 

public class MyRect extends Rect { 
    attribute fill:Paint = new Color(0, 0, 255); 
} 
