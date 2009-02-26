/**
 * JFXC-2780 : Can't compile when "bind rect.boundsInLocal.minX" in a function
 *
 * @subtest
 */

public class jfxc2780Rect {
  public var boundsInLocal : jfxc2780Rect;
  public var x : Number;
  public def minX : Number = 3.0;
}
