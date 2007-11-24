/* JFXC-278:  bad class file error when bound function generation is enabled.
 *
 * @test
 */
import javafx.ui.*; 

Frame { 
   context: Button { 
      text: "Test" 
   } as com.sun.javafx.api.ui.UIContext;
   visible: true 
} 
 
