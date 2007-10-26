/*
 * Regression test: jfxc159
 *
 * @test
 * @run
 */

public class Foo { 
    protected attribute keyMap:java.util.Map = new java.util.HashMap(); 

    public function getKeyStroke(id:Number): Bar { 
        return keyMap.get(id) as Bar; 
    } 
    public function putKeyStroke(id:Number, key:Bar) { 
        keyMap.put(id, key); 
    } 
} 

public class Bar { 
    public attribute foo:Foo; 
    public attribute foobar:Number on replace { 
        foo.putKeyStroke(foobar, this); 
   } 
} 
