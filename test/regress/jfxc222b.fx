/*
 * Regression test: JFXC-222: Invoke superclass methods with super.foo()
 *
 * @test
 * @run
 */
import javax.swing.JButton; 

public class But extends JButton { 
    public function getText(): String { 
        JButton.getText(); 
    } 
}
