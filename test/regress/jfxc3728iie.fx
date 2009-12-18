/*
 * JFXC-3728 : Compiled bind: bind-with-inverse of sequence
 *
 * Direct bind of Integer.  On-replace on bindee.
 *
 * @test
 * @run
 * 
 */

class B {
  var bse : Integer[]
       on replace [a..b] = nv { println("[{a}..{b}] = {sizeof nv}") };
  var bsr = bind bse with inverse;
  
  function show() {
    print("bse: ");
    println(bse);
    print("bsr: ");
    println(bsr);
  }
  
  function testa() {
    insert 45 into bse;
    insert 1000 into bse;
    insert [1..5] into bse;
    bse[5] = 99;
    delete bse[4];
    show();
    delete bse;
    show();
  }

  function testb() {
    insert 45 into bsr;
    insert 1000 into bsr;
    insert [1..5] into bsr;
    bsr[5] = 99;
    delete bsr[4];
    show();
    delete bsr;
    show();
  }
}
B{}.testa();
B{}.testb();
