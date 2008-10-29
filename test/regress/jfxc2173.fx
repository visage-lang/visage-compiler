/**
 * Regression test JFXC-2173 : override attribute syntax doesn't 
 *   provide correct type info to replace trigger
 *
 * @test
 * @compile jfxc2173.fx 
 */
public class Superclass {
  var nums:Integer[]; // a sequence
}

public class Subclass extends Superclass {
  override var nums on replace oldVal[lo..hi]=newVals {
    // the following used to be a compiler error, but
    // should not be. "newVals" should be inferred to be
    // a Integer sequence.
    var firstNewValue=newVals[0];   
  }
}
