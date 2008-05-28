package assortis.core.ui;


import javafx.gui.*;
import javax.swing.*;

import java.lang.System;



public class MyDesktopPane extends Component {
    
    
    private attribute desktopPane: JDesktopPane;
        
    public attribute frames: MyInternalFrame[]  on replace oldValue[lo..hi] = newVals{
        //System.out.println("[desktop pane] add internal frame");
        for (frame in oldValue[lo..hi]){
            desktopPane.remove(frame.getJComponent());
        }
        for (frame in newVals){
            //System.out.println("[desktop pane] frame: {frame.title}, visible: {frame.visible}");
            frame.rootPane = this;
            var rect = new java.awt.Rectangle(frame.x, frame.y, frame.width, frame.height);
            var comp = frame.getJComponent();
            comp.setBounds(rect);
            desktopPane.add(comp, 0, 0);
        }
        
    };
    
    protected function createJComponent(): JComponent {
        desktopPane = new JDesktopPane();
        return desktopPane;
    }
}