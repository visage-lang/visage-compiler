/* JFXC-278:  bad class file error when bound function generation is enabled.
 * !test disabled. regress test don't have the RTL on their class path
 */
import javafx.ui.*; 

Frame { 
   context: Button { 
      text: "Test" 
   } as com.sun.javafx.api.ui.UIContext;
   visible: true 
} 
 
