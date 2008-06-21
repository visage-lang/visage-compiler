
package javafx.dev;

/**
 * @author Alexandr Scherbatiy, alexsch@dev.java.net
 */

 import javafx.gui.*;
import javafx.dev.dnd.*;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JComponent;

import java.lang.System;

import javax.swing.TransferHandler;
import java.awt.datatransfer.Transferable;


public abstract class DevComponent extends Component{
    
    public abstract function composeComponent(): Component;        

    
    public attribute dragEnable: Boolean = false on replace{
        if(dragEnable){ ComponentDragSource{ component: this } }
    };
    
    public attribute dropEnable: Boolean = false on replace{
        if(dropEnable){ ComponentDropTarget{ component: this } }
    };
    
    private attribute mouseListener: java.awt.event.MouseListener;
    private attribute motionListener: java.awt.event.MouseMotionListener;
    
    public attribute drag: function():java.lang.Object; 
    public attribute drop: function(value: java.lang.Object);
    
    
    protected function createJComponent(): JComponent {
        //var panel = new JPanel(new BorderLayout());

//        var panel = JPanel{
//            
//        }
//        mouseListener = java.awt.event.MouseAdapter{
//                //public function mouseEntered(e: java.awt.event.MouseEvent) {  } 
//                public function mouseReleased(e: java.awt.event.MouseEvent) {  
//                    //System.out.println("[tools] mouse released");
//                    if(dragFlag and dragValue != null){ 
//                        dragFlag = false;
//                        drop(dragValue);
//                    }
//                } 
//                //public function mouseClicked(e: java.awt.event.MouseEvent) {  } 
//            };
//        motionListener = java.awt.event.MouseMotionAdapter{
//                public function mouseDragged(e: java.awt.event.MouseEvent) {  
//                    dragFlag = true;
//                    //System.out.println("[tools] mouse released");
//                    //if(dragValue != null){ drop(dragValue);}
//                } 
//                //public function mouseClicked(e: java.awt.event.MouseEvent) {  } 
//            };
        
        var component = composeComponent().getJComponent();
        //return component;
        //panel.add (component, BorderLayout.CENTER);
        //component.addMouseListener(mouseListener);
        
        //return panel;
        return component;
    }

}