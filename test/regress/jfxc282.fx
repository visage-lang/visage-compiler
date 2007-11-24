/* JFXC-282:  Bad attribution error. The Button was not considered of Widget type.
 * @test
 */
import javafx.ui.*; 

Frame { 
  content: Button { 
     text: "Test" 
  }; 
  visible: true 
}  
