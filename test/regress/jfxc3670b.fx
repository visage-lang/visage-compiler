/**
 * Regression test: JFXC-3670 : Compiled bind: bind permeates object literal initializers
 *
 * @test
 * @run
 */

class Baffle { 
  var moe : Integer;
  def id = ++bafCnt;
  init { println("Create {this}") }
  override function toString() : String { "Baffle#{id}:{moe}" }
}
class Nerf { 
  var cnt : Integer;
  var baf : Baffle;
  def id = ++nerfCnt;
  init { println("Create {this}") }
  override function toString() : String { "Nerf#{id}:cnt={cnt},baf={baf}" }
}
var bafCnt = 0;
var nerfCnt = 0;
var x = 2;
function foo(k : Integer) { println("foo({k})"); k }
var zzz = bind Nerf { cnt: x baf: Baffle { moe: 4 } }
var dummy = zzz;
++x;
dummy = zzz;
++x;
dummy = zzz;
