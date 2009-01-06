/**
 * JFXC-2602 : Overriding a protected var in a different package fails
 *
 # @compilefirst sub2602/jfxc2602base.fx
 * @test
 */

import sub2602.jfxc2602base;
class jfxc2602 extends jfxc2602base {
  override var minimumWidth = 1; 
}

