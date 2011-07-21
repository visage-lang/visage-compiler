/*
 * JFXC-4221 : Diamond shaped dependencies cause invalidations to be lost
 *
 * "this" references handled 
 *
 * @test
 * @run
 */

class jfxc4221this {
  var z : Object = "glup";
  var bb = bind z == this and this == z on replace { println("bb: {bb}") }
  function f() {
    z = this;
  }
}

jfxc4221this{}.f()
