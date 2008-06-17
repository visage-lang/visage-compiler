/*
* MyEditorPane.fx
*
* Created on Apr 18, 2008, 5:48:25 PM
*/

package assortis.core.ui;


import javafx.gui.*;
import javafx.gui.component.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;

import java.lang.System;

public class MyEditorPane extends Component{
    
    private attribute editorPane: JEditorPane;
    private attribute updateComponentFlag: Boolean = false;
    
    
    public attribute text: String on replace{
        if(not updateComponentFlag){
            editorPane.setText(text);
        }
    };

    public attribute editable: Boolean = true on replace{
        editorPane.setEditable(editable);
    };

    public attribute onKeyUp: function(keyEvent :KeyEvent);


    private function updateComponentField(){
            //System.out.println("[editor pane] updateComponentField!");
            updateComponentFlag = true;
            text = editorPane.getText();
            updateComponentFlag = false;
    }

    protected function createJComponent(): JComponent {
        editorPane = new JEditorPane();
        editorPane.setEditable(editable);

        var keyListener = KeyAdapter{
            public function keyReleased(e: java.awt.event.KeyEvent) {
                updateComponentField();
                if(onKeyUp <> null){onKeyUp(KeyEvent{}); }
            }
        };

        editorPane.addKeyListener( keyListener );
        var scrollPane = new JScrollPane(editorPane);
        return scrollPane;
        //return editorPane;
    
    }

}
