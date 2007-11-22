/* JFXC-282:  Bad attribution error. The Button was not considered of Widget type.
 * !test disabled. regress test don't have the RTL on their class path
 */
import javafx.ui.*; 

Frame { 
  content: Button { 
     text: "Test" 
  }; 
  visible: true 
}  
