/*
 * JFXC-3728 : Compiled bind: bind-with-inverse of sequence
 *
 * Select of Integer.  On-replace on bindee.
 *
 * @test
 * @run
 * 
 */

class A {
  var bse : Integer[] 
       on replace [a..b] = nv { println("[{a}..{b}] = {sizeof nv}") }
}

class B {
  def a = A{};
  var bsr = bind a.bse with inverse;
  
  function show() {
    print("a.bse: ");
    println(a.bse);
    print("bsr: ");
    println(bsr);
  }
  
  function testa() {
    insert 45 into a.bse;
    insert 1000 into a.bse;
    insert [1..5] into a.bse;
    a.bse[5] = 99;
    delete a.bse[4];
    show();
    delete a.bse;
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
