/**
 * Regression test: JFXC-3619 : Compiled bind: dependent state/mode -- non-bound initalizers of bound object literal
 *
 * Identifier reference to same level (instance) var in bound object literal from non-bound initializer - script-level
 *
 * @test
 * @run
 */

class jfxc3619identScript {
  var a : Integer
}

    var x = 10;
    def ol = bind jfxc3619identScript { a: x }
  
    println(ol.a);
    --x;
    println(ol.a);
    --x;
    println(ol.a);
    --x;
    println(ol.a);
