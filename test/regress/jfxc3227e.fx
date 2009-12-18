/**
 * JFXC-3227 : Compiler should always enforce sequence flattening
 *
 * @test
 * @run
 */

function foo(x:Object) {
   var seq:Object[] = [0];
   insert x into seq;
   println(seq);
}

foo([1,2,3]);
