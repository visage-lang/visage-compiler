/* Binding Overhaul test: indexof
 *
 * @test
 * @run
 */

import java.lang.System;

var enableBindingOverhaul;

function xxxxx() {
  var modvalue = 2;
  var xs = [1..5];
  var bfseq = bind for (x in xs where x mod (indexof x + 1) == 0) [ "bar", "foo" ];
  var iseq = bind for (x in xs) x*indexof x;
  var sseq = bind for (x in ["be", "bop", "bong", "bip"]) indexof x;
  var bseq = bind for (x in [true, true, false, true, false, false]) if (x) indexof x else 1000 * indexof x;

  System.out.println(bfseq);
  System.out.println(iseq);
  System.out.println(sseq);
  System.out.println(bseq);

  insert 88 into xs;

  System.out.println(bfseq);
  System.out.println(iseq);

  delete xs[0];

  System.out.println(bfseq);
  System.out.println(iseq);
}

xxxxx()
