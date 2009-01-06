/**
 * JFXC-2602 : Overriding a protected var in a different package fails
 *
 * @subtest
 */

package sub2602;
public class jfxc2602base {
  public-read protected var minimumWidth:Number = 0; 
}
