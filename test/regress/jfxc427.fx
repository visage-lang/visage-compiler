/*
 * JFXC-427 : demonstrate reference to objects under construction without 'var: self'
 *
 * @test
 * @run
 */

import java.lang.System;

class Frame {
  var content : Stuff[];
  public function toString() : String {
     "Frame"
  }
}

class Stuff {
  var name : String;
  var parent : Frame;
  public function toString() : String {
     "({name}:{parent})"
  }
}

var frame : Frame;
frame =
Frame {
  content: [
    Stuff {
      name: "first"
      parent: bind frame
    },
    Stuff {
      name: "second"
      parent: bind frame
    }
  ]
}

System.out.println( frame.content )
