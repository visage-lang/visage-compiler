/**
 * JFXC-3701 : compiled-bind: non-sequence var with bound sequence initializer causes compiler crash
 *
 * @test
 */

var seq:Object[] = [1,2,3,4];
var x:Object = bind seq[idx | idx mod 2 == 0][0];
