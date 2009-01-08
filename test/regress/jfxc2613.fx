/**
 * JFXC-2613 : protected access not properly analyzed in the face of multiple inheritance
 *
 * @compilefirst sub2602/jfxc2602base.fx
 * @test
 */

import sub2602.jfxc2602base;

class jfxc2613 extends java.util.ArrayList, jfxc2602base {
  function foo() { minimumWidth = 1; }
}

