/**
 * JFXC-2780 : Can't compile when "bind rect.boundsInLocal.minX" in a function
 *
 * @subtest
 */

public class Jfxc2780Rect {
  public var boundsInLocal : Rect;
  public var x : Number;
  public def minX : Number = 3.0;
}
