/* JFXC-211: jfxc211b test -- Cannot locate attribute declared in a super class with Mulitple Inheritence
 *
 * @test
 * @run
 */
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public abstract class jfxc211Widget {}

public abstract class jfxc211ActionWidget extends jfxc211Widget {
    /**
     * Function which implements this widget's action.
     */
    public attribute action: function():Void;
}

class jfxc211Button extends jfxc211Widget, jfxc211ActionWidget {

    init {
        java.awt.event.ActionListener {
            public function actionPerformed(e:java.awt.event.ActionEvent) {
                if(action <> null) {
                   action();
                }
            }
        };        
    }
}