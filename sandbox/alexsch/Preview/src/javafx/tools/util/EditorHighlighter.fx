package javafx.tools.util;


import javafx.tools.*;


import java.lang.System;
import javax.swing.text.JTextComponent;

public class EditorHighlighter{
    
    
    public attribute component: JTextComponent;

    public static attribute KEY_WORDS:String[] =  ["import"];

    
    public function clear(){
        var highlight = component.getHighlighter();
        highlight.removeAllHighlights();
    }
    
    public function highlightKeyWords(){
        //System.out.println("[highlight] keywords");
        var hilite = component.getHighlighter();
        //var painter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new java.awt.Color(188,207,246));
        var painter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new java.awt.Color(222,236,242));

        var content = component.getText();
        //System.out.println("  content:\n {content}");
        
        for(keyWord in KEY_WORDS){
            //System.out.println("  keyword: '{keyWord}'");
            
            
            var index = content.indexOf(keyWord, 0);
            
            while (0 <= index){
                var end = index + keyWord.length();
                //System.out.println("  positions: {index} - {end}");
                hilite.addHighlight(index, end, painter);
                index = content.indexOf(keyWord, end);
                
            }
            
            
        }
       
    }
    
    public function highlightErrors(diagnosticMessages: DiagnosticMessage[]){
        for(diagnosticMessage in diagnosticMessages){
            highlightError(diagnosticMessage);
        }
    }
    
    public function highlightError(diagnosticMessage: DiagnosticMessage){
        var highlight = component.getHighlighter();
        var painter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new java.awt.Color(254,134,126));
        var startPosition = diagnosticMessage.startPosition;
        var endPosition = diagnosticMessage.endPosition;
        //System.out.println("\n[highliter] error: [{startPosition} - {endPosition}]");
        if(startPosition == endPosition){
            startPosition -= 3;
            endPosition;
        }
        
        highlight.addHighlight(startPosition, endPosition, painter);
        
    }
    
}

