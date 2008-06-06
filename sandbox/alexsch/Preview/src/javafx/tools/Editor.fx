
package javafx.tools;

import javafx.gui.*;
import javafx.gui.Layout.*;

import javax.swing.*;
import java.awt.event.KeyAdapter;

import java.lang.System;

public class Editor extends ToolComponent{
    
    private attribute editorPane: JTextArea;
    private attribute updateComponentFlag: Boolean = false;
    
    public attribute line: Integer;
    
    
    public attribute caretPosition: Integer on replace{
        System.out.println("[editor] set caret position: {caretPosition}");
        editorPane.setCaretPosition(caretPosition);
        editorPane.requestFocus();
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
        //highlight ();
    };

    public attribute editable: Boolean;

    //public attribute onKeyUp: function(keyEvent :KeyEvent);


    public attribute diagnosticMessages: DiagnosticMessage[] on replace{
        shadowErrors(); 
        for( message in diagnosticMessages){
            highlightError(message);
        }
    }

    
    private function updateComponentField(text: String){
            updateComponentFlag = true;
            this.text = text;
            updateComponentFlag = false;
    }

    private static attribute KEY_WORDS:String[] =  ["import"];
//    
//    private function highlight () {
//        System.out.println("[editor] Highlight");
//        var hilite = editorPane.getHighlighter();
//        var painter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(java.awt.Color.BLUE);
//
//        //var content = editorPane.getText();
//        var content = text;
//        System.out.println("  content:\n {content}");
//        for(keyWord in KEY_WORDS){
//            System.out.println("  keyword: '{keyWord}'");
//            var index = content.indexOf(keyWord, 0);
//            if(0 <= index){
//                var end = index + keyWord.length();
//                System.out.println("  positions: {index} - {end}");
//                hilite.addHighlight(index, end, painter);
//            }
//        }
//    } 

    private function shadowErrors() {
        var highlight = editorPane.getHighlighter();
        highlight.removeAllHighlights();
    }
    
    private function highlightError(diagnosticMessage: DiagnosticMessage) {
        
        var highlight = editorPane.getHighlighter();
        highlight.removeAllHighlights();
        var painter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new java.awt.Color(254,134,126));
        var startPosition = diagnosticMessage.startPosition;
        var endPosition = diagnosticMessage.endPosition;
        System.out.println("\n[editor] Highlight: from {startPosition} to {endPosition}");
        if(startPosition == endPosition){
            startPosition -= 3;
            endPosition;
        }
        
        highlight.addHighlight(startPosition, endPosition, painter);
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
        
        return Component.fromJComponent(editorPane);
    
    }

}