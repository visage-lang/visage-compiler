/*
 * jfxc624.fx
 * @test/fail
 * Note: when fixed should be a plain compile test
 *
 * Note to mobile: Sorry about the SE code,but per comment:
 "The fact that "extends java.lang.Object" activates the bug suggests
  that it is related to different handling between plain classes and
  compound classes."
 * If this is simplified to use compound classes,ie., extend an fx class,
 * it degenerates to jfxc1253 test case.
 */

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Test extends java.lang.Object {

    var tickListener : ActionListener = ActionListener {
        override
        public function actionPerformed(evt:ActionEvent): Void {
            update();
        }
    }    
    
    public function update(): Void {
    }
}
