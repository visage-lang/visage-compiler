/* JFXC-376:  Build failure at SVN 1127
 * @test
 */
package javafx.ui.canvas;

import javafx.ui.*;
import javafx.ui.canvas.*;

class jfxc376 extends Transformable {
    function getNode() : Void {
        if(transform != null and sizeof transform > 0) {
             for(t in transform) {
                 t.transformable = this;
             }
        }
    }
}
