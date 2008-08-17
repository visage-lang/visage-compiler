/**
 * @subtest
 */

public class jfxc1629One {

  public-readable var twub = 333;

  function make() : jfxc1629One {
     twub = 444; // OK
     jfxc1629One {twub: 111} // OK
  }
}