/*
 * Regression test for bug JFXC-155
 * @test
 * @run
 */
import java.awt.*; 
import java.lang.System; 

public abstract class Node { 
    public var opacity: Number = 1; 
} 

public class AbstractVisualNode extends Node { 
    public var fill: Paint = null; 
    public var stroke: Paint = null; 
} 

public class VisualNode extends Node { 
} 

//TODO: extends order should be VisualNode first (JFXC-675)
public class Shape extends AbstractVisualNode, VisualNode  { 
} 

public class Rect extends Shape { 
    public var x: Number; 
    public var y: Number; 
    public var width: Number; 
    public var height: Number; 
} 

public class Text extends Shape { 
    //var fill:Paint = Color.black; 
    init { fill = Color.black }
} 

public class MyRect extends Rect { 
    //var fill:Paint = new Color(0, 0, 255); 
    init { fill = new Color(0, 0, 255) }
} 

function run() {
    var rect = MyRect { }
    var t = Text { }
}


