/*
 * JFXC-3478 : Lazy bound comprehension causes an NPE
 *
 * @test
 * @run
 * 
 */

class Rectangle {
  override function toString() : String { "Rectangle" }
}

var sprites = bind lazy for (i in [1..2]) {
                            Rectangle {}
                        } 
println(sprites);
