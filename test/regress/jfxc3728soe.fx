/*
 * JFXC-3728 : Compiled bind: bind-with-inverse of sequence
 *
 * Select of object.  On-replace on bindee.
 *
 * @test
 * @run
 * 
 */

class A {
  var bse : Object[] 
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
    insert "gulp" into a.bse;
    insert [1..5] into a.bse;
    delete a.bse[4];
    delete a.bse;
    show();
  }

  function testb() {
    insert 45 into bsr;
    insert "gulp" into bsr;
    insert [1..5] into bsr;
    delete bsr[4];
    delete bsr;
    show();
  }
}
B{}.testa();
B{}.testb();