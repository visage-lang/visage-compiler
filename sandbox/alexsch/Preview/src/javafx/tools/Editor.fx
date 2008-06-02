
package javafx.tools;

import javafx.gui.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;

import java.lang.System;

public class Editor extends ToolComponent{
    
    //private attribute editorPane: JEditorPane;
    private attribute editorPane: JTextArea;
    private attribute updateComponentFlag: Boolean = false;
    
    public attribute line: Integer on replace{
        System.out.println("[editor] set line: {line}");
        editorPane.setCaretPosition(line);
    }
    
    override attribute drop = function(value: java.lang.Object) {
        System.out.println("[editor] Drop: {value}");
        if(value instanceof String){
            editorPane.<<insert>>(value as String,editorPane.getCaretPosition());
            
            updateComponentField(editorPane.getText());
        }
    }
    
    
    public attribute text: String on replace{
        if(not updateComponentFlag){
            editorPane.setText(text);
        }
    };

    public attribute editable: Boolean;

    //public attribute onKeyUp: function(keyEvent :KeyEvent);

//    protected function drop(value: java.lang.Object){
//        System.out.println("[editor] drop: {value}");
//    }


    private function updateComponentField(text: String){
            //System.out.println("[editor pane] updateComponentField!");
            updateComponentFlag = true;
            //text = editorPane.getText();
            this.text = text;
            updateComponentFlag = false;
    }

    protected function composeComponent(): Component {
        //editorPane = new JEditorPane();
        editorPane = new JTextArea();
        editorPane.addMouseListener(mouseListener);
        editorPane.addMouseMotionListener(motionListener);
        
        var keyListener = KeyAdapter{
            public function keyReleased(e: java.awt.event.KeyEvent) {
                updateComponentField(editorPane.getText());
                //if(onKeyUp <> null){onKeyUp(KeyEvent{}); }
            }
        };

        editorPane.addKeyListener( keyListener );
        var scrollPane = new JScrollPane(editorPane);
        //return scrollPane;
        //return editorPane;
        //return Component.fromJComponent(scrollPane);
        return Component.fromJComponent(editorPane);
    
    }

}