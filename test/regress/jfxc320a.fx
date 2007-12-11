/* JFXC-320a: cannot locate another method in same class
 * @test
 * @run
 */
import java.lang.Object;
class jfxc320a {
    attribute inUpdateSelection: Boolean;
    public function updateSelection(oldIndex:Integer, newIndex:Integer):Void {
    }

    public attribute tabs: Object[]
        on insert [ndx] (t) {

    };

    public attribute selectedIndex: Number = -1 on replace (old)  {
                updateSelection(1.0, selectedIndex);
    };

}