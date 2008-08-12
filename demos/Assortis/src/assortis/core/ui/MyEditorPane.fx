/*
* MyEditorPane.fx
*
* Created on Apr 18, 2008, 5:48:25 PM
*/

package assortis.core.ui;

import javafx.input.*;
import javafx.ext.swing.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;

import java.lang.System;

public class MyEditorPane extends Component{
    
    var editorPane: JEditorPane;
    var updateComponentFlag: Boolean = false;
    
    
    public var text: String on replace{
        if(not updateComponentFlag){
            editorPane.setText(text);
        }
    };

    public var editable: Boolean = true on replace{
        editorPane.setEditable(editable);
    };

    public var onKeyUp: function(keyEvent :KeyEvent);


    function updateComponentField(){
            //System.out.println("[editor pane] updateComponentField!");
            updateComponentFlag = true;
            text = editorPane.getText();
            updateComponentFlag = false;
    }

    override function createJComponent(): JComponent {
        editorPane = new JEditorPane();
        editorPane.setEditable(editable);

        var keyListener = KeyAdapter{
            override function keyReleased(e: java.awt.event.KeyEvent) {
                updateComponentField();
                if(onKeyUp != null){onKeyUp(KeyEvent{}); }
            }
        };

        editorPane.addKeyListener( keyListener );
        var scrollPane = new JScrollPane(editorPane);
        return scrollPane;
        //return editorPane;
    
    }

}
