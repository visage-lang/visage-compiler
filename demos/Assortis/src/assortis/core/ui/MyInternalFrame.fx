package assortis.core.ui;

import javafx.gui.*;
import javafx.gui.swing.*;
import javax.swing.*;

import java.lang.System;

public class MyInternalFrame extends Component{
    
    private attribute internalFrame: JInternalFrame;

    public attribute rootPane: MyDesktopPane;

    //private attribute updateComponentFlag: Boolean = false;
    
    
    public attribute menus: Menu[] on replace oldMenus[a..b] = newSlice{
        var menubar = internalFrame.getJMenuBar();
        if (menubar == null){
            menubar = new JMenuBar();
            internalFrame.setJMenuBar(menubar);
        }
        for (menu in oldMenus[a..b]) {
            menubar.remove(menu.getJMenu());
        }
        
        for (menu in newSlice) {
            menubar.add(menu.getJMenu());
        }
        
    }


    public attribute title: String on replace{
        internalFrame.setTitle(title);
    };
    
    public attribute selected: Boolean on replace{
        //System.out.println("[internal frame] '{title}' selected: {selected}");
            if( not selected){
                internalFrame.setSelected(false);
            }else{
                for ( f in rootPane.frames[fr| fr <> this]){
                    f.selected = false; 
                }
                internalFrame.setSelected(true);
                //System.out.println("    selected: {internalFrame.isSelected()}");

            }
    };
    
    public attribute background: Color;
    
    public attribute content: Component on replace{
        if(content <> null){
            internalFrame.setContentPane(content.getJComponent());
            //internalFrame.repaint();
        }
    };
    
    public attribute onClose: function();
    
    
//    private function updateComponentSelection( selectedFlag: Boolean){
//        updateComponentFlag = true;
//        System.out.println("    selected: {selected}");
//        selected = selectedFlag;
//        updateComponentFlag = false;
//    }
    
    
    protected function createJComponent(): JComponent {
        internalFrame = new JInternalFrame();
        internalFrame.setResizable(true);
        internalFrame.setClosable(true);
        
        
        var listener = javax.swing.event.InternalFrameAdapter {
            public function internalFrameActivated(e:javax.swing.event.InternalFrameEvent):Void {
                //System.out.println("\n\n[internal frame] frame: {title} - activated()");
                selected = true;
            }

            public function internalFrameClosing(e: javax.swing.event.InternalFrameEvent):Void {
                if(onClose <> null) {
                    onClose();
                }
                rootPane.getJComponent().repaint();
            }

        };
        
        internalFrame.addInternalFrameListener(listener);
        
        return internalFrame;
    }
    
    
}
