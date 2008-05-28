/* JFXC-253:  cast of ObjLiteral implementing Java class.
 * @test
 * @run
 */
import javax.swing.Action;
import javax.swing.JButton;

class Bar {
    attribute jbr : javax.swing.JButton = new javax.swing.JButton;
    attribute actionMap = jbr.getActionMap();
    attribute i : javax.swing.Action;
    function bar() : Void {
        actionMap.put(i, javax.swing.AbstractAction { 
                            public function isEnabled():Boolean { 
                                return true; 
                            } 
                            public function actionPerformed(e:java.awt.event.ActionEvent):Void { 
                                
                            } 
                        } as javax.swing.Action); 
    }
}
