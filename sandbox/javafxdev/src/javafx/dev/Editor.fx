package javafx.dev;

/**
 * @author Alexandr Scherbatiy, alexsch@dev.java.net
 */

import javafx.gui.*;
import javafx.gui.Layout.*;

import javax.swing.*;
import java.awt.event.KeyAdapter;

import javafx.dev.util.*;

import java.lang.System;

public class Editor extends DevComponent{
    
    private attribute editorPane: JTextArea;
    private attribute updateComponentFlag: Boolean = false;
    
    public attribute line: Integer;
    
    public attribute caretPosition: Integer on replace{
        //System.out.println("[editor] set caret position: {caretPosition}");
        editorPane.setCaretPosition(caretPosition);
        editorPane.requestFocus();
    }
    override attribute dropEnable = true;
    override attribute drop = function(value: java.lang.Object) {
        //System.out.println("[editor] Drop: {value}");
        if(value instanceof String){
            editorPane.<<insert>>(value as String,editorPane.getCaretPosition());
            
            updateComponentField(editorPane.getText());
        }
    }
    
    
    public attribute text: String on replace{
        if(not updateComponentFlag){
            editorPane.setText(text);
        }
        highlighter.clear();
        highlighter.highlightKeyWords();
    };

    public attribute editable: Boolean;

    //public attribute onKeyUp: function(keyEvent :KeyEvent);

    private attribute highlighter: EditorHighlighter;// = EditorHighlighter{ component: bind editorPane };
    
    public attribute ErrorMessages: ErrorMessage[] on replace{
          highlighter.highlightErrors(ErrorMessages);
    }

    
    private function updateComponentField(text: String){
            updateComponentFlag = true;
            this.text = text;
            updateComponentFlag = false;
    }

    
    protected function composeComponent(): Component {
        //editorPane = new JEditorPane();
        editorPane = new JTextArea();
        highlighter = EditorHighlighter{ component: editorPane };
        
        editorPane.addMouseListener(mouseListener);
        editorPane.addMouseMotionListener(motionListener);
        
        var keyListener = KeyAdapter{
            public function keyReleased(e: java.awt.event.KeyEvent) {
                updateComponentField(editorPane.getText());
                //if(onKeyUp != null){onKeyUp(KeyEvent{}); }
            }
        };

        editorPane.addKeyListener( keyListener );
        
        return Component.fromJComponent(editorPane);
    
    }

}