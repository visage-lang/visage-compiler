/*
 * JFXC-1905 : Compiler rejects a legal override of a inherited function
 * @compilefirst ../pack1/jfxc1905Sup.fx
 * @test/fail
 *
 */

package pack2;
import pack1.jfxc1905Sup;


class jfxc1905Sub extends jfxc1905Sup{

protected function fn01(fparg:function(:Integer,:Number):java.lang.Double):java.lang.Double{
return 30.0;
}

override protected function fn02(a1:Number,a2:Integer):Number{
return 25.60;
}

} 
