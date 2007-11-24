/* JFXC-252:  Bad attribution error. The Button was not considered of Widget type.
 * @test
 */
import javafx.ui.*; 
import javax.swing.JComponent; 
import javax.swing.JLabel; 
import java.lang.System; 

public class Label extends Widget { 
    attribute jlabel: JLabel = new JLabel(); 
    attribute text: String = "" on replace { 
        jlabel.setText(text); 
    } 
    public function createComponent():JComponent { 
        return jlabel; 
    } 
} 

Frame { 
    title: "Hello" 
    height: 200 
    width: 300 
    centerOnScreen: true 
    visible: true 
    content: Label {text: "Hello World"} 
}
