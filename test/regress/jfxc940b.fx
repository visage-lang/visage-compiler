/**
 * regression test: JFXC-940 : Make the generated scope of a local variable declaration  * be the entire block in which it is defined.
 *
 * @test
 * @run
 */

import java.lang.System;

class Frame {
  var content : Stuff;
}

class Stuff {
  var parent : Frame;
  var name: String;
}        
        
var sframe:Frame = Frame {
  content:   
    Stuff { 
      name: "first"
      parent: bind  sframe
    }
}   

    
System.out.println( sframe.content.name);

var oldContent = sframe.content;

sframe.content = Stuff {name: "second" parent: bind sframe};

System.out.println(sframe.content.name);
