/**
 * JFXC-3711 : Compiled-bind: object literals locals are not lowered
 *
 * @test
 */

var x = 0;
var obj = Object {
   def v = x +=1;
}
