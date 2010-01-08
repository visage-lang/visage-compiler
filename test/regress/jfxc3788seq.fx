/*
 * Regression test 
 * JFXC-3788 : compiled-bind: recursive onReplace call on bound sequence
 *
 * sequence for-loop body
 *
 * @test
 * @run
 */

var onrCnt = 0;
var seen = [false, false, false];

var x = 1;
var content:Object[] = bind for (i in [1,2,3]) [ x + i ]
      on replace [a..b] = newV {
        ++onrCnt;
        // println("replace: [{a}..{b}] = {sizeof newV}"); 
        if (onrCnt == 1) {
           if (a != 0 or b != -1 or sizeof newV != 3) 
             println("bad initial replace: [{a}..{b}] = {sizeof newV}"); 
        } else {
           if (a != b) 
             println("expected single element replace, got {a}..{b}");
           if (sizeof newV != 1)
             println("expected sizeof newV==1, got {sizeof newV}");
           if (seen[a])
             println("duplicate on-replace -- {a}");
           seen[a] = true;
        }
        for(z in content) z; 
      }; 
x = 42;
if (onrCnt != 4) {
  println("expected four on-replace calls, got {onrCnt}");
}
